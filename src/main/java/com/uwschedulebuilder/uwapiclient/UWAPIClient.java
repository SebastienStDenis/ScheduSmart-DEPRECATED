package com.uwschedulebuilder.uwapiclient;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.ListIterator;
import java.util.Properties;
import java.util.Scanner;

import com.google.gson.Gson;
import com.uwschedulebuilder.builder.Block;
import com.uwschedulebuilder.builder.Component;
import com.uwschedulebuilder.builder.Section;
import com.uwschedulebuilder.builder.Term;

// UWAPIClient is used to access course information from the UW API
public class UWAPIClient {
	
	private static String baseURL;
	private static String apiKey;
	
	static{
		try(FileInputStream input = new FileInputStream("src/main/resources/private/config.properties")) {
			Properties prop = new Properties();
			prop.load(input);
			
			baseURL = prop.getProperty("uwbaseurl");
			apiKey = prop.getProperty("uwapikey");
		} catch (IOException exc) {
			exc.printStackTrace();
		}		
	}
	

	// UWAPI term course data is deserialized into instances of this class
	private class JSONTermCourses {
		private class JSONCourseName {
			String subject;
			String catalog_number;
			String title;
		}
		
		JSONCourseName[] data;
	}
	
	// UWAPI json course data is deserialized into instances of this class
	private class JSONCourse {		
		private class JSONComponent {		
			private class JSONBlock {
				private class JSONDate {
					String start_time;
					String end_time;
					String start_date;
					String end_date;
					String weekdays;
					boolean is_cancelled;
					boolean is_closed;
				}
				private class JSONLocation {
					String building;
					String room;				
				}
				
				JSONDate date;
				JSONLocation location;
				String[] instructors;
			}
			
			String subject;
			String catalog_number;
			int class_number;
			String section;
			int associated_class;
			int enrollment_capacity;
			int enrollment_total;
			JSONBlock[] classes;
		}

		JSONComponent[] data;
	}
	
	// getJSON returns JSON data from the UW API for the provided URL
	private static String getJSON(String url) throws MalformedURLException, IOException {
		URLConnection connection = new URL(url).openConnection();
		
		String charset = java.nio.charset.StandardCharsets.UTF_8.name();
		connection.setRequestProperty("Accept-Charset", charset);
		
		InputStream response = connection.getInputStream();
		
		try (Scanner scanner = new Scanner(response)) {
		    String responseBody = scanner.useDelimiter("\\A").next();
		    return responseBody;
		}
	}
		
	// getTerm returns an array of course names based on UW API data
	//    for the provided 4-digit term code	
	private static ArrayList<String> getTerm(String term) throws UWAPIException {
		JSONTermCourses obj;
		try {
			String url = baseURL + 
					"/" + term +
					"/courses.json" +
					"?key=" + apiKey;
			
			String data = getJSON(url);
			
			Gson gson = new Gson();			
			obj = gson.fromJson(data, JSONTermCourses.class);
			
			if (obj.data.length == 0) {
				throw new UWAPIException();
			}
		} catch (Throwable e) {
			throw new UWAPIException("cannot access course information for term " + term);
		}
		
		ArrayList<String> courses = new ArrayList<String>();
		for (int i = 0; i < obj.data.length; ++i) {
			courses.add(obj.data[i].subject + " " + obj.data[i].catalog_number + " - " + obj.data[i].title);
		}
		
		return courses;
	}
	
	// getTerms returns term information from the UWaterloo API for each term with course
	//    info available one the API.  The information is returned as an array of Term objects
	public static Term[] getTerms() throws UWAPIException {
		String[] termCodes = {"1171"};
		ArrayList<Term> termCourses = new ArrayList<Term>();
		
		for (int i = 0; i < termCodes.length; ++i) {
			String code = termCodes[i];
			
			String name = code.substring(1, 3);
			
			char seasonNum = code.charAt(3);
			if (seasonNum == '1') {
				name = "W" + name;
			} else if (seasonNum == '5') {
				name = "S" + name;
			} else {
				name = "F" + name;
			}
			
			try {
				ArrayList<String> courses = UWAPIClient.getTerm(code);
				termCourses.add(new Term(name, code, courses));
			} catch (UWAPIException e) {
				continue;
			}			
		}
		
		if (termCourses.size() == 0) {
			throw new UWAPIException("No term information found");
		} else {
			Term[] termCoursesArr = new Term[termCourses.size()];
			for (int i = 0; i < termCoursesArr.length; ++i) {
				termCoursesArr[i] = termCourses.get(i);
			}
			return termCoursesArr;
		}
	}
	
	
	
	// getSections returns a list of Sections based on UW API data for the provided className and term.
	// className must be of format "ABC123", term a 4-digit string
	public static ArrayList<Section> getSections(String className, String term) throws UWAPIException {
		JSONCourse obj;		
		try {
			int nameLen = className.length();
			int pos = 0;
			while (pos < nameLen && !Character.isDigit(className.charAt(pos))) {
				++pos;
			}
			
			String subject = className.substring(0, pos);
			String catalogNum = className.substring(pos);
			
			String url = baseURL + 
					"/" + term + 
					"/" + subject + 
					"/" + catalogNum + 
					"/schedule.json" + 
					"?key=" + apiKey;
			
			String data = getJSON(url);
			
			Gson gson = new Gson();			
			obj = gson.fromJson(data, JSONCourse.class);
			
			if (obj.data.length == 0) {
				throw new UWAPIException();
			}
		} catch (Throwable e) {
			throw new UWAPIException("cannot access course information for course " + className + ", term " + term);
		}
		
		ArrayList<Section> sections = new ArrayList<Section>();
		
		NextComponent:
		for (int pos = 0; pos < obj.data.length; ++pos) { // cycle through each component ("LEC 001", "LEC 002", "TUT 101", ...)
			JSONCourse.JSONComponent curr = obj.data[pos];
			
			String name = curr.subject + " " + curr.catalog_number;			
			boolean closed = (curr.enrollment_total >= curr.enrollment_capacity);
			
			Component comp = new Component(name, curr.class_number, curr.section, curr.associated_class, closed);
			
			String compType = (curr.section.split(" "))[0];
			
			for (int blockPos = 0; blockPos < curr.classes.length; ++blockPos) { // cycle through each block of the component
				JSONCourse.JSONComponent.JSONBlock currJBlock =  curr.classes[blockPos];
				
				if (currJBlock.date.start_time == null || currJBlock.date.end_time == null || currJBlock.date.weekdays == null) {
					// ignore blocks with no specified time or weekdays
					continue NextComponent;
				}
				
				if (currJBlock.date.is_cancelled) {
					continue NextComponent;
				}
				
				if (currJBlock.date.is_closed) {
					comp.setClosed(true);
				}
				
				String location;
				if (currJBlock.location.building == null || currJBlock.location.room == null) {
					location = "TBA";
				} else {
					location = currJBlock.location.building + " " + currJBlock.location.room;
				}
								
				Block block = null;
				
				try {
					block = new Block(currJBlock.date.start_time, currJBlock.date.end_time, currJBlock.date.weekdays,
						location, currJBlock.date.start_date, currJBlock.date.end_date, currJBlock.instructors, compType);
				} catch (IllegalArgumentException e) {
					continue NextComponent;
				}
				
				comp.addBlock(block);
			}
			
			if (comp.blocksSize() == 0) {
				continue NextComponent;
			}
			
			
			
			ListIterator<Section> secIt = sections.listIterator();
			boolean added = false;
			
			// add the component to the correct Section in sections
			while (secIt.hasNext()) {
				Section sec = secIt.next();
				if (sec.getType().equals(compType)) {
					sec.addComponent(comp);
					added = true;
					break;
				}				
			}
			
			if (!added) {
				Section sec = new Section(compType);
				sec.addComponent(comp);
				sections.add(sec);
			}			
		}
				
		return sections;		
	}
}

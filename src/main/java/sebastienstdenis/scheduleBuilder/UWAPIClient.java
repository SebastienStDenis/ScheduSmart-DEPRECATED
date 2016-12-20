package sebastienstdenis.scheduleBuilder;

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

// UWAPIClient is used to returns arrays of Section objects
//    representing course data of the UW API
class UWAPIClient {
	
	private static String baseURL;
	private static String apiKey;
	
	static{
		try(FileInputStream input = new FileInputStream("resources/config.properties")) {
			Properties prop = new Properties();
			prop.load(input);
			
			baseURL = prop.getProperty("uwbaseurl");
			apiKey = prop.getProperty("uwapikey");
		} catch (IOException exc) {
			exc.printStackTrace();
		}		
	}
	
	// UWAPI json data is deserialized into these classes
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
	
	// getJSON returns a JSON data from the UW API for the provided className and term.
	// className must be of format "ABC 123", term a 4-digit string
	private static String getJSON(String className, String term) throws MalformedURLException, IOException {
		String[] classNameSplit = className.toUpperCase().split(" ");
		
		String url = baseURL + term + 
				"/" + classNameSplit[0] + 
				"/" + classNameSplit[1] + 
				"/schedule.json" + 
				"?key=" + apiKey;
				
		
		URLConnection connection = new URL(url).openConnection();
		
		String charset = java.nio.charset.StandardCharsets.UTF_8.name();
		connection.setRequestProperty("Accept-Charset", charset);
		
		InputStream response = connection.getInputStream();
		
		try (Scanner scanner = new Scanner(response)) {
		    String responseBody = scanner.useDelimiter("\\A").next();
		    return responseBody;
		}
	}
	
	
	// getSections returns a list of Sections based on UW API data for the provided className and term.
	// className must be of format "ABC 123", term a 4-digit string
	static ArrayList<Section> getSections(String className, String term) throws UWAPIException {
		JSONCourse obj;		
		try {
			String data = getJSON(className, term);
			
			Gson gson = new Gson();			
			obj = gson.fromJson(data, JSONCourse.class);
			
			if (obj.data.length == 0) {
				throw new UWAPIException();
			}
		} catch (Throwable e) {
			throw new UWAPIException("cannot access course information for " + className + ", " + term);
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
				
				String instructors = "";
				int instrLen = currJBlock.instructors.length;
				if (instrLen >= 1) {
					String[] instrSplit = currJBlock.instructors[0].split(",");
					
					if (instrSplit.length == 1) {
						instructors = instrSplit[0];
					} else if (instrSplit.length > 1) {
						instructors = instrSplit[1] + " " + instrSplit[0];
					}
					
					if (instrLen > 1) {
						instructors += String.format(" (+%d)", instrLen - 1);
					}
				}
				
				Block block = null;
				
				try {
					block = new Block(currJBlock.date.start_time, currJBlock.date.end_time, currJBlock.date.weekdays,
						location, currJBlock.date.start_date, currJBlock.date.end_date, instructors, compType);
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

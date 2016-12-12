package sebastienstdenis.scheduleBuilder;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ListIterator;
import java.util.HashMap;
import java.util.Properties;

public class Builder {
	private ArrayList<Section> sections;
	private Calendar cal;
	private ArrayList<Schedule> schedules;
	private HashMap<String, Integer> assocNums;
	private String[] ignoreSecs; // must be sorted in ascending order
	private boolean omitClosed;
	
	private String baseURL;
	private String apiKey;
	
	public Builder() {
		resetBuilder();
		
	}
	
	void resetBuilder() {
		sections = new ArrayList<Section>();
		cal = new Calendar();
		schedules = new ArrayList<Schedule>();
		assocNums = new HashMap<String, Integer>();
		ignoreSecs = null;
		omitClosed = false;
	}
	
	void nextSection(ListIterator<Section> secIt) {
		if (secIt.hasNext()) {
			Section sec = secIt.next();
			
			int foundPos = Arrays.binarySearch(ignoreSecs, sec.getClassType());
			if (foundPos >= 0 && foundPos < ignoreSecs.length && ignoreSecs[foundPos].equals(sec.getClassType())) {
				nextSection(sections.listIterator(secIt.nextIndex()));
				return;
			}
			
			int compLen = sec.componentsLen();
			for (int pos = 0; pos < compLen; ++pos) {
				Component comp = sec.getComponent(pos);
				
				if (this.omitClosed && comp.getClosed()) {
					continue;
				}
				
				String course = comp.getName();
				int assocClass = comp.getAssocClass();
				if (assocNums.containsKey(course) && assocClass != 99 && assocClass != assocNums.get(course)) {
						continue;
				}				
				
				if (cal.addComponent(comp)) {
					boolean addedAssoc = false;
					if (!assocNums.containsKey(course) && assocClass != 99) {
						assocNums.put(course, assocClass);
						addedAssoc = true;
					}
					
					nextSection(sections.listIterator(secIt.nextIndex()));
					cal.removeComponent(comp);
					
					if (addedAssoc) {
						assocNums.remove(course);
					}
				}
			}			
		} else {
			schedules.add(cal.makeSchedule());
		}
	}
	
	public ArrayList<Schedule> getSchedules(String[] classes, String term, String[] ignoreSecs, boolean omitClosed) {
		resetBuilder();
		this.ignoreSecs = ignoreSecs; 
		Arrays.sort(ignoreSecs);
		this.omitClosed = omitClosed;
		
		for (int pos = 0; pos < classes.length; ++pos) {
			try {
				sections.addAll(UWAPIClient.getSections(classes[pos], term, baseURL, apiKey));
			} catch (UWAPIException e) {
				return new ArrayList<Schedule>();
			}
		}
		
		sections.sort((sec1, sec2) -> Integer.compare(sec1.componentsLen(), sec2.componentsLen()));
		
		nextSection(sections.listIterator());
		
		ArrayList<Schedule> result = schedules;
		resetBuilder();		
		
		return result;
	}
	
	void getProperties() throws IOException {		
		FileInputStream input = new FileInputStream("resources/config.properties");
		
		Properties prop = new Properties();
		prop.load(input);
		
		this.baseURL = prop.getProperty("uwbaseurl");
		this.apiKey = prop.getProperty("uwapikey");
		
		input.close();
	}
	
	public static void main(String[] args) {
		String[] classes = {"CHEM 120", "CHEM 120L", "MATH 114", "MATH 127", "PHYS 10", "PHYS 121", "PHYS 131L"};
		String term = "1169";
		String[] ignoreSecs = {"TUT", "LAB", "SEM"};
		
		Builder builder = new Builder();		

		try {
			builder.getProperties();
		} catch(IOException e) {
			System.out.println("could not read config.properties");
			return;
		}
		
		ArrayList<Schedule> scheds = builder.getSchedules(classes, term, ignoreSecs, false);
		
		int schedsLen = scheds.size();
		for (int pos = 0; pos < schedsLen; ++pos) {
			System.out.println(scheds.get(pos));
		}
		System.out.println(scheds.size());
		
	}
}

package com.schedusmart.schedulebuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.ListIterator;

import com.schedusmart.uwapiclient.UWAPIClient;
import com.schedusmart.uwapiclient.UWAPIException;

import java.util.HashMap;

// Builder is used to compute and return a list of valid 
//    Schedules based on the courses provided to it
public class Builder {
	private ArrayList<Section> allSections;
	private Calendar cal;
	private HashMap<String, Integer> assocNums; // the keys are course names, the values are the corresponding assoc_numbers being used for each
	private HashMap<String, String> rel1Codes;
	private HashMap<String, String> rel2Codes;
	
	private ArrayList<Schedule> validSchedules;

	private String[] ignoredSections; // Section types to ignore (ex: "TUT")
	private boolean omitClosed; // don't include schedules that are closed/full
	
	public Builder() {
		resetBuilder();
	}
	
	// resetBuilder resets the fields of the Builder object to its post-initialization state
	private void resetBuilder() {
		allSections = new ArrayList<Section>();
		cal = new Calendar();
		assocNums = new HashMap<String, Integer>();
		rel1Codes = new HashMap<String, String>();
		rel2Codes = new HashMap<String, String>();
		
		validSchedules = new ArrayList<Schedule>();
		
		ignoredSections = null;
		omitClosed = false;
	}
	
	// addValidSchedule adds schedule to the validSchedules field at the
	//    correct position (sorted from highest score to lowest)
	private void addValidSchedule(Schedule schedule) {
		if (schedule.getSize() == 0) {
			return;
		}
		
		ListIterator<Schedule> validIt = validSchedules.listIterator();
		
		double scheduleScore = schedule.getScore();
		while (validIt.hasNext()) {
			if (scheduleScore >= validIt.next().getScore()) {
				validIt.previous();
				validIt.add(schedule);
				return;
			}
		}
		validIt.add(schedule);
	}
	
	// nextSection adds all valid schedules to validSchedules starting at the Section
	//    pointed to by secIt (calls itself recursively)
	// ignoredSections must be sorted (done in getSchedules())
	private void nextSection(ListIterator<Section> secIt) {
		if (secIt.hasNext()) {
			Section sec = secIt.next();
			
			// skip current section if the type is in ignoredSections
			if (ignoredSections != null && ignoredSections.length > 0) {
				int foundPos = Arrays.binarySearch(ignoredSections, sec.getType());
				if (foundPos >= 0 && foundPos < ignoredSections.length && ignoredSections[foundPos].equals(sec.getType())) {
					nextSection(allSections.listIterator(secIt.nextIndex()));
					return;
				}
			}			
			
			int compLen = sec.componentsSize();
			for (int pos = 0; pos < compLen; ++pos) {
				Component comp = sec.getComponent(pos);
				
				if (this.omitClosed && comp.getClosed()) {
					continue;
				}
				
				String course = comp.getName();
				int assocClass = comp.getAssocClass();
				// if there is a value in assocNums for this class, comp's assocClass must match it or be 99
				if (assocNums.containsKey(course) && assocClass != 99 && assocClass != assocNums.get(course)) {
						continue;
				}
				
				// if section 0XX (eg LEC 001, TUT 012 ...) has a rel1 number, the corresponding 1YY section must have that catalog number
				// if section 0XX has a rel2 number, the corresponding 2YY section must have that catalog number
				String catNum = comp.getSectionName().split(" ")[1];
				boolean rel1Added = false;
				boolean rel2Added = false;
				
				if (catNum.charAt(0) == '0') {
					String rel1 = comp.getRel1();					
					if (rel1 != null) {
						if (!rel1.equals(rel1Codes.getOrDefault(course, rel1))) {
							continue;
						}
						if (!rel1.equals("99") && !rel1Codes.containsKey(course)) {
							rel1Codes.put(course, rel1);
							rel1Added = true;
						}
					}
					
					String rel2 = comp.getRel2();					
					if (rel2 != null) {
						if (!rel2.equals(rel2Codes.getOrDefault(course, rel2))) {
							continue;
						}
						if (!rel2.equals("99") && !rel2Codes.containsKey(course)) {
							rel2Codes.put(course, rel2);
							rel2Added = true;
						}
					}					
				} else if (catNum.charAt(0) == '1') {
					if (!catNum.equals(rel1Codes.getOrDefault(course, catNum))) {
						continue;
					}
					
					if (!rel1Codes.containsKey(course)) {
						rel1Codes.put(course, catNum);
						rel1Added = true;
					}					
				} else if (catNum.charAt(0) == '2') {
					if (!catNum.equals(rel2Codes.getOrDefault(course, catNum))) {
						continue;
					}
					
					if (!rel2Codes.containsKey(course)) {
						rel2Codes.put(course, catNum);
						rel2Added = true;
					}	
				}
				
				if (cal.addComponent(comp)) {
					boolean addedAssoc = false;
					if (!assocNums.containsKey(course) && assocClass != 99) {
						assocNums.put(course, assocClass);
						addedAssoc = true;
					}
					
					// this will return after eventually trying all valid possibilities.
					//    Afterwards, we remove the just-added component and try again
					//    with the next.
					nextSection(allSections.listIterator(secIt.nextIndex()));
					
					cal.removeComponent(comp); // backtracking
					if (addedAssoc) {
						assocNums.remove(course);
					}
					if (rel1Added) {
						rel1Codes.remove(course);
					}
					if (rel2Added) {
						rel2Codes.remove(course);
					}
				}
			}			
		} else { // no more Sections, add calendar to validSchedules
			addValidSchedule(cal.makeSchedule());
		}
	}
	
	// getSchedules returns a list of all possible schedules based on classes, term, ignoredSections, and omitClosed
	public ArrayList<Schedule> getSchedules(String[] classes, String term, String[] ignoredSections, 
			boolean omitClosed, ScorePreferences scorePreferences) throws UWAPIException {
		resetBuilder();
		
		cal.setScorePreferences(scorePreferences);
		
		if (ignoredSections != null) {
			this.ignoredSections = ignoredSections; 
			Arrays.sort(ignoredSections);
		}		
		
		this.omitClosed = omitClosed;
		
		for (int pos = 0; pos < classes.length; ++pos) {
			allSections.addAll(UWAPIClient.getSections(classes[pos], term));
		}
		
		// sort sections in allSections from least amount of components to most
		allSections.sort((sec1, sec2) -> Integer.compare(sec1.componentsSize(), sec2.componentsSize()));
		
		nextSection(allSections.listIterator());
		
		return validSchedules;
	}
}

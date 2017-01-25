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

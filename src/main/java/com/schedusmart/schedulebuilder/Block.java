package com.schedusmart.schedulebuilder;

import java.util.ArrayList;
import java.util.Arrays;

// Block represents a set of class times for a certain component
public class Block {	
	private String startTime;
	private String endTime;
	private String startDate;
	private String endDate;
	private String[] days; // valid entries are "M", "T", "W", "Th", "F", "S", "Su"
		
	private String location;
	
	private String[] instructors;
	
	private String sectionType; // "LEC", "TUT", ...
		
	public Block(String startTime, String endTime, String days, String location,
			String startDate,	String endDate, String[] instructors, String sectionType)
					throws IllegalArgumentException {
		this.startTime = startTime;
		this.endTime = endTime;
		this.days = parseDays(days);		
		this.location = location;
		this.startDate = startDate;
		this.endDate = endDate;
		this.instructors = instructors;
		this.sectionType = sectionType;
		
	}
	
	// parseDays splits a string of valid days (see ln. 12) into an array of strings
	private static String[] parseDays(String days) throws IllegalArgumentException {
		ArrayList<String> daysList = new ArrayList<String>();
		
		int pos = 0;
		int daysLen = days.length();
		String current = "";
		while (pos < daysLen) {
			current += days.charAt(pos);
			++pos;
			
			if (pos == daysLen || Character.isUpperCase(days.charAt(pos))) {
				if (Arrays.asList(Calendar.DAYS).contains(current)) {
					daysList.add(current);
				}
				
				current = "";
			}
		}
		
		if (daysList.size() <= 0) {
			throw new IllegalArgumentException("Provided days cannot be parsed: " + days);
		}
		
		return daysList.toArray(new String[daysList.size()]);
	}
	
	// hasDates returns true if the block has specified dates
	public boolean hasDates() {
		return startDate != null && endDate != null;
	}
	
	// getStartTime returns the startTime field
	public String getStartTime() {
		return startTime;
	}
	
	// getEndTime returns the endTime field
	public String getEndTime() {
		return endTime;
	}
		
	// getStartDate returns the startDate field
	public String getStartDate() {
		return startDate;
	}
	
	// getEndDate returns the endDate field
	public String getEndDate() {
		return endDate;
	}
	
	// getDays returns the days field
	public String[] getDays() {
		return days;
	}
	
	// getSectionType returns the sectionType field
	public String getSectionType() {
		return sectionType;
	}
	
	public String toString() {
		return String.format("Block: %s to %s, %s, %s, %s to %s, %s", startTime, endTime, Arrays.toString(days), location, startDate, endDate, Arrays.toString(instructors));
	}
}

package sebastienstdenis.scheduleBuilder;

import java.util.ArrayList;
import java.util.Arrays;

// Block represents a set of class times for a certain component
class Block {	
	private String startTime;
	private String endTime;
	private String startDate;
	private String endDate;
	private String[] days; // valid entries are "M", "T", "W", "Th", "F"
		
	private String location;
	
	private String sectionType;
		
	Block(String startTime, String endTime, String days, String location, String startDate, String endDate, String sectionType) {
		this.startTime = startTime;
		this.endTime = endTime;
		this.days = parseDays(days);
		this.location = location;
		this.startDate = startDate;
		this.endDate = endDate;
		this.sectionType = sectionType;
		
	}
	
	// parseDays splits a string of valid days (see ln. 12) into an array of strings
	private static String[] parseDays(String days) {
		ArrayList<String> daysList = new ArrayList<String>();
		
		int pos = 0;
		int daysLen = days.length();
		String current = "";
		while (pos < daysLen) {
			current += days.charAt(pos);
			++pos;
			if (pos == daysLen || Character.isUpperCase(days.charAt(pos))) {
				daysList.add(current);
				current = "";
			}
		}
		
		return daysList.toArray(new String[daysList.size()]);
	}
	
	// hasDates returns true if the block has specified dates
	boolean hasDates() {
		return startDate != null && endDate != null;
	}
	
	// getStartTime returns the startTime field
	String getStartTime() {
		return startTime;
	}
	
	// getEndTime returns the endTime field
	String getEndTime() {
		return endTime;
	}
		
	// getStartDate returns the startDate field
	String getStartDate() {
		return startDate;
	}
	
	// getEndDate returns the endDate field
	String getEndDate() {
		return endDate;
	}
	
	// getDays returns the days field
	String[] getDays() {
		return days;
	}
	
	// getSectionType returns the sectionType field
	String getSectionType() {
		return sectionType;
	}
	
	public String toString() {
		return String.format("\n\t\t\tBlock: startTime - %s, endTime - %s, days - %s, location - %s, startDate - %s, endDate - %s", startTime, endTime, Arrays.toString(days), location, startDate, endDate);
	}
}

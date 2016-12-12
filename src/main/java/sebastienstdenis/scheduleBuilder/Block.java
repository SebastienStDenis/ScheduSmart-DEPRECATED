package sebastienstdenis.scheduleBuilder;

import java.util.ArrayList;
import java.util.Arrays;

class Block {	
	private String startTime;
	private String endTime;
	private String startDate;
	private String endDate;
	private String[] days;
		
	String location;
		
	Block(String startTime, String endTime, String days, String location, String startDate, String endDate) {
		this.startTime = startTime;
		this.endTime = endTime;
		this.days = parseDays(days);
		this.location = location;
		this.startDate = startDate;
		this.endDate = endDate;
		
	}
	
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
	
	boolean hasDates() {
		return startDate != null && endDate != null;
	}
	
	String getStartTime() {
		return startTime;
	}
	
	String getEndTime() {
		return endTime;
	}
	
	String[] getDays() {
		return days;
	}
	
	String getStartDate() {
		return startDate;
	}
	
	String getEndDate() {
		return endDate;
	}
	
	public String toString() {
		return String.format("\n\t\t\tBlock: startTime - %s, endTime - %s, days - %s, location - %s, startDate - %s, endDate - %s", startTime, endTime, Arrays.toString(days), location, startDate, endDate);
	}
}

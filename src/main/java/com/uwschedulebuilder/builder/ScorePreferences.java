package com.uwschedulebuilder.builder;

// ScorePreferences stores the preferences to use
//    when calculating the score of a schedule
public class ScorePreferences {
	int classTimes; // 0 - no preference, 1 - prefer early classes, 2 - prefer late classes
	int dayPreference; // 0 - no preference, 1 - prefer light days, 2- prefer free days	
	
	public ScorePreferences() {
		this.classTimes = 0;
		this.dayPreference = 0;
	}
	
	// classTimes and dayPreference must be integers from 0 to 2 (incl.)
	public ScorePreferences(int classTimes, int dayPreference) {
		this.classTimes = classTimes;
		this.dayPreference = dayPreference;
	}
	
	// getClassTimes returns the classTimes field
	public int getClassTimes() {
		return classTimes;
	}
	
	// getDayPreference returns the dayPreference field
	public int getDayPreference() {
		return dayPreference;
	}
}

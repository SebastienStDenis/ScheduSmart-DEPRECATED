package sebastienstdenis.scheduleBuilder;

// ScorePreferences stores the preferences to use
//    when calculating the score of a schedule
public class ScorePreferences {
	int classTimes; // 0 - no preference, 1 - avoid early classes, 2 - avoid late classes
	int dayPreference; // 0 - no preference, 1 - prefer light days, 2- prefer free days	
	
	// classTimes and dayPreference must be integers from 0 to 2 (incl.)
	ScorePreferences(int classTimes, int dayPreference) {
		this.classTimes = classTimes;
		this.dayPreference = dayPreference;
	}
	
	// getClassTimes returns the classTimes field
	int getClassTimes() {
		return classTimes;
	}
	
	// getDayPreference returns the dayPreference field
	int getDayPreference() {
		return dayPreference;
	}
}

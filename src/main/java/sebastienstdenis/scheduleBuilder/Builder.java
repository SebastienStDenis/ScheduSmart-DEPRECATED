package sebastienstdenis.scheduleBuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.ListIterator;
import java.util.HashMap;

// Builder is used to compute and return a list of valid 
//    Schedules based on the courses provided to it 
public class Builder {
	private ArrayList<Section> allSections;
	private Calendar cal;
	private HashMap<String, Integer> assocNums; // map of Course names to it's current assoc_number being used
	
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
	
	private void addValidSchedule(Schedule schedule) {
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
	//    pointed to by secIt and ahead (calls itself recursively and with recursion)
	private void nextSection(ListIterator<Section> secIt) {
		if (secIt.hasNext()) {
			Section sec = secIt.next();
			
			int foundPos = Arrays.binarySearch(ignoredSections, sec.getType());
			if (foundPos >= 0 && foundPos < ignoredSections.length && ignoredSections[foundPos].equals(sec.getType())) {
				nextSection(allSections.listIterator(secIt.nextIndex()));
				return;
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
					
					// this will return after eventually reaching the end of allSections or
					//    having no more possibilities.  Afterwards, we remove the
					//    just-added component and try again with the next.
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
	public ArrayList<Schedule> getSchedules(String[] classes, String term, String[] ignoredSections, boolean omitClosed, ScorePreferences scorePreferences) throws UWAPIException {
		resetBuilder();
		
		cal.setScorePreferences(scorePreferences);
		this.ignoredSections = ignoredSections; 
		Arrays.sort(ignoredSections);
		this.omitClosed = omitClosed;
		
		for (int pos = 0; pos < classes.length; ++pos) {
			allSections.addAll(UWAPIClient.getSections(classes[pos], term));
		}
		
		allSections.sort((sec1, sec2) -> Integer.compare(sec1.componentsSize(), sec2.componentsSize()));
		
		nextSection(allSections.listIterator());
		
		return validSchedules;
	}
	
	
	// REMOVE THIS
	public static void main(String[] args) throws UWAPIException {
		//String[] classes = {"CHEM 120", "CHEM 120L", "MATH 114", "MATH 127", "PHYS 10", "PHYS 121", "PHYS 131L"};
		String[] classes = {"CS 240", "CS 241", "CS 251", "STV 205"};
		
		String term = "1171";
		String[] ignoreSecs = {"SEM", "TUT"};
		ScorePreferences prefs = new ScorePreferences(1, 1);
		
		Builder builder = new Builder();
		ArrayList<Schedule> scheds = builder.getSchedules(classes, term, ignoreSecs, false, prefs);
		
		int schedsLen = scheds.size();
		

		for (int pos = 0; pos < schedsLen; ++pos) {
			System.out.println(scheds.get(pos));
		}
		System.out.println(schedsLen);
		
	}
}

package sebastienstdenis.scheduleBuilder;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

// This isn't really a JUnit test, more of a manual helper for using the backend
public class BuilderTest {

	@Test
	public void testGetSchedules() {
		String[] classes = {"CS 240", "CS 241", "CS 251", "STV 205"};
		
		String term = "1171";
		String[] ignoreSecs = {"SEM", "TUT"};
		ScorePreferences prefs = new ScorePreferences(1, 1);
		
		Builder builder = new Builder();
		ArrayList<Schedule> scheds = null;
		try {
			scheds = builder.getSchedules(classes, term, ignoreSecs, false, prefs);
		} catch (UWAPIException e) {
			fail(e.toString());
		}
		
		int schedsLen = scheds.size();
		

		for (int pos = 0; pos < schedsLen; ++pos) {
			System.out.println(scheds.get(pos));
		}
		System.out.println(schedsLen);
	}

}

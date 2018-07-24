package com.schedusmart.schedulebuilder;

import com.schedusmart.uwapiclient.UWAPIException;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.fail;

// This isn't really a JUnit test, more of a manual helper for using the backend
public class BuilderTest {

	@Test
	public void testGetSchedules() {
		String[] classes = {"CS241"};
		
		String term = "1171";
		String[] ignoreSecs = {"TST"};
		ScorePreferences prefs = new ScorePreferences(1, 1);
		
		Builder builder = new Builder();
		ArrayList<Schedule> scheds = null;
		try {
			scheds = builder.getSchedules(classes, term, ignoreSecs, true, prefs);
		} catch (UWAPIException e) {
			fail(e.toString());
		}
		
		int schedsLen = scheds.size();


		//for (int pos = 0; pos < schedsLen; ++pos) {
		//	System.out.println(scheds.get(pos));
		//}
		//System.out.println(schedsLen);
	}

}

package com.uwschedulebuilder.builder;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

import com.uwschedulebuilder.builder.Builder;
import com.uwschedulebuilder.builder.Schedule;
import com.uwschedulebuilder.builder.ScorePreferences;
import com.uwschedulebuilder.uwapiclient.UWAPIException;

// This isn't really a JUnit test, more of a manual helper for using the backend
public class BuilderTest {

	@Test
	public void testGetSchedules() {
		String[] classes = {"CS240", "CS241", "CS251", "STV205", "ME482"};
		
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

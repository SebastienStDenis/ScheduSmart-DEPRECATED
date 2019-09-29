package com.schedusmart.uwapiclient;

import com.schedusmart.schedulebuilder.Section;
import com.schedusmart.schedulebuilder.Term;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.fail;

// These aren't really JUnit tests, more like helpers for manually testing the Client
public class UWAPIClientTest {

	@Test
	public void testGetSections() {
		String className = "CS240";
		String term = "1171";
		
		ArrayList<Section> sections = null;
		try {
			sections = UWAPIClient.getSections(className, term);
		} catch (UWAPIException e) {
			fail(e.toString());
		}
		
		//System.out.println(sections);
	}	
	
	@Test
	public void testGetTermCourses() {
		try {
			Term[] terms = UWAPIClient.getTerms();
			//System.out.println(Arrays.toString(terms));
		} catch (UWAPIException e) {
			fail(e.toString());
		}
		
	}

}

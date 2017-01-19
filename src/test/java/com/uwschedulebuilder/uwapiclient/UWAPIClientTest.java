package com.uwschedulebuilder.uwapiclient;

import static org.junit.Assert.*;
import org.junit.Test;

import com.uwschedulebuilder.builder.Section;
import com.uwschedulebuilder.builder.Term;
import com.uwschedulebuilder.uwapiclient.UWAPIClient;
import com.uwschedulebuilder.uwapiclient.UWAPIException;

import java.util.ArrayList;
import java.util.Arrays;

// These aren't really a JUnit tests, more like helpers for manually testing the Client
public class UWAPIClientTest {

	@Test
	public void testGetSections() {
		String className = "CS100";
		String term = "1171";
		
		ArrayList<Section> sections = null;
		try {
			sections = UWAPIClient.getSections(className, term);
		} catch (UWAPIException e) {
			fail(e.toString());
		}
		
		System.out.println(sections);
	}	
	
	@Test
	public void testGetTermCourses() {
		try {
			Term[] terms = UWAPIClient.getTerms();
			System.out.println(Arrays.toString(terms));
		} catch (UWAPIException e) {
			fail(e.toString());
		}
		
	}

}

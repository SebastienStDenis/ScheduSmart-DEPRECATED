package com.uwschedulebuilder.builder;

import java.util.ArrayList;

// Term represents the course information for a school term
public class Term {
	String name; // ex: "W17"
	String code; // ex: "1171"
	ArrayList<String> courses; // array of course names ("CS 241 - Foundations of Sequential Programs")
	
	public Term(String name, String code, ArrayList<String> courses) {
		this.name = name;
		this.code = code;
		this.courses = courses;
	}
	
	public String toString() {
		return String.format("name: %s, code: %s, courses: %s", name, code, courses.toString());
	}
}

package com.schedusmart.schedulebuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.ListIterator;

// Schedule represents a complete course schedule, with a set on Component objects
public class Schedule {
	// each pair is a course name and its corresponding list of components
	private HashMap<String, ArrayList<Component>> components;
	
	private boolean closed;
	
	private double score;
	
	public Schedule(ArrayList<Component> componentList, double score) {
		components = new HashMap<String, ArrayList<Component>>();
		
		// add each component in component
		ListIterator<Component> compIt = componentList.listIterator();
		while (compIt.hasNext()) {
			Component comp = compIt.next();
			String courseName = comp.getName();
			
			ArrayList<Component> compList = components.get(courseName);
			if (compList == null) { // no pair found for current course
				ArrayList<Component> newCourse = new ArrayList<Component>();
				newCourse.add(comp);
				components.put(courseName, newCourse);
			} else {
				compList.add(comp);
			}
		}		
		
		this.closed = false;		
		this.score = score;
		
		compIt = componentList.listIterator();
		while (compIt.hasNext()) {
			if (compIt.next().getClosed()) {
				this.closed = true;
				break;
			}
		}		
	}
	
	// getComponents returns the components field
	public HashMap<String, ArrayList<Component>> getComponents() {
		return components;
	}
	
	// getSize returns the number of courses in the schedule
	public int getSize() {
		return components.size();
	}
	
	// getClosed returns the closed field
	public boolean getClosed() {
		return closed;
	}
	
	// getScore returns the score field
	public double getScore() {
		return score;
	}
	
	public String toString() {
		return String.format("Schedule: score - %f, %s", score, components.toString());		
	}
}

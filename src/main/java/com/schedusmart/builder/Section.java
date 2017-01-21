package com.schedusmart.builder;

import java.util.ArrayList;

// Section represents all components of a section of
//    a course (eg. all "LEC" components of CS 241)
public class Section {
	private ArrayList<Component> components;
	private String type; // ex: "LEC"
	
	public Section() {
		this.type = "";
		components = new ArrayList<Component>();
	}
	
	public Section(String type) {
		this.type = type;
		components = new ArrayList<Component>();
	}
	
	// getComponent returns the Component at position ind in components
	public Component getComponent(int ind) {
		return components.get(ind);
	}
	
	// componentsSize returns the amount of Components in the components field
	public int componentsSize() {
		return components.size();
	}

	// getType returns the type field
	public String getType() {
		return type;
	}
	
	// addComponent adds comp to the components field
	public void addComponent(Component comp) {
		components.add(comp);
	}
		
	public String toString() {
		return String.format("Section: %s, components -  %s", type, components.toString());
	}
}

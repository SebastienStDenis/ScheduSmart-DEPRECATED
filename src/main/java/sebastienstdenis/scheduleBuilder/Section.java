package sebastienstdenis.scheduleBuilder;

import java.util.ArrayList;

// Section represents all components of a section of
//    a course (eg. add "LEC" components of CS 241)
class Section {
	private ArrayList<Component> components;
	private String type; // ex: "LEC"
	
	Section() {
		this.type = "";
		components = new ArrayList<Component>();
	}
	
	Section(String type) {
		this.type = type;
		components = new ArrayList<Component>();
	}
	
	// getComponent returns the Component at position ind in components
	Component getComponent(int ind) {
		return components.get(ind);
	}
	
	// componentsSize returns the amount of Components in the components field
	int componentsSize() {
		return components.size();
	}

	// getType returns the type field
	String getType() {
		return type;
	}
	
	// addComponent adds comp to the components field
	void addComponent(Component comp) {
		components.add(comp);
	}
		
	public String toString() {
		return String.format("Section: %s, components -  %s", type, components.toString());
	}
}

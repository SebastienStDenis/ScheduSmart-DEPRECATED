package sebastienstdenis.scheduleBuilder;

import java.util.ArrayList;

class Section { // each course has a Section object for "LEC", another for "TUT", ...
	private ArrayList<Component> components;
	private String classType; // "LEC"
	
	Section() {
		this.classType = "";
		components = new ArrayList<Component>();
	}
	
	Section(String classType) {
		this.classType = classType;
		components = new ArrayList<Component>();
	}
	
	String getClassType() {
		return classType;
	}
	
	int componentsLen() {
		return components.size();
	}
	
	Component getComponent(int ind) {
		return components.get(ind);
	}
	
	void addComponent(Component comp) {
		components.add(comp);
	}
		
	public String toString() {
		return String.format("\n\tSection: classType - %s, components - \n\t\t%s", classType, components.toString());
	}
}

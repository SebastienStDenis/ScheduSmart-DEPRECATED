package sebastienstdenis.scheduleBuilder;

import java.util.ArrayList;
import java.util.ListIterator;

class Schedule {
	private ArrayList<Component> components;
	
	private boolean closed;
	
	ArrayList<Component> getComponents() {
		return components;
	}
	
	boolean getClosed() {
		return closed;
	}
	
	// scores
	
	Schedule(ArrayList<Component> components) {
		this.components = new ArrayList<Component>(components);
		
		this.closed = false;
		
		ListIterator<Component> compIt = components.listIterator();
		while (compIt.hasNext()) {
			if (compIt.next().getClosed()) {
				this.closed = true;
				break;
			}
		}		
	}
	
	public String toString() {
		return components.toString();		
	}
}

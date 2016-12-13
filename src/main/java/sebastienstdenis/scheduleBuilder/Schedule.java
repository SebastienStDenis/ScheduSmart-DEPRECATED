package sebastienstdenis.scheduleBuilder;

import java.util.ArrayList;
import java.util.ListIterator;

// Schedule represents a complete course schedule, with a set on Component objects
class Schedule {
	private ArrayList<Component> components;
	
	private boolean closed;
	
	private int score;
	
	Schedule(ArrayList<Component> components, int score) {
		this.components = new ArrayList<Component>(components);		
		this.closed = false;		
		this.score = score;
		
		ListIterator<Component> compIt = components.listIterator();
		while (compIt.hasNext()) {
			if (compIt.next().getClosed()) {
				this.closed = true;
				break;
			}
		}		
	}
	
	// getComponents returns the components field
	ArrayList<Component> getComponents() {
		return components;
	}
	
	// getClosed returns the closed field
	boolean getClosed() {
		return closed;
	}
	
	// getScore returns the score field
	int getScore() {
		return score;
	}
	
	public String toString() {
		return String.format("%d %s", score, components.toString());		
	}
}

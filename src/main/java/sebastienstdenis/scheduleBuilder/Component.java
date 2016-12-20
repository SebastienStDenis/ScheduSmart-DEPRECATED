package sebastienstdenis.scheduleBuilder;

import java.util.ArrayList;


// Component represents one course component of a certain course (ex: CS 241 TUT 001)
class Component {
	private String name; // ex: "CS 241"
	private int classNumber; // ex: "1234"
	private String sectionName; // ex: "TUT 001"
	
	private int assocClass;	
	private boolean closed; // true if component is closed or full
	
	private ArrayList<Block> blocks;
	
	Component(String name, int classNumber, String sectionName, int assocClass, boolean closed) {
		this.name = name;
		this.classNumber = classNumber;
		this.sectionName = sectionName;
		this.assocClass = assocClass;
		this.closed = closed;
		
		blocks = new ArrayList<Block>();
	}
	
	// getName returns the name field 
	String getName() {
		return name;
	}
	
	// getClassNumber returns the classNumber field
	int getClassNumber() {
		return classNumber;
	}
	
	//getSectionName returns the sectionName field
	String getSectionName() {
		return sectionName;
	}
	
	// getAssocClass returns the assocClass field
	int getAssocClass() {
		return assocClass;
	}
	
	// getClosed returns the closed field
	boolean getClosed() {
		return closed;
	}
	
	// setClosed sets the closed field to the value of closed
	void setClosed(boolean closed) {
		this.closed = closed;
	}
	
	// getBlock returns the block at position ind of the blocks fields
	Block getBlock(int ind) {
		return blocks.get(ind);
	}
	
	// addBlock adds block to the blocks field
	void addBlock(Block block) {		
		blocks.add(block);
	}
	
	// blocksSize returns the amount of blocks in the blocks field
	int blocksSize() {
		return blocks.size();
	}
	
	public String toString() {
		return String.format("Component: %s %d %s, assoc %d, closed - %b, blocks - %s", 
				name, classNumber, sectionName, assocClass, closed, blocks.toString());
		//return String.format("(%s %s (%d))", name, sectionName, assocClass);
	}
}

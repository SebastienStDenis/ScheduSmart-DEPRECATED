package com.schedusmart.schedulebuilder;

import java.util.ArrayList;


// Component represents one course component of a certain course (eg. CS 241 TUT 001)
public class Component {
	private String name; // eg. "CS 241"
	private int classNumber; // eg. "1234"
	private String sectionName; // eg. "TUT 001"
	
	private int assocClass;	// associated class number
	private boolean closed; // true if component is closed or full
	
	private ArrayList<Block> blocks;
	
	public Component(String name, int classNumber, String sectionName, int assocClass, boolean closed) {
		this.name = name;
		this.classNumber = classNumber;
		this.sectionName = sectionName;
		this.assocClass = assocClass;
		this.closed = closed;
		
		blocks = new ArrayList<Block>();
	}
	
	// getName returns the name field 
	public String getName() {
		return name;
	}
	
	// getClassNumber returns the classNumber field
	public int getClassNumber() {
		return classNumber;
	}
	
	//getSectionName returns the sectionName field
	public String getSectionName() {
		return sectionName;
	}
	
	// getAssocClass returns the assocClass field
	public int getAssocClass() {
		return assocClass;
	}
	
	// getClosed returns the closed field
	public boolean getClosed() {
		return closed;
	}
	
	// setClosed sets the closed field to the value of closed
	public void setClosed(boolean closed) {
		this.closed = closed;
	}
	
	// getBlock returns the block at position ind in the blocks field
	public Block getBlock(int ind) {
		return blocks.get(ind);
	}
	
	// addBlock adds block to the blocks field
	public void addBlock(Block block) {		
		blocks.add(block);
	}
	
	// blocksSize returns the amount of blocks in the blocks field
	public int blocksSize() {
		return blocks.size();
	}
	
	public String toString() {
		return String.format("Component: %s %d %s, assoc %d, closed - %b, blocks - %s", 
				name, classNumber, sectionName, assocClass, closed, blocks.toString());
		//return String.format("(%s %s (%d))", name, sectionName, assocClass);
	}
}

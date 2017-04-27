package com.schedusmart.schedulebuilder;

import java.util.ArrayList;


// Component represents one course component of a certain course (eg. CS 241 TUT 001)
public class Component {
	private String name; // eg. "CS 241"
	private int classNumber; // eg. "1234"
	private String sectionName; // eg. "TUT 001"
	
	private int assocClass;	// associated class number
	private String rel1; // related class 1
	private String rel2; // related class 2
	private boolean closed; // true if component is closed or full
	
	private ArrayList<Block> blocks;
	
	public Component(String name, int classNumber, String sectionName, int assocClass,
			String rel1, String rel2, boolean closed) {
		this.name = name;
		this.classNumber = classNumber;
		this.sectionName = sectionName;
		this.assocClass = assocClass;
		this.rel1 = rel1;
		this.rel2 = rel2;
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
	
	// getRel1 returns the rel1 field
	public String getRel1() {
		return rel1;
	}
		
	// getRel2 returns the rel2 field
	public String getRel2() {
		return rel2;
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
		return String.format("Component: %s %d %s, assoc %d, rel1 %s, rel2 %s, closed - %b, blocks - %s", 
				name, classNumber, sectionName, assocClass, rel1, rel2, closed, blocks.toString());
		//return String.format("(%s %s (%d))", name, sectionName, assocClass);
	}
}

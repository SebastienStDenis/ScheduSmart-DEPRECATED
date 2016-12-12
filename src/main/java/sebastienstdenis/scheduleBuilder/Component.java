package sebastienstdenis.scheduleBuilder;

import java.util.ArrayList;

class Component {
	private String name; // "CS 241"
	private int classNum; // "1234"
	private String sectionName; // "LEC 001"
	
	private int assocClass; // 1
	
	private boolean closed; // true if component is closed or full
	
	private ArrayList<Block> blocks;
	
	Component(String name, int classNum, String sectionName, int assocClass, boolean closed) {
		this.name = name;
		this.classNum = classNum;
		this.sectionName = sectionName;
		this.assocClass = assocClass;
		this.closed = closed;
		
		blocks = new ArrayList<Block>();
	}
	
	String getName() {
		return name;
	}
	
	int getClassNum() {
		return classNum;
	}
	
	String getSectionName() {
		return sectionName;
	}
	
	int getAssocClass() {
		return assocClass;
	}
	
	boolean getClosed() {
		return closed;
	}
	
	void setClosed(boolean closed) {
		this.closed = closed;
	}
	
	Block getBlock(int ind) {
		return blocks.get(ind);
	}
	
	void addBlock(Block block) {		
		blocks.add(block);
	}
	
	int blocksLength() {
		return blocks.size();
	}
	
	public String toString() {
		
		//return String.format("\n\t\tComponent: name - %s, classNum - %d, sectionName - %s, assocClass - %d, closed - %b, blocks - \n\t\t\t%s", 
		//		name, classNum, sectionName, assocClass, closed, blocks.toString());
				
		return String.format("(%s %s (%d))", name, sectionName, assocClass);
	}
}

package sebastienstdenis.scheduleBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.ListIterator;

class Calendar {
	static final String[] DAYS = {"SU", "M", "T", "W", "Th", "F", "S"};
	static final int SLOTS_IN_DAY = 48;
	
	private HashMap<String, ArrayList<LinkedList<Block>>> timeTable; // the linkedlists are sorted by the startDate of each block
	
	private ArrayList<Component> components;
	
	Calendar() {		
		timeTable = new HashMap<String, ArrayList<LinkedList<Block>>>();		
		resetCalendar();		
	}	
	
	void resetCalendar() {
		components = new ArrayList<Component>();
		for (int pos = 0; pos < DAYS.length; ++ pos) {
			ArrayList<LinkedList<Block>> dayArr = new ArrayList<LinkedList<Block>>(SLOTS_IN_DAY);
			for (int dayPos = 0; dayPos < SLOTS_IN_DAY; ++dayPos) {
				dayArr.add(new LinkedList<Block>());
			}
			timeTable.put(DAYS[pos], dayArr);
		}
	}
	
	int getSlotInd(String time) {
		String[] hourMin = time.split(":");
		
		// check if it has 2 values, o/w erroe
		
		int hour = Integer.parseInt(hourMin[0]);
		int min = Integer.parseInt(hourMin[1]);
		
		if (min == 20 || min == 30) {
			return 2 * hour + 1;
		} else if (min == 50) {
			return 2 * (hour + 1);
		} else {
			return 2 * hour;
		}		
	}
	
	private static int compareDates(String date1, String date2) {
		String[] monthDay1 = date1.split("/");
		// check if two values, o/w error
		int month1 = Integer.parseInt(monthDay1[0]);
		int day1 = Integer.parseInt(monthDay1[1]);
		
		String[] monthDay2 = date2.split("/");
		// check if two values, o/w error
		int month2 = Integer.parseInt(monthDay2[0]);
		int day2 = Integer.parseInt(monthDay2[1]);
		
		if (month1 < month2) {
			return -1;
		} else if (month1 > month2) {
			return 1;
		} else if (day1 < day2) {
			return -1;
		} else if (day1 > day2) {
			return 1;
		} else {
			return 0;
		}		
	}
	
	/*
	// don't need this since our linked lists will always be in order
	private static boolean datesClash(String startDate1, String endDate1, String startDate2, String endDate2) {
		return ((compareDates(startDate2, startDate1) >= 0 && compareDates(startDate2, endDate1) <= 0) ||
				(compareDates(startDate1, startDate2) >= 0 && compareDates(startDate1, endDate2) <= 0));
	}
	*/
	
	boolean addComponent(Component comp) {
		//System.out.printf("Adding %s, %s:\n", comp.getName(), comp.getSectionName());
		
		int blocksLen = comp.blocksLength();
		
		for (int pos = 0; pos < blocksLen; ++pos) {
			Block block = comp.getBlock(pos);
			int startInd = getSlotInd(block.getStartTime());
			int endInd = getSlotInd(block.getEndTime());
			String[] days = block.getDays();
					
			for (int daysPos = 0; daysPos < days.length; ++daysPos) {
				NextSlot:
				for (int slot = startInd; slot < endInd; ++slot) {
					//System.out.printf("trying - day: %s, slot: %d\n", days[daysPos], slot);
					LinkedList<Block> blockList = timeTable.get(days[daysPos]).get(slot);
					int listLen = blockList.size();
					if (listLen == 0) {
						continue;
					} else if (listLen >= 1 && (!blockList.get(0).hasDates() || !block.hasDates())) {
						return false;
					} else { //listLen >= 1, blocks have dates
						String startDate = block.getStartDate();						
						ListIterator<Block> blockIt = blockList.listIterator(0);
						
						while (blockIt.hasNext()) {
							Block currBlock = blockIt.next();
							int compareStart = compareDates(startDate, currBlock.getStartDate());
							
							if (compareStart == 0) {
								//System.out.printf("%s vs %s = %d\n", startDate, currBlock.getStartDate(), compareStart);
								//System.out.println("failed 1");
								return false;
							} else if (compareStart < 0) {
								if (compareDates(block.getEndDate(), currBlock.getStartDate()) >= 0) {
									//System.out.println("failed 2");
									return false;
								} else {
									continue NextSlot;
								}
							} else if (compareStart > 0) {
								if (compareDates(startDate, currBlock.getEndDate()) <= 0) {
									//System.out.println("failed 3");
									return false;
								}
							}
						}
					}
				}
			}
		}
		
		components.add(comp);
		
		for (int pos = 0; pos < blocksLen; ++pos) {
			Block block = comp.getBlock(pos);
			int startInd = getSlotInd(block.getStartTime());
			int endInd = getSlotInd(block.getEndTime());
			String[] days = block.getDays();
					
			for (int daysPos = 0; daysPos < days.length; ++daysPos) {
				NextSlot:
				for (int slot = startInd; slot < endInd; ++slot) {
					LinkedList<Block> blockList = timeTable.get(days[daysPos]).get(slot);
					
					if (blockList.size() == 0) {
						blockList.addFirst(block);
					} else {
						String startDate = block.getStartDate();						
						ListIterator<Block> blockIt = blockList.listIterator(0);
						
						while(blockIt.hasNext()) {
							if (compareDates(startDate, blockIt.next().getStartDate()) < 0) {
								blockIt.previous();
								blockIt.add(block);
								continue NextSlot;
							}
						}
						blockIt.add(block);							
					}
				}
			}
		}
		
		return true;
	}
	
	void removeComponent(Component comp) { // assumes the full component has been added
		int blocksLen = comp.blocksLength();
		
		for (int pos = 0; pos < blocksLen; ++pos) {
			Block block = comp.getBlock(pos);
			int startInd = getSlotInd(block.getStartTime());
			int endInd = getSlotInd(block.getEndTime());
			String[] days = block.getDays();
				
			for (int daysPos = 0; daysPos < days.length; ++daysPos) {
				NextSlot:
				for (int slot = startInd; slot < endInd; ++slot) {
					LinkedList<Block> blockList = timeTable.get(days[daysPos]).get(slot);
					
					if (blockList.size() == 1) {
						blockList.removeFirst();
					} else {
						String startDate = block.getStartDate();						
						ListIterator<Block> blockIt = blockList.listIterator(0);
						
						while(blockIt.hasNext()) {
							if (compareDates(startDate, blockIt.next().getStartDate()) == 0) {
								blockIt.remove();
								continue NextSlot;
							}
						}	
					}
				}
			}
		}
		
		components.remove(comp);		
	}
	
	Schedule makeSchedule() {
		return new Schedule(components);
	}
}

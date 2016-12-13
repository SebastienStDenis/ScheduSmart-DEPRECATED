package sebastienstdenis.scheduleBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.ListIterator;

// Calendar takes Component objects, determines if they make a valid
//    schedule and return a Schedule object from them
class Calendar {
	
	// the keys are days "M", "T", ... and the values are arrays of 48 linked lists (corresponding
	//    to the 48 half-hours in the day - index 0 is 0:00, index 1 is 0:30, ...).
	//    The linked lists are sorted by the start date of each block.
	private HashMap<String, ArrayList<LinkedList<Block>>> timeTable; // the linkedLists are sorted by the startDate of each block
	
	private ArrayList<Component> components;
	
	ScorePreferences scorePreferences;
	
	static final String[] DAYS = {"SU", "M", "T", "W", "Th", "F", "S"};
	static final int SLOTS_IN_DAY = 48;
	
	Calendar() {		
		timeTable = new HashMap<String, ArrayList<LinkedList<Block>>>();		
		resetCalendar();		
	}	
	
	// resetCalendar() resets the Calendar object to its post-initialization state
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
	
	// getSlotInd returns the proper timeTable index (0-47) corresponding to time
	private int getSlotInd(String time) {
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
	
	// compareDates returns 0 if the dates are equal, <0 if date1 comes before date2, >0 otherwise
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
	
	// addComponent adds comp to the Calendar.  True is returned if comp was added without clashes.
	//    Otherwise, false is returned and nothing is added to the Calendar.
	boolean addComponent(Component comp) {
		//System.out.printf("Adding %s, %s:\n", comp.getName(), comp.getSectionName());
		
		int blocksLen = comp.blocksSize();
		
		for (int pos = 0; pos < blocksLen; ++pos) { // before adding anything, check for possible clashes
			Block block = comp.getBlock(pos);
			int startInd = getSlotInd(block.getStartTime());
			int endInd = getSlotInd(block.getEndTime());
			String[] days = block.getDays();
					
			// for each day in days, we will iterate through all timeTable slots from startInd (incl.) to endInd (excl.)
			for (int daysPos = 0; daysPos < days.length; ++daysPos) {
				NextSlot:
				for (int slot = startInd; slot < endInd; ++slot) {
					//System.out.printf("trying - day: %s, slot: %d\n", days[daysPos], slot);
					LinkedList<Block> blockList = timeTable.get(days[daysPos]).get(slot);
					int listLen = blockList.size();
					
					if (listLen == 0) {
						// time slot is free, so no clash - can continue 
						continue;
					} else if (listLen >= 1 && (!blockList.get(0).hasDates() || !block.hasDates())) {
						// time slot has at least one block, and either that block occurs all term or 'block' does.
						// this mean that both can't be in this slot at the same time - we have a clash, return false
						return false;
					} else { //listLen >= 1, blocks have dates
						// time slot has at least one block, check if their dates clash with block's dates
						String startDate = block.getStartDate();						
						ListIterator<Block> blockIt = blockList.listIterator(0);
						
						// iterate through blocks (checking for clashes) until a we find a block that starts after our block.
						// we then check if this block clashes with block. If not, then our block will not clash if added.
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
		
		// iterate through the relevant slots of timeTable just like above,
		//    this time adding our blocks to all the appropriate positions
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
	
	// removeComponent will remove comp from the Calendar.  This assumes that
	//    comp has previously been fully added to the Calendar and is still there.
	void removeComponent(Component comp) {
		int blocksLen = comp.blocksSize();
		
		// iterate through all relevant slots of timeTable just like in
		//    addComponent (above), remove the blocks of comp
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
	
	// TODO: this
	private int calulateScore() {
		return 0;
	}
	
	// makeSchedule returns a Schedule object based on the current contents of the Calendar
	Schedule makeSchedule() {
		return new Schedule(components, calulateScore());
	}
}

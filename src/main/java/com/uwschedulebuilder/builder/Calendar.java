package com.uwschedulebuilder.builder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.ListIterator;

// Calendar takes Component objects, determines if they make a valid
//    schedule and return a Schedule object from them
public class Calendar {
	
	// the keys are days "M", "T", ... and the values are arrays of 48 linked lists (corresponding
	//    to the 48 half-hours in the day - index 0 is 0:00, index 1 is 0:30, ...).
	//    The linked lists are sorted by the start date of each block.
	private HashMap<String, ArrayList<LinkedList<Block>>> timeTable; // the linkedLists are sorted by the startDate of each block
	
	private ArrayList<Component> components;
	
	ScorePreferences scorePreferences;
	
	static final String[] DAYS = {"Su", "M", "T", "W", "Th", "F", "S"};
	static final int SLOTS_IN_DAY = 48;
	
	Calendar() {		
		this.timeTable = new HashMap<String, ArrayList<LinkedList<Block>>>();
		this.scorePreferences = new ScorePreferences();
		resetCalendar();		
	}	
	
	// resetCalendar() resets the Calendar object to its post-initialization state
	public void resetCalendar() {
		components = new ArrayList<Component>();
		for (int pos = 0; pos < DAYS.length; ++ pos) {
			ArrayList<LinkedList<Block>> dayArr = new ArrayList<LinkedList<Block>>(SLOTS_IN_DAY);
			for (int dayPos = 0; dayPos < SLOTS_IN_DAY; ++dayPos) {
				dayArr.add(new LinkedList<Block>());
			}
			timeTable.put(DAYS[pos], dayArr);
		}
	}
	
	// getSlotInd returns the proper timeTable index (0-48) corresponding to time
	private int getSlotInd(String time) throws IllegalArgumentException {
		String[] hourMin = time.split(":");
		
		if (hourMin.length != 2) {
			throw new IllegalArgumentException("Provided time cannot be parsed: " + time);
		}
		
		int hour = Integer.parseInt(hourMin[0]);
		int min = Integer.parseInt(hourMin[1]);
		
		if (min == 0) {
			return 2 * hour;
		} else if (min <= 30) {
			return 2 * hour + 1;
		} else {
			return 2 * (hour + 1);
		}		
	}
	
	// compareDates returns 0 if the dates are equal, <0 if date1 comes before date2, >0 otherwise
	private static int compareDates(String date1, String date2) throws IllegalArgumentException {
		String[] monthDay1 = date1.split("/");
		
		if (monthDay1.length != 2) {
			throw new IllegalArgumentException("Provided date cannot be parsed: " + date1);
		}
		
		int month1 = Integer.parseInt(monthDay1[0]);
		int day1 = Integer.parseInt(monthDay1[1]);
		
		String[] monthDay2 = date2.split("/");
		
		if (monthDay2.length != 2) {
			throw new IllegalArgumentException("Provided date cannot be parsed: " + date2);
		}

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
	public boolean addComponent(Component comp) {
		int blocksLen = comp.blocksSize();
		
		for (int pos = 0; pos < blocksLen; ++pos) { // before adding anything, check for possible clashes
			Block block = comp.getBlock(pos);
			
			int startInd = 0;
			int endInd = 0;
			
			try {
				startInd = getSlotInd(block.getStartTime());
				endInd = getSlotInd(block.getEndTime());
			} catch (IllegalArgumentException e) {
				return false;
			}
				
			String[] days = block.getDays();
					
			// for each day in days, we will iterate through all timeTable slots from startInd (incl.) to endInd (excl.)
			for (int daysPos = 0; daysPos < days.length; ++daysPos) {
				NextSlot:
				for (int slot = startInd; slot < endInd; ++slot) {
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
							int compareStart = 0;
							
							try {
								compareStart = compareDates(startDate, currBlock.getStartDate());
							} catch (Exception e) {
								return false;
							}
							
							if (compareStart == 0) {
								return false;
							} else if (compareStart < 0) {
								if (compareDates(block.getEndDate(), currBlock.getStartDate()) >= 0) {
									return false;
								} else {
									continue NextSlot;
								}
							} else if (compareStart > 0) {
								if (compareDates(startDate, currBlock.getEndDate()) <= 0) {
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
	public void removeComponent(Component comp) {
		int blocksLen = comp.blocksSize();
		
		// iterate through all relevant slots of timeTable just like in
		//    addComponent (above), remove the blocks of comp
		for (int pos = 0; pos < blocksLen; ++pos) {
			Block block = comp.getBlock(pos);
			
			int startInd = 0;
			int endInd = 0;
			
			try {
				startInd = getSlotInd(block.getStartTime());
				endInd = getSlotInd(block.getEndTime());
			} catch (IllegalArgumentException e) {
				continue;
			}
			
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
	
	// setScorePreferences sets the scorePreferences field to scorePreferences
	public void setScorePreferences(ScorePreferences scorePreferences) {
		this.scorePreferences = scorePreferences;
	}
	
	// onlyTests returns true if all Blocks in blocks have sectionType "TST"
	private boolean onlyTests(LinkedList<Block> blocks) {
		ListIterator<Block> blocksIt = blocks.listIterator();
		
		while (blocksIt.hasNext()) {
			Block block = blocksIt.next();
			if (!block.getSectionType().equals("TST")) {
				return false;
			}
		}
		
		return true;
	}
	
	// calculateScore uses a top-secret ultra-precise algorithm to return a
	//    score of the current calendar based on the options in scorePreferences.
	//    Considers the amount of morning classes, afternoon classes, school
	//    days and the average day length.  (This algorithm is not very good)
	private double calculateScore() {		
		double noEarlyCount = 0;
		double noLateCount = 0;		
		double shortDaysCount = 0;		
		double schoolDays = 0;
		
		for (int daysPos = 0; daysPos < DAYS.length; ++daysPos) {
			ArrayList<LinkedList<Block>> day = timeTable.get(DAYS[daysPos]);
		
			boolean dayStarted = false;
			
			int startInd = 0;      // index of first slot of day
			int endInd = 0;        // index of last slot of day
			double classHours = 0; // total class hours of day
			
			for (int ind = 0; ind < SLOTS_IN_DAY; ++ind) {
				LinkedList<Block> slot = day.get(ind);
				
				if (slot.size() == 0 || onlyTests(slot)) {
					// don't consider "TST" sections for calculating score
					continue;
				} else {
					if (!dayStarted) {
						startInd = ind;
						dayStarted = true;
						classHours += 0.5;
					}
					classHours += 0.5;
					endInd = ind;
				}
			}
			
			if (dayStarted) {
				++schoolDays;
				
				if (startInd <= 16) {
					noEarlyCount += (startInd-16) / 2;
				} else if (startInd <= 26) {
					noEarlyCount += startInd - 16;
				} else {
					noEarlyCount += 10;
				}
				
				if (endInd <= 24) {
					noLateCount += 10;
				} else if (endInd <= 34) {
					noLateCount += 34 - endInd;
				} else {
					noLateCount += (34-endInd)/2.0;
				}
			
				if (classHours <= 1) {
					shortDaysCount += 10;
				} else if (classHours <= 6) {
					shortDaysCount += (6-classHours) * 2;
				} else {
					shortDaysCount += (6-classHours);
				}
			}
		}
		
		double score = 0;
		
		if (schoolDays != 0) {
			noEarlyCount /= schoolDays;
			noLateCount /= schoolDays;
			switch (scorePreferences.getClassTimes()) {
			case 1:
				score += 2*noLateCount;
				score += noEarlyCount;
				break;
			case 2:
				score += noLateCount;
				score += 2*noEarlyCount;
				break;
			default:
				score += noLateCount;
				score += noEarlyCount;
				break;			
			}
			
			shortDaysCount /= schoolDays;
			switch (scorePreferences.getClassTimes()) {
			case 1:
				score += 2*shortDaysCount;
				score += 7-schoolDays;
				break;
			case 2:
				score += shortDaysCount;
				score += 2*(7-schoolDays);
				break;
			default:
				score += shortDaysCount;
				score += 7-schoolDays;
				break;			
			}
		} else {
			score = 0;
		}		
		
		return score;
	}
	
	// makeSchedule returns a Schedule object based on the current contents of the Calendar
	public Schedule makeSchedule() {
		return new Schedule(components, calculateScore());
	}
}

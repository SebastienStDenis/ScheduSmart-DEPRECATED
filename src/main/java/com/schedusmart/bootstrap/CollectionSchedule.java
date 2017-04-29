package com.schedusmart.bootstrap;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import com.schedusmart.schedulebuilder.Component;
import com.schedusmart.schedulebuilder.Schedule;

// CollectionSchedules are like Schedules (in the 'schedulebuilder' package), but with a Collection of arrays instead of a map.
public class CollectionSchedule {
		Collection<ArrayList<Component>> courses; // each ArrayList represents a different course
		
		boolean closed;
		double score;
		
		public CollectionSchedule(Schedule sched) {
			this.courses = sched.getComponents().values();
			this.closed = sched.getClosed();
			this.score = sched.getScore();
		}
		
		public String toString() {
			String result = "";
			
			Iterator<ArrayList<Component>> it = this.courses.iterator();
			
			while (it.hasNext()) {
				ArrayList<Component> comps = it.next();
				result += comps.get(0).getName() + " (";
				
				for (int i = 0; i < comps.size(); ++i) {
					result += comps.get(i).getSectionName() + ", ";
				}
				
				result += ") ";				
			}
			
			return result;
		}
	}
package com.schedusmart.bootstrap;

import java.util.ArrayList;
import java.util.Collection;

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
	}
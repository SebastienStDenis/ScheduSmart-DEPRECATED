package com.schedusmart.bootstrap;

import java.util.ArrayList;
import java.util.Collection;

import com.schedusmart.schedulebuilder.Component;
import com.schedusmart.schedulebuilder.Schedule;

// ArraySchedules are like Schedules, but with a Collection of arrays instead of a map.
// Each collection element represents a different course.
public class CollectionSchedule {
		Collection<ArrayList<Component>> courses;
		
		boolean closed;
		double score;
		
		public CollectionSchedule(Schedule sched) {
			this.courses = sched.getComponents().values();
			this.closed = sched.getClosed();
			this.score = sched.getScore();
		}
	}
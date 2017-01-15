package com.uwschedulebuilder.bootstrap;

import java.util.ArrayList;
import java.util.Collection;

import com.uwschedulebuilder.builder.Component;
import com.uwschedulebuilder.builder.Schedule;

// ArraySchedules are like Schedules, but with a Collection of arrays instead of a map.
// Each collection element represents a different course.
public class CollectionSchedule {
		Collection<ArrayList<Component>> components;
		
		boolean closed;
		double score;
		
		public CollectionSchedule(Schedule sched) {
			this.components = sched.getComponents().values();
			this.closed = sched.getClosed();
			this.score = sched.getScore();
		}
	}
package com.schedusmart.schedulebuilder;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

public class ScheduleTest {

	@Test
	public void testSchedule() {
		ArrayList<Component> comps = new ArrayList<Component>();
		Schedule sched = new Schedule(comps, 0);
		
		assertEquals(sched.getComponents().size(), 0);
		assertEquals(sched.getClosed(), false);
		
		Component comp1 = new Component("Course1", 0, null, 0, null, null, false);
		Component comp2 = new Component("Course2", 0, null, 0, null, null, false);
		comps.add(comp1);
		comps.add(comp2);
		sched = new Schedule(comps, 0);
		
		assertEquals(sched.getComponents().size(), 2);
		assertEquals(sched.getClosed(), false);
		
		Component comp3 = new Component("Course3", 0, null, 0, null, null, true);
		comps.add(comp3);
		sched = new Schedule(comps, 0);
		
		assertEquals(sched.getComponents().size(), 3);
		assertEquals(sched.getClosed(), true);
	}

}

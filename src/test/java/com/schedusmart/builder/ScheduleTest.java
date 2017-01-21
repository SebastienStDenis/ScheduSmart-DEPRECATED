package com.schedusmart.builder;

import static org.junit.Assert.*;
import java.util.ArrayList;

import org.junit.Test;

import com.schedusmart.builder.Component;
import com.schedusmart.builder.Schedule;

public class ScheduleTest {

	@Test
	public void testSchedule() {
		ArrayList<Component> comps = new ArrayList<Component>();		
		Schedule sched = new Schedule(comps, 0);		
		
		assertEquals(sched.getComponents().size(), 0);
		assertEquals(sched.getClosed(), false);
		
		Component comp1 = new Component("Course1", 0, null, 0, false);
		Component comp2 = new Component("Course2", 0, null, 0, false);
		comps.add(comp1);
		comps.add(comp2);
		sched = new Schedule(comps, 0);		
		
		assertEquals(sched.getComponents().size(), 2);
		assertEquals(sched.getClosed(), false);
		
		Component comp3 = new Component("Course3", 0, null, 0, true);
		comps.add(comp3);
		sched = new Schedule(comps, 0);
		
		assertEquals(sched.getComponents().size(), 3);
		assertEquals(sched.getClosed(), true);
	}

}

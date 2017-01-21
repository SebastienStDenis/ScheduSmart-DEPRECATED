package com.schedusmart.builder;

import static org.junit.Assert.*;

import org.junit.Test;

import com.schedusmart.builder.Block;
import com.schedusmart.builder.Calendar;
import com.schedusmart.builder.Component;
import com.schedusmart.builder.Schedule;

public class CalendarTest {

	@Test
	public void testAddRemoveComponent1() {
		Calendar cal = new Calendar();
		
		Component comp1 = new Component("Comp1", 0, null, 0, false);
		comp1.addBlock(new Block("11:00", "12:20", "MWTh", null, null, null, null, "LEC"));
		assertTrue(cal.addComponent(comp1));
		
		Component comp2 = new Component("Comp2", 0, null, 0, false);
		comp2.addBlock(new Block("12:30", "2:20", "MWTh", null, null, null, null, "LEC"));
		assertTrue(cal.addComponent(comp2));
		
		Schedule sched = cal.makeSchedule();
		assertTrue(sched.getComponents().get("Comp1").contains(comp1));
		assertTrue(sched.getComponents().get("Comp2").contains(comp2));
		
		cal.removeComponent(comp1);
		sched = cal.makeSchedule();
		assertFalse(sched.getComponents().containsKey("Comp1"));
		assertTrue(sched.getComponents().get("Comp2").contains(comp2));
		
		assertTrue(cal.addComponent(comp1));
		
	}
	
	@Test
	public void testAddRemoveCopmonent2() {
		Calendar cal = new Calendar();
		
		Component comp1 = new Component("Comp1", 0, null, 0, false);
		comp1.addBlock(new Block("0:00", "23:50", "M", null, "9/5", "11/14", null, "LEC"));
		assertTrue(cal.addComponent(comp1));
		

		Component comp2 = new Component("Comp2", 0, null, 0, false);
		comp2.addBlock(new Block("0:00", "23:50", "M", null, "11/21", "12/12", null, "LEC"));
		assertTrue(cal.addComponent(comp2));
		
		Schedule sched = cal.makeSchedule();
		assertTrue(sched.getComponents().get("Comp1").contains(comp1));
		assertTrue(sched.getComponents().get("Comp2").contains(comp2));
		
		cal.removeComponent(comp1);
		sched = cal.makeSchedule();
		assertFalse(sched.getComponents().containsKey("Comp1"));
		assertTrue(sched.getComponents().get("Comp2").contains(comp2));
		
		assertTrue(cal.addComponent(comp1));
	}
	
	@Test
	public void testAddRemoveCopmonent3() {
		Calendar cal = new Calendar();
		
		Component comp1 = new Component("Comp1", 0, null, 0, false);
		comp1.addBlock(new Block("11:20", "12:20", "Su", null, "11/13", "11/13", null, "LEC"));
		assertTrue(cal.addComponent(comp1));
		

		Component comp2 = new Component("Comp2", 0, null, 0, false);
		comp2.addBlock(new Block("11:20", "12:50", "Su", null, "11/27", "11/27", null, "LEC"));
		assertTrue(cal.addComponent(comp2));
		
		Component comp3 = new Component("Comp3", 0, null, 0, false);
		comp2.addBlock(new Block("12:00", "1:20", "Su", null, "11/20", "11/20", null, "LEC"));
		assertTrue(cal.addComponent(comp3));
		
		Schedule sched = cal.makeSchedule();
		assertTrue(sched.getComponents().get("Comp1").contains(comp1));
		assertTrue(sched.getComponents().get("Comp2").contains(comp2));
		assertTrue(sched.getComponents().get("Comp3").contains(comp3));
		
		cal.removeComponent(comp1);
		sched = cal.makeSchedule();
		assertFalse(sched.getComponents().containsKey("Comp1"));
		assertTrue(sched.getComponents().get("Comp2").contains(comp2));
		assertTrue(sched.getComponents().get("Comp3").contains(comp3));
		
		assertTrue(cal.addComponent(comp1));
		
		cal.removeComponent(comp3);
		sched = cal.makeSchedule();
		assertTrue(sched.getComponents().get("Comp1").contains(comp1));
		assertTrue(sched.getComponents().get("Comp2").contains(comp2));
		assertFalse(sched.getComponents().containsKey("Comp3"));
		
		assertTrue(cal.addComponent(comp3));
	}
	
	@Test
	public void testAddRemoveCopmonent4() {
		Calendar cal = new Calendar();
		
		Component comp1 = new Component("Comp1", 0, null, 0, false);
		comp1.addBlock(new Block("11:30", "12:20", "W", null, null, null, null, "LEC"));
		assertTrue(cal.addComponent(comp1));
		

		Component comp2 = new Component("Comp2", 0, null, 0, false);
		comp2.addBlock(new Block("11:30", "12:50", "Su", null, null, null, null, "LEC"));
		comp2.addBlock(new Block("10:00", "11:50", "W", null, "11/14", "11/28", null, "TUT"));
		assertFalse(cal.addComponent(comp2));
		
		Schedule sched = cal.makeSchedule();
		assertTrue(sched.getComponents().get("Comp1").contains(comp1));
		assertFalse(sched.getComponents().containsKey("Comp2"));
	}
	
	@Test
	public void testAddRemoveCopmonent5() {
		Calendar cal = new Calendar();
		
		Component comp1 = new Component("Comp1", 0, null, 0, false);
		comp1.addBlock(new Block("11:20", "12:20", "S", null, "11/26", "12/17", null, "LEC"));
		assertTrue(cal.addComponent(comp1));
		

		Component comp2 = new Component("Comp2", 0, null, 0, false);
		comp2.addBlock(new Block("11:20", "12:20", "S", null, "11/05", "11/26", null, "LEC"));
		assertFalse(cal.addComponent(comp2));
		
		Schedule sched = cal.makeSchedule();
		assertTrue(sched.getComponents().get("Comp1").contains(comp1));
		assertFalse(sched.getComponents().containsKey("Comp2"));
	}
	

}

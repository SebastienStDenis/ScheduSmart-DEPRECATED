package com.schedusmart.schedulebuilder;

import static org.junit.Assert.*;

import org.junit.Test;

import com.schedusmart.schedulebuilder.Block;

public class BlockTest {

	@Test
	public void testBlock() {
		String days = "MThF";
		Block block = new Block(null, null, days, null, null, null, null, null);
		assertArrayEquals(block.getDays(), new String[]{"M", "Th", "F"});
		
		days = "Su";
		block = new Block(null, null, days, null, null, null, null, null);
		assertArrayEquals(block.getDays(), new String[]{"Su"});
		
		days = "SuMTWThFS";
		block = new Block(null, null, days, null, null, null, null, null);
		assertArrayEquals(block.getDays(), new String[]{"Su", "M", "T", "W", "Th", "F", "S"});
	}
}

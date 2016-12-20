package sebastienstdenis.scheduleBuilder;

import static org.junit.Assert.*;

import org.junit.Test;

public class BlockTest {

	@Test
	public void testBlock() {
		String days = "MThF";
		Block block = new Block(null, null, days, null, null, null, null, null);
		assertArrayEquals(block.getDays(), new String[]{"M", "Th", "F"});
		
		days = "";
		block = new Block(null, null, days, null, null, null, null, null);
		assertArrayEquals(block.getDays(), new String[]{});
		
		days = "Su";
		block = new Block(null, null, days, null, null, null, null, null);
		assertArrayEquals(block.getDays(), new String[]{"Su"});
		
		days = "SuMTWThFS";
		block = new Block(null, null, days, null, null, null, null, null);
		assertArrayEquals(block.getDays(), new String[]{"Su", "M", "T", "W", "Th", "F", "S"});
	}
}

package sebastienstdenis.scheduleBuilder;

import static org.junit.Assert.*;
import org.junit.Test;
import java.util.ArrayList;

// This isn't really a JUnit test, more of a manual helper for using the Client
public class UWAPIClientTest {

	@Test
	public void testGetSections() {
		String className = "CS 241";
		String term = "1171";
		
		ArrayList<Section> sections = null;
		try {
			sections = UWAPIClient.getSections(className, term);
		} catch (UWAPIException e) {
			fail(e.toString());
		}
		
		System.out.println(sections);
	}

}

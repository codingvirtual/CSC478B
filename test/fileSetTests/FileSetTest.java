/**
 * 
 */
package fileSetTests;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import core.FileSet;

/**
 * @author zackburch
 *
 */
public class FileSetTest {
	
	private String source = "/Users/zackburch/Desktop/test.txt";
	private String sourceb = "/Users/zackburch/Desktop/test.txt";
	private String dest = "Users/zackburch/Google Drive/Test";

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	/**
	 * Test method for {@link core.FileSet#FileSet()}.
	 */
	@Test
	public void testFileSet() {
		FileSet fs = new FileSet();
		assertTrue(true);
	}

	/**
	 * Test method for {@link core.FileSet#FileSet(java.lang.String)}.
	 */
	@Test
	public void testFileSetString() {
		try {
			FileSet fs = new FileSet(dest);
			assertEquals(fs.getDestination(), dest);
		} catch (IOException e) {
			fail("Could not set up fileset with destination");
			e.printStackTrace();
		}
	}

	/**
	 * Test method for {@link core.FileSet#addPath(java.lang.String)}.
	 */
	@Test
	public void testAddPath() {
		FileSet fs = new FileSet();
		fs.addPath(source);
		ArrayList<String> fsreturn = fs.getFileSet();
		assertTrue(fsreturn.contains(source));
		
		fs.addPath(source);
		ArrayList<String> fileSet = fs.getFileSet();
		try {
			String test = fileSet.get(1);
			fail("Should not allow duplicate files");
		} catch(IndexOutOfBoundsException e) {
			assertTrue(true);
		}
		
		
	}

	/**
	 * Test method for {@link core.FileSet#removePath(java.io.File)}.
	 */
	@Test
	public void testRemovePath() {
		FileSet fs = new FileSet();
		fs.addPath(source);
		ArrayList<String> fsreturn = fs.getFileSet();
		assertTrue(fsreturn.size() == 1);
		fs.removePath(source);
		assertFalse(fsreturn.contains(source));
		
	}

	/**
	 * Test method for {@link core.FileSet#getSchedule()}.
	 */
	@Test
	public void testGetSchedule() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link core.FileSet#setSchedule(core.Schedule)}.
	 */
	@Test
	public void testSetScheduleSchedule() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link core.FileSet#setSchedule(java.util.Date, core.Schedule.Recurrence)}.
	 */
	@Test
	public void testSetScheduleDateRecurrence() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link core.FileSet#getDestination()}.
	 */
	@Test
	public void testGetDestination() {
		FileSet fs = new FileSet();
		assertEquals(fs.getDestination(), null);
		fs.setDestination(dest);
		assertEquals(fs.getDestination(), dest);
	}

	/**
	 * Test method for {@link core.FileSet#setDestination(java.lang.String)}.
	 */
	@Test
	public void testSetDestination() {
		FileSet fs = new FileSet();
		fs.setDestination(dest);
		assertEquals(fs.getDestination(), dest);
	}

	/**
	 * Test method for {@link core.FileSet#getFileSet()}.
	 */
	@Test
	public void testGetFileSet() {
		FileSet fs = new FileSet();
		ArrayList<String> fsreturn = fs.getFileSet();
		assertTrue(fsreturn.size() == 0);
		
		fs.addPath(source);
		fsreturn = fs.getFileSet();
		assertEquals(fsreturn.get(0), source);
	}

}

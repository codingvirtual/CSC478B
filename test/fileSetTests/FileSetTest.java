/**
 * 
 */
package fileSetTests;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import core.FileSet;

/**
 * @author zackburch
 *
 */
//public class FileSetTest {
//	
//	private String source = "/Users/zackburch/Desktop/test.txt";
//	private String sourceb = "/Users/zackburch/Desktop/test2.txt";
//	private String sourceFail = "/Users/zackburch/Desktop/testFail.txt";
//	private String dest = "Users/zackburch/Google Drive/Test";
//	private String destb = "Users/zackburch/Google Drive/TestB";
//	private String destFail = "Users/zackburch/GoogleDrive/TestFail";
//
//	/**
//	 * @throws java.lang.Exception
//	 */
//	@Before
//	public void setUp() throws Exception {
//		
//	}
//
//	/**
//	 * @throws java.lang.Exception
//	 */
//	@After
//	public void tearDown() throws Exception {
//	}
//
//	/**
//	 * Test method for {@link core.FileSet#FileSet()}.
//	 */
//	@Test
//	public void testFileSet() {
//		FileSet fs = new FileSet();
//		assertTrue(true);
//	}
//
//	/**
//	 * Test method for {@link core.FileSet#FileSet(java.lang.String)}.
//	 */
//	@Test
//	public void testFileSetString() {
//		try {
//			FileSet fs = new FileSet(dest);
//			assertEquals(fs.getDestination(), dest);
//		} catch (IOException e) {
//			fail("Could not set up fileset with destination");
//			e.printStackTrace();
//		}
//	}
//
//	/**
//	 * Test method for {@link core.FileSet#addPath(java.lang.String)}.
//	 */
//	@Test
//	public void testAddPath() {
//		String testPath;
//		FileSet fs = new FileSet();
//		
//		//test adding a file
//		fs.addPath(source);
//		ArrayList<String> fsreturn = fs.getFileSet(); //grab newest file set
//		assertTrue(fsreturn.contains(source));
//		
//		//test adding an invalid filename
//		fs.addPath(sourceFail);
//		fsreturn = fs.getFileSet();
//		assertFalse(fsreturn.contains(sourceFail));
//		
//		//test for duplicate file paths
//		fs.addPath(source);
//		fsreturn = fs.getFileSet();
//		try {
//			testPath = fsreturn.get(1);
//			fail("Should not allow duplicate files");
//		} catch(IndexOutOfBoundsException e) {
//			assertTrue(true);
//		}
//		
//		//test add second file
//		fs.addPath(sourceb);
//		fsreturn = fs.getFileSet();
//		assertTrue(fsreturn.contains(sourceb));
//		
//		//test for duplicate paths with valid path between
//		fs.addPath(source);
//		fsreturn = fs.getFileSet();
//		try {
//			testPath = fsreturn.get(2);
//			fail("Should not allow duplicate files");
//		} catch(IndexOutOfBoundsException e) {
//			assertTrue(true);
//		}
//		
//		
//	}
//
//	/**
//	 * Test method for {@link core.FileSet#removePath(java.io.File)}.
//	 */
//	@Test
//	public void testRemovePath() {
//		FileSet fs = new FileSet();
//		fs.addPath(source);
//		ArrayList<String> fsreturn = fs.getFileSet();
//		assertTrue(fsreturn.size() == 1);
//		fs.removePath(source);
//		fsreturn = fs.getFileSet();
//		assertFalse(fsreturn.contains(source));
//		
//		//test removePath() when no paths exist
//		fsreturn = fs.getFileSet();
//		assertTrue(fsreturn.size() == 0);
//		fs.removePath(source);
//		assertTrue(fsreturn.size() == 0);
//		
//	}
//
//	/**
//	 * Test method for {@link core.FileSet#getSchedule()}.
//	 */
//	@Test
//	public void testGetSchedule() {
//		fail("Not yet implemented");
//	}
//
//	/**
//	 * Test method for {@link core.FileSet#setSchedule(core.Schedule)}.
//	 */
//	@Test
//	public void testSetScheduleSchedule() {
//		fail("Not yet implemented");
//	}
//
//	/**
//	 * Test method for {@link core.FileSet#setSchedule(java.util.Date, core.Schedule.Recurrence)}.
//	 */
//	@Test
//	public void testSetScheduleDateRecurrence() {
//		fail("Not yet implemented");
//	}
//
//	/**
//	 * Test method for {@link core.FileSet#getDestination()}.
//	 */
//	@Test
//	public void testGetDestination() {
//		FileSet fs = new FileSet();
//		assertEquals(fs.getDestination(), null);
//		fs.setDestination(dest);
//		assertEquals(fs.getDestination(), dest);
//	}
//
//	/**
//	 * Test method for {@link core.FileSet#setDestination(java.lang.String)}.
//	 */
//	@Test
//	public void testSetDestination() {
//		FileSet fs = new FileSet();
//		fs.setDestination(dest);
//		assertEquals(fs.getDestination(), dest);
//		
//		//test that adding a new destination replaces old destination
//		fs.setDestination(destb);
//		assertEquals(fs.getDestination(), destb);
//		
//		//test adding invalid destination does not replace old destination
//		fs.setDestination(destFail);
//		assertFalse(fs.getDestination() == destFail);
//	}
//
//	/**
//	 * Test method for {@link core.FileSet#getFileSet()}.
//	 */
//	@Test
//	public void testGetFileSet() {
//		FileSet fs = new FileSet();
//		ArrayList<String> fsreturn = fs.getFileSet();
//		assertTrue(fsreturn.size() == 0);
//		
//		fs.addPath(source);
//		fsreturn = fs.getFileSet();
//		assertEquals(fsreturn.get(0), source);
//	}
//
//}

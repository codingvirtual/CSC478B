/**
 * 
 */
package fileSetTests;

import static org.junit.Assert.*;

import java.io.IOException;

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
	private String sourceb = "/Users/zackburch/Desktop/test2.txt";
	private String sourceFail = "/Users/zackburch/Desktop/testFail.txt";
	private String dest = "Users/zackburch/Google Drive/Test";
	private String destb = "Users/zackburch/Google Drive/TestB";
	private String destFail = "Users/zackburch/GoogleDrive/TestFail";

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
		
		//test adding a file
		fs.addElement(source);
		assertTrue(fs.contains(source));
		
		//test adding an invalid filename
		fs.addElement(sourceFail);
		assertFalse(fs.contains(sourceFail));
		
		//test for duplicate file paths
		fs.addElement(source);
		assertFalse(fs.getSize() > 1);
		
		//test add second file
		fs.addElement(sourceb);
		assertTrue(fs.contains(sourceb));
		
		//test for duplicate paths with valid path between
		fs.addElement(source);
		assertFalse(fs.getSize() > 2);
	}

	/**
	 * Test method for {@link core.FileSet#removePath(java.io.File)}.
	 */
	@Test
	public void testRemovePath() {
		FileSet fs = new FileSet();
		fs.addElement(source);
		assertTrue(fs.size() == 1);
		fs.removeElement(source);
		assertFalse(fs.contains(source));
		
		//test removePath() when no paths exist
		assertTrue(fs.size() == 0);
		fs.removeElement(source);
		assertTrue(fs.size() == 0);
		
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
		
		//test that adding a new destination replaces old destination
		fs.setDestination(destb);
		assertEquals(fs.getDestination(), destb);
		
		//test adding invalid destination does not replace old destination
		fs.setDestination(destFail);
		assertFalse(fs.getDestination() == destFail);
	}

}

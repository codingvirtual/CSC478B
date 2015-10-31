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
	
	private String source = "/Users/zackburch/Desktop/test/test.txt";
	private String sourceb = "/Users/zackburch/Desktop/test/test2.txt";
	private String sourceFail = "/Users/zackburch/Desktop/testFail.txt";
	private String dest = "Users/zackburch/Google Drive/Test";
	private String destb = "Users/zackburch/Google Drive/Test2";
	private String destFail = "Users/zackburch/GoogleDrive/TestFail";
	private String fsPath = "/Users/zackburch/Desktop/test";
	private String invalidFsPath = "/Users/zackburch/Desktop/invalid";
	private String backupName = "backup";
	private String invalidBackupName = "back*up";

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
		@SuppressWarnings("unused")
		FileSet fs = new FileSet();
		assert(true);
	}

	/**
	 * Test method for {@link core.FileSet#FileSet(java.lang.String)}.
	 */
	@Test
	public void testFileSetName() {
		try {
			@SuppressWarnings("unused")
			FileSet fs = new FileSet("name");
		} catch (Exception e) {
			fail("Could not set up fileset with name");
			e.printStackTrace();
		}
	}
	
	/**
	 * Test method for {@link core.FileSet#FileSet(java.lang.String, java.lang.String)}.
	 */
	@Test
	public void testFileSetNameDest() {
		try {
			@SuppressWarnings("unused")
			FileSet fs = new FileSet("name", dest);
		} catch (Exception e) {
			fail("Could not set up fileset with name and destination");
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
		try {
			fs.setDestination(dest);
		} catch (Exception e) {
			e.printStackTrace();
		}
		assertEquals(fs.getDestination(), dest);
	}

	/**
	 * Test method for {@link core.FileSet#setDestination(java.lang.String)}.
	 */
	@Test
	public void testSetDestination() {
		FileSet fs = new FileSet();
		try {
			fs.setDestination(dest);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		assertEquals(fs.getDestination(), dest);
		
		//test that adding a new destination replaces old destination
		try {
			fs.setDestination(destb);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		assertEquals(fs.getDestination(), destb);
		
		//test adding invalid destination does not replace old destination
		try { 
			fs.setDestination(destFail);
		} catch (Exception e){
			assertFalse(fs.getDestination() == destFail);
		}	
	}
	
	@Test
	public void testGetName() {
		
		//test null in empty constructor
		FileSet fs1 = new FileSet();
		assertEquals(fs1.getName(), null);
		
		//test when valid name is present
		try { 
			fs1.setName(backupName);
			assertEquals(fs1.getName(),backupName);
		} catch (Exception e) {
			fail("could not add name to fileset");
		}
	}
	
	@Test
	public void testSetName() {
	
		//test valid input via setter
		try {
			FileSet fs1 = new FileSet();
			fs1.setName(backupName);
			assertEquals(fs1.getName(),backupName);
		} catch (Exception e) {
			fail("could not add name to fileset");
		}
		
		//test valid input via single param constructor
		try {
			FileSet fs2 = new FileSet(backupName);
			assertEquals(fs2.getName(), backupName);
		} catch (Exception e) {
			fail("could not add name to fileset");
		}
		
		//test valid input via dual param constructor
		try {
			FileSet fs3 = new FileSet(backupName, dest);
			assertEquals(fs3.getName(), backupName);
		} catch (Exception e) {
			fail("could not add name to fileset");
		}
		
		//test invalid input via setter
		try {
			FileSet fs4 = new FileSet();
			fs4.setName(invalidBackupName);
			fail("Should not accept backup names with prohibited characters");
		} catch (Exception e) {
			assert(true);
		}
		
		//test invalid input via single param constructor
		try {
			@SuppressWarnings("unused")
			FileSet fs5 = new FileSet(invalidBackupName);
			fail("Should not accept backup names with prohibited characters");
		} catch (Exception e) {
			assert(true);
		}
		
		//test invalid input via single param constructor
		try {
			@SuppressWarnings("unused")
			FileSet fs6 = new FileSet(invalidBackupName, dest);
			fail("Should not accept backup names with prohibited characters");
		} catch (Exception e) {
			assert(true);
		}
	}
	
	@Test
	public void testSave() {
		/**
		 * TODO - what is required of the absolutePath?
		 * Could we not use a predetermined location in the filesystem, and use the "name"
		 * of the file set to be the end file?
		 */
		
		//TODO - Pass empty file set to be saved - desired behavior?
		FileSet fs1 = new FileSet();
		try {
			FileSet.save(fsPath, fs1);
			assert(true);
		} catch (IOException e) {
			fail("Could not save the file set");
		}
		
		//TODO - Pass named file set to be saved - desired behavior?
		FileSet fs2;
		try {
			fs2 = new FileSet(backupName);
			try {
				FileSet.save(fsPath, fs2);
				assert(true);
			} catch (IOException e) {
				fail("Could not save the file set");
			}
		} catch (Exception e1) {
			e1.printStackTrace();
			fail("Could not create the file set");
		}
		
		//TODO - Pass fully parameterized empty file set to be saved - desired behavior?
		FileSet fs3;
		try {
			fs3 = new FileSet(backupName, dest);
			try {
				FileSet.save(fsPath, fs3);
				assert(true);
			} catch (IOException e) {
				fail("Could not save the file set");
			}
		} catch (Exception e1) {
			e1.printStackTrace();
			fail("Could not create the file set");
		}
		
		//Pass fully parameterized file set with files to be saved
		FileSet fs4;
		try {
			fs4 = new FileSet(backupName, dest);
			fs4.addElement(source);
			fs4.addElement(sourceb);
			try {
				FileSet.save(fsPath, fs4);
				assert(true);
			} catch (IOException e) {
				fail("Could not save the file set");
			}
		} catch (Exception e1) {
			e1.printStackTrace();
			fail("Could not create the file set");
		}
	}
	
	//TODO - implement read() tests
}

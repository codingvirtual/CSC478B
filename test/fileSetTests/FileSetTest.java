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
	private String invalidFsPathDir = "/Users/zackburch/Desktop/invalid";
	private String invalidFsPathFile = "/Users/zackburch/Desktop/test/testpath.txt";
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
		
		//test save empty file set to be saved
		FileSet fs1 = new FileSet();
		try {
			FileSet.save(fsPath, fs1);
			fail("FileSet is required to have a name");
		} catch (IOException e) {
			assert(true);
		}
		
		// test save named file set at valid location
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
		
		//test save named file set at non-existent directory
		FileSet fs3;
		try {
			fs3 = new FileSet(backupName);
			try {
				FileSet.save(invalidFsPathDir, fs3);
				fail("Directory does not exist, should refuse save");
			} catch (IOException e) {
				assert(true);
			}
		} catch (Exception e1) {
			e1.printStackTrace();
			fail("Could not create the file set");
		}
		
		// test save named file set at "file" path
		FileSet fs4;
		try {
			fs4 = new FileSet(backupName);
			try {
				FileSet.save(invalidFsPathFile, fs4);
				fail("Filepath should be a directory, not File");
			} catch (IOException e) {
				assert(true);
			}
		} catch (Exception e1) {
			e1.printStackTrace();
			fail("Could not create the file set");
		}
		
		//test save fully parameterized empty file set at valid location
		FileSet fs5;
		try {
			fs5 = new FileSet(backupName, dest);
			try {
				FileSet.save(fsPath, fs5);
				assert(true);
			} catch (IOException e) {
				fail("Could not save the file set");
			}
		} catch (Exception e1) {
			e1.printStackTrace();
			fail("Could not create the file set");
		}
		
		//test save fully parameterized empty file set at non-existent directory
		FileSet fs6;
		try {
			fs6 = new FileSet(backupName, dest);
			try {
				FileSet.save(invalidFsPathDir, fs6);
				fail("Directory does not exist, should refuse save");
			} catch (IOException e) {
				assert(true);
			}
		} catch (Exception e1) {
			e1.printStackTrace();
			fail("Could not create the file set");
		}
		
		//test save fully parameterized empty file set at "file" path
		FileSet fs7;
		try {
			fs7 = new FileSet(backupName, dest);
			try {
				FileSet.save(invalidFsPathFile, fs7);
				fail("Filepath should be a directory, not File");
			} catch (IOException e) {
				assert(true);
			}
		} catch (Exception e1) {
			e1.printStackTrace();
			fail("Could not create the file set");
		}
		
		//test save fully parameterized file set with files at valid location
		FileSet fs8;
		try {
			fs8 = new FileSet(backupName, dest);
			fs8.addElement(source);
			fs8.addElement(sourceb);
			try {
				FileSet.save(fsPath, fs8);
				assert(true);
			} catch (IOException e) {
				fail("Could not save the file set");
			}
		} catch (Exception e1) {
			e1.printStackTrace();
			fail("Could not create the file set");
		}
		
		//test save fully parameterized file set with files at non-existent directory
		FileSet fs9;
		try {
			fs9 = new FileSet(backupName, dest);
			fs9.addElement(source);
			fs9.addElement(sourceb);
			try {
				FileSet.save(invalidFsPathDir, fs9);
				fail("Directory does not exist, should refuse save");
			} catch (IOException e) {
				assert(true);
			}
		} catch (Exception e1) {
			e1.printStackTrace();
			fail("Could not create the file set");
		}
		
		//test save fully parameterized file set with files at "file" path
		FileSet fs10;
		try {
			fs10 = new FileSet(backupName, dest);
			fs10.addElement(source);
			fs10.addElement(sourceb);
			try {
				FileSet.save(fsPath, fs10);
				fail("Filepath should be a directory, not File");
			} catch (IOException e) {
				assert(true);
			}
		} catch (Exception e1) {
			e1.printStackTrace();
			fail("Could not create the file set");
		}
	}
	
	@Test
	public void testRead() {
		
		
	}
}

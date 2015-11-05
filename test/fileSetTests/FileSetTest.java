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
	
	//Need to rename these to tester's file system
	private String source = "/Users/zackburch/Desktop/test/test.txt";
	private String sourceb = "/Users/zackburch/Desktop/test/test2.txt";
	private String sourceFail = "/Users/zackburch/Desktop/testFail.txt";
	private String dest = "Users/zackburch/Google Drive/Test/";
	private String destb = "Users/zackburch/Google Drive/Test2/";
	private String destFail = "Users/zackburch/GoogleDrive/TestFail/";
	private String fsPathDir = "/Users/zackburch/Desktop/test/";
	private String invalidFsPathDir = "/Users/zackburch/Desktop/invalid/";
	private String fsPathFile = "/Users/zackburch/Desktop/test/testpath.txt";
	
	//Backup "name" test variables
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
		FileSet fs = new FileSet();
		assertEquals(fs.getName(), null);
		
		//test when valid name is present
		try { 
			fs.setName(backupName);
			assertEquals(fs.getName(),backupName);
		} catch (Exception e) {
			fail("could not add name to fileset");
		}
	}
	
	@Test
	public void testSetName() {
	
		//test valid input via setter
		try {
			FileSet fs = new FileSet();
			fs.setName(backupName);
			assertEquals(fs.getName(),backupName);
		} catch (Exception e) {
			fail("could not add name to fileset");
		}
		
		//test valid input via single param constructor
		try {
			FileSet fs = new FileSet(backupName);
			assertEquals(fs.getName(), backupName);
		} catch (Exception e) {
			fail("could not add name to fileset");
		}
		
		//test valid input via dual param constructor
		try {
			FileSet fs = new FileSet(backupName, dest);
			assertEquals(fs.getName(), backupName);
		} catch (Exception e) {
			fail("could not add name to fileset");
		}
		
		//test invalid input via setter
		try {
			FileSet fs = new FileSet();
			fs.setName(invalidBackupName);
			fail("Should not accept backup names with prohibited characters");
		} catch (Exception e) {
			assert(true);
		}
		
		//test invalid input via single param constructor
		try {
			@SuppressWarnings("unused")
			FileSet fs = new FileSet(invalidBackupName);
			fail("Should not accept backup names with prohibited characters");
		} catch (Exception e) {
			assert(true);
		}
		
		//test invalid input via single param constructor
		try {
			@SuppressWarnings("unused")
			FileSet fs = new FileSet(invalidBackupName, dest);
			fail("Should not accept backup names with prohibited characters");
		} catch (Exception e) {
			assert(true);
		}
	}
	
	@Test
	public void testSave() {
		
		//test save empty file set to be saved
		try {
			FileSet fs = new FileSet();
			FileSet.save(fsPathDir, fs);
			fail("FileSet is required to have a name");
		} catch (IOException e) {
			assert(true);
		}
		
		// test save named file set at valid location
		try {
			FileSet fs = new FileSet(backupName);
			try {
				FileSet.save(fsPathDir, fs);
				assert(true);
			} catch (IOException e) {
				fail("Could not save the file set");
			}
		} catch (Exception e1) {
			e1.printStackTrace();
			fail("Could not create the file set");
		}
		
		//test save named file set at non-existent directory
		try {
			FileSet fs = new FileSet(backupName);
			try {
				FileSet.save(invalidFsPathDir, fs);
				fail("Directory does not exist, should refuse save");
			} catch (IOException e) {
				assert(true);
			}
		} catch (Exception e1) {
			e1.printStackTrace();
			fail("Could not create the file set");
		}
		
		// test save named file set at "file" path
		try {
			FileSet fs = new FileSet(backupName);
			try {
				FileSet.save(fsPathFile, fs);
				fail("Filepath should be a directory, not File");
			} catch (IOException e) {
				assert(true);
			}
		} catch (Exception e1) {
			e1.printStackTrace();
			fail("Could not create the file set");
		}
		
		//test save fully parameterized empty file set at valid location
		try {
			FileSet fs = new FileSet(backupName, dest);
			try {
				FileSet.save(fsPathDir, fs);
				assert(true);
			} catch (IOException e) {
				fail("Could not save the file set");
			}
		} catch (Exception e1) {
			e1.printStackTrace();
			fail("Could not create the file set");
		}
		
		//test save fully parameterized empty file set at non-existent directory
		try {
			FileSet fs = new FileSet(backupName, dest);
			try {
				FileSet.save(invalidFsPathDir, fs);
				fail("Directory does not exist, should refuse save");
			} catch (IOException e) {
				assert(true);
			}
		} catch (Exception e1) {
			e1.printStackTrace();
			fail("Could not create the file set");
		}
		
		//test save fully parameterized empty file set at "file" path
		try {
			FileSet fs = new FileSet(backupName, dest);
			try {
				FileSet.save(fsPathFile, fs);
				fail("Filepath should be a directory, not File");
			} catch (IOException e) {
				assert(true);
			}
		} catch (Exception e1) {
			e1.printStackTrace();
			fail("Could not create the file set");
		}
		
		//test save fully parameterized file set with files at valid location
		try {
			FileSet fs = new FileSet(backupName, dest);
			fs.addElement(source);
			fs.addElement(sourceb);
			try {
				FileSet.save(fsPathDir, fs);
				assert(true);
			} catch (IOException e) {
				fail("Could not save the file set");
			}
		} catch (Exception e1) {
			e1.printStackTrace();
			fail("Could not create the file set");
		}
		
		//test save fully parameterized file set with files at non-existent directory
		try {
			FileSet fs = new FileSet(backupName, dest);
			fs.addElement(source);
			fs.addElement(sourceb);
			try {
				FileSet.save(invalidFsPathDir, fs);
				fail("Directory does not exist, should refuse save");
			} catch (IOException e) {
				assert(true);
			}
		} catch (Exception e1) {
			e1.printStackTrace();
			fail("Could not create the file set");
		}
		
		//test save fully parameterized file set with files at "file" path
		try {
			FileSet fs = new FileSet(backupName, dest);
			fs.addElement(source);
			fs.addElement(sourceb);
			try {
				FileSet.save(fsPathDir, fs);
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
		//test with valid path argument
		try {
			FileSet fs1 = new FileSet(backupName, dest);
			fs1.addElement(source);
			fs1.addElement(sourceb);
			try {
				FileSet.save(fsPathDir, fs1);
				assert(true);
			} catch (IOException e) {
				fail("Could not save the file set");
			}
			try {
				String path = fsPathDir + backupName;
				FileSet fs2 = FileSet.read(path);
				assertEquals(fs1,fs2);
			} catch (Exception e) {
				fail("IO Error on read()");
			}
		} catch (Exception e1) {
			e1.printStackTrace();
			fail("Could not create the file set");
		}
		
		//test with invalid path argument
		try {
			FileSet fs1 = new FileSet(backupName, dest);
			fs1.addElement(source);
			fs1.addElement(sourceb);
			try {
				FileSet.save(fsPathDir, fs1);
				assert(true);
			} catch (IOException e) {
				fail("Could not save the file set");
			}
			try {
				String path = fsPathDir + backupName + "1";
				@SuppressWarnings("unused")
				FileSet fs2 = FileSet.read(path);
				fail("Should not accept non-existant filepath");
			} catch (Exception e) {
				assert(true);
			}
		} catch (Exception e1) {
			e1.printStackTrace();
			fail("Could not create the file set");
		}
	}
}

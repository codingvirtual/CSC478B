package test;

import static org.junit.Assert.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import core.FileSet;

/**
 * @author Zack Burch
 * @version 1.0
 */
public class FileSetTest {

	private String testRoot = System.getProperty("user.home");

	/**
	 * TODO: Javadoc to explain how to set up the file system for the tests
	 */
	private String source = testRoot + "/Desktop/test/test.txt";
	private String sourceb = testRoot + "/Desktop/test/test2.txt";
	private String sourceFail = testRoot + "/Desktop/testFail.txt";
	private String dest = testRoot + "/Google Drive/Test/";
	private String destb = testRoot + "/Google Drive/Test2/";
	private String destFail = "//..\\..//*//GoogleDrive/TestFail/";
	private String fsPathDir = testRoot + "/Desktop/test/test/";
	private String invalidFsPathDir = "//..\\..//*/Desktop/invalid/";

	//Backup "name" test variables
	private String backupName = "backup";
	private String invalidBackupName = "/..\\...//*";


	@Rule
	public final ExpectedException expectedException = ExpectedException.none();

	/*
	 * TEST CONSTRUCTORS
	 */


	/**
	 * Test method for {@link core.FileSet#FileSet()}.
	 * Test to ensure an empty file set can be created via an empty constructor
	 */
	@Test
	public void given_EmptyDefaultConstructor_when_Validated_then_TestSucceeds() {
		FileSet fs = new FileSet();
		assertNotNull(fs);
	}


	/**
	 * Test method for {@link core.FileSet#FileSet(java.lang.String)}.
	 * Test to ensure a named fileset can be created when given a valid fileset
	 * name to the constructor
	 */
	@Test
	public void given_ValidFileSetName_when_CreatingFileSet_then_Success() {
		try {
			FileSet fs = new FileSet(backupName);
			assertNotNull(fs);
		} catch (Exception e) {
			fail("Could not set up fileset with name");
			e.printStackTrace();
		}
	}


	/**
	 * Test method for {@link core.FileSet#FileSet(java.lang.String, java.lang.String)}.
	 * Test to ensure a named fileset with a destination can be created when 
	 * given a valid fileset name and valid destination to the constructor
	 */
	@Test
	public void given_ValidDestValidName_when_Validated_then_TestSucceeds() {
		try {
			FileSet fs = new FileSet(backupName, dest);
			assertNotNull(fs);
		} catch (Exception e) {
			fail("Could not set up fileset with name and destination");
			e.printStackTrace();
		}
	}


	/*
	 * TEST CASES FOR VALID FILE NAME
	 */
	

	/**
	 * Test to ensure validFileName() works correctly for valid backup names
	 */
	@Test
	public void given_valid_fileName_when_validated_then_testPasses() {
		try {
			assertTrue(FileSet.validFileName(backupName));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Test to ensure validFileName() throws exception for invalid backup names
	 */
	@Test
	public void given_invalid_fileName_when_validated_then_testFails() throws Exception{
		expectedException.expect(IOException.class);
		System.out.println(FileSet.validFileName(invalidBackupName));
	}

	/*
	 * TEST ADD PATH
	 */


	/**
	 * Test method for {@link core.FileSet#addPath(java.lang.String)}.
	 * Test addElement() works correctly when adding a valid path
	 */
	@Test
	public void given_ValidPath_when_AddingToFileSet_then_ExceptionShouldntOccur() {
		FileSet fs = new FileSet();
		//test adding a file
		fs.addElement(source);
		assertTrue(fs.contains(source));
	}
	

	/**
	 * Test method for {@link core.FileSet#addPath(java.lang.String)}.
	 * Test addElement() throws exception when adding an invalid path
	 */
	@Test
	public void given_InvalidPath_when_AddingToFileSet_then_ExceptionShouldOccur() throws Exception{
		FileSet fs = new FileSet();

		//test adding an invalid filename, which should throw an exception
		expectedException.expect(IllegalArgumentException.class);
		fs.addElement(sourceFail);
	}
	

	/**
	 * Test method for {@link core.FileSet#addPath(java.lang.String)}.
	 * Test addElement() does not add the same path twice when added sequentially
	 */
	@Test
	public void given_ValidPath_when_AddingItTwice_then_DuplicateShouldntOccur() {
		FileSet fs = new FileSet();

		//test adding a file
		fs.addElement(source);
		assertTrue(fs.contains(source));

		//test for duplicate file paths
		fs.addElement(source);
		assertFalse(fs.getSize() > 1);
	}
	

	/**
	 * Test method for {@link core.FileSet#addPath(java.lang.String)}.
	 * Test addElement() does not add the same path twice when added with second
	 * file in between
	 */
	@Test
	public void given_ValidPath_when_AddingItTwiceWithValidFileBetween_then_DuplicateShouldntOccur() {
		FileSet fs = new FileSet();

		//test adding a file
		fs.addElement(source);
		assertTrue(fs.contains(source));

		//test adding second file
		fs.addElement(sourceb);
		assertTrue(fs.contains(sourceb));

		//test for duplicate file paths
		fs.addElement(source);
		assertFalse(fs.getSize() > 2);
	}

	//TEST REMOVE PATH
	

	/**
	 * Test method for {@link core.FileSet#removePath(java.io.File)}.
	 * Test an existing file can be removed from a fileset via removeElement()
	 */
	@Test
	public void given_ExistingFile_when_RemovingPath_then_TestSucceeds() {
		FileSet fs = new FileSet();
		fs.addElement(source);
		assertTrue(fs.size() == 1);
		fs.removeElement(source);
		assertFalse(fs.contains(source));
	}


	/**
	 * Test exception is thrown when attempting to remove a non-existing file 
	 * from a fileset
	 */
	@Test
	public void given_EmptyFileSet_when_RemovingFile_then_FileSetRemainsEmpty(){
		FileSet fs = new FileSet();
		try {
			fs.removeElement(source);
		} catch (Exception e) {
			assertTrue(e.getMessage().endsWith("is not in the FileSet"));
		} finally {
			assertTrue(fs.getSize() == 0);
		}
	}

	//TEST GET DESTINATION
	

	/**
	 * Test method for {@link core.FileSet#getDestination()}.
	 * Test that null is returned when requesting the destination path from
	 * a FileSet without a destination assigned
	 */
	@Test
	public void given_EmptyFileSet_when_GetDestination_then_NullReturned() {
		FileSet fs = new FileSet();
		assertEquals(fs.getDestination(), null);
		try {
			fs.setDestination(dest);
		} catch (Exception e) {
			e.printStackTrace();
		}
		assertEquals(fs.getDestination()+"/", dest);
	}


	/**
	 * Test destination is returned successfully via getDestination() when a FileSet
	 * contains a valid destination
	 */
	@Test
	public void given_FileSetWithDest_when_GetDestination_then_DestinationReturned(){
		FileSet fs = new FileSet();
		try {
			fs.setDestination(dest);
		} catch (Exception e) {
			e.printStackTrace();
		}
		assertEquals(fs.getDestination()+"/", dest);
	}


	//TEST SET DESTINATION

	/**
	 * Test method for {@link core.FileSet#setDestination(java.lang.String)}.
	 * Test that setDestination() completes successfully when a valid path is given
	 */
	@Test
	public void given_EmptyFileSet_when_SetDestWithValidDest_then_TestSucceeds() {
		FileSet fs = new FileSet();
		try {
			fs.setDestination(dest);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		assertEquals(fs.getDestination()+"/", dest);
	}


	/**
	 * Test that setDestination() completes successfully when a valid path is given
	 * to a FileSet that already has a destination, and that the new destination
	 * is returned upon calling getDestination()
	 */
	@Test
	public void given_FileSetWithDest_when_SetDestWithNewValidDest_then_TestSucceeds() {
		FileSet fs = new FileSet();
		//test set first destination
		try {
			fs.setDestination(dest);
		} catch (Exception e1) {
			e1.printStackTrace();
			fail("could not set destination");
		}
		assertEquals(fs.getDestination()+"/", dest);

		//test set replacement destination
		try {
			fs.setDestination(destb);
		} catch (Exception e1) {
			e1.printStackTrace();
			fail("Could not replace destination");
		}
		assertEquals(fs.getDestination()+"/", destb);
	}


	/**
	 * Test that exception is thrown when an invalid path is passed to 
	 * setDestination()
	 */
	@Test
	public void given_InvalidDestination_when_SetDest_then_Exception() throws Exception{
		FileSet fs = new FileSet();
		//test adding invalid destination does not replace old destination
		expectedException.expect(IOException.class);
		fs.setDestination(destFail);
	}

	/*
	 * TEST GET NAME
	 */


	/**
	 * Test that null is returned when calling getName() on an empty FileSet
	 */
	@Test
	public void given_EmptyFileSet_when_GetName_then_ReturnsNull() {

		//test null in empty constructor
		FileSet fs = new FileSet();
		assertEquals(fs.getName(), null);
	}


	/**
	 * Test that a name is returned when calling getName() on a FileSet
	 * with a valid name
	 */
	@Test
	public void given_FileSetWithName_when_GetName_then_ReturnsName() {
		FileSet fs = new FileSet();
		try { 
			fs.setName(backupName);
			assertEquals(fs.getName(),backupName);
		} catch (Exception e) {
			fail("could not add name to fileset");
		}
	}

	/*
	 * TEST SET NAME
	 */


	/**
	 * Test setName() successfully stores valid backup name
	 */
	@Test
	public void given_FileSetWithValidName_when_SetBySetName_then_TestSucceeds() {

		//test valid input via setter
		try {
			FileSet fs = new FileSet();
			fs.setName(backupName);
			assertEquals(fs.getName(),backupName);
		} catch (Exception e) {
			fail("could not add name to fileset");
		}
	}


	/**
	 * Test passing only a valid backup name as parameter to constructor successfully 
	 * stores backup name
	 */
	@Test
	public void given_FileSetWithValidName_when_SetBySingleParamConstructor_then_TestSucceeds() {

		//test valid input via single param constructor
		try {
			FileSet fs = new FileSet(backupName);
			assertEquals(fs.getName(), backupName);
		} catch (Exception e) {
			fail("could not add name to fileset");
		}
	}


	/**
	 * Test passing a valid backup name AND destination as parameters to constructor 
	 * successfully stores backup name
	 */
	@Test
	public void given_FileSetWithValidName_when_SetByDualParamConstructor_then_TestSucceeds() {	
		//test valid input via dual param constructor
		try {
			FileSet fs = new FileSet(backupName, dest);
			assertEquals(fs.getName(), backupName);
		} catch (Exception e) {
			fail("could not add name to fileset");
		}
	}


	/**
	 * Test passing an invalid backup name to setName() results in exception
	 */
	@Test
	public void given_FileSetWithInvalidName_when_SetBySetName_then_Exception() throws Exception{		
		//test invalid input via setter
		FileSet fs = new FileSet();
		expectedException.expect(IOException.class);
		fs.setName(invalidBackupName);
	}

	/*
	 * TEST SAVE
	 */


	/**
	 * Test saving a valid named fileset object to a valid path succeeds
	 */
	@Test
	public void given_ValidPath_when_InvokeFileSetSave_then_Success() {

		String fullPathToFileSet = fsPathDir + backupName;

		try {
			FileSet fs = null;
			try {
				fs = new FileSet(backupName);
			} catch (Exception e) {
				// This should not happen.
				fail("Error creating FileSet in test. This should not happen.");
				e.printStackTrace();
			}
			FileSet.save(fullPathToFileSet, fs);
			assertTrue(Files.exists(Paths.get(fullPathToFileSet)));
		} catch (IOException e) {
			e.printStackTrace();
			fail("Error writing file");
		}
	}


	/**
	 * Test saving a valid empty fileset object to a valid path succeeds
	 */
	@Test
	public void given_EmptyFileSet_when_Saved_then_TestSucceeds() {

		String fullPathToFileSet = fsPathDir + backupName;

		//test save empty file set to be saved
		try {
			FileSet fs = new FileSet();
			FileSet.save(fullPathToFileSet, fs);
			assertTrue(true);
		} catch (IOException e) {
			fail("FileSet is required to have a name");
		}
	}


	/**
	 * Test saving a valid named fileset object to a valid path succeeds
	 */
	@Test
	public void given_NamedEmptyFileSet_when_Saved_then_TestSucceeds() {
		String fullPathToFileSet = fsPathDir + backupName;
		// test save named file set at valid location
		try {
			FileSet fs = new FileSet(backupName);
			try {
				FileSet.save(fullPathToFileSet, fs);
				assertTrue(true);
			} catch (IOException e) {
				fail("Could not save the file set");
			}
		} catch (Exception e1) {
			e1.printStackTrace();
			fail("Could not create the file set");
		}
	}


	/**
	 * Test saving a valid fully parameterized fileset object to a valid 
	 * path succeeds
	 */
	@Test
	public void given_NamedParameterizedFileSet_when_Saved_then_TestSucceeds() {
		String fullPathToFileSet = fsPathDir + backupName;
		//test save fully parameterized empty file set at valid location
		try {
			FileSet fs = new FileSet(backupName, dest);
			try {
				FileSet.save(fullPathToFileSet, fs);
				assertTrue(true);
			} catch (IOException e) {
				fail("Could not save the file set");
			}
		} catch (Exception e1) {
			e1.printStackTrace();
			fail("Could not create the file set");
		}
	}

	

	/**
	 * Test saving valid fileset object to invalid object results in exception
	 * @throws Exception
	 */
	@Test
	public void given_NamedParameterizedFileSet_when_SavedWithInvalidFileName_then_Exception() throws Exception {
		String invalidFullPathToFileSet = invalidFsPathDir;
		//test save fully parameterized empty file set at non-existent directory
	    FileSet fs = new FileSet(backupName, dest);

	    expectedException.expect(IOException.class);
	    FileSet.save(invalidFullPathToFileSet, fs);
	}


	/**
	 * Test saving a valid fully parameterized fileset object with files to a valid 
	 * path succeeds
	 */
	@Test
	public void given_NamedParameterizedFileSetWithFiles_when_Saved_then_TestSucceeds() {
		String fullPathToFileSet = fsPathDir + backupName;
		//test save fully parameterized file set with files at valid location
		try {
			FileSet fs = new FileSet(backupName, dest);
			fs.addElement(source);
			fs.addElement(sourceb);
			try {
				FileSet.save(fullPathToFileSet, fs);
				assertTrue(true);
			} catch (IOException e) {
				fail("Could not save the file set");
			}
		} catch (Exception e1) {
			e1.printStackTrace();
			fail("Could not create the file set");
		}
	}

	/*
	 * TEST READ
	 */
	

	/**
	 * Given a valid filepath where a fileset object is stored, read() returns
	 * the fileset and allows getName() to be called.
	 */
	@Test
	public void given_ValidPath_when_InvokeFileSetRead_then_Success() {

		String fullPathToFileSet = fsPathDir + backupName;
		FileSet fs = null;
		try {
			fs = FileSet.read(fullPathToFileSet);
			assertNotNull(fs);
			assertTrue(fs.getName().equals(backupName));
		} catch (Exception e) {
			e.printStackTrace();
			fail("Error reading FileSet from disk.");
		}
	}


	/**
	 * Given a valid file {@link Path} where a {@link FileSet} object is stored with files, 
	 * core.FileOps.read() returns the same {@link FileSet}
	 */
	@Test
	public void given_ValidFileSet_when_InvokeReadWithValidPath_then_TestSucceeds() {
		String fullPathToFileSet = fsPathDir + backupName;
		//test with valid path argument
		try {
			FileSet fs1 = new FileSet(backupName, dest);
			fs1.addElement(source);
			fs1.addElement(sourceb);
			try {
				FileSet.save(fullPathToFileSet, fs1);
				assertTrue(true);
			} catch (IOException e) {
				fail("Could not save the file set");
			}
			try {
				String path = fsPathDir + backupName;
				FileSet fs2 = FileSet.read(path);
				assertEquals(fs1.getName(),fs2.getName());
				assertEquals(fs1.getDestination(),fs2.getDestination());
				assertEquals(fs1.getSize(), fs2.getSize());
				for(int i=0;i<fs1.getSize();i++){
					assertEquals(fs1.get(i),fs2.get(i));
				}
			} catch (Exception e) {
				fail("IO Error on read()");
			}
		} catch (Exception e1) {
			e1.printStackTrace();
			fail("Could not create the file set");
		}
	}
	

	/**
	 * Given an invalid path, read() throws an exception
	 * @throws Exception
	 */
	@Test
	public void given_ValidFileSet_when_InvokeReadWithInvalidPath_then_Exception() throws Exception{
		String fullPathToFileSet = fsPathDir + backupName;
		//test with invalid path argument
		//try {
			FileSet fs1 = new FileSet(backupName, dest);
			fs1.addElement(source);
			fs1.addElement(sourceb);
			try {
				FileSet.save(fullPathToFileSet, fs1);
				assertTrue(true);
			} catch (IOException e) {
				fail("Could not save the file set");
			}
			
			String path = fsPathDir + backupName + "1";
			expectedException.expect(IOException.class);
			@SuppressWarnings("unused")
			FileSet fs2 = FileSet.read(path);
	}
}

/**
 * 
 */
package fileSetTests;

import static org.junit.Assert.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import core.FileSet;

/**
 * @author zackburch
 *
 */

public class FileSetTest {

	private String testRoot = System.getProperty("user.home");
//	private String testRoot = "Q:";

	//Need to rename these to tester's file system
	private String source = testRoot + "/Desktop/test/test.txt";
	private String sourceb = testRoot + "/Desktop/test/test2.txt";
	private String sourceFail = testRoot + "/Desktop/testFail.txt";
	private String dest = testRoot + "/Google Drive/Test/";
	private String destb = testRoot + "/Google Drive/Test2/";
	private String destFail = "Q:/GoogleDrive/TestFail/";
	private String destFail2 = "Q:/GoogleDrive/TestFail//..\\...//*";
	private String fsPathDir = testRoot + "/Desktop/test/test/";
	private String invalidFsPathDir = "Q:/Desktop/invalid/";
	private String invalidFsPathDir2 = "Q:/Desktop//..\\...//*";
	private String fsPathFile = testRoot + "/Desktop/test/testpath.txt";

	//Backup "name" test variables
	private String backupName = "backup";
	private String invalidBackupName = "/..\\...//*";


	@Rule
	public final ExpectedException expectedException = ExpectedException.none();
	
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
	 * TEST CONSTRUCTORS
	 */

	/**
	 * Test method for {@link core.FileSet#FileSet()}.
	 */
	@Test
	public void given_EmptyDefaultConstructor_when_Validated_then_TestSucceeds() {
		FileSet fs = new FileSet();
		assertNotNull(fs);
	}

	/**
	 * Test method for {@link core.FileSet#FileSet(java.lang.String)}.
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
	 * TEST VALID FILE NAME
	 */
	@Test
	public void given_valid_fileName_when_validated_then_testPasses() {
		try {
			assertTrue(FileSet.validFileName(backupName));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void given_invalid_fileName_when_validated_then_testFails() {
		try {
			System.out.println(FileSet.validFileName(invalidBackupName));
			assertFalse(FileSet.validFileName(invalidBackupName));
		} catch (IOException e) {
			assert(true);
		}
	}

	/*
	 * TEST ADD PATH
	 */

	/**
	 * Test method for {@link core.FileSet#addPath(java.lang.String)}.
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
	 */
	@Test
	public void given_InvalidPath_when_TryingToAddToFileSet_then_ExceptionShouldOccur() {
		FileSet fs = new FileSet();

		try {
			//test adding an invalid filename
			fs.addElement(sourceFail);
			assertFalse(fs.contains(sourceFail));
			fail("Adding an invalid filename or path should have caused an excpetion.");
		} catch (Exception e) {
			assertTrue(true);
		}
	}

	/**
	 * Test method for {@link core.FileSet#addPath(java.lang.String)}.
	 */
	@Test
	public void given_InvalidPath_when_AddingToFileSet_then_ExceptionShouldOccur() {
		FileSet fs = new FileSet();

		//test adding an invalid filename, which should throw an exception
		try {
			fs.addElement(sourceFail);
			assertTrue(false);
		} catch (Exception e) {
			assertFalse(fs.contains(sourceFail));
			assertTrue(true);
		}
	}

	/**
	 * Test method for {@link core.FileSet#addPath(java.lang.String)}.
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

	/*TEST REMOVE PATH

	/**
	 * Test method for {@link core.FileSet#removePath(java.io.File)}.
	 */
	@Test
	public void given_ExistingFile_when_RemovingPath_then_TestSucceeds() {
		FileSet fs = new FileSet();
		fs.addElement(source);
		assertTrue(fs.size() == 1);
		fs.removeElement(source);
		assertFalse(fs.contains(source));
	}

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

	@Test
	public void given_InvalidDestination_when_SetDest_then_Exception(){
		FileSet fs = new FileSet();
		//test adding invalid destination does not replace old destination
		try { 
			fs.setDestination(destFail);
		} catch (Exception e){
			assertFalse(fs.getDestination() == destFail);
		}
	}

	/*
	 * TEST GET NAME
	 */

	@Test
	public void given_EmptyFileSet_when_GetName_then_ReturnsNull() {

		//test null in empty constructor
		FileSet fs = new FileSet();
		assertEquals(fs.getName(), null);
	}

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

	@Test
	public void given_FileSetWithInvalidName_when_SetBySetName_then_Exception() {		
		//test invalid input via setter
		try {
			FileSet fs = new FileSet();
			fs.setName(invalidBackupName);
			fail("Should not accept backup names with prohibited characters");
		} catch (Exception e) {
			assert(true);
		}
	}

	/*
	 * TEST SAVE
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

	@Test
	public void given_EmptyFileSet_when_Saved_then_TestSucceeds() {

		String fullPathToFileSet = fsPathDir + backupName;

		//test save empty file set to be saved
		//TODO - Null Pointer on save()
		try {
			FileSet fs = new FileSet();
			FileSet.save(fullPathToFileSet, fs);
			assertTrue(true);
		} catch (IOException e) {
			fail("FileSet is required to have a name");
		}
	}

	@Test
	public void given_NamedEmptyFileSet_when_Saved_then_TestSucceeds() {
		String fullPathToFileSet = fsPathDir + backupName;
		// test save named file set at valid location
		//TODO - Null Pointer on save()
		try {
			FileSet fs = new FileSet(backupName);
			try {
				FileSet.save(fullPathToFileSet, fs);
				assert(true);
			} catch (IOException e) {
				fail("Could not save the file set");
			}
		} catch (Exception e1) {
			e1.printStackTrace();
			fail("Could not create the file set");
		}
	}

	//	@Test
	//	public void given_NamedEmptyFileSet_when_SavedToInvalidDir_then_Exception() {
	//		String invalidFullPathToFileSet = invalidFsPathDir + backupName;
	//
	//		//test save named file set at non-existent directory
	//		// TODO: the FileSet.save() method will create any directories it needs
	//		// to in order to save the FileSet. The only way to make this test fail
	//		// is to give it a path that has invalid characters in it. We can probably
	//		// remove this test.
	//		try {
	//			FileSet fs = new FileSet(backupName);
	//			try {
	//				FileSet.save(invalidFullPathToFileSet, fs);
	//				fail("Directory does not exist, should refuse save");
	//			} catch (IOException e) {
	//				assert(true);
	//			}
	//		} catch (Exception e1) {
	//			e1.printStackTrace();
	//			fail("Could not create the file set");
	//		}
	//	}

	@Test
	public void given_NamedParameterizedFileSet_when_Saved_then_TestSucceeds() {
		String fullPathToFileSet = fsPathDir + backupName;
		//test save fully parameterized empty file set at valid location
		//TODO - Null Pointer on save()
		try {
			FileSet fs = new FileSet(backupName, dest);
			try {
				FileSet.save(fullPathToFileSet, fs);
				assert(true);
			} catch (IOException e) {
				fail("Could not save the file set");
			}
		} catch (Exception e1) {
			e1.printStackTrace();
			fail("Could not create the file set");
		}
	}

	
	@Test
	public void given_NamedParameterizedFileSet_when_SavedWithInvalidFileName_then_Exception() throws Exception {
		String invalidFullPathToFileSet = invalidFsPathDir;
		//test save fully parameterized empty file set at non-existent directory
	    FileSet fs = new FileSet(backupName, dest);

	    expectedException.expect(IOException.class);
	    FileSet.save(invalidFullPathToFileSet, fs);
	}

	@Test
	public void given_NamedParameterizedFileSetWithFiles_when_Saved_then_TestSucceeds() {
		String fullPathToFileSet = fsPathDir + backupName;
		//test save fully parameterized file set with files at valid location
		//TODO - Null Pointer on save()
		try {
			FileSet fs = new FileSet(backupName, dest);
			fs.addElement(source);
			fs.addElement(sourceb);
			try {
				FileSet.save(fullPathToFileSet, fs);
				assert(true);
			} catch (IOException e) {
				fail("Could not save the file set");
			}
		} catch (Exception e1) {
			e1.printStackTrace();
			fail("Could not create the file set");
		}
	}

	//	@Test
	// TODO: This test will always fail as written since FileSet.save() will create any directories
	// it needs to in order to save the FileSet. It may be better to structure this test with a Windows
	// path that doesn't exist (like one that starts with Q:\test).

	//	public void given_NamedParameterizedFileSetWithFiles_when_SavedWithInvalidPath_then_Exception() {	
	//		String invalidFullPathToFileSet = invalidFsPathDir + backupName;
	//		try {
	//			FileSet fs = new FileSet(backupName, dest);
	//			fs.addElement(source);
	//			fs.addElement(sourceb);
	//			try {
	//				FileSet.save(invalidFullPathToFileSet, fs);
	//				fail("Directory does not exist, should refuse save");
	//			} catch (IOException e) {
	//				assert(true);
	//			}
	//		} catch (Exception e1) {
	//			e1.printStackTrace();
	//			fail("Could not create the file set");
	//		}
	//	}

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

	/*
	 * TEST READ
	 */
	@Test
	public void given_ValidFileSet_when_InvokeReadWithValidPath_then_TestSucceeds() {
		String fullPathToFileSet = fsPathDir + backupName;
		//test with valid path argument
		//TODO - Null Pointer on save()
		try {
			FileSet fs1 = new FileSet(backupName, dest);
			fs1.addElement(source);
			fs1.addElement(sourceb);
			try {
				FileSet.save(fullPathToFileSet, fs1);
				assert(true);
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
	@Test
	public void given_ValidFileSet_when_InvokeReadWithInvalidPath_then_Exception() {
		String fullPathToFileSet = fsPathDir + backupName;
		//test with invalid path argument
		try {
			FileSet fs1 = new FileSet(backupName, dest);
			fs1.addElement(source);
			fs1.addElement(sourceb);
			try {
				FileSet.save(fullPathToFileSet, fs1);
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

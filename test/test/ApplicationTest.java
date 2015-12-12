package test;

import static org.junit.Assert.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import app.Application;
import core.FileSet;

/** Provides tests of functionality of the main Application class
*
* @author Greg Palen
* @version 1.0
* @see Application
*
*/
public class ApplicationTest {
	
	Path defaultFSPath = Paths.get(System.getProperty("user.home"));


	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		defaultFSPath = defaultFSPath.resolve("Mirror");
		defaultFSPath = defaultFSPath.resolve("DefaultFileSet");
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	/**
	 * Test method for {@link app.Application#Application()}.
	 * @throws IOException 
	 */
	@Test
	public void testApplicationConstructor() throws Exception {
		Application app;
		// First, delete any existing default file set, which would be located in the user's
		// home directory within a sub-directory named "Mirror" and have the name "DefaultFileSet"
		Path defaultFSPath = Paths.get(System.getProperty("user.home"));
		defaultFSPath = defaultFSPath.resolve("Mirror");
		defaultFSPath = defaultFSPath.resolve("DefaultFileSet");
		Files.deleteIfExists(defaultFSPath);
		
		assertFalse(Files.exists(defaultFSPath));
		try {
			// Since the default file set was deleted, the constructor should create a new
			// default file set with the name "DefaultFileSet" and no destination (destination is null);
			app = new Application();
			assertNotNull(app);
			assertTrue(app.getCurrentFileSet().getName().equals("DefaultFileSet"));
			assertNull(app.getCurrentFileSet().getDestination());
			// Also, the list of paths to back up should be empty.
			assertEquals(app.getCurrentFileSet().getSize(), 0);
		} catch (Exception e) {
			e.printStackTrace();
			fail("Unable to create new Application object");
		}

	}

	/**
	 * Test method for {@link app.Application#getCurrentFileSet()}.
	 */
	@Test
	public void testGetCurrentFileSet() throws Exception {

		FileSet fs = null;
		try {
			fs = new Application().getCurrentFileSet();
			assertNotNull(fs);
			assertTrue(fs.getName().equals("DefaultFileSet"));
		} catch (Exception e) {
			fail("Exception trying to create Application object while testing"
					+ "getCurrentFileSet.");
			e.printStackTrace();
		}
	}

	/**
	 * Test method for {@link app.Application#setCurrentFileSet(core.FileSet)}.
	 * @throws  
	 */
	@Test
	public void testSetCurrentFileSet() throws Exception {
		Application app = null;
		FileSet fs = null;
		FileSet fs2 = null;
		
		try {
			fs = new FileSet("testFileSet");
		} catch (Exception e) {
			fail("This failure should not occur.");
			e.printStackTrace();
		}
		try {
			app = new Application();
		} catch (Exception e) {
			fail("This failure should not occur.");
			e.printStackTrace();
		}
		app.setCurrentFileSet(fs);
		try {
			fs2 = app.getCurrentFileSet();
		} catch (Exception e) {
			e.printStackTrace();
			fail("Exception while trying to get current"
					+ "file set.");
		}
		assertNotNull(fs2);
		assertEquals(fs, fs2);
	}

	@Test
	public void testSaveDefaultFileSet() throws Exception {
		// Delete any default file set that may exist
		deleteDefaultFileSet();
		// Now create the application. Since there is not presently a default file set, the App
		// will create a new, blank fileSet
		Application app = new Application();
		FileSet fileSet = app.getCurrentFileSet();
		assertNotNull(fileSet);
		fileSet.setName("Testing");
		fileSet.setDestination(System.getProperty("user.home"));
		app.saveDefaultFileSet();
		assertTrue(Files.exists(defaultFSPath));
		FileSet newFileSet = FileSet.read(defaultFSPath.toString());
		assertNotNull(newFileSet);
		assertEquals(fileSet.getName(), newFileSet.getName());
		assertEquals(fileSet.getDestination(), newFileSet.getDestination());
		
	}
	
	private void deleteDefaultFileSet() {
		// Deletes any existing default file set, which would be located in the user's
		// home directory within a sub-directory named "Mirror" and have the name "DefaultFileSet"
		try {
			Files.deleteIfExists(defaultFSPath);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			assertFalse(Files.exists(defaultFSPath));
		}
	}
	
}

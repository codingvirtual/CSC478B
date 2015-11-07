/**
 *
 * @author Greg Palen
 * @version 0.1.0
 *
 * <h3>Revision History</h3>
 * <p>
 * 0.1.0	GP	Initial revision
 * 
 * </p>
 */
package ApplicationTests;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import app.Application;
import core.FileSet;

/**
 * @author Greg
 *
 */
public class ApplicationTest {

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
	 * Test method for {@link app.Application#Application()}.
	 */
	@Test
	public void testApplicationConstructor() {
		Application app;
		try {
			app = new Application();
			assertNotNull(app);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail("Unable to create new Application object");
		}

	}

	/**
	 * Test method for {@link app.Application#getCurrentFileSet()}.
	 */
	@Test
	public void testGetCurrentFileSet() {
		// TODO: is there a more robust test here? Since creating the
		// Application object will create a new, empty FileSet if it
		// can't read an existing default FileSet, this test should
		// pretty much always pass. Could consider doing a setCurrentFileSet
		// first within this block and then see if getCurrentFileSet
		// retrieves it properly, but that assumes that setCurrentFileSet
		// works properly.
		FileSet fs = null;
		try {
			fs = new Application().getCurrentFileSet();
			assertNotNull(fs);
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
	public void testSetCurrentFileSet() {
		Application app = null;
		FileSet fs = null;
		FileSet fs2 = null;
		
		try {
			fs = new FileSet("testFileSet");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			fail("This failure should not occur.");
			e.printStackTrace();
		}
		try {
			app = new Application();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			fail("This failure should not occur.");
			e.printStackTrace();
		}
		app.setCurrentFileSet(fs);
		try {
			fs2 = app.getCurrentFileSet();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail("Exception while trying to get current"
					+ "file set.");
		}
		assertNotNull(fs2);
		assertEquals(fs, fs2);
	}

	@Test
	public void testSaveDefaultFileSet() throws Exception {
		Application app = new Application();
		app.saveDefaultFileSet();
		
	}
}

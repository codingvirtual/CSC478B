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
package fileOpsTests;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Observable;
import java.util.Observer;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import core.FileSet;
import fileops.FileOps;

/**
* @author Greg
*
*/
public class FileOpsTest implements Observer {

	private FileSet files = new FileSet();
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		files.setName("Copy");
		files.setDestination("/Users/Greg/Google Drive/");
		files.addElement("/Users/Greg/Documents/Udacity Classes/Developing Scalable Apps/ud859-master/Lesson_2/00_Conference_Central/src/main/webapp/index.html");
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	/**
	 * Test method for {@link fileops.FileOps#FileOps(core.FileSet)}.
	 */
	@Test
	public void testFileOpsCreation() {
		FileOps testOps = new FileOps(this.files);
		assertEquals(this.files, testOps.getFilesToCopy());
	}

	/**
	 * Test method for {@link fileops.FileOps#run()}.
	 */
	@Test
	public void testRun() {
		FileOps testOps = new FileOps(this.files);
		try {
			testOps.run();
			assertTrue(true);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			fail("Running FileOps failed");
			e.printStackTrace();
		}
	}

	/* (non-Javadoc)
	 * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
	 */
	@Override
	public void update(Observable o, Object arg) {
		FileOps.Progress progress = (FileOps.Progress) arg;
		assert(true);
	}
}
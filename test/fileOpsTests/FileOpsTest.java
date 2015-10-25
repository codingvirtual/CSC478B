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

import fileops.FileOps;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import core.FileSet;
import fileops.FileOps;

/**
* @author Greg
*
*/
public class FileOpsTest {

	private FileSet files = new FileSet();
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		files.setDestination("/Users/Greg/Google Drive/Copy");
		files.addPath("/Users/Greg/Documents/Udacity Classes/Developing Scalable Apps/ud859-master/Lesson_2/00_Conference_Central/src/main/webapp/index.html");
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
			fail("IO Exception occurred");
			e.printStackTrace();
		}
	}
}
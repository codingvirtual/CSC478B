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

import java.io.File;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import core.FileSet;
import fileops.FileOps;
import fileops.FileOpsMessageHandler;
import fileops.Progress;

/**
* @author Greg
*
*/
public class FileOpsTest implements FileOpsMessageHandler {

	private static String testRoot = System.getProperty("user.home");
	
	private FileSet files = new FileSet();
	
	private int progressReceived = 0;
	private Boolean completionReceived = false;
	final CountDownLatch latch = new CountDownLatch(1);
	
	
	@Rule
	public TemporaryFolder tempFolder = new TemporaryFolder();
	
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		files.setName("Copy");
		File path = tempFolder.getRoot();
		String dest = path.toString();
		files.setDestination(dest);
		files.addElement(testRoot + "/Desktop/test/test.txt");
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	/* (non-Javadoc)
	 * @see fileops.FileOpsMessageHandler#handleProgress(java.util.List)
	 */
	@Override
	public void handleProgress(List<Progress> progressItems) {
		progressReceived = progressItems.size();
		System.out.println("progress received");
		for (Progress progress : progressItems) {
			System.out.println(progress);
		}
	}

	/* (non-Javadoc)
	 * @see fileops.FileOpsMessageHandler#handleCompletion()
	 */
	@Override
	public void handleCompletion() {
		System.out.println("completionReceived");
		completionReceived = true;
		latch.countDown();
	}

	/**
	 * Test method for {@link fileops.FileOps#run()}.
	 */
	@Test
	public void testRun() {
		FileOps testOps = new FileOps(this.files, this);
		try {
			final ExecutorService threadPool = Executors.newFixedThreadPool(1);
			threadPool.submit(new Runnable() {
				public void run() {
					try {
						testOps.run();
					} catch (final Throwable e) {
						e.printStackTrace();
					}
				}
			});
			latch.await();
			System.out.println("completion: " + completionReceived);
			System.out.println("progressReceived: " + progressReceived);
			assertTrue(completionReceived);
			assertTrue(progressReceived > 0);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			fail("Running FileOps failed");
			e.printStackTrace();
		}
	}
}

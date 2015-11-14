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
	private String fileName = testRoot + "/Desktop/test/test.txt";
	
	
	
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
	 * @throws Exception 
	 */
	@Test
	public void given_FullyParameterizedFileSetWithFile_when_TestRun_TestSucceeds() throws Exception {
		FileSet files = new FileSet();
		try {
			files.setName("Copy");
		} catch (Exception e1) {
			fail("Could not name FileSet");
		}
		File path = tempFolder.getRoot();
		String dest = path.toString();
		try {
			files.setDestination(dest);
		} catch (Exception e1) {
			fail("Could not set destination");
		}
		files.addElement(fileName);
		
		FileOps testOps = new FileOps(files, this);
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
	
	@Test
	public void given_CompletelyEmptyFileSet_when_RunFileOps_then_Exception() throws Exception {
		FileSet files = new FileSet();
		
		FileOps testOps = new FileOps(files, this);
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
			fail("Should not accept completely empty FileSet");
		} catch (Exception e) {
			assertTrue(true);
		}
	}
	
	@Test
	public void given_FullyParameterizedFileSetWithNoFiles_when_RunFileOps_then_Exception() throws Exception {
		FileSet files = new FileSet();
		try {
			files.setName("Copy");
		} catch (Exception e1) {
			fail("could not set fileset name");
		}
		File path = tempFolder.getRoot();
		String dest = path.toString();
		try {
			files.setDestination(dest);
		} catch (Exception e1) {
			fail("could not set fileset destination");
		}
		
		FileOps testOps = new FileOps(files, this);
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
			fail("Should not accept FileSet without files");
		} catch (Exception e) {
			assert(true);
		}
	}
	
	@Test
	public void given_NamedFileSetWithSingleFileNoDest_when_RunFileOps_then_Exception() throws Exception {
		FileSet files = new FileSet();
		try {
			files.setName("Copy");
		} catch (Exception e1) {
			fail("could not set fileset name");
		}
		
		files.addElement(fileName);
		
		FileOps testOps = new FileOps(files, this);
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
			fail("Should not accept FileSet without destination");
		} catch (Exception e) {
			assert(true);
		}
	}
	
	@Test
	public void given_UnnamedFileSetWithSingleFileAndDest_when_RunFileOps_then_Exception() throws Exception {
		FileSet files = new FileSet();
		File path = tempFolder.getRoot();
		String dest = path.toString();
		try {
			files.setDestination(dest);
		} catch (Exception e1) {
			fail("could not set fileset destination");
		}
		files.addElement(fileName);
		
		FileOps testOps = new FileOps(files, this);
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
			fail("Should not accept unnamed FileSet");
		} catch (Exception e) {
			assert(true);
		}
	}
}

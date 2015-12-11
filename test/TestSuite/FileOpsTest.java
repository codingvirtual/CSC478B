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
package TestSuite;

import static org.junit.Assert.*;

import java.io.File;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.rules.TemporaryFolder;

import core.FileSet;
import fileops.FileOps;
import fileops.FileOpsMessageHandler;
import fileops.Progress;

/**
 * @author Greg, Zack
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

	@Rule
	public final ExpectedException expectedException = ExpectedException.none();


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

	@Test
	/**
	 * Test method for {@link fileops.FileOps#run()}.
	 * Test run() completes successfully when given a valid fileset with a file
	 * @throws Exception 
	 */
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
	}

	@Test
	/**
	 * Test exception thrown if calling run() on empty fileset
	 * @throws Exception
	 */
	public void given_CompletelyEmptyFileSet_when_RunFileOps_then_Exception() throws Exception {
		FileSet files = new FileSet();

		expectedException.expect(IllegalArgumentException.class);
		FileOps testOps = new FileOps(files, this);
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
	}

	@Test
	/**
	 * Given empty fileset, validation fails and exception is thrown
	 * @throws Exception
	 */
	public void given_CompletelyEmptyFileSet_when_ValidateFileSetIsCalled_then_Exception() throws Exception {
		FileSet files = new FileSet();

		expectedException.expect(Exception.class);
		FileOps.validateFileSet(files);
	}


	@Test
	/**
	 * Test if a fully parameterized fileset without files is called to run(), an
	 * exception is thrown
	 * @throws Exception
	 */
	public void given_FullyParameterizedFileSetWithNoFiles_when_RunFileOps_then_Exception() throws Exception {
		FileSet files = new FileSet();
		files.setName("Copy");
		File path = tempFolder.getRoot();
		String dest = path.toString();
		files.setDestination(dest);

		expectedException.expect(IllegalArgumentException.class);
		FileOps testOps = new FileOps(files, this);
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
	}

	@Test
	/**
	 * Test if a named fileset with a file, but no destination is called to run(),
	 * an exception is thrown
	 * @throws Exception
	 */
	public void given_NamedFileSetWithSingleFileNoDest_when_RunFileOps_then_Exception() throws Exception {
		FileSet files = new FileSet();
		files.setName("Copy");

		files.addElement(fileName);

		expectedException.expect(IllegalArgumentException.class);
		FileOps testOps = new FileOps(files, this);
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
	}

	@Test
	/**
	 * Test if an unnamed FileSet with a destination and file is called to run(),
	 * then exception is thrown
	 * @throws Exception
	 */
	public void given_UnnamedFileSetWithSingleFileAndDest_when_RunFileOps_then_Exception() throws Exception {
		FileSet files = new FileSet();
		File path = tempFolder.getRoot();
		String dest = path.toString();
		files.setDestination(dest);
		files.addElement(fileName);

		expectedException.expect(IllegalArgumentException.class);
		FileOps testOps = new FileOps(files, this);
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
	}
}
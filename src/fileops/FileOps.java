package fileops;

import static java.nio.file.LinkOption.NOFOLLOW_LINKS;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import javax.swing.SwingWorker;
import core.FileSet;

/**
 * The FileOps class defines the operation and behavior of the entire backup operation.
 * 
 * @author Greg Palen
 * @version 0.1.0
 *
 * <h3>Revision History</h3>
 * <p>
 * 0.1.0	GP	Initial revision
 * 0.1.1	AR	Use i/o streams to set progress status
 * </p>
 */
public class FileOps extends SwingWorker<Void, Progress> {

	private final FileSet mFilesToCopy;
	private final FileOpsMessageHandler mMessageHandler;


	public FileOps(FileSet files, FileOpsMessageHandler handler) {
		mFilesToCopy = files;
		mMessageHandler = handler;
	}

	public FileOps(FileSet files) {
		mMessageHandler = null;
		mFilesToCopy = files;
	}

	/* (non-Javadoc)
	 * @see javax.swing.SwingWorker#doInBackground()
	 */
	@Override
	public Void doInBackground() throws Exception {
		System.out.println("starting backup");

		final long totalBytes = mFilesToCopy.getTotalBytes();
		long completedBytes = 0;
		final int totalFiles =mFilesToCopy.getSize();
		int completedFiles = 0;

		// Create a File object from the destination path of the FileSet
		Path destParent = Paths.get(mFilesToCopy.getDestination()).toRealPath(NOFOLLOW_LINKS);
		System.out.println("created destination parent directory");

		Path destination = destParent.resolve(mFilesToCopy.getName());
		System.out.println("created destination backup directory");

		// Check that the destination doesn't already exist and also that it is writable
		if (Files.exists(destination) || !Files.isWritable(destParent)) {
			throw new IOException("Destination already exists - copying aborted");
		}

		Files.createDirectories(destination);
		System.out.println("created destination directory");

		ArrayList<Path> mFilesToCopy = new ArrayList<Path>();
		for (int i = 0; i < totalFiles; i++) {
			Path sourcePath = Paths.get(this.mFilesToCopy.get(i)).toRealPath(NOFOLLOW_LINKS);

			// Validate the file is readable.
			if (!Files.isReadable(sourcePath)) throw new IOException("File " + sourcePath.getFileName().toString() + " is not readable.");

			// Add the file to the ArrayList
			mFilesToCopy.add(sourcePath);
		}

		// Notify observers that operation is about to begin.
		//publish(new Progress("", totalBytes, completedBytes, totalFiles, completedFiles));

		// Copy all the files in the FileSet one by one
		for (int i = 0; i < mFilesToCopy.size() && !isCancelled(); i++) {
			Path sourcePath = mFilesToCopy.get(i);
			try {
				Path destPath = destination.resolve(sourcePath.toString().substring(1));
				Files.createDirectories(destPath.getParent());
				String sourceCopied = sourcePath.toString();
				System.out.println("Directory created");
				System.out.println("copied " + sourceCopied);
				// Files.copy(sourcePath, destPath);
				// Update number of bytes copied
				// completedBytes += Files.size(sourcePath);

				File sp = new File(sourcePath.toString());
				File dp = new File(destPath.toString());
				InputStream in = new FileInputStream(sp);
				OutputStream out = new FileOutputStream(dp);
				byte[] buffer = new byte[1024];
				int length;
				while ((length = in.read(buffer)) > 0) {
					out.write(buffer, 0, length);
					completedBytes += length;
					publish(new Progress("", totalBytes, completedBytes, totalFiles, completedFiles));
				}
				in.close();
				out.close();
				publish(new Progress(sourceCopied, totalBytes, completedBytes, totalFiles, completedFiles++));
			} catch (Exception e) {
				System.err.println("Failed trying to copy " + sourcePath.toString());
				e.printStackTrace();
			}
		}
		return null;
	}

	public static Boolean isValidDest(FileSet fs) throws IOException {
		Path destParent = Paths.get(fs.getDestination()).toRealPath(NOFOLLOW_LINKS);
		Path destination = destParent.resolve(fs.getName());
		if (Files.exists(destination) || !Files.isWritable(destParent)) {
			return false;
		}
		return true;
	}

	@Override
	public void process(List<Progress> progressItems) {
		if (mMessageHandler != null) {
			mMessageHandler.handleProgress(progressItems);
		}
	}

	@Override
	public void done() {
		if (mMessageHandler != null) {
			mMessageHandler.handleCompletion();
		}
	}
}

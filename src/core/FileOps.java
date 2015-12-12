package core;

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
 * @version 1.0
 *
 */
public class FileOps extends SwingWorker<Void, Progress> {

	private final FileSet mFilesToCopy;
	private final FileOpsMessageHandler mMessageHandler;


	public FileOps(FileSet files, FileOpsMessageHandler handler) throws IllegalArgumentException {
		validateFileSet(files);
		mFilesToCopy = files;
		mMessageHandler = handler;
	}

	public FileOps(FileSet files) throws IllegalArgumentException {
		validateFileSet(files);
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
		Path destParent = Paths.get(mFilesToCopy.getDestination());
		System.out.println("Set destination parent directory to: " + destParent);

		Path destinationDir = destParent.resolve(mFilesToCopy.getName());
		System.out.println("Set destination backup directory to: " + destinationDir);
		// Check that the destination doesn't already exist and also that it is writable
		if (Files.exists(destinationDir) || !Files.isWritable(destParent)) {
			throw new IOException("Destination already exists - copying aborted");
		}

		Files.createDirectories(destinationDir);

		ArrayList<Path> mFilesToCopy = new ArrayList<Path>();
		for (int i = 0; i < totalFiles; i++) {
			Path sourcePath = Paths.get(this.mFilesToCopy.get(i));

			// Validate the file is readable.
			if (!Files.isReadable(sourcePath)) throw new IOException("File " + sourcePath.getFileName() + " is not readable.");

			// Add the file to the ArrayList
			mFilesToCopy.add(sourcePath);
		}

		// Notify observers that operation is about to begin.
		publish(new Progress("", totalBytes, completedBytes, totalFiles, completedFiles));

		// Copy all the files in the FileSet one by one
		for (int i = 0; i < mFilesToCopy.size() && !isCancelled(); i++) {
			Path sourcePath = mFilesToCopy.get(i);
			try {
				int subStringIndex = 1;
				
				System.out.println(sourcePath.toString());
				System.out.println(sourcePath.toString().substring(subStringIndex, subStringIndex + 1));
				if (sourcePath.toString().substring(subStringIndex, subStringIndex + 1).equals(":")) {
					subStringIndex = 3;
				}
				System.out.println(sourcePath.toString().substring(subStringIndex));
				System.out.println(destinationDir.resolve(sourcePath.toString().substring(subStringIndex)));

				Path destPath = destinationDir.resolve(sourcePath.toString().substring(subStringIndex));
				System.out.println("Full path to destination set to: " + destPath);
				Files.createDirectories(destPath.getParent());
				String sourceCopied = sourcePath.toString();
				System.out.println("copied " + sourceCopied);
				// Files.copy(sourcePath, destPath);
				// Update number of bytes copied
				// completedBytes += Files.size(sourcePath);

				File sp = sourcePath.toFile();
				File dp = destPath.toFile();
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

	public static Boolean backupExists(FileSet fs) throws IOException {
		Path destParent = Paths.get(fs.getDestination());
		Path destination = destParent.resolve(fs.getName());
		if (Files.exists(destination)) {
			return true;
		}
		return false;
	}

	public static Boolean cannotWrite(FileSet fs) throws IOException {
		Path destParent = Paths.get(fs.getDestination());
		if (!Files.isWritable(destParent)) {
			return true;
		}
		return false;
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

	public static void validateFileSet(FileSet files) throws IllegalArgumentException {
		String exceptionString = "";
		
		if (files == null) { 
			exceptionString = exceptionString.concat("FileSet cannot be null.\n");
		} else {
			if (files.getSize() == 0) {
				exceptionString = exceptionString.concat("FileSet contains no files. Aborting backup operation.\n");
			}
			if (files.getName() == null || files.getName().length() == 0) {
				exceptionString = exceptionString.concat("FileSet has no name for the backup. Aborting backup operation.\n");
			}
			if (files.getDestination() == null || files.getDestination().length() == 0) {
				exceptionString = exceptionString.concat("No destination path specified in FileSet. Aborting backup operation.\n");
			}
		}
		if (exceptionString.length() > 1) {
			throw new IllegalArgumentException(exceptionString);
		}
	}
}

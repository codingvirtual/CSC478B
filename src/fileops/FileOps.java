package fileops;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Observable;

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
 * </p>
 */
public class FileOps extends Observable {
	
		
	private FileSet filesToCopy;
	
	/**
	 * @return the filesToCopy
	 */
	public FileSet getFilesToCopy() {
		return filesToCopy;
	}

	/**
	 * @param filesToCopy the filesToCopy to set
	 */
	public void setFilesToCopy(FileSet filesToCopy) {
		this.filesToCopy = filesToCopy;
	}

	public FileOps(FileSet files) {
		this.filesToCopy = files;
	}
	
	public void run() throws IOException {
		// Create a File object from the destination path of the FileSet
		File destination = new File(this.filesToCopy.getDestination());
		// Check that the destination doesn't already exist and also that it is writable
		if (destination.exists()) throw new IOException("Destination already exists - copying aborted");
		destination.mkdir();
		
		int totalBytes = 0;
		int completedBytes = 0;
		int totalFiles = this.filesToCopy.getSize();
		int completedFiles = 0;
		notifyObservers(new Progress(0, totalBytes, completedBytes, totalFiles, completedFiles));
		// Iterate over the entire set of files in the FileSet

		for (int i = 0; i < totalFiles; i++) {
			String path = this.filesToCopy.get(i);
			// Set up input and output stream objects
			InputStream input = null;
			OutputStream output = null;
			// Try the copy operation
			try {
				/* Create a File object for the destination file by appending the
				 * source path to the destination path.
				 * TODO: this is fraught with peril - need to think about how to do this better
				 */
				File sourceFile = new File(path);
				File destFile = new File(destination.getAbsolutePath() + sourceFile.getAbsolutePath());
				File destDirTree = new File(destFile.getParent());
				destDirTree.mkdirs();
				// Configure the input and output streams
				input = new FileInputStream(new File(path));
				output = new FileOutputStream(destFile);
				// Set up a buffer for the operation
				byte[] buf = new byte[1024];
				int bytesRead;
				// Read and write {buf} bytes at a time
				while ((bytesRead = input.read(buf)) > 0) {
					output.write(buf, 0, bytesRead);
					completedBytes += bytesRead;
				}
				notifyObservers(new Progress(0, totalBytes, completedBytes, totalFiles, completedFiles++));
			} finally {
				// Something went wrong - close the streams before bailing
				input.close();
				output.close();
			}
			
		}
	}
	
	public class Progress {
		public int percentComplete;
		public long totalBytes;
		public long completedBytes;
		public int totalFiles;
		public int completedFiles;
		/**
		 * @param percentComplete
		 * @param totalBytes
		 * @param completedBytes
		 * @param totalFiles
		 * @param completedFiles
		 */
		public Progress(int percentComplete, long totalBytes, long completedBytes, int totalFiles, int completedFiles) {
			this.percentComplete = percentComplete;
			this.totalBytes = totalBytes;
			this.completedBytes = completedBytes;
			this.totalFiles = totalFiles;
			this.completedFiles = completedFiles;
		}
		

	}
}

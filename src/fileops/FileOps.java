package fileops;

import java.io.IOException;
import static java.nio.file.StandardCopyOption.*;
import static java.nio.file.LinkOption.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
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
		
		long totalBytes = 0;
		long completedBytes = 0;
		int totalFiles = this.filesToCopy.getSize();
		int completedFiles = 0;
		
		// Create a File object from the destination path of the FileSet
		Path destParent = Paths.get(filesToCopy.getDestination()).toRealPath(NOFOLLOW_LINKS);
		Path destination = destParent.resolve(filesToCopy.getName());
		// Check that the destination doesn't already exist and also that it is writable
		if (Files.exists(destination) || !Files.isWritable(destination)) throw new IOException("Destination already exists - copying aborted");
		
		
		ArrayList<Path> filesToCopy = new ArrayList<Path>();
		for (int i = 0; i < totalFiles; i++) {
			Path sourcePath = Paths.get(this.filesToCopy.get(i)).toRealPath(NOFOLLOW_LINKS);

			// Validate the file is readable.
			if (!Files.isReadable(sourcePath)) throw new IOException("File " + sourcePath.getFileName().toString() + " is not readable.");
			
			// Add the file size ("length") to the total number of bytes to be copied
			totalBytes += Files.size(sourcePath);
			
			// Add the file to the ArrayList
			filesToCopy.add(sourcePath);
		}

		// Notify observers that operation is about to begin.
		notifyObservers(new Progress(totalBytes, completedBytes, totalFiles, completedFiles));

		// Copy all the files in the FileSet one by one
		for (Path sourcePath : filesToCopy) {
			try {
				Files.copy(sourcePath, destination, ATOMIC_MOVE, COPY_ATTRIBUTES, NOFOLLOW_LINKS);
				// Update number of bytes copied
				completedBytes += Files.size(sourcePath);
				// Notify observers of new progress
				notifyObservers(new Progress(totalBytes, completedBytes, totalFiles, completedFiles++));
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}
	}
	
	public class Progress {
		/**
		 * The Progress class represents a point-in-time progress indication of the copy operation.
		 * 
		 * When completedBytes = 0 and completedFiles = 0, that should be considered "not started"
		 * or "about to start."
		 * 
		 * When completedBytes == totalBytes or completedFiles == totalFiles, that should be construed
		 * as "finished."
		 * 
		 * Anything else is "in progress" and the user of this class can decide how to calculate
		 * percentage completion.
		 * 
		 * The FileOps class reports back a Progress object to it's Observers in the following manner:
		 * 
		 * a)	Right before the copy operation begins and after basic destination path validation is
		 * 		checked, a Progress object with completedBytes = 0 and completedFiles = 0 will be
		 * 		sent to Observers. This would be considered "starting up."
		 * b)	After each file finishes copying, a Progress object is returned with updated values
		 * 		for completedBytes (which will have been incremented by the size of the file that was
		 * 		just copied) and for completedFiles (which will be incremented by 1). To calculate
		 * 		percentage completion, you can either use completedBytes / totalBytes or
		 * 		completedFiles / totalFiles. In the latter case, remember that completedFiles and
		 * 		totalFiles are integers so you'll need to cast to a Float to prevent rounding.
		 * c)	When the last file is copied, a Progress object will be returned just like in b)
		 * 		above, but in this case, completedBytes should equal totalBytes and completedFiles
		 * 		should equal totalFiles. It is suggested to use the completedFiles == totalFiles
		 * 		comparison just in case the File System erroneously reports file sizes.
		 */
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
		public Progress(long totalBytes, long completedBytes, int totalFiles, int completedFiles) {
			this.totalBytes = totalBytes;
			this.completedBytes = completedBytes;
			this.totalFiles = totalFiles;
			this.completedFiles = completedFiles;
		}
		

	}
}

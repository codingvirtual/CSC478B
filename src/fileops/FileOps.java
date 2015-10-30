package fileops;

import java.io.IOException;
import static java.nio.file.LinkOption.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import javax.swing.SwingWorker;

import core.FileSet;
import ui.UIView;

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
public class FileOps extends SwingWorker<Void, Progress> {
	
		
	private final FileSet filesToCopy;
	private final UIView mView;

	public FileOps(FileSet files, UIView view) {
		this.filesToCopy = files;
		this.mView = view;
	}
	

	/* (non-Javadoc)
	 * @see javax.swing.SwingWorker#doInBackground()
	 */
	@Override
	protected Void doInBackground() throws Exception {		
		long totalBytes = 0;
		long completedBytes = 0;
		int totalFiles = this.filesToCopy.getSize();
		int completedFiles = 0;
		
		// Create a File object from the destination path of the FileSet
		Path destParent = Paths.get(filesToCopy.getDestination()).toRealPath(NOFOLLOW_LINKS);
		Path destination = destParent.resolve(filesToCopy.getName());
		// Check that the destination doesn't already exist and also that it is writable
		if (Files.exists(destination) || !Files.isWritable(destParent)) throw new IOException("Destination already exists - copying aborted");
		
		Files.createDirectories(destination);
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
		publish(new Progress(totalBytes, completedBytes, totalFiles, completedFiles));

		// Copy all the files in the FileSet one by one
		for (Path sourcePath : filesToCopy) {
			try {
				Path destPath = destination.resolve(sourcePath.toString().substring(1));
				Files.createDirectories(destPath.getParent());
				Files.copy(sourcePath, destPath);
				// Update number of bytes copied
				completedBytes += Files.size(sourcePath);
				// Notify observers of new progress
				publish(new Progress(totalBytes, completedBytes, totalFiles, completedFiles++));
			} catch (Exception e) {
				System.err.println("Failed trying to copy " + sourcePath.toString());
				e.printStackTrace();
			}
			
		}
		return null;
	}
	
	@Override
	protected void process(List<Progress> progressItems) {
		mView.handleProgress(progressItems);
	}

}

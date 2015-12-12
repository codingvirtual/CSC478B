package core;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.EOFException;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.BasicFileAttributes;

import javax.swing.DefaultListModel;

/**
 * <p>Contains a complete specification for a backup operation.
 * The FileSet describes the settings for a backup operation and includes a collection of source paths
 * to back up, a name for the backup (both the FileSet object itself as well as the name of the root directory in the
 * file system that the backup will process into), and a destination path to copy the source files into.</p>
 *
 * <p>When the backup operation finishes, the complete path heirarchy for the source files will be replicated inside
 * the destination directory. In other words, if the source path for a file is <code>/home/someuser/mytest</code> and the destination
 * is set to <code>/home/someuser/backup</code> and the name of the FileSet is 2015-12-10, then the <code>mytest</code> file will
 * be found at <code>/home/someuser/backup/2015-12-10/home/someuser/mytest</code>.</p>
 * <p>The destination path, FileSet name, and all sources must be valid paths in the file system and must be absolute references.</p>
 * 
 * 
 * @author Greg Palen
 * @version 1.0.0
 * 
 */

public class FileSet extends DefaultListModel<String> {



	private static final long serialVersionUID = 3146908287308357247L;

	/**	The full path to the destination directory to copy the source files into.
	 * 
	 */
	private String destination;
	
	/**	<p>The name of this FileSet object. Also becomes the name of the directory that is created to store the backup into.</p>
	 * <p>This name is appended to the {@link #destination} above to form the full {@link Path} to the backup directory.
	 * 
	 */
	private String name;
	
	/**	The total number of bytes to be copied as reported by the file system.
	 * 
	 */
	private long totalBytes;

	/** Standard no-arg constructor. Use to instantiate a FileSet when you do not know what the name of the FileSet should be
	 * and you do not know the destination path. 
	 * 
	 * @see #setName(String)
	 * @see #setDestination(String)
	 */
	public FileSet() {

	}

	/** Convenience onstructor used to create a new FileSet and set the {@link #name} of the FileSet at the same time. Use this constructor
	 * when you know what {@link #name} you want to give to the FileSet.
	 * 
	 * @param name	String representing the name to give to the FileSet. This name must conform to the file naming rules of the host file system/platform.
	 * @throws Exception
	 * 	when the name is not a valid file name on the underlying host file system.
	 */
	public FileSet(String name) throws Exception {
		setName(name);
	}

	/**
	 * Convenience constructor that creates a new FileSet with the destination path specified. The destination must be an absolute reference, must
	 * be valid on the underlying file system/platform, and must be writeable.
	 * 
	 * @param destination String representing the full path to the destination for the backup operation. This should be a folder path.
	 * @throws IOException 
	 * 	if the path provided is contains illegal characters for the underlying file system or the directory is unwritable (usually due to bad permissions).
	 */
	public FileSet(String name, String destination) throws Exception {
		setName(name);
		setDestination(destination);
	}


	/**
	 * Factory method that instantiates a FileSet by reading the contents of a file on disk from the path specified and constructs a FileSet object from the contents of the file
	 * 
	 * @param fullPathToFile 	String representing the absolute path to a file containing a saved FileSet.
	 * @return FileSet 			A new FileSet instantiated with the values read in from disk.
	 * @throws Exception 
	 * 	If the file specified does not represent a FileSet object or is otherwise unreadable (doesn't exist, insufficient permissions, etc.).
	 */
	public static FileSet read(String fullPathToFile) throws Exception {
		// Create File object and verify the file exists, is a file (not a directory), and is readable
		Path inPath = Paths.get(fullPathToFile);
		if (!Files.exists(inPath)) {
			throw new IOException("File at " + fullPathToFile + " does not appear to exist.");
		}
		if (!Files.isReadable(inPath)) {
			throw new IOException("File at " + fullPathToFile + " does not appear to be readable (may"
					+ "be corrupt or you don't have necessary privileges to read it.");
		}
		ObjectInputStream in = null;
		FileSet fileSet = null;

		try {
			in = new ObjectInputStream(new
					BufferedInputStream(Files.newInputStream(inPath)));
			fileSet = (FileSet) in.readObject();
		} catch (EOFException e) {
			e.printStackTrace();
		} finally {
			in.close();
		}
		return fileSet;
	}

	/**
	 * Saves a FileSet to a directory. The file will have the name contained in {@link FileSet#name} and will be stored in the path
	 * provided in the absolutePath parameter.
	 * 
	 * @param fullPathToFile	A string representing the absolute path for where to create and save the FileSet. This should be a FILE
	 * 						path and NOT a directory. The name of the saved FileSet will be the right-most element
	 * 						in the filePath parameter. If a file with this complete path already exists, it will be 
	 * 						overwritten with the contents of the FileSet passed in via this parameter.
	 * @param fileSet 			The FileSet object to save to disk. 
	 * 
	 * @throws IOException 		
	 * 	Will throw an IOException if the destination cannot be written to or a write error occurs.
	 * 
	 */
	public static void save(String fullPathToFile, FileSet fileSet) throws IOException {
		ObjectOutputStream out = null;
		Path outFilePath = Paths.get(fullPathToFile);
		Path outDirPath = outFilePath.getParent();

		if (Files.exists(outFilePath)) {
			Files.delete(outFilePath);
		}
		if (!Files.exists(outDirPath)) {
			Files.createDirectories(outDirPath);
		}
		try {
			out = new ObjectOutputStream(new
					BufferedOutputStream(Files.newOutputStream(outFilePath)));
			out.writeObject(fileSet);
		} catch (Exception e) {
			throw new IOException(e);
		} finally {
			out.close();
		}
	}

	/**	Adds the specified path to the list of sources that the FileSet is going to copy.
	 * 
	 */
	public void addElement(String path) throws IllegalArgumentException {
		if (super.contains(path)) return;
		if (validExistingPath(path)) {
			super.addElement(path);
			Path filePath = Paths.get(path);
			try {
				BasicFileAttributes fileAttrs = Files.getFileAttributeView(filePath, BasicFileAttributeView.class).readAttributes();
				totalBytes += fileAttrs.size();
			} catch (IOException e) {
				throw new IllegalArgumentException("Attributes of file " + path + " are not readable.");
			}

		} else {
			throw new IllegalArgumentException("Adding path failed - `" + path + "` appears to be invalid.");
		}
	}

	/**	Removes the specified path from the list of sources that the FileSet is going to copy. Nothing happens if the supplied
	 * path does not exist in the current FileSet list of sources.
	 * 
	 * @param path	A string representing an absolute path to a file to be backed up as part of the copy operation.
	 * 
	 * @throws IllegalArgumentException
	 * 	When the specified path does not exist or is not readable (usually due to permissions).
	 */
	public void removeElement(String path) throws IllegalArgumentException {

		if (!super.contains(path)) {
			throw new IllegalArgumentException(path + " is not in the FileSet");
		} else {
			super.removeElement(path);
			Path filePath = Paths.get(path);
			try {
				BasicFileAttributes fileAttrs = Files.getFileAttributeView(filePath, BasicFileAttributeView.class).readAttributes();
				totalBytes -= fileAttrs.size();
			} catch (IOException e) {
				throw new IllegalArgumentException("Attributes of file " + path + " are not readable.");
			}

		} 
	}

	
	// --- Utility Methods -- //

	/**	Utility method to determine if a given path is valid. To be valid, the path must:
	 * @param path	String representing the absolute {@link Path} to validate.
	 * @return Boolean True if the path is valid and the {@link File} specified by the last element of the path string exists.
	 */
	public static boolean validExistingPath(String path) {
		Path testPath = Paths.get(path);
		File testFile = testPath.toFile();
		return testFile.exists();
	}

	/**	Validates that a given fileName represents a legal name for a file on the underlying host file system/platform.
	 * 
	 * @param fileName	String representing ONLY the file name to test (NOT an absolute path - just the name itself)
	 * @return Boolean	True if the fileName is valid.
	 * @throws IOException
	 * 	when a file with that name is invalid. This is tested by attempting to create a file with that name in a temporary folder. 
	 * Could return the exception if JVM is unable to create the temp directory.
	 */
	public static boolean validFileName(String fileName) throws IOException {
		// Create a temporary directory
		Path testFileDir = Files.createTempDirectory(null);
		File testFile = testFileDir.resolve(fileName).toFile();
		// createNewFile() will return true if the file system can create a new, empty file
		// with the name specified at the path specified. If a file with that name already
		// exists, it will fail, hence the check before-hand to see if the file already exists.
		// If the file DOES exist, then obviously the fileName is valid, so use it.
		if (testFile.exists()) {
			testFile.delete();
			testFileDir.toFile().delete();
			return true;
		} else if (testFile.createNewFile()) {
			testFile.delete();
			testFileDir.toFile().delete();
			return true;
		} else {
			throw new IOException("FileSet with name " + fileName + " could not be created.");
		}
	}
	
	// --- Getters & Setters -- //

	/**
	 * @return the name
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Sets the name of the FileSet (which will become the file name if the FileSet is saved)
	 * @param name the name to set
	 */ 
	public void setName(String name) throws Exception {
		if (name == null) {
			throw new IllegalArgumentException("Name is null");
		}
		if (validFileName(name)) {
			this.name = name;
		}
	}

	/**	Get the destination directory the backup will get created in. This represents the absolute {@link Path} to a directory that
	 * will contain the backup. 
	 * @return
	 */
	public String getDestination() {
		return destination;
	}

	/**	Sets the destination directory the backup will get created in. This represents the absolute {@link Path} to a directory that
	 * 	will contain the backup. 
	 * 
	 * @param destination String representing the absolute path to a directory to write the backup into.
	 * @throws Exception when the directory contains illegal characters for the underlying file system/platform or if the specified
	 * destination cannot be written into (due to permissions typically).
	 */
	public void setDestination(String destination) throws Exception {
		// Test destination to make sure it's a valid destination (to be valid, it must be a directory and it must exist right now).
		if (destination == null) {
			throw new IllegalArgumentException("Destination is null");
		}
		Path testDest = Paths.get(destination);
		if (!Files.isDirectory(testDest)) throw new IOException("Destination does not exist or is not a directory");
		this.destination = testDest.toString();
	}
	

	/**
	 * @return the totalBytes of all files that are in the FileSet at the time this call is made.
	 */
	public long getTotalBytes() {
		return totalBytes;
	}

}


/**
 * Contains a complete specification for a backup operation.
 * The FileSet contains a complete spec for a backup operation including a collection of paths,
 * a schedule option for recurring, scheduled backups, and of course a destination indicating where to write
 * the backup to.
 * 
 * @author Greg
 * @version 1.0
 * 
 */

package core;

import static java.nio.file.LinkOption.*;
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

public class FileSet extends DefaultListModel<String> {


	/**
	 * 
	 */
	private static final long serialVersionUID = 3146908287308357247L;

	private String destination;
	private String name;
	private long totalBytes;

	public FileSet() {

	}

	public FileSet(String name) throws Exception {
		setName(name);
	}

	/**
	 * Convenience constructor that creates a new, empty FileSet with the destination path specified.
	 * @param destination String representing the full path to the destination for the backup operation. This should be a folder path.
	 * @throws IOException if the path provided in destination is invalid or unreachable.
	 */
	public FileSet(String name, String destination) throws Exception {
		setName(name);
		setDestination(destination);
	}


	/**
	 * Factory method that reads a FileSet from the path specified and constructs a FileSet object from the contents of the file
	 * 
	 * @param fullPathToFile 	The absolute path of where to read the FileSet from
	 * @return FileSet 			The FileSet that was just read in from disk.
	 * @throws Exception 
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
	 * Saves a FileSet to a directory. The file will have the name contained in fileSet.name and will be stored in the path
	 * provided in the absolutePath parameter.
	 * 
	 * @param fullPathToFile	The absolute path indicating where to create and save the FileSet. This should be a FILE
	 * 						path and NOT a directory. The name of the saved FileSet will be the right-most element
	 * 						in the filePath parameter. If a file with this complete path already exists, it will be 
	 * 						overwritten with the contents of the FileSet passed in via this parameter.
	 * @param fileSet 			The FileSet object to save to disk. 
	 * 
	 * @throws IOException 		Will throw an IOException if the destination cannot be written to or a write error occurs.
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
			e.printStackTrace();
		} finally {
			out.close();
		}
	}

	public void addElement(String path) throws IllegalArgumentException {
		if (super.contains(path)) return;
		if (validExistingPath(path)) {
			super.addElement(path);
			Path filePath = Paths.get(path);
			try {
				BasicFileAttributes fileAttrs = Files.getFileAttributeView(filePath, BasicFileAttributeView.class).readAttributes();
				totalBytes += fileAttrs.size();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				throw new IllegalArgumentException("Attributes of file " + path + " are not readable.");
			}

		} else {
			throw new IllegalArgumentException("Adding path failed - `" + path + "` appears to be invalid.");
		}
	}

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
				// TODO Auto-generated catch block
				throw new IllegalArgumentException("Attributes of file " + path + " are not readable.");
			}

		} 
	}

	
	// --- Utility Methods -- //

	/**
	 * @param path
	 * @return
	 */
	public static boolean validExistingPath(String path) {
		Path testPath = Paths.get(path);
		File testFile = testPath.toFile();
		return testFile.exists();
	}

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

	public String getDestination() {
		return destination;
	}

	public void setDestination(String destination) throws Exception {
		// Test destination to make sure it's a valid destination (to be valid, it must be a directory and it must exist right now).
		if (destination == null) {
			throw new IllegalArgumentException("Destination is null");
		}
		Path testDest = Paths.get(destination).toRealPath(NOFOLLOW_LINKS);
		if (!Files.isDirectory(testDest, NOFOLLOW_LINKS)) throw new IOException("Destination does not exist or is not a directory");
		this.destination = testDest.toRealPath(NOFOLLOW_LINKS).toString();
	}
	

	/**
	 * @return the totalBytes
	 */
	public long getTotalBytes() {
		return totalBytes;
	}

}


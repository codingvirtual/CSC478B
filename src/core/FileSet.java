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
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.swing.DefaultListModel;

public class FileSet extends DefaultListModel<String> {


	/**
	 * 
	 */
	private static final long serialVersionUID = 3146908287308357247L;
	
	private String destination;
	private String name;
	
	public FileSet() {
		
	}
	
	public FileSet(String name) throws Exception {
		super();
		Path homeDir = Paths.get(System.getProperty("user.home"));
		Path testFilePath = homeDir.resolve(name);
		File testFile = testFilePath.toFile();
		if (testFile.createNewFile()) {
			this.setName(name);
		} else {
			throw new IOException("FileSet with name " + name + " could not be created.");
		}
	}
	
	/**
	 * Convenience constructor that creates a new, empty FileSet with the destination path specified.
	 * @param destination String representing the full path to the destination for the backup operation. This should be a folder path.
	 * @throws IOException if the path provided in destination is invalid or unreachable.
	 */
	public FileSet(String name, String destination) throws Exception {
		super();
		setName(name);
		setDestination(destination);
	}


	/**
	 * Factory method that reads a FileSet from the path specified and constructs a FileSet object from the contents of the file
	 * @param absolutePath The absolute path of where to read the FileSet from
	 * @return FileSet The FileSet that was just read in from disk.
	 * @throws Exception 
	 */
	public static FileSet read(String absolutePath) throws Exception {
		// Create File object and verify the file exists, is a file (not a directory), and is readable
		Path inPath = Paths.get(absolutePath).toRealPath();
		if (!Files.isReadable(inPath)) {
			throw new IOException("File not found or not readable.");
		}
		ObjectInputStream in = null;
		FileSet fileSet = null;
		try {
            in = new ObjectInputStream(new
                    BufferedInputStream(new FileInputStream(inPath.toString())));
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
	 * @param absolutePath	The absolute path to the directory of where to save the FileSet to.
	 * @param fileSet The FileSet object to save to disk. fileSet.name will be the filename it is saved with.
	 * @throws IOException Will throw an IOException if the destination cannot be written to or a write error occurs.
	 */
	public static void save(String absolutePath, FileSet fileSet) throws IOException {
		ObjectOutputStream out = null;
		Path outPath = Paths.get(absolutePath).toRealPath();
		Path outFile = outPath.resolve(fileSet.getName());
		try {
			out = new ObjectOutputStream(new
                    BufferedOutputStream(new FileOutputStream(outFile.toString())));
			out.writeObject(fileSet);
		} finally {
			out.close();
		}
	}
	

	
	// --- Getters & Setters -- //

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	@SuppressWarnings("unused")
	public
	/**
	 * Sets the name of the FileSet (which will become the file name if the FileSet is saved)
	 * @param name the name to set
	 */ Boolean setName(String name) throws Exception {
		// TODO: add error checking to make sure the name we get is in a valid format for the filesystem
		if (name == null) {
			throw new IllegalArgumentException("Name is null");
		}
		// FIXME: implement proper name validation below and get rid of "true"
		if (true) {
			this.name = name;
		} else {
			throw new IllegalArgumentException("Supplied name does not conform to file system naming requirements");
		}
		return true;
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
}


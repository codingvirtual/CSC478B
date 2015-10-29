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

import java.io.IOException;
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
	
	public FileSet(String name) {
		this.name = name;
	}
	
	/**
	 * Convenience constructor that creates a new, empty FileSet with the destination path specified.
	 * @param destination String representing the full path to the destination for the backup operation. This should be a folder path.
	 * @throws IOException if the path provided in destination is invalid or unreachable.
	 */
	public FileSet(String name, String destination) throws IOException {
		this.name = name;
		this.destination = destination;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the name of the FileSet (which will become the file name if the FileSet is saved)
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	public String getDestination() {

		return destination;
	}

	public void setDestination(String destination) {
		// TODO: validate the destination and throw an exception if it's not valid
		this.destination = destination;
	}

	/**
	 * Reads a FileSet from the path specified and returns the updated FileSet
	 * @param absolutePath The absolute path of where to read the FileSet from
	 * @return FileSet The FileSet that was just read in from disk.
	 * @throws IOException Will throw an IOException if the file does not exist or cannot be read.
	 */
	public FileSet read(String absolutePath) throws IOException {

		return this;
	}
	
	/**
	 * Saves the FileSet to the path specified
	 * @param absolutePath	The absolute path of where to save the FileSet to.
	 * @throws IOException Will throw an IOException if the destination cannot be written to or a write error occurs.
	 */
	public void save(String absolutePath) throws IOException {

	}
}


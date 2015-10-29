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

	public FileSet read() {
		return this;
	}
	
	public void save(String absolutePath) {
		
	}
}


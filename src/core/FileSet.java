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
	@SuppressWarnings("unused")
	private static final long serialVersionUID = 3146908287308357247L;
	
	private String destination;
	
	public FileSet() {
		
	}
	
	/**
	 * Convenience constructor that creates a new, empty FileSet with the destination path specified.
	 * @param destination String representing the full path to the destination for the backup operation. This should be a folder path.
	 * @throws IOException if the path provided in destination is invalid or unreachable.
	 */
	public FileSet(String destination) throws IOException {
		this.setDestination(destination);
	}

	public String getDestination() {
		return destination;
	}

	public void setDestination(String destination) {
		// TODO: validate the destination and throw an exception if it's not valid
		this.destination = destination;
	}

}


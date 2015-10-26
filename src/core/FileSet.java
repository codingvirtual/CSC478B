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

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import core.Schedule.Recurrence;

public class FileSet {


	// TODO: should this be FilePath instead? If so, we'll need a convenience method to return the list as a string instead.
	private ArrayList<String> fileSet; 
	private Schedule schedule;
	private String destination;
	

	/**
	 * Constructor that creates a new, empty FileSet.
	 * 
	 */
	public FileSet() {
		fileSet = new ArrayList<String>();
	}
	
	/**
	 * Convenience constructor that creates a new, empty FileSet with the destination path specified.
	 * @param destination String representing the full path to the destination for the backup operation. This should be a folder path.
	 * @throws IOException if the path provided in destination is invalid or unreachable.
	 */
	public FileSet(String destination) throws IOException {
		fileSet = new ArrayList<String>();
		this.setDestination(destination);
	}
	
	/**
	 * Adds a path to the FileSet.
	 * @param path String representing a path to a source file or folder to include in the backup.
	 * The string passed should be a full path specification and NOT a relative path reference.
	 */
	public void addPath(String path) {
		// TODO: Error checking needs to be added to validate that the path supplied is actually reachable.
		// TODO: Do we throw an exception if the path is bad, or instead return a Boolean and let the caller handle it?
		// TODO: Ensure we aren't adding a duplicate. If it's a folder reference, need to check to be sure that no
		//			parent folders are already accounted for; if it's a file, need to check that there are no parent
		//			folders already set to back up.
		fileSet.add(path);
	}
	
	public Boolean removePath(String path) {
		// will return true if the path exists and was removed. 
		// TODO: As above, however, should we throw an exception if the path doesn't exist?
		// 
		return fileSet.remove(path);
	}

	public Schedule getSchedule() {
		return schedule;
	}

	public void setSchedule(Schedule schedule) {
		this.schedule = schedule;
	}

	
	public void setSchedule(Date date, Recurrence interval) {
		schedule = new Schedule(date, interval);
	}
	
	public String getDestination() {
		return destination;
	}

	public void setDestination(String destination) {
		// TODO: validate the destination and throw an exception if it's not valid
		this.destination = destination;
	}

	public ArrayList<String> getFileSet() {
		return this.fileSet;
	}
}


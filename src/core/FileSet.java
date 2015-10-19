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

import java.util.ArrayList;
import java.util.Date;

public class FileSet {

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
	 * Adds a path to the FileSet.
	 * @param path String representing a path to a source file or folder to include in the backup
	 */
	public void addPath(String path) {
		// TOOD: need to add error checking here or assume path is correct?
		fileSet.add(path);
	}
	
	public Boolean removePath(String path) {
		// will return true if the path exists and was removed
		return fileSet.remove(path);
	}
	
	public void setSchedule(Date date, Long interval) {
		schedule = new Schedule(date, interval);
	}
	
	/**
	 * 
	 * @author Greg
	 * @version 1.0
	 * 
	 *
	 */
	public class Schedule {
		
		private Date date;
		private Long interval;
		
		public Schedule(Date date, Long interval) {
			// TODO: add range and error checking
			this.date = date;
			this.interval = interval;
		}
		
		public Schedule setInterval(Long interval) {
			// TODO: add range and error checking
			this.interval = interval;
			return this;
		}
		
		public Long getInterval() {
			return interval;
		}
	}

}


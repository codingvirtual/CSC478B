package core;

import java.util.Date;

/**
 * The Schedule class represents both a specific date and time as well as a recurrence
 * interval. 
 * The date and time are represented by the standard Java Date class.
 * Recurrence is specified via an Enum defined in within the class.
 * 
 * @author Greg
 * @version 1.0
 * 
 *
 */
public class Schedule {
	
	/**
	 * The Recurrence enum represents the frequency of recurrence of the scheduled object.
	 * 
	 */
	public enum Recurrence {
		Daily,
		Weekly,
		Monthly
	}
	
	private Date date;
	private Recurrence interval;
	
	public Schedule(Date date, Recurrence interval) {
		// TODO: add range and error checking
		this.setDate(date);
		this.interval = interval;
	}
	
	/**
	 * 
	 * @param interval Recurrence interval as specified by the Recurrence enum
	 * @return Schedule Returns the updated Schedule object
	 */
	public Schedule setRecurrence(Recurrence interval) {
		// TODO: add range and error checking
		this.interval = interval;
		return this;
	}
	
	/**
	 * Gets the Recurrence for this Schedule object.
	 * @return Recurrence A Recurrence value from the enum specification
	 */
	public Recurrence getRecurrence() {
		return interval;
	}

	/**
	 * Gets the date and time set for this Schedule
	 * @return Date Standard Java date is returned that includes a date and time.
	 */
	public Date getDate() {
		return date;
	}

	/**
	 * Sets a new date for the schedule. Should be a date in the future.
	 * @param date Java Date object representing the new data to be set.
	 */
	public void setDate(Date date) {
		this.date = date;
	}
}
package core;

import java.util.Date;

/**
 * 
 * @author Greg
 * @version 1.0
 * 
 *
 */
public class Schedule {
	
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
	
	public Schedule setRecurrence(Recurrence interval) {
		// TODO: add range and error checking
		this.interval = interval;
		return this;
	}
	
	public Recurrence getRecurrence() {
		return interval;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}
}
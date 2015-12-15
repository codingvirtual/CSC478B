package core;


/**
 * The Progress class represents a point-in-time progress indication of the copy operation.
 * 
 * <br /><br />
 * <p>When completedBytes = 0 and completedFiles = 0, that should be considered "not started"
 * or "about to start."</p>
 * 
 * <p>When completedBytes == totalBytes or completedFiles == totalFiles, that should be construed
 * as "finished."</p>
 * 
 * <p>Anything else is "in progress" and the user of this class can decide how to calculate
 * percentage completion.</p>
 * 
 * <p>The FileOps class reports back a Progress object to it's Observers in the following manner:
 * <ol>
 * 
 * <li>	Right before the copy operation begins and after basic destination path validation is
 * 		checked, a Progress object with completedBytes = 0 and completedFiles = 0 will be
 * 		sent to Observers. This would be considered "starting up."</li>
 * <li>	After each file finishes copying, a Progress object is returned with updated values
 * 		for completedBytes (which will have been incremented by the size of the file that was
 * 		just copied) and for completedFiles (which will be incremented by 1). To calculate
 * 		percentage completion, you can either use completedBytes / totalBytes or
 * 		completedFiles / totalFiles. In the latter case, remember that completedFiles and
 * 		totalFiles are integers so you'll need to cast to a Float to prevent rounding.</li>
 * <li>	When the last file is copied, a Progress object will be returned just like in 2)
 * 		above, but in this case, completedBytes should equal totalBytes and completedFiles
 * 		should equal totalFiles. It is suggested to use the completedFiles == totalFiles
 * 		comparison just in case the File System erroneously reports file sizes.</li>
 * </ol>
 * </p>
 * <p>Requirement 1.1.5.1: The user must be notified of the status of any backup (failure or success).</p>
 * 
 * @author Greg Palen
 * 
 * @version 1.0.0
 * @see FileOps
 *
 */
public class Progress {

	/**
	 * The name of the source file being copied at present.
	 */
	public String sourceCopied;
	/**
	 * Total number of bytes in the complete file.
	 */
	public long totalBytes;
	/**
	 * Number of bytes that have been copied.
	 */
	public long completedBytes;
	/**
	 * Total number of files to be copied.
	 */
	public int totalFiles;
	/**
	 * Number of files that have completed being copied.
	 */
	public int completedFiles;
	
	/** Constructor to create a Progress item.
	 * @param percentComplete The percentage of the copy operation that has completed.
	 * @param totalBytes The total number of bytes represented in the entire copy operation.
	 * @param completedBytes The number of bytes that have been transferred as of this progress report.
	 * @param totalFiles The total number of files in the entire copy operation.
	 * @param completedFiles The total number of files that have been transferred (in full) as of this progress report.
	 */
	public Progress(String sourceCopied, long totalBytes, long completedBytes, int totalFiles, int completedFiles) {
		this.sourceCopied = sourceCopied;
		this.totalBytes = totalBytes;
		this.completedBytes = completedBytes;
		this.totalFiles = totalFiles;
		this.completedFiles = completedFiles;
	}
	
	@Override
	public String toString() {
		return ("Total Bytes: " + totalBytes + "; Completed Bytes: " + completedBytes
				+ "; Total Files: " + totalFiles + "; Completed Files: " + completedFiles);
	}
}

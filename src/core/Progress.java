/**
 *
 * @author Greg Palen
 * @version 0.1.0
 *
 * <h3>Revision History</h3>
 * <p>
 * 0.1.0	GP	Initial revision
 * 
 * </p>
 */
package core;


/**
 * @author Greg
 *
 */
public class Progress {
	/**
	 * The Progress class represents a point-in-time progress indication of the copy operation.
	 * 
	 * When completedBytes = 0 and completedFiles = 0, that should be considered "not started"
	 * or "about to start."
	 * 
	 * When completedBytes == totalBytes or completedFiles == totalFiles, that should be construed
	 * as "finished."
	 * 
	 * Anything else is "in progress" and the user of this class can decide how to calculate
	 * percentage completion.
	 * 
	 * The FileOps class reports back a Progress object to it's Observers in the following manner:
	 * 
	 * a)	Right before the copy operation begins and after basic destination path validation is
	 * 		checked, a Progress object with completedBytes = 0 and completedFiles = 0 will be
	 * 		sent to Observers. This would be considered "starting up."
	 * b)	After each file finishes copying, a Progress object is returned with updated values
	 * 		for completedBytes (which will have been incremented by the size of the file that was
	 * 		just copied) and for completedFiles (which will be incremented by 1). To calculate
	 * 		percentage completion, you can either use completedBytes / totalBytes or
	 * 		completedFiles / totalFiles. In the latter case, remember that completedFiles and
	 * 		totalFiles are integers so you'll need to cast to a Float to prevent rounding.
	 * c)	When the last file is copied, a Progress object will be returned just like in b)
	 * 		above, but in this case, completedBytes should equal totalBytes and completedFiles
	 * 		should equal totalFiles. It is suggested to use the completedFiles == totalFiles
	 * 		comparison just in case the File System erroneously reports file sizes.
	 */
	public String sourceCopied;
	public long totalBytes;
	public long completedBytes;
	public int totalFiles;
	public int completedFiles;
	
	/**
	 * @param percentComplete
	 * @param totalBytes
	 * @param completedBytes
	 * @param totalFiles
	 * @param completedFiles
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

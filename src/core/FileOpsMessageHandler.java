package core;

import java.util.List;

/** Defines required methods for any class that wants to receive {@link Progress} updates from the {@link FileOps}
 * operation as it proceeds.
 * 
 * @author Greg Palen
 * 
 * @version 1.0.0
 *
 */
public interface FileOpsMessageHandler {

	/**	handleProgress will periodically receive {@link Progress} objects during the copy operation.
	 * 
	 * @param progressItems	One or more {@link Progress} objects containing progress updates. {@link SwingWorker}
	 * can potentially batch progress updates, hence the reason a {@link List} must be handled.
	 */
	public void handleProgress(List<Progress> progressItems);
	
	/**	handleCompletion will be called when the copy operation has fully completed and is about to return.
	 *  The callback signifies successful completion.
	 */
	public void handleCompletion();
	
}

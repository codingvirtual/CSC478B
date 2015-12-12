package core;

import java.util.List;

/**
 * @author Greg Palen
 * 
 * @version 1.0.0
 *
 */
public interface FileOpsMessageHandler {

	public void handleProgress(List<Progress> progressItems);
	public void handleCompletion();
	
}

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

import java.util.List;

/**
 * @author Greg
 *
 */
public interface FileOpsMessageHandler {

	public void handleProgress(List<Progress> progressItems);
	public void handleCompletion();
	
}

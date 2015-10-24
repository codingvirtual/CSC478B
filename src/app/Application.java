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
package app;

import java.lang.reflect.InvocationTargetException;

import ui.UIController;
import core.FileSet;

/**
 * @author Greg
 *
 */
public class Application {

	private FileSet currentFileSet;
	
	public Application() {
		// look for current FileSet file on disk
		// if exists, read it and set the currentFileSet to it
		// else create a new FileSet and set it as current
		currentFileSet = new FileSet();
	}
	
	/**
	 * @return the currentFileSet
	 */
	public FileSet getCurrentFileSet() {
		return currentFileSet;
	}

	/**
	 * @param currentFileSet the currentFileSet to set
	 */
	public void setCurrentFileSet(FileSet currentFileSet) {
		this.currentFileSet = currentFileSet;
	}

	public static void main(String args[]) {
	    try {
			java.awt.EventQueue.invokeAndWait(new Runnable() {
			    public void run() {
			        new UIController(new Application());
			    }
			});
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
        
}
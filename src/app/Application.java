/**
 *
 * @author Greg Palen
 * @version 0.1.0
 *
 * <h3>Revision History</h3>
 * <p>
 * 0.1.0	GP	Initial revision
 * 0.1.1	AR	Add SwingUtilities to main()
 * 
 * </p>
 */
package app;


import javax.swing.UnsupportedLookAndFeelException;
import ui.UIViewController;
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
		try {
			currentFileSet = new FileSet("Default");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
	
	public void getFileSet(String absolutePath) {
		/**
		 * Reads a FileSet from the specified location
		 */
		
	}
	
	public void saveFileSet(String absolutePath) {
		/**
		 * Saves FileSet to a specific location
		 */
	}
	
	public void saveFileSet() {
		/**
		 * Saves FileSet to the default location (whatever that may be)
		 */
		
	}

	public static void main(String args[]) {
		
//	    try {
//			java.awt.EventQueue.invokeAndWait(new Runnable() {
	    	javax.swing.SwingUtilities.invokeLater(new Runnable() {
			    public void run() {
			        try {
						new UIViewController(new Application()).setVisible(true);
					} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
							| UnsupportedLookAndFeelException e) {
						e.printStackTrace();
					}
			    }
			});
//		} catch (InvocationTargetException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}     
}

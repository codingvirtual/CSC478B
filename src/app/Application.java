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


import java.nio.file.Path;
import java.nio.file.Paths;

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
			getDefaultFileSet();
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
	
	public void getDefaultFileSet() {
		/**
		 * Reads a FileSet from the specified location
		 */
		Path defaultFileSet = Paths.get(System.getProperty("user.home"));
		defaultFileSet.resolve("Mirror");
		defaultFileSet.resolve("DefaultFileSet");
		try {
			currentFileSet = FileSet.read(defaultFileSet.toAbsolutePath().toString());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			currentFileSet = new FileSet();
		}
	}
	
	
	public void saveDefaultFileSet() {
		/**
		 * Saves FileSet to the default location (whatever that may be)
		 */
		Path appFilesDir = Paths.get(System.getProperty("user.home"));
		appFilesDir.resolve("Mirror");
		try {
			FileSet.save(appFilesDir.toAbsolutePath().toString(), currentFileSet);
			} catch (Exception e) {
			// FIXME: generate an error if unable to set name (this shouldn't happen)
			e.printStackTrace();
		}
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

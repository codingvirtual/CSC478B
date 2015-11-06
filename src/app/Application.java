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

	private FileSet mCurrentFileSet;
	
	public Application() throws Exception {
		// look for current FileSet file on disk
		// if exists, read it and set the currentFileSet to it
		// else create a new FileSet and set it as current
		getDefaultFileSet();
	}
	
	/**
	 * @return the currentFileSet
	 */
	public FileSet getCurrentFileSet() {
		if (mCurrentFileSet == null) {
			getDefaultFileSet();
		}
		return mCurrentFileSet;
	}

	/**
	 * @param currentFileSet the currentFileSet to set
	 */
	public void setCurrentFileSet(FileSet fileSet) {
		if (fileSet == null) throw new NullPointerException("Attempted to set the current FileSet"
				+ "to a 'null' object.");
		this.mCurrentFileSet = fileSet;
	}
	
	private void getDefaultFileSet() {
		/**
		 * Reads a FileSet from the specified location
		 */
		Path defaultFileSet = Paths.get(System.getProperty("user.home"));
		defaultFileSet.resolve("Mirror");
		defaultFileSet.resolve("DefaultFileSet");
		try {
			mCurrentFileSet = FileSet.read(defaultFileSet.toAbsolutePath().toString());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			mCurrentFileSet = new FileSet();
		}
	}
	
	
	public void saveDefaultFileSet() {
		/**
		 * Saves FileSet to the default location (whatever that may be)
		 */
		Path appFilesDir = Paths.get(System.getProperty("user.home"));
		appFilesDir.resolve("Mirror");
		try {
			FileSet.save(appFilesDir.toAbsolutePath().toString(), mCurrentFileSet);
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
					} catch (Exception e) {
						System.out.println("Unable to create Application object.");
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

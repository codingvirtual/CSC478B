
package app;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.swing.UnsupportedLookAndFeelException;
import ui.UIViewController;
import core.FileSet;

/**
 * Defines the Application context (for storage of global objects). Also serves as a proxy for the Model in the Model-View-Controller pattern.
 *
 * @author Greg Palen
 * @version 1.0
 * 
 *
 */

public class Application {
	
	/**
	 * Holds the active FileSet that the UI will display (the "Model").
	 */
	private FileSet mCurrentFileSet;

	/**
	 * Standard Public Constructor with no arguments. Upon exit, a default file set will have either been loaded or instantiated.
	 * @see getDefaultFileSet
	 * 
	 * @throws Exception if there are errors while attempting to locate or read the default FileSet
	 */
	public Application() throws Exception {
		// look for current FileSet file on disk
		// if exists, read it and set the currentFileSet to it
		// else create a new FileSet and set it as current
		getDefaultFileSet();
	}

	/**
	 * @return the currentFileSet
	 * @throws Exception 
	 */
	public FileSet getCurrentFileSet() throws Exception {
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

	/**f
	 * Loads
	 * @throws Exception
	 */
	private void getDefaultFileSet() throws Exception {
		/**
		 * Reads a FileSet from the specified location
		 */
		Path defaultFileSet = Paths.get(System.getProperty("user.home"));
		defaultFileSet = defaultFileSet.resolve("Mirror");
		defaultFileSet = defaultFileSet.resolve("DefaultFileSet");
		if (Files.isReadable(defaultFileSet)) {
			try {
				mCurrentFileSet = FileSet.read(defaultFileSet.toAbsolutePath().toString());
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			mCurrentFileSet = new FileSet("DefaultFileSet");
		}
	}


	public void saveDefaultFileSet() throws IOException {
		/**
		 * Saves FileSet to the default location (whatever that may be)
		 */
		Path defaultFSPath = Paths.get(System.getProperty("user.home"));
		defaultFSPath = defaultFSPath.resolve("Mirror");
		defaultFSPath = defaultFSPath.resolve("DefaultFileSet");
		FileSet.save(defaultFSPath.toString(), mCurrentFileSet);
	}

	public static void main(String args[]) {

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

	}     
}


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
 * <br /><br />
 * Extend this class and override getDefaultFileSet and saveDefaultFileSet to alter the default location that the active FileSet is stored.
 *
 * @author Greg Palen
 * @version 1.0
 * 
 *
 */
public class Application {
	
	/**
	 * Holds the active {@link FileSet} (the "Model") that both the {@link UIViewController} (the View-Controller) will interact with 
	 * as well as the model that the {@link FileOps} class uses for its operations.
	 * 
	 * @see UIViewController
	 */
	private FileSet mCurrentFileSet;

	/**
	 * Standard default constructor to instantiate the entire application. Upon return, a default file set will have either been loaded or instantiated and
	 * the user interface will be displayed.
	 * 
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
	 * Method to retrieve the current {@link FileSet} from the application context.
	 * <br /><br />
	 * If there is no {@link FileSet} defined in the context, a new default (empty) {@link FileSet} will be created.
	 * @return the current {@link FileSet}
	 * @throws Exception (propagated by {@link #getDefaultFileSet}
	 * @see #getDefaultFileSet()
	 */
	public FileSet getCurrentFileSet() throws Exception {
		if (mCurrentFileSet == null) {
			getDefaultFileSet();
		}
		return mCurrentFileSet;
	}

	/**
	 * Takes a {@link FileSet} and sets it as the current {@link FileSet} for the application to operate on.
	 * @param currentFileSet the currentFileSet to set
	 * @throws NullPointerException if the passed {@link FileSet} is null.
	 */
	public void setCurrentFileSet(FileSet fileSet) {
		if (fileSet == null) throw new NullPointerException("Attempted to set the current FileSet"
				+ "to a 'null' object.");
		this.mCurrentFileSet = fileSet;
	}

	/**
	 * Loads a {@link FileSet} from the user's home directory (platform-dependent location). The default {@link FileSet} will
	 * be stored in a sub-directory named <code>Mirror</code> within the user's home directory in a file named <code>DefaultFileSet</code>.
	 * @throws Exception if the sub-directory or file does not exist or is not readable (for example due to bad permissions).
	 */
	private void getDefaultFileSet() throws Exception {
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

	/**
	 * Saves the current in-memory {@link FileSet} to the default location.
	 * 
	 * @see #getDefaultFileSet()
	 * @throws IOException if the file or sub-directory doesn't exist or if the file is unreadable (for example due to bad permissions).
	 */
	public void saveDefaultFileSet() throws IOException {
		Path defaultFSPath = Paths.get(System.getProperty("user.home"));
		defaultFSPath = defaultFSPath.resolve("Mirror");
		defaultFSPath = defaultFSPath.resolve("DefaultFileSet");
		FileSet.save(defaultFSPath.toString(), mCurrentFileSet);
	}

	/**
	 * Called by the JVM at runtime to launch the application. Upon execution, <code>main()</code> will attempt to create and open the
	 * User Interface.
	 * @param args - Standard command line arguments
	 * @throws ClassNotFoundException
	 * @throws InstantiationException
	 * @throws IllegalClassException
	 * @throws UnsupportedLookAndFeelException
	 */
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

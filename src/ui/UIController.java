/**
 *
 * @author Greg Palen
 * @version 0.1.1
 *
 * <h3>Revision History</h3>
 * <p>
 * 0.1.0	GP	Initial revision
 * 0.1.1	GP 	Add doRun, addPath,removePath, and setDestination methods
 * </p>
 */
package ui;

import java.awt.Color;
import java.io.IOException;

import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.UnsupportedLookAndFeelException;

import app.Application;
import core.FileSet;
import fileops.FileOps;

/**
 * @author Greg
 *
 */
public class UIController {
	
	private Application mApp;
	private FileSet currentFileSet;



	public UIController(Application app) throws ClassNotFoundException, InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException {
		mApp = app;
		currentFileSet = mApp.getCurrentFileSet();
		System.out.println(currentFileSet.getDestination());
		try {
    		// set custom colors for Nimbus
    		UIManager.put("nimbusBase", Color.GRAY);
    		UIManager.put("nimbusFocus", new Color(157, 224, 35));
    	    UIManager.put("nimbusSelectionBackground", new Color(0, 158, 0));
            UIManager.put("nimbusSelection", new Color(0, 158, 0));
    	    UIManager.put("control", new Color(242, 242, 242));
    		// set Nimbus L&F
    	    for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
    	        if ("Nimbus".equals(info.getName())) {
    	            UIManager.setLookAndFeel(info.getClassName());
    	            break;
    	        }
    	    }
    	} catch (Exception e) {
    		// set to System L&F if Nimbus isn't available
    		UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    		e.printStackTrace();
    	}
	
	    /* Create and display the form */
	    java.awt.EventQueue.invokeLater(new Runnable() {
	        public void run() {
	            new UIView(UIController.this).setVisible(true);
	        }
	    });
	}
	
	public void doRun() {
		// TODO: this will need to be enhanced later. No status is provided and no result is provided.
		FileOps ops = new FileOps(currentFileSet);
		ops.setFilesToCopy(currentFileSet);
		try {
			ops.run();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void setDestination(String destination) {
		// FIXME: this needs to be changed. Probably should get the backup name and the destination
		// from the View and build the true destination by appending a trailing slash (/) and the 
		// backup name to the destination, then calling setDestination using that concatenation.
		currentFileSet.setDestination(destination + "/Testing123");
	}
	
	/**
	 * @return the currentFileSet
	 */
	public FileSet getCurrentFileSet() {
		return currentFileSet;
	}
}

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
package ui;

import javax.swing.UIManager;

import app.Application;
import core.FileSet;

/**
 * @author Greg
 *
 */
public class UIController {
	
	private Application mApp;
	private FileSet currentFileSet;

	
	public UIController(Application app) {
		mApp = app;
		currentFileSet = mApp.getCurrentFileSet();
		System.out.println(currentFileSet.getDestination());
	    try {
	        // Set System L&F
	        UIManager.setLookAndFeel(
	        UIManager.getSystemLookAndFeelClassName());
	    } catch (ClassNotFoundException ex) {
	        java.util.logging.Logger.getLogger(UIView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
	    } catch (InstantiationException ex) {
	        java.util.logging.Logger.getLogger(UIView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
	    } catch (IllegalAccessException ex) {
	        java.util.logging.Logger.getLogger(UIView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
	    } catch (javax.swing.UnsupportedLookAndFeelException ex) {
	        java.util.logging.Logger.getLogger(UIView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
	    }
	    //</editor-fold>
	
	    /* Create and display the form */
	    java.awt.EventQueue.invokeLater(new Runnable() {
	        public void run() {
	            new UIView(UIController.this).setVisible(true);
	        }
	    });
	}
}

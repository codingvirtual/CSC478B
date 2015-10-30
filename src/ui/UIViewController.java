/**
 * Creates a graphical user interface to control backup operations.
 * 
 * @author Ashley
 * @version 0.1.1
 *
 * <h3>Revision History</h3>
 * <p>
 * 0.1.0	AR	Initial revision
 * 0.1.1	GP 	Add code to link Add File, Remove Selection, Browse, and Run
 * 				buttons to associated code in UIController
 * </p>
 */

package ui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.swing.ButtonGroup;
import javax.swing.DefaultListModel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.MatteBorder;
import org.jdesktop.swingx.JXDatePicker;

import app.Application;
import core.FileSet;
import fileops.FileOps;
import fileops.Progress;

//public void doRun() {
//	// TODO: this will need to be enhanced later. No status is provided and no result is provided.

//}


public class UIViewController extends javax.swing.JFrame {

	private static final long serialVersionUID = -7478454925642500957L;
	private Application mApp;
	private FileSet mCurrentFileSet;
	
	/**
     * Creates new form UIView
     */
    public UIViewController(Application app) throws ClassNotFoundException, InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException {
    	
    	mApp = app;
    	mCurrentFileSet = mApp.getCurrentFileSet();
    	
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
    	    UIManager.getLookAndFeelDefaults().put("List[Selected].textBackground", Color.GRAY);
    	    UIManager.getLookAndFeelDefaults().put("List[Selected].textForeground", Color.WHITE);
    	} catch (Exception e) {
    		// set to System L&F if Nimbus isn't available
    		UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    		e.printStackTrace();
    	}

        
        
		setResizable(false);
		setSize(new Dimension(685, 711));
		getContentPane().setFont(new Font("Helvetica Neue", 0, 14));
		menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		menuOpen = new JMenu("Open...");
		menuOpen.setFont(new Font("Helvetica Neue", Font.PLAIN, 14));
		menuBar.add(menuOpen);
		menuItemLog = new JMenuItem("Log");
		menuItemLog.setFont(new Font("Helvetica Neue", Font.PLAIN, 14));
		menuOpen.add(menuItemLog);
		JSeparator separator = new JSeparator();
		menuOpen.add(separator);
		menuItemManual = new JMenuItem("User's Manual");
		menuItemManual.setFont(new Font("Helvetica Neue", Font.PLAIN, 14));
		menuOpen.add(menuItemManual);
		menuItemManual.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				// open user's manual in browser on click
        		File f = new File("res/users-manual.html");
				try {
					Desktop.getDesktop().browse(f.toURI());
					if (System.getProperty("os.name").startsWith("Mac OS")) {
						// firefox (for mac) workaround - wait 2 seconds, then re-open uri
						java.util.concurrent.TimeUnit.SECONDS.sleep(2);
						Desktop.getDesktop().browse(f.toURI());
					}
				} catch (IOException | InterruptedException e2) {
					e2.printStackTrace();
				}
				
			}
		});
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
    	listModel = mCurrentFileSet;
    	listSources = new JList<String>(listModel);
    	panelSettings = new JPanel();
    	panelFreq = new JPanel();
    	panelFreq.setFont(new Font("Helvetica Neue", Font.PLAIN, 14));
    	panelBackup = new JPanel();
    	panelNameBackup = new JPanel();
    	panelNameBackup.setFont(new Font("Helvetica Neue", Font.PLAIN, 14));
    	scrollSources = new JScrollPane();
    	scrollStatus = new JScrollPane();
    	scrollStatus.setFont(new Font("Helvetica Neue", Font.PLAIN, 12));
    	lblAppTitle = new JLabel();
    	lblAppTitle.setBorder(new MatteBorder(0, 0, 1, 0, (Color) new Color(0, 0, 0)));
    	lblSchedSync = new JLabel();
    	lblDate = new JLabel();
    	lblTime = new JLabel();
    	lblSources = new JLabel();
    	lblDestination = new JLabel();
    	lblDestNote = new JLabel();
    	lblDestNote.setForeground(Color.GRAY);
    	jXDatePicker = new JXDatePicker();
        spinTime = new JSpinner();
        txtNameBackup = new JTextField();
        txtNameBackup.setFont(new Font("Helvetica Neue", Font.PLAIN, 14));
        // TODO: create ActionListener for txtNameBackup. It should respond to losing focus
        // and when it does lose focus, it should do this
        
        // try {
        //     // this next line is going to throw an exception for sure if the destination
        //     // box isn't filled in.
        //     mCurrentFileSet.setName(txtNameBackup.getText());
        // } catch (IOException e) {
        //     // error is probably because the name isn't valid or the ultimate destination
        //	   // isn't writeable.
        //     // do something about error like a dialog box or something.
        //     e.printStackTrace();
        // }
        
        txtDestination = new JTextField();
        grpRadioSyncSwitch = new ButtonGroup();
        grpRadioFreq = new ButtonGroup();
        
        radioOn = new JRadioButton();
        radioOn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            }
        });
        
        radioOff = new JRadioButton();
        radioOff.setSelected(true);
        radioOff.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            }
        });
        
        radioDaily = new JRadioButton();
        radioDaily.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            }
        });
        
        radioWeekly = new JRadioButton();
        radioWeekly.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            }
        });
        
        radioMonthly = new JRadioButton();
        radioMonthly.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            }
        });
        
        btnAddFile = new JButton();
        btnAddFile.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		JFileChooser fc = new JFileChooser();
        		fc.setPreferredSize(new Dimension(500, 400));
        		fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
        		int returnVal = fc.showDialog(UIViewController.this, "Add file");
        		if (returnVal == JFileChooser.APPROVE_OPTION) {
        			File file = fc.getSelectedFile();
        			listModel.addElement(file.getAbsolutePath());
        		}
        	}
        });
        
        btnAddFolder = new JButton();
        btnAddFolder.setEnabled(false);
        btnAddFolder.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        	}
        });
        
        btnRemove = new JButton();
        btnRemove.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		// don't remove if source list is empty or no selection was made
        		if (listModel.getSize() != 0 && !listSources.isSelectionEmpty()) {
        			int i = listSources.getSelectedIndex();
        			listModel.remove(i);
        		}
        	}
        });
        
        btnBrowse = new JButton();
        btnBrowse.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	JFileChooser fc = new JFileChooser();
            	fc.setPreferredSize(new Dimension(500, 400));
        		fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        		int returnVal = fc.showDialog(UIViewController.this, "Set destination");
        		if (returnVal == JFileChooser.APPROVE_OPTION) {
        			txtDestination.setText(fc.getSelectedFile().toString());
        			// TODO: here is where I call up to the controller to set the destination
        			// FIXME: this code is broken. Right now, the Controller manually sets
        			// the destination to a folder named "Testing123" inside the destination
        			// path identified below. This should really pass the Name of Backup and
        			// the path in "Destination" up to the Controller and let it concatenate
        			// them and put them in the FileSet destination.
        			try {
						mCurrentFileSet.setDestination(txtDestination.getText());
						mCurrentFileSet.setName(txtNameBackup.getText());
					} catch (Exception e1) {
						// TODO If an error is thrown, it's likely because the selected
						// destination does not have write-access. Show a dialog to the user
						// and have them pick a different location
						e1.printStackTrace();
					}
        		}
            }
        });
        
        btnSave = new JButton();
        btnSave.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        	}
        });
        
        btnRun = new JButton();
        btnRun.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
    			try {
					mCurrentFileSet.setDestination(txtDestination.getText());
					mCurrentFileSet.setName(txtNameBackup.getText());
				} catch (Exception e1) {
					// TODO If an error is thrown, it's likely because the selected
					// destination does not have write-access. Show a dialog to the user
					// and have them pick a different location
					e1.printStackTrace();
				}
        		FileOps ops = new FileOps(mCurrentFileSet, UIViewController.this);
        		ops.run();
        	}
        });
        
        

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        panelSettings.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "SETTINGS", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Helvetica Neue", 0, 14))); // NOI18N
        panelSettings.setFont(new Font("Helvetica Neue", Font.PLAIN, 16)); // NOI18N

        lblSchedSync.setFont(new Font("Helvetica Neue", Font.PLAIN, 14)); // NOI18N
        lblSchedSync.setText("Scheduled sync:");

        grpRadioSyncSwitch.add(radioOn);
        radioOn.setFont(new Font("Helvetica Neue", Font.PLAIN, 18)); // NOI18N
        radioOn.setText("ON");

        grpRadioSyncSwitch.add(radioOff);
        radioOff.setFont(new Font("Helvetica Neue", Font.PLAIN, 18)); // NOI18N
        radioOff.setText("OFF");

        jXDatePicker.setFont(new Font("Helvetica Neue", 0, 14)); // NOI18N

        spinTime.setFont(new Font("Helvetica Neue", 0, 14)); // NOI18N
        spinTime.setModel(new javax.swing.SpinnerDateModel(new java.util.Date(1444176000000L), null, null, java.util.Calendar.MINUTE));
        spinTime.setEditor(new JSpinner.DateEditor(spinTime, "h:mm a"));

        lblDate.setFont(new Font("Helvetica Neue", Font.PLAIN, 13)); // NOI18N
        lblDate.setText("Date:");

        lblTime.setFont(new Font("Helvetica Neue", Font.PLAIN, 13)); // NOI18N
        lblTime.setText("Time:");

        panelFreq.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Frequency", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Helvetica Neue", 0, 12))); // NOI18N
        panelFreq.setLocation(new java.awt.Point(-32267, -32677));

        grpRadioFreq.add(radioDaily);
        radioDaily.setFont(new Font("Helvetica Neue", Font.PLAIN, 13)); // NOI18N
        radioDaily.setText("Daily");

        grpRadioFreq.add(radioWeekly);
        radioWeekly.setFont(new Font("Helvetica Neue", Font.PLAIN, 13)); // NOI18N
        radioWeekly.setText("Weekly");

        grpRadioFreq.add(radioMonthly);
        radioMonthly.setFont(new Font("Helvetica Neue", Font.PLAIN, 13)); // NOI18N
        radioMonthly.setText("Monthly");

        javax.swing.GroupLayout gl_panelFreq = new javax.swing.GroupLayout(panelFreq);
        gl_panelFreq.setHorizontalGroup(
        	gl_panelFreq.createParallelGroup(Alignment.LEADING)
        		.addGroup(gl_panelFreq.createSequentialGroup()
        			.addContainerGap()
        			.addComponent(radioDaily)
        			.addPreferredGap(ComponentPlacement.RELATED, 18, Short.MAX_VALUE)
        			.addComponent(radioWeekly)
        			.addGap(18)
        			.addComponent(radioMonthly)
        			.addContainerGap())
        );
        gl_panelFreq.setVerticalGroup(
        	gl_panelFreq.createParallelGroup(Alignment.LEADING)
        		.addGroup(gl_panelFreq.createSequentialGroup()
        			.addContainerGap()
        			.addGroup(gl_panelFreq.createParallelGroup(Alignment.BASELINE)
        				.addComponent(radioMonthly)
        				.addComponent(radioDaily)
        				.addComponent(radioWeekly))
        			.addContainerGap(10, Short.MAX_VALUE))
        );
        panelFreq.setLayout(gl_panelFreq);

        javax.swing.GroupLayout gl_panelSettings = new javax.swing.GroupLayout(panelSettings);
        gl_panelSettings.setHorizontalGroup(
        	gl_panelSettings.createParallelGroup(Alignment.LEADING)
        		.addGroup(gl_panelSettings.createSequentialGroup()
        			.addGroup(gl_panelSettings.createParallelGroup(Alignment.LEADING)
        				.addGroup(gl_panelSettings.createSequentialGroup()
        					.addContainerGap()
        					.addComponent(lblSchedSync)
        					.addPreferredGap(ComponentPlacement.RELATED)
        					.addComponent(radioOn)
        					.addPreferredGap(ComponentPlacement.RELATED)
        					.addComponent(radioOff))
        				.addGroup(gl_panelSettings.createSequentialGroup()
        					.addGap(33)
        					.addGroup(gl_panelSettings.createParallelGroup(Alignment.LEADING, false)
        						.addGroup(gl_panelSettings.createSequentialGroup()
        							.addComponent(lblDate)
        							.addPreferredGap(ComponentPlacement.RELATED)
        							.addComponent(jXDatePicker, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
        							.addPreferredGap(ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        							.addComponent(lblTime))
        						.addComponent(panelFreq, GroupLayout.PREFERRED_SIZE, 272, GroupLayout.PREFERRED_SIZE))
        					.addPreferredGap(ComponentPlacement.RELATED)
        					.addComponent(spinTime, GroupLayout.PREFERRED_SIZE, 149, GroupLayout.PREFERRED_SIZE)))
        			.addContainerGap(207, Short.MAX_VALUE))
        );
        gl_panelSettings.setVerticalGroup(
        	gl_panelSettings.createParallelGroup(Alignment.LEADING)
        		.addGroup(gl_panelSettings.createSequentialGroup()
        			.addGroup(gl_panelSettings.createParallelGroup(Alignment.BASELINE)
        				.addComponent(lblSchedSync)
        				.addComponent(radioOn)
        				.addComponent(radioOff))
        			.addGap(18)
        			.addGroup(gl_panelSettings.createParallelGroup(Alignment.BASELINE)
        				.addComponent(jXDatePicker, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
        				.addComponent(lblDate)
        				.addComponent(lblTime)
        				.addComponent(spinTime, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
        			.addGap(18)
        			.addComponent(panelFreq, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
        			.addContainerGap(25, Short.MAX_VALUE))
        );
        panelSettings.setLayout(gl_panelSettings);

        panelBackup.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "BACKUP", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Helvetica Neue", 0, 14))); // NOI18N
        panelBackup.setFont(new Font("Helvetica Neue", Font.PLAIN, 16)); // NOI18N

        lblSources.setFont(new Font("Helvetica Neue", Font.PLAIN, 14)); // NOI18N
        lblSources.setText("Source File(s) / Folder(s):");

        lblDestination.setFont(new Font("Helvetica Neue", Font.PLAIN, 14)); // NOI18N
        lblDestination.setText("Destination:");

        txtDestination.setFont(new Font("Helvetica Neue", Font.PLAIN, 14)); // NOI18N
        // set default destination to user's system-dependent home directory
        txtDestination.setText(System.getProperty("user.home"));

        lblDestNote.setFont(new Font("Helvetica Neue", Font.PLAIN, 12)); // NOI18N
        lblDestNote.setText("A zip file containing all backup data will be stored at this location");
        
        btnAddFile.setFont(new Font("Helvetica Neue", Font.PLAIN, 14)); // NOI18N
        btnAddFile.setText("Add File");

        btnAddFolder.setFont(new Font("Helvetica Neue", Font.PLAIN, 14)); // NOI18N
        btnAddFolder.setText("Add Folder");

        btnRemove.setFont(new Font("Helvetica Neue", Font.PLAIN, 14)); // NOI18N
        btnRemove.setText("Remove Selection");

        btnBrowse.setFont(new Font("Helvetica Neue", Font.PLAIN, 14)); // NOI18N
        btnBrowse.setText("Browse...");

        btnSave.setFont(new Font("Helvetica Neue", Font.PLAIN, 15)); // NOI18N
        btnSave.setText("SAVE");
        btnSave.setPreferredSize(new Dimension(73, 45));
        btnSave.setMinimumSize(new Dimension(73, 45));
        btnSave.setMaximumSize(new Dimension(73, 45));
        
        btnRun.setFont(new Font("Helvetica Neue", Font.PLAIN, 15));
        btnRun.setText("RUN");
        btnRun.setPreferredSize(new Dimension(73, 45));
        btnRun.setMinimumSize(new Dimension(73, 45));
        btnRun.setMaximumSize(new Dimension(73, 45));
        
        scrollStatus.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Status", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Helvetica Neue", 0, 12))); // NOI18N
        
        panelNameBackup.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Name of Backup", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Helvetica Neue", 0, 12))); // NOI18N

        txtNameBackup.setHorizontalAlignment(JTextField.CENTER);
        txtNameBackup.setText("backup-m-d-yy");

        javax.swing.GroupLayout gl_panelNameBackup = new javax.swing.GroupLayout(panelNameBackup);
        panelNameBackup.setLayout(gl_panelNameBackup);
        gl_panelNameBackup.setHorizontalGroup(
            gl_panelNameBackup.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(txtNameBackup, javax.swing.GroupLayout.DEFAULT_SIZE, 138, Short.MAX_VALUE)
        );
        gl_panelNameBackup.setVerticalGroup(
            gl_panelNameBackup.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(gl_panelNameBackup.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(txtNameBackup, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        
        javax.swing.GroupLayout gl_panelBackup = new javax.swing.GroupLayout(panelBackup);
        gl_panelBackup.setHorizontalGroup(
        	gl_panelBackup.createParallelGroup(Alignment.LEADING)
        		.addGroup(gl_panelBackup.createSequentialGroup()
        			.addContainerGap()
        			.addGroup(gl_panelBackup.createParallelGroup(Alignment.LEADING)
        				.addGroup(gl_panelBackup.createSequentialGroup()
        					.addGroup(gl_panelBackup.createParallelGroup(Alignment.LEADING)
        						.addGroup(gl_panelBackup.createSequentialGroup()
        							.addPreferredGap(ComponentPlacement.RELATED, 7, Short.MAX_VALUE)
        							.addGroup(gl_panelBackup.createParallelGroup(Alignment.LEADING)
        								.addGroup(gl_panelBackup.createSequentialGroup()
        									.addGroup(gl_panelBackup.createParallelGroup(Alignment.LEADING)
        										.addComponent(scrollSources, Alignment.TRAILING, GroupLayout.PREFERRED_SIZE, 288, GroupLayout.PREFERRED_SIZE)
        										.addComponent(txtDestination, Alignment.TRAILING, GroupLayout.PREFERRED_SIZE, 288, GroupLayout.PREFERRED_SIZE))
        									.addPreferredGap(ComponentPlacement.UNRELATED)
        									.addGroup(gl_panelBackup.createParallelGroup(Alignment.LEADING)
        										.addComponent(btnAddFolder, GroupLayout.DEFAULT_SIZE, 160, Short.MAX_VALUE)
        										.addComponent(btnAddFile, GroupLayout.DEFAULT_SIZE, 160, Short.MAX_VALUE)
        										.addGroup(gl_panelBackup.createSequentialGroup()
        											.addGroup(gl_panelBackup.createParallelGroup(Alignment.TRAILING, false)
        												.addComponent(btnBrowse, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        												.addComponent(btnRemove, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        											.addGap(9))))
        								.addGroup(gl_panelBackup.createSequentialGroup()
        									.addGap(6)
        									.addComponent(lblDestNote, GroupLayout.PREFERRED_SIZE, 405, GroupLayout.PREFERRED_SIZE))))
        						.addComponent(scrollStatus, GroupLayout.PREFERRED_SIZE, 466, GroupLayout.PREFERRED_SIZE))
        					.addPreferredGap(ComponentPlacement.UNRELATED)
        					.addGroup(gl_panelBackup.createParallelGroup(Alignment.TRAILING, false)
        						.addComponent(panelNameBackup, GroupLayout.PREFERRED_SIZE, 164, GroupLayout.PREFERRED_SIZE)
        						.addGroup(gl_panelBackup.createSequentialGroup()
        							.addComponent(btnSave, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
        							.addPreferredGap(ComponentPlacement.RELATED)
        							.addComponent(btnRun, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
        							.addGap(6)))
        					.addPreferredGap(ComponentPlacement.RELATED))
        				.addComponent(lblSources)
        				.addComponent(lblDestination))
        			.addGap(18))
        );
        gl_panelBackup.setVerticalGroup(
        	gl_panelBackup.createParallelGroup(Alignment.LEADING)
        		.addGroup(gl_panelBackup.createSequentialGroup()
        			.addContainerGap()
        			.addComponent(lblSources)
        			.addPreferredGap(ComponentPlacement.RELATED)
        			.addGroup(gl_panelBackup.createParallelGroup(Alignment.LEADING)
        				.addComponent(scrollSources, GroupLayout.DEFAULT_SIZE, 99, Short.MAX_VALUE)
        				.addGroup(gl_panelBackup.createParallelGroup(Alignment.BASELINE)
        					.addComponent(panelNameBackup, GroupLayout.DEFAULT_SIZE, 99, Short.MAX_VALUE)
        					.addGroup(gl_panelBackup.createSequentialGroup()
        						.addComponent(btnAddFile)
        						.addPreferredGap(ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        						.addComponent(btnAddFolder)
        						.addPreferredGap(ComponentPlacement.RELATED)
        						.addComponent(btnRemove))))
        			.addGroup(gl_panelBackup.createParallelGroup(Alignment.LEADING, false)
        				.addGroup(gl_panelBackup.createSequentialGroup()
        					.addGap(25)
        					.addComponent(lblDestination)
        					.addPreferredGap(ComponentPlacement.RELATED)
        					.addGroup(gl_panelBackup.createParallelGroup(Alignment.BASELINE)
        						.addComponent(txtDestination, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
        						.addComponent(btnBrowse))
        					.addPreferredGap(ComponentPlacement.RELATED)
        					.addComponent(lblDestNote)
        					.addGap(25)
        					.addComponent(scrollStatus, GroupLayout.PREFERRED_SIZE, 71, GroupLayout.PREFERRED_SIZE)
        					.addContainerGap())
        				.addGroup(Alignment.TRAILING, gl_panelBackup.createSequentialGroup()
        					.addPreferredGap(ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        					.addGroup(gl_panelBackup.createParallelGroup(Alignment.BASELINE)
        						.addComponent(btnRun, GroupLayout.PREFERRED_SIZE, 45, GroupLayout.PREFERRED_SIZE)
        						.addComponent(btnSave, GroupLayout.PREFERRED_SIZE, 45, GroupLayout.PREFERRED_SIZE))
        					.addGap(11))))
        );
        gl_panelBackup.linkSize(SwingConstants.HORIZONTAL, new Component[] {btnAddFile, btnAddFolder, btnRemove});
        
        scrollSources.setViewportView(listSources);
        panelBackup.setLayout(gl_panelBackup);

        lblAppTitle.setFont(new Font("Helvetica Neue", Font.PLAIN, 15)); // NOI18N
        lblAppTitle.setText("    Mirror Machine (Name TBD)");
        
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        layout.setHorizontalGroup(
        	layout.createParallelGroup(Alignment.TRAILING)
        		.addGroup(layout.createSequentialGroup()
        			.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        			.addGroup(layout.createParallelGroup(Alignment.LEADING)
        				.addGroup(layout.createSequentialGroup()
        					.addGroup(layout.createParallelGroup(Alignment.TRAILING)
        						.addComponent(panelSettings, GroupLayout.PREFERRED_SIZE, 679, GroupLayout.PREFERRED_SIZE)
        						.addComponent(panelBackup, Alignment.LEADING, GroupLayout.PREFERRED_SIZE, 679, GroupLayout.PREFERRED_SIZE))
        					.addContainerGap())
        				.addGroup(Alignment.TRAILING, layout.createSequentialGroup()
        					.addComponent(lblAppTitle)
        					.addGap(15))))
        );
        layout.setVerticalGroup(
        	layout.createParallelGroup(Alignment.LEADING)
        		.addGroup(layout.createSequentialGroup()
        			.addComponent(lblAppTitle, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE)
        			.addGap(12)
        			.addComponent(panelSettings, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
        			.addPreferredGap(ComponentPlacement.RELATED, 20, Short.MAX_VALUE)
        			.addComponent(panelBackup, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
        			.addContainerGap())
        );
        getContentPane().setLayout(layout);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    public void handleProgress(List<Progress> progressItems) {
    	System.out.println("got " + progressItems.size() + " progress objects.");
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private ButtonGroup grpRadioSyncSwitch;
    private ButtonGroup grpRadioFreq;
    private DefaultListModel<String> listModel;
    private JButton btnAddFile;
    private JButton btnAddFolder;
    private JButton btnRemove;
    private JButton btnBrowse;
    private JButton btnSave;
    private JButton btnRun;
    private JLabel lblAppTitle;
    private JLabel lblSchedSync;
    private JLabel lblSources;
    private JLabel lblDestination;
    private JLabel lblDestNote;
    private JLabel lblDate;
    private JLabel lblTime;
    private JPanel panelSettings;
    private JPanel panelBackup;
    private JPanel panelNameBackup;
    private JPanel panelFreq;
    private JRadioButton radioOn;
    private JRadioButton radioOff;
    private JRadioButton radioDaily;
    private JRadioButton radioWeekly;
    private JRadioButton radioMonthly;
    private JScrollPane scrollSources;
    private JScrollPane scrollStatus;
    private JSpinner spinTime;
    private JTextField txtDestination;
    private JTextField txtNameBackup;
    private JXDatePicker jXDatePicker;
    private JMenuBar menuBar;
    private JMenu menuOpen;
    private JMenuItem menuItemLog;
    private JMenuItem menuItemManual;
    private JList<String> listSources;
}

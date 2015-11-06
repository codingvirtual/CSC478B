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
 * 0.1.2	AR	Add status text & circular progress bar
 * 0.1.3	AR	Display warning dialog boxes
 * </p>
 */

package ui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Arc2D;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.DefaultListModel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.MatteBorder;
import javax.swing.plaf.basic.BasicProgressBarUI;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import org.jdesktop.swingx.JXDatePicker;
import app.Application;
import core.FileSet;
import fileops.FileOps;
import fileops.FileOpsMessageHandler;
import fileops.Progress;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;


public class UIViewController extends JFrame implements FileOpsMessageHandler {
	private static final long serialVersionUID = 1L;
	static final Color drkGreen = new Color(0, 158, 0);
	private Document doc;
	private Application mApp;
	private FileSet mCurrentFileSet;
	
	/**
     * Creates new form UIViewController
     */
    public UIViewController(Application app) throws ClassNotFoundException, InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException {
    	
    	mApp = app;
    	mCurrentFileSet = mApp.getCurrentFileSet();
    	
    	try {
    		// set custom colors for Nimbus
    		UIManager.put("nimbusBase", Color.GRAY);
    		UIManager.put("nimbusFocus", new Color(157, 224, 35));
    	    UIManager.put("nimbusSelectionBackground", drkGreen);
            UIManager.put("nimbusSelection", drkGreen);
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
    	listSources.setFont(new Font("Helvetica Neue", Font.PLAIN, 14));
    	panelSettings = new JPanel();
    	panelFreq = new JPanel();
    	panelFreq.setFont(new Font("Helvetica Neue", Font.PLAIN, 14));
    	panelBackup = new JPanel();
    	panelNameBackup = new JPanel();
    	panelNameBackup.setFont(new Font("Helvetica Neue", Font.PLAIN, 14));
    	panelProgress = new JPanel();
    	panelProgress.setOpaque(false);
    	panelProgress.setFont(new Font("Helvetica Neue", Font.PLAIN, 14));
    	progressCirc = new JProgressBar() {
    		private static final long serialVersionUID = 1L;
    		@Override public void updateUI() {
                super.updateUI();
                setUI(new ProgressCircle());
                setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));
            }
        };
        progressCirc.setStringPainted(true);
        progressCirc.setFont(new Font("Helvetica Neue", Font.BOLD, 14));
    	panelProgress.add(progressCirc);
    	panelProgress.setVisible(false);
    	scrollSources = new JScrollPane();
    	scrollSources.setFont(new Font("Helvetica Neue", Font.PLAIN, 14));
    	scrollStatus = new JScrollPane();
    	scrollStatus.setViewportBorder(null);
    	lblAppTitle = new JLabel();
    	lblAppTitle.setBorder(new MatteBorder(0, 0, 1, 0, Color.BLACK));
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
        txtNameBackup.addFocusListener(new FocusAdapter() {
        	@Override
        	public void focusLost(FocusEvent e) {
        			txtNameBackup.setText(txtNameBackup.getText().trim());
        	}
        });
        
        txtStatus = new JTextPane();
        txtStatus.setForeground(new Color(0, 128, 0));
        txtStatus.setFont(new Font("Helvetica Neue", Font.BOLD | Font.ITALIC, 13));
        txtStatus.setOpaque(false);
        txtStatus.setEditable(false);
        txtStatus.setBackground(new Color(0, 0, 0, 0));
        txtStatus.setMargin(new java.awt.Insets(0, 0, 0, 0));
        doc = txtStatus.getDocument();
        txtDestination = new JTextField();
        txtDestination.addFocusListener(new FocusAdapter() {
        	@Override
        	public void focusLost(FocusEvent e) {
        		txtDestination.setText(txtDestination.getText().trim());
        	}
        });
        grpRadioSyncSwitch = new ButtonGroup();
        grpRadioFreq = new ButtonGroup();
        try {
			checkMark = ImageIO.read(new File("res/icons/green-check.png"));
		} catch (IOException e3) {
			e3.printStackTrace();
		}
		lblCheck = new JLabel(new ImageIcon(checkMark));
        
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
        			try {
						mCurrentFileSet.setDestination(txtDestination.getText());
					} catch (Exception e1) {
						// FIXME: user has somehow entered a "bad" destination. Make them fix it.
						e1.printStackTrace();
					}
        		}
            }
        });
        
        btnSave = new JButton();
        btnSave.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		try {
					mCurrentFileSet.setName(txtNameBackup.getText());
				} catch (Exception e2) {
					// FIXME: Ideally, show the user a dialog box and get them to pick a different name
					e2.printStackTrace();
				}
				mApp.saveDefaultFileSet();
        	}
        });
        
        btnRun = new JButton();
        btnRun.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		try {
        			if (txtDestination.getText().isEmpty() && txtNameBackup.getText().isEmpty()) {
        				throw new Exception("invalid backup name and destination");
        			}
        			if (txtDestination.getText().isEmpty()) {
    					throw new Exception("invalid destination path");
        			}
        			if (txtNameBackup.getText().isEmpty()) {
						throw new Exception("invalid backup name");
					}
        		} catch (Exception e1) {
        			if (txtDestination.getText().isEmpty() && txtNameBackup.getText().isEmpty()) {
        				JOptionPane.showMessageDialog(getRootPane(),
            					"-Please enter a valid destination path.\n"
            						+ "-Please enter a valid backup name.",
            					"Invalid Entries",
            					JOptionPane.WARNING_MESSAGE);
        			}
        			else if (txtDestination.getText().isEmpty()) {
        				JOptionPane.showMessageDialog(getRootPane(),
            					"Please enter a valid destination path.",
            					"Invalid Destination",
            					JOptionPane.WARNING_MESSAGE);
        			} else {
        				JOptionPane.showMessageDialog(getRootPane(),
    							"Please enter a valid backup name.",
    							"Invalid Name",
    							JOptionPane.WARNING_MESSAGE);
        			}
        			System.err.println("Exception: " + e1.getMessage());
        			return;
        		}
				
        		
				try {
					mCurrentFileSet.setDestination(txtDestination.getText());
					mCurrentFileSet.setName(txtNameBackup.getText());
					if (FileOps.isValidDest(mCurrentFileSet) == false)  {
    					throw new Exception("backup already exists");
    				}
				} catch (Exception e2) {
					JOptionPane.showMessageDialog(getRootPane(),
							"Either a backup already exists, or you cannot write to this location.",
							"Invalid Destination",
							JOptionPane.ERROR_MESSAGE);
					System.err.println("Exception: " + e2.getMessage());
					return;
				}
				
        		btnRun.setEnabled(false);
        		panelProgress.setVisible(true);
        		progressCirc.setValue(0);
        		txtStatus.setCaretPosition(doc.getLength());
        		
        		// re-add progress circle if previous backup removed it
        		if (progressCirc.isDisplayable() == false) {
        			panelProgress.remove(lblCheck);
        			panelProgress.revalidate();
        			panelProgress.repaint();
        			panelProgress.add(progressCirc);
        			panelProgress.revalidate();
        			panelProgress.repaint();
        		}	
        		
            	try {
            		if (doc.getLength() == 0) {
            			doc.insertString(doc.getLength(), "Backup running...", null);
            		} else {
            			doc.insertString(doc.getLength(), "\nBackup running...", null);
            		}
				} catch (BadLocationException e2) {
					e2.printStackTrace();
				}
        		
        		FileOps worker = new FileOps(mCurrentFileSet, UIViewController.this);
                	
        		worker.addPropertyChangeListener(new PropertyChangeListener() {
                    public void propertyChange(PropertyChangeEvent event) {
                        if ("progress".equals(event.getPropertyName())) {
                        	progressCirc.setIndeterminate(false);
                            progressCirc.setValue((Integer)event.getNewValue());
                        }
                    }
                });
        		worker.execute();  // worker.run();
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
        try {
			mCurrentFileSet.setDestination(txtDestination.getText());
			mCurrentFileSet.setName(txtNameBackup.getText());
		} catch (Exception e1) {
			e1.printStackTrace();
		}

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
        scrollStatus.setFont(new Font("Helvetica Neue", Font.PLAIN, 12));
        
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
        				.addComponent(lblSources)
        				.addGroup(gl_panelBackup.createSequentialGroup()
        					.addGroup(gl_panelBackup.createParallelGroup(Alignment.LEADING)
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
        						.addGroup(gl_panelBackup.createSequentialGroup()
        							.addComponent(lblDestination)
        							.addGap(404)))
        					.addPreferredGap(ComponentPlacement.RELATED)
        					.addGroup(gl_panelBackup.createParallelGroup(Alignment.LEADING, false)
        						.addGroup(gl_panelBackup.createParallelGroup(Alignment.TRAILING, false)
        							.addComponent(panelNameBackup, GroupLayout.PREFERRED_SIZE, 164, GroupLayout.PREFERRED_SIZE)
        							.addGroup(gl_panelBackup.createSequentialGroup()
        								.addComponent(btnSave, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
        								.addPreferredGap(ComponentPlacement.RELATED)
        								.addComponent(btnRun, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
        								.addGap(6)))
        						.addComponent(panelProgress, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
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
        			.addPreferredGap(ComponentPlacement.RELATED, 15, Short.MAX_VALUE)
        			.addGroup(gl_panelBackup.createParallelGroup(Alignment.TRAILING, false)
        				.addGroup(gl_panelBackup.createSequentialGroup()
        					.addComponent(lblDestination)
        					.addPreferredGap(ComponentPlacement.RELATED)
        					.addGroup(gl_panelBackup.createParallelGroup(Alignment.BASELINE)
        						.addComponent(txtDestination, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
        						.addComponent(btnBrowse))
        					.addPreferredGap(ComponentPlacement.RELATED)
        					.addComponent(lblDestNote)
        					.addGap(20)
        					.addComponent(scrollStatus, GroupLayout.PREFERRED_SIZE, 90, GroupLayout.PREFERRED_SIZE)
        					.addContainerGap())
        				.addGroup(gl_panelBackup.createSequentialGroup()
        					.addComponent(panelProgress, GroupLayout.PREFERRED_SIZE, 102, GroupLayout.PREFERRED_SIZE)
        					.addPreferredGap(ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        					.addGroup(gl_panelBackup.createParallelGroup(Alignment.BASELINE)
        						.addComponent(btnRun, GroupLayout.PREFERRED_SIZE, 45, GroupLayout.PREFERRED_SIZE)
        						.addComponent(btnSave, GroupLayout.PREFERRED_SIZE, 45, GroupLayout.PREFERRED_SIZE))
        					.addGap(11))))
        );
        panelProgress.setLayout(new GridLayout(1, 1, 0, 0));
        gl_panelBackup.linkSize(SwingConstants.HORIZONTAL, new Component[] {btnAddFile, btnAddFolder, btnRemove});
        
        scrollStatus.setViewportView(txtStatus);
        
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
    

    @Override public void handleProgress(List<Progress> progressItems) {
    	for (Progress p : progressItems) {
    		if (p.sourceCopied != "") {
    			txtStatus.setCaretPosition(doc.getLength());
    	    	try {
    				doc.insertString(doc.getLength(), "\nSuccessfully copied " + p.sourceCopied, null);
    			} catch (BadLocationException e) {
    				e.printStackTrace();
    			}
    		}
    	}
    	System.out.println("got " + progressItems.size() + " progress objects.");
    }
    

	/* (non-Javadoc)
	 * @see fileops.FileOpsMessageHandler#handleCompletion()
	 */
	@Override public void handleCompletion() {
		btnRun.setEnabled(true);
		txtStatus.setCaretPosition(doc.getLength());
		try {
			doc.insertString(doc.getLength(), "\nBACKUP COMPLETE.", null);
			panelProgress.remove(progressCirc);
			panelProgress.revalidate();
			panelProgress.repaint();
			panelProgress.add(lblCheck);
			panelProgress.revalidate();
			panelProgress.repaint();
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
		System.out.println("got completion notice from FileOps");
	}
	
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private ButtonGroup grpRadioSyncSwitch;
    private ButtonGroup grpRadioFreq;
    private BufferedImage checkMark;
    private DefaultListModel<String> listModel;
    private JButton btnAddFile;
    private JButton btnAddFolder;
    private JButton btnRemove;
    private JButton btnBrowse;
    private JButton btnSave;
    private JButton btnRun;
    private JLabel lblAppTitle;
    private JLabel lblCheck;
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
    private JPanel panelProgress;
    private JProgressBar progressCirc;
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
    private JTextPane txtStatus;
    private JXDatePicker jXDatePicker;
    private JMenuBar menuBar;
    private JMenu menuOpen;
    private JMenuItem menuItemLog;
    private JMenuItem menuItemManual;
    private JList<String> listSources;
}

class ProgressCircle extends BasicProgressBarUI {
	
    @Override public Dimension getPreferredSize(JComponent c) {
        Dimension d = super.getPreferredSize(c);
        int v = Math.max(d.width, d.height);
        d.setSize(v, v);
        return d;
    }
    
    // set progressCirc string color
    @Override protected Color getSelectionBackground() { return UIViewController.drkGreen; }
    
    @Override public void paint(Graphics g, JComponent c) {
        Insets b = progressBar.getInsets();   // area for border
        int barRectWidth  = progressBar.getWidth()  - b.right - b.left;
        int barRectHeight = progressBar.getHeight() - b.top - b.bottom;
        if (barRectWidth <= 0 || barRectHeight <= 0) {
            return;
        }

        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        double degree = 360 * progressBar.getPercentComplete();
        double sz = Math.min(barRectWidth, barRectHeight);
        double cx = b.left + barRectWidth  * .5;
        double cy = b.top  + barRectHeight * .5;
        double or = sz * .5;
        double ir = or * .8;    // change circle width
        Shape inner  = new Ellipse2D.Double(cx - ir, cy - ir, ir * 2, ir * 2);
        Shape outer  = new Ellipse2D.Double(cx - or, cy - or, sz, sz);
        Shape sector = new Arc2D.Double(cx - or, cy - or, sz, sz, 90 - degree, degree, Arc2D.PIE);

        Area foreground = new Area(sector);
        Area background = new Area(outer);
        Area hole = new Area(inner);

        foreground.subtract(hole);
        background.subtract(hole);

        // draw the track
        g2.setPaint(new Color(0xDDDDDD));
        g2.fill(background);

        // draw the circular sector
        g2.setPaint(new Color(150, 255, 105));
        g2.fill(foreground);
        g2.dispose();

        // deal with possible text painting
        if (progressBar.isStringPainted()) {
            paintString(g, b.left, b.top, barRectWidth, barRectHeight, 0, b);
        }
    }
}

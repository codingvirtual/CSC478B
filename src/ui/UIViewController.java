package ui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Arc2D;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.plaf.basic.BasicProgressBarUI;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import org.jdesktop.swingx.JXDatePicker;
import app.Application;
import core.FileSet;
import core.FileOps;
import core.FileOpsMessageHandler;
import core.Progress;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Creates a graphical user interface to control backup operations.
 * 
 * @author Ashley Robertson
 * @version 1.0.0
 *
 */
public class UIViewController extends JFrame implements FileOpsMessageHandler {
	private static final long serialVersionUID = 1L;
	/** The green color used for selections and highlights. */
	static final Color drkGreen = new Color(0, 180, 0);
	/** The red color used to mark invalid fields in the form. */
	static final Color errorRed = new Color(255, 178, 178);
	/** Flag true if an identical backup already exists. */
	private static Boolean backupExists = false;
	/** Flag true if the file system failed to write. */
	private static Boolean cannotWrite = false;
	/** Flag true if the destination path is valid. */
	private static Boolean destOK = true;
	/** Flag true if the backup name is valid. */
	private static Boolean nameOK = true;
	/** The default backup name. */
	private String defaultName;
	/** A container for status message text. */
	private Document doc;
	/** An instance of the {@code Application} class.
	 *  @see app.Application */
	private Application mApp;
	/** The current file set. */
	private FileSet mCurrentFileSet;
	/** The object that relies on {@code SwingWorker<Void, Progress>} to run the backup in a background thread.
	 *  @see core.FileOps#doInBackground() */
	private FileOps worker;

	/**
	 * Creates new form UIViewController.
	 * 
	 * @param app an instance of the {@link app.Application} class
	 * @throws Exception if this constructor fails to initialize
	 */
	public UIViewController(Application app) throws Exception {
		addWindowListener(new WindowAdapter() {
			@Override public void windowClosing(WindowEvent e) {
				if (worker != null && !worker.isDone()) {
					JOptionPane.showMessageDialog(getRootPane(),
							"A backup is currently in progress.\n" +
									"To avoid creating a corrupt or incomplete backup, please wait.",
									"Backup Running",
									JOptionPane.WARNING_MESSAGE);
					// v2.0 - signal cancel, set mayInterruptIfRunning parameter to true
					// worker.cancel(true);
				} else {
					System.exit(0);
				}
			}
		});

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
			System.err.println("Nimbus is unavailable; System look and feel presented.");
		}

		File icon16 = new File("res/icons/icon16.png");
		File icon32 = new File("res/icons/icon32.png");
		File icon64 = new File("res/icons/icon64.png");
		File icon128 = new File("res/icons/icon128.png");

		final List<Image> icons  = new ArrayList<Image>();
		icons.add(ImageIO.read(icon16));
		icons.add(ImageIO.read(icon32));
		icons.add(ImageIO.read(icon64));
		icons.add(ImageIO.read(icon128));

		/* dynamically load Apple's Application.class since it cannot
		 * be instantiated on Windows platforms
		 */
		if (System.getProperty("os.name").startsWith("Mac OS")) {
			Class<?> c = Class.forName("com.apple.eawt.Application");
			Object obj = c.newInstance();
			Class<?>[] paramTypes = new Class[1];
			paramTypes[0] = Image.class;
			// use reflection to access the appropriate method
			Method m = c.getMethod("setDockIconImage", paramTypes);
			m.invoke(obj, ImageIO.read(icon128));
		}

		setIconImages(icons);
		setTitle("Mirror");
		setSize(new Dimension(685, 711));
		setResizable(false);
		getContentPane().setBackground(new Color(242, 242, 242));
		getContentPane().setFont(new Font("Helvetica Neue", 0, 14));
		menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		menuOpen = new JMenu("Open...");
		menuOpen.setFont(new Font("Helvetica Neue", Font.PLAIN, 14));
		menuBar.add(menuOpen);
		menuItemLog = new JMenuItem("Log");
		menuItemLog.setFont(new Font("Helvetica Neue", Font.PLAIN, 14));
		// hide the log until it's ready for v2.0
		// menuOpen.add(menuItemLog);
		// JSeparator separator = new JSeparator();
		// menuOpen.add(separator);
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
	 * Some content in this method may be regenerated by the Form Editor.
	 */
	private void initComponents() {
		/* Requirement: 1.1.1.1
		 * The user must be able to create a list of files that the application
		 * is to operate on.
		 */
		listModel = mCurrentFileSet;
		listSources = new JList<String>(listModel);
		listSources.setFont(new Font("Helvetica Neue", Font.PLAIN, 14));
		panelSettings = new JPanel();
		panelSettings.setEnabled(false);
		panelFreq = new JPanel();
		panelFreq.setEnabled(false);
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
		progressCirc.setMinimum(0);
		progressCirc.setMaximum(listModel.getSize());
		progressCirc.setIndeterminate(false);
		progressCirc.setStringPainted(true);
		progressCirc.setFont(new Font("Helvetica Neue", Font.BOLD, 14));
		panelProgress.add(progressCirc);
		panelProgress.setVisible(false);
		scrollSources = new JScrollPane();
		scrollSources.setFont(new Font("Helvetica Neue", Font.PLAIN, 14));
		scrollStatus = new JScrollPane();
		scrollStatus.setViewportBorder(null);
		lblAppTitle = new JLabel();
		lblAppTitle.setIcon(new ImageIcon(UIViewController.class.getResource("/icons/mirror.png")));
		lblAppTitle.setFocusTraversalKeysEnabled(false);
		lblAppTitle.setRequestFocusEnabled(false);
		lblAppTitle.setFocusable(false);
		lblAppTitle.setIconTextGap(0);
		lblSchedSync = new JLabel();
		lblSchedSync.setEnabled(false);
		lblDate = new JLabel();
		lblDate.setEnabled(false);
		lblTime = new JLabel();
		lblTime.setEnabled(false);
		lblSources = new JLabel();
		lblDestination = new JLabel();
		lblDestNote = new JLabel();
		lblDestNote.setVisible(false);
		lblDestNote.setEnabled(false);
		lblDestNote.setForeground(Color.GRAY);
		jXDatePicker = new JXDatePicker();
		jXDatePicker.setEnabled(false);
		jXDatePicker.getEditor().setEnabled(false);
		spinTime = new JSpinner();
		spinTime.setEnabled(false);
		txtStatus = new JTextPane();
		txtStatus.setForeground(Color.GRAY);
		txtStatus.setFont(new Font("Helvetica Neue", Font.BOLD | Font.ITALIC, 13));
		txtStatus.setOpaque(false);
		txtStatus.setEditable(false);
		txtStatus.setBackground(new Color(0, 0, 0, 0));
		txtStatus.setMargin(new java.awt.Insets(0, 0, 0, 0));
		doc = txtStatus.getDocument();
		/* Requirement: 1.1.7.1
		 * The user shall name a backup, and the backup shall be saved in a folder with
		 * the chosen name at the root of the destination.
		 */
		txtNameBackup = new JTextField();
		txtNameBackup.setFont(new Font("Helvetica Neue", Font.PLAIN, 12));
		defaultName = getBackupDateTime();
		txtNameBackup.setText(defaultName);
		txtNameBackup.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				txtNameBackup.setText(txtNameBackup.getText().trim());
				if (txtNameBackup.getText().isEmpty()) {
					txtNameBackup.setBackground(errorRed);
					nameOK = false;
				} else {
					try {
						mCurrentFileSet.setName(txtNameBackup.getText());
						if (txtNameBackup.getBackground() == errorRed) {
							txtNameBackup.setBackground(Color.WHITE);
						}
						nameOK = true;
					} catch (Exception e1) {
						txtNameBackup.setBackground(errorRed);
						System.err.println("Exception: invalid backup name");
						nameOK = false;
					}
				}
			}
			@Override
			public void focusGained(FocusEvent e) {
				if (txtNameBackup.getBackground() == errorRed) {
					txtNameBackup.setBackground(Color.WHITE);
				}
			}
		});
		/* Requirement: 1.1.2.1
		 * The user shall be able to specify a destination folder or drive for the backup
		 * operation to store the backup into.
		 */
		txtDestination = new JTextField();
		txtDestination.setFocusable(false);
		txtDestination.setRequestFocusEnabled(false);
		txtDestination.setEditable(false);
		if (mCurrentFileSet.getDestination() != null) {
			txtDestination.setText(mCurrentFileSet.getDestination());
		} else {
			// set default destination to user's system-dependent home directory
			txtDestination.setText(System.getProperty("user.home"));
		}

		// set initial name and destination
		try {
			mCurrentFileSet.setName(txtNameBackup.getText());
			mCurrentFileSet.setDestination(txtDestination.getText());
		} catch (Exception e1) {
			System.err.println("Exception: invalid backup name and/or destination path");
		}

		grpRadioSyncSwitch = new ButtonGroup();
		grpRadioFreq = new ButtonGroup();

		try {
			checkMark = ImageIO.read(new File("res/icons/checkmark.png"));
		} catch (IOException e1) {
			System.err.println("IOException: unable to locate resource");
		}
		lblCheck = new JLabel(new ImageIcon(checkMark));

		radioOn = new JRadioButton();
		radioOn.setEnabled(false);
		radioOn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});

		radioOff = new JRadioButton();
		radioOff.setEnabled(false);
		radioOff.setSelected(true);
		radioOff.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});

		radioDaily = new JRadioButton();
		radioDaily.setEnabled(false);
		radioDaily.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});

		radioWeekly = new JRadioButton();
		radioWeekly.setEnabled(false);
		radioWeekly.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});

		radioMonthly = new JRadioButton();
		radioMonthly.setEnabled(false);
		radioMonthly.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});

		/* Requirement: 1.1.3.1
		 * The user must be able to modify the list of files to operate on prior to backup.
		 * After backup, the user can modify the list and save as a new backup.
		 */
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
		btnAddFolder.setEnabled(true);
		btnAddFolder.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(getRootPane(),
						"This feature is coming soon...",
						"Add Folder",
						JOptionPane.WARNING_MESSAGE);
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
						if (txtDestination.getBackground() == errorRed) {
							txtDestination.setBackground(Color.WHITE);
						}
						destOK = true;
					} catch (Exception e1) {
						txtDestination.setBackground(errorRed);
						JOptionPane.showMessageDialog(getRootPane(),
								"Please provide a valid destination path.",
								"Invalid Destination",
								JOptionPane.WARNING_MESSAGE);
						System.err.println("Exception: invalid destination path");
						destOK = false;
					}
				}
			}
		});

		btnSave = new JButton();
		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				// validate file set and show dialogs if necessary
				if (!validateFileSet()) { return; }

				try {
					mApp.saveDefaultFileSet();
				} catch (IOException e1) {
					JOptionPane.showMessageDialog(getRootPane(),
							"The backup could not be saved.\n" +
									"Please make sure you have sufficient privileges.",
									"Failed to Save",
									JOptionPane.ERROR_MESSAGE);
					System.err.println("IOException: failed to save backup");
					return;
				}

				txtStatus.setCaretPosition(doc.getLength());
				// Scheduled backup is ON
				if (radioOn.isSelected()) {
					try {
						if (doc.getLength() == 0) {
							doc.insertString(doc.getLength(), "Backup settings saved.", null);
						} else {
							doc.insertString(doc.getLength(), "\nBackup settings saved.", null);
						}
					} catch (BadLocationException e1) {
						System.err.println("Bad caret position; cannot insert string.");
					}
				} else {
					// Scheduled backup is OFF
					try {
						if (doc.getLength() == 0) {
							doc.insertString(doc.getLength(), "Backup settings saved.", null);
						} else {
							doc.insertString(doc.getLength(), "\nBackup settings saved.", null);
						}
					} catch (BadLocationException e1) {
						System.err.println("Bad caret position; cannot insert string.");
					}
				}

				if (!panelProgress.isVisible()) { panelProgress.setVisible(true); }
				panelProgress.remove(progressCirc);
				panelProgress.revalidate();
				panelProgress.repaint();
				panelProgress.add(lblCheck);
				panelProgress.revalidate();
				panelProgress.repaint();
			}
		});

		/* Requirement: 1.1.4.1
		 * The user shall have the capability of executing a backup on demand.
		 */
		btnRun = new JButton();
		btnRun.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				// validate file set and show dialogs if necessary
				if (!validateFileSet()) { return; }

				btnRun.setEnabled(false);
				panelProgress.setVisible(true);
				progressCirc.setValue(0);
				progressCirc.setMaximum(100);

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
				} catch (BadLocationException e1) {
					System.err.println("Bad caret position; cannot insert string.");
				}

				/* Requirement: 1.1.5.1
				 * The user must be notified of the status of any backup (failure or success).
				 */
				worker = null;
				try {
					worker = new FileOps(mCurrentFileSet, UIViewController.this);
				} catch (Exception e1) {
					JOptionPane.showMessageDialog(getRootPane(),
							"The backup failed to run.\n" +
									"Please try again.",
									"Unknown Error",
									JOptionPane.ERROR_MESSAGE);
					System.err.println("Exception: failed to run backup");
					return;
				}
				worker.execute();
			}
		});


		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

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
						.addGap(21)
						.addComponent(radioWeekly, GroupLayout.DEFAULT_SIZE, 80, Short.MAX_VALUE)
						.addGap(13)
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

		JLabel lblComingSoon = new JLabel("Coming soon...");
		lblComingSoon.setForeground(Color.LIGHT_GRAY);
		lblComingSoon.setFont(new Font("Helvetica Neue", Font.PLAIN, 18));

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
										.addGroup(gl_panelSettings.createParallelGroup(Alignment.LEADING)
												.addGroup(gl_panelSettings.createSequentialGroup()
														.addPreferredGap(ComponentPlacement.RELATED)
														.addComponent(spinTime, GroupLayout.PREFERRED_SIZE, 149, GroupLayout.PREFERRED_SIZE))
												.addGroup(gl_panelSettings.createSequentialGroup()
														.addGap(110)
														.addComponent(lblComingSoon)))))
						.addContainerGap(132, Short.MAX_VALUE))
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
						.addGroup(gl_panelSettings.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_panelSettings.createSequentialGroup()
										.addGap(18)
										.addComponent(panelFreq, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
								.addGroup(gl_panelSettings.createSequentialGroup()
										.addGap(48)
										.addComponent(lblComingSoon)))
						.addContainerGap(39, Short.MAX_VALUE))
				);
		panelSettings.setLayout(gl_panelSettings);

		panelBackup.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "BACKUP", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Helvetica Neue", 0, 14))); // NOI18N
		panelBackup.setFont(new Font("Helvetica Neue", Font.PLAIN, 16)); // NOI18N

		lblSources.setFont(new Font("Helvetica Neue", Font.PLAIN, 14)); // NOI18N
		lblSources.setText("Source File(s) / Folder(s):");

		lblDestination.setFont(new Font("Helvetica Neue", Font.PLAIN, 14)); // NOI18N
		lblDestination.setText("Destination:");

		txtDestination.setFont(new Font("Helvetica Neue", Font.PLAIN, 14)); // NOI18N

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

		lblAppTitle.setFont(new Font("Helvetica Neue", Font.PLAIN, 15));

		javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
		layout.setHorizontalGroup(
				layout.createParallelGroup(Alignment.TRAILING)
				.addGroup(layout.createSequentialGroup()
						.addContainerGap()
						.addGroup(layout.createParallelGroup(Alignment.LEADING)
								.addGroup(layout.createParallelGroup(Alignment.TRAILING)
										.addComponent(panelSettings, GroupLayout.PREFERRED_SIZE, 679, GroupLayout.PREFERRED_SIZE)
										.addComponent(panelBackup, GroupLayout.PREFERRED_SIZE, 679, GroupLayout.PREFERRED_SIZE))
								.addGroup(layout.createSequentialGroup()
										.addGap(584)
										.addComponent(lblAppTitle)))
						.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
				);
		layout.setVerticalGroup(
				layout.createParallelGroup(Alignment.LEADING)
				.addGroup(layout.createSequentialGroup()
						.addContainerGap()
						.addComponent(lblAppTitle)
						.addGap(1)
						.addComponent(panelSettings, GroupLayout.PREFERRED_SIZE, 219, GroupLayout.PREFERRED_SIZE)
						.addGap(20)
						.addComponent(panelBackup, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
				);
		getContentPane().setLayout(layout);

		pack();
	}// </editor-fold>//GEN-END:initComponents
	
	/**
	 * Validates the source file set before every save or run.
	 * 
	 * @return true if the file set is valid or false if the file set is invalid
	 */
	private Boolean validateFileSet() {
		// update current date and time
		if (txtNameBackup.getText().equals(defaultName)) {
			defaultName = getBackupDateTime();
			txtNameBackup.setText(defaultName);
		}
		if (mCurrentFileSet.isEmpty()) {
			JOptionPane.showMessageDialog(getRootPane(),
					"Please add at least one source to continue.",
					"No Source(s)",
					JOptionPane.WARNING_MESSAGE);
			return false;
		}
		if (!destOK && !nameOK) {
			JOptionPane.showMessageDialog(getRootPane(),
					"-Please enter a valid destination path.\n"
							+ "-Please enter a valid backup name.",
							"Invalid Entries",
							JOptionPane.WARNING_MESSAGE);
			return false;
		}
		else if (!destOK) {
			JOptionPane.showMessageDialog(getRootPane(),
					"Please provide a valid destination path.",
					"Invalid Destination",
					JOptionPane.WARNING_MESSAGE);
			return false;
		}
		else if (!nameOK) {
			JOptionPane.showMessageDialog(getRootPane(),
					"Please enter a valid backup name.",
					"Invalid Name",
					JOptionPane.WARNING_MESSAGE);
			txtNameBackup.requestFocus();
			return false;
		} else {
			try {
				mCurrentFileSet.setDestination(txtDestination.getText());
				mCurrentFileSet.setName(txtNameBackup.getText());
				if (FileOps.backupExists(mCurrentFileSet)) {
					backupExists = true;
					throw new Exception("backup already exists");
				}
				if (FileOps.cannotWrite(mCurrentFileSet)) {
					cannotWrite = true;
					throw new Exception("cannot write to specified location");
				}
			} catch (Exception e1) {
				if (backupExists)  {
					JOptionPane.showMessageDialog(getRootPane(),
							"A backup with this name already exists.\n" +
									"Please choose another name or change the destination to continue.",
									"Backup Found",
									JOptionPane.WARNING_MESSAGE);
					backupExists = false;
				} else if (cannotWrite) {
					JOptionPane.showMessageDialog(getRootPane(),
							"You cannot write to this location.\n" +
									"Please choose another destination to continue.",
									"No Access",
									JOptionPane.ERROR_MESSAGE);
					cannotWrite = false;
				}
				System.err.println("Exception: " + e1.getMessage());
				return false;
			}
		}
		return true;
	}
	
	/** 
	 * Returns the date and time String for the default backup name.
	 * 
	 * @return the backup date and time String
	 */
	private String getBackupDateTime() {
		String date = new SimpleDateFormat("MM.dd.yy-HH.mm").format(new Date());
		return "Backup" + date;
	}

	/* Requirement: 1.1.5.1
	 * The user must be notified of the status of any backup (failure or success).
	 */
	
	/**
	 * Receives progress data chunks from {@code FileOpsMessageHandler}.
	 * @see core.FileOpsMessageHandler
	 * @see core.FileOps#process(List)
	 */
	@Override public void handleProgress(List<Progress> progressItems) {
		for (Progress p : progressItems) {
			if (p.sourceCopied != "") {
				txtStatus.setCaretPosition(doc.getLength());
				try {
					doc.insertString(doc.getLength(), "\nSuccessfully copied " + p.sourceCopied, null);
				} catch (BadLocationException e) {
					System.err.println("Bad caret position; cannot insert string.");
				}
			}
			int pctComplete = (int) ((p.completedBytes / (float) p.totalBytes) * 100);
			if (progressCirc.getValue() < pctComplete) {
				progressCirc.setValue(pctComplete);
			}
			// System.out.println(pctComplete + "% complete.");
		}
		// System.out.println("got " + progressItems.size() + " progress objects.");
	}
	
	/**
	 * Receives the completion notice from {@code FileOpsMessageHandler}.
	 * @see core.FileOpsMessageHandler
	 * @see core.FileOps#done()
	 */
	@Override public void handleCompletion() {
		btnRun.setEnabled(true);
		txtStatus.setCaretPosition(doc.getLength());
		try {
			doc.insertString(doc.getLength(), "\nBackup complete.", null);
			panelProgress.remove(progressCirc);
			panelProgress.revalidate();
			panelProgress.repaint();
			panelProgress.add(lblCheck);
			panelProgress.revalidate();
			panelProgress.repaint();
		} catch (BadLocationException e) {
			System.err.println("Bad caret position; cannot insert string.");
		} finally {
			System.out.println("got completion notice from FileOps");
		}
	}

	// Variables declaration - do not modify//GEN-BEGIN:variables
	
	/** The group of radio buttons that switch scheduled backups on or off. */
	private ButtonGroup grpRadioSyncSwitch;
	/** The group of radio buttons that contains frequencies for scheduled backups. */
	private ButtonGroup grpRadioFreq;
	/** The check mark image that displays when a backup completes. */
	private BufferedImage checkMark;
	/** Contains the source file set that displays in {@code listSources}.
	 *  @see #listSources */
	private DefaultListModel<String> listModel;
	/** The button that adds a file to the source list. */
	private JButton btnAddFile;
	/** The button that adds a folder to the source list. */
	private JButton btnAddFolder;
	/** The button that removes a selected item from the source list. */
	private JButton btnRemove;
	/** The button that allows the user to set the destination path. */
	private JButton btnBrowse;
	/** The button that saves all backup settings. */
	private JButton btnSave;
	/** The button that starts the backup process. */
	private JButton btnRun;
	/** Mirror's title label. */
	private JLabel lblAppTitle;
	/** The label that contains the check mark icon image. */
	private JLabel lblCheck;
	/** The scheduled sync/backup label. */
	private JLabel lblSchedSync;
	/** The source list label. */
	private JLabel lblSources;
	/** The destination path label. */
	private JLabel lblDestination;
	/** The label for the destination note. */
	private JLabel lblDestNote;
	/** The date label. */
	private JLabel lblDate;
	/** The time label. */
	private JLabel lblTime;
	/** Displays the source data that is within {@code listModel}. 
	 *  @see #listModel */
	private JList<String> listSources;
	/** The "Open..." menu. */
	private JMenu menuOpen;
	/** The menu bar. */
	private JMenuBar menuBar;
	/** The menu item for the log. */
	private JMenuItem menuItemLog;
	/** The menu item for the user's manual. */
	private JMenuItem menuItemManual;
	/** The settings panel. */
	private JPanel panelSettings;
	/** The backup panel. */
	private JPanel panelBackup;
	/** The panel that contains the text field for the backup name. */
	private JPanel panelNameBackup;
	/** The panel that contains the backup frequency radio buttons. */
	private JPanel panelFreq;
	/** The panel that may contain the circular progress bar or the check mark image. */
	private JPanel panelProgress;
	/** The circular progress bar. */
	private JProgressBar progressCirc;
	/** The radio button that indicates when scheduled backups are on. */
	private JRadioButton radioOn;
	/** The radio button that indicates when scheduled backups are off. */
	private JRadioButton radioOff;
	/** The radio button that indicates a daily backup frequency. */
	private JRadioButton radioDaily;
	/** The radio button that indicates a weekly backup frequency. */
	private JRadioButton radioWeekly;
	/** The radio button that indicates a monthly backup frequency. */
	private JRadioButton radioMonthly;
	/** The scroll pane for the source list. */
	private JScrollPane scrollSources;
	/** The scroll pane for status message text. */
	private JScrollPane scrollStatus;
	/** The time spinner. */
	private JSpinner spinTime;
	/** The text field that contains the destination path. */
	private JTextField txtDestination;
	/** The text field that contains the backup name. */
	private JTextField txtNameBackup;
	/** The text pane that contains status message text. */
	private JTextPane txtStatus;
	/** The {@code SwingX} date picker. */
	private JXDatePicker jXDatePicker;
}

/**
 * An inner class of {@code UIViewController} that paints a circular progress bar by subclassing {@code BasicProgressBarUI}.
 */
class ProgressCircle extends BasicProgressBarUI {
	
	/**   
	 * Returns the preferred size of the circular progress bar.
	 * 
	 * @return the {@code Dimension} object that encapsulates the preferred width and height integers
	 */
	@Override public Dimension getPreferredSize(JComponent c) {
		Dimension d = super.getPreferredSize(c);
		int v = Math.max(d.width, d.height);
		d.setSize(v, v);
		return d;
	}

	/**
	 * Sets the text color of the percentage inside the progress bar.
	 * 
	 * @return the color for the percentage String
	 */
	@Override protected Color getSelectionBackground() { return Color.GRAY; }
	
	/**
	 * Paints the circular progress bar. 
	 * 
	 * @param g the object that allows the application to draw onto a component
	 * @param c the component object to draw on
	 */
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
		g2.setPaint(new Color(50, 204, 220));
		g2.fill(foreground);
		g2.dispose();

		// deal with possible text painting
		if (progressBar.isStringPainted()) {
			paintString(g, b.left, b.top, barRectWidth, barRectHeight, 0, b);
		}
	}
}

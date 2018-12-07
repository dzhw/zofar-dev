package eu.dzhw.zofar.management.dev.automation.deploy.main;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingWorker;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.dzhw.zofar.management.dev.automation.AbstractExecutor.ABSTRACTPARAMETER;
import eu.dzhw.zofar.management.dev.automation.AbstractExecutor.ParameterMap;
import eu.dzhw.zofar.management.dev.automation.deploy.Executor;
//import eu.dzhw.zofar.management.dev.automation.reminder.Executor;
import eu.dzhw.zofar.management.utils.files.CSVClient;
import eu.dzhw.zofar.management.utils.files.DirectoryClient;
import eu.dzhw.zofar.management.utils.files.FileClient;
import net.miginfocom.swing.MigLayout;

public class Webstart implements ActionListener, ChangeListener, FocusListener, Observer {

	/** The Constant LOGGER. */
	private static final Logger LOGGER = LoggerFactory.getLogger(Webstart.class);

	/** The task. */
	private Task task;

	/** The frame. */
	private JFrame frame;

	private Map<String, String> additionalArgs;

	private JTextArea consolePane;
	private JLabel lblDBServer;
	private JLabel lblDbName;
	private JLabel lblDbPort;
	private JLabel lblDbUser;
	private JLabel lblDbPassword;
	private JLabel lblMailServer;
	private JLabel lblMailUser;
	private JLabel lblMailPassword;
	private JLabel lblMailPath;
	private JTextField txtDbname;
	private JTextField txtDbserver;
	private JTextField txtDbport;
	private JTextField txtDbuser;
	private JPasswordField pwdDbpass;
	private JTextField txtMailserver;
	private JTextField txtMailuser;
	private JTextField txtMailpath;
	private JPasswordField pwdMailpass;
	private JLabel lblInvitations;
	private JLabel lblRemind;
	private JLabel lblIgnore;
	private JButton btnProcess;

	private JButton saveIgnoreBtn;

	private JButton saveRemindBtn;

	private JButton loadBtn;

	private JFileChooser fc;

	private File remindFile;

	private File ignoreFile;

	private File invitationsFile;
	private String invitationDefaultLabel;

	private File clientTempDir;

	/** The format. */
	private NumberFormat format;

	private String blacklistDefaultLabel;

	private JButton blackBtn;

	private JLabel lblBlacklist;

	private File blacklistFile;

	private JLabel lblMailField;
	private JComboBox<String> mailField;

	/**
	 * The Class Task.
	 */
	private class Task extends SwingWorker<Void, Void> {
		@Override
		public Void doInBackground() throws Exception {
			for (final Map.Entry<String, String> property : Webstart.this.additionalArgs.entrySet()) {
				LOGGER.info("property : {} = {}", property.getKey(), property.getValue());
			}
			Webstart.this.btnProcess.setEnabled(false);
//			Webstart.this.remindFile = null;
//			Webstart.this.ignoreFile = null;
//			Webstart.this.saveRemindBtn.setEnabled(false);
//			Webstart.this.saveIgnoreBtn.setEnabled(false);

			Webstart.this.frame.revalidate();
			Webstart.this.frame.repaint();

//			final String dbServer = Webstart.this.txtDbserver.getText();
//			final String dbName = Webstart.this.txtDbname.getText();
//			final String dbPort = Webstart.this.txtDbport.getText();
//			final String dbUser = Webstart.this.txtDbuser.getText();
//			final String dbPass = new String(Webstart.this.pwdDbpass.getPassword());
//
//			final String mailboxUser = Webstart.this.txtMailuser.getText();
//			final String mailboxPass = new String(Webstart.this.pwdMailpass.getPassword());
//			final String mailboxServer = Webstart.this.txtMailserver.getText();
//			final String mailboxPath = Webstart.this.txtMailpath.getText();

			try {
				LOGGER.info("execute");
				final Executor executor = Executor.getInstance();
//				File tmpRemind = FileClient.getInstance().createOrGetFile("remind" + System.currentTimeMillis(), ".csv", Webstart.this.clientTempDir);
//				File tmpIgnore = FileClient.getInstance().createOrGetFile("ignore" + System.currentTimeMillis(), ".csv", Webstart.this.clientTempDir);
//				ParameterMap<ABSTRACTPARAMETER, Object> parameter = executor.getParameterMap(mailboxUser, mailboxPath, mailboxServer, mailboxUser, mailboxPass, dbServer, dbName, dbPort, dbUser, dbPass, Webstart.this.invitationsFile,Webstart.this.blacklistFile,tmpRemind, tmpIgnore, "token", (String)Webstart.this.mailField.getSelectedItem());
//				executor.process(parameter);
//				Webstart.this.remindFile = tmpRemind;
//				Webstart.this.ignoreFile = tmpIgnore;
				LOGGER.info("done");

			} catch (Exception e) {
				Webstart.this.showError(e);
			}
			return null;
		}

		@Override
		public void done() {
//			Webstart.this.btnProcess.setEnabled(Webstart.this.invitationsFile != null);
//			Webstart.this.saveRemindBtn.setEnabled(Webstart.this.remindFile != null);
//			Webstart.this.saveIgnoreBtn.setEnabled(Webstart.this.ignoreFile != null);
		}
	}

	private class CustomOutputStream extends OutputStream {

		/** The field. */
		private final JTextArea field;

		/**
		 * Instantiates a new custom output stream.
		 * 
		 * @param field
		 *            the field
		 */
		public CustomOutputStream(final JTextArea field) {
			this.field = field;
			if (this.field != null)
				this.field.setLineWrap(true);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.io.OutputStream#write(int)
		 */
		@Override
		public void write(final int b) throws IOException {
			if (field == null)
				return;
			// redirects data to the text area
			this.field.append(String.valueOf((char) b));
			// scrolls the text area to the end of data
			this.field.setCaretPosition(this.field.getDocument().getLength());
		}
	}

	/**
	 * Launch the application.
	 * 
	 * @param args
	 *            the arguments
	 */
	public static void main(final String[] args) {
		final Map<String, String> additionalArgs = new HashMap<String, String>();

		if ((args != null) && (args.length > 0)) {
			final int argCount = args.length;
			for (int a = 0; a < argCount; a++) {
				final String arg = args[a];
				final String[] splittedArg = arg.split("=");
				if ((splittedArg != null) && (splittedArg.length == 2)) {
					final String key = splittedArg[0].trim();
					final String value = splittedArg[1].trim();
					additionalArgs.put(key, value);
				}
				LOGGER.info("Argument {}", args[a]);
			}
		}

		additionalArgs.put("dbServer", "XXXXXX");
		additionalArgs.put("dbName", "XXXXXX");
		additionalArgs.put("dbPort", "5432");
		additionalArgs.put("dbUser", "XXXXXX");
		additionalArgs.put("dbPass", "XXXXXX");

		additionalArgs.put("mailbox", "xxxx@dzhw.eu");
		additionalArgs.put("mailboxUser", "xxxx@dzhw.eu");
		additionalArgs.put("mailboxPass", "XXXXXX");
		additionalArgs.put("mailboxServer", "XXXXXX");
		additionalArgs.put("mailboxPath", "BounceMails");

		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					final Webstart window = new Webstart(additionalArgs);
					window.frame.setVisible(true);
				} catch (final Exception e) {
					LOGGER.error("run failed ", e);
				}
			}
		});
	}

	/**
	 * @wbp.parser.entryPoint
	 */
	public Webstart(final Map<String, String> additionalArgs) {
		super();
		this.additionalArgs = additionalArgs;
		this.initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 * @wbp.parser.entryPoint
	 */
	private void initialize() {
		this.clientTempDir = DirectoryClient.getInstance().getTemp();
		this.format = new DecimalFormat();
		this.frame = new JFrame();
		this.frame.setTitle("Zofar Reminder Filter");
		this.frame.setBounds(100, 100, 591, 500);
		this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//		frame.getContentPane().setLayout(new FormLayout(new ColumnSpec[] {},
//			new RowSpec[] {}));
		frame.getContentPane().setLayout(new MigLayout("", "[fill][fill][fill][grow,fill]", "[fill][fill][fill][fill][fill][fill][fill][fill][fill][fill][fill][][][][][][][][132.00,fill]"));
	
		lblDbName = new JLabel("DB Name:");
		frame.getContentPane().add(lblDbName, "cell 1 0,alignx left,aligny center");
		
		txtDbname = new JTextField();
		txtDbname.setText("db_name");
		frame.getContentPane().add(txtDbname, "cell 3 0,growx,aligny center");
		txtDbname.setColumns(10);

		lblDBServer = new JLabel("DB Server:");
		frame.getContentPane().add(lblDBServer, "cell 1 1,alignx left,aligny center");

		txtDbserver = new JTextField();
		txtDbserver.setText("db_server");
		frame.getContentPane().add(txtDbserver, "cell 3 1,growx,aligny center");
		txtDbserver.setColumns(10);

		lblDbPort = new JLabel("DB Port:");
		frame.getContentPane().add(lblDbPort, "cell 1 2,alignx left,aligny center");

		txtDbport = new JFormattedTextField(this.format);
		txtDbport.setText("db_port");
		frame.getContentPane().add(txtDbport, "cell 3 2,growx,aligny center");
		txtDbport.setColumns(10);

		lblDbUser = new JLabel("DB User:");
		frame.getContentPane().add(lblDbUser, "cell 1 3,alignx left,aligny center");

		txtDbuser = new JTextField();
		txtDbuser.setText("db_user");
		frame.getContentPane().add(txtDbuser, "cell 3 3,growx,aligny center");
		txtDbuser.setColumns(10);

		lblDbPassword = new JLabel("DB Password:");
		frame.getContentPane().add(lblDbPassword, "cell 1 4,alignx left,aligny center");

		pwdDbpass = new JPasswordField();
		pwdDbpass.setText("db_pass");
		frame.getContentPane().add(pwdDbpass, "cell 3 4,growx,aligny center");

		lblMailServer = new JLabel("Mail Server:");
		frame.getContentPane().add(lblMailServer, "cell 1 6,alignx left,aligny center");

		txtMailserver = new JTextField();
		txtMailserver.setText("mail_server");
		frame.getContentPane().add(txtMailserver, "cell 3 6,growx,aligny center");
		txtMailserver.setColumns(10);

		lblMailUser = new JLabel("Mail User:");
		frame.getContentPane().add(lblMailUser, "cell 1 7,alignx left,aligny center");

		txtMailuser = new JTextField();
		txtMailuser.setText("mail_user");
		frame.getContentPane().add(txtMailuser, "cell 3 7,growx,aligny center");
		txtMailuser.setColumns(10);

		lblMailPassword = new JLabel("Mail Password:");
		frame.getContentPane().add(lblMailPassword, "cell 1 8,alignx left,aligny center");

		pwdMailpass = new JPasswordField();
		pwdMailpass.setText("mail_path");
		frame.getContentPane().add(pwdMailpass, "cell 3 8,growx,aligny center");

		lblMailPath = new JLabel("Mail Path:");
		frame.getContentPane().add(lblMailPath, "cell 1 9,aligny center");

		txtMailpath = new JTextField();
		txtMailpath.setText("mail_path");
		frame.getContentPane().add(txtMailpath, "cell 3 9,growx,aligny center");
		txtMailpath.setColumns(10);

		lblInvitations = new JLabel("Invitations:");
		frame.getContentPane().add(lblInvitations, "cell 1 11,alignx left,aligny center");

		invitationDefaultLabel = "Load Invitation List";
		this.loadBtn = new JButton(this.invitationDefaultLabel);
		this.loadBtn.setEnabled(true);
		this.loadBtn.addActionListener(this);
		frame.getContentPane().add(loadBtn, "cell 3 11,alignx left,aligny center");
		
		lblMailField = new JLabel("Mail Column:");
		frame.getContentPane().add(lblMailField, "cell 1 12,alignx left,aligny center");
		
		mailField = new JComboBox<String>();
		mailField.setEnabled(false);
		mailField.setEditable(true);
		frame.getContentPane().add(mailField, "cell 3 12,growx");
		
		lblBlacklist = new JLabel("Blacklist:");
		frame.getContentPane().add(lblBlacklist, "cell 1 13,alignx left,aligny center");
		
		blacklistDefaultLabel = "Load Blacklist";
		this.blackBtn = new JButton(this.blacklistDefaultLabel);
		this.blackBtn.setEnabled(true);
		this.blackBtn.addActionListener(this);
		frame.getContentPane().add(blackBtn, "cell 3 13,alignx left,aligny center");

		lblRemind = new JLabel("Remind:");
		frame.getContentPane().add(lblRemind, "cell 1 14,alignx left,aligny center");

		this.saveRemindBtn = new JButton("Save Remind List");
		this.saveRemindBtn.setEnabled(false);
		this.saveRemindBtn.addActionListener(this);
		frame.getContentPane().add(saveRemindBtn, "cell 3 14,alignx left,aligny center");

		lblIgnore = new JLabel("Ignore:");
		frame.getContentPane().add(lblIgnore, "cell 1 15,alignx left,aligny center");

		this.saveIgnoreBtn = new JButton("Save Ignore List");
		this.saveIgnoreBtn.setEnabled(false);
		this.saveIgnoreBtn.addActionListener(this);
		frame.getContentPane().add(saveIgnoreBtn, "cell 3 15,alignx left,aligny center");

		btnProcess = new JButton("Process");
		this.btnProcess.setEnabled(false);
		this.btnProcess.addActionListener(this);
		frame.getContentPane().add(btnProcess, "cell 3 17,growx,aligny center");

		this.consolePane = new JTextArea();
		this.consolePane.setColumns(100);
		this.consolePane.setEditable(false);
		final PrintStream printStream = new PrintStream(new CustomOutputStream(this.consolePane));
		final JScrollPane consoleScroll = new JScrollPane(this.consolePane);
		this.frame.getContentPane().add(consoleScroll, "cell 3 18");

		System.setOut(printStream);
		System.setErr(printStream);

		if (this.additionalArgs.containsKey("dbServer"))
			this.txtDbserver.setText(this.additionalArgs.get("dbServer"));
		if (this.additionalArgs.containsKey("dbPort"))
			this.txtDbport.setText(Integer.parseInt(this.additionalArgs.get("dbPort")) + "");
		if (this.additionalArgs.containsKey("dbName"))
			this.txtDbname.setText(this.additionalArgs.get("dbName"));
		if (this.additionalArgs.containsKey("dbUser"))
			this.txtDbuser.setText(this.additionalArgs.get("dbUser"));
		if (this.additionalArgs.containsKey("dbPass"))
			this.pwdDbpass.setText(this.additionalArgs.get("dbPass"));

		if (this.additionalArgs.containsKey("mailboxServer"))
			this.txtMailserver.setText(this.additionalArgs.get("mailboxServer"));
		if (this.additionalArgs.containsKey("mailboxUser"))
			this.txtMailuser.setText(this.additionalArgs.get("mailboxUser"));
		if (this.additionalArgs.containsKey("mailboxPass"))
			this.pwdMailpass.setText(this.additionalArgs.get("mailboxPass"));
		if (this.additionalArgs.containsKey("mailboxPath"))
			this.txtMailpath.setText(this.additionalArgs.get("mailboxPath"));
//
		this.frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
			}
		});
	}

	/**
	 * Process.
	 */
	private void process() {
		this.task = new Task();
		this.task.execute();
	}

	private void showError(Object e) {
		String msg = "UNKOWN";
		if ((Throwable.class).isAssignableFrom(e.getClass())) {
			msg = ((Throwable) e).getMessage();
			((Throwable) e).printStackTrace();
		} else
			msg = e + "";
		JOptionPane.showMessageDialog(this.frame, msg, "Error", JOptionPane.ERROR_MESSAGE, null);
	}

	public void update(Observable o, Object arg) {
		// TODO Auto-generated method stub

	}

	public void focusGained(FocusEvent e) {
		// TODO Auto-generated method stub

	}

	public void focusLost(FocusEvent e) {
		// TODO Auto-generated method stub

	}

	public void stateChanged(ChangeEvent e) {
		// TODO Auto-generated method stub

	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(this.btnProcess)) {

			this.process();
		}
		if (e.getSource().equals(this.loadBtn)) {
			this.btnProcess.setEnabled(false);
			this.loadBtn.setText(invitationDefaultLabel);
			
			this.mailField.removeAllItems();
			this.mailField.setEnabled(false);
			
			if (this.fc == null) {
				this.fc = new JFileChooser();
			}
			File presetDir = new File(System.getProperty("user.home"));
			if (this.invitationsFile != null){
				presetDir = this.invitationsFile.getParentFile();
			}
			else if(this.blacklistFile != null){
				presetDir = this.blacklistFile.getParentFile();
			}
			
			final File preset = new File(presetDir, "invitations.csv");
			this.fc.setSelectedFile(preset);
			final int returnVal = this.fc.showOpenDialog(this.frame);
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				this.invitationsFile = this.fc.getSelectedFile();
			} else {
				this.invitationsFile = null;
			}
			if (this.invitationsFile != null) {
				this.loadBtn.setText(this.invitationsFile.getName());
				
				try {
					final ArrayList<String> columns = CSVClient.getInstance().getCSVHeaders(this.invitationsFile, ';', '\"');
					for(final String column : columns){
						this.mailField.addItem(column);
					}
					this.mailField.setEnabled(true);
					this.btnProcess.setEnabled(true);
				} catch (IOException e1) {
					Webstart.this.showError("Column retrieval failed : " + e1.getMessage());
				}
			}
		}
		if (e.getSource().equals(this.blackBtn)) {
			this.blackBtn.setText(blacklistDefaultLabel);
			if (this.fc == null) {
				this.fc = new JFileChooser();
			}
			File presetDir = new File(System.getProperty("user.home"));
			if (this.invitationsFile != null){
				presetDir = this.invitationsFile.getParentFile();
			}
			else if(this.blacklistFile != null){
				presetDir = this.blacklistFile.getParentFile();
			}
			
			final File preset = new File(presetDir, "blacklist.csv");
			this.fc.setSelectedFile(preset);
			final int returnVal = this.fc.showOpenDialog(this.frame);
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				this.blacklistFile = this.fc.getSelectedFile();
			} else {
				this.blacklistFile = null;
			}
			if (this.blacklistFile != null) {
				this.blackBtn.setText(this.blacklistFile.getName());
			}
		}
		if (e.getSource().equals(this.saveRemindBtn)) {
			if (this.fc == null) {
				this.fc = new JFileChooser();
			}
			if (this.fc == null) {
				this.fc = new JFileChooser();
			}
			
			File presetDir = new File(System.getProperty("user.home"));
			if (this.invitationsFile != null)
				presetDir = this.invitationsFile.getParentFile();
			final File preset = new File(presetDir, "toRemind.csv");
			this.fc.setSelectedFile(preset);
			
			final int returnVal = this.fc.showSaveDialog(this.frame);
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				final File file = this.fc.getSelectedFile();
				try {
					FileUtils.copyFile(this.remindFile, file);
				} catch (final IOException e1) {
					Webstart.this.showError("Copy file failed : " + e1.getMessage());
					LOGGER.error("Copy file failed ", e1);
				}
			}
		}
		if (e.getSource().equals(this.saveIgnoreBtn)) {
			if (this.fc == null) {
				this.fc = new JFileChooser();
			}
			
			File presetDir = new File(System.getProperty("user.home"));
			if (this.invitationsFile != null)
				presetDir = this.invitationsFile.getParentFile();
			final File preset = new File(presetDir, "toIgnore.csv");
			this.fc.setSelectedFile(preset);

			final int returnVal = this.fc.showSaveDialog(this.frame);
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				final File file = this.fc.getSelectedFile();
				try {
					FileUtils.copyFile(this.ignoreFile, file);
				} catch (final IOException e1) {
					Webstart.this.showError("Copy file failed : " + e1.getMessage());
					LOGGER.error("Copy file failed ", e1);
				}
			}
		}
	}

}

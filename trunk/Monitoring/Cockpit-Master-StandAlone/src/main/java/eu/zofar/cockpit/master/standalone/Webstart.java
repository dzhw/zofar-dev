package eu.zofar.cockpit.master.standalone;

import java.awt.Cursor;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.net.URL;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.Properties;
import java.util.UUID;

import javax.jnlp.BasicService;
import javax.jnlp.ServiceManager;
import javax.jnlp.UnavailableServiceException;
import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.SwingWorker;

import org.hibernate.cfg.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

import eu.zofar.cockpit.service.impl.Client;
import eu.zofar.cockpit.service.transfer.ClientTransfer;
import eu.zofar.cockpit.utils.ConfigurationUtils;

// TODO: Auto-generated Javadoc
/**
 * The Class Webstart.
 */
public class Webstart implements ActionListener, FocusListener, Observer {

	/** The Constant LOGGER. */
	private static final Logger LOGGER = LoggerFactory.getLogger(Webstart.class);

	/** The frame. */
	private JFrame frame;
	
	/** The server. */
	private JTextField server;
	
	/** The port. */
	private JFormattedTextField port;
	
	/** The database. */
	private JTextField database;
	
	/** The user. */
	private JTextField user;
	
	/** The password. */
	private JPasswordField password;
	
	/** The btn proceed. */
	private JButton btnProceed;

	/** The format. */
	private NumberFormat format;

	/** The client. */
	private final Client client;
	
	/** The additional args. */
	private final Map<String, String> additionalArgs;
	
	/** The text pane. */
	private JTextPane textPane;
	
	/** The scroll pane. */
	private JScrollPane scrollPane;
	
	/** The lbl server. */
	private JLabel lblServer;
	
	/** The lbl port. */
	private JLabel lblPort;
	
	/** The lbl database. */
	private JLabel lblDatabase;
	
	/** The progress bar. */
	private JProgressBar progressBar;
	
	/** The phases. */
	private int phases;

	/** The task. */
	private Task task;

	/** The conf. */
	private final Configuration conf;

	/**
	 * The Class Task.
	 */
	private class Task extends SwingWorker<Void, Void> {
		/*
		 * Main task. Executed in background thread.
		 */
		/* (non-Javadoc)
		 * @see javax.swing.SwingWorker#doInBackground()
		 */
		@Override
		public Void doInBackground() {
			for (final Map.Entry<String, String> property : Webstart.this.additionalArgs.entrySet()) {
				LOGGER.info("property : {} = {}", property.getKey(), property.getValue());
			}

			btnProceed.setEnabled(false);
			progressBar.setValue(0);
			progressBar.setString("processing data ...");
			progressBar.setIndeterminate(true);
			textPane.setText("");
			frame.revalidate();
			frame.repaint();
			// final String url = "jdbc:postgresql://" +
			// additionalArgs.get("server") + ":" + additionalArgs.get("port") +
			// "/" + additionalArgs.get("database");
			final String url = "jdbc:postgresql://" + Webstart.this.additionalArgs.get("server") + ":" + Webstart.this.additionalArgs.get("port") + "/" + Webstart.this.additionalArgs.get("database");

			Webstart.this.conf.setProperty("connection.url", url);
			Webstart.this.conf.setProperty("hibernate.connection.url", url);
			Webstart.this.conf.setProperty("connection.username", Webstart.this.additionalArgs.get("user"));
			Webstart.this.conf.setProperty("connection.password", Webstart.this.additionalArgs.get("password"));
			Webstart.this.conf.setProperty("hibernate.connection.username", Webstart.this.additionalArgs.get("user"));
			Webstart.this.conf.setProperty("hibernate.connection.password", Webstart.this.additionalArgs.get("password"));
			Webstart.this.frame.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
			try {
				ClientTransfer status = client.status(Webstart.this.conf,UUID.randomUUID().toString());
				if (status != null) {
					final StringBuffer buffer = new StringBuffer();
					final DateFormat df = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss z");
					buffer.append("Timestamp : " + df.format(status.getTimestamp()) + "\n");
					buffer.append("Finished : " + status.getFinished().intValue() + "\n");
					buffer.append("Participated : " + status.getParticipated().intValue() + "\n");
					buffer.append("Exit Pages ("+df.format(status.getTimestamp()-(1000L*60L*60L))+") : " + "\n");
					final String[][] exits = status.getExitPages();
					for (final String[] exit : exits) {
//						System.out.println("exit : "+exit[0]+","+exit[1]);
						if((exit[0] != null)&&(!exit[0].equals("null")))buffer.append("\t" + exit[0] + " = " + exit[1] + "\n");
					}
					textPane.setText(buffer.toString());
				} else {
					textPane.setText("no status found");
				}

			} catch (Throwable e) {
				textPane.setText("Error : " + e.getMessage());
				frame.repaint();
			}
			Webstart.this.frame.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			return null;
		}

		/*
		 * Executed in event dispatch thread
		 */
		/* (non-Javadoc)
		 * @see javax.swing.SwingWorker#done()
		 */
		@Override
		public void done() {
			btnProceed.setEnabled(true);
			progressBar.setString("done");
		}
	}

	/**
	 * Launch the application.
	 *
	 * @param args the arguments
	 */
	public static void main(String[] args) {

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

		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Webstart window = new Webstart(additionalArgs);
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 *
	 * @param additionalArgs the additional args
	 */
	public Webstart(Map<String, String> additionalArgs) {
		this.client = new Client(this);
		this.additionalArgs = additionalArgs;
		format = NumberFormat.getIntegerInstance();
		format.setParseIntegerOnly(true);
		format.setGroupingUsed(false);
		initialize();

		this.conf = this.client.getConfiguration();

		final Properties system = ConfigurationUtils.getInstance().getConfiguration("System.properties");
		for (final Map.Entry<Object, Object> property : system.entrySet()) {
			if (!this.additionalArgs.containsKey(property.getKey() + "")) {
				this.additionalArgs.put(property.getKey() + "", property.getValue() + "");
			}
		}

		URL url = null;
		try {
			// Lookup the javax.jnlp.BasicService object
			final BasicService bs = (BasicService) ServiceManager.lookup("javax.jnlp.BasicService");
			if (bs != null)
				url = bs.getCodeBase();
		} catch (final UnavailableServiceException ue) {
		}
		LOGGER.info("base url {}", url);

		// if (url != null) {
		// InputStream in = null;
		// try {
		// if (additionalArgs.containsKey("qml")) {
		// in = new URL(url.toString() +
		// additionalArgs.get("qml")).openStream();
		// final File qmlFile = File.createTempFile("qml",
		// System.currentTimeMillis() + "");
		// FileUtils.writeStringToFile(qmlFile, IOUtils.toString(in));
		// this.additionalArgs.put("qml", qmlFile.getAbsolutePath());
		// }
		// if (additionalArgs.containsKey("exportKey")) {
		// in = new URL(url.toString() +
		// additionalArgs.get("exportKey")).openStream();
		// final File certFile = File.createTempFile("export",
		// System.currentTimeMillis() + "");
		// FileUtils.writeStringToFile(certFile, IOUtils.toString(in));
		// this.additionalArgs.put("exportKey", certFile.getAbsolutePath());
		// }
		// } catch (final IOException e) {
		// e.printStackTrace();
		// } finally {
		// if (in != null)
		// IOUtils.closeQuietly(in);
		// }
		// }
		//
		// // Add Defaults
		// if (!this.additionalArgs.containsKey("packetSize")) {
		// this.additionalArgs.put("packetSize", "1000");
		// }

	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame("Cockpit");
		frame.setBounds(100, 100, 482, 398);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(
				new FormLayout(new ColumnSpec[] {
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("max(38dlu;default)"),
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("default:grow"),
				FormFactory.RELATED_GAP_COLSPEC,},
			new RowSpec[] {
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("50dlu:grow"),
				FormFactory.RELATED_GAP_ROWSPEC,}));

		lblServer = new JLabel("Server");
		frame.getContentPane().add(lblServer, "4, 2, right, center");

		server = new JTextField();
		server.addActionListener(this);
		server.addFocusListener(this);
		server.setEditable(false);
		frame.getContentPane().add(server, "6, 2, fill, center");
		server.setColumns(10);

		if (this.additionalArgs.containsKey("server"))
			server.setText(this.additionalArgs.get("server"));

		lblPort = new JLabel("Port");
		frame.getContentPane().add(lblPort, "4, 4, right, center");

		port = new JFormattedTextField(format);
		port.addActionListener(this);
		port.addFocusListener(this);
		port.setEditable(false);
		frame.getContentPane().add(port, "6, 4, fill, center");
		port.setColumns(10);

		if (this.additionalArgs.containsKey("port"))
			port.setValue(Integer.parseInt(this.additionalArgs.get("port")));

		lblDatabase = new JLabel("Database");
		frame.getContentPane().add(lblDatabase, "4, 6, right, center");

		database = new JTextField();
		database.addActionListener(this);
		database.addFocusListener(this);
		database.setEditable(false);
		frame.getContentPane().add(database, "6, 6, fill, center");
		database.setColumns(10);

		if (this.additionalArgs.containsKey("database"))
			database.setText(this.additionalArgs.get("database"));

		JLabel lblUser = new JLabel("User");
		frame.getContentPane().add(lblUser, "4, 10, right, center");

		user = new JTextField();
		user.addActionListener(this);
		user.addFocusListener(this);
		user.setEditable(false);
		frame.getContentPane().add(user, "6, 10, fill, center");
		user.setColumns(10);

		if (this.additionalArgs.containsKey("user"))
			user.setText(this.additionalArgs.get("user"));

		JLabel lblPassword = new JLabel("Password");
		frame.getContentPane().add(lblPassword, "4, 12, right, center");

		password = new JPasswordField();

		password.addActionListener(this);
		password.addFocusListener(this);
		password.setEditable(false);
		frame.getContentPane().add(password, "6, 12, fill, center");
		password.setColumns(10);

		if (this.additionalArgs.containsKey("password"))
			password.setText(this.additionalArgs.get("password"));

		btnProceed = new JButton("Proceed");
		btnProceed.addActionListener(this);
		frame.getContentPane().add(btnProceed, "6, 14");

		progressBar = new JProgressBar();
		progressBar.setStringPainted(true);
		progressBar.setMinimum(0);
		progressBar.setMaximum(this.phases);
		progressBar.setValue(0);
		progressBar.setString("initialisation");
		frame.getContentPane().add(progressBar, "6, 16, fill, center");

		scrollPane = new JScrollPane();
		frame.getContentPane().add(scrollPane, "6, 18, fill, fill");

		textPane = new JTextPane();
		scrollPane.setViewportView(textPane);
		textPane.setEditable(false);
	}

	/**
	 * Process.
	 */
	private void process() {
		task = new Task();
		task.execute();
	}

	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e) {
		// LOGGER.info("action {}", e.getSource());
		if (e.getSource().equals(this.btnProceed)) {
			this.additionalArgs.put("server", this.server.getText());
			this.additionalArgs.put("port", this.port.getText());
			this.additionalArgs.put("database", this.database.getText());
			this.additionalArgs.put("user", this.user.getText());
			this.additionalArgs.put("password", new String(this.password.getPassword()));
			LOGGER.info("==> {}", this.additionalArgs);
			this.process();
		}
	}

	/* (non-Javadoc)
	 * @see java.awt.event.FocusListener#focusGained(java.awt.event.FocusEvent)
	 */
	public void focusGained(FocusEvent e) {
		// LOGGER.info("focus gained {}", e.getSource());

	}

	/* (non-Javadoc)
	 * @see java.awt.event.FocusListener#focusLost(java.awt.event.FocusEvent)
	 */
	public void focusLost(FocusEvent e) {
		// LOGGER.info("focus lost {}", e.getSource());
//		if (e.getSource().equals(server))
//			additionalArgs.put("server", server.getText());
//		if (e.getSource().equals(port))
//			additionalArgs.put("port", port.getText());
//		if (e.getSource().equals(database))
//			additionalArgs.put("database", database.getText());
//		if (e.getSource().equals(user))
//			additionalArgs.put("user", user.getText());
//		if (e.getSource().equals(password))
//			additionalArgs.put("password", new String(this.password.getPassword()));
	}

	/* (non-Javadoc)
	 * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
	 */
	public void update(Observable o, Object arg) {
		LOGGER.info("observer {} ({})", o.getClass(), arg);
		if ((Client.class).isAssignableFrom(o.getClass())) {
			if (((String) arg).startsWith("phases=")) {
				String n = ((String) arg).replaceAll("phases=", "");
				phases = Integer.parseInt(n);
			} else {
				progressBar.setIndeterminate(false);
				progressBar.setValue(progressBar.getValue() + 1);
				progressBar.setString(arg + "");
			}
		}
	}

}

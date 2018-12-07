package eu.dzhw.zofar.management.security.certificates.main;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.cert.Certificate;
import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import javax.jnlp.BasicService;
import javax.jnlp.ServiceManager;
import javax.jnlp.UnavailableServiceException;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.SwingWorker;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileFilter;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

import eu.dzhw.zofar.management.security.certificates.CertificateClient;
import eu.dzhw.zofar.management.security.certificates.exceptions.CustomCertificateException;
import eu.dzhw.zofar.management.security.certificates.exceptions.CustomKeyException;
import eu.dzhw.zofar.management.utils.files.FileClient;

public class Webstart implements ActionListener, ChangeListener, FocusListener, Observer {

	/** The Constant LOGGER. */
	private static final Logger LOGGER = LoggerFactory.getLogger(Webstart.class);

	private JFrame frame;
	/** The additional args. */
	private final Map<String, String> additionalArgs;
	/** The task. */
	private Task task;
	private final JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
	private final JPanel generationPanel = new JPanel();
	private final JPanel encryptionPanel = new JPanel();
	private final JPanel decryptionPanel = new JPanel();
	private final JButton btnDecrypt = new JButton("Decrypt");
	private final JButton btnEncrypt = new JButton("Encrypt");
	private final JButton btnGenerate = new JButton("Generate");
	private JComboBox certTypeBx = null;
	private final JLabel lblType = new JLabel("Type");
	private final JLabel lblSubjectdn = new JLabel("SubjectDN");
	private final JLabel lblValidTill = new JLabel("Valid Duration");
	private final JTextField subjectDNField = new JTextField();
	private JSlider slider = null;
	private final JLabel sliderDayLbs = new JLabel("1 Tage");
	private final JLabel lblCaCert = new JLabel("CA Cert");
	private final JTextField caCertField = new JTextField();
	private final JButton btnSaveCert = new JButton("Speichern Zertifikat");
	private final JButton btnSavePrivateKey = new JButton("Speichern privater Schlüssel");

	private Certificate parentCert;
	private PrivateKey parentPrivate;

	private Certificate userCert;
	private PrivateKey userPrivate;

	private final JLabel lblFileToEncrypt = new JLabel("File to encrypt");
	private final JLabel lblCertificate = new JLabel("Certificate");
	private final JButton encryptFileBtn = new JButton("Load ...");
	private final JTextField encryptFileField = new JTextField();
	private final JButton encryptCertBtn = new JButton("Load ...");
	private final JTextField encryptCertField = new JTextField();
	private final JButton btnSaveEncrypted = new JButton("Save encrypted ...");

	private Certificate encryptCert;
	private File encryptFile;
	private File encryptFileEncrypted;

	private PrivateKey decryptKey;
	private Certificate decryptCert;

	private File decryptFile;
	private File decryptFileDecrypted;

	private final JLabel lblFileToDecrypt = new JLabel("File to decrypt");
	private final JLabel lblPrivatekey = new JLabel("PrivateKey");
	private final JButton decryptFileBtn = new JButton("Load ...");
	private final JButton decryptKeyBtn = new JButton("Load ...");
	private final JTextField decryptFileField = new JTextField();
	private final JTextField decryptKeyField = new JTextField();
	private final JButton btnSaveDecrypted = new JButton("Save decrypted ...");
	private final JLabel lblCertificate_1 = new JLabel("Certificate");
	private final JButton decryptCertBtn = new JButton("Load ...");
	private final JTextField decryptCertField = new JTextField();

	private class Task extends SwingWorker<Void, Void> {

		private String command = "UNKOWN";

		public Task(String command) {
			super();
			this.command = command;
		}

		@Override
		protected Void doInBackground() throws Exception {
			for (final Map.Entry<String, String> property : Webstart.this.additionalArgs.entrySet()) {
				LOGGER.info("property : {} = {}", property.getKey(), property.getValue());
			}
			try {
				if ((this.command != null) && (this.command.equals("generate"))) {
					Webstart.this.btnGenerate.setEnabled(false);
					Webstart.this.btnSaveCert.setEnabled(false);
					Webstart.this.btnSavePrivateKey.setEnabled(false);

					Webstart.this.userCert = null;
					Webstart.this.userPrivate = null;

					// generate Certificate
					final CertificateClient certClient = CertificateClient.getInstance();
					final KeyPair keyPair = certClient.createRSAKeyPair();
					
					if(Webstart.this.parentPrivate == null){
						Webstart.this.showError(new CustomKeyException("No parent Private Key found"));
					}

					long validFor = (long) slider.getValue() * 1000 * 60 * 60 * 24;
					Webstart.this.userCert = certClient.createEndUserCertificate(Webstart.this.subjectDNField.getText(), keyPair.getPublic(), Webstart.this.parentPrivate, Webstart.this.parentCert, validFor);
					Webstart.this.userPrivate = keyPair.getPrivate();

					if (Webstart.this.userCert != null)
						Webstart.this.btnSaveCert.setEnabled(true);
					if (Webstart.this.userPrivate != null)
						Webstart.this.btnSavePrivateKey.setEnabled(true);
					Webstart.this.btnGenerate.setEnabled(true);
				} else if ((this.command != null) && (this.command.equals("saveCert"))) {
					// save generated Certificate to File
					final JFileChooser fc = new JFileChooser();
					fc.setSelectedFile(new File("certificate.pem"));
					fc.setFileFilter(new FileFilter() {
						public boolean accept(File f) {
							if (f.isDirectory())
								return true;
							return f.getName().toLowerCase().endsWith(".pem");
						}

						public String getDescription() {
							return "Certificate-File (*.pem)";
						}
					});

					int returnVal = fc.showSaveDialog(Webstart.this.frame);

					if (returnVal == JFileChooser.APPROVE_OPTION) {
						final CertificateClient certClient = CertificateClient.getInstance();
						final File file = fc.getSelectedFile();
						try {
							certClient.saveCertificate(Webstart.this.userCert, file);
						} catch (final Exception e) {
							Webstart.this.showError(e);
						}

					}
				} else if ((this.command != null) && (this.command.equals("savePrivateKey"))) {
					// save generated PrivateKey to File
					final JFileChooser fc = new JFileChooser();
					fc.setSelectedFile(new File("privkey.pem"));
					fc.setFileFilter(new FileFilter() {
						public boolean accept(File f) {
							if (f.isDirectory())
								return true;
							return f.getName().toLowerCase().endsWith(".pem");
						}

						public String getDescription() {
							return "PrivateKey-File (*.pem)";
						}
					});

					int returnVal = fc.showSaveDialog(Webstart.this.frame);

					if (returnVal == JFileChooser.APPROVE_OPTION) {
						final CertificateClient certClient = CertificateClient.getInstance();
						final File file = fc.getSelectedFile();
						try {
							certClient.savePrivateKey(Webstart.this.userPrivate, file);
						} catch (final Exception e) {
							Webstart.this.showError(e);
						}

					}
				} else if ((this.command != null) && (this.command.equals("loadEncryptFile"))) {
					// load File to encrypt
					Webstart.this.encryptFileField.setText("");
					final JFileChooser fc = new JFileChooser();
					int returnVal = fc.showOpenDialog(Webstart.this.frame);

					if (returnVal == JFileChooser.APPROVE_OPTION) {
						final File file = fc.getSelectedFile();
						Webstart.this.encryptFile = file;
						Webstart.this.encryptFileField.setText(Webstart.this.encryptFile.getAbsolutePath());
					}
				} else if ((this.command != null) && (this.command.equals("loadEncryptCert"))) {
					// load Certificate to encrypt
					Webstart.this.encryptCertField.setText("");
					final JFileChooser fc = new JFileChooser();
					fc.setSelectedFile(new File("certificate.pem"));
					fc.setFileFilter(new FileFilter() {
						public boolean accept(File f) {
							if (f.isDirectory())
								return true;
							return f.getName().toLowerCase().endsWith(".pem");
						}

						public String getDescription() {
							return "Certificate-File (*.pem)";
						}
					});

					int returnVal = fc.showOpenDialog(Webstart.this.frame);

					if (returnVal == JFileChooser.APPROVE_OPTION) {
						final CertificateClient certClient = CertificateClient.getInstance();
						final File file = fc.getSelectedFile();

						try {
							Webstart.this.encryptCert = certClient.loadCertificate(file);
							Webstart.this.encryptCertField.setText(certClient.getInfo(Webstart.this.encryptCert));
						} catch (final Exception e) {
							Webstart.this.showError(e);
						}
					}
				} else if ((this.command != null) && (this.command.equals("encrypt"))) {
					// encrypt File
					Webstart.this.btnEncrypt.setEnabled(false);
					Webstart.this.btnSaveEncrypted.setEnabled(false);
					Webstart.this.encryptFileEncrypted = null;
					final CertificateClient certClient = CertificateClient.getInstance();
					try {
						Webstart.this.encryptFileEncrypted = certClient.encryptFile(Webstart.this.encryptFile, FileClient.getInstance().createTempFile(Webstart.this.encryptFile.getName(), "toEncrypt"), Webstart.this.encryptCert);
					} catch (final Exception e) {
						Webstart.this.showError(e);
					}
				} else if ((this.command != null) && (this.command.equals("saveEncrypted"))) {
					// save encrypted File
					final JFileChooser fc = new JFileChooser();
					fc.setSelectedFile(new File(Webstart.this.encryptFile.getParentFile(), Webstart.this.encryptFile.getName() + ".encrypted"));
					int returnVal = fc.showSaveDialog(Webstart.this.frame);

					if (returnVal == JFileChooser.APPROVE_OPTION) {
						final File file = fc.getSelectedFile();
						final FileClient fileClient = FileClient.getInstance();

						try {
							fileClient.copyFile(Webstart.this.encryptFileEncrypted, file);
						} catch (final Exception e) {
							Webstart.this.showError(e);
						}
					} else {
						// log.append("Open command cancelled by user." +
						// newline);
					}

				} else if ((this.command != null) && (this.command.equals("loadDecryptFile"))) {
					// load File to encrypt
					Webstart.this.decryptFileField.setText("");
					final JFileChooser fc = new JFileChooser();
					int returnVal = fc.showOpenDialog(Webstart.this.frame);

					if (returnVal == JFileChooser.APPROVE_OPTION) {
						final File file = fc.getSelectedFile();
						Webstart.this.decryptFile = file;
						Webstart.this.decryptFileField.setText(Webstart.this.decryptFile.getAbsolutePath());
					}
				} else if ((this.command != null) && (this.command.equals("loadDecryptKey"))) {
					// load Certificate to decrypt
					Webstart.this.decryptKeyField.setText("");
					final JFileChooser fc = new JFileChooser();
					fc.setSelectedFile(new File("privatekey.pem"));
					fc.setFileFilter(new FileFilter() {
						public boolean accept(File f) {
							if (f.isDirectory())
								return true;
							return f.getName().toLowerCase().endsWith(".pem");
						}

						public String getDescription() {
							return "PrivateKey-File (*.pem)";
						}
					});

					int returnVal = fc.showOpenDialog(Webstart.this.frame);

					if (returnVal == JFileChooser.APPROVE_OPTION) {
						final CertificateClient certClient = CertificateClient.getInstance();
						final File file = fc.getSelectedFile();

						try {
							Webstart.this.decryptKey = certClient.loadPrivatePEMKey(file);
							Webstart.this.decryptKeyField.setText(certClient.getInfo(Webstart.this.decryptKey));
						} catch (final Exception e) {
							Webstart.this.showError(e);
						}

					}
				} else if ((this.command != null) && (this.command.equals("loadDecryptCert"))) {
					// load Certificate to encrypt
					Webstart.this.decryptCertField.setText("");
					final JFileChooser fc = new JFileChooser();
					fc.setSelectedFile(new File("certificate.pem"));
					fc.setFileFilter(new FileFilter() {
						public boolean accept(File f) {
							if (f.isDirectory())
								return true;
							return f.getName().toLowerCase().endsWith(".pem");
						}

						public String getDescription() {
							return "Certificate-File (*.pem)";
						}
					});

					int returnVal = fc.showOpenDialog(Webstart.this.frame);

					if (returnVal == JFileChooser.APPROVE_OPTION) {
						final CertificateClient certClient = CertificateClient.getInstance();
						final File file = fc.getSelectedFile();
						try {
							Webstart.this.decryptCert = certClient.loadCertificate(file);
							Webstart.this.decryptCertField.setText(certClient.getInfo(Webstart.this.decryptCert));
						} catch (final Exception e) {
							Webstart.this.showError(e);
						}
					}
				} else if ((this.command != null) && (this.command.equals("decrypt"))) {
					// decrypt File
					Webstart.this.btnDecrypt.setEnabled(false);
					Webstart.this.btnSaveDecrypted.setEnabled(false);
					Webstart.this.decryptFileDecrypted = null;
					final CertificateClient certClient = CertificateClient.getInstance();
					try {
						Webstart.this.decryptFileDecrypted = certClient.decryptFile(Webstart.this.decryptFile, FileClient.getInstance().createTempFile(Webstart.this.decryptFile.getName(), "toDecrypt"), Webstart.this.decryptKey, Webstart.this.decryptCert);
					} catch (final Exception e) {
						Webstart.this.showError(e);
					}
				} else if ((this.command != null) && (this.command.equals("saveDecrypted"))) {
					// save decrypted File
					final JFileChooser fc = new JFileChooser();
					fc.setSelectedFile(new File(Webstart.this.decryptFile.getParentFile(), Webstart.this.decryptFile.getName() + ".decrypted"));
					int returnVal = fc.showSaveDialog(Webstart.this.frame);

					if (returnVal == JFileChooser.APPROVE_OPTION) {
						final File file = fc.getSelectedFile();
						final FileClient fileClient = FileClient.getInstance();

						try {
							fileClient.copyFile(Webstart.this.decryptFileDecrypted, file);
						} catch (final Exception e) {
							Webstart.this.showError(e);
						}
					}
				} else {
					LOGGER.error("UNKOWN COMMAND: {}", this.command);
				}

			} catch (Throwable e) {
				Webstart.this.showError(e);
			}
			return null;
		}

		@Override
		public void done() {
			try {
				if ((this.command != null) && (this.command.equals("generate"))) {
					Webstart.this.btnGenerate.setEnabled(true);
				} else if ((this.command != null) && (this.command.equals("saveCert"))) {
				} else if ((this.command != null) && (this.command.equals("savePrivateKey"))) {
				} else if ((this.command != null) && (this.command.equals("loadEncryptFile"))) {
					// load File to encrypt
					if ((Webstart.this.encryptFile != null) && (Webstart.this.encryptCert != null))
						Webstart.this.btnEncrypt.setEnabled(true);
				} else if ((this.command != null) && (this.command.equals("loadEncryptCert"))) {
					// load Certificate to encrypt
					if ((Webstart.this.encryptFile != null) && (Webstart.this.encryptCert != null))
						Webstart.this.btnEncrypt.setEnabled(true);
				} else if ((this.command != null) && (this.command.equals("encrypt"))) {
					Webstart.this.btnEncrypt.setEnabled(true);
					if (Webstart.this.encryptFileEncrypted != null)
						Webstart.this.btnSaveEncrypted.setEnabled(true);
				} else if ((this.command != null) && (this.command.equals("saveEncrypted"))) {
				} else if ((this.command != null) && (this.command.equals("loadDecryptFile"))) {
					if ((Webstart.this.decryptFile != null) && (Webstart.this.decryptKey != null) && (Webstart.this.decryptCert != null))
						Webstart.this.btnDecrypt.setEnabled(true);
				} else if ((this.command != null) && (this.command.equals("loadDecryptKey"))) {
					if ((Webstart.this.decryptFile != null) && (Webstart.this.decryptKey != null) && (Webstart.this.decryptCert != null))
						Webstart.this.btnDecrypt.setEnabled(true);
				} else if ((this.command != null) && (this.command.equals("loadDecryptCert"))) {
					if ((Webstart.this.decryptFile != null) && (Webstart.this.decryptKey != null) && (Webstart.this.decryptCert != null))
						Webstart.this.btnDecrypt.setEnabled(true);
				} else if ((this.command != null) && (this.command.equals("decrypt"))) {
					Webstart.this.btnDecrypt.setEnabled(true);
					if (Webstart.this.decryptFileDecrypted != null)
						Webstart.this.btnSaveDecrypted.setEnabled(true);
				} else if ((this.command != null) && (this.command.equals("saveDecrypted"))) {
				} else {
					LOGGER.error("UNKOWN COMMAND: {}", this.command);
				}

			} catch (Throwable e) {
				// firePropertyChange("done-exception", null, e);
				// throw new Exception(e);
				JOptionPane.showMessageDialog(Webstart.this.frame, e, "Error", JOptionPane.ERROR_MESSAGE, null);
			}
		}
	}

	/**
	 * Launch the application.
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
	 */
	public Webstart(final Map<String, String> additionalArgs) {
		this.additionalArgs = additionalArgs;

		URL url = null;
		try {
			// Lookup the javax.jnlp.BasicService object
			final BasicService bs = (BasicService) ServiceManager.lookup("javax.jnlp.BasicService");
			if (bs != null)
				url = bs.getCodeBase();
		} catch (final UnavailableServiceException ue) {
			LOGGER.error("BasicService retrieval failed : {}", ue.getCause());
		}

		LOGGER.info("base url {}", url);

		if (url != null) {
			InputStream in = null;
			try {
				if (additionalArgs.containsKey("parentCert")) {
					in = new URL(url.toString() + additionalArgs.get("parentCert")).openStream();
					final File certFile = File.createTempFile("parentCert", System.currentTimeMillis() + "");
					FileUtils.writeStringToFile(certFile, IOUtils.toString(in));
					this.additionalArgs.put("parentCertFile", certFile.getAbsolutePath());
				}
				if (additionalArgs.containsKey("parentPrivate")) {
					in = new URL(url.toString() + additionalArgs.get("parentPrivate")).openStream();
					final File keyFile = File.createTempFile("parentPrivate", System.currentTimeMillis() + "");
					FileUtils.writeStringToFile(keyFile, IOUtils.toString(in));
					this.additionalArgs.put("parentPrivateFile", keyFile.getAbsolutePath());
				}
			} catch (final IOException e) {
				LOGGER.error("Base URL retrieval failed ", e);
			} finally {
				if (in != null)
					IOUtils.closeQuietly(in);
			}
		}

		final CertificateClient certClient = CertificateClient.getInstance();
		if (this.additionalArgs.containsKey("parentCertFile")) {
			try {
				parentCert = certClient.loadCertificate(new File(this.additionalArgs.get("parentCertFile") + ""));
				if (this.additionalArgs.containsKey("parentPrivateFile")) {
					parentPrivate = certClient.loadPrivatePEMKey(new File(this.additionalArgs.get("parentPrivateFile") + ""));
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

		} else {
			LOGGER.warn("Unable to load parent Certificate. Building a Fallback");
			try {
				long validFor = (long) 1000 * 60 * 60 * 24 * 1;
				final KeyPair keyPair = certClient.createRSAKeyPair();
				parentPrivate = keyPair.getPrivate();
				parentCert = certClient.createRootCertificate("CN=Christian Meisner, OU=Infrastruktur und Methoden, O=Deutsches Zentrum f. Hochschul- und Wissenschaftsforschung GmbH, C=DE", keyPair, validFor);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		decryptCertField.setEditable(false);
		decryptCertField.setColumns(10);
		decryptFileField.setEditable(false);
		decryptFileField.setColumns(10);
		encryptCertField.setEditable(false);
		encryptCertField.setColumns(10);
		encryptFileField.setEditable(false);
		encryptFileField.setColumns(10);
		subjectDNField.setColumns(10);
		frame = new JFrame();
		this.frame.setTitle("Zofar Security Tools");
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(new FormLayout(new ColumnSpec[] { ColumnSpec.decode("440px:grow"), }, new RowSpec[] { RowSpec.decode("269px:grow"), }));

		frame.getContentPane().add(tabbedPane, "1, 1, fill, fill");

		tabbedPane.addTab("Generation", null, generationPanel, null);
		generationPanel.setLayout(new FormLayout(new ColumnSpec[] { FormFactory.RELATED_GAP_COLSPEC, FormFactory.DEFAULT_COLSPEC, FormFactory.RELATED_GAP_COLSPEC, FormFactory.DEFAULT_COLSPEC, FormFactory.RELATED_GAP_COLSPEC, ColumnSpec.decode("default:grow"), }, new RowSpec[] { FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, }));
		btnGenerate.addActionListener(this);

		generationPanel.add(lblType, "4, 4, right, center");

		String[] types = { "Root", "Intermediated", "End User" };
		certTypeBx = new JComboBox(types);
		certTypeBx.setSelectedItem("End User");
		certTypeBx.setEditable(false);
		certTypeBx.setEnabled(false);
		generationPanel.add(certTypeBx, "6, 4, left, center");

		generationPanel.add(lblCaCert, "4, 6, right, center");

		// caCertField.setEnabled(false);
		caCertField.setEditable(false);
		caCertField.setColumns(10);
		caCertField.setText(CertificateClient.getInstance().getInfo(this.parentCert));

		generationPanel.add(caCertField, "6, 6, fill, center");

		generationPanel.add(lblSubjectdn, "4, 8, right, center");

		generationPanel.add(subjectDNField, "6, 8, fill, center");

		generationPanel.add(lblValidTill, "4, 10, right, center");

		slider = new JSlider(JSlider.HORIZONTAL, 1, 365, 1);
		slider.addChangeListener(this);

		generationPanel.add(slider, "6, 10");

		generationPanel.add(sliderDayLbs, "6, 12");

		btnGenerate.setActionCommand("generate");
		generationPanel.add(btnGenerate, "6, 14, left, default");
		btnSaveCert.setEnabled(false);
		btnSaveCert.addActionListener(this);
		btnSaveCert.setActionCommand("saveCert");

		generationPanel.add(btnSaveCert, "6, 16, left, default");
		btnSavePrivateKey.setEnabled(false);
		btnSavePrivateKey.addActionListener(this);
		btnSavePrivateKey.setActionCommand("savePrivateKey");

		generationPanel.add(btnSavePrivateKey, "6, 18, left, default");

		tabbedPane.addTab("Encryption", null, encryptionPanel, null);
		encryptionPanel.setLayout(new FormLayout(new ColumnSpec[] { FormFactory.RELATED_GAP_COLSPEC, FormFactory.DEFAULT_COLSPEC, FormFactory.RELATED_GAP_COLSPEC, FormFactory.DEFAULT_COLSPEC, FormFactory.RELATED_GAP_COLSPEC, FormFactory.DEFAULT_COLSPEC, FormFactory.RELATED_GAP_COLSPEC, ColumnSpec.decode("default:grow"), }, new RowSpec[] { FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, }));

		encryptionPanel.add(lblFileToEncrypt, "4, 4, right, center");

		encryptFileBtn.addActionListener(this);
		encryptFileBtn.setActionCommand("loadEncryptFile");
		encryptionPanel.add(encryptFileBtn, "6, 4, fill, center");
		encryptionPanel.add(encryptFileField, "8, 4, fill, center");

		encryptionPanel.add(lblCertificate, "4, 6, right, center");

		encryptCertBtn.addActionListener(this);
		encryptCertBtn.setActionCommand("loadEncryptCert");
		encryptionPanel.add(encryptCertBtn, "6, 6, fill, center");
		encryptionPanel.add(encryptCertField, "8, 6, fill, center");

		btnEncrypt.setEnabled(false);
		btnEncrypt.addActionListener(this);
		btnEncrypt.setActionCommand("encrypt");
		encryptionPanel.add(btnEncrypt, "6, 10, fill, default");

		btnSaveEncrypted.setEnabled(false);
		btnSaveEncrypted.addActionListener(this);
		btnSaveEncrypted.setActionCommand("saveEncrypted");
		encryptionPanel.add(btnSaveEncrypted, "6, 12, fill, center");

		tabbedPane.addTab("Decryption", null, decryptionPanel, null);
		decryptionPanel.setLayout(new FormLayout(new ColumnSpec[] { FormFactory.RELATED_GAP_COLSPEC, FormFactory.DEFAULT_COLSPEC, FormFactory.RELATED_GAP_COLSPEC, FormFactory.DEFAULT_COLSPEC, FormFactory.RELATED_GAP_COLSPEC, FormFactory.DEFAULT_COLSPEC, FormFactory.RELATED_GAP_COLSPEC, ColumnSpec.decode("default:grow"), }, new RowSpec[] { FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, }));

		decryptionPanel.add(lblFileToDecrypt, "4, 4, right, center");

		decryptFileBtn.addActionListener(this);
		decryptFileBtn.setActionCommand("loadDecryptFile");
		decryptionPanel.add(decryptFileBtn, "6, 4, fill, center");

		decryptionPanel.add(decryptFileField, "8, 4, fill, center");

		btnDecrypt.setEnabled(false);
		btnDecrypt.addActionListener(this);

		decryptionPanel.add(lblCertificate_1, "4, 6, right, center");

		decryptCertBtn.addActionListener(this);
		decryptCertBtn.setActionCommand("loadDecryptCert");
		decryptionPanel.add(decryptCertBtn, "6, 6, fill, center");

		decryptionPanel.add(decryptCertField, "8, 6, fill, center");

		decryptionPanel.add(lblPrivatekey, "4, 8, right, center");

		decryptKeyBtn.addActionListener(this);
		decryptKeyBtn.setActionCommand("loadDecryptKey");
		decryptionPanel.add(decryptKeyBtn, "6, 8, fill, center");
		decryptKeyField.setEditable(false);
		decryptKeyField.setColumns(10);

		decryptionPanel.add(decryptKeyField, "8, 8, fill, center");
		btnDecrypt.setActionCommand("decrypt");
		decryptionPanel.add(btnDecrypt, "6, 12, fill, center");

		btnSaveDecrypted.addActionListener(this);
		btnSaveDecrypted.setActionCommand("saveDecrypted");
		btnSaveDecrypted.setEnabled(false);

		decryptionPanel.add(btnSaveDecrypted, "6, 14, fill, center");
	}

	private void showError(Throwable e) {
		if ((CustomCertificateException.class).isAssignableFrom(e.getClass())) {
			String msg = e.getMessage();
			JOptionPane.showMessageDialog(this.frame, msg, "Certificate Error", JOptionPane.ERROR_MESSAGE, null);
		} else if ((CustomKeyException.class).isAssignableFrom(e.getClass())) {
			String msg = e.getMessage();
			JOptionPane.showMessageDialog(this.frame, msg, "Key Error", JOptionPane.ERROR_MESSAGE, null);
		} else {
			String msg = e.getMessage();
			JOptionPane.showMessageDialog(this.frame, msg, "Unkown Error", JOptionPane.ERROR_MESSAGE, null);
		}

	}

	/**
	 * Process.
	 */
	private void process(final String command) {
		this.task = new Task(command);
		this.task.execute();
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
		final Object source = e.getSource();
		if (source != null) {
			if (source.equals(slider)) {
				String tmp = "";
				if (slider.getValue() == 1)
					tmp = slider.getValue() + " Tag";
				else
					tmp = slider.getValue() + " Tage";
				sliderDayLbs.setText(tmp);
				frame.repaint();
			}
		}
	}

	public void actionPerformed(ActionEvent e) {
		final String cmd = e.getActionCommand();
		// final Object source = e.getSource();

		this.process(cmd);
	}

}

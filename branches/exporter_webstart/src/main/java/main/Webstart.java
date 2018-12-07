package main;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Pattern;

import javax.jnlp.BasicService;
import javax.jnlp.ServiceManager;
import javax.jnlp.UnavailableServiceException;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingWorker;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.hibernate.cfg.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.NodeList;

import com.google.common.io.Files;
import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

import de.his.export.xml.export.ParticipantType;
import eu.dzhw.zofar.management.comm.svn.SVNClient;
import eu.dzhw.zofar.management.security.certificates.CertificateClient;
import eu.dzhw.zofar.management.utils.files.DirectoryClient;
import eu.dzhw.zofar.management.utils.files.FileClient;
import eu.dzhw.zofar.management.utils.files.PackagerClient;
import eu.dzhw.zofar.management.utils.system.ConfigurationUtils;
import eu.dzhw.zofar.management.utils.xml.XmlClient;
import model.HeaderEntry;
import model.TransitionEntry;
import model.ValueEntry;
import presentation.statistics.format.stata.StataFormat.FORMATTYPE;
import service.counting.CountingService;
import service.data.DataService;
import service.statistics.StatisticService;
import service.statistics.StatisticService.TYPE;

/**
 * The Class Webstart.
 */
public class Webstart implements ActionListener, ChangeListener, FocusListener, Observer {

	/** The Constant LOGGER. */
	private static final Logger LOGGER = LoggerFactory.getLogger(Webstart.class);

	/** The frame. */
	private JFrame frame;

	/** The server. */
	private JTextField server;

	/** The port. */
	private JTextField port;

	/** The database. */
	private JTextField database;

	/** The user. */
	private JTextField user;

	/** The password. */
	private JPasswordField password;

	/** The btn proceed. */
	private JButton btnProceed;

	/** The save btn. */
	private JButton saveBtn;

	/** The fc. */
	private JFileChooser fc;

	/** The format. */
//	private NumberFormat format;

	/** The additional args. */
	private final Map<String, String> additionalArgs;

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

	/** The service. */
	private final StatisticService service;

	/** The conf. */
	private final Configuration conf;

	/** The output file. */
	private File outputFile;

	/** The console pane. */
	private JTextArea consolePane;

	/** The chckbx data. */
	private JCheckBox chckbxData;

	/** The chckbx static. */
	private JCheckBox chckbxStatic;

	/** The panel. */
	private JPanel panel;

	/** The lbl packet size. */
	private JLabel lblPacketSize;

	/** The packet size field. */
	private JTextField packetSizeField;

	/** The panel_1. */
	private JPanel panel_1;

	/** The panel_2. */
	private JPanel panel_2;

	/** The lbl missings. */
	private JLabel lblMissings;

	/** The lbl output. */
	private JLabel lblOutput;

	/** The lbl wahr. */
	private JLabel lblWahr;

	/** The lbl falsch. */
	private JLabel lblFalsch;

	/** The mapping true. */
	private JTextField mappingTrue;

	/** The mapping false. */
	private JTextField mappingFalse;

	/** The lbl nicht beantwortet. */
	private JLabel lblNichtBeantwortet;

	/** The lbl nicht gesehen. */
	private JLabel lblNichtGesehen;

	/** The lbl nicht gesehen. */
	private JLabel lblNichtBesucht;

	/** The missing not answered. */
	private JTextField missingNotAnswered;

	private JLabel lblINITIALGET;
	private JTextField missingInitGet;

	/** The missing not answered. */
	private JTextField missingNotVisited;

	/** The missing not seen. */
	private JTextField missingNotSeen;
	private JCheckBox chckbxHistory;
	private JCheckBox chckbxNoEmpty;
	private JCheckBox chckbxPretestComments;
	private JTextField defaultField;
	private JLabel lblDefault;
	private JCheckBox chckbxVerschluesselt;

	private JCheckBox chckbxParadata;
	private JCheckBox chckbxJson;
	
	private File clientTempDir;

	private NumberFormat format;

	/**
	 * The Class Task.
	 */
	private class Task extends SwingWorker<Void, Void> {
		/*
		 * Main task. Executed in background thread.
		 */
		/*
		 * (non-Javadoc)
		 * 
		 * @see javax.swing.SwingWorker#doInBackground()
		 */
		@Override
		public Void doInBackground() throws Exception {
			for (final Map.Entry<String, String> property : Webstart.this.additionalArgs.entrySet()) {
				LOGGER.info("property : {} = {}", property.getKey(), property.getValue());
			}
			try {
				Webstart.this.outputFile = null;
				Webstart.this.btnProceed.setEnabled(false);
				Webstart.this.saveBtn.setEnabled(false);
				Webstart.this.progressBar.setValue(0);
				Webstart.this.progressBar.setString("composing data ...");
				Webstart.this.progressBar.setIndeterminate(true);
				Webstart.this.frame.revalidate();
				Webstart.this.frame.repaint();
				final long startTime = System.currentTimeMillis();
				final String url = "jdbc:postgresql://" + Webstart.this.additionalArgs.get("server") + ":" + Webstart.this.additionalArgs.get("port") + "/" + Webstart.this.additionalArgs.get("database");

				Webstart.this.conf.setProperty("connection.url", url);
				Webstart.this.conf.setProperty("hibernate.connection.url", url);
				Webstart.this.conf.setProperty("connection.username", Webstart.this.additionalArgs.get("user"));
				Webstart.this.conf.setProperty("connection.password", Webstart.this.additionalArgs.get("password"));
				Webstart.this.conf.setProperty("hibernate.connection.username", Webstart.this.additionalArgs.get("user"));
				Webstart.this.conf.setProperty("hibernate.connection.password", Webstart.this.additionalArgs.get("password"));

				boolean limitStrings = true;
				final String confLimitStrings = Webstart.this.additionalArgs.get("limitStrings");
				if (confLimitStrings != null) {
					try {
						limitStrings = Boolean.parseBoolean(confLimitStrings);
					} catch (Exception e) {
					}
				}
				
				boolean limitLabels = false;
				final String confLimitLabels = Webstart.this.additionalArgs.get("limitLabels");
				if (confLimitLabels != null) {
					try {
						limitLabels = Boolean.parseBoolean(confLimitLabels);
					} catch (Exception e) {
					}
				}
				
				String dataAcquestionProject="DATAACQUESTIONPROJECT";
				if(Webstart.this.additionalArgs.containsKey("DATAACQUESTIONPROJECT"))dataAcquestionProject = Webstart.this.additionalArgs.get("DATAACQUESTIONPROJECT");
				
				String surveyId="SURVEYID";
				if(Webstart.this.additionalArgs.containsKey("SURVEYID"))dataAcquestionProject = Webstart.this.additionalArgs.get("SURVEYID");
	
				String instrumentId="INSTRUMENTID";
				if(Webstart.this.additionalArgs.containsKey("INSTRUMENTID"))dataAcquestionProject = Webstart.this.additionalArgs.get("INSTRUMENTID");

				String datasetId="DATASETID";
				if(Webstart.this.additionalArgs.containsKey("DATASETID"))dataAcquestionProject = Webstart.this.additionalArgs.get("DATASETID");

				Map<File, Set<String>> fileMapping = null;

				final Map<String, String> mapping = new HashMap<String, String>();
				mapping.put("true", Webstart.this.mappingTrue.getText());
				mapping.put("false", Webstart.this.mappingFalse.getText());

				mapping.put("DEFAULT", Webstart.this.defaultField.getText());
				mapping.put("NOT ANSWERED", Webstart.this.missingNotAnswered.getText());
				mapping.put("UNSELECTED", Webstart.this.missingNotSeen.getText());
				mapping.put("EMPTY", Webstart.this.missingNotSeen.getText());
				mapping.put("UNSET", Webstart.this.missingNotSeen.getText());
				mapping.put("NOT CALCULATED", Webstart.this.missingNotSeen.getText());
				mapping.put("NOTVISITED", Webstart.this.missingNotVisited.getText());
				mapping.put("INITFORGET1", Webstart.this.missingInitGet.getText());
				mapping.put("INITFORGET2", Webstart.this.missingInitGet.getText());

//				final File qmlFile = new File(Webstart.this.additionalArgs.get("qml"));
//				LOGGER.info("QML File {} ({})", qmlFile, qmlFile.canRead());
				final File qmlFile;
				/* MEINS SVN OK*/
				try {
					// load schema files
					SVNClient svn = SVNClient.getInstance();
					qmlFile=
					 svn.getFile(Webstart.this.additionalArgs.get("svnServer"),
					 Webstart.this.additionalArgs.get("svnUser"),
					 Webstart.this.additionalArgs.get("svnPass"),
					 Webstart.this.additionalArgs.get("svnPathPrefix")+""+Webstart.this.additionalArgs.get("svnProject")+""+Webstart.this.additionalArgs.get("svnPathPostfix")+"/"+Webstart.this.additionalArgs.get("qml"),clientTempDir);
					for (String file : clientTempDir.list()) {
						LOGGER.info("loaded schema file from svn : {} ({})",
								file, clientTempDir.getAbsolutePath());
					}
				} catch (Exception e) {
					throw new Exception(e.getMessage());
				}

				// File missingFile = null;
				// if (Webstart.this.additionalArgs.containsKey("missings"))
				// missingFile = new
				// File(Webstart.this.additionalArgs.get("missings"));
				File commandsFile = null;
				if (Webstart.this.additionalArgs.containsKey("commands"))
					commandsFile = new File(Webstart.this.additionalArgs.get("commands"));

				final String qmlPath = qmlFile.getAbsolutePath();

				final File certFile = new File(Webstart.this.additionalArgs.get("exportKey"));

				LOGGER.info("Cert File {} ({})", certFile, certFile.canRead());

				final boolean noEmpties = (Webstart.this.chckbxNoEmpty.isEnabled() && Webstart.this.chckbxNoEmpty.isSelected());
				final boolean pretestComments = (Webstart.this.chckbxPretestComments.isEnabled() && Webstart.this.chckbxPretestComments.isSelected());
				final boolean paraData = (Webstart.this.chckbxParadata.isEnabled() && Webstart.this.chckbxParadata.isSelected());

				final int packetSize = Integer.parseInt(Webstart.this.additionalArgs.get("packetSize"));

				System.out.print("usingNodes...");
				final NodeList usingNodes = XmlClient.getInstance().getByXPath(qmlPath, "//*[@variable]");
				System.out.println("done");
				
				final Map<String, Object> packageObj = new HashMap<String, Object>();
				final List<String> unfilteredVariables = Webstart.this.service.getVariables(qmlPath,usingNodes);

				if (unfilteredVariables == null) {
					Webstart.this.showError("no unfiltered Variables found for " + qmlPath);
				}

				final List<String> variables = unfilteredVariables;

				if (pretestComments) {
					final Set<String> commentSet = new LinkedHashSet<String>();
					commentSet.add("id");
					commentSet.add("token");

					final List<String> pages = Webstart.this.service.getPages(qmlPath);

					if (pages == null) {
						Webstart.this.showError("no pages found for " + qmlPath);
					}

					for (final String page : pages) {
						variables.add("comment" + page);
						commentSet.add("comment" + page);
					}

//					if (se21Split)
//						fileMapping.put(FileClient.getInstance().createOrGetFile("data_comments", "", additionalDir), commentSet);
				}

				// LOGGER.info("variables contains iinavbewin :
				// {}",variables.contains("iinavbewin"));

				// globals
				System.out.println("initialise...");
//				System.out.print("usingNodes...");
//				final NodeList usingNodes = XmlClient.getInstance().getByXPath(qmlPath, "//*[@variable]");
//				System.out.println("done");
				System.out.print("missings...");
				final Map<String, String> missings = DataService.getInstance().getMissings(qmlPath, usingNodes);
				System.out.println("done");
				System.out.print("values...");
				final Map<String, Map<String, String>> values = DataService.getInstance().getValues(qmlPath, usingNodes);
				System.out.println("done");
				System.out.print("variablePages...");
				final Map<String, List<String>> variablePages = DataService.getInstance().retrievePages(qmlPath, usingNodes);
				System.out.println("done");
				System.out.print("variableVisibles...");
				final Map<String, List<List<String>>> variableVisibles = DataService.getInstance().retrieveVisibles(qmlPath, usingNodes);
				System.out.println("done");
				System.out.print("types...");
				final Map<String, String> types = DataService.getInstance().getVariableTypes(qmlPath,usingNodes);
				System.out.println("done");
				System.out.print("pageTree...");
				final Map<String, List<String>> pageTree = DataService.getInstance().pageTree(qmlPath);
				System.out.println("done");
				System.out.print("questions...");
				final Map<String, Set<String>> questions = CountingService.getInstance().getQuestions(qmlPath, variables, usingNodes);
				System.out.println("done");

				if (Webstart.this.chckbxData.isSelected()) {
					try {

						final Map<String, Map<String, ValueEntry>> options = CountingService.getInstance().getOptions(qmlPath, variables, usingNodes);

						// packets
						System.out.println("packet size " + packetSize);
						int packetCount = Webstart.this.service.getPacketCount(Webstart.this.conf, Integer.parseInt(Webstart.this.additionalArgs.get("packetSize")), noEmpties);
						// packetCount = 1;
						System.out.println("packet count " + packetCount);
						// Merge packages
						final Map<HeaderEntry, Map<ValueEntry, Integer>> counting = new LinkedHashMap<HeaderEntry, Map<ValueEntry, Integer>>();
						Map<String, Integer> exitPages = new HashMap<String, Integer>();
						Map<String, Integer> cockpitInformations = new HashMap<String, Integer>();

						cockpitInformations.put("invited", Webstart.this.service.getParticipantCount(Webstart.this.conf));

						final Map<String, Object> csvPackage = new HashMap<String, Object>();
						final File dataFile = FileClient.getInstance().createOrGetFile("data", "tmp",clientTempDir);
						final File historyFile = FileClient.getInstance().createOrGetFile("history", "tmp",clientTempDir);

						boolean first = true;
						// --------------------
						for (int index = 1; index <= packetCount; index++) {
							final Map<TYPE, Object> packetData = Webstart.this.service.createPacket(qmlPath, variables, options, Webstart.this.conf, index, packetSize, noEmpties, pretestComments, !paraData, usingNodes, missings, values, variablePages, variableVisibles, types, pageTree, questions);
							if (packetData != null) {
								LOGGER.info("data packet {} of {} loaded", index, packetCount);
								// Merge countings
								final Map<HeaderEntry, Map<ValueEntry, Integer>> tmp = (Map<HeaderEntry, Map<ValueEntry, Integer>>) packetData.get(TYPE.counting);
								for (final Map.Entry<HeaderEntry, Map<ValueEntry, Integer>> entry : tmp.entrySet()) {
									Map<ValueEntry, Integer> countMap = null;
									if (counting.containsKey(entry.getKey())) {
										countMap = counting.get(entry.getKey());
										for (final Map.Entry<ValueEntry, Integer> item : entry.getValue().entrySet()) {
											if (countMap.containsKey(item.getKey()))
												countMap.put(item.getKey(), countMap.get(item.getKey()) + item.getValue());
											else
												countMap.put(item.getKey(), item.getValue());
										}
										counting.put(entry.getKey(), countMap);
									} else
										counting.put(entry.getKey(), entry.getValue());
								}

								// Merge exit Pages
								final Map<String, Integer> tmp1 = (Map<String, Integer>) packetData.get(TYPE.feedback);
								for (final Map.Entry<String, Integer> entry : tmp1.entrySet()) {
									if (exitPages.containsKey(entry.getKey()))
										exitPages.put(entry.getKey(), exitPages.get(entry.getKey()) + entry.getValue());
									else
										exitPages.put(entry.getKey(), entry.getValue());
								}

								// Merge Cockpit Informations
								final Map<String, Integer> tmp2 = (Map<String, Integer>) packetData.get(TYPE.cockpit);
								if (tmp2 != null) {
									Integer finishedCounter = 0;
									if (cockpitInformations.containsKey("finished"))
										finishedCounter = cockpitInformations.get("finished");
									if (tmp2.containsKey("finished"))
										finishedCounter = finishedCounter + tmp2.get("finished");
									cockpitInformations.put("finished", finishedCounter);

									Integer participatedCounter = 0;
									if (cockpitInformations.containsKey("participated"))
										participatedCounter = cockpitInformations.get("participated");
									if (tmp2.containsKey("participated"))
										participatedCounter = participatedCounter + tmp2.get("participated");
									cockpitInformations.put("participated", participatedCounter);
								}

								presentation.data.format.FormatFactory.getInstance().getFormat(presentation.data.format.FormatFactory.FORMAT.csv).formatAsStream(historyFile, dataFile, (Set<ParticipantType>) packetData.get(TYPE.data), ',', '\n', mapping, !first, Webstart.this.chckbxHistory.isSelected(), limitStrings, fileMapping);
								first = false;
							}
						}
						// ----------------------
						// Sortings
						if (exitPages != null) {
							final Map<String, String> sortingMap = new HashMap<String, String>();
							for (final String page : exitPages.keySet()) {
								String tmp = page;
								if (tmp.startsWith("/page"))
									tmp = tmp.replaceAll(Pattern.quote("/page"), "");
								tmp = tmp.replaceAll(Pattern.quote(".html"), "");
								sortingMap.put(tmp, page);
							}
							final Map<String, Integer> sortedExitPages = new LinkedHashMap<String, Integer>();
							final List<String> sortedKeys = new ArrayList<String>(sortingMap.keySet());
							Collections.sort(sortedKeys);
							for (final String sortKey : sortedKeys) {
								sortedExitPages.put(sortingMap.get(sortKey), exitPages.get(sortingMap.get(sortKey)));
							}
							exitPages = sortedExitPages;
						}
						final Map<String, Object> countingPackage = new HashMap<String, Object>();
						Object odfCounting = null;
						Object txtCounting = null;
						Object xmlCounting = null;

						odfCounting = presentation.counting.format.FormatFactory.getInstance().getFormat(presentation.counting.format.FormatFactory.FORMAT.odf).format(counting);
						txtCounting = presentation.counting.format.FormatFactory.getInstance().getFormat(presentation.counting.format.FormatFactory.FORMAT.txt).format(counting);
						xmlCounting = presentation.counting.format.FormatFactory.getInstance().getFormat(presentation.counting.format.FormatFactory.FORMAT.xml).format(counting);

						if (odfCounting != null)
							countingPackage.put("counting.odf", odfCounting);
						if (txtCounting != null)
							countingPackage.put("counting.txt", txtCounting);
						if (xmlCounting != null)
							countingPackage.put("counting.xml", xmlCounting);
						packageObj.put("counting", countingPackage);

						final Map<String, Object> feedbackPackage = new HashMap<String, Object>();
						Object txtExitPages = null;
						Object txtCountsPages = null;

						txtExitPages = presentation.feedback.format.FormatFactory.getInstance().getFormat(presentation.feedback.format.FormatFactory.FORMAT.txt).formatExits(exitPages);
						txtCountsPages = presentation.feedback.format.FormatFactory.getInstance().getFormat(presentation.feedback.format.FormatFactory.FORMAT.txt).formatCounts(cockpitInformations);

						if (txtExitPages != null)
							feedbackPackage.put("exitPages.txt", txtExitPages);
						if (txtCountsPages != null)
							feedbackPackage.put("feedback.txt", txtCountsPages);
						packageObj.put("feedback", feedbackPackage);

						if ((dataFile != null) && (dataFile.exists())) {
							// File copiedFile =
							// FileClient.getInstance().createTextFile("data.csv",
							// FileClient.getInstance().readAsString(dataFile));
							// File compressedCsvFile =
							// PackagerClient.getInstance().packZIP(copiedFile);
							File copiedFile = FileClient.getInstance().createOrGetFile("data", ".csv", dataFile.getParentFile());
							Files.move(dataFile, copiedFile);
							File compressedCsvFile = PackagerClient.getInstance().packZIP(copiedFile);
							if (!Webstart.this.chckbxVerschluesselt.isSelected()) {
								if (compressedCsvFile != null)
									csvPackage.put(compressedCsvFile.getName(), compressedCsvFile);
							} else {
								if ((certFile != null) && (certFile.exists())) {
									final CertificateClient certTools = CertificateClient.getInstance();
									try {
										final java.security.cert.Certificate cert = certTools.loadCertificate(certFile);
										if (compressedCsvFile != null) {
											File encryptedCompressedCsvFile = certTools.encryptFile(compressedCsvFile, FileClient.getInstance().createOrGetFile("data.csv.zip.encrypted", "", clientTempDir), cert);
											if (encryptedCompressedCsvFile != null)
												csvPackage.put("data.csv.zip.encrypted", encryptedCompressedCsvFile);
										}
									} catch (final Exception e2) {
										Webstart.this.showError("data.csv encryption failed : " + e2.getMessage());
									}
								}
							}
						}

//						if ((fileMapping != null) && (!fileMapping.isEmpty())) {
//							for (final File additionalDataFile : fileMapping.keySet()) {
//								if ((additionalDataFile != null) && (additionalDataFile.exists())) {
//									File copiedFile = FileClient.getInstance().createOrGetFile(additionalDataFile.getName(), ".csv", clientTempDir);
//									Files.move(additionalDataFile, copiedFile);
//									File compressedCsvFile = PackagerClient.getInstance().packZIP(copiedFile);
//									if (!Webstart.this.chckbxVerschluesselt.isSelected()) {
//										if (compressedCsvFile != null)
//											csvPackage.put(compressedCsvFile.getName(), compressedCsvFile);
//									} else {
//										if ((certFile != null) && (certFile.exists())) {
//											final CertificateClient certTools = CertificateClient.getInstance();
//											try {
//												final java.security.cert.Certificate cert = certTools.loadCertificate(certFile);
//												if (compressedCsvFile != null) {
//													File encryptedCompressedCsvFile = certTools.encryptFile(compressedCsvFile, FileClient.getInstance().createOrGetFile(additionalDataFile.getName() + ".csv.zip.encrypted", "", clientTempDir), cert);
//													if (encryptedCompressedCsvFile != null)
//														csvPackage.put(additionalDataFile.getName() + ".csv.zip.encrypted", encryptedCompressedCsvFile);
//												}
//											} catch (final Exception e2) {
//												Webstart.this.showError(additionalDataFile.getName() + ".csv encryption failed : " + e2.getMessage());
//											}
//										}
//									}
//								}
//
//							}
//						}

						if ((historyFile != null) && (Webstart.this.chckbxHistory.isSelected())) {
							// File copiedFile =
							// FileClient.getInstance().createTextFile("history.csv",
							// FileClient.getInstance().readAsString(historyFile));
							File copiedFile = FileClient.getInstance().createOrGetFile("history", ".csv", clientTempDir);
							Files.move(historyFile, copiedFile);
							File compressedCsvFile = PackagerClient.getInstance().packZIP(copiedFile);
							if (compressedCsvFile != null)
								csvPackage.put(compressedCsvFile.getName(), compressedCsvFile);
						}

						packageObj.put("csv", csvPackage);

					} catch (Throwable e) {
						Webstart.this.showError(e);
					}
				}

				if (Webstart.this.chckbxVerschluesselt.isSelected()) {
					final Map<String, Object> cerPackage = new HashMap<String, Object>();
					cerPackage.put("export.pem", certFile);
					packageObj.put("Certificates", cerPackage);

					final Map<String, Object> howtoPackage = new HashMap<String, Object>();

					LOGGER.info("Commands File : {}", commandsFile);
					if ((commandsFile != null) && (commandsFile.exists())) {
						final String commandsContent = FileClient.getInstance().readAsString(commandsFile);
						if (commandsContent != null) {
							LOGGER.info("Commands Content : {}", commandsContent);
							howtoPackage.put("Commands.txt", commandsContent);
						}
					}
					final Map<String, Object> installerPackage = new HashMap<String, Object>();
					installerPackage.put("Download_OpenSSL.txt", "You can find the Open SSL Software at https://www.openssl.org/about/binaries.html");
					howtoPackage.put("Installer", installerPackage);
					howtoPackage.put("decrypt.bat", "C:\\OpenSSL-Win64\\bin\\openssl.exe smime -decrypt -des -in  ../csv/data.csv.zip.encrypted  -inform DEM -inkey ../Certificates/privkey.pem  -out  ../csv/data.csv.decrypted.zip");
					howtoPackage.put("encrypt.bat", "C:\\OpenSSL-Win64\\bin\\openssl.exe smime -encrypt -des -binary -in  ../csv/data.csv.zip -outform DEM -out ../csv/data.csv.zip.encrypted  ../Certificates/export.pem");
					packageObj.put("HOWTO", howtoPackage);
				}

				if (Webstart.this.chckbxStatic.isSelected()) {
					System.out.println("Build static informations");
					// static
					final Map<TYPE, Object> staticData = Webstart.this.service.createStatic(qmlPath, variables, Webstart.this.conf, usingNodes);
					// LOGGER.info("static Data {}", staticData);
					if (staticData != null) {

						final Map<String, Object> codebookPackage = new HashMap<String, Object>();
						try {
							final Map<presentation.codebook.format.odf.OdfFormat.FORMAT, File> odfCodebook = (Map<presentation.codebook.format.odf.OdfFormat.FORMAT, File>) presentation.codebook.format.FormatFactory.getInstance().getFormat(presentation.codebook.format.FormatFactory.FORMAT.odf).format((Map<HeaderEntry, Map<String, ValueEntry>>) staticData.get(TYPE.codebook), mapping);
							final Object txtCodebook = presentation.codebook.format.FormatFactory.getInstance().getFormat(presentation.codebook.format.FormatFactory.FORMAT.txt).format((Map<HeaderEntry, Map<String, ValueEntry>>) staticData.get(TYPE.codebook), mapping);
							final Object xmlCodebook = presentation.codebook.format.FormatFactory.getInstance().getFormat(presentation.codebook.format.FormatFactory.FORMAT.xml).format((Map<HeaderEntry, Map<String, ValueEntry>>) staticData.get(TYPE.codebook), mapping);

							final Object txtVariableList = presentation.codebook.format.FormatFactory.getInstance().getFormat(presentation.codebook.format.FormatFactory.FORMAT.txt).formatVariableList((Map<HeaderEntry, Map<String, ValueEntry>>) staticData.get(TYPE.codebook), mapping);

							codebookPackage.put("codebook.odf", odfCodebook.get(presentation.codebook.format.odf.OdfFormat.FORMAT.odf));
							// codebookPackage.put("codebook.pdf",
							// odfCodebook.get(presentation.codebook.format.odf.OdfFormat.FORMAT.pdf));

							codebookPackage.put("codebook.txt", txtCodebook);
							codebookPackage.put("codebook.xml", xmlCodebook);
							codebookPackage.put("variables.txt", txtVariableList);

						} catch (final Exception e2) {
							Webstart.this.showError("codebook creation failed : " + e2.getMessage());
							// LOGGER.error("codebook creation failed", e2);
							// e2.printStackTrace();
						}
						packageObj.put("codebook", codebookPackage);
						// LOGGER.info("codebookPackage {}", codebookPackage);

						final Map<String, Object> navigationPackage = new HashMap<String, Object>();
						try {
							final Object txtTransitions = presentation.navigation.format.FormatFactory.getInstance().getFormat(presentation.navigation.format.FormatFactory.FORMAT.txt).format((Map<String, Set<TransitionEntry>>) staticData.get(TYPE.transition));
							final Object xmlTransitions = presentation.navigation.format.FormatFactory.getInstance().getFormat(presentation.navigation.format.FormatFactory.FORMAT.xml).format((Map<String, Set<TransitionEntry>>) staticData.get(TYPE.transition));
							navigationPackage.put("transitions.txt", txtTransitions);
							navigationPackage.put("transitions.xml", xmlTransitions);
						} catch (final Exception e2) {
							Webstart.this.showError("transitions creation failed : " + e2.getMessage());
							// LOGGER.error("transitions creation failed", e2);
							// // e2.printStackTrace();
							// firePropertyChange("done-exception", null, e2);
							// throw e2;
						}
						packageObj.put("navigation", navigationPackage);

						final Map<String, Object> intructionPackage = new HashMap<String, Object>();
						final Map<String, Object> stataPackage = new HashMap<String, Object>();
						@SuppressWarnings("unchecked")
						final Map<FORMATTYPE, Object> exportStata = (Map<FORMATTYPE, Object>) presentation.statistics.format.FormatFactory.getInstance().getFormat(presentation.statistics.format.FormatFactory.FORMAT.stata).format((Map<TYPE, Object>) staticData.get(TYPE.instruction), mapping, Webstart.this.additionalArgs.get("database"), new Date().toString(),limitLabels);

						stataPackage.put("data.do", exportStata.get(FORMATTYPE.data));
						stataPackage.put("history.do", exportStata.get(FORMATTYPE.history));
						intructionPackage.put("Stata", stataPackage);

						final Map<String, Object> dokudatPackage = new HashMap<String, Object>();
						final Map<String, Object> exportDokudat = (Map<String, Object>) presentation.dokudat.format.FormatFactory.getInstance().getFormat(presentation.dokudat.format.FormatFactory.FORMAT.txt).format((Map<HeaderEntry, Object>) staticData.get(TYPE.dokudat), mapping);

						dokudatPackage.put("questions.csv", exportDokudat.get("questions") + "");
						dokudatPackage.put("answers.csv", exportDokudat.get("answers") + "");
						intructionPackage.put("Dokudat", dokudatPackage);

						final Map<String, Object> qmlPackage = new HashMap<String, Object>();
						qmlPackage.put("questionnaire.xml", qmlFile);
						intructionPackage.put("QML", qmlPackage);

						packageObj.put("instruction", intructionPackage);

						final Map<String, Object> docPackage = new HashMap<String, Object>();
						docPackage.put("doc.txt", "Documentation File");

						// Add Missings Informations
						final StringBuffer missingsContent = new StringBuffer();
						missingsContent.append("Missings im Export\n");
						missingsContent.append("-------------------\n");
						missingsContent.append("\n");
						missingsContent.append("NICHT BEANTWORTET\n");
						missingsContent.append("NOT ANSWERED = " + Webstart.this.missingNotAnswered.getText() + "\n");
						missingsContent.append("\n");
						missingsContent.append("SEITE NICHT BESUCHT\n");
						missingsContent.append("NOT VISITED = " + Webstart.this.missingNotVisited.getText() + "\n");
						missingsContent.append("\n");
						missingsContent.append("NICHT GESEHEN\n");
						missingsContent.append("UNSELECTED, EMPTY, UNSET, NOT CALCULATED = " + Webstart.this.missingNotSeen.getText() + "\n");
						missingsContent.append("\n");
						missingsContent.append("STEUERUNGSVARIABLE FEHLT\n");
						missingsContent.append("INITFORGET1, INITFORGET2 = " + Webstart.this.missingInitGet.getText() + "\n");
						missingsContent.append("\n");
						missingsContent.append("DEFAULT = " + mapping.get("DEFAULT") + "\n");
						missingsContent.append("\n");
						missingsContent.append("--------------------\n");
						missingsContent.append("\n");
						missingsContent.append("Legende\n");
						missingsContent.append("\n");
						missingsContent.append("NICHT BEANTWORTET: Item wurde gesehen, aber nicht beantwortet\n");
						missingsContent.append("\n");
						missingsContent.append("SEITE NICHT BESUCHT: Seite, auf der sich das Item befindet, wurde gemäß Fragebogensteuerung oder aufgrund eines vorherigen Befragungsabbruches nicht besucht\n");
						missingsContent.append("UNSELECTED für Einfachauswahl\n");
						missingsContent.append("UNSET für Mehrfachauswahl\n");
						missingsContent.append("EMPTY für offene Fragen\n");
						missingsContent.append("NOT CALCULATED für nicht berechnete Variablen\n");
						missingsContent.append("\n");
						missingsContent.append("NICHT GESEHEN: Item wurde gemäß Fragebogensteuerung nicht angezeigt oder befindet sich auf der Seite des Befragungsabbruches\n");
						missingsContent.append("UNSELECTED für Einfachauswahl\n");
						missingsContent.append("UNSET für Mehrfachauswahl\n");
						missingsContent.append("EMPTY für offene Fragen\n");
						missingsContent.append("NOT CALCULATED für nicht berechnete Variablen\n");
						missingsContent.append("\n");
						missingsContent.append("STEUERUNGSVARIABLE FEHLT: Variable wurde nicht erhoben (-9991 oder -9992), jedoch für die Fragebogensteuerung verwendet\n");
						missingsContent.append("INITFORGET1 für SEITE NICHT BESUCHT\n");
						missingsContent.append("INITFORGET2 für NICHT GESEHEN\n");
						missingsContent.append("\n");
						missingsContent.append("DEFAULT: voreingestellte Missing-Werte, insbesondere bei technischen Variablen\n");

						docPackage.put("Missings.txt", missingsContent.toString());

						packageObj.put("Doc", docPackage);
					}
				}
				
				if (Webstart.this.chckbxJson.isSelected()) {
//					final String dataAcquestionProject="DATAACQUESTIONPROJECT";
//					final String surveyId="SURVEYID";
//					final String instrumentId="INSTRUMENTID";
//					final String datasetId="DATASETID";
					
//					final Map<String, Object> jsonPackage = new HashMap<String, Object>();
//					MDMService jsonService = MDMService.getInstance();
//					final Map<String, Object> jsonData =  jsonService.buildUp(qmlPath,null,null, dataAcquestionProject, surveyId, instrumentId,
//							datasetId);
//					
//					
//
//					
//					if (jsonData != null) {
//						
//						Map<String,Map<String,Object>> csvVariables = (Map<String,Map<String,Object>>) jsonData.get("variable");
//						Map<String, String> variablesOutput = (Map<String, String>) FormatFactory.getInstance().getFormat(FormatFactory.FORMAT.csv).formatCSV(csvVariables);
//						final Map<String, Object> jsonVariablePackage = new HashMap<String, Object>();
//						for (Map.Entry<String, String> item : variablesOutput.entrySet()) {
////							jsonVariablePackage.put(item.getKey()+".json", item.getValue()+"");
//						}
//						jsonPackage.put("Variables", jsonVariablePackage);
//						
//						Map<String, JsonObject> jsonQuestions = (Map<String, JsonObject>) jsonData.get("question");
//						Map<String, String> questionsOutput = (Map<String, String>) FormatFactory.getInstance().getFormat(FormatFactory.FORMAT.json).formatJSON(jsonQuestions);
//						final Map<String, Object> jsonQuestionPackage = new HashMap<String, Object>();
//						for (Map.Entry<String, String> item : questionsOutput.entrySet()) {
//							jsonQuestionPackage.put(item.getKey()+".json", item.getValue()+"");
//						}
//						jsonPackage.put("Questions", jsonQuestionPackage);
//					}
//					
//					
//					packageObj.put("MDM", jsonPackage);
				}
				
				
				final long stopTime = System.currentTimeMillis();
				final long minuten = ((stopTime - startTime) / 1000) / 60;
				LOGGER.info("completed in {} minutes", minuten);
				try {
					LOGGER.info("create output file...");
					// Webstart.this.outputFile = File.createTempFile("output",
					// System.currentTimeMillis() + "");
//					Webstart.this.outputFile = FileClient.getInstance().createOrGetFile("output", System.currentTimeMillis() + "", clientTempDir);
					Webstart.this.outputFile = FileClient.getInstance().createOrGetFile("output", "", clientTempDir);
					LOGGER.info("pack data...");
					PackagerClient.getInstance().packageZip(Webstart.this.outputFile, packageObj,clientTempDir);
					LOGGER.info("pack data done");
				} catch (final IOException e) {
					Webstart.this.showError(e);
					// e.printStackTrace();
					// LOGGER.error("error ", e);
					// firePropertyChange("done-exception", null, e);
					// throw e;
				}
			} catch (Exception e) {
				Webstart.this.showError(e);
				// LOGGER.error("main error ", e);
				// firePropertyChange("done-exception", null, e);
				// throw e;
			}
			return null;
		}

		/*
		 * Executed in event dispatch thread
		 */
		/*
		 * (non-Javadoc)
		 * 
		 * @see javax.swing.SwingWorker#done()
		 */
		@Override
		public void done() {
			Webstart.this.btnProceed.setEnabled(true);
			Webstart.this.progressBar.setValue(0);
			Webstart.this.progressBar.setString("done");
			Webstart.this.progressBar.setIndeterminate(false);
			final boolean flag = ((Webstart.this.outputFile != null) && (Webstart.this.outputFile.exists()));
			Webstart.this.saveBtn.setEnabled(flag);
		}
	}

	/**
	 * The Class CustomOutputStream.
	 */
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
			this.field.setLineWrap(true);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.io.OutputStream#write(int)
		 */
		@Override
		public void write(final int b) throws IOException {
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
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					final Webstart window = new Webstart(additionalArgs);
					window.frame.setVisible(true);
				} catch (final Exception e) {
					// e.printStackTrace();
					LOGGER.error("run failed ", e);
				}
			}
		});
	}

	/**
	 * Create the application.
	 * 
	 * @param additionalArgs
	 *            the additional args
	 */
	public Webstart(final Map<String, String> additionalArgs) {
		this.service = StatisticService.getInstance();
		this.service.addObserver(this);
		this.additionalArgs = additionalArgs;
		this.initialize();

		this.conf = DataService.getInstance().getConfiguration();

		URL url = null;
		try {
			// Lookup the javax.jnlp.BasicService object
			final BasicService bs = (BasicService) ServiceManager.lookup("javax.jnlp.BasicService");
			if (bs != null)
				url = bs.getCodeBase();
		} catch (final UnavailableServiceException ue) {
			LOGGER.error("BasicService retrieval failed : {}", ue.getCause());
		}

		final Properties system = ConfigurationUtils.getInstance().getConfiguration("System.properties");
		for (final Map.Entry<Object, Object> property : system.entrySet()) {
			if (!this.additionalArgs.containsKey(property.getKey() + ""))
				this.additionalArgs.put(property.getKey() + "", property.getValue() + "");
		}

//		LOGGER.info("base url {}", url);

		if (url != null) {
			InputStream in = null;
			try {
				
				
				// if (additionalArgs.containsKey("missings")) {
				// in = new URL(url.toString() +
				// additionalArgs.get("missings")).openStream();
				// final File missingsFile = File.createTempFile("missings",
				// System.currentTimeMillis() + "");
				// FileUtils.writeStringToFile(missingsFile,
				// IOUtils.toString(in));
				// this.additionalArgs.put("missings",
				// missingsFile.getAbsolutePath());
				// }
				if (additionalArgs.containsKey("commands")) {
					in = new URL(url.toString() + additionalArgs.get("commands")).openStream();
					final File commandsFile = new File(clientTempDir,"commands");
					FileUtils.writeStringToFile(commandsFile, IOUtils.toString(in));
					this.additionalArgs.put("commands", commandsFile.getAbsolutePath());
				}
				if (additionalArgs.containsKey("exportKey")) {
					in = new URL(url.toString() + additionalArgs.get("exportKey")).openStream();
					final File certFile = new File(clientTempDir,"export");
					FileUtils.writeStringToFile(certFile, IOUtils.toString(in));
					this.additionalArgs.put("exportKey", certFile.getAbsolutePath());
				}
			} catch (final IOException e) {
				LOGGER.error("Base URL retrieval failed ", e);
				// e.printStackTrace();
			} finally {
				if (in != null)
					IOUtils.closeQuietly(in);
			}
		}

		// Add Defaults
		if (!this.additionalArgs.containsKey("packetSize")) {
			this.additionalArgs.put("packetSize", "1000");
		}

		// this.mapping = new HashMap<String, String>();
		// this.mapping.put("true", "1");
		// this.mapping.put("false", "0");
		// // this.mapping.put("UNSELECTED", "-99");
		// // this.mapping.put("EMPTY", "-99");
		// // this.mapping.put("UNSET", "-99");
		// // this.mapping.put("NOT CALCULATED", "-99");
		//
		// this.mapping.put("UNSELECTED", "-2");
		// this.mapping.put("EMPTY", "-2");
		// this.mapping.put("UNSET", "-2");
		// this.mapping.put("NOT CALCULATED", "-2");
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		this.format = new DecimalFormat("##########");
//		this.clientTempDir = Files.createTempDir();
		this.clientTempDir = DirectoryClient.getInstance().getTemp();
		this.frame = new JFrame();
		this.frame.setTitle("Zofar Exporter");
		this.frame.setBounds(100, 100, 591, 881);
		this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		this.database = new JTextField();
		this.database.addActionListener(this);
		this.database.addFocusListener(this);
		this.frame.getContentPane().setLayout(new FormLayout(new ColumnSpec[] { ColumnSpec.decode("35px"), ColumnSpec.decode("72px"), ColumnSpec.decode("442px:grow"), ColumnSpec.decode("10dlu"), }, new RowSpec[] { FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, RowSpec.decode("19px"), FormFactory.LINE_GAP_ROWSPEC, RowSpec.decode("19px"), FormFactory.LINE_GAP_ROWSPEC, RowSpec.decode("19px"), RowSpec.decode("20px"), RowSpec.decode("19px"), FormFactory.LINE_GAP_ROWSPEC, RowSpec.decode("19px"), FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, RowSpec.decode("max(123dlu;default):grow"), FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.LINE_GAP_ROWSPEC, RowSpec.decode("60px:grow"), FormFactory.LINE_GAP_ROWSPEC, RowSpec.decode("37px:grow"), FormFactory.LINE_GAP_ROWSPEC, RowSpec.decode("10px"), RowSpec.decode("20px"), RowSpec.decode("10px"), FormFactory.LINE_GAP_ROWSPEC, RowSpec.decode("25px"), FormFactory.LINE_GAP_ROWSPEC, RowSpec.decode("151px"), FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, }));

		this.lblServer = new JLabel("Server");
		this.frame.getContentPane().add(this.lblServer, "2, 3, fill, center");

		this.port = new JTextField();
		this.port = new JFormattedTextField(this.format);
		this.port.addActionListener(this);
		this.port.addFocusListener(this);

		this.server = new JTextField();
		this.server.addActionListener(this);
		this.server.addFocusListener(this);
		this.server.setEditable(false);
		this.frame.getContentPane().add(this.server, "3, 3, fill, top");
		this.server.setColumns(10);

		this.lblPort = new JLabel("Port");
		this.frame.getContentPane().add(this.lblPort, "2, 5, fill, center");
		this.port.setEditable(false);
		this.frame.getContentPane().add(this.port, "3, 5, fill, top");
		this.port.setColumns(10);

		this.lblDatabase = new JLabel("Database");
		this.frame.getContentPane().add(this.lblDatabase, "2, 7, fill, center");
		this.database.setEditable(false);
		this.frame.getContentPane().add(this.database, "3, 7, fill, top");
		this.database.setColumns(10);

		final JLabel lblUser = new JLabel("User");
		this.frame.getContentPane().add(lblUser, "2, 9, fill, center");

		this.user = new JTextField();
		this.user.addActionListener(this);
		this.user.addFocusListener(this);
		this.user.setEditable(false);
		this.frame.getContentPane().add(this.user, "3, 9, fill, top");
		this.user.setColumns(10);

		final JLabel lblPassword = new JLabel("Password");
		this.frame.getContentPane().add(lblPassword, "2, 11, left, center");

		this.password = new JPasswordField();

		this.password.addActionListener(this);
		this.password.addFocusListener(this);
		this.password.setEditable(false);
		this.frame.getContentPane().add(this.password, "3, 11, fill, top");
		this.password.setColumns(10);

		lblMissings = new JLabel("Mappings");
		frame.getContentPane().add(lblMissings, "3, 13, center, center");

		panel_2 = new JPanel();
		frame.getContentPane().add(panel_2, "3, 15, fill, top");
		panel_2.setLayout(new FormLayout(new ColumnSpec[] { FormFactory.RELATED_GAP_COLSPEC, FormFactory.DEFAULT_COLSPEC, FormFactory.RELATED_GAP_COLSPEC, ColumnSpec.decode("default:grow"), FormFactory.RELATED_GAP_COLSPEC, FormFactory.DEFAULT_COLSPEC, FormFactory.RELATED_GAP_COLSPEC, FormFactory.DEFAULT_COLSPEC, }, new RowSpec[] { FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, RowSpec.decode("default:grow"), }));

		lblWahr = new JLabel("WAHR");
		panel_2.add(lblWahr, "2, 2, right, center");

		mappingTrue = new JTextField();
		mappingTrue.setText("1");
		panel_2.add(mappingTrue, "4, 2, left, center");
		mappingTrue.setColumns(10);

		lblFalsch = new JLabel("FALSCH");
		panel_2.add(lblFalsch, "2, 4, right, center");

		mappingFalse = new JTextField();
		mappingFalse.setText("0");
		panel_2.add(mappingFalse, "4, 4, left, center");
		mappingFalse.setColumns(10);

		lblNichtBeantwortet = new JLabel("NICHT BEANTWORTET");
		panel_2.add(lblNichtBeantwortet, "2, 6, right, center");

		missingNotAnswered = new JTextField();
		missingNotAnswered.setText("-9990");
		panel_2.add(missingNotAnswered, "4, 6, left, center");
		missingNotAnswered.setColumns(10);

		lblNichtBesucht = new JLabel("NICHT BESUCHT");
		panel_2.add(lblNichtBesucht, "2, 8, right, center");

		missingNotVisited = new JTextField();
		missingNotVisited.setText("-9991");
		panel_2.add(missingNotVisited, "4, 8, left, center");
		missingNotVisited.setColumns(10);

		lblNichtGesehen = new JLabel("NICHT GESEHEN");
		panel_2.add(lblNichtGesehen, "2, 10, right, center");

		missingNotSeen = new JTextField();
		missingNotSeen.setText("-9992");
		panel_2.add(missingNotSeen, "4, 10, left, center");
		missingNotSeen.setColumns(10);

		lblINITIALGET = new JLabel("GET INITIALISIERT");
		panel_2.add(lblINITIALGET, "2, 12, right, center");

		missingInitGet = new JTextField();
		missingInitGet.setText("-9995");
		panel_2.add(missingInitGet, "4, 12, left, center");
		missingInitGet.setColumns(10);

		lblDefault = new JLabel("DEFAULT");
		panel_2.add(lblDefault, "2, 14, right, default");

		defaultField = new JTextField();
		defaultField.setText("-9999");
		panel_2.add(defaultField, "4, 14, left, center");
		defaultField.setColumns(10);

		lblOutput = new JLabel("Output");
		frame.getContentPane().add(lblOutput, "3, 17, center, center");

		this.panel = new JPanel();
		this.frame.getContentPane().add(this.panel, "3, 19, center, fill");
		this.panel.setLayout(new FormLayout(new ColumnSpec[] { FormFactory.RELATED_GAP_COLSPEC, FormFactory.DEFAULT_COLSPEC, FormFactory.RELATED_GAP_COLSPEC, FormFactory.DEFAULT_COLSPEC, FormFactory.RELATED_GAP_COLSPEC, FormFactory.DEFAULT_COLSPEC, FormFactory.RELATED_GAP_COLSPEC, FormFactory.DEFAULT_COLSPEC, FormFactory.RELATED_GAP_COLSPEC, FormFactory.DEFAULT_COLSPEC, }, new RowSpec[] { FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, }));

		this.chckbxStatic = new JCheckBox("Static");
		this.panel.add(this.chckbxStatic, "2, 2, center, center");
		this.chckbxStatic.setSelected(true);

		this.chckbxData = new JCheckBox("Data");
		this.chckbxData.addChangeListener(this);
		this.panel.add(this.chckbxData, "4, 2, center, center");

		this.chckbxHistory = new JCheckBox("History");
		this.chckbxHistory.setEnabled(false);
		panel.add(chckbxHistory, "6, 2, center, center");

		this.chckbxNoEmpty = new JCheckBox("No empty Sets");
		this.chckbxNoEmpty.setEnabled(false);
		panel.add(chckbxNoEmpty, "8, 2, left, center");
		
		this.chckbxJson = new JCheckBox("JSON");
		panel.add(chckbxJson, "2, 4, center, center");

		this.chckbxVerschluesselt = new JCheckBox("Verschlüsselt");
		this.chckbxVerschluesselt.setSelected(true);
		this.chckbxVerschluesselt.setEnabled(false);
		panel.add(chckbxVerschluesselt, "6, 4");

		this.chckbxParadata = new JCheckBox("Para-Daten");
		this.chckbxParadata.setSelected(false);
		this.chckbxParadata.setEnabled(false);
		panel.add(chckbxParadata, "4, 4");

		this.chckbxPretestComments = new JCheckBox("Pretest Comments");
		this.chckbxPretestComments.setEnabled(false);
		panel.add(chckbxPretestComments, "8, 4, left, center");

		this.lblPacketSize = new JLabel("Packet Size");
		this.panel.add(this.lblPacketSize, "2, 6, right, default");

		this.packetSizeField = new JTextField();
		this.packetSizeField.setText(this.additionalArgs.get("packetSize"));
		this.packetSizeField.setEditable(false);
		this.panel.add(this.packetSizeField, "4, 6, fill, default");
		this.packetSizeField.setColumns(10);

		this.panel_1 = new JPanel();
		this.frame.getContentPane().add(this.panel_1, "3, 21, center, center");
		this.panel_1.setLayout(new FormLayout(new ColumnSpec[] { FormFactory.RELATED_GAP_COLSPEC, FormFactory.DEFAULT_COLSPEC, FormFactory.RELATED_GAP_COLSPEC, FormFactory.DEFAULT_COLSPEC, }, new RowSpec[] { FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, }));

		this.btnProceed = new JButton("Proceed");
		this.panel_1.add(this.btnProceed, "2, 2, center, center");

		this.saveBtn = new JButton("Save Export");
		this.panel_1.add(this.saveBtn, "4, 2, center, center");
		this.saveBtn.setEnabled(false);
		this.saveBtn.addActionListener(this);
		this.btnProceed.addActionListener(this);

		this.progressBar = new JProgressBar();
		this.progressBar.setStringPainted(true);
		this.progressBar.setMinimum(0);
		this.progressBar.setMaximum(this.phases);
		this.progressBar.setValue(0);
		this.progressBar.setString("initialisation");
		this.frame.getContentPane().add(this.progressBar, "3, 24, fill, top");
		
		this.consolePane = new JTextArea();
		this.consolePane.setColumns(100);
		this.consolePane.setEditable(false);
		final PrintStream printStream = new PrintStream(new CustomOutputStream(this.consolePane));
		final JScrollPane consoleScroll = new JScrollPane(this.consolePane);
		this.frame.getContentPane().add(consoleScroll, "3, 29, fill, fill");
		if (this.additionalArgs.containsKey("server"))
			this.server.setText(this.additionalArgs.get("server"));
		if (this.additionalArgs.containsKey("port")){
//			this.port.setText(Integer.parseInt(this.additionalArgs.get("port"))+"");
			this.port.setText(this.additionalArgs.get("port"));
		}
		if (this.additionalArgs.containsKey("database"))
			this.database.setText(this.additionalArgs.get("database"));
		if (this.additionalArgs.containsKey("user"))
			this.user.setText(this.additionalArgs.get("user"));
		if (this.additionalArgs.containsKey("password"))
			this.password.setText(this.additionalArgs.get("password"));
		System.setOut(printStream);
		System.setErr(printStream);
		
		this.frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				try {
					FileUtils.deleteDirectory(clientTempDir);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(final ActionEvent e) {
		// LOGGER.info("action {}", e.getSource());
		if (e.getSource().equals(this.btnProceed)) {
			this.additionalArgs.put("server", this.server.getText());
			this.additionalArgs.put("port", this.port.getText());
			this.additionalArgs.put("database", this.database.getText());
			this.additionalArgs.put("user", this.user.getText());
			this.additionalArgs.put("password", new String(this.password.getPassword()));
			this.additionalArgs.put("packetSize", this.packetSizeField.getText() + "");
			LOGGER.info("==> {}", this.additionalArgs);
			this.process();
		}
		if (e.getSource().equals(this.saveBtn)) {
			if (this.fc == null) {
				this.fc = new JFileChooser();
				final File preset = new File(new File(System.getProperty("user.home")), "export.zip");
				this.fc.setSelectedFile(preset);
			}
			final int returnVal = this.fc.showSaveDialog(this.frame);
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				final File file = this.fc.getSelectedFile();
				try {
					FileUtils.copyFile(this.outputFile, file);
				} catch (final IOException e1) {
					Webstart.this.showError("Copy file failed : " + e1.getMessage());
					// e1.printStackTrace();
					LOGGER.error("Copy file failed ", e1);
				}
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.FocusListener#focusGained(java.awt.event.FocusEvent)
	 */
	@Override
	public void focusGained(final FocusEvent e) {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.FocusListener#focusLost(java.awt.event.FocusEvent)
	 */
	@Override
	public void focusLost(final FocusEvent e) {
		// LOGGER.info("focus lost {}", e.getSource());
		// if (e.getSource().equals(server))
		// additionalArgs.put("server", server.getText());
		// if (e.getSource().equals(port))
		// additionalArgs.put("port", port.getText());
		// if (e.getSource().equals(database))
		// additionalArgs.put("database", database.getText());
		// if (e.getSource().equals(user))
		// additionalArgs.put("user", user.getText());
		// if (e.getSource().equals(password))
		// additionalArgs.put("password", password.getPassword() + "");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
	 */
	@Override
	public void update(final Observable o, final Object arg) {
		LOGGER.info("observer {} ({})", o.getClass(), arg);
		if (((String) arg).startsWith("phases=")) {
			final String n = ((String) arg).replaceAll("phases=", "");
			this.phases = Integer.parseInt(n);
		} else {
			this.progressBar.setIndeterminate(false);
			this.progressBar.setValue(this.progressBar.getValue() + 1);
			this.progressBar.setString(arg + "");
		}
	}

	@Override
	public void stateChanged(ChangeEvent e) {
		Object source = e.getSource();
		if (source != null) {
			if (source.equals(this.chckbxData)) {
				this.chckbxHistory.setEnabled(this.chckbxData.isSelected());
				if (!this.chckbxData.isSelected())
					this.chckbxHistory.setSelected(false);

				this.chckbxNoEmpty.setEnabled(this.chckbxData.isSelected());
				if (!this.chckbxData.isSelected())
					this.chckbxNoEmpty.setSelected(false);

				this.chckbxPretestComments.setEnabled(this.chckbxData.isSelected());
				if (!this.chckbxData.isSelected())
					this.chckbxPretestComments.setSelected(false);

				this.chckbxVerschluesselt.setEnabled(this.chckbxData.isSelected());

				this.chckbxParadata.setEnabled(this.chckbxData.isSelected());
				if (!this.chckbxData.isSelected())
					this.chckbxParadata.setSelected(false);
			}
		}
	}

}

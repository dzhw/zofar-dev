package eu.dzhw.zofar.management.dev.automation.mdm;

import java.awt.Dimension;
import java.io.File;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Pattern;

import javax.json.JsonObject;

import org.apache.commons.io.FilenameUtils;
import org.apache.xmlbeans.XmlObject;
import org.odftoolkit.odfdom.dom.element.text.TextTableOfContentElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.his.zofar.xml.questionnaire.PageType;
import de.his.zofar.xml.questionnaire.QuestionnaireDocument;
import eu.dzhw.zofar.management.dev.automation.AbstractExecutor;
import eu.dzhw.zofar.management.dev.automation.mdm.presentation.FormatFactory;
import eu.dzhw.zofar.management.dev.automation.mdm.service.MDMService;
import eu.dzhw.zofar.management.dev.automation.screenshot.ScreenshotGenerator;
import eu.dzhw.zofar.management.security.text.TextCipherClient;
import eu.dzhw.zofar.management.utils.files.DirectoryClient;
import eu.dzhw.zofar.management.utils.files.FileClient;
import eu.dzhw.zofar.management.utils.files.PackagerClient;
import eu.dzhw.zofar.management.utils.odf.TextClient;
import eu.dzhw.zofar.management.utils.odf.components.WriterDocument;
import eu.dzhw.zofar.management.utils.system.ConfigurationUtils;

public class ExecutorDebug extends AbstractExecutor {

	private static final long serialVersionUID = 1L;
	private static final ExecutorDebug INSTANCE = new ExecutorDebug();
	private static final Logger LOGGER = LoggerFactory.getLogger(ExecutorDebug.class);

	public enum Parameter implements ABSTRACTPARAMETER {
		survey, dataAcquestionProject, surveyId, instrumentId, datasetId, workDir, surveyScreenshotCacheDir, surveyExportDir, svnUrl, svnUser, svnPass, serverUrl, serverUser, serverPass, dbLocation, dbUser, dbPass, dbPort, app_logins;
	};

	private ExecutorDebug() {
		super();
	}

	public static ExecutorDebug getInstance() {
		return INSTANCE;
	}

	public ParameterMap<ABSTRACTPARAMETER, Object> getParameterMap(final String survey,
			final String dataAcquestionProject, final String surveyId, final String instrumentId,
			final String datasetId, final File workDir, final File surveyScreenshotCacheDir, final File surveyExportDir,
			final String svnUrl, final String svnUser, final String svnPass, final String serverUrl,
			final String serverUser, final String serverPass, final String dbLocation, final String dbPort,
			final String dbUser, final String dbPass, final Map<String, String> appLogins) {
		final ParameterMap<ABSTRACTPARAMETER, Object> back = new ParameterMap<ABSTRACTPARAMETER, Object>();
		back.put(Parameter.survey, survey);
		back.put(Parameter.dataAcquestionProject, dataAcquestionProject);
		back.put(Parameter.surveyId, surveyId);
		back.put(Parameter.instrumentId, instrumentId);
		back.put(Parameter.datasetId, datasetId);

		back.put(Parameter.workDir, workDir);
		back.put(Parameter.surveyScreenshotCacheDir, surveyScreenshotCacheDir);
		back.put(Parameter.surveyExportDir, surveyExportDir);
		back.put(Parameter.svnUrl, svnUrl);
		back.put(Parameter.svnUser, svnUser);
		back.put(Parameter.svnPass, svnPass);
		back.put(Parameter.serverUrl, serverUrl);
		back.put(Parameter.serverUser, serverUser);
		back.put(Parameter.serverPass, serverPass);
		back.put(Parameter.dbLocation, dbLocation);

		back.put(Parameter.dbPort, dbPort);
		back.put(Parameter.dbUser, dbUser);
		back.put(Parameter.dbPass, dbPass);

		back.put(Parameter.app_logins, appLogins);

		return back;
	}

	public void process(ParameterMap<ABSTRACTPARAMETER, Object> parameter) throws Exception {
		this.process(parameter, null);
	}

	public void process(ParameterMap<ABSTRACTPARAMETER, Object> parameter,
			final ArrayList<java.awt.Dimension> dimensions) throws Exception {

		final String survey = (String) parameter.get(Parameter.survey);

		final String dataAcquestionProject = (String) parameter.get(Parameter.dataAcquestionProject);
		System.out.println("dataAcquestionProject : " + dataAcquestionProject);
		final String surveyId = (String) parameter.get(Parameter.surveyId);
		final String instrumentId = (String) parameter.get(Parameter.instrumentId);
		final String datasetId = (String) parameter.get(Parameter.datasetId);

		final File workDir = (File) parameter.get(Parameter.workDir);

		final String svnUrl = (String) parameter.get(Parameter.svnUrl);
		final String svnUser = (String) parameter.get(Parameter.svnUser);
		final String svnPass = (String) parameter.get(Parameter.svnPass);
		// final String serverUrl = (String) parameter.get(Parameter.serverUrl);

		// final String serverUser = (String) parameter.get(Parameter.serverUser);
		// final String serverPass = (String) parameter.get(Parameter.serverPass);

		final String dbLocation = (String) parameter.get(Parameter.dbLocation);
		final String dbPort = (String) parameter.get(Parameter.dbPort);
		final String dbUser = (String) parameter.get(Parameter.dbUser);
		final String dbPass = (String) parameter.get(Parameter.dbPass);

		// final Map<String, String> app_logins = (Map<String, String>)
		// parameter.get(Parameter.app_logins);

		String token = eu.dzhw.zofar.management.utils.string.StringUtils.getInstance().randomString(5)
				+ System.currentTimeMillis();

		final File projectDir = builder.checkoutProject(survey, workDir, svnUrl, svnUser, svnPass);
		//
		// // Build Project for generating Translation
		//
		final String dbName = ("mdm_" + survey).toLowerCase().replace('.', '_').replace('-', '_');

		final File fieldProjectDir = builder.buildExistingProject(projectDir, dbLocation, dbPort, dbName,
				dbName + ".session", dbUser, dbPass, false, true, false, true, true, false, true,
				"forward,backward,same", "TOKEN", true, false, false, svnUrl, svnUser, svnPass, true, token);

		// Build from Screenshot-Automation
		// final File modifiedProjectDir = builder.buildExistingProject(projectDir,
		// dbLocation,dbPort, dbName,
		// dbName + ".session", dbUser, dbPass,
		// false, true, false, true, true, mdm, noVisibleMap,
		// "forward,backward,same", "TOKEN", true, false, false, svnUrl, svnUser,
		// svnPass, true, token);

		File warFile = null;
		if (fieldProjectDir != null) {
			try {
				warFile = mavenClient.doCleanInstall(fieldProjectDir);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		List<File> unpackedWar = null;
		if ((warFile != null) && (warFile.exists())) {
			unpackedWar = PackagerClient.getInstance().extractZip(warFile);
		}

		// Extract Translation and QML from war
		Map<String, Properties> transFiles = new HashMap<String, Properties>();
		File qmlFile = null;
		File fieldScreenshots = null;
		File mdmScreenshots = null;
		File preloadSQL = null;

		if (unpackedWar != null) {
			for (final File file : unpackedWar) {
				final String filename = file.getName();

				if (filename.equals("questionnaire.xml"))
					qmlFile = file;

				if (filename.equals("preloads_postgres.sql"))
					preloadSQL = file;

				if (filename.matches("text_[a-z]+\\.properties")) {
					// LOGGER.info("filename : " + filename);
					String location = filename.replaceAll("\\.properties", "");
					location = location.replaceAll("text_", "");
					transFiles.put(location,
							ConfigurationUtils.getInstance().getConfigurationFromFileSystem(file.getAbsolutePath()));
				}
			}

			if (qmlFile != null) {
				qmlFile = FileClient.getInstance().copyToDir(qmlFile, workDir);
			}

			if (preloadSQL != null) {
				preloadSQL = FileClient.getInstance().copyToDir(preloadSQL, workDir);
			}
		}
		// // Generate Field Screenshots
		if (!transFiles.isEmpty()) {
			if ((warFile != null) && (warFile.exists())) {
				 fieldScreenshots = this.processingScreenshotting(warFile, parameter,
				 dimensions, transFiles,ScreenshotMode.FIELD);
			}
		}

		// fieldScreenshots = new
		// File("xxxx/MDM_Projects/debug2/screenshots.zip");

		MDMService mdmService = MDMService.getInstance();
		final FileClient fileClient = FileClient.getInstance();
		final File mdmDirectory = DirectoryClient.getInstance().createDir(workDir, "MDM");
		if ((qmlFile != null) && (fieldScreenshots != null)) {

			final File instrumentDirectory = DirectoryClient.getInstance().createDir(mdmDirectory, "instrument");
			DirectoryClient.getInstance().cleanDirectory(instrumentDirectory);

			// Export Instrument Data
			final Map<String, Object> data = mdmService.buildUpInstrument(qmlFile.getAbsolutePath(), fieldScreenshots);
			if (data != null) {
				Map<String, Object> images = (Map<String, Object>) data.get("images");
				if (images != null) {
					final TextClient textClient = TextClient.getInstance();
					try {
						// final WriterDocument output =
						// textClient.createDocument("MasterTemplate.odt");
						final WriterDocument output = textClient.createDocument();
						output.setLandscape(false);

						TextTableOfContentElement toc = textClient.addTOC(output, "Seiten");
						textClient.addBreak(output);
						XmlObject doc = (XmlObject) data.get("qml");

						final PageType[] pages = ((QuestionnaireDocument) doc).getQuestionnaire().getPageArray();
						if (pages != null) {
							for (final PageType page : pages) {
								final String pageID = page.getUid();
								final Object imageObj = images.get(pageID);

								Map<String, Object> resolutionMap = null;
								if ((imageObj != null) && ((Map.class).isAssignableFrom(imageObj.getClass())))
									resolutionMap = (Map<String, Object>) images.get(pageID);
								if (resolutionMap == null)
									continue;
								if (resolutionMap.isEmpty())
									continue;

								for (final Map.Entry<String, Object> resolutionMapItem : resolutionMap.entrySet()) {
									final String resolution = resolutionMapItem.getKey();
									if ((Map.class).isAssignableFrom(resolutionMapItem.getValue().getClass())) {
										Map<String, Object> languageMap = (Map<String, Object>) resolutionMapItem
												.getValue();
										for (final Map.Entry<String, Object> languageMapItem : languageMap.entrySet()) {
											final String language = languageMapItem.getKey();
											final File imageFile = (File) languageMapItem.getValue();
											String ext = FilenameUtils.getExtension(imageFile.getAbsolutePath());
											if (!ext.equals("png"))
												continue;

											textClient.addBreak(output);
											textClient.addHeading(output,
													pageID + " (" + resolution + " , " + language + ") ");
											// textClient.addImage(output, imageFile, 1000, 700);
											textClient.addImage(output, imageFile, 1280, 720);
											// textClient.addImage(output, imageFile, 640, 480);
										}
									}
								}
							}
						}
						if (toc != null)
							textClient.updateTOC(output, toc);
						textClient.saveDocument(output, instrumentDirectory.getAbsolutePath() + "/instrument.odf");
						// textClient.exportDocumentAsPDF(output,
						// instrumentDirectory.getAbsolutePath()+"/instrument.pdf");
					} catch (final Exception e) {
						e.printStackTrace();
					}
				}
			}
		}

		// DirectoryClient.getInstance().deleteDir(fieldProjectDir.getParentFile(),
		// fieldProjectDir.getName());
		// System.exit(0);

		// Generate MDM Screenshots
		warFile = null;

		if ((!transFiles.isEmpty()) && (qmlFile != null)) {
			// Create Screenshots in MDM Mode (need to zip)
			// LOGGER.info("build MDM Project : " + survey);
			final File mdmProjectDir = builder.buildExistingProject(projectDir, dbLocation, dbPort, dbName,
					dbName + ".session", dbUser, dbPass, false, true, false, true, true, true, true,
					"forward,backward,same", "TOKEN", false, false, false, svnUrl, svnUser, svnPass, true, token);

			// forcing translation File generation
			try {
				mavenClient.doCleanInstall(mdmProjectDir);
			} catch (Exception e) {
				e.printStackTrace();
			}

			// fill up translations
			System.out.println("fill up Translation");
			File mdmProjectTransDir = new File(
					mdmProjectDir.getAbsolutePath() + "/src/main/resources/de/his/zofar/messages");

			final Map<String, Properties> transFilesFromMDM = new HashMap<String, Properties>();
			final List<File> transFilesFromMDMList = DirectoryClient.getInstance().readDir(mdmProjectTransDir);
			if (transFilesFromMDMList != null) {
				for (final File transFile : transFilesFromMDMList) {
					final String filename = transFile.getName();
					if (filename.matches("text_[a-z]+\\.properties")) {
						// // LOGGER.info("filename : " + filename);
						String location = filename.replaceAll("\\.properties", "");
						location = location.replaceAll("text_", "");
						transFilesFromMDM.put(location, ConfigurationUtils.getInstance()
								.getConfigurationFromFileSystem(transFile.getAbsolutePath()));
					}
				}
			}

			// reference translation
			final Properties refTrans = transFilesFromMDM.get("de");

			for (final Map.Entry<String, Properties> transFileItem : transFilesFromMDM.entrySet()) {
				final String language = transFileItem.getKey();
				if (language.equals("de"))
					continue;
				boolean dirty = false;

				final Properties langProps = transFileItem.getValue();

				for (final Object key : refTrans.keySet()) {
					if (langProps.containsKey(key))
						continue;

					String tmpKey = key + "";
					String pageId = tmpKey.substring(0, tmpKey.indexOf("."));

					if (pageId.indexOf("_") != -1) {
						pageId = pageId.substring(0, pageId.indexOf("_"));
					}
					String pathTmpKey = tmpKey.substring(tmpKey.indexOf(".") + 1);
					String pathEnd = pathTmpKey;
					if (pathEnd.indexOf("body.") != -1)
						pathEnd = pathEnd.substring(pathEnd.indexOf("body.") + 5);
					// System.out.println(pageId+".*."+pathEnd);
					final List<String> found = new ArrayList<String>();

					for (final Object langKeyObj : langProps.keySet()) {
						String langKey = langKeyObj + "";
						if ((langKey.startsWith(pageId + ".")) && (langKey.endsWith("." + pathEnd)))
							found.add(langKey);
					}

					if (found.size() == 1) {
						String origKey = found.get(0);
						final String origValue = langProps.getProperty(origKey);

						if (origValue == null)
							System.out.println("No Value found for Original : " + origKey + " = " + origValue);

						langProps.setProperty(tmpKey, origValue);
						dirty = true;

					} else {
						System.err.println("found nothing or more than one for " + tmpKey + " ==> " + found);
					}
				}

				if (dirty) {
					// backup original and set modified as lang file
					File original = new File(mdmProjectTransDir, "text_" + language + ".properties");
					File backup = new File(mdmProjectTransDir, "text_" + language + ".properties.backup");
					File clone = new File(mdmProjectTransDir, "text_" + language + ".properties");

					FileClient.getInstance().copyFile(original, backup);
					ConfigurationUtils.getInstance().saveConfiguration(langProps, clone.getAbsolutePath());
				}
			}

			System.out.println("done");

			try {
				warFile = mavenClient.doCleanInstall(mdmProjectDir);
			} catch (Exception e) {
				e.printStackTrace();
			}

			// Generate MDM Screenshots
			if ((warFile != null) && (warFile.exists())) {
				mdmScreenshots = this.processingScreenshotting(warFile, parameter, dimensions, transFiles, ScreenshotMode.MDM);
			}
			DirectoryClient.getInstance().deleteDir(mdmProjectDir.getParentFile(), mdmProjectDir.getName());
		}

		//mdmScreenshots = new File("xxxx/MDM_Projects/debug3/screenshots.zip");

		// Generate JSON Files
		if ((!transFiles.isEmpty()) && (qmlFile != null) && (mdmScreenshots != null)) {
			// final FileClient fileClient = FileClient.getInstance();
			// final File mdmDirectory = DirectoryClient.getInstance().createDir(workDir,
			// "MDM");

			final File jsonDirectory = DirectoryClient.getInstance().createDir(mdmDirectory, "json");
			DirectoryClient.getInstance().cleanDirectory(jsonDirectory);

			final File variableDirectory = DirectoryClient.getInstance().createDir(jsonDirectory, "variables");
			DirectoryClient.getInstance().cleanDirectory(variableDirectory);

			final File questionDirectory = DirectoryClient.getInstance().createDir(jsonDirectory, "questions");
			DirectoryClient.getInstance().cleanDirectory(questionDirectory);

			final File questionInstrumentDirectory = DirectoryClient.getInstance().createDir(questionDirectory,
					"ins" + instrumentId);
			DirectoryClient.getInstance().cleanDirectory(questionInstrumentDirectory);

			final File pageDirectory = DirectoryClient.getInstance().createDir(jsonDirectory, "pages");
			DirectoryClient.getInstance().cleanDirectory(pageDirectory);
			
			final File pageInstrumentDirectory = DirectoryClient.getInstance().createDir(pageDirectory,
					"ins" + instrumentId);
			DirectoryClient.getInstance().cleanDirectory(pageInstrumentDirectory);

			final File questionImagesDirectory = DirectoryClient.getInstance().createDir(questionInstrumentDirectory,
					"images");
			DirectoryClient.getInstance().cleanDirectory(questionImagesDirectory);

			final File pageImagesDirectory = DirectoryClient.getInstance().createDir(pageInstrumentDirectory, "images");
			DirectoryClient.getInstance().cleanDirectory(pageImagesDirectory);

			// Export MDM Data
			final Map<String, Object> data = mdmService.buildUpQuestion(qmlFile.getAbsolutePath(), transFiles,
					mdmScreenshots, dataAcquestionProject, surveyId, instrumentId, datasetId);

			if (data != null) {
				@SuppressWarnings("unchecked")
				Map<String, Map<String, Object>> variables = (Map<String, Map<String, Object>>) data.get("variable");
				String variablesOutput = (String) FormatFactory.getInstance().getFormat(FormatFactory.FORMAT.csv)
						.formatCSV(variables);
				if (variablesOutput != null) {
					// System.out.println("Variable Output : "+variablesOutput);
					final File variable_csv_File = fileClient.createTextFile("Variables.csv", variablesOutput,
							variableDirectory);
				}

				@SuppressWarnings("unchecked")
				Map<String, JsonObject> questions = (Map<String, JsonObject>) data.get("question");
				@SuppressWarnings("unchecked")
				Map<String, String> questionsOutput = (Map<String, String>) FormatFactory.getInstance()
						.getFormat(FormatFactory.FORMAT.json).formatJSON(questions);
				if (questionsOutput != null) {
					for (Map.Entry<String, String> item : questionsOutput.entrySet()) {
						final File question_json_File = fileClient.createTextFile(item.getKey() + ".json",
								item.getValue(), questionInstrumentDirectory);
					}
				}

				@SuppressWarnings("unchecked")
				Map<String, JsonObject> pages = (Map<String, JsonObject>) data.get("pages");
				@SuppressWarnings("unchecked")
				Map<String, String> pagesOutput = (Map<String, String>) FormatFactory.getInstance()
						.getFormat(FormatFactory.FORMAT.json).formatJSON(pages);
				if (pagesOutput != null) {
					for (Map.Entry<String, String> item : pagesOutput.entrySet()) {
						final File page_json_File = fileClient.createTextFile(item.getKey() + ".json", item.getValue(),
								pageInstrumentDirectory);
					}
				}

				@SuppressWarnings("unchecked")
				Map<String, Object> images = (Map<String, Object>) data.get("images");
				if (images != null) {
					for (Map.Entry<String, Object> itemSet : images.entrySet()) {
						System.out.println("image : " + itemSet.getKey());

						final File folder = DirectoryClient.getInstance().createDir(questionImagesDirectory,
								itemSet.getKey());
						DirectoryClient.getInstance().cleanDirectory(folder);
						final Map<String, Object> content = (Map<String, Object>) itemSet.getValue();
						for (Map.Entry<String, Object> contentItem : content.entrySet()) {
							File tmpFile = null;
							JsonObject tmpMeta = null;
							if ((File.class).isAssignableFrom(contentItem.getValue().getClass())) {
								tmpFile = (File) contentItem.getValue();

							} else if ((Set.class).isAssignableFrom(contentItem.getValue().getClass())) {
								final Set<?> tmp = (Set<?>) contentItem.getValue();
								for (final Object setItem : tmp) {
									if ((File.class).isAssignableFrom(setItem.getClass()))
										tmpFile = (File) setItem;
									else if ((JsonObject.class).isAssignableFrom(setItem.getClass()))
										tmpMeta = (JsonObject) setItem;
								}
							}

							if (tmpFile != null) {
								final File dest = fileClient.createOrGetFile(contentItem.getKey(),
										"." + fileClient.getSuffix(tmpFile), folder);
								fileClient.copyFile(tmpFile, dest);
							}
							if (tmpMeta != null) {
								final Map<String, JsonObject> metas = new LinkedHashMap<String, JsonObject>();
								metas.put(contentItem.getKey(), tmpMeta);
								Map<String, String> imageMetaOutput = (Map<String, String>) FormatFactory.getInstance()
										.getFormat(FormatFactory.FORMAT.json).formatJSON(metas);
								for (Map.Entry<String, String> metaItem : imageMetaOutput.entrySet()) {
									final File question_json_File = fileClient
											.createTextFile(metaItem.getKey() + ".json", metaItem.getValue(), folder);
								}
							}
						}
					}
				}

				@SuppressWarnings("unchecked")
				Map<String, Object> pageImages = (Map<String, Object>) data.get("pageImages");
				if (pageImages != null) {
					for (Map.Entry<String, Object> itemSet : pageImages.entrySet()) {
						System.out.println("page image : " + itemSet.getKey());

						final File folder = DirectoryClient.getInstance().createDir(pageImagesDirectory,
								itemSet.getKey());
						DirectoryClient.getInstance().cleanDirectory(folder);
						final Map<String, Object> content = (Map<String, Object>) itemSet.getValue();
						for (Map.Entry<String, Object> contentItem : content.entrySet()) {
							File tmpFile = null;
							JsonObject tmpMeta = null;
							if ((File.class).isAssignableFrom(contentItem.getValue().getClass())) {
								tmpFile = (File) contentItem.getValue();

							} else if ((Set.class).isAssignableFrom(contentItem.getValue().getClass())) {
								final Set<?> tmp = (Set<?>) contentItem.getValue();
								for (final Object setItem : tmp) {
									if ((File.class).isAssignableFrom(setItem.getClass()))
										tmpFile = (File) setItem;
									else if ((JsonObject.class).isAssignableFrom(setItem.getClass()))
										tmpMeta = (JsonObject) setItem;
								}
							}

							if (tmpFile != null) {
								final File dest = fileClient.createOrGetFile(contentItem.getKey(),
										"." + fileClient.getSuffix(tmpFile), folder);
								fileClient.copyFile(tmpFile, dest);
							}
							if (tmpMeta != null) {
								final Map<String, JsonObject> metas = new LinkedHashMap<String, JsonObject>();
								metas.put(contentItem.getKey(), tmpMeta);
								Map<String, String> imageMetaOutput = (Map<String, String>) FormatFactory.getInstance()
										.getFormat(FormatFactory.FORMAT.json).formatJSON(metas);
								for (Map.Entry<String, String> metaItem : imageMetaOutput.entrySet()) {
									final File question_json_File = fileClient
											.createTextFile(metaItem.getKey() + ".json", metaItem.getValue(), folder);
								}
							}
						}
					}
				}
			}
		}
	}



	public enum ScreenshotMode {
		MDM, FIELD
	}
	

	private File processingScreenshotting(final File warFile, ParameterMap<ABSTRACTPARAMETER, Object> parameter,
			final ArrayList<java.awt.Dimension> dimensions, final Map<String, Properties> transFiles,
			final ScreenshotMode mode) throws Exception {

		final String survey = (String) parameter.get(Parameter.survey);

		final File workDir = (File) parameter.get(Parameter.workDir);

		final String serverUrl = (String) parameter.get(Parameter.serverUrl);

		final String serverUser = (String) parameter.get(Parameter.serverUser);
		final String serverPass = (String) parameter.get(Parameter.serverPass);

		final String dbLocation = (String) parameter.get(Parameter.dbLocation);
		final String dbPort = (String) parameter.get(Parameter.dbPort);
		final String dbUser = (String) parameter.get(Parameter.dbUser);
		final String dbPass = (String) parameter.get(Parameter.dbPass);

		final Map<String, String> app_logins = (Map<String, String>) parameter.get(Parameter.app_logins);

		String token = eu.dzhw.zofar.management.utils.string.StringUtils.getInstance().randomString(5);

		final String dbName = ("mdm_" + survey).toLowerCase().replace('.', '_').replace('-', '_');

		File screenshotFile = null;

		if ((warFile != null) && (warFile.exists())) {
			final String surveyName = warFile.getName().replaceAll(Pattern.quote(".war"), "");
			// // // set up db
			final Connection dbConn = postgresClient.getMaintenanceConnection(dbLocation, dbPort, dbUser, dbPass);
			if (dbConn != null) {
				if (postgresClient.existDB(dbConn, dbName)) {
					postgresClient.disconnectOtherFromDB(dbConn, dbName);
					postgresClient.dropDB(dbConn, dbName);
				}
				postgresClient.createDB(dbConn, dbName);
			}

			if (tomcatManager.isInstalled("/" + surveyName, serverUrl, app_logins)) {
				LOGGER.info("Application already installed on Tomcat");
				tomcatManager.undeploy(serverUrl, app_logins, "/" + surveyName);
			}
			if (tomcatManager.deploy(serverUrl, serverUser, serverPass, app_logins, surveyName, warFile)) {
				LOGGER.info("" + survey + " deployed ");

				// insert Preloads
				final StringBuffer batch = new StringBuffer();
				batch.append(
						"INSERT INTO participant(id, version, password, token) VALUES (nextval('seq_participant_id'),0,'"
								+ TextCipherClient.getInstance().encodeSHA(token) + "','" + token + "');\n");

				final Connection preloadConn = postgresClient.getConnection(dbLocation, dbPort, dbName, dbUser, dbPass);
				postgresClient.executeDb(preloadConn, batch.toString());
				if (preloadConn != null)
					postgresClient.close(preloadConn);

				// run screenshot bot
				final File surveyScreenshotCacheDir = (File) parameter.get(Parameter.surveyScreenshotCacheDir);
				DirectoryClient.getInstance().cleanDirectory(surveyScreenshotCacheDir);

				if (mode.equals(ScreenshotMode.FIELD))
					this.takeScreenshotsForField(serverUrl, surveyName, token, surveyScreenshotCacheDir, dimensions,
							new ArrayList<String>(transFiles.keySet()));
				if (mode.equals(ScreenshotMode.MDM))
					this.takeScreenshotsForMDM(serverUrl, surveyName, token, surveyScreenshotCacheDir, dimensions,
							new ArrayList<String>(transFiles.keySet()));

				final Map<String, Object> packageObj = new HashMap<String, Object>();
				final List<File> screenshots = DirectoryClient.getInstance().readDir(surveyScreenshotCacheDir);
				final Map<String, File> screenshotMap = new LinkedHashMap<String, File>();
				if (screenshots != null) {
					for (final File screenshot : screenshots)
						screenshotMap.put(screenshot.getName(), screenshot);
				}
				packageObj.put("screenshots", screenshotMap);
				screenshotFile = FileClient.getInstance().createOrGetFile("screenshots", ".zip", workDir);
				PackagerClient.getInstance().packageZip(screenshotFile, packageObj);
				if (tomcatManager.stop(serverUrl, app_logins, surveyName)) {
					LOGGER.info("" + survey + " stopped ");
					if (tomcatManager.undeploy(serverUrl, app_logins, surveyName)) {
						LOGGER.info("" + survey + " undeployed ");
					} else {
						LOGGER.info("" + survey + " not undeployed ");
					}
				} else {
					LOGGER.info("" + survey + " not stopped ");
				}
			} else {
				LOGGER.info("" + survey + " not deployed ");
			}
			// drop db
			if (dbConn != null)
				postgresClient.dropDB(dbConn, dbName);
			postgresClient.close(dbConn);
		}
		return screenshotFile;
	}

	private void takeScreenshotsForMDM(final String serverUrl, final String surveyName, final String token,
			final File screenshotDir, final ArrayList<java.awt.Dimension> dimensions, final List<String> languages) {
		ScreenshotGenerator generator = ScreenshotGenerator.getInstance();
		try {
			final String query = serverUrl + "/" + surveyName + "/special/login.html?zofar_token=" + token;
			System.out.println("query : " + query);
			DirectoryClient directoryClient = DirectoryClient.getInstance();

			File surveyScreenshotDir = directoryClient.createDir(screenshotDir, surveyName);
			directoryClient.cleanDirectory(surveyScreenshotDir);

			generator.spiderSurveyForMDM(query, screenshotDir, dimensions, false, languages);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void takeScreenshotsForField(final String serverUrl, final String surveyName, final String token,
			final File screenshotDir, final ArrayList<java.awt.Dimension> dimensions, final List<String> languages) {
		ScreenshotGenerator generator = ScreenshotGenerator.getInstance();
		try {
			final String query = serverUrl + "/" + surveyName + "/special/login.html?zofar_token=" + token;
			System.out.println("query : " + query);
			DirectoryClient directoryClient = DirectoryClient.getInstance();

			File surveyScreenshotDir = directoryClient.createDir(screenshotDir, surveyName);
			directoryClient.cleanDirectory(surveyScreenshotDir);

			generator.spiderSurvey(query, screenshotDir, dimensions, false, languages);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

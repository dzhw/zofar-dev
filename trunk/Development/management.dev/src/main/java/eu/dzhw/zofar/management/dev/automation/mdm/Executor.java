package eu.dzhw.zofar.management.dev.automation.mdm;

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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.dzhw.zofar.management.dev.automation.AbstractExecutor;
import eu.dzhw.zofar.management.dev.automation.mdm.presentation.FormatFactory;
import eu.dzhw.zofar.management.dev.automation.mdm.service.MDMService;
import eu.dzhw.zofar.management.dev.automation.screenshot.ScreenshotGenerator;
import eu.dzhw.zofar.management.security.text.TextCipherClient;
import eu.dzhw.zofar.management.utils.files.DirectoryClient;
import eu.dzhw.zofar.management.utils.files.FileClient;
import eu.dzhw.zofar.management.utils.files.PackagerClient;
import eu.dzhw.zofar.management.utils.system.ConfigurationUtils;


public class Executor extends AbstractExecutor {

	private static final long serialVersionUID = 1L;
	private static final Executor INSTANCE = new Executor();
	private static final Logger LOGGER = LoggerFactory.getLogger(Executor.class);

	public enum Parameter implements ABSTRACTPARAMETER {
		survey, dataAcquestionProject, surveyId, instrumentId, datasetId, workDir, surveyScreenshotCacheDir, surveyExportDir, svnUrl, svnUser, svnPass, serverUrl, serverUser, serverPass, dbLocation, dbUser, dbPass, dbPort;
	};

	private Executor() {
		super();
	}

	public static Executor getInstance() {
		return INSTANCE;
	}

	public ParameterMap<ABSTRACTPARAMETER, Object> getParameterMap(final String survey,
			final String dataAcquestionProject, final String surveyId, final String instrumentId,
			final String datasetId, final File workDir, final File surveyScreenshotCacheDir, final File surveyExportDir,
			final String svnUrl, final String svnUser, final String svnPass, final String serverUrl,
			final String serverUser, final String serverPass, final String dbLocation, final String dbPort,
			final String dbUser, final String dbPass) {
		final ParameterMap<ABSTRACTPARAMETER, Object> back = new ParameterMap<ABSTRACTPARAMETER, Object>();
		back.put(Parameter.survey, survey);
		back.put(Parameter.dataAcquestionProject, survey);
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

		return back;
	}

	public void process(ParameterMap<ABSTRACTPARAMETER, Object> parameter) throws Exception {
		this.process(parameter, null);
	}
	public void process(ParameterMap<ABSTRACTPARAMETER, Object> parameter,
			final ArrayList<java.awt.Dimension> dimensions) throws Exception {
		final String survey = (String) parameter.get(Parameter.survey);
		final String dataAcquestionProject = (String) parameter.get(Parameter.dataAcquestionProject);
		final String surveyId = (String) parameter.get(Parameter.surveyId);
		final String instrumentId = (String) parameter.get(Parameter.instrumentId);
		final String datasetId = (String) parameter.get(Parameter.datasetId);
		final File workDir = (File) parameter.get(Parameter.workDir);
		final File surveyScreenshotCacheDir = (File) parameter.get(Parameter.surveyScreenshotCacheDir);
		final File surveyExportDir = (File) parameter.get(Parameter.surveyExportDir);
		final String svnUrl = (String) parameter.get(Parameter.svnUrl);
		final String svnUser = (String) parameter.get(Parameter.svnUser);
		final String svnPass = (String) parameter.get(Parameter.svnPass);
		final String serverUrl = (String) parameter.get(Parameter.serverUrl);

		final String serverUser = (String) parameter.get(Parameter.serverUser);
		final String serverPass = (String) parameter.get(Parameter.serverPass);

		final String dbLocation = (String) parameter.get(Parameter.dbLocation);
		final String dbPort = (String) parameter.get(Parameter.dbPort);
		final String dbUser = (String) parameter.get(Parameter.dbUser);
		final String dbPass = (String) parameter.get(Parameter.dbPass);

		Map<String, String> app_logins = new LinkedHashMap<String, String>();
		app_logins.put("admin", "admin");

		// File transFile = null;
		Map<String, Properties> transFiles = new HashMap<String, Properties>();
		File qmlFile = null;
		File screenshotFile = null;
		File preloadSQL = null;
		final String token = eu.dzhw.zofar.management.utils.string.StringUtils.getInstance().randomString(5)
				+ System.currentTimeMillis();
		final File projectDir = builder.checkoutProject(survey, workDir, svnUrl, svnUser, svnPass);
		if ((projectDir != null) && (projectDir.exists())) {
			LOGGER.info("build standard Project : " + survey);
			final String dbName = ("mdm_" + survey).toLowerCase().replace('.', '_').replace('-', '_');

			// Build Project for generating Translation
			final File translationProjectDir = builder.buildExistingProject(projectDir, dbLocation, dbPort, dbName,
					dbName + ".session", dbUser, dbPass, false, true, false, true, true, false, true,
					"forward,backward,same", "TOKEN", true, false, false, svnUrl, svnUser, svnPass, true, "dummy");

			File warFile = null;
			try {
				warFile = mavenClient.doCleanInstall(translationProjectDir);
			} catch (Exception e) {
				e.printStackTrace();
			}
			if ((warFile != null) && (warFile.exists())) {
				// Extract Translation and QML from war
				final List<File> unpackedWar = PackagerClient.getInstance().extractZip(warFile);
				if (unpackedWar != null) {
					for (final File file : unpackedWar) {
						final String filename = file.getName();
						// final String filePath = file.getPath();
						// System.out.println("Path : "+filePath+" File : "+filename);

						if (filename.equals("questionnaire.xml"))
							qmlFile = file;

						if (filename.equals("preloads_postgres.sql"))
							preloadSQL = file;

						final List<String> tmp = new ArrayList<String>();
						tmp.add(filename);
						// if(ReplaceClient.getInstance().contains(tmp, "text_..\\.properties")) {
						if (filename.matches("text_[a-z]+\\.properties")) {
							LOGGER.info("filename : " + filename);
							String location = filename.replaceAll("\\.properties", "");
							location = location.replaceAll("text_", "");
							transFiles.put(location, ConfigurationUtils.getInstance()
									.getConfigurationFromFileSystem(file.getAbsolutePath()));
						}
					}

					if (qmlFile != null) {
						qmlFile = FileClient.getInstance().copyToDir(qmlFile, workDir);
					}

					if (preloadSQL != null) {
						preloadSQL = FileClient.getInstance().copyToDir(preloadSQL, workDir);
					}
				}
			}
			DirectoryClient.getInstance().deleteDir(translationProjectDir.getParentFile(),
					translationProjectDir.getName());
			if ((!transFiles.isEmpty()) && (qmlFile != null)) {
				// Create Screenshots in MDM Mode (need to zip)

				LOGGER.info("build MDM Project : " + survey);
				final File mdmProjectDir = builder.buildExistingProject(projectDir, dbLocation, dbPort, dbName,
						dbName + ".session", dbUser, dbPass, false, true, false, true, true, true, true,
						"forward,backward,same", "TOKEN", false, false, false, svnUrl, svnUser, svnPass, true, token);
				warFile = null;
				try {
					warFile = mavenClient.doCleanInstall(mdmProjectDir);
				} catch (Exception e) {
					e.printStackTrace();
				}
				if ((warFile != null) && (warFile.exists())) {
					final String surveyName = warFile.getName().replaceAll(Pattern.quote(".war"), "");
					// set up db
					final Connection dbConn = postgresClient.getMaintenanceConnection(dbLocation, dbPort, dbUser,
							dbPass);
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

						final Connection preloadConn = postgresClient.getConnection(dbLocation, dbPort, dbName, dbUser,
								dbPass);
						postgresClient.executeDb(preloadConn, batch.toString());
						if (preloadConn != null)
							postgresClient.close(preloadConn);

						// run screenshot bot
						DirectoryClient.getInstance().cleanDirectory(surveyScreenshotCacheDir);
						takeScreenshots(serverUrl, surveyName, token, surveyScreenshotCacheDir, dimensions,
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
				DirectoryClient.getInstance().deleteDir(mdmProjectDir.getParentFile(), mdmProjectDir.getName());
			}
		}
		DirectoryClient.getInstance().deleteDir(projectDir.getParentFile(), projectDir.getName());

		if ((screenshotFile != null) && (screenshotFile.exists()) && (!transFiles.isEmpty()) && (qmlFile != null)
				&& (qmlFile.exists())) {
			final FileClient fileClient = FileClient.getInstance();
			final File mdmDirectory = DirectoryClient.getInstance().createDir(workDir, "MDM");

			final File jsonDirectory = DirectoryClient.getInstance().createDir(mdmDirectory, "json");
			DirectoryClient.getInstance().cleanDirectory(jsonDirectory);

			final File variableDirectory = DirectoryClient.getInstance().createDir(jsonDirectory, "variables");
			DirectoryClient.getInstance().cleanDirectory(variableDirectory);

			final File questionDirectory = DirectoryClient.getInstance().createDir(jsonDirectory, "questions");
			DirectoryClient.getInstance().cleanDirectory(questionDirectory);

			final File imagesDirectory = DirectoryClient.getInstance().createDir(questionDirectory, "images");
			DirectoryClient.getInstance().cleanDirectory(imagesDirectory);

			// Export MDM Data
			MDMService mdmService = MDMService.getInstance();
			final Map<String, Object> data = mdmService.buildUpQuestion(qmlFile.getAbsolutePath(), transFiles, 
					screenshotFile, dataAcquestionProject, surveyId, instrumentId, datasetId);
			// final Map<String, Object> data = null;
			
			System.exit(1);
			
			
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
				for (Map.Entry<String, String> item : questionsOutput.entrySet()) {
					// System.out.println("Question Output : "+item.getKey() + "
					// => " + item.getValue());
					final File question_json_File = fileClient.createTextFile(item.getKey() + ".json", item.getValue(),
							questionDirectory);
				}

				// @SuppressWarnings("unchecked")
				// Map<String, File> images = (Map<String, File>) data.get("images");
				// if (images != null) {
				// for (Map.Entry<String, File> item : images.entrySet()) {
				// if (item.getValue() != null) {
				// final File dest = fileClient.createOrGetFile(item.getKey(),
				// "." + fileClient.getSuffix(item.getValue()), imagesDirectory);
				// fileClient.copyFile(item.getValue(), dest);
				// }
				// }
				// }

				@SuppressWarnings("unchecked")
				Map<String, Object> images = (Map<String, Object>) data.get("images");
				if (images != null) {
					for (Map.Entry<String, Object> itemSet : images.entrySet()) {
						final File folder = DirectoryClient.getInstance().createDir(imagesDirectory, itemSet.getKey());
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
					//
					// for (Map.Entry<String, Object> item : images.entrySet()) {
					// if (item.getValue() != null) {
					// File tmpFile = null;
					// JsonObject tmpMeta = null;
					// if ((File.class).isAssignableFrom(item.getValue().getClass())) {
					// tmpFile = (File) item.getValue();
					//
					// } else if ((Set.class).isAssignableFrom(item.getValue().getClass())) {
					// final Set<?> tmp = (Set<?>) item.getValue();
					// for (final Object setItem : tmp) {
					// if ((File.class).isAssignableFrom(setItem.getClass()))
					// tmpFile = (File) setItem;
					// else if ((JsonObject.class).isAssignableFrom(setItem.getClass()))
					// tmpMeta = (JsonObject) setItem;
					// }
					// }
					//
					// if (tmpFile != null) {
					// final File dest = fileClient.createOrGetFile(item.getKey(),
					// "." + fileClient.getSuffix(tmpFile), imagesDirectory);
					// fileClient.copyFile(tmpFile, dest);
					// }
					// if(tmpMeta != null) {
					// metas.put(item.getKey(), tmpMeta);
					// }
					//
					// }
					// }
					//
					// Map<String, String> metaObjects = (Map<String, String>)
					// FormatFactory.getInstance()
					// .getFormat(FormatFactory.FORMAT.json).formatJSON(metas);
					// for (Map.Entry<String, String> item : metaObjects.entrySet()) {
					// final File image_json_File = fileClient.createTextFile(item.getKey() +
					// ".json", item.getValue(),
					// imagesDirectory);
					// }
				}

				final File packageFile = fileClient.createOrGetFile("mdm_" + survey, ".zip", mdmDirectory);
				Map<String, Object> packageObj = new HashMap<String, Object>();
				packageObj.put(variableDirectory.getName(), variableDirectory);
				packageObj.put(questionDirectory.getName(), questionDirectory);
				PackagerClient.getInstance().packageZip(packageFile, packageObj);

				if ((surveyExportDir != null) && (surveyExportDir.exists())) {

					if (!transFiles.isEmpty()) {
						for (final Map.Entry<String, Properties> props : transFiles.entrySet()) {
							ConfigurationUtils.getInstance().saveConfiguration(props.getValue(),
									surveyExportDir.getAbsolutePath() + File.separator + "text_" + props.getKey()
											+ ".properties");
						}
					}

					if (qmlFile != null)
						FileClient.getInstance().copyToDir(qmlFile, surveyExportDir);
					if (screenshotFile != null)
						FileClient.getInstance().copyToDir(screenshotFile, surveyExportDir);
					if (packageFile != null)
						FileClient.getInstance().copyToDir(packageFile, surveyExportDir);
				}
			}
		}
	}



	private void takeScreenshots(final String serverUrl, final String surveyName, final String token,
			final File screenshotDir, final ArrayList<java.awt.Dimension> dimensions, final List<String> languages) {
		ScreenshotGenerator generator = ScreenshotGenerator.getInstance();
		try {
			final String query = serverUrl + "/" + surveyName + "/special/login.html?zofar_token=" + token;
			DirectoryClient directoryClient = DirectoryClient.getInstance();

			File surveyScreenshotDir = directoryClient.createDir(screenshotDir, surveyName);
			directoryClient.cleanDirectory(surveyScreenshotDir);

			generator.spiderSurveyForMDM(query, screenshotDir, dimensions, false, languages);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

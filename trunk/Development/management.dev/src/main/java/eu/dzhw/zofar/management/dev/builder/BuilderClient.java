package eu.dzhw.zofar.management.dev.builder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.io.Serializable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Pattern;

import org.apache.maven.model.Build;
import org.apache.maven.model.Model;
import org.apache.maven.model.Plugin;
import org.apache.maven.model.PluginExecution;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.apache.maven.model.io.xpp3.MavenXpp3Writer;
import org.apache.maven.shared.invoker.MavenInvocationException;
import org.apache.xmlbeans.XmlDocumentProperties;
import org.apache.xmlbeans.XmlObject;
import org.codehaus.plexus.util.xml.Xpp3Dom;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tmatesoft.svn.core.SVNException;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

import eu.dzhw.zofar.management.comm.network.http.HTTPClient;
import eu.dzhw.zofar.management.comm.svn.SVNClient;
import eu.dzhw.zofar.management.dev.maven.MavenClient;
import eu.dzhw.zofar.management.utils.files.FileClient;
import eu.dzhw.zofar.management.utils.system.ConfigurationUtils;
import eu.dzhw.zofar.management.utils.xml.XmlClient;

public class BuilderClient implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 70472259058126660L;

	private static final BuilderClient INSTANCE = new BuilderClient();

	private static final Logger LOGGER = LoggerFactory.getLogger(BuilderClient.class);

	private BuilderClient() {
		super();
	}

	public static BuilderClient getInstance() {
		return INSTANCE;
	}

	public File buildExistingProject(final File projectDir, final String dbLocation, final String dbPort,
			final String dbname, final String sessionDbname, final String dbUser, final String dbPass,
			final boolean overrideNav, final boolean cutHistory, final boolean pretest, final boolean linearNavigation,
			final boolean overrideRendering, final boolean mdm, final boolean noVisibleMap, final String saveMode,
			final String login, final boolean preloadOnStart, final boolean record, final boolean cluster,
			final String svnUrl, final String svnUser, final String svnPass, final boolean overrideCommit,
			final String token) throws Exception {
		return this.buildExistingProject(projectDir.getName(), projectDir, dbLocation, dbPort, dbname, sessionDbname,
				dbUser, dbPass, overrideNav, cutHistory, pretest, linearNavigation, overrideRendering, mdm,
				noVisibleMap, saveMode, login, preloadOnStart, record, cluster, svnUrl, svnUser, svnPass,
				overrideCommit, token);
	}

	public File buildExistingProject(final String finalName, final File projectDir, final String dbLocation,
			final String dbPort, final String dbname, final String sessionDbname, final String dbUser,
			final String dbPass, final boolean overrideNav, final boolean cutHistory, final boolean pretest,
			final boolean linearNavigation, final boolean overrideRendering, final boolean mdm,
			final boolean noVisibleMap, final String saveMode, final String login, final boolean preloadOnStart,
			final boolean record, final boolean cluster, final String svnUrl, final String svnUser,
			final String svnPass, final boolean overrideCommit, final String token) throws Exception {

		final File qmlFile = new File(projectDir.getAbsolutePath() + "/src/main/resources/questionnaire.xml");
		this.modifyQML(qmlFile, finalName, svnUser, svnPass, overrideCommit, token);

		final File dbFile = new File(projectDir.getAbsolutePath() + "/src/main/resources/survey/database.properties");
		this.modifyDB(dbFile, dbLocation, dbPort, dbname, dbUser, dbPass, svnUrl, svnUser, svnPass, overrideCommit);

		final File jsfTemplateFile = new File(projectDir.getAbsolutePath() + "/src/main/webapp/template/survey.xhtml");
		this.modifyJSFTemplate(jsfTemplateFile, mdm, svnUser, svnPass, overrideCommit);

		File securityFile = new File(projectDir.getAbsolutePath() + "/src/main/resources/session-security.cfg.xml");
		if ((securityFile == null) || (!securityFile.exists())) {
			final FileClient file = FileClient.getInstance();
			final File templateFile = file.getResource("builder/session-securityTemplate.txt");
			if ((templateFile != null) && (templateFile.exists())) {
				final String content = file.readAsString(templateFile);
				securityFile = file.createOrGetFile("session-security", ".cfg.xml",
						new File(projectDir.getAbsolutePath() + "/src/main/resources"));
				file.writeToFile(securityFile, content, false);
			}
		}
		this.modifySessionSecurity(securityFile, dbLocation, dbPort,sessionDbname, dbUser, dbPass, svnUrl, svnUser, svnPass,
				overrideCommit);

		final File systemFile = new File(projectDir.getAbsolutePath() + "/src/main/resources/system.properties");
		this.modifySystem(systemFile, overrideNav, cutHistory, saveMode, login, preloadOnStart, record, cluster,
				svnUser, svnPass, overrideCommit);

		this.modifyPom(finalName, this.getPom(projectDir), pretest, linearNavigation, overrideRendering, mdm,
				noVisibleMap, svnUrl, svnUser, svnPass);

		return projectDir;
	}

	public File createProject(final String name, final File baseDir, final String svnUrl, final String svnUser,
			final String svnPass) throws Exception {
		final MavenClient mavenClient = MavenClient.getInstance();
		final File tmpProjectDir = mavenClient.createProjectFromArchetype("eu.dzhw.zofar", name, "de.his.zofar",
				"zofar.survey.archetype.surveytemplate_responsive", "0.0.1-SNAPSHOT", baseDir);
		try {

			this.modifyPom(name, this.getPom(tmpProjectDir), false, false, false, false, false, svnUrl, svnUser,
					svnPass);

			mavenClient.doCleanInstall(tmpProjectDir);
			return tmpProjectDir;
		} catch (final MavenInvocationException e) {
			throw new Exception(e);
		}
	}

	public File getProject(final String name, final File baseDir) {
		return new File(baseDir, name);
	}

	public File checkoutProject(final String name, final File baseDir, final String svnUrl, final String svnUser,
			final String svnPass) throws Exception {
		final SVNClient svn = SVNClient.getInstance();
		try {
			final File projectDir = new File(baseDir, name);
			svn.doCheckout(svnUrl, svnUser, svnPass, name, projectDir);
			return projectDir;
		} catch (final SVNException e) {
			throw new Exception(e);
		}
	}

	public void commitProject(final String name, final File projectDir, final String svnUrl, final String svnUser,
			final String svnPass) throws Exception {
		final SVNClient svn = SVNClient.getInstance();
		try {
			svn.createProject(svnUrl, svnUser, svnPass, name, projectDir);
		} catch (final SVNException e) {
			throw new Exception(e);
		}
	}

	public void deleteProject(final String svnUrl, final String svnUser, final String svnPass, final String name)
			throws Exception {
		final SVNClient svn = SVNClient.getInstance();
		try {
			svn.deleteProject(svnUrl, svnUser, svnPass, name);
		} catch (final SVNException e) {
			throw new Exception(e);
		}
	}

	public List<String> listProjects(final String svnUrl, final String svnUser, final String svnPass) throws Exception {
		final SVNClient svn = SVNClient.getInstance();
		try {
			return svn.getProjects(svnUrl, svnUser, svnPass, "");
		} catch (final SVNException e) {
			throw new Exception(e);
		} catch (final IOException e) {
			throw new Exception(e);
		}
	}

	private void modifyQML(final File qmlFile, final String name, final String svnUser, final String svnPass,
			final boolean overrideCommit, final String token) throws Exception {
		final XmlClient qml = XmlClient.getInstance();
		final SVNClient svn = SVNClient.getInstance();
		final Document qmlDoc = qml.getDocument(qmlFile.getAbsolutePath());
		final NodeList nodes = qml.getByXPath(qmlDoc, "//*");
		final StringBuffer comments = new StringBuffer();

		if (nodes != null) {
			try {
				final int count = nodes.getLength();
				for (int a = 0; a < count; a++) {
					final Node node = nodes.item(a);
					final String nodeName = node.getNodeName();

					// Change name of Survey
					if (nodeName.equals("zofar:name")) {
						final String oldName = node.getTextContent();
						node.setTextContent(name);
						comments.append("Name changed from " + oldName + " to " + name + ", ");
					} else if (nodeName.equals("zofar:description")) {
						final String oldDescription = node.getTextContent();
						node.setTextContent(name);
						comments.append("Description changed from " + oldDescription + " to " + name + ", ");
					}
					// add Screenshot Token
					else if (nodeName.equals("zofar:preloads")) {
						boolean tokenExist = false;

						// clean old Tokens
						// for (Node child; (child = node.getFirstChild()) !=
						// null; node.removeChild(child));

						final NodeList tokens = node.getChildNodes();
						if (tokens != null) {
							final int tokenCount = tokens.getLength();
							for (int b = 0; b < tokenCount; b++) {
								final Node tokenNode = tokens.item(b);
								if ((tokenNode != null) && (tokenNode.getNodeName().equals("zofar:preload"))) {
									final String nameValue = ((Element) tokenNode).getAttribute("name");
									if ((nameValue != null) && (nameValue.equals(token))) {
										tokenExist = true;
									}
								}
							}
						}

						if (!tokenExist) {
							// Add test token
							final Node tokenNode = qmlDoc.createElement("zofar:preload");
							((Element) tokenNode).setAttribute("password", token);
							((Element) tokenNode).setAttribute("name", token);
							node.appendChild(tokenNode);
						}
					}
				}
			} catch (final Exception e) {
				e.printStackTrace();
			}
		}
		qml.saveDocument(qmlDoc, qmlFile);
		if (!overrideCommit) {
			try {
				svn.doCommit(svnUser, svnPass, qmlFile, comments.toString());
			} catch (final SVNException e) {
				throw new Exception(e);
			}
		}

	}

	private void modifyJSFTemplate(final File templateFile, final boolean mdm, final String svnUser,
			final String svnPass, final boolean overrideCommit) throws Exception {	
		final SVNClient svn = SVNClient.getInstance();
		final FileClient fileClient = FileClient.getInstance();
		if(mdm) {
			if ((templateFile != null) && (templateFile.exists())) {
				final String[] fileName = templateFile.getName().split(Pattern.quote("."));
				File backup = fileClient.createOrGetFile(fileName[0] + "_backup", "." + fileName[1],
						templateFile.getParentFile());
				fileClient.copyFile(templateFile, backup);
			}

			final File screenshotTemplateFile = fileClient.getResource("builder/mdmSurveyScreenshot.xhtml");
			if ((screenshotTemplateFile != null) && (screenshotTemplateFile.exists())) {
				final String content = fileClient.readAsString(screenshotTemplateFile);
				fileClient.writeToFile(templateFile, content, false);
			}
			
		}
		
		
		
	}

	private void modifyDB(final File dbPropertieFile, final String dbLocation, final String dbPort, final String dbName,
			final String dbUser, final String dbPass, final String svnUrl, final String svnUser, final String svnPass,
			final boolean overrideCommit) throws Exception {
		final ConfigurationUtils conf = ConfigurationUtils.getInstance();
		final SVNClient svn = SVNClient.getInstance();
		final Properties props = conf.getConfigurationFromFileSystem(dbPropertieFile.getAbsolutePath());
		props.setProperty("jdbc.username", dbUser);
		props.setProperty("jdbc.password", dbPass);
		props.setProperty("jdbc.url", "jdbc:postgresql://" + dbLocation + ":" + dbPort + "/" + dbName);
		props.setProperty("connectionPool.maxSize", "50");
		props.setProperty("connectionPool.maxStatements", "0");
		props.setProperty("connectionPool.minSize", "10");
		props.setProperty("connectionPool.testConnectionOnCheckout", "true");
		props.setProperty("connectionPool.idleConnectionTestPeriod", "300");
		props.setProperty("connectionPool.preferredTestQuery", "select 1;");
		if (conf.saveConfiguration(props, dbPropertieFile.getAbsolutePath())) {
			if (!overrideCommit) {
				try {
					svn.doCommit(svnUser, svnPass, dbPropertieFile, "Database Connection set to\n Location: "
							+ dbLocation + "\n DBName : " + dbName + "\n User: " + dbUser + "\n Pass: " + dbPass + "");
				} catch (final SVNException e) {
					throw new Exception(e);
				}
			}
		} else {
			System.err.println("cannot modify " + dbPropertieFile);
		}
	}

	private void modifySessionSecurity(final File securityFile, final String dbLocation,final String dbPort, final String dbName,
			final String dbUser, final String dbPass, final String svnUrl, final String svnUser, final String svnPass,
			final boolean overrideCommit) throws Exception {
		final SVNClient svn = SVNClient.getInstance();
		final XmlClient xml = XmlClient.getInstance();
		final Document doc = xml.getDocument(securityFile.getAbsolutePath());
		if (doc != null) {
			final XmlObject config = xml.docToXmlObject(doc);


			config.save(securityFile);

			final XmlObject[] propertyNodes = xml.getByXPath(config,
					"/hibernate-configuration/session-factory/property[@name]");
			if (propertyNodes != null) {
				for (final XmlObject propertyNode : propertyNodes) {
					final String type = xml.getAttribute(propertyNode, "name");
					if (type.equals("connection.url")) {
						propertyNode.newCursor().setTextValue("jdbc:postgresql://" + dbLocation + ":"+dbPort+"/" + dbName);
					} else if (type.equals("connection.username")) {
						propertyNode.newCursor().setTextValue(dbUser);
					} else if (type.equals("connection.password")) {
						propertyNode.newCursor().setTextValue(dbPass);
					}
				}
				config.save(securityFile);

			}

			if ((!overrideCommit)) {
				try {
					svn.doCommit(svnUser, svnPass, securityFile, "Session Security Connection set to\n Location: "
							+ dbLocation + "\n DBName : " + dbName + "\n User: " + dbUser + "\n Pass: " + dbPass + "");
				} catch (final SVNException e) {
					throw new Exception(e);
				}
			}
			final String content = FileClient.getInstance().readAsString(securityFile);
			System.out.println("session-security : " + content);
		}


	}

	private void modifySystem(final File systemPropertieFile, final boolean overrideNav, final boolean cutHistory,
			final String saveMode, final String login, final boolean preloadOnStart, final boolean record,
			final boolean cluster, final String svnUser, final String svnPass, final boolean overrideCommit)
			throws Exception {
		final ConfigurationUtils conf = ConfigurationUtils.getInstance();
		final SVNClient svn = SVNClient.getInstance();
		final StringBuffer comments = new StringBuffer();

		final Properties props = conf.getConfigurationFromFileSystem(systemPropertieFile.getAbsolutePath());

		props.setProperty("overrideNavigation", overrideNav + "");
		comments.append("overrideNavigation : " + overrideNav + ",");

		props.setProperty("cutHistory", cutHistory + "");
		comments.append("cutHistory : " + cutHistory + ",");

		props.setProperty("saveMode", saveMode);
		comments.append("saveMode : " + saveMode + ",");

		props.setProperty("login", login);
		comments.append("login : " + login + ",");

		props.setProperty("preload_on_start", preloadOnStart + "");
		comments.append("preload_on_start : " + preloadOnStart + ",");

		props.setProperty("record", record + "");
		comments.append("record : " + record + ",");

		props.setProperty("cluster", cluster + "");
		comments.append("record : " + record + ",");

		if (conf.saveConfiguration(props, systemPropertieFile.getAbsolutePath())) {
			if (!overrideCommit) {
				try {
					svn.doCommit(svnUser, svnPass, systemPropertieFile,
							"System properties modified: " + comments.toString());
				} catch (final SVNException e) {
					throw new Exception(e);
				}
			}
		} else {
			System.err.println("cannot modify " + systemPropertieFile);
		}
	}

	public File getPom(final File projectDir) {
		final File pom = new File(projectDir.getAbsolutePath() + File.separator + "pom.xml");
		return pom;
	}

	private boolean removeXpp3Node(final Xpp3Dom parent, final Xpp3Dom child) {
		int removeIndex = -1;
		final int parentCount = parent.getChildCount();
		for (int i = 0; i < parentCount; i++) {
			if (parent.getChild(i) == child) {
				removeIndex = i;
				break;
			}
		}
		if (removeIndex == -1) {
			return false;
		} else {
			parent.removeChild(removeIndex);
			return true;
		}
	}

	private void modifyPom(final String finalName, final File pomFile, final boolean pretest,
			final boolean linearNavigation, final boolean overrideRendering, final boolean mdm,
			final boolean noVisibleMap, final String svnServer, final String svnUser, final String svnPass)
			throws Exception {

		System.out.println("modify pom");
		final Reader reader = new FileReader(pomFile);
		try {
			final MavenXpp3Reader xpp3Reader = new MavenXpp3Reader();
			final Model model = xpp3Reader.read(reader);
			final Build build = model.getBuild();
			build.setFinalName(finalName);
			final Map<String, Plugin> plugins = build.getPluginsAsMap();
			if (plugins != null) {
				final Plugin plugin = plugins.get("de.his.zofar:zofar.survey.generator.maven.plugin");
				if (plugin != null) {
					plugin.setVersion("1.0.0MultipleQML");
//					plugin.setVersion("0.0.9-SNAPSHOT");
					
					Xpp3Dom conf = (Xpp3Dom) plugin.getConfiguration();
					if (conf == null) {
						conf = new Xpp3Dom("configuration");
						plugin.setConfiguration(conf);
					}

					removeXpp3Node(conf, conf.getChild("pretest"));
					final Xpp3Dom pretestDom = new Xpp3Dom("pretest");
					pretestDom.setParent(conf);
					pretestDom.setValue("" + pretest);
					conf.addChild(pretestDom);

					removeXpp3Node(conf, conf.getChild("linearNavigation"));
					final Xpp3Dom linearNavDom = new Xpp3Dom("linearNavigation");
					linearNavDom.setParent(conf);
					linearNavDom.setValue("" + linearNavigation);
					conf.addChild(linearNavDom);

					removeXpp3Node(conf, conf.getChild("overrideRendering"));
					final Xpp3Dom overrideRenderingDom = new Xpp3Dom("overrideRendering");
					overrideRenderingDom.setParent(conf);
					overrideRenderingDom.setValue("" + overrideRendering);
					conf.addChild(overrideRenderingDom);

					removeXpp3Node(conf, conf.getChild("mdm"));
					final Xpp3Dom mdmDom = new Xpp3Dom("mdm");
					mdmDom.setParent(conf);
					mdmDom.setValue("" + mdm);
					conf.addChild(mdmDom);

					removeXpp3Node(conf, conf.getChild("noVisibleMap"));
					final Xpp3Dom noVisibleMapDom = new Xpp3Dom("noVisibleMap");
					noVisibleMapDom.setParent(conf);
					noVisibleMapDom.setValue("" + noVisibleMap);
					conf.addChild(noVisibleMapDom);
					
					
					
//					final Map<String, PluginExecution> executions = plugin.getExecutionsAsMap();
//					final PluginExecution execution = executions.get("generate-survey");
//					if (execution != null) {
//						Xpp3Dom conf = (Xpp3Dom) execution.getConfiguration();
//						if (conf == null) {
//							conf = new Xpp3Dom("configuration");
//							execution.setConfiguration(conf);
//						}
//
//						removeXpp3Node(conf, conf.getChild("pretest"));
//						final Xpp3Dom pretestDom = new Xpp3Dom("pretest");
//						pretestDom.setParent(conf);
//						pretestDom.setValue("" + pretest);
//						conf.addChild(pretestDom);
//
//						removeXpp3Node(conf, conf.getChild("linearNavigation"));
//						final Xpp3Dom linearNavDom = new Xpp3Dom("linearNavigation");
//						linearNavDom.setParent(conf);
//						linearNavDom.setValue("" + linearNavigation);
//						conf.addChild(linearNavDom);
//
//						removeXpp3Node(conf, conf.getChild("overrideRendering"));
//						final Xpp3Dom overrideRenderingDom = new Xpp3Dom("overrideRendering");
//						overrideRenderingDom.setParent(conf);
//						overrideRenderingDom.setValue("" + overrideRendering);
//						conf.addChild(overrideRenderingDom);
//
//						removeXpp3Node(conf, conf.getChild("mdm"));
//						final Xpp3Dom mdmDom = new Xpp3Dom("mdm");
//						mdmDom.setParent(conf);
//						mdmDom.setValue("" + mdm);
//						conf.addChild(mdmDom);
//
//						removeXpp3Node(conf, conf.getChild("noVisibleMap"));
//						final Xpp3Dom noVisibleMapDom = new Xpp3Dom("noVisibleMap");
//						noVisibleMapDom.setParent(conf);
//						noVisibleMapDom.setValue("" + noVisibleMap);
//						conf.addChild(noVisibleMapDom);
//					}
				}

				Plugin verifier = plugins.get("zofar.testing:verifier");

				System.out.println("Verifier : " + verifier);

				if (verifier == null) {
					verifier = new Plugin();
					verifier.setArtifactId("verifier");
					verifier.setGroupId("zofar.testing");
					plugins.put("zofar.testing:verifier", verifier);
					build.getPlugins().add(verifier);
				}
				if (verifier != null) {
					verifier.setVersion("1.0.2-SNAPSHOT");
					Xpp3Dom conf = (Xpp3Dom) verifier.getConfiguration();
					if (conf == null) {
						conf = new Xpp3Dom("configuration");
						verifier.setConfiguration(conf);
					}
					removeXpp3Node(conf, conf.getChild("contentPath"));
					final Xpp3Dom contentPath = new Xpp3Dom("contentPath");
					contentPath.setParent(conf);
					contentPath.setValue("${basedir}/src/main/resources/questionnaire.xml");
					conf.addChild(contentPath);

					// TODO server path only needed. not complete path
					String debugSvnServer = svnServer;
					if (debugSvnServer.endsWith("/svn/hiob/tags/surveys")) {
						LOGGER.warn("[BUG] need to clean svnServer");
						debugSvnServer = debugSvnServer.replace("/svn/hiob/tags/surveys", "");
					}

					removeXpp3Node(conf, conf.getChild("svnServer"));
					final Xpp3Dom svnServerDom = new Xpp3Dom("svnServer");
					svnServerDom.setParent(conf);
					svnServerDom.setValue(debugSvnServer);
					conf.addChild(svnServerDom);

					removeXpp3Node(conf, conf.getChild("svnUser"));
					final Xpp3Dom svnUserDom = new Xpp3Dom("svnUser");
					svnUserDom.setParent(conf);
					// svnUserDom.setValue(svnUser);
					svnUserDom.setValue("gast0");
					conf.addChild(svnUserDom);

					removeXpp3Node(conf, conf.getChild("svnPass"));
					final Xpp3Dom svnPassDom = new Xpp3Dom("svnPass");
					svnPassDom.setParent(conf);
					// svnPassDom.setValue(svnPass);
					svnPassDom.setValue("gr7a3v");
					conf.addChild(svnPassDom);

					removeXpp3Node(conf, conf.getChild("svnPath"));
					final Xpp3Dom svnPath = new Xpp3Dom("svnPath");
					svnPath.setParent(conf);
					svnPath.setValue(
							"svn/hiob/trunk/zofar/zofar.service/zofar.service.questionnaire/zofar.service.questionnaire.xml/src/main/xsd/de/his/zofar/xml");
					conf.addChild(svnPath);

					removeXpp3Node(conf, conf.getChild("qmlSchemaFileName"));
					final Xpp3Dom qmlSchemaFileName = new Xpp3Dom("qmlSchemaFileName");
					qmlSchemaFileName.setParent(conf);
					qmlSchemaFileName.setValue("zofar_questionnaire_0.2.xsd");
					conf.addChild(qmlSchemaFileName);

					removeXpp3Node(conf, conf.getChild("navigationSchemaFileName"));
					final Xpp3Dom navigationSchemaFileName = new Xpp3Dom("navigationSchemaFileName");
					navigationSchemaFileName.setParent(conf);
					navigationSchemaFileName.setValue("navigation_0.1.xsd");
					conf.addChild(navigationSchemaFileName);

					removeXpp3Node(conf, conf.getChild("displaySchemaFileName"));
					final Xpp3Dom displaySchemaFileName = new Xpp3Dom("displaySchemaFileName");
					displaySchemaFileName.setParent(conf);
					displaySchemaFileName.setValue("display_0.1.xsd");
					conf.addChild(displaySchemaFileName);

					removeXpp3Node(conf, conf.getChild("researchdatacenterSchemaFileName"));
					final Xpp3Dom researchdatacenterSchemaFileName = new Xpp3Dom("researchdatacenterSchemaFileName");
					researchdatacenterSchemaFileName.setParent(conf);
					researchdatacenterSchemaFileName.setValue("researchdatacenter_0.1.xsd");
					conf.addChild(researchdatacenterSchemaFileName);

					final Map<String, PluginExecution> executions = verifier.getExecutionsAsMap();
					PluginExecution execution = executions.get("generate-survey");
					if (execution == null) {
						execution = new PluginExecution();
						executions.put("generate-survey", execution);
					}

					if (execution != null) {
						execution.setId("validate-qml");
						execution.setPhase("validate");
						execution.getGoals().add("validate-qml");
					}
				}
			}

			// model.setBuild(build);

			final MavenXpp3Writer writer = new MavenXpp3Writer();
			writer.write(new FileOutputStream(pomFile), model);

			System.out.println("Pom : " + FileClient.getInstance().readAsString(pomFile));

		} finally {
			reader.close();
		}
	}
}

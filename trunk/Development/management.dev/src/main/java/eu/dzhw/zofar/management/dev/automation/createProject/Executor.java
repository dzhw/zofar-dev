package eu.dzhw.zofar.management.dev.automation.createProject;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.offbytwo.jenkins.JenkinsServer;
import com.offbytwo.jenkins.model.JobWithDetails;

import eu.dzhw.zofar.management.comm.continuousintegration.jenkins.JenkinsClient;
import eu.dzhw.zofar.management.dev.automation.AbstractExecutor;
import eu.dzhw.zofar.management.utils.files.DirectoryClient;
import eu.dzhw.zofar.management.utils.files.FileClient;
import eu.dzhw.zofar.management.utils.string.StringUtils;

public class Executor extends AbstractExecutor {

	private static final long serialVersionUID = -7369649930100173777L;
	private static final Executor INSTANCE = new Executor();
	private static final Logger LOGGER = LoggerFactory.getLogger(Executor.class);

	public enum Parameter implements ABSTRACTPARAMETER {
		survey, addon, svnServer, svnUser, svnPass, dbLocation, dbPort, dbUser, dbPass, workDir, jenkinsServer, jenkinsUser, jenkinsPass;
	};

	private Executor() {
		super();

	}

	public static Executor getInstance() {
		return INSTANCE;
	}

	public ParameterMap<ABSTRACTPARAMETER, Object> getParameterMap(final String survey, final String addon,
			final File workDir, final String svnServer, final String svnUser, final String svnPass,
			final String jenkinsServer, final String jenkinsUser, final String jenkinsPass, final String dbLocation,
			final String dbPort, final String dbUser, final String dbPass) {
		final ParameterMap<ABSTRACTPARAMETER, Object> back = new ParameterMap<ABSTRACTPARAMETER, Object>();
		back.put(Parameter.survey, survey);
		back.put(Parameter.addon, addon);

		back.put(Parameter.workDir, workDir);

		back.put(Parameter.svnServer, svnServer);
		back.put(Parameter.svnUser, svnUser);
		back.put(Parameter.svnPass, svnPass);

		back.put(Parameter.jenkinsServer, jenkinsServer);
		back.put(Parameter.jenkinsUser, jenkinsUser);
		back.put(Parameter.jenkinsPass, jenkinsPass);

		back.put(Parameter.dbLocation, dbLocation);
		back.put(Parameter.dbUser, dbUser);
		back.put(Parameter.dbPass, dbPass);
		back.put(Parameter.dbPort, dbPort);
		return back;
	}

	@Override
	public void process(ParameterMap<ABSTRACTPARAMETER, Object> parameter) throws Exception {
		final String survey = (String) parameter.get(Parameter.survey);
		final String addon = (String) parameter.get(Parameter.addon);

		final String svnServer = (String) parameter.get(Parameter.svnServer);
		final String svnUrl = svnServer + "/svn/hiob/tags/surveys";
		final String svnUser = (String) parameter.get(Parameter.svnUser);
		final String svnPass = (String) parameter.get(Parameter.svnPass);

		final File workDir = (File) parameter.get(Parameter.workDir);

		if (survey != null) {
			final File project = builder.createProject(survey, workDir,svnUrl,svnUser,svnPass);
			// final File project = new
			// File("xxxx/CREATE_Projects/works/automatisierungstestprojekt1");

			// clean Project
			final File clone = DirectoryClient.getInstance().createDir(project.getParentFile(),
					project.getName() + "_clean");
			DirectoryClient.getInstance().copyDirectory(project, clone);

			removeIrrelevantRelevantFiles(clone);

			builder.commitProject(survey, clone, svnUrl, svnUser, svnPass);
			// Jenkins
			File preloadFile = new File(project.getAbsolutePath() + "/src/main/resources/survey/preloads_postgres.sql");
			installToDB(preloadFile, "build_" + survey, parameter);
			installToJenkins(survey, "build_" + survey, parameter);
		}

		if ((addon != null)&&(!addon.equals(""))) {
			builder.createProject(addon, workDir,svnUrl,svnUser,svnPass);
			builder.commitProject(addon, workDir, svnUrl, svnUser, svnPass);

			// Jenkins
			installToJenkins(addon, "build_" + addon, parameter);
		}

	}

	private void removeIrrelevantRelevantFiles(final File current) {
		final List<File> files = DirectoryClient.getInstance().readDir(current, null);
		for (final File file : files) {
			final String fileName = file.getName();
			boolean flag = false;

			if (fileName.startsWith("."))
				flag = true;
			if (fileName.equals("target"))
				flag = true;

			if (flag) {
				System.out.println("Delete : " + file.getAbsolutePath());
				try {
					FileUtils.forceDelete(file);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {
				if (file.isDirectory())
					removeIrrelevantRelevantFiles(file);
				else if (file.isFile()) {
				}
			}
		}
	}

	private boolean installToDB(final File preloads, final String dbName,
			ParameterMap<ABSTRACTPARAMETER, Object> parameter) throws Exception {
		final String dbLocation = (String) parameter.get(Parameter.dbLocation);
		final String dbPort = (String) parameter.get(Parameter.dbPort);
		final String dbUser = (String) parameter.get(Parameter.dbUser);
		final String dbPass = (String) parameter.get(Parameter.dbPass);

		final Connection dbConn = postgresClient.getMaintenanceConnection(dbLocation, dbPort, dbUser, dbPass);
		if (dbConn != null) {
			if (postgresClient.existDB(dbConn, dbName)) {
				LOGGER.info("DB {} already exist.", dbName);
			} else {
				postgresClient.createDB(dbConn, dbName);
			}
			if (postgresClient.existDB(dbConn, dbName + ".session")) {
				LOGGER.info("DB {} already exist.", dbName + ".session");
			} else {
				postgresClient.createDB(dbConn, dbName + ".session");
			}
			postgresClient.close(dbConn);
		}

		// // Insert Participants
		if ((preloads != null) && (preloads.exists())) {
			System.out.println("load Preloads to DB");
			final String content = FileClient.getInstance().readAsString(preloads);
			if ((content != null) && (!content.equals(""))) {
				final Connection preloadConn = postgresClient.getConnection(dbLocation, dbPort, dbName, dbUser, dbPass);
				postgresClient.executeDb(preloadConn,
						"CREATE TABLE participant (id bigint NOT NULL,version integer NOT NULL,password character varying(255) NOT NULL,token character varying(255) NOT NULL,CONSTRAINT participant_pkey PRIMARY KEY (id),CONSTRAINT participant_token_key UNIQUE (token)) WITH (OIDS=FALSE); ALTER TABLE participant OWNER TO postgres;");
				postgresClient.executeDb(preloadConn, "TRUNCATE participant CASCADE;");
				postgresClient.executeDb(preloadConn,
						"CREATE TABLE surveydata ( id bigint NOT NULL, version integer NOT NULL, value character varying(2000) NOT NULL, variablename character varying(255) NOT NULL, participant_id bigint NOT NULL, CONSTRAINT surveydata_pkey PRIMARY KEY (id), CONSTRAINT fkd7db5a04a3f7481e FOREIGN KEY (participant_id) REFERENCES participant (id) MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION, CONSTRAINT surveydata_participant_id_variablename_key UNIQUE (participant_id, variablename) ) WITH ( OIDS=FALSE ); ALTER TABLE surveydata OWNER TO postgres;");
				postgresClient.executeDb(preloadConn, "TRUNCATE surveydata CASCADE;");
				// postgresClient.executeDb(preloadConn, "TRUNCATE surveyhistory CASCADE;");
				// postgresClient.executeDb(preloadConn, content);
				postgresClient.close(preloadConn);
				System.out.println("done");
			} else
				System.err.println("Preload Content empty");
		}
		return false;
	}

	private boolean installToJenkins(final String surveyName, final String dbName,
			ParameterMap<ABSTRACTPARAMETER, Object> parameter) throws Exception {
		final String jenkinsServer = (String) parameter.get(Parameter.jenkinsServer);
		final String jenkinsUser = (String) parameter.get(Parameter.jenkinsUser);
		final String jenkinsPass = (String) parameter.get(Parameter.jenkinsPass);

		final String svnServer = (String) parameter.get(Parameter.svnServer);

		final String dbLocation = (String) parameter.get(Parameter.dbLocation);
		final String dbUser = (String) parameter.get(Parameter.dbUser);
		final String dbPass = (String) parameter.get(Parameter.dbPass);

		final JenkinsServer jenkins = JenkinsClient.getInstance().getServer(jenkinsServer, jenkinsUser, jenkinsPass);
		if (jenkins != null) {
			final JobWithDetails job = JenkinsClient.getInstance().createJob(jenkins, surveyName, svnServer,
					"/svn/hiob/tags/surveys", dbLocation, dbName, dbUser, dbPass);
			return (job != null);
		}

		return false;
	}

}

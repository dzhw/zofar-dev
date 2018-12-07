/**
 *
 */
package de.his.zofar.presentation.surveyengine.listener;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.encoding.ShaPasswordEncoder;
import org.springframework.web.context.support.WebApplicationContextUtils;

import au.com.bytecode.opencsv.CSVReader;
import de.his.zofar.persistence.surveyengine.daos.AnswerOptionDao;
import de.his.zofar.persistence.surveyengine.entities.AnswerOptionEntity;
import de.his.zofar.presentation.surveyengine.security.concurrentSession.ConcurrentSessionManager;
import de.his.zofar.presentation.surveyengine.util.SystemConfiguration;
import de.his.zofar.service.surveyengine.model.Participant;
import de.his.zofar.service.surveyengine.model.SurveyData;
import de.his.zofar.service.surveyengine.service.SurveyEngineService;

/**
 * @author le
 * 
 */
public class InitializeDatabaseContextListener implements
		ServletContextListener {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(InitializeDatabaseContextListener.class);

	private final static String PRELOAD_PREFIX = "PRELOAD";

	private class CsvPackage {
		private List<String> header;
		private Map<String, Map<String, String>> content;

		public CsvPackage() {
			super();
			this.header = new ArrayList<String>();
			this.content = new HashMap<String, Map<String, String>>();
		}

		public List<String> getHeader() {
			return header;
		}

		public Map<String, Map<String, String>> getContent() {
			return content;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result
					+ ((content == null) ? 0 : content.hashCode());
			result = prime * result
					+ ((header == null) ? 0 : header.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			CsvPackage other = (CsvPackage) obj;
			if (content == null) {
				if (other.content != null)
					return false;
			} else if (!content.equals(other.content))
				return false;
			if (header == null) {
				if (other.header != null)
					return false;
			} else if (!header.equals(other.header))
				return false;
			return true;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.servlet.ServletContextListener#contextInitialized(javax.servlet
	 * .ServletContextEvent)
	 */
	@Override
	public void contextInitialized(final ServletContextEvent sce) {
		LOGGER.info("initialize database listener");

		final ApplicationContext context = WebApplicationContextUtils
				.getRequiredWebApplicationContext(sce.getServletContext());

		SystemConfiguration systemConfiguration = SystemConfiguration
				.getInstance();

		if (systemConfiguration.preloadOnStart())
			this.createPreloads(context);
		this.createAnswerOptionLabels(context);
		
		ConcurrentSessionManager manager = (ConcurrentSessionManager) context.getBean("concurrentSessionManager");
	}

	private CsvPackage readCSV(final InputStream in, final char separator,
			final String keyFieldName) {
		if (in == null)
			return null;
		;

		CsvPackage back = new CsvPackage();

		if (in != null) {
			final CSVReader reader = new CSVReader(new InputStreamReader(in),
					separator);
			String[] nextLine;

			try {
				boolean firstLine = true;
				while ((nextLine = reader.readNext()) != null) {
					if (firstLine) {
						back.getHeader().addAll(Arrays.asList(nextLine));
						firstLine = false;
					} else {
						final Iterator<String> variableIt = back.getHeader()
								.iterator();
						final Map<String, String> data = new LinkedHashMap<String, String>();
						String keyField = null;
						while (variableIt.hasNext()) {
							final String variablename = variableIt.next();
							final int index = back.getHeader().indexOf(
									variablename);
							final String value = nextLine[index];
							if (variablename.equals(keyFieldName))
								keyField = value;
							if (!value.equals(""))
								data.put(variablename, value);
						}
						if (keyField != null)
							back.getContent().put(keyField, data);
					}
				}
			} catch (final IOException e) {
				e.printStackTrace();
			}
		}
		return back;
	}

	private void createPreloads(final ApplicationContext context) {
		final SurveyEngineService service = context.getBean(
				"surveyEngineService", SurveyEngineService.class);

		// parse preload.csv
		final InputStream in = InitializeDatabaseContextListener.class
				.getResourceAsStream("/survey/preload.csv");

		if (in != null) {
			final CsvPackage existingCsv = this.readCSV(in, '#', "name");
			if (existingCsv != null) {

				final List<String> header = existingCsv.getHeader();
				header.remove("name");
				header.remove("password");

				final Map<String, Map<String, String>> content = existingCsv
						.getContent();

				final Iterator<String> it = content.keySet().iterator();
				while (it.hasNext()) {
					final String token = it.next();
					final Map<String, String> data = content.get(token);

					boolean dirty = false;

					Participant participant = service.loadParticipant(token);
					if (participant == null) {
						String password = null;
						if (data.containsKey("password")
								&& data.get("name").equals(token))
							password = data.get("password");
						if (password != null) {
							dirty = true;
							participant = service.createParticipant(token,
									password);
						}
						LOGGER.info("create Participant {} ({})",token,password);
					}

					if (participant != null) {
						final Iterator<String> variableIt = header.iterator();
						while (variableIt.hasNext()) {
							final String variable = variableIt.next();
							final String value = data.get(variable);
							
							final String preloadVariable = PRELOAD_PREFIX+variable;
							// Data query
							if (value != null) {
								if (!participant.getSurveyData().containsKey(preloadVariable)) {
									dirty = true;
									final SurveyData preload = new SurveyData();
									preload.setParticipant(participant);
									preload.setVariableName(preloadVariable);
									preload.setValue(value);
									participant.addSurveyData(preload);
								}
								else{
									LOGGER.info("Preload Information {} already exists for Participant {}",variable,token);
								}
							}
						}
					}
					if(dirty)service.saveParticipant(participant);
				}
			}
		} else {
			LOGGER.error("preload file do not exist");
		}

	}

	/**
	 * @param context
	 */
	// private void createPreloads(final ApplicationContext context) {
	// final SurveyEngineService service =
	// context.getBean("surveyEngineService", SurveyEngineService.class);
	// final Map<String,String> participantMap = new HashMap<String,String>();
	// final Map<String,Map<String,String>> preloadMap = new
	// HashMap<String,Map<String,String>>();
	//
	// //parse preload.csv
	//
	// final InputStream in = InitializeDatabaseContextListener.class
	// .getResourceAsStream("/survey/preload.csv");
	//
	// if (in != null) {
	// final CSVReader reader = new CSVReader(new InputStreamReader(in),'#');
	// String[] nextLine;
	// final List<String> variableNames = new ArrayList<String>();
	// try {
	// boolean firstLine = true;
	// while ((nextLine = reader.readNext()) != null) {
	// if(firstLine){
	// variableNames.addAll(Arrays.asList(nextLine));
	// firstLine = false;
	// }
	// else{
	// Iterator<String> variableIt = variableNames.iterator();
	// String name = null;
	// String password = null;
	// Map<String,String> data = new HashMap<String,String>();
	// while (variableIt.hasNext()) {
	// final String variablename = variableIt.next();
	// final int index = variableNames.indexOf(variablename);
	// final String value = nextLine[index];
	// if(variablename.equals("name"))name = value;
	// else if(variablename.equals("password"))password = value;
	// else{
	// if(!value.equals(""))data.put(variablename, value);
	// }
	// }
	// participantMap.put(name, password);
	// preloadMap.put(name, data);
	// }
	// }
	// } catch (final IOException e) {
	// e.printStackTrace();
	// }
	// }
	//
	// final Iterator<String> it = participantMap.keySet().iterator();
	// while(it.hasNext()){
	// final String token = it.next();
	// final String password = participantMap.get(token);
	// final Map<String,String> preloads = preloadMap.get(token);
	//
	// final Participant participant = service.loadParticipant(token);
	// if(participant != null){
	// LOGGER.info("Participant {} already exists.",token);
	// if(preloads != null){
	// LOGGER.info("Update Preloads for Participant {}.",token);
	// final Iterator<String> preloadIt = preloads.keySet().iterator();
	// boolean dirty = false;
	// while(preloadIt.hasNext()){
	// final String variable = preloadIt.next();
	// final String value = preloads.get(variable);
	// if(participant.getSurveyData().containsKey(variable)){
	// LOGGER.info("Preload {} for Participant {} already exist.",variable,token);
	// }
	// else{
	// LOGGER.info("add Preload {} for Participant {}.",variable+" = "+value,token);
	//
	// final SurveyData data = new SurveyData();
	// data.setParticipant(participant);
	// data.setVariableName(variable);
	// data.setValue(value);
	// participant.addSurveyData(data);
	// dirty = true;
	// }
	// }
	// if(dirty)service.saveParticipant(participant);
	// }
	// }
	// else{
	// LOGGER.info("create Participant {}.",token);
	// service.createParticipant(token, password, preloads);
	// }
	// }
	// }

	/**
	 * @param context
	 */
	private void createAnswerOptionLabels(final ApplicationContext context) {
		final AnswerOptionDao answerOptionDao = context.getBean(
				"answerOptionDao", AnswerOptionDao.class);

		final InputStream in = InitializeDatabaseContextListener.class
				.getResourceAsStream("/survey/labels.csv");

		if (in != null) {
			final CSVReader reader = new CSVReader(new InputStreamReader(in));

			String[] nextLine;
			try {
				final Map<String, AnswerOptionEntity> entities = new HashMap<String, AnswerOptionEntity>();
				while ((nextLine = reader.readNext()) != null) {
					final String uid = nextLine[0];
					final String variable = nextLine[1];
					final String condition = nextLine[2];
					final String bundlekey = nextLine[3];
					final String key = uid + "#" + variable;
					if (!entities.containsKey(key)) {
						entities.put(key, new AnswerOptionEntity(uid, variable));
					}
					entities.get(key).addConditionAndKey(condition, bundlekey);
				}
				if (!entities.isEmpty()) {
					LOGGER.info("creating visible conditions and resource keys of answer options.");
					answerOptionDao.deleteAll();
					answerOptionDao.save(entities.values());
				}
			} catch (final IOException e) {
				e.printStackTrace();
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.ServletContextListener#contextDestroyed(javax.servlet.
	 * ServletContextEvent)
	 */
	@Override
	public void contextDestroyed(final ServletContextEvent sce) {
		// Nothing to do!
	}

}

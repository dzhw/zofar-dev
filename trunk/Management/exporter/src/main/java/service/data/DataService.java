package service.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import model.ParticipantEntity;
import model.SurveyData;
import model.SurveyHistory;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.jdbc.Work;
import org.hibernate.service.ServiceRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import utils.XmlUtils;
import de.his.export.xml.export.DataType;
import de.his.export.xml.export.DatasetType;
import de.his.export.xml.export.ParticipantType;
//import de.his.zofar.persistence.surveyengine.entities.ParticipantEntity;
//import de.his.zofar.persistence.surveyengine.entities.SurveyDataEntity;
//import de.his.zofar.service.surveyengine.model.Participant;
//import de.his.zofar.service.surveyengine.model.SurveyHistory;

public class DataService {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(DataService.class);

	private static DataService INSTANCE;

	private SessionFactory sessionFactory;
	private Configuration configuration;

	private class CustomWork implements Work {
		private final Set<Map<String, Object>> back;
		private final String query;

		public CustomWork(final Set<Map<String, Object>> back,
				final String query) {
			super();
			this.back = back;
			this.query = query;

		}

		public Set<Map<String, Object>> getBack() {
			return this.back;
		}

		@Override
		public void execute(final Connection connection) throws SQLException {
			PreparedStatement st = null;
			try {
				st = connection.prepareStatement(this.query);
				final ResultSet rs = st.executeQuery();

				if (rs != null) {
					while (rs.next()) {
						final Map<String, Object> row = new HashMap<String, Object>();
						final int count = rs.getMetaData().getColumnCount();
						for (int a = 1; a <= count; a++) {
							final String name = rs.getMetaData().getColumnName(
									a);
							final String value = rs.getString(a);
							row.put(name, value);
						}

						this.back.add(row);
					}
				}

			} finally {
				if (st != null)
					st.close();
			}
		}
	};

	// private ApplicationContext context = null;

	private DataService() {
		super();
	}

	public static DataService getInstance() {
		if (INSTANCE == null)
			INSTANCE = new DataService();
		return INSTANCE;
	}

	// private ApplicationContext getContext() {
	// if (this.context == null) {
	// context = new ClassPathXmlApplicationContext(
	// "application-context.xml");
	// }
	// return context;
	// }
	//
	// private SurveyEngineService getSurveyEngineService() {
	// return getContext().getBean(SurveyEngineService.class);
	// }

	public Configuration getConfiguration() {
		if (this.configuration == null) {
			this.configuration = new Configuration();
			this.configuration.configure("/database.cfg.xml");
		}
		return this.configuration;
	}

	private SessionFactory getSessionFactory(final Configuration conf)
			throws Throwable {
		final ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
				.applySettings(conf.getProperties()).build();
		return conf.buildSessionFactory(serviceRegistry);
	}

	private Session getSession(final Configuration conf) throws Throwable {
		if (this.sessionFactory == null)
			this.sessionFactory = this.getSessionFactory(conf);
		if (this.sessionFactory != null) {
			return this.sessionFactory.openSession();
		}
		return null;
	}

	private Set<Map<String, Object>> queryDB(final String queryStr,
			final Configuration conf) throws Throwable {
		// LOGGER.info("Query DB : {}", queryStr);
		final Session session = this.getSession(conf);
		if (session != null) {
			final Transaction transaction = session.beginTransaction();
			try {
				final CustomWork work = new CustomWork(
						new LinkedHashSet<Map<String, Object>>(), queryStr);
				session.doWork(work);
				transaction.commit();
				return work.getBack();
			} catch (final Throwable t) {
				transaction.rollback();
				throw t;
			} finally {
				if ((session != null) && (session.isOpen()))
					session.close();
			}
		} else {
			LOGGER.error("Creating Hibernate Session failed");
		}
		return null;
	}

	// public List<ParticipantEntity> getParticipants() {
	// return getSurveyEngineService().exportParticipants();
	//
	// }

	public List<ParticipantEntity> getParticipants(final Configuration conf) {
		try {
			final Set<Map<String, Object>> result = this.queryDB(
					"select id,password,token from participant order by id;",
					conf);
			if (result != null) {
				final List<ParticipantEntity> back = new ArrayList<ParticipantEntity>();
				for (final Map<String, Object> entry : result) {
					final ParticipantEntity partEntry = new ParticipantEntity();
					partEntry.setId(entry.get("id") + "");
					partEntry.setPassword(entry.get("password") + "");
					partEntry.setToken(entry.get("token") + "");
					// partEntry.setSurveyData(this.getData(entry.get("id")+"",
					// conf));
					back.add(partEntry);
				}
				return back;
			}
		} catch (final Throwable e) {
			e.printStackTrace();
		}
		return null;
	}

	public Map<String, List<SurveyHistory>> getHistory(
			final List<ParticipantEntity> participants, final Configuration conf) {
		final Map<String, List<SurveyHistory>> back = new HashMap<String, List<SurveyHistory>>();
		for (final ParticipantEntity participant : participants) {
			back.put(participant.getToken(), this.getHistory(participant, conf));
		}
		return back;
	}

	private List<SurveyHistory> getHistory(
			final ParticipantEntity participantEntity, final Configuration conf) {
		try {
			final Set<Map<String, Object>> result = this
					.queryDB(
							"select t1.page as page,t1.timestamp as stamp from surveyhistory as t1 where t1.participant_id="
									+ participantEntity.getId()
									+ " order by t1.timestamp;", conf);
			if (result != null) {
				final List<SurveyHistory> back = new ArrayList<SurveyHistory>();
				final DateFormat format = new SimpleDateFormat(
						"yyyy-MM-dd HH:mm:ss.SSS");
				for (final Map<String, Object> entry : result) {
					final SurveyHistory historyEntry = new SurveyHistory();
					historyEntry.setPage(entry.get("page") + "");
					historyEntry.setParticipant(participantEntity);
					String stamp = entry.get("stamp") + "";
					// LOGGER.info("stamp {}",stamp);
					if (stamp.length() < 19)
						LOGGER.info("stam p too short {}", stamp);
					if (stamp.length() == 19)
						stamp = stamp + ".000";
					if (stamp.length() == 20)
						stamp = stamp + "000";
					if (stamp.length() == 21)
						stamp = stamp + "00";
					if (stamp.length() == 22)
						stamp = stamp + "0";
					if (stamp.length() > 23)
						stamp = stamp.substring(0, 23);
					historyEntry.setTimestamp(format.parse(stamp.trim()));
					back.add(historyEntry);
				}
				return back;
			}
		} catch (final Throwable e) {
			e.printStackTrace();
		}
		return null;
	}

	public Map<String, SurveyData> getData(final String partId,
			final Configuration conf) {
		try {
			final Set<Map<String, Object>> result = this
					.queryDB(
							"select t1.id as id, t1.value as value, t1.variablename as variablename from surveydata as t1 where t1.participant_id="
									+ partId + " order by t1.id;", conf);
			if (result != null) {
				final Map<String, SurveyData> back = new HashMap<String, SurveyData>();
				for (final Map<String, Object> entry : result) {
					final SurveyData dataEntry = new SurveyData();
					dataEntry.setValue(entry.get("value") + "");
					dataEntry.setVariableName(entry.get("variablename") + "");
					back.put(dataEntry.getVariableName(), dataEntry);
				}
				return back;
			}
		} catch (final Throwable e) {
			e.printStackTrace();
		}
		return null;
	}

	private Map<String, Map<String, String>> getValues(final String qmlPath) {
		final Map<String, Map<String, String>> back = new HashMap<String, Map<String, String>>();

		final NodeList usingNodes = XmlUtils.getInstance().getByXPath(qmlPath,
				"//parent::*[@variable]");
		for (int i = 0; i < usingNodes.getLength(); ++i) {
			final Node e = usingNodes.item(i);

			final NamedNodeMap nodeAttributes = e.getAttributes();
			final Node variableNode = nodeAttributes.getNamedItem("variable");
			final String nodeType = e.getNodeName();

			final String variable = variableNode.getTextContent();

			if (nodeType.equals("zofar:responseDomain")) {
				final NodeList answerNodes = XmlUtils.getInstance().getByXPath(
						e, "./*");
				back.putAll(this.helper1(back, variable, answerNodes));
			}
		}
		return back;
	}

	private Map<String, Map<String, String>> helper1(
			final Map<String, Map<String, String>> map, final String variable,
			final NodeList answerNodes) {
		for (int a = 0; a < answerNodes.getLength(); ++a) {
			final Node node = answerNodes.item(a);
			final String type = node.getNodeName();

			if (type.equals("zofar:answerOption")) {
				final NamedNodeMap answerAttributes = node.getAttributes();
				final Node answerValueNode = answerAttributes
						.getNamedItem("value");

				final Node answerUIDNode = answerAttributes.getNamedItem("uid");

				String answerValue = null;
				if (answerValueNode != null)
					answerValue = answerValueNode.getTextContent();
				String answerUID = null;
				if (answerUIDNode != null)
					answerUID = answerUIDNode.getTextContent();

				if (answerValue != null) {
					Map<String, String> answerOptions = null;
					if (map.containsKey(variable))
						answerOptions = map.get(variable);
					if (answerOptions == null)
						answerOptions = new HashMap<String, String>();
					answerOptions.put(answerUID, answerValue);
					map.put(variable, answerOptions);
				}
			} else {
				map.putAll(this.helper1(map, variable, XmlUtils.getInstance()
						.getByXPath(node, "./*")));
			}
		}
		return map;
	}

	private Map<String, String> getVariableTypes(final String qmlPath) {
		final Map<String, String> back = new HashMap<String, String>();
		final NodeList result = XmlUtils.getInstance().getByXPath(qmlPath,
				"/questionnaire/variables/*");

		for (int i = 0; i < result.getLength(); ++i) {
			final Node e = result.item(i);
			final NamedNodeMap attributes = e.getAttributes();
			final Node name = attributes.getNamedItem("name");
			final Node type = attributes.getNamedItem("type");
			back.put(name.getNodeValue(), type.getNodeValue());
		}
		return back;
	}

	private Map<String, String> getMissings(final String qmlPath,
			final String defaultMissing) {
		final Map<String, String> back = new HashMap<String, String>();

		final NodeList usingNodes = XmlUtils.getInstance().getByXPath(qmlPath,
				"//parent::*[@variable]");
		for (int i = 0; i < usingNodes.getLength(); ++i) {
			final Node e = usingNodes.item(i);

			final NamedNodeMap nodeAttributes = e.getAttributes();
			final Node variableNode = nodeAttributes.getNamedItem("variable");
			final String nodeType = e.getNodeName();

			final String variable = variableNode.getTextContent();

			if (nodeType.equals("zofar:responseDomain")) {
				final NodeList missingNodes = XmlUtils.getInstance()
						.getByXPath(e, "./*[@missing=\"true\"]");
				if (missingNodes.getLength() == 1) {
					final NamedNodeMap missingAttributes = missingNodes.item(0)
							.getAttributes();
					final Node missingValueNode = missingAttributes
							.getNamedItem("value");
					final String missingValue = missingValueNode
							.getTextContent();
					// LOGGER.info("found one missing with {} as value",missingValue);
					back.put(variable, missingValue);
				} else if (missingNodes.getLength() > 1) {
					for (int a = 0; a < missingNodes.getLength(); ++a) {
						final NamedNodeMap missingAttributes = missingNodes
								.item(a).getAttributes();
						final Node missingValueNode = missingAttributes
								.getNamedItem("value");
						final String missingValue = missingValueNode
								.getTextContent();
						if ((missingValue != null)
								&& (missingValue.equals(defaultMissing))) {
							// LOGGER.info("found missing with defaultMissing as value : {}",missingValue);
							back.put(variable, missingValue);
							break;
						}
					}
				} else {
					back.put(variable, "UNSELECTED");
				}
			}
			if (nodeType.equals("zofar:questionOpen"))
				back.put(variable, "EMPTY");
			if (nodeType.equals("zofar:question"))
				back.put(variable, "EMPTY");
			if (nodeType.equals("zofar:answerOption"))
				back.put(variable, "UNSET");
			if (nodeType.equals("zofar:left"))
				back.put(variable, "UNSELECTED");
			if (nodeType.equals("zofar:right"))
				back.put(variable, "UNSELECTED");
			if (nodeType.equals("zofar:variable"))
				back.put(variable, "NOT CALCULATED");
		}
		return back;
	}

	// public Set<Map<String, Object>> getExport(final String qmlPath) {
	// return getExport(qmlPath, getVariables(qmlPath), "EMPTY");
	// }
	//
	// public Set<Map<String, Object>> getExport(final String qmlPath,
	// final List<String> variables) {
	// return getExport(qmlPath, variables, "EMPTY");
	// }

	public Set<ParticipantType> getExport(final String qmlPath,
			final List<String> variables,
			final List<ParticipantEntity> participants,
			final Map<String, List<SurveyHistory>> historyMap,
			final String defaultMissing, final String notAnsweredMissing,
			final boolean all, final Configuration conf) {
		return this.getExport(qmlPath, variables, participants, historyMap,
				defaultMissing, notAnsweredMissing, all, false, conf);
	}

	public Set<ParticipantType> getExport(final String qmlPath,
			final List<String> variables,
			final List<ParticipantEntity> participants,
			final Map<String, List<SurveyHistory>> historyMap,
			final String defaultMissing, final String notAnsweredMissing,
			final boolean all, final boolean nometa, final Configuration conf) {

		// LOGGER.info("export : all={} nometa={}",all,nometa);
		if (participants != null) {
			final Set<ParticipantType> back = new HashSet<ParticipantType>();
			final Map<String, String> missings = this.getMissings(qmlPath,
					defaultMissing);
			final Map<String, Map<String, String>> values = this
					.getValues(qmlPath);

			final Map<String, String> types = this.getVariableTypes(qmlPath);

			final Iterator<ParticipantEntity> participantIt = participants
					.iterator();
			int total = participants.size();
			int lft = 1;
			while (participantIt.hasNext()) {
				final ParticipantEntity participant = participantIt.next();

				// final Map<String, SurveyData> data = participant
				// .getSurveyData();
				final Map<String, SurveyData> data = this.getData(
						participant.getId(), conf);
				// LOGGER.info("part : {} ({})",participant.getToken(),data.isEmpty());
				final List<SurveyHistory> history = historyMap.get(participant
						.getToken());
				
				LOGGER.info("part {} of {}",lft,total);
				lft = lft + 1;
				System.gc();

				if ((!all) && (data.isEmpty()))
					continue;

				final ParticipantType participant1 = ParticipantType.Factory
						.newInstance();
				participant1.setId(participant.getId() + "");
				participant1.setToken(participant.getToken());

				final DatasetType dataset = participant1.addNewDataset();

				if (!nometa) {
					final DataType token = dataset.addNewData();
					token.setVariable("participant");
					token.setStringValue(participant.getId() + "");
					token.setType("string");

					final DataType finished = dataset.addNewData();
					final DataType lastPageData = dataset.addNewData();
					boolean finishedSurvey = false;
					String lastPage = "UNKOWN";
					if ((history != null) && (!history.isEmpty())) {
						final Iterator<SurveyHistory> it = history.iterator();
						while (it.hasNext()) {
							final SurveyHistory tmp = it.next();
							if (tmp.getPage().toLowerCase()
									.equals("/end.xhtml")) {
								finishedSurvey = true;
								break;
							}
						}
						final SurveyHistory last = history
								.get(history.size() - 1);
						lastPage = last.getPage();
					}
					finished.setVariable("finished");
					finished.setStringValue(finishedSurvey + "");
					finished.setType("boolean");

					lastPageData.setVariable("lastPage");
					lastPageData.setStringValue(lastPage + "");
					lastPageData.setType("string");
				}

				final Iterator<String> variableIt = variables.iterator();
				while (variableIt.hasNext()) {
					final String variable = variableIt.next();
					final SurveyData value = data.get(variable);
					final DataType var = dataset.addNewData();
					var.setVariable(variable);
					var.setType(types.get(variable));
					if (value != null) {
						String valueStr = value.getValue();
						final Map<String, String> answerOptions = values
								.get(variable);
						if ((answerOptions != null)
								&& answerOptions.containsKey(value.getValue()))
							valueStr = answerOptions.get(value.getValue());
						if (valueStr.equals(""))
							valueStr = notAnsweredMissing;
						var.setStringValue(valueStr);
					} else {
						String missing = null;
						// Try to retrieve from qml
						if (missings.containsKey(variable))
							missing = missings.get(variable);
						if (missing == null)
							missing = defaultMissing;
						var.setStringValue(missing);
					}
				}
				back.add(participant1);
			}
			return back;
		}
		return null;
	}

}

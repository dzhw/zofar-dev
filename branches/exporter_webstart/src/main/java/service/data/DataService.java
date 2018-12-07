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
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

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

import com.google.common.collect.Lists;

import de.his.export.xml.export.DataType;
import de.his.export.xml.export.DatasetType;
import de.his.export.xml.export.HistoryType;
import de.his.export.xml.export.HistorysetType;
import de.his.export.xml.export.ParticipantType;
import eu.dzhw.zofar.management.utils.bool.DecisionClient;
import eu.dzhw.zofar.management.utils.objects.CollectionClient;
import eu.dzhw.zofar.management.utils.xml.XmlClient;
import model.ParticipantEntity;
import model.SurveyData;
import model.SurveyHistory;
import support.elements.conditionEvaluation.NavigatorBean;
import support.elements.conditionEvaluation.SessionControllerBean;
import support.elements.conditionEvaluation.VariableBean;
import support.elements.conditionEvaluation.ZofarBean;

/**
 * The Class DataService.
 */
public class DataService {

	/** The Constant LOGGER. */
	private static final Logger LOGGER = LoggerFactory.getLogger(DataService.class);

	/** The instance. */
	private static DataService INSTANCE;

	/** The session factory. */
	private SessionFactory sessionFactory;

	/** The configuration. */
	private Configuration configuration;

	/**
	 * The Class CustomWork.
	 */
	private class CustomWork implements Work {

		/** The back. */
		private final Set<Map<String, Object>> back;

		/** The query. */
		private final String query;

		/**
		 * Instantiates a new custom work.
		 *
		 * @param back
		 *            the back
		 * @param query
		 *            the query
		 */
		public CustomWork(final Set<Map<String, Object>> back, final String query) {
			super();
			this.back = back;
			this.query = query;

		}

		/**
		 * Gets the back.
		 *
		 * @return the back
		 */
		public Set<Map<String, Object>> getBack() {
			return this.back;
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see org.hibernate.jdbc.Work#execute(java.sql.Connection)
		 */
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
							final String name = rs.getMetaData().getColumnName(a);
							final String value = rs.getString(a);
							row.put(name, value);
						}

						this.back.add(row);
					}
				}

			} finally {
				if (st != null) {
					st.close();
				}
			}
		}
	};

	/**
	 * Instantiates a new data service.
	 */
	private DataService() {
		super();
	}

	/**
	 * Gets the single instance of DataService.
	 *
	 * @return single instance of DataService
	 */
	public static DataService getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new DataService();
		}
		return INSTANCE;
	}

	/**
	 * Gets the configuration.
	 *
	 * @return the configuration
	 */
	public Configuration getConfiguration() {
		if (this.configuration == null) {
			this.configuration = new Configuration();
			this.configuration.configure("database.cfg.xml");
		}
		return this.configuration;
	}

	/**
	 * Gets the session factory.
	 *
	 * @param conf
	 *            the conf
	 * @return the session factory
	 * @throws Throwable
	 *             the throwable
	 */
	private SessionFactory getSessionFactory(final Configuration conf) throws Throwable {
		final ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder().applySettings(conf.getProperties())
				.build();
		return conf.buildSessionFactory(serviceRegistry);
	}

	/**
	 * Gets the session.
	 *
	 * @param conf
	 *            the conf
	 * @return the session
	 * @throws Throwable
	 *             the throwable
	 */
	private Session getSession(final Configuration conf) throws Throwable {
		if (this.sessionFactory == null) {
			this.sessionFactory = this.getSessionFactory(conf);
		}
		if (this.sessionFactory != null) {
			return this.sessionFactory.openSession();
		}
		return null;
	}

	/**
	 * Query db.
	 *
	 * @param queryStr
	 *            the query str
	 * @param conf
	 *            the conf
	 * @return the sets the
	 * @throws Throwable
	 *             the throwable
	 */
	private Set<Map<String, Object>> queryDB(final String queryStr, final Configuration conf) throws Throwable {
		final Session session = this.getSession(conf);
		if (session != null) {
			final Transaction transaction = session.beginTransaction();
			try {
				final CustomWork work = new CustomWork(new LinkedHashSet<Map<String, Object>>(), queryStr);
				session.doWork(work);
				transaction.commit();
				return work.getBack();
			} catch (final Throwable t) {
				LOGGER.info("Creating Hibernate Transaction failed {}", t.getCause());
				transaction.rollback();
				throw t;
			} finally {
				if ((session != null) && (session.isOpen())) {
					session.close();
				}
			}
		} else {
			LOGGER.info("Creating Hibernate Session failed");
		}
		return null;
	}

	/**
	 * Gets the participants count.
	 *
	 * @param conf
	 *            the conf
	 * @return the participants count
	 * @throws Throwable
	 */
	public int getParticipantsCount(final Configuration conf, final boolean noEmpties) throws Throwable {
		// return 1;
		Set<Map<String, Object>> result = null;
		if (!noEmpties) {
			result = this.queryDB("select id from participant;", conf);
		} else {
			result = this.queryDB(
					"select id from participant where id in (select distinct participant_id from surveyhistory);",
					conf);
		}
		if (result != null) {
			return result.size();
		}
		return 0;
	}

	/**
	 * Gets the participants.
	 *
	 * @param conf
	 *            the conf
	 * @param startIndex
	 *            the start index
	 * @param count
	 *            the count
	 * @return the participants
	 * @throws Throwable
	 */
	// xx
	public List<ParticipantEntity> getParticipants(final Configuration conf, final int startIndex, final int count,
			final boolean noEmpties) throws Throwable {
		String limit = "ALL";
		if (count > 0) {
			limit = count + "";
		}
		Set<Map<String, Object>> result = null;
		if (!noEmpties) {
			result = this.queryDB("select id,password,token from participant order by id OFFSET " + startIndex
					+ " LIMIT " + limit + ";", conf);
		} else {
			result = this.queryDB(
					"select id,password,token from participant where id in (select distinct participant_id from surveyhistory) order by id OFFSET "
							+ startIndex + " LIMIT " + limit + ";",
					conf);
		}
		if (result != null) {
			System.out.println("Loading " + result.size() + " Participants");
			final List<ParticipantEntity> back = new ArrayList<ParticipantEntity>();
			for (final Map<String, Object> entry : result) {
				final ParticipantEntity partEntry = new ParticipantEntity();
				partEntry.setId(entry.get("id") + "");
				partEntry.setPassword(entry.get("password") + "");
				partEntry.setToken(entry.get("token") + "");
				partEntry.setSurveyData(this.getData(entry.get("id") + "", conf));
				back.add(partEntry);
				System.out.print(".");
			}
			System.out.println();
			return back;
		}

		return null;
	}

	/**
	 * Gets the participants.
	 *
	 * @param conf
	 *            the conf
	 * @return the participants
	 * @throws Throwable
	 */
	public List<ParticipantEntity> getParticipants(final Configuration conf, final boolean noEmpties) throws Throwable {
		return this.getParticipants(conf, 0, 0, noEmpties);
	}

	/**
	 * Gets the history.
	 *
	 * @param participants
	 *            the participants
	 * @param conf
	 *            the conf
	 * @return the history
	 * @throws Throwable
	 */

	// xx
	public Map<String, List<SurveyHistory>> getHistory(final List<ParticipantEntity> participants,
			final Configuration conf) throws Throwable {
		final Map<String, List<SurveyHistory>> back = new LinkedHashMap<String, List<SurveyHistory>>();
		for (final ParticipantEntity participant : participants) {
			back.put(participant.getToken(), this.getHistory(participant, conf));
		}
		return back;
	}

	/**
	 * Gets the history.
	 *
	 * @param participantEntity
	 *            the participant entity
	 * @param conf
	 *            the conf
	 * @return the history
	 * @throws Throwable
	 */
	private List<SurveyHistory> getHistory(final ParticipantEntity participantEntity, final Configuration conf)
			throws Throwable {
		final Set<Map<String, Object>> result = this.queryDB(
				"select t1.id as id, t1.page as page,t1.timestamp as stamp from surveyhistory as t1 where t1.participant_id="
						+ participantEntity.getId() + " order by t1.timestamp;",
				conf);
		if (result != null) {
			final List<SurveyHistory> back = new ArrayList<SurveyHistory>();
			final DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
			for (final Map<String, Object> entry : result) {
				final SurveyHistory historyEntry = new SurveyHistory();
				historyEntry.setId(entry.get("id") + "");
				historyEntry.setPage(entry.get("page") + "");
				historyEntry.setParticipant(participantEntity);
				String stamp = entry.get("stamp") + "";
				if (stamp.length() < 19) {
					LOGGER.info("stamp too short {}", stamp);
				}
				if (stamp.length() == 19) {
					stamp = stamp + ".000";
				}
				if (stamp.length() == 20) {
					stamp = stamp + "000";
				}
				if (stamp.length() == 21) {
					stamp = stamp + "00";
				}
				if (stamp.length() == 22) {
					stamp = stamp + "0";
				}
				if (stamp.length() > 23) {
					stamp = stamp.substring(0, 23);
				}
				historyEntry.setTimestamp(format.parse(stamp.trim()));
				back.add(historyEntry);
				// System.out.print("*");
			}
			return back;
		}
		return null;
	}

	/**
	 * Gets the data.
	 *
	 * @param partId
	 *            the part id
	 * @param conf
	 *            the conf
	 * @return the data
	 * @throws Throwable
	 */
	// xx
	private Map<String, SurveyData> getData(final String partId, final Configuration conf) throws Throwable {
		final Set<Map<String, Object>> result = this.queryDB(
				"select t1.id as id, t1.value as value, t1.variablename as variablename from surveydata as t1 where t1.participant_id="
						+ partId + " order by t1.id;",
				conf);
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
		return null;
	}

	/**
	 * Gets the values.
	 *
	 * @param qmlPath
	 *            the qml path
	 * @return the values
	 * @throws Exception
	 */
	public Map<String, Map<String, String>> getValues(final String qmlPath, final NodeList usingNodes)
			throws Exception {
		final Map<String, Map<String, String>> back = new HashMap<String, Map<String, String>>();

		// final NodeList usingNodes = XmlClient.getInstance().getByXPath(qmlPath,
		// "//*[@variable]");
		for (int i = 0; i < usingNodes.getLength(); ++i) {
			final Node e = usingNodes.item(i);
			if (XmlClient.getInstance().hasParent(e, "zofar:researchdata")) {
				continue;
			}
			final NamedNodeMap nodeAttributes = e.getAttributes();
			final Node variableNode = nodeAttributes.getNamedItem("variable");
			final String nodeType = e.getNodeName();

			final String variable = variableNode.getTextContent();

			if (nodeType.equals("zofar:responseDomain")) {
				final NodeList answerNodes = XmlClient.getInstance().getByXPath(e, "./*");
				back.putAll(this.helper1(back, variable, answerNodes));
			}
			if (nodeType.equals("zofar:left") || nodeType.equals("zofar:right")) {
				final NodeList answerNodes = XmlClient.getInstance().getByXPath(e, "./*");
				back.putAll(this.helper1(back, variable, answerNodes));
			}
		}
		return back;
	}

	/**
	 * Helper1.
	 *
	 * @param map
	 *            the map
	 * @param variable
	 *            the variable
	 * @param answerNodes
	 *            the answer nodes
	 * @return the map
	 */
	private Map<String, Map<String, String>> helper1(final Map<String, Map<String, String>> map, final String variable,
			final NodeList answerNodes) {
		for (int a = 0; a < answerNodes.getLength(); ++a) {
			final Node node = answerNodes.item(a);
			if (XmlClient.getInstance().hasParent(node, "zofar:researchdata")) {
				continue;
			}
			final String type = node.getNodeName();

			if (type.equals("zofar:answerOption") || type.equals("zofar:SlotItem")) {
				final NamedNodeMap answerAttributes = node.getAttributes();
				final Node answerValueNode = answerAttributes.getNamedItem("value");

				final Node answerUIDNode = answerAttributes.getNamedItem("uid");

				String answerValue = null;
				if (answerValueNode != null) {
					answerValue = answerValueNode.getTextContent();
				}
				String answerUID = null;
				if (answerUIDNode != null) {
					answerUID = answerUIDNode.getTextContent();
				}

				if (answerValue != null) {
					Map<String, String> answerOptions = null;
					if (map.containsKey(variable)) {
						answerOptions = map.get(variable);
					}
					if (answerOptions == null) {
						answerOptions = new HashMap<String, String>();
					}
					answerOptions.put(answerUID, answerValue);
					map.put(variable, answerOptions);
				}
			} else {
				map.putAll(this.helper1(map, variable, XmlClient.getInstance().getByXPath(node, "./*")));
			}
		}
		return map;
	}

	/**
	 * Gets the variable types.
	 *
	 * @param qmlPath
	 *            the qml path
	 * @return the variable types
	 * @throws Exception
	 */
	public Map<String, String> getVariableTypes(final String qmlPath, final NodeList usingNodes) throws Exception {
		// Indexing questions with variable and type attribute

		final Map<String, String> indexedTypedQuestions = new HashMap<String, String>();
		for (int i = 0; i < usingNodes.getLength(); ++i) {
			final Node e = usingNodes.item(i);
			if (XmlClient.getInstance().hasParent(e, "zofar:researchdata")) {
				continue;
			}

			if (!e.hasAttributes()) {
				continue;
			}
			final NamedNodeMap nodeAttributes = e.getAttributes();
			final Node variableNode = nodeAttributes.getNamedItem("variable");
			final Node typeNode = nodeAttributes.getNamedItem("type");
			if (variableNode == null) {
				continue;
			}
			if (typeNode == null) {
				continue;
			}

			final String variable = variableNode.getTextContent();
			final String type = typeNode.getTextContent();
			indexedTypedQuestions.put(variable, type);
		}

		System.out.println("indexed questions with type attribute : " + indexedTypedQuestions.size());
		final Map<String, String> back = new HashMap<String, String>();
		final NodeList result = XmlClient.getInstance().getByXPath(qmlPath, "/questionnaire/variables/*");
		final int count1 = result.getLength();
		for (int i = 0; i < count1; ++i) {
			final Node e = result.item(i);
			if (XmlClient.getInstance().hasParent(e, "zofar:researchdata")) {
				continue;
			}
			final NamedNodeMap attributes = e.getAttributes();
			final Node name = attributes.getNamedItem("name");
			final Node type = attributes.getNamedItem("type");
			final String nameValue = name.getNodeValue();

			String typeValue = type.getNodeValue();
			if ((typeValue != null) && (typeValue.equals("string"))) {
				// // Search for question. maybe its number or grade. then set type
				// // to number
				// final NodeList subResult = XmlClient.getInstance().getByXPath(qmlPath,
				// "//question[@variable='" + nameValue + "' and @type]");
				// final int count2 = subResult.getLength();
				// for (int j = 0; j < count2; ++j) {
				// final Node tmp = subResult.item(j);
				// if (XmlUtils.getInstance().hasParent(tmp, "zofar:researchdata"))
				// continue;
				// final NamedNodeMap tmpAttributes = tmp.getAttributes();
				// final Node subType = tmpAttributes.getNamedItem("type");
				// typeValue = subType.getNodeValue();
				// break;
				// }
				if (indexedTypedQuestions.containsKey(nameValue)) {
					typeValue = indexedTypedQuestions.get(nameValue);
				}
			}

			back.put(nameValue, typeValue);
		}
		return back;
	}

	/**
	 * Gets the missings.
	 *
	 * @param qmlPath
	 *            the qml path
	 * @param defaultMissing
	 *            the default missing
	 * @return the missings
	 * @throws Exception
	 */
	public Map<String, String> getMissings(final String qmlPath, final NodeList usingNodes) throws Exception {
		final Map<String, String> back = new HashMap<String, String>();

		// final NodeList usingNodes = XmlClient.getInstance().getByXPath(qmlPath,
		// "//*[@variable]");
		for (int i = 0; i < usingNodes.getLength(); ++i) {
			final Node e = usingNodes.item(i);
			if (XmlClient.getInstance().hasParent(e, "zofar:researchdata")) {
				continue;
			}
			final NamedNodeMap nodeAttributes = e.getAttributes();
			final Node variableNode = nodeAttributes.getNamedItem("variable");
			final String nodeType = e.getNodeName();

			final String variable = variableNode.getTextContent();

			if (nodeType.equals("zofar:responseDomain")) {
				back.put(variable, "UNSELECTED");
			} else if (nodeType.equals("zofar:questionOpen")) {
				back.put(variable, "EMPTY");
			} else if (nodeType.equals("zofar:attachedOpen")) {
				back.put(variable, "EMPTY");
			} else if (nodeType.equals("zofar:question")) {
				back.put(variable, "EMPTY");
			} else if (nodeType.equals("zofar:answerOption")) {
				back.put(variable, "UNSET");
			} else if (nodeType.equals("zofar:SlotItem")) {
				back.put(variable, "UNSET");
			} else if (nodeType.equals("zofar:left")) {
				back.put(variable, "UNSELECTED");
			} else if (nodeType.equals("zofar:right")) {
				back.put(variable, "UNSELECTED");
			} else if (nodeType.equals("zofar:variable")) {
				back.put(variable, "NOT CALCULATED");
			} else if (nodeType.equals("zofar:preloadItem")) {
				back.put(variable, "NOT DEFINED");
			} else {
				LOGGER.info("unhandled Type {}", nodeType);
			}
		}
		return back;
	}

	/**
	 * Gets the export.
	 *
	 * @param qmlPath
	 *            the qml path
	 * @param back
	 *            the back
	 * @param variables
	 *            the variables
	 * @param participants
	 *            the participants
	 * @param historyMap
	 *            the history map
	 * @param defaultMissing
	 *            the default missing
	 * @param all
	 *            the all
	 * @param nometa
	 *            the nometa
	 * @param conf
	 *            the conf
	 * @return the export
	 * @throws Exception
	 */
	public Set<ParticipantType> getExport(final String qmlPath, Set<ParticipantType> back, final List<String> variables,
			final List<ParticipantEntity> participants, final Map<String, List<SurveyHistory>> historyMap,
			final boolean all, final boolean nometa, final Configuration conf, final Map<String, String> missings,
			final Map<String, Map<String, String>> values, final Map<String, List<String>> variablePages,
			final Map<String, List<List<String>>> variableVisibles, final Map<String, String> types,
			final Map<String, List<String>> pageTree) throws Exception {
		if (participants != null) {
			if (back == null) {
				back = new HashSet<ParticipantType>();
			}
			
			final DateFormat formatter = new SimpleDateFormat();

			final DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
			final DecisionClient decisionClient = DecisionClient.getInstance();

			final Iterator<ParticipantEntity> participantIt = participants.iterator();
			final int total = participants.size();
			int lft = 1;

			while (participantIt.hasNext()) {
				final ParticipantEntity participant = participantIt.next();

				final Map<String, SurveyData> data = participant.getSurveyData();
//				final List<SurveyHistory> history = historyMap.get(participant.getToken());

				System.out.println("part " + lft + " of " + total);
				lft = lft + 1;

				if ((!all) && (data.isEmpty())) {
					continue;
				}
				
				final List<SurveyHistory> history = historyMap.get(participant.getToken());

				final ParticipantType participant1 = ParticipantType.Factory.newInstance();
				participant1.setId(participant.getId() + "");
				participant1.setToken(participant.getToken());

				final DatasetType dataset = participant1.addNewDataset();

				final DataType lastPageData = dataset.addNewData();
				final DataType lastContactData = dataset.addNewData();

				String lastPage = "UNKNOWN";
				String lastContact = "UNKNOWN";
				String firstContact = "UNKNOWN";
				if ((history != null) && (!history.isEmpty())) {

					final SurveyHistory last = history.get(history.size() - 1);
					lastPage = last.getPage();
					lastContact = formatter.format(last.getTimestamp());

					final SurveyHistory first = history.get(0);
					firstContact = formatter.format(first.getTimestamp());
				}

				if (!nometa) {
					final DataType firstContactData = dataset.addNewData();
					firstContactData.setVariable("firstContact");
					firstContactData.setStringValue(firstContact + "");
					firstContactData.setType("string");
				}

				lastPageData.setVariable("lastPage");
				lastPageData.setStringValue(lastPage + "");
				lastPageData.setType("string");

				lastContactData.setVariable("lastContact");
				lastContactData.setStringValue(lastContact + "");
				lastContactData.setType("string");

				// check if reached end - Page
				final DataType finished = dataset.addNewData();
				boolean finishedSurvey = false;

				if ((history != null) && (!history.isEmpty())) {
					final Iterator<SurveyHistory> it = history.iterator();
					while (it.hasNext()) {
						final SurveyHistory tmp = it.next();
						if (tmp.getPage().toLowerCase().equals("/end.xhtml")) {
							finishedSurvey = true;
							break;
						}
					}
				}
				finished.setVariable("finished");
				finished.setStringValue(finishedSurvey + "");
				finished.setType("boolean");

				final List<String> intersectFromHistory = new ArrayList<String>();
				final List<String> pagesFromHistory = new ArrayList<String>();

				if ((history != null) && (!history.isEmpty())) {
					// System.out.println("create history data... ");
					final HistorysetType historySet = participant1.addNewHistoryset();
					final Iterator<SurveyHistory> it = history.iterator();

					while (it.hasNext()) {
						final SurveyHistory tmp = it.next();
						final HistoryType historyItem = historySet.addNewHistory();
						historyItem.setId(tmp.getId());
						historyItem.setPid(tmp.getParticipant().getId());
						historyItem.setToken(tmp.getParticipant().getToken());
						historyItem.setId(tmp.getId());
						historyItem.setStamp(df.format(tmp.getTimestamp()));
						String cleanedPageStr = tmp.getPage();
						cleanedPageStr = cleanedPageStr.replaceAll(Pattern.quote("/"), "");
						cleanedPageStr = cleanedPageStr.replaceAll(Pattern.quote(".xhtml"), "");
						historyItem.setStringValue(cleanedPageStr);
						if (!intersectFromHistory.contains(tmp.getPage())) {
							intersectFromHistory.add(tmp.getPage());
						}
						pagesFromHistory.add(tmp.getPage());
					}
					// System.out.println("create history data... done");
				}

				// ***********************
				final Iterator<String> variableIt = variables.iterator();
				while (variableIt.hasNext()) {
					final String variable = variableIt.next();

					final String variableType = types.get(variable);
					final SurveyData value = data.get(variable);

					final DataType var = dataset.addNewData();

					var.setVariable(variable);
					var.setType(variableType);

					final List<String> pageList = variablePages.get(variable);
					final List<String> tmpList = CollectionClient.getInstance().intersection(pageList,
							intersectFromHistory);

					if (value != null) {
						String valueStr = value.getValue();
						final Map<String, String> answerOptions = values.get(variable);
						if ((answerOptions != null) && answerOptions.containsKey(value.getValue())) {
							valueStr = answerOptions.get(value.getValue());
						}

						// check if variable has default value
						boolean isDefaultValue = false;
						if (variableType != null) {
							if (variableType.equals("string")) {
								if (valueStr.equals("")) {
									isDefaultValue = true;
								}
							} else if (variableType.equals("singleChoiceAnswerOption")) {
								if (valueStr.equals("")) {
									isDefaultValue = true;
								}
							} else if (variableType.equals("boolean")) {
								if (valueStr.equals("false")) {
									isDefaultValue = true;
								}
							} else if (variableType.equals("number")) {
								if (valueStr.equals("")) {
									isDefaultValue = true;
								}
							} else if (variableType.equals("grade")) {
								if (valueStr.equals("")) {
									isDefaultValue = true;
								}
							} else {
								LOGGER.error("unhandeled variableType : " + variableType);
							}
						}

						// if (variableType.equals("singleChoiceAnswerOption")&&(isDefaultValue)){
						// System.out.println("Default SC : "+variable);
						// }

						// isDefaultValue = false;
						if (isDefaultValue) {
							// Check for QML defined Variables. Not for Pretest
							// Comments
							if (((pageList != null) && (!pageList.isEmpty()))
									&& ((tmpList == null) || (tmpList.size() == 0))) {
								// question's page was not visited
								valueStr = "INITFORGET1";
							} else if (((pageList != null) && (!pageList.isEmpty()))
									&& ((tmpList != null) && (tmpList.size() > 0))) {
								// question's page was visited
								// check if question was visible
								final List<List<String>> visiblesList = variableVisibles.get(variable);
								if ((visiblesList != null) && (!visiblesList.isEmpty())) {
									// Context
									final Map<String, Object> contextData = new HashMap<String, Object>();
									contextData.put("zofar", new ZofarBean(missings, values));
									contextData.put("sessionController", new SessionControllerBean(participant));

									for (final Map.Entry<String, SurveyData> item : data.entrySet()) {
										final String variableName = item.getKey();
										final SurveyData valueObj = item.getValue();
										final String type = types.get(variableName);

										if (type != null) {
											try {
												if (type.equals("singleChoiceAnswerOption")) {
													final String tmp = valueObj.getValue();
													contextData.put(variableName,
															new VariableBean(variableName, tmp, tmp));
												} else if (type.equals("boolean")) {
													final boolean tmp = Boolean.parseBoolean(valueObj.getValue());
													contextData.put(variableName,
															new VariableBean(variableName, tmp, tmp));
												} else if (type.equals("string")) {
													final String tmp = valueObj.getValue();
													contextData.put(variableName,
															new VariableBean(variableName, tmp, tmp));
												} else if (type.equals("number")) {
													String str = valueObj.getValue();
													if (str.equals("") || str.equals(".")) {
														str = Double.MIN_VALUE + "";
													}
													final Double tmp = Double.parseDouble(str);
													contextData.put(variableName,
															new VariableBean(variableName, tmp, tmp));
												} else if (type.equals("grade")) {
													String str = valueObj.getValue();
													if (str.equals("") || str.equals(".")) {
														str = Double.MIN_VALUE + "";
													}
													final Double tmp = Double.parseDouble(str);
													contextData.put(variableName,
															new VariableBean(variableName, tmp, tmp));
												} else if (type.equals("preload")) {
													final String tmp = valueObj.getValue();
													contextData.put(variableName,
															new VariableBean(variableName, tmp, tmp));
												} else {
													LOGGER.error("unhandeled Type : " + type);
												}
											} catch (final Exception e) {
												LOGGER.error(
														"Error while Context Build for Visibility-Check ({}) : {} ",
														participant.getToken() + " , " + variableName, e);
												throw new Exception("Error while Context Build for Visibility-Check ("
														+ participant.getToken() + " , " + variableName + ") : " + e
														+ " ");
											}
										}
									}
									// ##
									// boolean debug = variable.startsWith("mo37");

									// if(debug)LOGGER.info("Variable : "+variable);
									contextData.put("navigatorBean",
											new NavigatorBean(pageList, pagesFromHistory, pageTree));
									boolean mergedFlag = false;
									for (final List<String> conditionPath : visiblesList) {
										boolean pathFlag = true;
										for (final String condition : conditionPath) {

											final boolean flag = decisionClient.evaluateSpel("" + condition + "",
													contextData);
											// if(debug)LOGGER.info("condition : "+condition+" => "+flag);

											if (!flag) {
												// One cascaded visible
												// condition is
												// false ==> element was NOT
												// visible
												// valueStr = "INITFORGET2";
												pathFlag = false;
												break;
											} else {
												// Element was visible. ==> not
												// answered
												// if (!variableType.equals("boolean"))
												// valueStr = "NOT ANSWERED";
											}
										}
										// if(debug)LOGGER.info("pathFlag : "+pathFlag);
										mergedFlag = mergedFlag | pathFlag;
									}
									// if(debug)LOGGER.info("mergedFlag : "+mergedFlag);
									if (!mergedFlag) {
										// One cascaded visible condition is
										// false ==> element was NOT visible
										valueStr = "INITFORGET2";
									} else {
										// Element was visible. ==> not
										// answered
										if (!variableType.equals("boolean")) {
											valueStr = "NOT ANSWERED";
										}
									}
									// ##

								} else {
									// Element was visible. ==> not answered
									if (valueStr.equals("")) {
										valueStr = "NOT ANSWERED";
									}
								}
							} else {
								// There is no page with this question on it
								valueStr = "NOPAGE";
							}
						} else {
							// Element was visible. ==> take original value
						}

						var.setStringValue(valueStr);
					} else {
						// no value setted ==> choose missing
						String missing = null;
						// Try to retrieve from qml
						if (missings.containsKey(variable)) {
							boolean pageVisited = false;
							if ((history != null) && (!history.isEmpty())) {
								if ((tmpList != null) && (tmpList.size() > 0)) {
									pageVisited = true;
								}
							}
							if (pageVisited) {
								missing = missings.get(variable);
							} else {
								missing = "NOTVISITED";
							}
						}

						if (missing == null) {
							// missing = defaultMissing;
							missing = "DEFAULT";
							if (LOGGER.isDebugEnabled()) {
								LOGGER.info("set {} to default-Missing", var.getVariable());
							}
						}
						var.setStringValue(missing);
					}

				}
				// ***********************
				// System.out.println("create survey data... done");

				back.add(participant1);
			}
			// -----------------------
			return back;
		}
		return null;
	}

	public Map<String, List<String>> retrieveVisiblesOld(final String qmlPath, final NodeList usingNodes)
			throws Exception {
		final Map<String, List<String>> back = new HashMap<String, List<String>>();

		for (int i = 0; i < usingNodes.getLength(); ++i) {
			final Node e = usingNodes.item(i);
			if (XmlClient.getInstance().hasParent(e, "zofar:researchdata")) {
				continue;
			}
			final NamedNodeMap nodeAttributes = e.getAttributes();
			final Node variableNode = nodeAttributes.getNamedItem("variable");
			final String variable = variableNode.getTextContent();

			Node parentNode = e;
			String parentType = parentNode.getNodeName();

			while (parentType.startsWith("zofar:")) {
				List<String> visibles = null;
				if (back.containsKey(variable)) {
					visibles = back.get(variable);
				}
				if (visibles == null) {
					visibles = new ArrayList<String>();
				}

				parentNode = parentNode.getParentNode();
				parentType = parentNode.getNodeName();

				if (parentType.equals("zofar:page")) {
					break;
				}

				final NamedNodeMap attributes = parentNode.getAttributes();
				if (attributes != null) {
					final Node visibleNode = attributes.getNamedItem("visible");

					if (visibleNode != null) {

						Lists.reverse(visibles);
						final String visibleCondition = visibleNode.getTextContent();
						visibles.add(visibleCondition);
						Lists.reverse(visibles);
					}
				}

				back.put(variable, visibles);
			}
		}
		return back;
	}

	public Map<String, List<List<String>>> retrieveVisibles(final String qmlPath, final NodeList usingNodes)
			throws Exception {
		final Map<String, List<List<String>>> back = new HashMap<String, List<List<String>>>();

		for (int i = 0; i < usingNodes.getLength(); ++i) {
			final Node e = usingNodes.item(i);
			if (XmlClient.getInstance().hasParent(e, "zofar:researchdata")) {
				continue;
			}
			final NamedNodeMap nodeAttributes = e.getAttributes();
			final Node variableNode = nodeAttributes.getNamedItem("variable");
			final String variable = variableNode.getTextContent();

			List<List<String>> variableList = null;
			if (back.containsKey(variable)) {
				variableList = back.get(variable);
			}
			if (variableList == null) {
				variableList = new ArrayList<List<String>>();
			}

			Node parentNode = e;
			String parentType = parentNode.getNodeName();

			final List<String> visibles = new ArrayList<String>();

			while (parentType.startsWith("zofar:")) {
				parentNode = parentNode.getParentNode();
				parentType = parentNode.getNodeName();

				if (parentType.equals("zofar:page")) {
					break;
				}

				final NamedNodeMap attributes = parentNode.getAttributes();
				if (attributes != null) {
					final Node visibleNode = attributes.getNamedItem("visible");

					if (visibleNode != null) {
						Lists.reverse(visibles);
						final String visibleCondition = visibleNode.getTextContent();
						visibles.add(visibleCondition);
						Lists.reverse(visibles);
					}
				}
			}
			variableList.add(visibles);
			back.put(variable, variableList);
		}
		return back;
	}

	public Map<String, List<String>> pageTree(final String qmlPath) throws Exception {
		final Map<String, List<String>> back = new HashMap<String, List<String>>();

		final NodeList result = XmlClient.getInstance().getByXPath(qmlPath, "page");
		for (int i = 0; i < result.getLength(); ++i) {
			final Node e = result.item(i);
			if (XmlClient.getInstance().hasParent(e, "zofar:researchdata")) {
				continue;
			}
			final NamedNodeMap attributes = e.getAttributes();
			final Node name = attributes.getNamedItem("uid");

			final NodeList transitionList = XmlClient.getInstance().getByXPath(e, "transitions/*");
			for (int j = 0; j < transitionList.getLength(); ++j) {
				final Node transition = transitionList.item(j);
				final NamedNodeMap transitionAttributes = transition.getAttributes();
				final Node target = transitionAttributes.getNamedItem("target");
				List<String> edgeSet = null;

				if (edgeSet == null) {
					edgeSet = new ArrayList<String>();
				}
				edgeSet.add(target.getNodeValue());
				back.put(name.getNodeValue(), edgeSet);
			}
		}
		return back;
	}

	public Map<String, List<String>> retrievePages(final String qmlPath, final NodeList usingNodes) throws Exception {
		final Map<String, List<String>> back = new HashMap<String, List<String>>();

		// final NodeList usingNodes = XmlClient.getInstance().getByXPath(qmlPath,
		// "//*[@variable]");
		for (int i = 0; i < usingNodes.getLength(); ++i) {
			final Node e = usingNodes.item(i);
			if (XmlClient.getInstance().hasParent(e, "zofar:researchdata")) {
				continue;
				// final String nodeType = e.getNodeName();
			}

			final NamedNodeMap nodeAttributes = e.getAttributes();
			final Node variableNode = nodeAttributes.getNamedItem("variable");
			final String variable = variableNode.getTextContent();

			Node parentNode = e;
			String parentType = parentNode.getNodeName();

			while (parentType.startsWith("zofar:")) {
				parentNode = parentNode.getParentNode();
				parentType = parentNode.getNodeName();

				if (parentType.equals("zofar:page")) {
					List<String> pages = null;
					if (back.containsKey(variable)) {
						pages = back.get(variable);
					}
					if (pages == null) {
						pages = new ArrayList<String>();
					}

					final NamedNodeMap pageAttributes = parentNode.getAttributes();
					final Node uidNode = pageAttributes.getNamedItem("uid");
					final String uid = uidNode.getTextContent();
					pages.add("/" + uid + ".xhtml");
					back.put(variable, pages);
					break;
				}
			}
		}
		return back;
	}

}

package service.statistics;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.Properties;
import java.util.Set;

import model.HeaderEntry;
import model.ParticipantEntity;
import model.SurveyHistory;
import model.TransitionEntry;
import model.ValueEntry;

import org.hibernate.cfg.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import service.codebook.CodebookService;
import service.counting.CountingService;
import service.data.DataService;
import service.feedback.FeedbackService;
import service.navigation.NavigationGraphService;
import utils.ConfigurationUtils;
import utils.StringUtils;
import utils.XmlUtils;
import de.his.export.xml.export.ParticipantType;
//import de.his.zofar.persistence.surveyengine.entities.ParticipantEntity;
//import de.his.zofar.service.surveyengine.model.SurveyHistory;

public class StatisticService extends Observable {

	private static StatisticService INSTANCE;
	private static final Logger LOGGER = LoggerFactory
			.getLogger(StatisticService.class);

	public enum TYPE {
		instruction, data, options, types, codebook, counting, transition, feedback
	}

	private StatisticService() {
		super();
	}

	public static StatisticService getInstance() {
		if (INSTANCE == null)
			INSTANCE = new StatisticService();
		return INSTANCE;
	}

	@Override
	public synchronized void addObserver(final Observer o) {
		super.addObserver(o);
		this.setChanged();
		this.notifyObservers("phases=6");
	}

	public Map<TYPE, Object> create(final Configuration conf) {
		final Properties system = ConfigurationUtils.getInstance()
				.getConfiguration("System.properties");
		return this.create(system.getProperty("qml.location"), conf);
	}

	public Map<TYPE, Object> create(final String qmlLocation,
			final Configuration conf) {
		LOGGER.info("qml location {}", qmlLocation);
		final List<String> variables = this.getVariables(qmlLocation);

		final List<String> filteredVariables = new ArrayList<String>();
		filteredVariables.addAll(variables);

		final DataService dataService = DataService.getInstance();
		final CodebookService codebookService = CodebookService.getInstance();
		final NavigationGraphService navigationService = NavigationGraphService
				.getInstance();
		final CountingService countingService = CountingService.getInstance();
		final FeedbackService feedbackService = FeedbackService.getInstance();

		this.setChanged();
		this.notifyObservers("loading participants ...");

		final List<ParticipantEntity> participants = dataService
				.getParticipants(conf);
		final Map<String, List<SurveyHistory>> historyMap = dataService
				.getHistory(participants, conf);

		final Map<TYPE, Object> back = new HashMap<TYPE, Object>();
		back.put(TYPE.instruction,
				this.createDoData(qmlLocation, filteredVariables));
		this.setChanged();
		this.notifyObservers("compose data ...");
		back.put(TYPE.data, this.createCSVData(dataService, qmlLocation,
				filteredVariables, historyMap, participants,conf));
		LOGGER.info("codebook");
		this.setChanged();
		this.notifyObservers("compose codebook ...");
		back.put(TYPE.codebook,
				this.createCodebookData(codebookService, qmlLocation));
		this.setChanged();
		this.notifyObservers("compose transitions");
		back.put(TYPE.transition, this.createTransitionNavigationData(
				navigationService, qmlLocation));
		this.setChanged();
		this.notifyObservers("compose counting ...");
		back.put(TYPE.counting, this.createCountingData(countingService,
				qmlLocation, filteredVariables, participants));
		this.setChanged();
		this.notifyObservers("compose feedback ...");
		back.put(TYPE.feedback, this.createFeedbackData(feedbackService,
				historyMap, participants));
		return back;
	}

	private Object createDoData(final String qmlPath,
			final List<String> variables) {
		final Map<String, Map<String, ValueEntry>> options = this.getOptions(
				qmlPath, variables);
		final Map<String, String> types = new LinkedHashMap<String, String>();
		for (final String variable : variables)
			types.put(variable, this.getQuestionType(qmlPath, variable));

		final Map<TYPE, Object> back = new LinkedHashMap<TYPE, Object>();
		back.put(TYPE.options, options);
		back.put(TYPE.types, types);

		return back;
	}

	private Object createCSVData(final DataService dataService,
			final String qmlPath, final List<String> variables,
			final Map<String, List<SurveyHistory>> historyMap,
			final List<ParticipantEntity> participants,final Configuration conf) {
		// final DataService dataService = DataService.getInstance();
		final Set<ParticipantType> dataExport = dataService.getExport(qmlPath,
				variables, participants, historyMap, "-96", "-97", false, true,conf);
		return dataExport;
	}

	private Object createFeedbackData(final FeedbackService service,
			final Map<String, List<SurveyHistory>> historyMap,
			final List<ParticipantEntity> participants) {
		final Map<String, Integer> exitPages = service.getExitPages(historyMap);
		return exitPages;
	}

	private Object createCodebookData(final CodebookService service,
			final String qmlPath) {
		final Map<HeaderEntry, Map<String, ValueEntry>> codebookMatrix = service
				.buildUpDataMatrix(qmlPath);
		return codebookMatrix;
	}

	private Object createTransitionNavigationData(
			final NavigationGraphService service, final String qmlPath) {
		// NavigationGraphService service =
		// NavigationGraphService.getInstance();
		final Map<String, Set<TransitionEntry>> transitionMatrix = service
				.buildUpTransitionMatrix(qmlPath);
		return transitionMatrix;
	}

	private Object createCountingData(final CountingService service,
			final String qmlPath, final List<String> variables,
			final List<ParticipantEntity> participants) {
		// CountingService service = CountingService.getInstance();
		final Map<HeaderEntry, Map<ValueEntry, Integer>> codebookMatrix = service
				.buildUpDataMatrix(qmlPath, variables, participants);
		return codebookMatrix;
	}

	private List<String> getVariables(final String qmlPath) {
		// final NodeList usingNodes =
		// XmlUtils.getInstance().getByXPath(qmlPath,
		// "//parent::*[@variable]");
		final NodeList usingNodes = XmlUtils.getInstance().getByXPath(qmlPath,
				"//@variable");
		final List<String> variables = new ArrayList<String>();
		final int count = usingNodes.getLength();

		for (int i = 0; i < count; ++i) {
			final Node e = usingNodes.item(i);
			// System.out.println("Node : "+e);
			// final NamedNodeMap nodeAttributes = e.getAttributes();
			// final Node variableNode =
			// nodeAttributes.getNamedItem("variable");

			final Node variableNode = e;
			final String variable = variableNode.getTextContent();
			variables.add(variable);
		}
		return variables;
	}

	// private List<String> getDeclaredVariable(final String qmlPath) {
	// final NodeList result =
	// XmlUtils.getInstance().getByXPath(qmlPath,"/questionnaire/variables/*");
	// List<String> declaredVariables = new ArrayList<String>();
	// for (int i = 0; i < result.getLength(); ++i) {
	// Node e = (Node) result.item(i);
	// final NamedNodeMap attributes = e.getAttributes();
	// final Node name = attributes.getNamedItem("name");
	// declaredVariables.add(name.getNodeValue());
	// }
	//
	// return declaredVariables;
	// }

	private String getQuestionType(final String qmlPath,
			final String variablename) {
		String back = "";

		final NodeList usingNodes = XmlUtils.getInstance().getByXPath(qmlPath,
				"//*[@variable]");

		final int count = usingNodes.getLength();
		for (int i = 0; i < count; ++i) {
			final Node e = usingNodes.item(i);
			final NamedNodeMap nodeAttributes = e.getAttributes();
			final Node variableNode = nodeAttributes.getNamedItem("variable");
			final String nodeType = e.getNodeName();
			final String variable = variableNode.getTextContent();
			if (!variablename.equals(variable))
				continue;

			if (nodeType.equals("zofar:responseDomain")) {
				back = "singleChoice";
			} else if (nodeType.equals("zofar:answerOption")) {
				back = "multipleChoice";
			} else if (nodeType.equals("zofar:questionOpen")) {
				back = "open";
			} else if (nodeType.equals("zofar:question")) {
				back = "open";
			} else {
				// LOGGER.info("type : {}",nodeType);
			}
		}
		return back;
	}

	private Set<String> retrieveHeader(final Node parentNode) {
		final NodeList header = XmlUtils.getInstance().getByXPath(parentNode,
				"./header");
		final Set<String> back = new LinkedHashSet<String>();

		if (header != null) {
			final int childCount = header.getLength();
			for (int a = 0; a < childCount; ++a) {
				final Node tmp = header.item(a);
				// LOGGER.info("header : {}",tmp.getNodeName());
				if (tmp.getNodeName().equals("zofar:header")) {
					final NodeList question = XmlUtils.getInstance()
							.getByXPath(tmp, "./question");
					final int questionCount = question.getLength();
					for (int b = 0; b < questionCount; ++b) {
						final Node questionText = question.item(b);
						// LOGGER.info("questionText : {}",questionText.getNodeName());
						final StringBuffer text = new StringBuffer();
						if (questionText != null)
							text.append(questionText.getTextContent());
						back.add(StringUtils.getInstance().cleanedString(
								text.toString()));
					}
				}
			}
		}
		return back;
	}

	private Map<String, Map<String, ValueEntry>> getOptions(
			final String qmlPath, final List<String> variables) {
		final Map<String, Map<String, ValueEntry>> back = new LinkedHashMap<String, Map<String, ValueEntry>>();

		// final NodeList usingNodes =
		// XmlUtils.getInstance().getByXPath(qmlPath,
		// "//parent::*[@variable]");
		final NodeList usingNodes = XmlUtils.getInstance().getByXPath(qmlPath,
				"//*[@variable]");

		final int count = usingNodes.getLength();
		for (int i = 0; i < count; ++i) {
			final Node e = usingNodes.item(i);

			final NamedNodeMap nodeAttributes = e.getAttributes();
			final Node variableNode = nodeAttributes.getNamedItem("variable");
			final String nodeType = e.getNodeName();

			final String variable = variableNode.getTextContent();
			if (!variables.contains(variable))
				continue;
			Map<String, ValueEntry> optionSet = null;
			if (back.containsKey(variable))
				optionSet = back.get(variable);
			if (optionSet == null)
				optionSet = new LinkedHashMap<String, ValueEntry>();

			if (nodeType.equals("zofar:responseDomain")) {
				final NodeList options = XmlUtils.getInstance().getByXPath(e,
						".//*");
				if (options != null) {
					final int optionsCount = options.getLength();
					for (int a = 0; a < optionsCount; ++a) {
						final Node node = options.item(a);
						if (node.getNodeName().equals("zofar:answerOption")) {
							final NamedNodeMap attributes = node
									.getAttributes();
							final Node uidNode = attributes.getNamedItem("uid");
							final String uid = uidNode.getTextContent();

							final Node valueNode = attributes
									.getNamedItem("value");
							final String value = valueNode.getTextContent();

							final Node labelNode = attributes
									.getNamedItem("label");
							final StringBuffer label = new StringBuffer();
							if (labelNode != null)
								label.append(labelNode.getTextContent());

							if (labelNode == null) {
								final NodeList optionChilds = XmlUtils
										.getInstance().getByXPath(node,
												"./label");
								if (optionChilds != null) {
									final int childCount = optionChilds
											.getLength();
									for (int b = 0; b < childCount; ++b) {
										final Node tmp = optionChilds.item(b);
										label.append(tmp.getTextContent());
										if (b < childCount - 1)
											label.append(" # ");
									}
								}
							}

							if ((uid != null) && (label != null)) {
								optionSet.put(
										uid,
										new ValueEntry(value, StringUtils
												.getInstance().cleanedString(
														label.toString())));
							}
						}
					}
				}
			} else if (nodeType.equals("zofar:answerOption")) {
				optionSet.put("true", new ValueEntry("true", "Ja"));
				optionSet.put("false", new ValueEntry("false", "Nein"));
			} else if (nodeType.equals("zofar:questionOpen")) {
				// optionSet.put("OPEN", new ValueEntry("OPEN", "Freier Text"));
			} else if (nodeType.equals("zofar:question")) {
				// optionSet.put("OPEN", new ValueEntry("OPEN", "Freier Text"));
			} else {
				// LOGGER.info("type : {}",nodeType);
			}
			back.put(variable, optionSet);
		}
		return back;
	}
}

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

import org.hibernate.cfg.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import de.his.export.xml.export.ParticipantType;
import eu.dzhw.zofar.management.utils.numbers.UtilClient;
import eu.dzhw.zofar.management.utils.objects.CollectionClient;
import eu.dzhw.zofar.management.utils.string.ReplaceClient;
import eu.dzhw.zofar.management.utils.system.ConfigurationUtils;
import eu.dzhw.zofar.management.utils.xml.XmlClient;
import model.HeaderEntry;
import model.ParticipantEntity;
import model.SurveyHistory;
import model.TransitionEntry;
import model.ValueEntry;
import service.codebook.CodebookService;
import service.counting.CountingService;
import service.data.DataService;
import service.dokudat.DokudatService;
import service.feedback.FeedbackService;
import service.navigation.NavigationGraphService;

/**
 * The Class StatisticService.
 */
public class StatisticService extends Observable {

	/** The instance. */
	private static StatisticService INSTANCE;

	/** The Constant LOGGER. */
	private static final Logger LOGGER = LoggerFactory.getLogger(StatisticService.class);

	/**
	 * The Enum TYPE.
	 */
	public enum TYPE {

		/** The instruction. */
		instruction,
		/** The data. */
		data,
		/** The options. */
		options,
		/** The types. */
		types, variables,
		/** The codebook. */
		codebook,
		/** The counting. */
		counting,
		/** The transition. */
		transition,
		/** The feedback. */
		feedback, dokudat, cockpit
	}

	/**
	 * Instantiates a new statistic service.
	 */
	private StatisticService() {
		super();
	}

	/**
	 * Gets the single instance of StatisticService.
	 * 
	 * @return single instance of StatisticService
	 */
	public static StatisticService getInstance() {
		if (INSTANCE == null)
			INSTANCE = new StatisticService();
		return INSTANCE;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Observable#addObserver(java.util.Observer)
	 */
	@Override
	public synchronized void addObserver(final Observer o) {
		super.addObserver(o);
		this.setChanged();
		this.notifyObservers("phases=6");
	}

	/**
	 * Creates the.
	 * 
	 * @param conf
	 *            the conf
	 * @return the map
	 * @throws Throwable
	 */
	public Map<TYPE, Object> create(final Configuration conf, final boolean noEmpties, final boolean nometa,final NodeList usingNodes,final Map<String, String> missings,final Map<String, Map<String, String>> values,final Map<String, List<String>> variablePages,final Map<String, List<List<String>>> variableVisibles,final Map<String, String> types,final Map<String, List<String>> pageTree,final Map<String, Set<String>> questions)
			throws Throwable {
		final Properties system = ConfigurationUtils.getInstance().getConfiguration("System.properties");
		return this.create(system.getProperty("qml.location"), conf, noEmpties, nometa,usingNodes,missings,values,variablePages,variableVisibles,types,pageTree,questions);
	}

	/**
	 * Creates the.
	 * 
	 * @param qmlLocation
	 *            the qml location
	 * @param conf
	 *            the conf
	 * @return the map
	 * @throws Throwable
	 */
	private Map<TYPE, Object> create(final String qmlLocation, final Configuration conf, final boolean noEmpties,
			final boolean nometa,final NodeList usingNodes,final Map<String, String> missings,final Map<String, Map<String, String>> values,final Map<String, List<String>> variablePages,final Map<String, List<List<String>>> variableVisibles,final Map<String, String> types,final Map<String, List<String>> pageTree,final Map<String, Set<String>> questions) throws Throwable {
		final List<String> variables = this.getVariables(qmlLocation,usingNodes);
		final Map<String, Map<String, ValueEntry>> options 	 = this.getOptions(qmlLocation, variables,usingNodes);
		final List<String> filteredVariables = new ArrayList<String>();
		filteredVariables.addAll(variables);

		final DataService dataService = DataService.getInstance();
		final CodebookService codebookService = CodebookService.getInstance();
		final NavigationGraphService navigationService = NavigationGraphService.getInstance();
		final CountingService countingService = CountingService.getInstance();
		final FeedbackService feedbackService = FeedbackService.getInstance();
		final DokudatService dokudatService = DokudatService.getInstance();

		this.setChanged();
		this.notifyObservers("loading participants ...");

		final List<ParticipantEntity> participants = dataService.getParticipants(conf, noEmpties);
		final Map<String, List<SurveyHistory>> historyMap = dataService.getHistory(participants, conf);

		final Map<TYPE, Object> back = new HashMap<TYPE, Object>();
		back.put(TYPE.instruction, this.createDoData(qmlLocation, filteredVariables,usingNodes));
		this.setChanged();
		this.notifyObservers("compose data ...");
		back.put(TYPE.data, this.createCSVData(dataService, qmlLocation, filteredVariables, historyMap, participants,
				nometa, conf,missings,values,variablePages,variableVisibles,types,pageTree));
		this.setChanged();
		this.notifyObservers("compose codebook ...");
		back.put(TYPE.codebook, this.createCodebookData(codebookService, qmlLocation,usingNodes));
		this.setChanged();
		this.notifyObservers("compose dokudat ...");
		back.put(TYPE.dokudat, this.createDokudatData(dokudatService, qmlLocation,usingNodes));
		this.setChanged();
		this.notifyObservers("compose transitions");
		back.put(TYPE.transition, this.createTransitionNavigationData(navigationService, qmlLocation));
		this.setChanged();
		this.notifyObservers("compose counting ...");
//		back.put(TYPE.counting, this.createCountingData(countingService, qmlLocation, filteredVariables, participants));
		back.put(TYPE.counting, this.createCountingData(countingService, null,qmlLocation, filteredVariables,options, participants,usingNodes,questions));
		this.setChanged();
		this.notifyObservers("compose feedback ...");
		back.put(TYPE.feedback, this.createFeedbackData(feedbackService, historyMap, participants));
		this.setChanged();
		this.notifyObservers("compose cockpit ...");
		back.put(TYPE.cockpit, this.createCockpitData(feedbackService, historyMap, participants));
		return back;
	}

	/**
	 * Gets the packet count.
	 * 
	 * @param conf
	 *            the conf
	 * @param packetSize
	 *            the packet size
	 * @return the packet count
	 * @throws Throwable
	 */
	public int getPacketCount(final Configuration conf, final int packetSize) throws Throwable {
		return getPacketCount(conf, packetSize, false);
	}

	public int getPacketCount(final Configuration conf, final int packetSize, final boolean noEmpties)
			throws Throwable {
		final DataService dataService = DataService.getInstance();

		final int participantCount = dataService.getParticipantsCount(conf, noEmpties);
		final int packetCount = UtilClient.getInstance().ceil(((double) participantCount) / ((double) packetSize));

		LOGGER.info("participantCount : {} packetCount : {}", participantCount, packetCount);

		return Math.max(packetCount, 1);
	}

	public Integer getParticipantCount(final Configuration conf) throws Throwable {
		final DataService dataService = DataService.getInstance();

		final int participantCount = dataService.getParticipantsCount(conf, false);
		return participantCount;
	}

	/**
	 * Creates the packet.
	 * 
	 * @param qmlLocation
	 *            the qml location
	 * @param filteredVariables
	 *            the filtered variables
	 * @param conf
	 *            the conf
	 * @param index
	 *            the index
	 * @param packetSize
	 *            the packet size
	 * @return the map
	 * @throws Throwable
	 */
	public Map<TYPE, Object> createPacket(final String qmlLocation, final List<String> filteredVariables,final Map<String, Map<String, ValueEntry>> options,final Configuration conf, final int index, final int packetSize, final String defaultMissing,
			final boolean nometa,final NodeList usingNodes,final Map<String, String> missings,final Map<String, Map<String, String>> values,final Map<String, List<String>> variablePages,final Map<String, List<List<String>>> variableVisibles,final Map<String, String> types,final Map<String, List<String>> pageTree,final Map<String, Set<String>> questions) throws Throwable {
		return createPacket(qmlLocation, filteredVariables,options, conf, index, packetSize, false, false, nometa,usingNodes,missings,values,variablePages,variableVisibles,types,pageTree,questions);
	}

	public Map<TYPE, Object> createPacket(final String qmlLocation, final List<String> filteredVariables,final Map<String, Map<String, ValueEntry>> options,
			final Configuration conf, final int index, final int packetSize, final boolean noEmpties,
			final boolean pretestComments, final boolean nometa,final NodeList usingNodes,final Map<String, String> missings,final Map<String, Map<String, String>> values,final Map<String, List<String>> variablePages,final Map<String, List<List<String>>> variableVisibles,final Map<String, String> types,final Map<String, List<String>> pageTree,final Map<String, Set<String>> questions) throws Throwable {
		final DataService dataService = DataService.getInstance();
		final CountingService countingService = CountingService.getInstance();
		final FeedbackService feedbackService = FeedbackService.getInstance();

		final Map<TYPE, Object> back = new HashMap<TYPE, Object>();

		final int startIndex = (index * packetSize) - packetSize;
		System.out.println("create Packet " + index + " StartIndex " + startIndex + " ...");

		final List<ParticipantEntity> participants = dataService.getParticipants(conf, startIndex, packetSize,
				noEmpties);

		System.out.println(participants.size() + " Participants loaded");

		final Map<String, List<SurveyHistory>> historyMap = dataService.getHistory(participants, conf);

		System.out.println(historyMap.size() + " HistoryEntries loaded");

		back.put(TYPE.data, this.createCSVData(dataService, qmlLocation, filteredVariables, historyMap, participants,
				nometa, conf,missings,values,variablePages,variableVisibles,types,pageTree));
//		back.put(TYPE.counting, this.createCountingData(countingService, qmlLocation, filteredVariables, participants));
		back.put(TYPE.counting, this.createCountingData(countingService,null, qmlLocation, filteredVariables,options, participants,usingNodes,questions));
		back.put(TYPE.feedback, this.createFeedbackData(feedbackService, historyMap, participants));
		back.put(TYPE.cockpit, this.createCockpitData(feedbackService, historyMap, participants));
		System.out.println("Packet " + index + " loaded");
		return back;
	}

	/**
	 * Creates the static.
	 * 
	 * @param qmlLocation
	 *            the qml location
	 * @param filteredVariables
	 *            the filtered variables
	 * @param conf
	 *            the conf
	 * @return the map
	 * @throws Exception
	 */
	public Map<TYPE, Object> createStatic(final String qmlLocation, final List<String> filteredVariables,
			final Configuration conf,final NodeList usingNodes) throws Exception {
		final CodebookService codebookService = CodebookService.getInstance();
		final DokudatService dokudatService = DokudatService.getInstance();
		final NavigationGraphService navigationService = NavigationGraphService.getInstance();

		final Map<TYPE, Object> back = new HashMap<TYPE, Object>();

		LOGGER.info("Stata script");
		this.setChanged();
		this.notifyObservers("compose Stata script ...");
		back.put(TYPE.instruction, this.createDoData(qmlLocation, filteredVariables,usingNodes));
		LOGGER.info("codebook");
		this.setChanged();
		this.notifyObservers("compose codebook ...");
		back.put(TYPE.codebook, this.createCodebookData(codebookService, qmlLocation,usingNodes));
		this.setChanged();
		this.notifyObservers("compose dokudat ...");
		back.put(TYPE.dokudat, this.createDokudatData(dokudatService, qmlLocation,usingNodes));
		this.setChanged();
		this.notifyObservers("compose transitions");
		back.put(TYPE.transition, this.createTransitionNavigationData(navigationService, qmlLocation));
		return back;
	}

	/**
	 * Creates the do data.
	 * 
	 * @param qmlPath
	 *            the qml path
	 * @param variables
	 *            the variables
	 * @return the object
	 * @throws Exception
	 */
	private Object createDoData(final String qmlPath, final List<String> variables,final NodeList usingNodes) throws Exception {
		System.out.println("create Stata script ...");
		final Map<String, Map<String, ValueEntry>> options = this.getOptions(qmlPath, variables,usingNodes);
		final Map<String, String> types = new LinkedHashMap<String, String>();
		final Map<String, Map<String, Object>> variableLabels = getQuestions(qmlPath,usingNodes);
		
		final Map<String, String> questionTypes = this.getQuestionTypes(usingNodes);
		
//		final NodeList usingNodes = XmlClient.getInstance().getByXPath(qmlPath, "//*[@variable]");
		
//		for (final String variable : variables)
//			types.put(variable, this.getQuestionType(usingNodes, variable));
		
		for (final String variable : variables)
			types.put(variable, questionTypes.get(variable));
		final Map<TYPE, Object> back = new LinkedHashMap<TYPE, Object>();
		back.put(TYPE.options, options);
		back.put(TYPE.types, types);
		back.put(TYPE.variables, variableLabels);
		System.out.println("Stata script created");
		return back;
	}

	/**
	 * Gets the questions.
	 * 
	 * @param qmlPath
	 *            the qml path
	 * @return the questions
	 * @throws Exception
	 */
	private Map<String, Map<String, Object>> getQuestions(final String qmlPath,final NodeList usingNodes) throws Exception {
		final Map<String, Map<String, Object>> back = new LinkedHashMap<String, Map<String, Object>>();
		
//		final NodeList usingNodes = XmlClient.getInstance().getByXPath(qmlPath, "//*[@variable]");
		
		CollectionClient collectionClient = CollectionClient.getInstance();
		final int count = usingNodes.getLength();
		for (int i = 0; i < count; ++i) {
			final Node e = usingNodes.item(i);
			if (XmlClient.getInstance().hasParent(e, "zofar:researchdata"))
				continue;
			final String type = e.getNodeName();
			final NamedNodeMap nodeAttributes = e.getAttributes();
			final Node variableNode = nodeAttributes.getNamedItem("variable");
			final String variable = variableNode.getTextContent();
			final Map<String, Object> data = new HashMap<String, Object>();
			String questionType = "UNKOWN";
			if (type.equals("zofar:responseDomain"))
				questionType = "singlechoice";
			if (type.equals("zofar:questionOpen"))
				questionType = "open";
			if (type.equals("zofar:attachedOpen"))
				questionType = "open";
			if (type.equals("zofar:question"))
				questionType = "open";
			if (type.equals("zofar:answerOption"))
				questionType = "multiplechoice";
			if (type.equals("zofar:left"))
				questionType = "singlechoice";
			if (type.equals("zofar:right"))
				questionType = "singlechoice";
			if (type.equals("zofar:variable"))
				questionType = "variable";
			if (type.equals("zofar:preloadItem"))
				questionType = "preload";

			data.put("type", questionType);
			data.put("header", collectionClient.implode(this.getQuestionsHelper(e).toArray(), " "));
			final Map<String, String> values = new LinkedHashMap<String, String>();
			if (questionType.equals("singlechoice")) {
				NodeList optionNodes = XmlClient.getInstance().getByXPath(e, "descendant::answerOption");
				final int optionsCount = optionNodes.getLength();
				for (int j = 0; j < optionsCount; ++j) {
					final Node optionNode = optionNodes.item(j);
					final NamedNodeMap optionAttributes = optionNode.getAttributes();

					final Node valueNode = optionAttributes.getNamedItem("value");
					final String value = valueNode.getTextContent();

					String label = "UNKOWN";
					final Node labelNode = optionAttributes.getNamedItem("label");
					if (labelNode != null)
						label = ReplaceClient.getInstance().cleanedString(labelNode.getTextContent());

					values.put(value, label);
				}
			} else if (questionType.equals("multiplechoice")) {
				values.put("true", "ja");
				values.put("false", "nein");
			}
			data.put("values", values);
			back.put(variable, data);
		}
		return back;
	}

	/**
	 * Gets the questions helper.
	 * 
	 * @param node
	 *            the node
	 * @return the questions helper
	 */
	private Set<String> getQuestionsHelper(final Node node) {
		final Set<String> back = new LinkedHashSet<String>();

		final String nodeType = node.getNodeName();
		if (nodeType.startsWith("zofar:")) {
			back.addAll(this.getQuestionsHelper(node.getParentNode()));
		}
		back.addAll(this.retrieveHeader(node));
		return back;
	}

	/**
	 * Retrieve header.
	 * 
	 * @param parentNode
	 *            the parent node
	 * @return the sets the
	 */
	private Set<String> retrieveHeader(final Node parentNode) {
		final Set<String> back = new LinkedHashSet<String>();
		final NodeList header = XmlClient.getInstance().getByXPath(parentNode, "./header");
		if (header != null) {
			final int childCount = header.getLength();
			for (int a = 0; a < childCount; ++a) {
				final Node tmp = header.item(a);
				if (XmlClient.getInstance().hasParent(tmp, "zofar:researchdata"))
					continue;
				if (tmp.getNodeName().equals("zofar:header")) {
					final NodeList question = XmlClient.getInstance().getByXPath(tmp, "./question");
					final int questionCount = question.getLength();
					for (int b = 0; b < questionCount; ++b) {
						final Node questionText = question.item(b);
						if (XmlClient.getInstance().hasParent(questionText, "zofar:researchdata"))
							continue;
						// LOGGER.info("questionText :
						// {}",questionText.getNodeName());
						final StringBuffer text = new StringBuffer();
						if (questionText != null)
							text.append(questionText.getTextContent());
						back.add(ReplaceClient.getInstance().cleanedString(text.toString()));
					}
				}
			}
		}
		if (parentNode.getNodeName().equals("zofar:answerOption")) {
			final NamedNodeMap attributes = parentNode.getAttributes();
			final Node labelNode = attributes.getNamedItem("label");
			if (labelNode != null) {
				back.add(ReplaceClient.getInstance().cleanedString(labelNode.getTextContent()));
			}
		}
		return back;
	}

	/**
	 * Creates the csv data.
	 * 
	 * @param dataService
	 *            the data service
	 * @param qmlPath
	 *            the qml path
	 * @param variables
	 *            the variables
	 * @param historyMap
	 *            the history map
	 * @param participants
	 *            the participants
	 * @param conf
	 *            the conf
	 * @return the object
	 * @throws Exception
	 */
	private Object createCSVData(final DataService dataService, final String qmlPath, final List<String> variables,
			final Map<String, List<SurveyHistory>> historyMap, final List<ParticipantEntity> participants,
			final boolean nometa, final Configuration conf,final Map<String, String> missings,final Map<String, Map<String, String>> values,final Map<String, List<String>> variablePages,final Map<String, List<List<String>>> variableVisibles,final Map<String, String> types,final Map<String, List<String>> pageTree) throws Exception {
		return this.createCSVData(dataService, null, qmlPath, variables, historyMap, participants, nometa, conf,missings,values,variablePages,variableVisibles,types,pageTree);
	}

	/**
	 * Creates the csv data.
	 * 
	 * @param dataService
	 *            the data service
	 * @param preset
	 *            the preset
	 * @param qmlPath
	 *            the qml path
	 * @param variables
	 *            the variables
	 * @param historyMap
	 *            the history map
	 * @param participants
	 *            the participants
	 * @param conf
	 *            the conf
	 * @return the object
	 * @throws Exception
	 */
	private Object createCSVData(final DataService dataService, final Set<ParticipantType> preset, final String qmlPath,
			final List<String> variables, final Map<String, List<SurveyHistory>> historyMap,
			final List<ParticipantEntity> participants, final boolean nometa, final Configuration conf,final Map<String, String> missings,final Map<String, Map<String, String>> values,final Map<String, List<String>> variablePages,final Map<String, List<List<String>>> variableVisibles,final Map<String, String> types,final Map<String, List<String>> pageTree)
			throws Exception {
		System.out.println("create CSV data ...");
		final Set<ParticipantType> dataExport = dataService.getExport(qmlPath, preset, variables, participants,
				historyMap, false, nometa, conf,missings,values,variablePages,variableVisibles,types,pageTree);
		System.out.println("CSV data created");
		return dataExport;
	}

	/**
	 * Creates the feedback data.
	 * 
	 * @param service
	 *            the service
	 * @param historyMap
	 *            the history map
	 * @param participants
	 *            the participants
	 * @return the object
	 */
	private Object createFeedbackData(final FeedbackService service, final Map<String, List<SurveyHistory>> historyMap,
			final List<ParticipantEntity> participants) {
		return this.createFeedbackData(service, null, historyMap, participants);
	}

	private Object createCockpitData(final FeedbackService service, final Map<String, List<SurveyHistory>> historyMap,
			final List<ParticipantEntity> participants) {
		System.out.println("create cockpit informations ...");
		final Map<String, Integer> cockpitData = service.getCockpitInformations(historyMap);
		System.out.println("cockpit informations created");
		return cockpitData;
	}

	/**
	 * Creates the feedback data.
	 * 
	 * @param service
	 *            the service
	 * @param preset
	 *            the preset
	 * @param historyMap
	 *            the history map
	 * @param participants
	 *            the participants
	 * @return the object
	 */
	private Object createFeedbackData(final FeedbackService service, final Map<String, Integer> preset,
			final Map<String, List<SurveyHistory>> historyMap, final List<ParticipantEntity> participants) {
		System.out.println("create exit informations ...");
		final Map<String, Integer> exitPages = service.getExitPages(historyMap, preset);
		System.out.println("exit informations created");
		return exitPages;
	}

	/**
	 * Creates the codebook data.
	 * 
	 * @param service
	 *            the service
	 * @param qmlPath
	 *            the qml path
	 * @return the object
	 * @throws Exception
	 */
	private Object createCodebookData(final CodebookService service, final String qmlPath,final NodeList usingNodes) throws Exception {
		System.out.println("create codebook informations ...");
		final Map<HeaderEntry, Map<String, ValueEntry>> codebookMatrix = service.buildUpDataMatrix(qmlPath,usingNodes);
		System.out.println("codebook informations created");
		return codebookMatrix;
	}

	/**
	 * Creates the Dokudat data.
	 * 
	 * @param service
	 *            the service
	 * @param qmlPath
	 *            the qml path
	 * @return the object
	 * @throws Exception
	 */
	private Object createDokudatData(final DokudatService service, final String qmlPath,final NodeList usingNodes) throws Exception {
		System.out.println("create dokudat informations ...");
		final Map<HeaderEntry, Object> dokudatMatrix = service.buildUpDataMatrix(qmlPath,usingNodes);
		System.out.println("dokudat informations created");
		return dokudatMatrix;
	}

	/**
	 * Creates the transition navigation data.
	 * 
	 * @param service
	 *            the service
	 * @param qmlPath
	 *            the qml path
	 * @return the object
	 * @throws Exception
	 */
	private Object createTransitionNavigationData(final NavigationGraphService service, final String qmlPath)
			throws Exception {
		System.out.println("create transition informations ...");
		final Map<String, Set<TransitionEntry>> transitionMatrix = service.buildUpTransitionMatrix(qmlPath);
		System.out.println("transition informations created");
		return transitionMatrix;
	}

	/**
	 * Creates the counting data.
	 * 
	 * @param service
	 *            the service
	 * @param qmlPath
	 *            the qml path
	 * @param variables
	 *            the variables
	 * @param participants
	 *            the participants
	 * @return the object
	 * @throws Exception
	 */
//	private Object createCountingData(final CountingService service, final String qmlPath, final List<String> variables,
//			final List<ParticipantEntity> participants) throws Exception {
//		return this.createCountingData(service, null, qmlPath, variables, participants);
//	}

	/**
	 * Creates the counting data.
	 * 
	 * @param service
	 *            the service
	 * @param preset
	 *            the preset
	 * @param qmlPath
	 *            the qml path
	 * @param variables
	 *            the variables
	 * @param participants
	 *            the participants
	 * @return the object
	 * @throws Exception
	 */
	private Object createCountingData(final CountingService service,
			final Map<HeaderEntry, Map<ValueEntry, Integer>> preset, final String qmlPath, final List<String> variables,final Map<String, Map<String, ValueEntry>> options,
			final List<ParticipantEntity> participants,final NodeList usingNodes,final Map<String, Set<String>> questions) throws Exception {
		System.out.println("create countings ...");
		final Map<HeaderEntry, Map<ValueEntry, Integer>> countingMatrix = service.buildUpDataMatrix(qmlPath, preset,
				variables,options, participants,usingNodes,questions);
		System.out.println("countings created");
		return countingMatrix;
	}

	/**
	 * Gets the variables.
	 * 
	 * @param qmlPath
	 *            the qml path
	 * @return the variables
	 * @throws Exception
	 */

	public List<String> getVariables(final String qmlPath,final NodeList usingNodes) throws Exception {
//		final NodeList usingNodes = XmlClient.getInstance().getByXPath(qmlPath, "//*[@variable]");
		final List<String> variables = new ArrayList<String>();
		final int count = usingNodes.getLength();

		for (int i = 0; i < count; ++i) {
			final Node e = usingNodes.item(i);
			if (XmlClient.getInstance().hasParent(e, "zofar:researchdata"))
				continue;

			final NamedNodeMap nodeAttributes = e.getAttributes();
			final Node variableNode = nodeAttributes.getNamedItem("variable");
			final String nodeType = e.getNodeName();

			// LOGGER.info("variable : ({})
			// {}",nodeType,variableNode.getTextContent());

			String variable = variableNode.getTextContent().trim();
			if (nodeType.equals("zofar:preloadItem")) {
				LOGGER.info("Preload variable found : {}", variable);
				variable = "PRELOAD" + variable;
			}
			variables.add(variable.trim());
		}
		return variables;
	}

	// public List<String> getVariables(final String qmlPath) {
	// final NodeList usingNodes = XmlClient.getInstance().getByXPath(qmlPath,
	// "//@variable");
	// final List<String> variables = new ArrayList<String>();
	// final int count = usingNodes.getLength();
	//
	// for (int i = 0; i < count; ++i) {
	// final Node e = usingNodes.item(i);
	// LOGGER.info("variable : ({}) {}",e.getParentNode(),e.getTextContent());
	// if (XmlUtils.getInstance().hasParent(e, "zofar:researchdata"))
	// continue;
	// final Node variableNode = e;
	// String variable = variableNode.getTextContent();
	// if (XmlUtils.getInstance().hasParent(e, "zofar:preloadItem")) {
	// LOGGER.info("Preload variable found : {}",variable);
	// variable = "PRELOAD" + variable;
	// }
	// variables.add(variable);
	// }
	// return variables;
	// }

	/**
	 * Gets the variables.
	 * 
	 * @param qmlPath
	 *            the qml path
	 * @return the variables
	 * @throws Exception
	 */
	public List<String> getPages(final String qmlPath) throws Exception {
		final NodeList usingNodes = XmlClient.getInstance().getByXPath(qmlPath, "page");
		final List<String> pages = new ArrayList<String>();
		final int count = usingNodes.getLength();

		for (int i = 0; i < count; ++i) {
			final Node e = usingNodes.item(i);
			if (XmlClient.getInstance().hasParent(e, "zofar:researchdata"))
				continue;
			final NamedNodeMap attributes = e.getAttributes();
			final Node uid = attributes.getNamedItem("uid");
			pages.add(uid.getTextContent());
		}
		return pages;
	}

	/**
	 * Gets the question type.
	 * 
	 * @param usingNodes
	 *            the using nodes
	 * @param variablename
	 *            the variablename
	 * @return the question type
	 */
	private String getQuestionType(final NodeList usingNodes, final String variablename) {
		String back = "";
		final int count = usingNodes.getLength();
		for (int i = 0; i < count; ++i) {
			final Node e = usingNodes.item(i);
			final NamedNodeMap nodeAttributes = e.getAttributes();
			final Node variableNode = nodeAttributes.getNamedItem("variable");
			final String nodeType = e.getNodeName();
			final String variable = variableNode.getTextContent().trim();
			if (!variablename.equals(variable))
				continue;

			if (nodeType.equals("zofar:responseDomain")) {
				back = "singleChoice";
			} else if (nodeType.equals("zofar:answerOption")) {
				back = "multipleChoice";
			} else if (nodeType.equals("zofar:SlotItem")) {
				back = "multipleChoice";
			} else if (nodeType.equals("zofar:questionOpen")) {
				back = "open";
			} else if (nodeType.equals("zofar:attachedOpen")) {
				back = "open";
			} else if (nodeType.equals("zofar:question")) {
				back = "open";
			} else if (nodeType.equals("zofar:preloadItem")) {
				back = "preload";
			} else {
				// LOGGER.info("type : {}",nodeType);
			}
		}
		return back;
	}
	
	private Map<String,String> getQuestionTypes(final NodeList usingNodes) {
		final Map<String,String> back = new HashMap<String,String>();
		final int count = usingNodes.getLength();
		for (int i = 0; i < count; ++i) {
			final Node e = usingNodes.item(i);
			final NamedNodeMap nodeAttributes = e.getAttributes();
			final Node variableNode = nodeAttributes.getNamedItem("variable");
			final String nodeType = e.getNodeName();
			final String variable = variableNode.getTextContent().trim();

			if (nodeType.equals("zofar:responseDomain")) {
				back.put(variable, "singleChoice");
			} else if (nodeType.equals("zofar:answerOption")) {
				back.put(variable, "multipleChoice");
			} else if (nodeType.equals("zofar:SlotItem")) {
				back.put(variable, "multipleChoice");
			} else if (nodeType.equals("zofar:questionOpen")) {
				back.put(variable, "open");
			} else if (nodeType.equals("zofar:attachedOpen")) {
				back.put(variable, "open");
			} else if (nodeType.equals("zofar:question")) {
				back.put(variable, "open");
			} else if (nodeType.equals("zofar:preloadItem")) {
				back.put(variable, "preload");
			} else {
				// LOGGER.info("type : {}",nodeType);
			}
		}
		return back;
	}

	/**
	 * Gets the options.
	 * 
	 * @param qmlPath
	 *            the qml path
	 * @param variables
	 *            the variables
	 * @return the options
	 * @throws Exception
	 */
	private Map<String, Map<String, ValueEntry>> getOptions(final String qmlPath, final List<String> variables,final NodeList usingNodes)
			throws Exception {
		final Map<String, Map<String, ValueEntry>> back = new LinkedHashMap<String, Map<String, ValueEntry>>();

//		final NodeList usingNodes = XmlClient.getInstance().getByXPath(qmlPath, "//*[@variable]");

		final int count = usingNodes.getLength();
		for (int i = 0; i < count; ++i) {
			final Node e = usingNodes.item(i);
			if (XmlClient.getInstance().hasParent(e, "zofar:researchdata"))
				continue;
			final NamedNodeMap nodeAttributes = e.getAttributes();
			final Node variableNode = nodeAttributes.getNamedItem("variable");
			final String nodeType = e.getNodeName();

			final String variable = variableNode.getTextContent().trim();
		
			if (!variables.contains(variable))
				continue;
			
			Map<String, ValueEntry> optionSet = null;
			if (back.containsKey(variable))
				optionSet = back.get(variable);
			if (optionSet == null)
				optionSet = new LinkedHashMap<String, ValueEntry>();

			if (nodeType.equals("zofar:responseDomain")) {
				final NodeList options = XmlClient.getInstance().getByXPath(e, ".//*");
				if (options != null) {
					final List<String> matrixHeader = new ArrayList<String>();
					if(XmlClient.getInstance().hasParent(e, "zofar:item")){
						//Escalate to Item
						Node pointedNode = e;
						while((pointedNode !=null)&&(!pointedNode.getNodeName().equals("zofar:item"))){
							pointedNode = pointedNode.getParentNode();
						}
						//Escalate to Matrix RDC
						while((pointedNode !=null)&&(!pointedNode.getNodeName().equals("zofar:responseDomain"))){
							pointedNode = pointedNode.getParentNode();
						}
						
						if((pointedNode !=null)&&(pointedNode.getNodeName().equals("zofar:responseDomain"))){
							final NodeList headerTitles= XmlClient.getInstance().getByXPath(pointedNode, "./header/title");
							if (headerTitles != null) {
								final int childCount = headerTitles.getLength();
								for (int a = 0; a < childCount; ++a) {
									final Node headerTitle = headerTitles.item(a);
									final String headerTitleContent = headerTitle.getTextContent();
									matrixHeader.add(headerTitleContent);
								}
							}
						}
					}
					int labelIndex = 0;
					final int optionsCount = options.getLength();
					for (int a = 0; a < optionsCount; ++a) {
						final Node node = options.item(a);
						if (node.getNodeName().equals("zofar:answerOption")) {
							final NamedNodeMap attributes = node.getAttributes();
							final Node uidNode = attributes.getNamedItem("uid");
							final String uid = uidNode.getTextContent();

							final Node valueNode = attributes.getNamedItem("value");
							final String value = valueNode.getTextContent();

							final Node missingNode = attributes.getNamedItem("missing");
							boolean missing = false;
							if (missingNode != null) {
								missing = Boolean.getBoolean(missingNode.getTextContent());
							}

							final Node labelNode = attributes.getNamedItem("label");
							final StringBuffer label = new StringBuffer();
							if (labelNode != null)
								label.append(labelNode.getTextContent());

							if (labelNode == null) {
								boolean found = false;
								final NodeList optionChilds = XmlClient.getInstance().getByXPath(node, "./label");
								if (optionChilds != null) {
									final int childCount = optionChilds.getLength();
									for (int b = 0; b < childCount; ++b) {
										final Node tmp = optionChilds.item(b);
										label.append(tmp.getTextContent());
										if (b < childCount - 1)
											label.append(" # ");
										found = true;
									}
								}
								if(!found){
									// Bugfix Label in Matrix
									if((!matrixHeader.isEmpty())&&(matrixHeader.size() > labelIndex)){
										try{
											label.append(matrixHeader.get(labelIndex));
										}
										catch(Exception exp){
											LOGGER.info("## ERROR {} ==> ", exp.getMessage()+" ("+variable+")",labelIndex+","+a+" -- "+matrixHeader);
										}
									}
								}
							}

							if ((uid != null) && (label != null)) {
								optionSet.put(uid, new ValueEntry(value,
										ReplaceClient.getInstance().cleanedString(label.toString()), missing));
							}
							labelIndex = labelIndex + 1;
						}
					}
				}
			} else if (nodeType.equals("zofar:answerOption")) {
				optionSet.put("true", new ValueEntry("true", "Ja", false));
				optionSet.put("false", new ValueEntry("false", "Nein", false));
			} else if (nodeType.equals("zofar:SlotItem")) {
				optionSet.put("true", new ValueEntry("true", "Ja", false));
				optionSet.put("false", new ValueEntry("false", "Nein", false));
			} else if (nodeType.equals("zofar:questionOpen")) {
				// optionSet.put("OPEN", new ValueEntry("OPEN", "Freier Text"));
			} else if (nodeType.equals("zofar:attachedOpen")) {
				// optionSet.put("OPEN", new ValueEntry("OPEN", "Freier Text"));
			} else if (nodeType.equals("zofar:question")) {
				// optionSet.put("OPEN", new ValueEntry("OPEN", "Freier Text"));
			} else if (nodeType.equals("zofar:preloadItem")) {
				// optionSet.put("PRELOAD", new ValueEntry("PRELOAD",
				// "Preload"));
			} else {
				// LOGGER.info("type : {}",nodeType);
			}
			back.put(variable, optionSet);
		}
		return back;
	}
}

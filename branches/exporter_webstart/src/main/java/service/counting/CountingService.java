package service.counting;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import eu.dzhw.zofar.management.utils.string.ReplaceClient;
import eu.dzhw.zofar.management.utils.xml.XmlClient;
// TODO: Auto-generated Javadoc
//import de.his.zofar.persistence.surveyengine.entities.ParticipantEntity;
//import de.his.zofar.persistence.surveyengine.entities.SurveyDataEntity;
import model.HeaderEntry;
import model.ParticipantEntity;
import model.SurveyData;
import model.ValueEntry;

/**
 * The Class CountingService.
 */
public class CountingService {

	/** The Constant LOGGER. */
	private static final Logger LOGGER = LoggerFactory.getLogger(CountingService.class);

	/** The instance. */
	private static CountingService INSTANCE;

	/**
	 * Instantiates a new counting service.
	 */
	private CountingService() {
		super();
	}

	/**
	 * Gets the single instance of CountingService.
	 * 
	 * @return single instance of CountingService
	 */
	public static CountingService getInstance() {
		if (INSTANCE == null)
			INSTANCE = new CountingService();
		return INSTANCE;
	}

	/**
	 * Builds the up data matrix.
	 * 
	 * @param qmlPath
	 *            the qml path
	 * @param variables
	 *            the variables
	 * @param participants
	 *            the participants
	 * @return the map
	 * @throws Exception 
	 */
	
	// from main.Codebook
	public Map<HeaderEntry, Map<ValueEntry, Integer>> buildUpDataMatrix(final String qmlPath, final List<String> variables,final Map<String, Map<String, ValueEntry>> options, final List<ParticipantEntity> participants,final NodeList usingNodes,final Map<String, Set<String>> questions) throws Exception {
		return this.buildUpDataMatrix(qmlPath, null, variables, options,participants,usingNodes,questions);
	}

	/**
	 * Builds the up data matrix.
	 * 
	 * @param qmlPath
	 *            the qml path
	 * @param back
	 *            the back
	 * @param variables
	 *            the variables
	 * @param participants
	 *            the participants
	 * @return the map
	 * @throws Exception 
	 */
	
	//from service.statistics.StatisticService
	public Map<HeaderEntry, Map<ValueEntry, Integer>> buildUpDataMatrix(final String qmlPath, Map<HeaderEntry, Map<ValueEntry, Integer>> back, List<String> variables,final Map<String, Map<String, ValueEntry>> options,
			final List<ParticipantEntity> participants,final NodeList usingNodes,final Map<String, Set<String>> questions) throws Exception {
		if (variables == null)
			variables = this.getVariables(qmlPath,usingNodes);

		final Map<HeaderEntry, Map<ValueEntry, Integer>> process = new LinkedHashMap<HeaderEntry, Map<ValueEntry, Integer>>();

		// Initialize
		if (options != null) {
			final Set<String> varnames = options.keySet();
			final Iterator<String> varnameIt = varnames.iterator();
			while (varnameIt.hasNext()) {
				final String varname = varnameIt.next();
				if (!variables.contains(varname))
					continue;
				final Set<String> possibleValues = options.get(varname).keySet();
				final Map<ValueEntry, Integer> varMap = new LinkedHashMap<ValueEntry, Integer>();
				final Iterator<String> valueIt = possibleValues.iterator();
				while (valueIt.hasNext()) {
					final String value = valueIt.next();
					varMap.put(options.get(varname).get(value), 0);
				}
				process.put(new HeaderEntry(varname, null), varMap);
			}
		}
		final Map<String, String> questionTypes = this.getQuestionTypes(usingNodes);
//		// counting
		final Iterator<ParticipantEntity> partIt = participants.iterator();
		
		while (partIt.hasNext()) {
			final ParticipantEntity part = partIt.next();

			final Map<String, SurveyData> surveyData = part.getSurveyData();
			final Set<String> varnames = surveyData.keySet();
			final Iterator<String> varnameIt = varnames.iterator();
			while (varnameIt.hasNext()) {
				final String varname = varnameIt.next();
				if (!variables.contains(varname))
					continue;
				final SurveyData data = surveyData.get(varname);
				final String value = data.getValue();

				final HeaderEntry varEntry = new HeaderEntry(varname, null);
				final Map<ValueEntry, Integer> counterMap = process.get(varEntry);
				if (counterMap != null) {
					final Integer counter = counterMap.get(options.get(varname).get(value));

					if (counter != null) {
						counterMap.put(options.get(varname).get(value), counter + 1);
					} else {
						// retrieve Question type
//						final String type = this.getQuestionType(usingNodes, varname);
						String type = "";
						if(questionTypes.containsKey(varname))type = questionTypes.get(varname);
						if (type.equals("singleChoice")) {
						} else if (type.equals("multipleChoice")) {
						} else if (type.equals("open"))
							counterMap.put(new ValueEntry("OPEN", value,false), 1);
						else {
						}
					}
				} else {
//					LOGGER.error("No CounterMap for {}", varname);
				}
			}
		}
		
		if (back == null)
			back = new LinkedHashMap<HeaderEntry, Map<ValueEntry, Integer>>();

		final Set<HeaderEntry> header = process.keySet();
		final Iterator<HeaderEntry> it = header.iterator();
		while (it.hasNext()) {
			final HeaderEntry tmp = it.next();
			final Map<ValueEntry, Integer> value = process.get(tmp);
			tmp.setText(questions.get(tmp.getVariable()));
			back.put(tmp, value);
		}
		return back;
	}

	/**
	 * Gets the variables.
	 * 
	 * @param qmlPath
	 *            the qml path
	 * @return the variables
	 * @throws Exception 
	 */
	private List<String> getVariables(final String qmlPath,final NodeList usingNodes) throws Exception {

//		final NodeList usingNodes = XmlClient.getInstance().getByXPath(qmlPath, "//*[@variable]");

		final List<String> variables = new ArrayList<String>();
		final int count = usingNodes.getLength();
		for (int i = 0; i < count; ++i) {
			final Node e = usingNodes.item(i);
			if(XmlClient.getInstance().hasParent(e, "zofar:researchdata"))continue;
			final NamedNodeMap nodeAttributes = e.getAttributes();
			final Node variableNode = nodeAttributes.getNamedItem("variable");

			final String variable = variableNode.getTextContent();
			variables.add(variable);
		}
		return variables;
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
//	private String getQuestionType(final NodeList usingNodes, final String variablename) {
//		String back = "";
//
//		final int count = usingNodes.getLength();
//		for (int i = 0; i < count; ++i) {
//			final Node e = usingNodes.item(i);
//			if(XmlClient.getInstance().hasParent(e, "zofar:researchdata"))continue;
//			final NamedNodeMap nodeAttributes = e.getAttributes();
//			final Node variableNode = nodeAttributes.getNamedItem("variable");
//			final String nodeType = e.getNodeName();
//			final String variable = variableNode.getTextContent();
//			if (!variablename.equals(variable))
//				continue;
//			// LOGGER.info("found node : {}",nodeType);
//
//			if (nodeType.equals("zofar:responseDomain")) {
//				back = "singleChoice";
//			} else if (nodeType.equals("zofar:answerOption")) {
//				back = "multipleChoice";
//			} else if (nodeType.equals("zofar:SlotItem")) {
//				back = "multipleChoice";
//			} else if (nodeType.equals("zofar:questionOpen")) {
//				back = "open";
//			} else if (nodeType.equals("zofar:question")) {
//				back = "open";
//			} else {
//				// LOGGER.info("type : {}",nodeType);
//			}
//		}
//		return back;
//	}
	
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
	 * Gets the questions.
	 * 
	 * @param qmlPath
	 *            the qml path
	 * @param variables
	 *            the variables
	 * @return the questions
	 * @throws Exception 
	 */
	public Map<String, Set<String>> getQuestions(final String qmlPath, final List<String> variables,final NodeList usingNodes) throws Exception {
		final Map<String, Set<String>> back = new LinkedHashMap<String, Set<String>>();

//		final NodeList usingNodes = XmlClient.getInstance().getByXPath(qmlPath, "//*[@variable]");

		final int count = usingNodes.getLength();
		for (int i = 0; i < count; ++i) {
			final Node e = usingNodes.item(i);
			if(XmlClient.getInstance().hasParent(e, "zofar:researchdata"))continue;
			final NamedNodeMap nodeAttributes = e.getAttributes();
			final Node variableNode = nodeAttributes.getNamedItem("variable");
			final String nodeType = e.getNodeName();

			final String variable = variableNode.getTextContent();
			if (!variables.contains(variable))
				continue;
			Set<String> questionSet = null;
			if (back.containsKey(variable))
				questionSet = back.get(variable);
			if (questionSet == null)
				questionSet = new LinkedHashSet<String>();

			if (nodeType.equals("zofar:responseDomain")) {
				final Node parent = e.getParentNode();
				questionSet.addAll(this.retrieveHeader(parent));
			} else if (nodeType.equals("zofar:answerOption") || nodeType.equals("zofar:SlotItem")) {
				final NamedNodeMap attributes = e.getAttributes();
				final Node labelNode = attributes.getNamedItem("label");
				final StringBuffer label = new StringBuffer();
				if (labelNode != null)
					label.append(labelNode.getTextContent());

				if (labelNode == null) {
					final NodeList optionChilds = XmlClient.getInstance().getByXPath(e, "./label");
					if (optionChilds != null) {
						final int childCount = optionChilds.getLength();
						for (int b = 0; b < childCount; ++b) {
							final Node tmp = optionChilds.item(b);
							label.append(tmp.getTextContent());
							if (b < childCount - 1)
								label.append(" # ");
						}
					}
				}
				questionSet.add(ReplaceClient.getInstance().cleanedString(label.toString()));

				final Node parent = e.getParentNode();
				questionSet.addAll(this.retrieveHeader(parent));
				questionSet.addAll(this.retrieveHeader(parent.getParentNode()));
			} else if (nodeType.equals("zofar:questionOpen")) {
				questionSet.addAll(this.retrieveHeader(e));
			} else if (nodeType.equals("zofar:question")) {
				questionSet.addAll(this.retrieveHeader(e));
			} else {
				// LOGGER.info("type : {}",nodeType);
			}
			back.put(variable, questionSet);
		}
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
		final NodeList header = XmlClient.getInstance().getByXPath(parentNode, "./header");
		final Set<String> back = new LinkedHashSet<String>();

		if (header != null) {
			final int childCount = header.getLength();
			for (int a = 0; a < childCount; ++a) {
				final Node tmp = header.item(a);
				if(XmlClient.getInstance().hasParent(tmp, "zofar:researchdata"))continue;
				// LOGGER.info("header : {}",tmp.getNodeName());
				if (tmp.getNodeName().equals("zofar:header")) {
					final NodeList question = XmlClient.getInstance().getByXPath(tmp, "./question");
					final int questionCount = question.getLength();
					for (int b = 0; b < questionCount; ++b) {
						final Node questionText = question.item(b);
						// LOGGER.info("questionText : {}",questionText.getNodeName());
						final StringBuffer text = new StringBuffer();
						if (questionText != null)
							text.append(questionText.getTextContent());
						back.add(ReplaceClient.getInstance().cleanedString(text.toString()));
					}
				}
			}
		}
		return back;
	}

	/**
	 * Gets the options.
	 * Missed Labels for answerOptions in Matrix added from matrix header
	 * @param qmlPath
	 *            the qml path
	 * @param variables
	 *            the variables
	 * @return the options
	 * @throws Exception 
	 */
	
	public Map<String, Map<String, ValueEntry>> getOptions(final String qmlPath, final List<String> variables,final NodeList usingNodes) throws Exception {
		final Map<String, Map<String, ValueEntry>> back = new LinkedHashMap<String, Map<String, ValueEntry>>();

		final int count = usingNodes.getLength();
		for (int i = 0; i < count; ++i) {
			final Node e = usingNodes.item(i);
			if(XmlClient.getInstance().hasParent(e, "zofar:researchdata"))continue;
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
					final int optionsCount = options.getLength();
					int labelIndex = 0;
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
							if(missingNode != null){
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
										if (b < childCount - 1)label.append(" # ");
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
								optionSet.put(uid, new ValueEntry(value, ReplaceClient.getInstance().cleanedString(label.toString()),missing));
							}
							labelIndex = labelIndex + 1;
						}
					}
				}
			} else if (nodeType.equals("zofar:answerOption")) {
				optionSet.put("true", new ValueEntry("true", "Ja",false));
				optionSet.put("false", new ValueEntry("false", "Nein",false));
			} else if (nodeType.equals("zofar:SlotItem")) {
				optionSet.put("true", new ValueEntry("true", "Ja",false));
				optionSet.put("false", new ValueEntry("false", "Nein",false));
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
	
//	public Map<String, Map<String, ValueEntry>> getOptions(final String qmlPath, final List<String> variables,final NodeList usingNodes) throws Exception {
//		final Map<String, Map<String, ValueEntry>> back = new LinkedHashMap<String, Map<String, ValueEntry>>();
//
//		final int count = usingNodes.getLength();
//		for (int i = 0; i < count; ++i) {
//			final Node e = usingNodes.item(i);
//			if(XmlUtils.getInstance().hasParent(e, "zofar:researchdata"))continue;
//			final NamedNodeMap nodeAttributes = e.getAttributes();
//			final Node variableNode = nodeAttributes.getNamedItem("variable");
//			final String nodeType = e.getNodeName();
//
//			final String variable = variableNode.getTextContent();
//			if (!variables.contains(variable))
//				continue;
//			Map<String, ValueEntry> optionSet = null;
//			if (back.containsKey(variable))
//				optionSet = back.get(variable);
//			if (optionSet == null)
//				optionSet = new LinkedHashMap<String, ValueEntry>();
//
//			if (nodeType.equals("zofar:responseDomain")) {
//				final NodeList options = XmlClient.getInstance().getByXPath(e, ".//*");
//				if (options != null) {
//					final int optionsCount = options.getLength();
//					for (int a = 0; a < optionsCount; ++a) {
//						final Node node = options.item(a);
//						if (node.getNodeName().equals("zofar:answerOption")) {
//							final NamedNodeMap attributes = node.getAttributes();
//							final Node uidNode = attributes.getNamedItem("uid");
//							final String uid = uidNode.getTextContent();
//
//							final Node valueNode = attributes.getNamedItem("value");
//							final String value = valueNode.getTextContent();
//							
//							final Node missingNode = attributes.getNamedItem("missing");
//							boolean missing = false;
//							if(missingNode != null){
//								missing = Boolean.getBoolean(missingNode.getTextContent());
//							}
//
//							final Node labelNode = attributes.getNamedItem("label");
//							final StringBuffer label = new StringBuffer();
//							if (labelNode != null)
//								label.append(labelNode.getTextContent());
//
//							if (labelNode == null) {
//								final NodeList optionChilds = XmlClient.getInstance().getByXPath(node, "./label");
//								if (optionChilds != null) {
//									final int childCount = optionChilds.getLength();
//									for (int b = 0; b < childCount; ++b) {
//										final Node tmp = optionChilds.item(b);
//										label.append(tmp.getTextContent());
//										if (b < childCount - 1)
//											label.append(" # ");
//									}
//								}
//							}
//
//							if ((uid != null) && (label != null)) {
//								optionSet.put(uid, new ValueEntry(value, ReplaceClient.getInstance().cleanedString(label.toString()),missing));
//							}
//						}
//					}
//				}
//			} else if (nodeType.equals("zofar:answerOption")) {
//				optionSet.put("true", new ValueEntry("true", "Ja",false));
//				optionSet.put("false", new ValueEntry("false", "Nein",false));
//			} else if (nodeType.equals("zofar:SlotItem")) {
//				optionSet.put("true", new ValueEntry("true", "Ja",false));
//				optionSet.put("false", new ValueEntry("false", "Nein",false));
//			} else if (nodeType.equals("zofar:questionOpen")) {
//				// optionSet.put("OPEN", new ValueEntry("OPEN", "Freier Text"));
//			} else if (nodeType.equals("zofar:question")) {
//				// optionSet.put("OPEN", new ValueEntry("OPEN", "Freier Text"));
//			} else {
//				// LOGGER.info("type : {}",nodeType);
//			}
//			back.put(variable, optionSet);
//		}
//		return back;
//	}

}

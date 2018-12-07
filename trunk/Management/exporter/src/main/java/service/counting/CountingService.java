package service.counting;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import model.HeaderEntry;
import model.ParticipantEntity;
import model.SurveyData;
import model.ValueEntry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import service.data.DataService;
import utils.StringUtils;
import utils.XmlUtils;
//import de.his.zofar.persistence.surveyengine.entities.ParticipantEntity;
//import de.his.zofar.persistence.surveyengine.entities.SurveyDataEntity;

public class CountingService {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(CountingService.class);
	
	private static CountingService INSTANCE;

	private CountingService() {
		super();
	}

	public static CountingService getInstance() {
		if (INSTANCE == null)
			INSTANCE = new CountingService();
		return INSTANCE;
	}

	public Map<HeaderEntry, Map<ValueEntry, Integer>> buildUpDataMatrix(
			final String qmlPath, List<String> variables,final List<ParticipantEntity> participants) {
		final DataService dataService = DataService.getInstance();
//		final List<ParticipantEntity> participants = dataService.getParticipants(conf);
		if (variables == null)
			variables = this.getVariables(qmlPath);

		final Map<String, Map<String, ValueEntry>> options = this.getOptions(qmlPath, variables);

		final Map<HeaderEntry, Map<ValueEntry, Integer>> process = new LinkedHashMap<HeaderEntry, Map<ValueEntry, Integer>>();

		// Initialize
		if (options != null) {
			final Set<String> varnames = options.keySet();
			final Iterator<String> varnameIt = varnames.iterator();
			while (varnameIt.hasNext()) {
				final String varname = varnameIt.next();
				if (!variables.contains(varname))
					continue;
				final Set<String> possibleValues = options.get(varname)
						.keySet();
				final Map<ValueEntry, Integer> varMap = new LinkedHashMap<ValueEntry, Integer>();
				final Iterator<String> valueIt = possibleValues.iterator();
				while (valueIt.hasNext()) {
					final String value = valueIt.next();
					varMap.put(options.get(varname).get(value), 0);
				}
				process.put(new HeaderEntry(varname,null), varMap);
			}
		}

		// counting
		final Iterator<ParticipantEntity> partIt = participants.iterator();
		while (partIt.hasNext()) {
			final ParticipantEntity part = partIt.next();
//			final Map<String, SurveyData> surveyData = part
//					.getSurveyData();			
			final Map<String, SurveyData> surveyData = dataService.getData(part.getId(), dataService.getConfiguration());
			final Set<String> varnames = surveyData.keySet();
			final Iterator<String> varnameIt = varnames.iterator();
			while (varnameIt.hasNext()) {
				final String varname = varnameIt.next();
				if (!variables.contains(varname))
					continue;
				final SurveyData data = surveyData.get(varname);
				final String value = data.getValue();
				
				final HeaderEntry varEntry = new HeaderEntry(varname,null);
				final Map<ValueEntry, Integer> counterMap = process.get(varEntry);
				
				if (counterMap != null) {
					final Integer counter = counterMap.get(options.get(varname)
							.get(value));
					if (counter != null) {
						counterMap.put(options.get(varname).get(value),
								counter + 1);
					} else {
						//retrieve Question type
						final String type = this.getQuestionType(qmlPath, varname);
//						LOGGER.info("type : {}",type);
						if(!type.equals("singleChoice"))counterMap.put(new ValueEntry("OPEN", value), 1);
						else{
//							if(varname.equals("vsc_1_1"))LOGGER.info("NOT ANSWERED ({}) : {}",part.getToken(),varname);
						}
					}
				} else {
					LOGGER.error("No CounterMap for {}", varname);
				}
			}
		}
		final Map<String, Set<String>> questions = getQuestions(qmlPath,variables);
		final Map<HeaderEntry, Map<ValueEntry, Integer>> back = new LinkedHashMap<HeaderEntry, Map<ValueEntry, Integer>>();

		final Set<HeaderEntry> header = process.keySet();
		final Iterator<HeaderEntry> it = header.iterator();
		while(it.hasNext()){
			final HeaderEntry tmp = it.next();
			final Map<ValueEntry, Integer> value = process.get(tmp);
			tmp.setText(questions.get(tmp.getVariable()));
			back.put(tmp, value);
		}
		
		return back;
	}

	private List<String> getVariables(final String qmlPath) {
//		final NodeList usingNodes = XmlUtils.getInstance().getByXPath(qmlPath,
//				"//parent::*[@variable]");		
		final NodeList usingNodes = XmlUtils.getInstance().getByXPath(qmlPath,
						"//*[@variable]");

		final List<String> variables = new ArrayList<String>();
		final int count = usingNodes.getLength();
		for (int i = 0; i < count; ++i) {
			Node e = (Node) usingNodes.item(i);

			final NamedNodeMap nodeAttributes = e.getAttributes();
			final Node variableNode = nodeAttributes.getNamedItem("variable");

			final String variable = variableNode.getTextContent();
			variables.add(variable);
		}
		return variables;
	}
	
	private String getQuestionType(final String qmlPath,final String variablename) {
		String back = "";
//		final NodeList usingNodes = XmlUtils.getInstance().getByXPath(qmlPath,
//				"//parent::*[@variable]");
		final NodeList usingNodes = XmlUtils.getInstance().getByXPath(qmlPath,
				"//*[@variable]");
		

		final int count = usingNodes.getLength();
		for (int i = 0; i < count; ++i) {
			Node e = (Node) usingNodes.item(i);

			final NamedNodeMap nodeAttributes = e.getAttributes();
			final Node variableNode = nodeAttributes.getNamedItem("variable");
			final String nodeType = e.getNodeName();
			final String variable = variableNode.getTextContent();
			if(!variablename.equals(variable))continue;
//			LOGGER.info("found node : {}",nodeType);

			if (nodeType.equals("zofar:responseDomain")) {
				back="singleChoice";
			}
			else if (nodeType.equals("zofar:answerOption")) {
				back="multipleChoice";
			}
			else if (nodeType.equals("zofar:questionOpen")) {
				back="open";
			}
			else if (nodeType.equals("zofar:question")) {
				back="open";
			}
			else{
//				LOGGER.info("type : {}",nodeType);
			}
		}
		return back;
	}

	private Map<String, Set<String>> getQuestions(final String qmlPath,final List<String> variables) {
		final Map<String, Set<String>> back = new LinkedHashMap<String, Set<String>>();

//		final NodeList usingNodes = XmlUtils.getInstance().getByXPath(qmlPath,
//				"//parent::*[@variable]");		
		final NodeList usingNodes = XmlUtils.getInstance().getByXPath(qmlPath,
						"//*[@variable]");

		final int count = usingNodes.getLength();
		for (int i = 0; i < count; ++i) {
			Node e = (Node) usingNodes.item(i);

			final NamedNodeMap nodeAttributes = e.getAttributes();
			final Node variableNode = nodeAttributes.getNamedItem("variable");
			final String nodeType = e.getNodeName();

			final String variable = variableNode.getTextContent();
			if(!variables.contains(variable))continue;
			Set<String> questionSet = null;
			if (back.containsKey(variable))
				questionSet = back.get(variable);
			if (questionSet == null)
				questionSet = new LinkedHashSet<String>();

			if (nodeType.equals("zofar:responseDomain")) {
				final Node parent = e.getParentNode();
				questionSet.addAll(retrieveHeader(parent));			
			}
			else if (nodeType.equals("zofar:answerOption")) {
				final NamedNodeMap attributes = e.getAttributes();
				final Node labelNode = attributes.getNamedItem("label");
				final StringBuffer label = new StringBuffer();
				if (labelNode != null)label.append(labelNode.getTextContent());

				if (labelNode == null) {
					NodeList optionChilds = XmlUtils.getInstance().getByXPath(e, "./label");
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
				questionSet.add(StringUtils.getInstance().cleanedString(label.toString()));
				
				final Node parent = e.getParentNode();
				questionSet.addAll(retrieveHeader(parent));	
				questionSet.addAll(retrieveHeader(parent.getParentNode()));	
			}
			else if (nodeType.equals("zofar:questionOpen")) {
				questionSet.addAll(retrieveHeader(e));		
			}
			else if (nodeType.equals("zofar:question")) {
				questionSet.addAll(retrieveHeader(e));		
			}
			else{
//				LOGGER.info("type : {}",nodeType);
			}
			back.put(variable, questionSet);
		}
		return back;
	}
	
	private Set<String> retrieveHeader(final Node parentNode){
		NodeList header = XmlUtils.getInstance().getByXPath(parentNode, "./header");
		final Set<String> back = new LinkedHashSet<String>();
		
		if(header != null){
			final int childCount = header.getLength();
			for (int a = 0; a < childCount; ++a) {
				final Node tmp = header.item(a);
//				LOGGER.info("header : {}",tmp.getNodeName());
				if (tmp.getNodeName().equals("zofar:header")) {
					NodeList question = XmlUtils.getInstance().getByXPath(tmp, "./question");
					final int questionCount = question.getLength();
					for (int b = 0; b < questionCount; ++b) {
						final Node questionText = question.item(b);
//						LOGGER.info("questionText : {}",questionText.getNodeName());
						StringBuffer text = new StringBuffer();
						if(questionText != null)text.append(questionText.getTextContent());
						back.add(StringUtils.getInstance().cleanedString(text.toString()));
					}
				}
			}
		}
		return back;
	}
	
	private Map<String, Map<String, ValueEntry>> getOptions(
			final String qmlPath, final List<String> variables) {
		final Map<String, Map<String, ValueEntry>> back = new LinkedHashMap<String, Map<String, ValueEntry>>();

//		final NodeList usingNodes = XmlUtils.getInstance().getByXPath(qmlPath,
//				"//parent::*[@variable]");
		
		final NodeList usingNodes = XmlUtils.getInstance().getByXPath(qmlPath,
				"//*[@variable]");

		final int count = usingNodes.getLength();
		for (int i = 0; i < count; ++i) {
			Node e = (Node) usingNodes.item(i);

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
				NodeList options = XmlUtils.getInstance().getByXPath(e, ".//*");
				if (options != null) {
					final int optionsCount = options.getLength();
					for (int a = 0; a < optionsCount; ++a) {
						final Node node = options.item(a);
						if (node.getNodeName().equals("zofar:answerOption")) {
							final NamedNodeMap attributes = node.getAttributes();
							final Node uidNode = attributes.getNamedItem("uid");
							final String uid = uidNode.getTextContent();

							final Node valueNode = attributes.getNamedItem("value");
							final String value = valueNode.getTextContent();

							final Node labelNode = attributes.getNamedItem("label");
							final StringBuffer label = new StringBuffer();
							if (labelNode != null)label.append(labelNode.getTextContent());

							if (labelNode == null) {
								NodeList optionChilds = XmlUtils.getInstance().getByXPath(node, "./label");
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

							if ((uid != null) && (label != null)) {
								optionSet.put(uid, new ValueEntry(value,StringUtils.getInstance().cleanedString(label.toString())));
							}
						}
					}
				}
			} else if (nodeType.equals("zofar:answerOption")) {
				optionSet.put("true", new ValueEntry("true", "Ja"));
				optionSet.put("false", new ValueEntry("false", "Nein"));
			} else if (nodeType.equals("zofar:questionOpen")) {
				// optionSet.put("OPEN", new ValueEntry("OPEN", "Freier Text"));
			} 
			else if (nodeType.equals("zofar:question")) {
				// optionSet.put("OPEN", new ValueEntry("OPEN", "Freier Text"));
			}
			else {
				// LOGGER.info("type : {}",nodeType);
			}
			back.put(variable, optionSet);
		}
		return back;
	}

}

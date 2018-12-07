package service.codebook;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import model.HeaderEntry;
import model.ValueEntry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import utils.StringUtils;
import utils.XmlUtils;

public class CodebookService {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(CodebookService.class);

	private static CodebookService INSTANCE;

	private CodebookService() {
		super();
	}

	public static CodebookService getInstance() {
		if (INSTANCE == null)
			INSTANCE = new CodebookService();
		return INSTANCE;
	}

	public Map<HeaderEntry, Map<String, ValueEntry>> buildUpDataMatrix(
			final String qmlPath) {
		final Map<String, Set<String>> questions = getQuestions(qmlPath);
		final Map<String, Map<String, ValueEntry>> options = this.getOptions(qmlPath);

		final Map<HeaderEntry, Map<String, ValueEntry>> back = new LinkedHashMap<HeaderEntry, Map<String, ValueEntry>>();

		for (Map.Entry<String, Set<String>> question : questions.entrySet()) {
			 Set<String> header = question.getValue();
			 Map<String, ValueEntry> questionOptions = options.get(question.getKey());
			 final HeaderEntry entry = new HeaderEntry(question.getKey(),header);
			 back.put(entry, questionOptions);
		}
		return back;
	}

	private Map<String, Set<String>> getQuestions(final String qmlPath) {
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
			final String variable = variableNode.getTextContent();
			back.put(variable, getQuestionsHelper(e));
		}
		return back;
	}

	private Set<String> getQuestionsHelper(final Node node) {
		final Set<String> back = new LinkedHashSet<String>();

		final String nodeType = node.getNodeName();
		if (nodeType.startsWith("zofar:")) {
			back.addAll(getQuestionsHelper(node.getParentNode()));
		}
		back.addAll(retrieveHeader(node));
		return back;
	}

	// private Map<String, Set<String>> getQuestions(final String qmlPath) {
	// final Map<String, Set<String>> back = new LinkedHashMap<String,
	// Set<String>>();
	//
	// final NodeList usingNodes = XmlUtils.getInstance().getByXPath(qmlPath,
	// "//parent::*[@variable]");
	//
	// final int count = usingNodes.getLength();
	// for (int i = 0; i < count; ++i) {
	// Node e = (Node) usingNodes.item(i);
	//
	// final NamedNodeMap nodeAttributes = e.getAttributes();
	// final Node variableNode = nodeAttributes.getNamedItem("variable");
	// final String nodeType = e.getNodeName();
	//
	// final String variable = variableNode.getTextContent();
	// Set<String> questionSet = null;
	// if (back.containsKey(variable))
	// questionSet = back.get(variable);
	// if (questionSet == null)
	// questionSet = new LinkedHashSet<String>();
	//
	// if (nodeType.equals("zofar:responseDomain")) {
	// final Node parent = e.getParentNode();
	// questionSet.addAll(retrieveHeader(parent));
	// if(parent.getNodeName().equals("zofar:item")){
	// questionSet.addAll(retrieveHeader(parent.getParentNode()));
	// }
	// }
	// else if (nodeType.equals("zofar:answerOption")) {
	// final NamedNodeMap attributes = e.getAttributes();
	// final Node labelNode = attributes.getNamedItem("label");
	// final StringBuffer label = new StringBuffer();
	// if (labelNode != null)label.append(labelNode.getTextContent());
	//
	// if (labelNode == null) {
	// NodeList optionChilds = XmlUtils.getInstance().getByXPath(e, "./label");
	// if (optionChilds != null) {
	// final int childCount = optionChilds.getLength();
	// for (int b = 0; b < childCount; ++b) {
	// final Node tmp = optionChilds.item(b);
	// label.append(tmp.getTextContent());
	// if (b < childCount - 1)
	// label.append(" # ");
	// }
	// }
	// }
	// questionSet.add(StringUtils.getInstance().cleanedString(label.toString()));
	//
	// final Node parent = e.getParentNode();
	// questionSet.addAll(retrieveHeader(parent));
	// questionSet.addAll(retrieveHeader(parent.getParentNode()));
	// if(parent.getParentNode().getNodeName().equals("zofar:item")){
	// questionSet.addAll(retrieveHeader(parent.getParentNode().getParentNode()));
	// }
	// }
	// else if (nodeType.equals("zofar:questionOpen")) {
	// questionSet.addAll(retrieveHeader(e));
	// }
	// else{
	// // LOGGER.info("type : {}",nodeType);
	// }
	// back.put(variable, questionSet);
	// }
	// return back;
	// }

	private Set<String> retrieveHeader(final Node parentNode) {
		NodeList header = XmlUtils.getInstance().getByXPath(parentNode,
				"./header");
		final Set<String> back = new LinkedHashSet<String>();

		if (header != null) {
			final int childCount = header.getLength();
			for (int a = 0; a < childCount; ++a) {
				final Node tmp = header.item(a);
				if (tmp.getNodeName().equals("zofar:header")) {
					NodeList question = XmlUtils.getInstance().getByXPath(tmp,
							"./question");
					final int questionCount = question.getLength();
					for (int b = 0; b < questionCount; ++b) {
						final Node questionText = question.item(b);
						// LOGGER.info("questionText : {}",questionText.getNodeName());
						StringBuffer text = new StringBuffer();
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

	private Map<String, Map<String, ValueEntry>> getOptions(final String qmlPath) {
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
								NodeList optionChilds = XmlUtils.getInstance()
										.getByXPath(node, "./label");
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
				optionSet.put("OPEN", new ValueEntry("OPEN", "Freier Text"));
			} 
			else if (nodeType.equals("zofar:question")) {
				optionSet.put("OPEN", new ValueEntry("OPEN", "Freier Text"));
			}
			else {
				// LOGGER.info("type : {}",nodeType);
			}
			back.put(variable, optionSet);
		}
		return back;
	}

}

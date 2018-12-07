package service.codebook;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import eu.dzhw.zofar.management.utils.string.ReplaceClient;
import eu.dzhw.zofar.management.utils.xml.XmlClient;
import model.HeaderEntry;
import model.ValueEntry;

// TODO: Auto-generated Javadoc
/**
 * The Class CodebookService.
 */
public class CodebookService {

	/** The Constant LOGGER. */
	private static final Logger LOGGER = LoggerFactory.getLogger(CodebookService.class);

	/** The instance. */
	private static CodebookService INSTANCE;

	/**
	 * Instantiates a new codebook service.
	 */
	private CodebookService() {
		super();
	}

	/**
	 * Gets the single instance of CodebookService.
	 * 
	 * @return single instance of CodebookService
	 */
	public static CodebookService getInstance() {
		if (INSTANCE == null)
			INSTANCE = new CodebookService();
		return INSTANCE;
	}

	/**
	 * Builds the up data matrix.
	 * 
	 * @param qmlPath
	 *            the qml path
	 * @return the map
	 * @throws Exception
	 */
	public Map<String, Set<Node>> buildUpDataMatrixNew(final String qmlPath) throws Exception {
		Map<Node, String> filtered = getFilteredQuestions(qmlPath);

		final Map<String, Set<Node>> pageSorted = new LinkedHashMap<String, Set<Node>>();

		for (final Map.Entry<Node, String> question : filtered.entrySet()) {
			final String pageId = question.getValue();
			final Node questionNode = question.getKey();

			Set<Node> pageSet = null;
			if (pageSorted.containsKey(pageId))
				pageSet = pageSorted.get(pageId);
			if (pageSet == null)
				pageSet = new LinkedHashSet<Node>();
			pageSet.add(questionNode);
			pageSorted.put(pageId, pageSet);
		}
		return pageSorted;
	}

	private Map<Node, String> getFilteredQuestions(final String qmlPath) throws Exception {
		final Map<Node, String> back = new LinkedHashMap<Node, String>();

		final Set<String> neededTypes = new HashSet<String>();

		neededTypes.add("zofar:multipleChoice");
		neededTypes.add("zofar:questionSingleChoice");
		neededTypes.add("zofar:questionOpen");
		neededTypes.add("zofar:questionPretest");

		neededTypes.add("zofar:matrixQuestionMixed");
		neededTypes.add("zofar:matrixDouble");
		neededTypes.add("zofar:comparison");
		neededTypes.add("zofar:matrixQuestionSingleChoice");
		neededTypes.add("zofar:matrixMultipleChoice");
		neededTypes.add("zofar:matrixQuestionOpen");
		 neededTypes.add("zofar:calendar");
		// neededTypes.add("");

		final NodeList pageNodes = XmlClient.getInstance().getByXPath(qmlPath, "page");
		final int count = pageNodes.getLength();
		for (int i = 0; i < count; ++i) {
			final Node pageNode = pageNodes.item(i);
			final NamedNodeMap attributes = pageNode.getAttributes();
			final Node uidNode = attributes.getNamedItem("uid");
			final String pageId = uidNode.getTextContent();
			
			getFilteredQuestionsHelper(pageNode,back,neededTypes,pageId);
		}

		return back;
	}
	
	private void getFilteredQuestionsHelper(Node node,final Map<Node, String> back,final Set<String> neededTypes,final String pageId){
		final NodeList nodeBody = XmlClient.getInstance().getByXPath(node, "body/*[@uid]");
		final int bodyCount = nodeBody.getLength();
		for (int j = 0; j < bodyCount; ++j) {
			final Node bodyNode = nodeBody.item(j);
			if(XmlClient.getInstance().hasParent(bodyNode, "zofar:researchdata"))continue;
			final String bodyNodeType = bodyNode.getNodeName();
			if(neededTypes.contains(bodyNodeType)){
				back.put(bodyNode, pageId);
			}
			else if (bodyNodeType.equals("zofar:section")){
				getFilteredQuestionsHelper(bodyNode,back,neededTypes,pageId);
			}
			else{
				System.out.println("unhandeled body Node Type : "+bodyNodeType);
			}
		}
	}

	public Map<HeaderEntry, Map<String, ValueEntry>> buildUpDataMatrix(final String qmlPath,final NodeList usingNodes) throws Exception {
//		final NodeList usingNodes = XmlClient.getInstance().getByXPath(qmlPath, "//*[@variable]");
		final Map<String, Set<String>> questions = this.getQuestions(usingNodes);
		final Map<String, Map<String, ValueEntry>> options = this.getOptions(usingNodes);

		final Map<HeaderEntry, Map<String, ValueEntry>> back = new LinkedHashMap<HeaderEntry, Map<String, ValueEntry>>();

		for (final Map.Entry<String, Set<String>> question : questions.entrySet()) {
			final Set<String> header = question.getValue();
			// LOGGER.info("Header of {} : {}", question.getKey(), header);
			final Map<String, ValueEntry> questionOptions = options.get(question.getKey());
			final HeaderEntry entry = new HeaderEntry(question.getKey(), header);
			back.put(entry, questionOptions);
		}
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
	private Map<String, Set<String>> getQuestions(final NodeList usingNodes) throws Exception {
		final Map<String, Set<String>> back = new LinkedHashMap<String, Set<String>>();
		final int count = usingNodes.getLength();
		for (int i = 0; i < count; ++i) {
			final Node e = usingNodes.item(i);
			if (XmlClient.getInstance().hasParent(e, "zofar:researchdata"))
				continue;
			final NamedNodeMap nodeAttributes = e.getAttributes();
			final Node variableNode = nodeAttributes.getNamedItem("variable");
			final String variable = variableNode.getTextContent();
			back.put(variable, this.getQuestionsHelper(e));
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
	 * Gets the options.
	 * 
	 * @param qmlPath
	 *            the qml path
	 * @return the options
	 * @throws Exception
	 */
	private Map<String, Map<String, ValueEntry>> getOptions(final NodeList usingNodes) throws Exception {
		final Map<String, Map<String, ValueEntry>> back = new LinkedHashMap<String, Map<String, ValueEntry>>();

		final int count = usingNodes.getLength();
		for (int i = 0; i < count; ++i) {
			final Node e = usingNodes.item(i);
			if (XmlClient.getInstance().hasParent(e, "zofar:researchdata"))
				continue;
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
				final NodeList options = XmlClient.getInstance().getByXPath(e, ".//*");
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

							final Node missingNode = attributes.getNamedItem("missing");
							boolean missing = false;
							if (missingNode != null) {
								missing = Boolean.parseBoolean(missingNode.getTextContent());
								// System.out.println("Option : uid: "+uid+"
								// missingNode :
								// "+missingNode.getTextContent()+" missing :
								// "+missing);
							}

							final Node labelNode = attributes.getNamedItem("label");
							final StringBuffer label = new StringBuffer();
							if (labelNode != null)
								label.append(labelNode.getTextContent());

							if (labelNode == null) {
								final NodeList optionChilds = XmlClient.getInstance().getByXPath(node, "./label");
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
								optionSet.put(uid, new ValueEntry(value, ReplaceClient.getInstance().cleanedString(label.toString()), missing));
							}
						}
					}
				}
			} else if (nodeType.equals("zofar:answerOption")) {
				optionSet.put("true", new ValueEntry("true", "Ja", false));
				optionSet.put("false", new ValueEntry("false", "Nein", false));
			} else if (nodeType.equals("zofar:questionOpen")) {
				optionSet.put("OPEN", new ValueEntry("OPEN", "Freier Text", false));
			} else if (nodeType.equals("zofar:question")) {
				optionSet.put("OPEN", new ValueEntry("OPEN", "Freier Text", false));
			} else {
				// LOGGER.info("type : {}",nodeType);
			}
			back.put(variable, optionSet);
		}
		return back;
	}

}

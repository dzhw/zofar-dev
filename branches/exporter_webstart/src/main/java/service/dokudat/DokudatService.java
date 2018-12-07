package service.dokudat;

import java.util.HashMap;
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

public class DokudatService {

	/** The Constant LOGGER. */
	private static final Logger LOGGER = LoggerFactory.getLogger(DokudatService.class);

	/** The instance. */
	private static DokudatService INSTANCE;
	
	
	/**
	 * Gets the single instance of DokudatService.
	 * 
	 * @return single instance of DokudatService
	 */
	public static DokudatService getInstance() {
		if (INSTANCE == null)
			INSTANCE = new DokudatService();
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
	public Map<HeaderEntry, Object> buildUpDataMatrix(final String qmlPath,final NodeList usingNodes) throws Exception {
		final Map<HeaderEntry, Object> back = new LinkedHashMap<HeaderEntry, Object>();
		
		final Map<String, Map<String,Object>> questions = this.getQuestions(qmlPath,usingNodes);
		for (final Map.Entry<String, Map<String,Object>> question : questions.entrySet()) {
			final Map<String,Object> data = question.getValue();
			final HeaderEntry entry = new HeaderEntry(question.getKey(), (Set<String>)data.get("header"));
			final Map<String,Object> tmp = new HashMap<String,Object>();
			tmp.put("type", data.get("type"));
			tmp.put("values", data.get("values"));
			back.put(entry, tmp);
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
	private Map<String, Map<String,Object>> getQuestions(final String qmlPath,final NodeList usingNodes) throws Exception {
		final Map<String, Map<String,Object>> back = new LinkedHashMap<String, Map<String,Object>>();
//		final NodeList usingNodes = XmlClient.getInstance().getByXPath(qmlPath, "//*[@variable]");
		final int count = usingNodes.getLength();
		for (int i = 0; i < count; ++i) {
			final Node e = usingNodes.item(i);
			if(XmlClient.getInstance().hasParent(e, "zofar:researchdata"))continue;
			final String type = e.getNodeName();
			final NamedNodeMap nodeAttributes = e.getAttributes();
			final Node variableNode = nodeAttributes.getNamedItem("variable");
			final String variable = variableNode.getTextContent();
			final Map<String,Object> data = new HashMap<String,Object>();
			String questionType = "UNKOWN";
	    	if(type.equals("zofar:responseDomain")) questionType ="singlechoice";
	    	if(type.equals("zofar:questionOpen")) questionType ="open";
	    	if(type.equals("zofar:attachedOpen")) questionType ="open";
	    	if(type.equals("zofar:question")) questionType ="open";
	    	if(type.equals("zofar:answerOption")) questionType ="multiplechoice";
	    	if(type.equals("zofar:left")) questionType ="singlechoice";
	    	if(type.equals("zofar:right")) questionType ="singlechoice";
	    	if(type.equals("zofar:variable")) questionType = "variable";

			
			data.put("type", questionType);
			data.put("header", this.getQuestionsHelper(e));
			final Map<String,String> values = new LinkedHashMap<String,String>();
			if(questionType.equals("singlechoice")){	
		    	NodeList optionNodes = XmlClient.getInstance().getByXPath(e,"descendant::answerOption");
				final int optionsCount = optionNodes.getLength();
				for (int j = 0; j < optionsCount; ++j) {
					final Node optionNode = optionNodes.item(j);
					final NamedNodeMap optionAttributes = optionNode.getAttributes();
					
					final Node valueNode = optionAttributes.getNamedItem("value");
					final String value = valueNode.getTextContent();
					
					String label = "UNKOWN";
					final Node labelNode = optionAttributes.getNamedItem("label");
//					if(labelNode != null) label = labelNode.getTextContent();
					if(labelNode != null) label = ReplaceClient.getInstance().cleanedString(labelNode.getTextContent());
					
					values.put(value,label);
				}
			}			
			else if(questionType.equals("multiplechoice")){			
				values.put("true","ja");
				values.put("false","nein");
			}
			data.put("values", values);
			back.put(variable, data);
		}
		return back;
	}
	
//	/**
//	 * Gets the questions.
//	 * 
//	 * @param qmlPath
//	 *            the qml path
//	 * @return the questions
//	 */
//	private Map<String, Set<String>> getAnswers(final String qmlPath) {
//		final Map<String, Set<String>> back = new LinkedHashMap<String, Set<String>>();
//		final NodeList usingNodes = XmlClient.getInstance().getByXPath(qmlPath, "//*[@variable]");
//		final int count = usingNodes.getLength();
//		for (int i = 0; i < count; ++i) {
//			final Node e = usingNodes.item(i);
//			final String type = e.getNodeName();
//			System.out.println("type : "+type);
////			final NamedNodeMap nodeAttributes = e.getAttributes();
////			final Node variableNode = nodeAttributes.getNamedItem("variable");
////			final String variable = variableNode.getTextContent();
////			back.put(variable, this.getQuestionsHelper(e));
//		}
//		return back;
//	}
//	
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
				if(XmlClient.getInstance().hasParent(tmp, "zofar:researchdata"))continue;
				if (tmp.getNodeName().equals("zofar:header")) {
					final NodeList question = XmlClient.getInstance().getByXPath(tmp, "./question");
					final int questionCount = question.getLength();
					for (int b = 0; b < questionCount; ++b) {
						final Node questionText = question.item(b);
						if(XmlClient.getInstance().hasParent(questionText, "zofar:researchdata"))continue;
						// LOGGER.info("questionText : {}",questionText.getNodeName());
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

}

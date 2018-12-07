package presentation.codebook.format.odf.renderer;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import org.odftoolkit.simple.style.StyleTypeDefinitions.HorizontalAlignmentType;
import org.odftoolkit.simple.text.Paragraph;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import eu.dzhw.zofar.management.utils.odf.components.SimpleWriterDocument;
import eu.dzhw.zofar.management.utils.xml.XmlClient;

public abstract class ODFRenderer {
	
	/** The Constant LOGGER. */
	private static final Logger LOGGER = LoggerFactory.getLogger(ODFRenderer.class);

	/** The instance. */
	private static Map<String,ODFRenderer> INSTANCE;

	protected  ODFRenderer() {
		super();
	}
	
	public static ODFRenderer getInstance(final Node current) {
		if (INSTANCE == null){
			INSTANCE = new HashMap<String,ODFRenderer>();
		}
		String type = "UNKOWN";
		if(current != null){
			type = current.getNodeName();
		}
		
//		System.out.println("Type : "+type);
		
		if(!INSTANCE.containsKey(type)){
			if (type.equals("zofar:questionSingleChoice")) INSTANCE.put(type, new SCODFRenderer());
			else if (type.equals("zofar:multipleChoice")) INSTANCE.put(type, new MCODFRenderer());
			else if (type.equals("zofar:questionOpen")) INSTANCE.put(type, new QOODFRenderer());
			else if (type.equals("zofar:question")) INSTANCE.put(type, new QOODFRenderer());
			else if (type.equals("zofar:matrixQuestionSingleChoice")) INSTANCE.put(type, new MSCODFRenderer());
			else if (type.equals("zofar:matrixMultipleChoice")) INSTANCE.put(type, new MMCODFRenderer());
			else if (type.equals("zofar:matrixQuestionOpen")) INSTANCE.put(type, new MQOODFRenderer());
			else if (type.equals("zofar:matrixQuestionMixed")) INSTANCE.put(type, new MMXODFRenderer());
			else if (type.equals("zofar:matrixDouble")) INSTANCE.put(type, new MDOUBLEODFRenderer());
			else if (type.equals("zofar:calendar")) INSTANCE.put(type, new CalendarODFRenderer());
			
			else if (type.equals("zofar:responseDomain")) INSTANCE.put(type, new RDCODFRenderer());
			else if (type.equals("zofar:answerOption")) INSTANCE.put(type, new AnswerOptionODFRenderer());
			else if (type.equals("zofar:item")) INSTANCE.put(type, new ItemODFRenderer());
			else if (type.equals("zofar:SlotItem")) INSTANCE.put(type, new SlotItemODFRenderer());
			else if (type.equals("zofar:SlotConfiguration")) INSTANCE.put(type, new SlotConfODFRenderer());
			else if (type.equals("zofar:header")) INSTANCE.put(type, new HeaderODFRenderer());
			
			else if (type.equals("zofar:leftHeader")) INSTANCE.put(type, new HeaderODFRenderer());
			else if (type.equals("zofar:rightHeader")) INSTANCE.put(type, new HeaderODFRenderer());
			else if (type.equals("zofar:leftScaleHeader")) INSTANCE.put(type, new HeaderODFRenderer());
			else if (type.equals("zofar:rightScaleHeader")) INSTANCE.put(type, new HeaderODFRenderer());
			
			else if (type.equals("zofar:body")) INSTANCE.put(type, new BodyODFRenderer());
			else if (type.equals("zofar:configuration")) INSTANCE.put(type, new BodyODFRenderer());

			else if (type.equals("zofar:unit")) INSTANCE.put(type, new UnitODFRenderer());
			
			else if (type.equals("zofar:left")) INSTANCE.put(type, new LEFTODFRenderer());
			else if (type.equals("zofar:right")) INSTANCE.put(type, new RIGHTODFRenderer());
			
			else if (type.equals("#text")) INSTANCE.put(type, new TextODFRenderer());
			
			else{
				System.err.println("unrendered type : " + type+" ==> "+current.getParentNode().getNodeName());
			}
		}
		
		return INSTANCE.get(type);
	}
	
	protected String getUID(final Node parentNode) {
		if (parentNode == null)
			return null;

		final NamedNodeMap nodeAttributes = parentNode.getAttributes();
		if (nodeAttributes != null) {
			final Node uidNode = nodeAttributes.getNamedItem("uid");
			if (uidNode != null)
				return uidNode.getTextContent();
		}
		return null;
	}
	
	protected String getPath(final Node parentNode) {
		if (parentNode == null)
			return null;
		
		String uidPath = "";
		
		if(parentNode.getNodeName().startsWith("zofar:")){
			final NamedNodeMap nodeAttributes = parentNode.getAttributes();
			if (nodeAttributes != null) {
				final Node uidNode = nodeAttributes.getNamedItem("uid");
				if (uidNode != null)
					uidPath = uidNode.getTextContent();
			}
			if(!uidPath.equals(""))uidPath = ":"+uidPath;
			uidPath = getPath(parentNode.getParentNode())+uidPath;
		}
		if(uidPath.startsWith(":"))uidPath = uidPath.substring(1);
		return uidPath;
	}
	
	protected Set<String> getVisible(final Node node) {
		if (node == null)
			return null;
		
		Set<String> back = null;
		
		Node parent = node.getParentNode();
		if(parent != null){
			if (XmlClient.getInstance().hasParent(parent, "zofar:responseDomain")) {
			}			
			else if (node.getNodeName().equals("zofar:header") || XmlClient.getInstance().hasParent(parent, "zofar:header")) {
			}
			else if (XmlClient.getInstance().hasParent(parent, "zofar:page")) {
				Set<String> parentBack = getVisible(parent);
				if(parentBack != null){
					if(back == null) back = new LinkedHashSet<String>();
					back.addAll(parentBack);
				}
			}
		}
		final NamedNodeMap nodeAttributes = node.getAttributes();
		if (nodeAttributes != null) {
			final Node visibleNode = nodeAttributes.getNamedItem("visible");
			if (visibleNode != null){
				if(back == null) back = new LinkedHashSet<String>();
				back.add(visibleNode.getTextContent());
			}
		}
		return back;
	}
	
//	protected Set<String> getVisible(final Node node) {
//		if (node == null)
//			return null;
//		
//		Set<String> back = null;
//		
//		Node parent = node.getParentNode();
//		if((parent != null)&&((XmlUtils.getInstance().hasParent(parent, "zofar:page")))){
//			Set<String> parentBack = getVisible(parent);
//			if(parentBack != null){
//				if(back == null) back = new LinkedHashSet<String>();
//				back.addAll(parentBack);
//			}
//		}
//		final NamedNodeMap nodeAttributes = node.getAttributes();
//		if (nodeAttributes != null) {
//			final Node visibleNode = nodeAttributes.getNamedItem("visible");
//			if (visibleNode != null){
//				if(back == null) back = new LinkedHashSet<String>();
//				back.add(visibleNode.getTextContent());
//			}
//		}
//		return back;
//	}
	
	protected String getVariable(final Node parentNode) {
		if (parentNode == null)
			return null;
		// parentNode is rdc or openquestion or answeroption with variable
		// attribute
		final NamedNodeMap nodeAttributes = parentNode.getAttributes();
		if (nodeAttributes != null) {
			final Node variableNode = nodeAttributes.getNamedItem("variable");
			if (variableNode != null)
				return variableNode.getTextContent();
		}
		// parentNode is a singleChoice Question
		final NodeList rdcs = XmlClient.getInstance().getByXPath(parentNode, "./responseDomain[@variable]");
		if (rdcs != null) {
			final int childCount = rdcs.getLength();
			for (int a = 0; a < childCount; ++a) {
				final Node rdc = rdcs.item(a);
				final NamedNodeMap rdcAttributes = rdc.getAttributes();
				if (rdcAttributes != null) {
					final Node variableNode = rdcAttributes.getNamedItem("variable");
					if (variableNode != null)
						return variableNode.getTextContent();
				}
			}
		}
		return null;
	}
	
	protected Paragraph addQuestionType(final String type, SimpleWriterDocument doc) throws Exception{
//		final Font font = new Font("Arial", FontStyle.ITALIC, 12,Color.GRAY);
		final Paragraph para = doc.addText(type);
		para.setHorizontalAlignment(HorizontalAlignmentType.RIGHT);
		return para;
	}
	
	public void renderChildren(final String pageId, final Node question,final Node current, final SimpleWriterDocument doc, final Map<String, Object> attachements,final Object container) throws Exception{
		renderChildren(pageId,question,current,doc,attachements,container,false);
	}

	
	public void renderChildren(final String pageId, final Node question,final Node current, final SimpleWriterDocument doc, final Map<String, Object> attachements,final Object container,final boolean attachementMode) throws Exception{
		final NodeList children = current.getChildNodes();
		if (children != null) {
			final int childCount = children.getLength();
			for (int a = 0; a < childCount; ++a) {
				final Node child = children.item(a);
				if (XmlClient.getInstance().hasParent(child, "zofar:researchdata"))
					continue;
				final ODFRenderer renderer = ODFRenderer.getInstance(child);
				if(renderer != null)renderer.render(pageId,question, child, doc, attachements,container,attachementMode);
			}
		}
	}
	
	public void render(final String pageId, final Node question, final Node current, final SimpleWriterDocument doc, final Map<String, Object> attachements,final Object container) throws Exception{
		render(pageId,question,current,doc,attachements,container,false);
	}


	public abstract void render(final String pageId, final Node question, final Node current, final SimpleWriterDocument doc, final Map<String, Object> attachements,final Object container,final boolean attachementMode) throws Exception;
}

package presentation.codebook.format.odf.renderer;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import eu.dzhw.zofar.management.utils.odf.components.SimpleWriterDocument;
import eu.dzhw.zofar.management.utils.string.ReplaceClient;
import eu.dzhw.zofar.management.utils.xml.XmlClient;

public class QOODFRenderer extends ODFRenderer {

	public QOODFRenderer() {
		super();
	}

	@Override
	public void render(String pageId,Node question, Node current, SimpleWriterDocument doc, Map<String, Object> attachements,final Object container,final boolean attachementMode) throws Exception {
		addQuestionType("Open",doc);
		final Set<String> visible = this.getVisible(current);
		
//		final String questionType = current.getNodeName().substring(6);
		final String variable = getVariable(current);
		
		String topStr = "";
//		if(questionType != null)topStr += " ("+questionType+")";
		if(variable != null)topStr += "  ["+variable+"]";
		if(visible != null)topStr += "  "+visible+"";
		doc.addText(topStr.trim());
		
		Map<String, String> headers = getHeader(current, "prefix");
		if(headers != null){
			String headerStr = "";
			for (final Map.Entry<String, String> header : headers.entrySet()) {
				if (!header.getValue().equals("")) headerStr = headerStr + "  [" + header.getValue() + "]  ";
				headerStr = headerStr + header.getKey();
			}
			if(!headerStr.trim().equals("")){
				doc.addText("(Prefix) "+ headerStr);
				doc.addTextLineBreak();
			}
		}
		
		headers = getHeader(current, "postfix");
		if(headers != null){
			String headerStr = "";
			for (final Map.Entry<String, String> header : headers.entrySet()) {
				if (!header.getValue().equals("")) headerStr = headerStr + "  [" + header.getValue() + "]  ";
				headerStr = headerStr + header.getKey();
			}
			if(!headerStr.trim().equals("")){
				doc.addText("(Postfix) "+ headerStr);
				doc.addTextLineBreak();
			}
		}
		
//		renderChildren(pageId,question, current, doc, attachements);
		
	}
	
	protected Map<String, String> getHeader(final Node parentNode, final String type) {
		if (parentNode == null)
			return null;

		final Map<String, String> back = new LinkedHashMap<String, String>();

		final NodeList question = XmlClient.getInstance().getByXPath(parentNode, "./" + type);
		if (question != null) {
			final int count = question.getLength();
			for (int a = 0; a < count; ++a) {
				final Node tmp = question.item(a);
				if (XmlClient.getInstance().hasParent(tmp, "zofar:researchdata"))	continue;
				
				final StringBuffer text = new StringBuffer();
//				if (tmp != null)
//					text.append(tmp.getTextContent());
				
				
				final NodeList label = XmlClient.getInstance().getByXPath(tmp, "./label");
				final int labelCount = label.getLength();
				for (int b = 0; b < labelCount; ++b) {
					final Node labelText = label.item(b);
					if (labelText != null)text.append(labelText.getTextContent());			
				}

				String condition = "";
				final NamedNodeMap nodeAttributes = tmp.getAttributes();
				if (nodeAttributes != null) {
					final Node visibleNode = nodeAttributes.getNamedItem("visible");
					if (visibleNode != null)
						condition = visibleNode.getTextContent();
				}

				back.put(ReplaceClient.getInstance().cleanedString(text.toString()), condition);
			}
		}
		return back;
	}
}

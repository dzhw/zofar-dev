package presentation.codebook.format.odf.renderer;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import org.odftoolkit.odfdom.type.Color;
import org.odftoolkit.simple.style.Font;
import org.odftoolkit.simple.style.StyleTypeDefinitions.FontStyle;
import org.odftoolkit.simple.style.StyleTypeDefinitions.HorizontalAlignmentType;
import org.odftoolkit.simple.text.Paragraph;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import eu.dzhw.zofar.management.utils.odf.components.SimpleWriterDocument;
import eu.dzhw.zofar.management.utils.string.ReplaceClient;
import eu.dzhw.zofar.management.utils.xml.XmlClient;

public class HeaderODFRenderer extends ODFRenderer {

	public HeaderODFRenderer() {
		super();
	}
	
	@Override
	public void render(String pageId, Node question, Node current, SimpleWriterDocument doc, Map<String, Object> attachements,final Object container,final boolean attachementMode) throws Exception {
//		final Set<String> visible = this.getVisible(current);
		
		String variable = getVariable(current);
		if ((variable == null) || (variable.equals("")))
			variable = getVariable(question);

		String topStr = "";
		if (variable != null)
			topStr += " " + variable + "";
		
//		if (visible != null)
//			topStr = " " + visible + "  " + topStr;

		if(!topStr.trim().equals("")){
			final Paragraph para = doc.addText(topStr.trim());
			para.setHorizontalAlignment(HorizontalAlignmentType.CENTER);
		}
		
		Map<String, String> headers = getHeader(current, "question");
		if (headers != null) {
			String headerStr = "";
			for (final Map.Entry<String, String> header : headers.entrySet()) {
				headerStr = headerStr + header.getKey();
				if (!header.getValue().equals(""))
					headerStr = headerStr + "  [" + header.getValue() + "]  ";
			}
			headerStr = headerStr.trim();
			if (!headerStr.equals("")) {
				doc.addSubHeading1("" + headerStr);
			}
		}

		headers = getHeader(current, "title");
		if (headers != null) {
			String headerStr = "";
			for (final Map.Entry<String, String> header : headers.entrySet()) {
				headerStr = headerStr + header.getKey();
				if (!header.getValue().equals(""))
					headerStr = headerStr + "  [" + header.getValue() + "]  ";
			}
			headerStr = headerStr.trim();
			if (!headerStr.equals("")) {
				final Paragraph para = doc.addText(headerStr);
				para.setFont(doc.TITLEFONT);
			}
		}

		headers = getHeader(current, "instruction");
		if (headers != null) {
			String headerStr = "";
			for (final Map.Entry<String, String> header : headers.entrySet()) {
				headerStr = headerStr + header.getKey();
				if (!header.getValue().equals(""))
					headerStr = headerStr + "  [" + header.getValue() + "]  ";
			}
			headerStr = headerStr.trim();
			if (!headerStr.equals("")) {
				final Paragraph para = doc.addText(headerStr);
				para.setFont(doc.INSTRUCTIONFONT);
			}
		}

		headers = getHeader(current, "introduction");
		if (headers != null) {
			String headerStr = "";
			for (final Map.Entry<String, String> header : headers.entrySet()) {
				headerStr = headerStr + header.getKey();
				if (!header.getValue().equals(""))
					headerStr = headerStr + "  [" + header.getValue() + "]  ";
			}
			headerStr = headerStr.trim();
			if (!headerStr.equals("")) {
				final Paragraph para = doc.addText(headerStr);
				para.setFont(doc.INTRODUCTIONFONT);
			}
		}
		
		headers = getHeader(current, "text");
		if (headers != null) {
			String headerStr = "";
			for (final Map.Entry<String, String> header : headers.entrySet()) {
				headerStr = headerStr + header.getKey();
				if (!header.getValue().equals(""))
					headerStr = headerStr + "  [" + header.getValue() + "]  ";
			}
			headerStr = headerStr.trim();
			if (!headerStr.equals("")) {
				final Paragraph para = doc.addText(headerStr);
				para.setFont(doc.TEXTFONT);
			}
		}
	}

//	@Override
//	public void render(String pageId, Node question, Node current, SimpleWriterDocument doc, Map<String, Object> attachements,final Object container) throws Exception {
//		final Set<String> visible = this.getVisible(current);
//		
//		String variable = getVariable(current);
//		if ((variable == null) || (variable.equals("")))
//			variable = getVariable(question);
//
//		String topStr = "";
//		if (variable != null)
//			topStr += " " + variable + "";
//		
//		if (visible != null)
//			topStr = " " + visible + "  " + topStr;
//
//		if(!topStr.trim().equals("")){
////			final Font font = new Font("Arial", FontStyle.ITALIC, 12,Color.GRAY);
//			final Paragraph para = doc.addText(topStr.trim());
//			para.setHorizontalAlignment(HorizontalAlignmentType.RIGHT);
//		}
//
//		Map<String, String> headers = getHeader(current, "question");
//		if (headers != null) {
//			String headerStr = "";
//			for (final Map.Entry<String, String> header : headers.entrySet()) {
//				if (!header.getValue().equals(""))
//					headerStr = headerStr + "  [" + header.getValue() + "]  ";
//				headerStr = headerStr + header.getKey();
//			}
//			if (!headerStr.trim().equals("")) {
//				doc.addSubHeading1("" + headerStr);
//				// doc.addTextLineBreak();
//			}
//		}
//
//		headers = getHeader(current, "title");
//		if (headers != null) {
//			String headerStr = "";
//			for (final Map.Entry<String, String> header : headers.entrySet()) {
//				if (!header.getValue().equals(""))
//					headerStr = headerStr + "  [" + header.getValue() + "]  ";
//				headerStr = headerStr + header.getKey();
//			}
//			if (!headerStr.trim().equals("")) {
////				final Font font = new Font("Arial", FontStyle.ITALIC, 12,Color.GRAY);
//				final Paragraph para = doc.addText(headerStr.trim());
//				// doc.addTextLineBreak();
//			}
//		}
//
//		headers = getHeader(current, "instruction");
//		if (headers != null) {
//			String headerStr = "";
//			for (final Map.Entry<String, String> header : headers.entrySet()) {
//				if (!header.getValue().equals(""))
//					headerStr = headerStr + "  [" + header.getValue() + "]  ";
//				headerStr = headerStr + header.getKey();
//			}
//			if (!headerStr.trim().equals("")) {
////				final Font font = new Font("Arial", FontStyle.ITALIC, 12,Color.GRAY);
//				final Paragraph para = doc.addText(headerStr.trim());
//				// doc.addTextLineBreak();
//			}
//		}
//
//		headers = getHeader(current, "introduction");
//		if (headers != null) {
//			String headerStr = "";
//			for (final Map.Entry<String, String> header : headers.entrySet()) {
//				if (!header.getValue().equals(""))
//					headerStr = headerStr + "  [" + header.getValue() + "]  ";
//				headerStr = headerStr + header.getKey();
//			}
//			if (!headerStr.trim().equals("")) {
////				final Font font = new Font("Arial", FontStyle.REGULAR, 12,Color.GRAY);
//				final Paragraph para = doc.addText(headerStr.trim());
//				// doc.addTextLineBreak();
//			}
//		}
//		
//		headers = getHeader(current, "text");
//		if (headers != null) {
//			String headerStr = "";
//			for (final Map.Entry<String, String> header : headers.entrySet()) {
//				if (!header.getValue().equals(""))
//					headerStr = headerStr + "  [" + header.getValue() + "]  ";
//				headerStr = headerStr + header.getKey();
//			}
//			if (!headerStr.trim().equals("")) {
////				final Font font = new Font("Arial", FontStyle.REGULAR, 12,Color.TEAL);
//				final Paragraph para = doc.addText(headerStr.trim());
//				// doc.addTextLineBreak();
//			}
//		}
////		doc.addTextLineBreak();
//	}

	private Map<String, String> getHeader(final Node parentNode, final String type) {
		if (parentNode == null)
			return null;

		final Map<String, String> back = new LinkedHashMap<String, String>();

		final NodeList question = XmlClient.getInstance().getByXPath(parentNode, "./" + type);
		if (question != null) {
			final int count = question.getLength();
			for (int a = 0; a < count; ++a) {
				final Node textNode = question.item(a);
				final StringBuffer text = new StringBuffer();
				if (textNode != null){
					final String headerText = textNode.getTextContent();
					if(!headerText.trim().equals(""))text.append(headerText);
				}
					
				
				String condition = "";
				Set<String> visibles = this.getVisible(textNode);
				if((visibles != null)&&(!visibles.isEmpty()))condition = visibles.toString();
				back.put(ReplaceClient.getInstance().cleanedString(text.toString()), condition);
			}
		}
		return back;
	}

}

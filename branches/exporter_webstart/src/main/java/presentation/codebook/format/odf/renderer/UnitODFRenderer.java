package presentation.codebook.format.odf.renderer;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import org.odftoolkit.simple.table.Row;
import org.odftoolkit.simple.table.Table;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import eu.dzhw.zofar.management.utils.odf.components.SimpleWriterDocument;
import eu.dzhw.zofar.management.utils.string.ReplaceClient;
import eu.dzhw.zofar.management.utils.xml.XmlClient;

public class UnitODFRenderer extends ODFRenderer {

	public UnitODFRenderer() {
		super();
	}

	@Override
	public void render(String pageId, Node question, Node current, SimpleWriterDocument doc, Map<String, Object> attachements,final Object container,final boolean attachementMode) throws Exception {
		final Set<String> visible = this.getVisible(current);
		if(visible != null)doc.addText(visible.toString());
		final int cellIndex = 2;
		Map<String, String> headers = getHeader(current, "question");
		if (headers != null) {
			String headerStr = "";
			for (final Map.Entry<String, String> header : headers.entrySet()) {
				if (!header.getValue().equals(""))
					headerStr = headerStr + "  [" + header.getValue() + "]  ";
				headerStr = headerStr + header.getKey();
			}
			if (!headerStr.trim().equals("")) {
//				doc.addText("" + headerStr);
				if((container != null)&&((Table.class).isAssignableFrom(container.getClass()))){
					Table table = (Table)container;
					final Row row = doc.addRow(table);
//					final Font font = new Font("Arial", FontStyle.ITALIC, 12,Color.GRAY);
					doc.addToCell(row.getCellByIndex(cellIndex),doc.createTextParagraph(headerStr,null,row.getCellByIndex(cellIndex),doc.UNITHEADERFONT));
				}
				else doc.addText(headerStr);
			}
		}

		headers = getHeader(current, "title");
		if (headers != null) {
			String headerStr = "";
			for (final Map.Entry<String, String> header : headers.entrySet()) {
				if (!header.getValue().equals(""))
					headerStr = headerStr + "  [" + header.getValue() + "]  ";
				headerStr = headerStr + header.getKey();
			}
			if (!headerStr.trim().equals("")) {
//				final Font font = new Font("Arial", FontStyle.ITALIC, 12,Color.GRAY);
//				final Paragraph para = doc.addText(headerStr.trim(),font);
//				doc.addText("" + headerStr);
				if((container != null)&&((Table.class).isAssignableFrom(container.getClass()))){
					Table table = (Table)container;
					final Row row = doc.addRow(table);
					doc.addToCell(row.getCellByIndex(cellIndex),doc.createTextParagraph(headerStr,null,row.getCellByIndex(cellIndex),doc.UNITTITLEFONT));
				}
				else doc.addText(headerStr.trim());
			}
		}

		headers = getHeader(current, "instruction");
		if (headers != null) {
			String headerStr = "";
			for (final Map.Entry<String, String> header : headers.entrySet()) {
				if (!header.getValue().equals(""))
					headerStr = headerStr + "  [" + header.getValue() + "]  ";
				headerStr = headerStr + header.getKey();
			}
			if (!headerStr.trim().equals("")) {
//				final Font font = new Font("Arial", FontStyle.ITALIC, 12,Color.GRAY);
//				final Paragraph para = doc.addText(headerStr.trim(),font);
				if((container != null)&&((Table.class).isAssignableFrom(container.getClass()))){
					Table table = (Table)container;
					final Row row = doc.addRow(table);
					doc.addToCell(row.getCellByIndex(cellIndex),doc.createTextParagraph(headerStr,null,row.getCellByIndex(cellIndex),doc.UNITINSTRUCTIONFONT));
				}
				else doc.addText(headerStr.trim());
			}
		}

		headers = getHeader(current, "introduction");
		if (headers != null) {
			String headerStr = "";
			for (final Map.Entry<String, String> header : headers.entrySet()) {
				if (!header.getValue().equals(""))
					headerStr = headerStr + "  [" + header.getValue() + "]  ";
				headerStr = headerStr + header.getKey();
			}
			if (!headerStr.trim().equals("")) {
//				final Font font = new Font("Arial", FontStyle.REGULAR, 12,Color.GRAY);
//				final Paragraph para = doc.addText(headerStr.trim(),font);
				if((container != null)&&((Table.class).isAssignableFrom(container.getClass()))){
					Table table = (Table)container;
					final Row row = doc.addRow(table);
					doc.addToCell(row.getCellByIndex(cellIndex),doc.createTextParagraph(headerStr,null,row.getCellByIndex(cellIndex),doc.UNITINTRODUCTIONFONT));
				}
				else doc.addText(headerStr.trim());
			}
		}
		renderChildren(pageId, question,current, doc, attachements,container,attachementMode);
	}
	
	public void renderChildren(final String pageId, final Node question,final Node current, final SimpleWriterDocument doc, final Map<String, Object> attachements,final Object container,final boolean attachementMode) throws Exception{
		final NodeList children = current.getChildNodes();
		if (children != null) {
			final int childCount = children.getLength();
			for (int a = 0; a < childCount; ++a) {
				final Node child = children.item(a);
				if (XmlClient.getInstance().hasParent(child, "zofar:researchdata"))
					continue;
				if(child.getNodeName().equals("zofar:header"))continue;
				final ODFRenderer renderer = ODFRenderer.getInstance(child);
				if(renderer != null)renderer.render(pageId,question, child, doc, attachements,container,attachementMode);
			}
		}
	}
	
	protected Map<String, String> getHeader(final Node parentNode, final String type) {
		if (parentNode == null)
			return null;

		final Map<String, String> back = new LinkedHashMap<String, String>();

		final NodeList question = XmlClient.getInstance().getByXPath(parentNode, "./header/" + type);
		if (question != null) {
			final int count = question.getLength();
			for (int a = 0; a < count; ++a) {
				final Node textNode = question.item(a);
				final StringBuffer text = new StringBuffer();
				if (textNode != null)
					text.append(textNode.getTextContent());

				String condition = "";
				final NamedNodeMap nodeAttributes = textNode.getAttributes();
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

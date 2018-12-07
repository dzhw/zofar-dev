package presentation.codebook.format.odf.renderer;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.odftoolkit.simple.table.Cell;
import org.odftoolkit.simple.table.Row;
import org.odftoolkit.simple.table.Table;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import eu.dzhw.zofar.management.utils.odf.components.SimpleWriterDocument;
import eu.dzhw.zofar.management.utils.string.ReplaceClient;
import eu.dzhw.zofar.management.utils.xml.XmlClient;

public class AnswerOptionODFRenderer extends ODFRenderer {

	public AnswerOptionODFRenderer() {
		super();
	}

	@Override
	public void render(String pageId, Node question, Node current, SimpleWriterDocument doc, Map<String, Object> attachements,final Object container,final boolean attachementMode) throws Exception {
		final Set<String> headers = this.getLabel(current);

		final String variable = this.getVariable(current);
		final Set<String> visible = this.getVisible(current);
		final String uid = this.getUID(current);
		final String value = this.getValue(current);

		String headerStr = "";
		String label = "";
		for (final String header : headers) {
			label = label + " " + header;
		}
		
		if (visible != null)
			label = " " + visible + "  " + label;
		
		headerStr = headerStr + label;
		
		label = label.trim();

		if (value != null)
			headerStr = " {" + value + "} " + headerStr;
		if (variable != null)
			headerStr = " [" + variable + "] " + headerStr;
		if (uid != null)
			headerStr = " (" + uid + ") " + headerStr;


		final List<Node> attached = this.getAttached(current);
		if (attached != null) {
			for (final Node tmp : attached) {
				Map<String, String> attachedHeaders = getAttachedOpenHeader(tmp, "prefix");
				if (headers != null) {
					String attachedHeaderStr = "";
					for (final Map.Entry<String, String> header : attachedHeaders.entrySet()) {
						if (!header.getValue().equals(""))
							attachedHeaderStr = attachedHeaderStr + " [" + header.getValue() + "] ";
						attachedHeaderStr = attachedHeaderStr + header.getKey();
					}
					if (!attachedHeaderStr.trim().equals("")) {
						headerStr = headerStr +" "+ attachedHeaderStr;
					}
				}
				
				headerStr = headerStr + " [["+getVariable(tmp)+"]]";

				attachedHeaders = getAttachedOpenHeader(tmp, "postfix");
				if (headers != null) {
					String attachedHeaderStr = "";
					for (final Map.Entry<String, String> header : attachedHeaders.entrySet()) {
						if (!header.getValue().equals(""))
							attachedHeaderStr = attachedHeaderStr + " [" + header.getValue() + "] ";
						attachedHeaderStr = attachedHeaderStr + header.getKey();
					}
					if (!attachedHeaderStr.trim().equals("")) {
						headerStr = headerStr +" "+ attachedHeaderStr;
					}
				}				
			}
		}
		if(container != null){
			if ((Table.class).isAssignableFrom(container.getClass())){
				Table table = (Table)container;
				final Row row = doc.addRow(table);
				doc.addToCell(row.getCellByIndex(0),doc.createTextParagraph(uid,null,row.getCellByIndex(0)));
				if(variable != null)doc.addToCell(row.getCellByIndex(1),doc.createTextParagraph(variable,null,row.getCellByIndex(1)));
				else if(value != null)doc.addToCell(row.getCellByIndex(1),doc.createTextParagraph(value,null,row.getCellByIndex(1)));
				doc.addToCell(row.getCellByIndex(2),doc.createTextParagraph(label,null,row.getCellByIndex(2)));
			}
			else if ((Cell.class).isAssignableFrom(container.getClass())){
				Cell cell = (Cell)container;
				doc.addToCell(cell, doc.createTextParagraph(headerStr, null, cell));
			}
			else doc.addText(headerStr);
		}
		else doc.addText(headerStr);
		renderChildren(pageId, question, current, doc, attachements,container);
	}

	@Override
	public void renderChildren(final String pageId, final Node question, final Node current, final SimpleWriterDocument doc, final Map<String, Object> attachements,final Object container) throws Exception {
		final NodeList children = current.getChildNodes();
		if (children != null) {
			final int childCount = children.getLength();
			for (int a = 0; a < childCount; ++a) {
				final Node child = children.item(a);
				if (XmlClient.getInstance().hasParent(child, "zofar:researchdata"))
					continue;

				final String childName = child.getNodeName();

				if (childName.equals("zofar:header"))
					continue;
				if (childName.equals("zofar:questionOpen"))
					continue;

				final ODFRenderer renderer = ODFRenderer.getInstance(child);
				if (renderer != null)
					renderer.render(pageId, question, child, doc, attachements,container);
			}
		}
	}

	public List<Node> getAttached(final Node current) throws Exception {
		final NodeList children = current.getChildNodes();
		if (children != null) {
			final List<Node> back = new ArrayList<Node>();
			final int childCount = children.getLength();
			for (int a = 0; a < childCount; ++a) {
				final Node child = children.item(a);
				if (XmlClient.getInstance().hasParent(child, "zofar:researchdata"))
					continue;

				final String childName = child.getNodeName();

				if (childName.equals("zofar:questionOpen"))
					back.add(child);
			}
			return back;
		}
		return null;
	}

	public String getValue(final Node parentNode) {
		if (parentNode == null)
			return null;

		final NamedNodeMap nodeAttributes = parentNode.getAttributes();
		if (nodeAttributes != null) {
			final Node uidNode = nodeAttributes.getNamedItem("value");
			if (uidNode != null)
				return uidNode.getTextContent();
		}
		return null;
	}

	protected Map<String, String> getAttachedOpenHeader(final Node parentNode, final String type) {
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

	public Set<String> getLabel(final Node parentNode) {
		if (parentNode == null)
			return null;

		final Set<String> back = new LinkedHashSet<String>();

		final NamedNodeMap attributes = parentNode.getAttributes();
		final Node labelNode = attributes.getNamedItem("label");
		if (labelNode != null) {
			back.add(ReplaceClient.getInstance().cleanedString(labelNode.getTextContent()));
		}
		return back;
	}

}

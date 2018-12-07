package presentation.codebook.format.odf.renderer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.odftoolkit.simple.table.Row;
import org.odftoolkit.simple.table.Table;
import org.odftoolkit.simple.text.Paragraph;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import eu.dzhw.zofar.management.utils.odf.components.SimpleWriterDocument;
import eu.dzhw.zofar.management.utils.string.ReplaceClient;
import eu.dzhw.zofar.management.utils.xml.XmlClient;

public class ItemODFRenderer extends ODFRenderer {

	public ItemODFRenderer() {
		super();
	}

	@Override
	public void render(String pageId, Node question, Node current, SimpleWriterDocument doc, Map<String, Object> attachements, final Object container,final boolean attachementMode) throws Exception {
		final Set<String> visible = this.getVisible(current);
		if (question.getNodeName().equals("zofar:matrixDouble")) {
			List<Map<String, String>> headers = this.getHeader(current, "/question/question");
			String headerStr = "";
			if (headers != null) {
				for (Map<String, String> item : headers) {
					final String label = item.get("label");
					final String condition = item.get("condition");
					if (!condition.equals(""))
						headerStr = headerStr + "  [" + condition + "]  ";
					headerStr = headerStr + label;
					if (visible != null)
						headerStr = headerStr + " " + visible + "";
				}

				if ((container != null) && ((Row.class).isAssignableFrom(container.getClass()))) {
					Row row = (Row) container;
					int middleIndex = (int) Math.floor(row.getCellCount() / 2);

					if (!headerStr.trim().equals("")) {
						final Paragraph itemHeader = doc.createTextParagraph(headerStr, null, row.getCellByIndex(middleIndex));
//						itemHeader.applyHeading(true, 3);
						row.getCellByIndex(middleIndex).setTextWrapped(false);
						doc.addToCell(row.getCellByIndex(middleIndex), itemHeader);
					}
				} else{
//					doc.addSubHeading2(headerStr);
					doc.addText(headerStr);
				}
			}
		} else if (question.getNodeName().equals("zofar:matrixQuestionMixed")) {
			if (visible != null) {
				doc.addText(visible.toString());
			}
		} else {
			final String variable = getVariable(current);
			List<Map<String, String>> headers = this.getHeader(current, "/header/question");
			String headerStr = "";
			if (headers != null) {
				for (Map<String, String> item : headers) {
					final String label = item.get("label");
					final String condition = item.get("condition");
					if (!condition.equals(""))
						headerStr = headerStr + " [" + condition + "] ";
					headerStr = headerStr + label;
					if (visible != null)
						headerStr = headerStr + " " + visible + "";
				}
			}

			if ((container != null) && ((Table.class).isAssignableFrom(container.getClass()))) {
				Table table = (Table) container;
				final Row row = doc.addRow(table);

				if (!headerStr.trim().equals("")) {
					final Paragraph itemHeader = doc.createTextParagraph(headerStr, null, row.getCellByIndex(0));
					// itemHeader.applyHeading(true, 3);
					doc.addToCell(row.getCellByIndex(0), itemHeader);

					final Paragraph itemVariable = doc.createTextParagraph(variable, null, row.getCellByIndex(1));
					doc.addToCell(row.getCellByIndex(1), itemVariable);
				}

				renderChildren(pageId, question, current, doc, attachements, row);
			} else{
//				doc.addSubHeading2(headerStr);
				doc.addText(headerStr);
			}
		}
		renderChildren(pageId, question, current, doc, attachements, container);
	}

	// @Override
	// public void render(String pageId, Node question, Node current,
	// SimpleWriterDocument doc, Map<String, Object> attachements, final Object
	// container) throws Exception {
	// final Set<String> visible = this.getVisible(current);
	// if (question.getNodeName().equals("zofar:matrixDouble")) {
	// if ((container != null) &&
	// ((Row.class).isAssignableFrom(container.getClass()))) {
	// List<Map<String, String>> headers = this.getHeader(current,
	// "/question/question");
	// Row row = (Row) container;
	// int middleIndex = (int) Math.floor(row.getCellCount() / 2);
	//
	// if (headers != null) {
	// for (Map<String, String> item : headers) {
	// final String label = item.get("label");
	// final String condition = item.get("condition");
	// String headerStr = "";
	// if (!condition.equals(""))
	// headerStr = headerStr + " [" + condition + "] ";
	// headerStr = headerStr + label;
	//
	// if(visible != null){
	// headerStr = headerStr + " " +visible+"";
	// }
	//
	// if (!headerStr.trim().equals("")) {
	// final Paragraph itemHeader = doc.createTextParagraph(headerStr, null,
	// row.getCellByIndex(middleIndex));
	// itemHeader.applyHeading(true, 3);
	// row.getCellByIndex(middleIndex).setTextWrapped(false);
	// doc.addToCell(row.getCellByIndex(middleIndex), itemHeader);
	// } else
	// doc.addSubHeading2(headerStr);
	// }
	// }
	// renderChildren(pageId, question, current, doc, attachements, container);
	// }
	// } else if (question.getNodeName().equals("zofar:matrixQuestionMixed")) {
	// if(visible != null){
	// doc.addText(visible.toString());
	// }
	// renderChildren(pageId, question, current, doc, attachements, container);
	// } else {
	// final String variable = getVariable(current);
	//
	//
	// List<Map<String, String>> headers = this.getHeader(current,
	// "/header/question");
	// if ((container != null) &&
	// ((Table.class).isAssignableFrom(container.getClass()))) {
	// Table table = (Table) container;
	// final Row row = doc.addRow(table);
	//
	// if (headers != null) {
	// for (Map<String, String> item : headers) {
	// final String label = item.get("label");
	// final String condition = item.get("condition");
	// String headerStr = "";
	// if (!condition.equals(""))
	// headerStr = headerStr + " [" + condition + "] ";
	// headerStr = headerStr + label;
	//
	// if(visible != null){
	// headerStr = headerStr + " " +visible+"";
	// }
	//
	// if (!headerStr.trim().equals("")) {
	// final Paragraph itemHeader = doc.createTextParagraph(headerStr, null,
	// row.getCellByIndex(0));
	// itemHeader.applyHeading(true, 3);
	//
	// doc.addToCell(row.getCellByIndex(0), itemHeader);
	//
	// if (variable != null) {
	// final Paragraph itemVariable = doc.createTextParagraph(variable, null,
	// row.getCellByIndex(1));
	// doc.addToCell(row.getCellByIndex(1), itemVariable);
	// }
	// } else
	// doc.addSubHeading2(headerStr);
	// }
	// }
	// renderChildren(pageId, question, current, doc, attachements, row);
	// } else
	// if(visible != null){
	// doc.addText(visible.toString());
	// }
	// renderChildren(pageId, question, current, doc, attachements, container);
	// }
	// }

	@Override
	public void renderChildren(final String pageId, final Node question, final Node current, final SimpleWriterDocument doc, final Map<String, Object> attachements, final Object container) throws Exception {
		if (question.getNodeName().equals("zofar:matrixQuestionMixed")) {
			List<Map<String, String>> referenceHeaders = this.getHeader(current.getParentNode(), "header/title");
			int referenceCount = -1;
			if (referenceHeaders != null)
				referenceCount = referenceHeaders.size();

			List<Map<String, String>> itemHeaders = this.getHeader(current, "header/title");
			String headerStr = "";
			if (itemHeaders != null) {
				for (Map<String, String> item : itemHeaders) {
					final String label = item.get("label");
					final String condition = item.get("condition");
					String tmpStr = label;
					if (!condition.equals(""))
						tmpStr = tmpStr + "  [" + condition + "]  ";
					headerStr = headerStr + tmpStr;

				}
				// doc.addText(headerStr);
			}

			final NodeList children = getMixedItems(current);
			if (children != null) {
				final int childCount = children.getLength();
				for (int a = 0; a < childCount; ++a) {
					doc.addTextLineBreak();

					String itemHeader = headerStr;

					if (a < referenceCount) {
						Map<String, String> header = referenceHeaders.get(a);
						// doc.addText(header.get("label"));
						itemHeader += " " + header.get("label");
					}
					doc.addText(itemHeader.trim());
					final Node child = children.item(a);
					final ODFRenderer renderer = ODFRenderer.getInstance(child);
					if (renderer != null)
						renderer.render(pageId, child, child, doc, attachements, container);
				}
			}
		} else {
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
					else {
						if (childName.equals("zofar:question")) {
							if ((container != null) && ((Row.class).isAssignableFrom(container.getClass()))) {
								Row row = (Row) container;

								final String variable = getVariable(child);
								final String uid = getUID(child);

								if (variable != null) {
									final Paragraph itemVariable = doc.createTextParagraph(variable, null, row.getCellByIndex(1));
									doc.addToCell(row.getCellByIndex(1), itemVariable);
								}
							}
						} else {
							// System.out.println("childName : "+childName);
							final ODFRenderer renderer = ODFRenderer.getInstance(child);
							if (renderer != null)
								renderer.render(pageId, question, child, doc, attachements, container);
						}
					}
				}
			}
		}
	}

	// private void renderOpenItemChild(String pageId, Node question, Node
	// current, SimpleWriterDocument doc, Map<String, Object> attachements,
	// final Object container) throws Exception {
	// final String variable = getVariable(current);
	// final String uid = getUID(current);
	//
	// System.out.println("Container : "+container);
	//
	// String topStr = "";
	// if (uid != null)
	// topStr += " (" + uid + ")";
	// if (variable != null)
	// topStr += " [" + variable + "]";
	// doc.addText("## "+topStr.trim());
	//
	// // Map<String, String> headers = getHeader(current, "question");
	// // System.out.println("Open Item Header : "+headers);
	// //
	// // if (headers != null) {
	// // String headerStr = "";
	// // for (final Map.Entry<String, String> header : headers.entrySet()) {
	// // if (!header.getValue().equals(""))
	// // headerStr = headerStr + " [" + header.getValue() + "] ";
	// // headerStr = headerStr + header.getKey();
	// // }
	// // if (!headerStr.trim().equals("")) {
	// // doc.addSubHeading1("(Open Item Header) " + headerStr);
	// // }
	// // }
	// // renderChildren(pageId, question,current, doc, attachements);
	// }

	// protected Map<String, String> getHeader(final Node parentNode, final
	// String type) {
	// if (parentNode == null)
	// return null;
	//
	// final Map<String, String> back = new LinkedHashMap<String, String>();
	//
	// final NodeList question = XmlClient.getInstance().getByXPath(parentNode,
	// "./header/" + type);
	// if (question != null) {
	// final int count = question.getLength();
	// for (int a = 0; a < count; ++a) {
	// final Node textNode = question.item(a);
	// final StringBuffer text = new StringBuffer();
	// if (textNode != null)
	// text.append(textNode.getTextContent());
	//
	// String condition = "";
	// final NamedNodeMap nodeAttributes = textNode.getAttributes();
	// if (nodeAttributes != null) {
	// final Node visibleNode = nodeAttributes.getNamedItem("visible");
	// if (visibleNode != null)
	// condition = visibleNode.getTextContent();
	// }
	//
	// back.put(ReplaceClient.getInstance().cleanedString(text.toString()),
	// condition);
	// }
	// }
	// return back;
	// }

	private NodeList getMixedItems(final Node parentNode) {
		if (parentNode == null)
			return null;
		final NodeList items = XmlClient.getInstance().getByXPath(parentNode, "./body/*");
		return items;
	}

	private List<Map<String, String>> getHeader(final Node parentNode, final String type) {
		if (parentNode == null)
			return null;

		final List<Map<String, String>> back = new ArrayList<Map<String, String>>();

		final NodeList question = XmlClient.getInstance().getByXPath(parentNode, "./" + type);
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
				final String cleaned = ReplaceClient.getInstance().cleanedString(text.toString());

				Map<String, String> pair = new HashMap<String, String>();
				pair.put("label", cleaned);
				pair.put("condition", condition);

				back.add(pair);
			}
		}
		return back;
	}
}

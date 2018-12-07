package presentation.codebook.format.odf.renderer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.odftoolkit.simple.table.Row;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import eu.dzhw.zofar.management.utils.odf.components.SimpleWriterDocument;
import eu.dzhw.zofar.management.utils.string.ReplaceClient;
import eu.dzhw.zofar.management.utils.xml.XmlClient;

public class RDCODFRenderer extends ODFRenderer {

	public RDCODFRenderer() {
		super();
	}

	@Override
	public void render(String pageId, Node question, Node current, SimpleWriterDocument doc, Map<String, Object> attachements, final Object container, final boolean attachementMode) throws Exception {
		// final Set<String> visible = this.getVisible(current);
		// if(visible != null)doc.addText(visible.toString());

		String parentType = "UNKOWN";
		if (question != null) {
			parentType = current.getParentNode().getNodeName();
		}

		Object parentContainer = doc.getDoc();
		// if(container != null){
		// parentContainer = container;
		// }
		boolean renderChilds = true;
		Object rdcContainer = null;
		if (parentType.equals("zofar:questionSingleChoice")) {
			// final String attachmendId = this.getPath(current);
			final String attachmendId = (attachements.size() + 1) + "";
			// or attachmentContainer, when AO count over 20
			if ((!attachementMode) && (countChilds(current) >= 20)) {
				attachements.put(attachmendId, current);
				doc.addText("Siehe Anhang " + attachmendId);
				renderChilds = false;
			} else {
				String[] columnLabels = new String[3];
				columnLabels[0] = "UID";
				columnLabels[1] = "Value";
				columnLabels[2] = "Label";
				rdcContainer = doc.addTable("", columnLabels, parentContainer);
			}
		} else if (parentType.equals("zofar:multipleChoice")) {
			// final String attachmendId = this.getPath(current);
			final String attachmendId = (attachements.size() + 1) + "";
			// or attachmentContainer, when AO count over 20
			if ((!attachementMode) && (countChilds(current) >= 20)) {
				attachements.put(attachmendId, current);
				doc.addText("Siehe Anhang " + attachmendId);
				renderChilds = false;
			} else {
				String[] columnLabels = new String[3];
				columnLabels[0] = "UID";
				columnLabels[1] = "Variable";
				columnLabels[2] = "Label";
				rdcContainer = doc.addTable("", columnLabels, parentContainer);
			}
		} else if (parentType.equals("zofar:matrixQuestionSingleChoice")) {
			List<Map<String, String>> headers = this.getHeader(current, "header/title");
			if (headers == null) {
				headers = new ArrayList<Map<String, String>>();
				Map<String, String> pair = new HashMap<String, String>();
				pair.put("label", "Optionen");
				pair.put("condition", "");
			}

			String[] columnLabels = new String[2 + headers.size()];
			columnLabels[0] = "Item Header";
			columnLabels[1] = "Variable";

			int lft = 2;
			for (Map<String, String> item : headers) {
				columnLabels[lft] = item.get("label");
				if (!item.get("condition").equals(""))
					columnLabels[lft] = columnLabels[lft] + " [" + item.get("condition") + "]";
				lft = lft + 1;
			}

			rdcContainer = doc.addTable("Items", columnLabels);

			// Separate AOs from Items, when no special visibles
		} else if (parentType.equals("zofar:matrixQuestionOpen")) {
			List<Map<String, String>> headers = this.getHeader(current, "header/title");

			String[] columnLabels = new String[2];
			columnLabels[0] = "Item Header";
			columnLabels[1] = "Variable";

			String headerStr = "";
			for (Map<String, String> item : headers) {
				headerStr = headerStr + " " + item.get("label");
			}
			if (!headerStr.equals("")) {
				columnLabels[1] = columnLabels[1] + " (" + headerStr.trim() + ")";
			}

			rdcContainer = doc.addTable("Items", columnLabels);
		} else if (parentType.equals("zofar:matrixQuestionMixed")) {
			// List<Map<String, String>> headers = this.getHeader(current,
			// "header/title");
			//
			// if ((headers != null) && (!headers.isEmpty())) {
			// String headerStr = "";
			// for (Map<String, String> item : headers) {
			// headerStr = headerStr + " " + item.get("label");
			// }
			// if(!headerStr.equals("")){
			// final Font font = new Font("Arial", FontStyle.ITALIC, 10);
			// doc.addText(headerStr,font);
			// }
			// }
		} else if (parentType.equals("zofar:matrixDouble")) {

			// show right underneath left

			// List<Map<String, String>> leftHeaders = this.getHeader(current,
			// "leftHeader/title");
			// List<Map<String, String>> rightHeaders = this.getHeader(current,
			// "rightHeader/title");
			// List<Map<String, String>> leftScaleHeaders =
			// this.getHeader(current, "leftScaleHeader/title");
			// List<Map<String, String>> rightScaleHeaders =
			// this.getHeader(current, "rightScaleHeader/title");
			// int colCount = 0;
			// if (leftHeaders != null)
			// colCount = Math.max(colCount, leftHeaders.size());
			// if (rightHeaders != null)
			// colCount = Math.max(colCount, rightHeaders.size());
			// if (leftScaleHeaders != null)
			// colCount = Math.max(colCount, leftScaleHeaders.size());
			// if (rightScaleHeaders != null)
			// colCount = Math.max(colCount, rightScaleHeaders.size());
			//
			// String[] columnLabels = new String[colCount + 1 + colCount];
			//
			// int index = 0;
			// if ((leftHeaders != null) && (!leftHeaders.isEmpty())) {
			// for (Map<String, String> item : leftHeaders) {
			// columnLabels[index] = item.get("label");
			// index = index + 1;
			// }
			// }
			//
			// columnLabels[colCount] = "";
			//
			// index = colCount + 1;
			// if ((rightHeaders != null) && (!rightHeaders.isEmpty())) {
			// for (Map<String, String> item : rightHeaders) {
			// columnLabels[index] = item.get("label");
			// index = index + 1;
			// }
			// }
			//
			// rdcContainer = doc.addTable("", columnLabels);
			// if (((leftScaleHeaders != null) && (!leftScaleHeaders.isEmpty()))
			// || ((rightScaleHeaders != null) &&
			// (!rightScaleHeaders.isEmpty()))) {
			// Row headerRow = doc.addRow(((Table) rdcContainer));
			// Row nextRow = doc.addRow(((Table) rdcContainer));
			//
			// CellRange leftScaleHeader = ((Table)
			// rdcContainer).getCellRangeByPosition(0, headerRow.getRowIndex(),
			// colCount - 1, headerRow.getRowIndex());
			// leftScaleHeader.merge();
			// CellRange rightScaleHeader = ((Table)
			// rdcContainer).getCellRangeByPosition(colCount + 1,
			// headerRow.getRowIndex(), ((Table) rdcContainer).getColumnCount()
			// - 1, headerRow.getRowIndex());
			// rightScaleHeader.merge();
			//
			// if ((leftScaleHeaders != null) && (!leftScaleHeaders.isEmpty()))
			// {
			// // System.out.println("Left Scale Headers :
			// // "+leftScaleHeaders);
			// final Cell cell = headerRow.getCellByIndex(0);
			//
			// String headerStr = "";
			// for (Map<String, String> item : leftScaleHeaders) {
			// headerStr = headerStr + " " + item.get("label");
			// }
			// doc.addToCell(cell, doc.createTextParagraph(headerStr, null,
			// cell));
			// }
			//
			// if ((rightScaleHeaders != null) &&
			// (!rightScaleHeaders.isEmpty())) {
			// // System.out.println("Right Scale Headers :
			// // "+rightScaleHeaders);
			// final Cell cell = headerRow.getCellByIndex(colCount + 1);
			//
			// String headerStr = "";
			// for (Map<String, String> item : rightScaleHeaders) {
			// headerStr = headerStr + " " + item.get("label");
			// }
			// doc.addToCell(cell, doc.createTextParagraph(headerStr, null,
			// cell));
			// }
			// }

		} else {
			rdcContainer = container;
		}
		if (renderChilds) {
			renderChildren(pageId, question, current, doc, attachements, rdcContainer);
			doc.optimize(rdcContainer, parentType);
		}
	}

	private int countChilds(Node current) {
		if (current == null)
			return 0;
		if (XmlClient.getInstance().hasParent(current, "zofar:researchdata"))
			return 0;
		final NodeList children = current.getChildNodes();
		if (children != null) {
			int back = 0;
			final int childCount = children.getLength();
			for (int a = 0; a < childCount; ++a) {
				final Node child = children.item(a);
				if (XmlClient.getInstance().hasParent(child, "zofar:researchdata"))
					continue;
				final String childType = child.getNodeName();
				if (childType.equals("zofar:answerOption")) {
					back = back + 1;
				} else if (childType.equals("zofar:unit")) {
					back = back + countChilds(child);
				} else if (childType.equals("zofar:header")) {

				} else if (childType.equals("#text")) {

				} else {
					System.out.println("unkown child Type : " + childType);
				}
			}
			return back;
		}
		return 0;
	}

	@Override
	public void renderChildren(String pageId, Node question, Node current, SimpleWriterDocument doc, Map<String, Object> attachements, Object container) throws Exception {
		String parentType = "UNKOWN";
		if (question != null) {
			parentType = current.getParentNode().getNodeName();
		}
		if (parentType.equals("zofar:matrixQuestionSingleChoice")) {
			final NodeList children = current.getChildNodes();
			if (children != null) {
				final int childCount = children.getLength();
				for (int a = 0; a < childCount; ++a) {
					final Node child = children.item(a);
					if (XmlClient.getInstance().hasParent(child, "zofar:researchdata"))
						continue;
					if (child.getNodeName().equals("zofar:header"))
						continue;

					// final ODFRenderer renderer =
					// ODFRenderer.getInstance(child);
					// if (renderer != null)
					// renderer.render(pageId, question, child, doc,
					// attachements, container);
				}
			}
		} else if (parentType.equals("zofar:matrixQuestionMixed")) {
			final NodeList children = current.getChildNodes();
			if (children != null) {
				final int childCount = children.getLength();
				for (int a = 0; a < childCount; ++a) {
					final Node child = children.item(a);
					if (XmlClient.getInstance().hasParent(child, "zofar:researchdata"))
						continue;
					if (child.getNodeName().equals("zofar:header"))
						continue;
					if (child.getNodeName().equals("zofar:item")) {
						final ODFRenderer renderer = ODFRenderer.getInstance(child);
						if (renderer != null)
							renderer.render(pageId, question, child, doc, attachements, container);
						doc.addTextLineBreak();
					}
				}
				doc.addTextLineBreak();
			}
		} else if (parentType.equals("zofar:matrixQuestionOpen")) {
			final NodeList children = current.getChildNodes();
			if (children != null) {
				final int childCount = children.getLength();
				for (int a = 0; a < childCount; ++a) {
					final Node child = children.item(a);
					if (XmlClient.getInstance().hasParent(child, "zofar:researchdata"))
						continue;
					if (child.getNodeName().equals("zofar:header"))
						continue;
					final ODFRenderer renderer = ODFRenderer.getInstance(child);
					if (renderer != null)
						renderer.render(pageId, question, child, doc, attachements, container);
				}
			}
		} else if (parentType.equals("zofar:item")) {
			final NodeList children = current.getChildNodes();
			if ((container != null) && ((Row.class).isAssignableFrom(container.getClass()))) {
				Row row = (Row) container;
				int colIndex = 2;
				if (children != null) {
					final int childCount = children.getLength();
					for (int a = 0; a < childCount; ++a) {
						final Node child = children.item(a);
						if (XmlClient.getInstance().hasParent(child, "zofar:researchdata"))
							continue;
						if (child.getNodeName().equals("zofar:answerOption")) {
							final ODFRenderer renderer = ODFRenderer.getInstance(child);
							if (renderer != null) {
								renderer.render(pageId, question, child, doc, attachements, row.getCellByIndex(colIndex));
								colIndex = colIndex + 1;
							}
						}
					}
				}
			}
		} else if (parentType.equals("zofar:matrixDouble")) {
			final NodeList children = current.getChildNodes();
			if (children != null) {
				List<Map<String, String>> leftHeaders = this.getHeader(current, "leftHeader/title");
				List<Map<String, String>> rightHeaders = this.getHeader(current, "rightHeader/title");
				List<Map<String, String>> leftScaleHeaders = this.getHeader(current, "leftScaleHeader/title");
				List<Map<String, String>> rightScaleHeaders = this.getHeader(current, "rightScaleHeader/title");
				
				Map<Node,String> itemHeader = new HashMap<Node,String>();
				Map<Node,Node> itemLeft = new HashMap<Node,Node>();
				Map<Node,Node> itemRight = new HashMap<Node,Node>();

				final int childCount = children.getLength();
				for (int a = 0; a < childCount; ++a) {
					final Node child = children.item(a);
					if (XmlClient.getInstance().hasParent(child, "zofar:researchdata"))
						continue;

					if (child.getNodeName().equals("zofar:item")) {
						List<Map<String, String>> headers = this.getHeader(child, "/question/question");
						String headerStr = "";
						if (headers != null) {
							for (Map<String, String> item : headers) {
								final String label = item.get("label");
								final String condition = item.get("condition");
								if (!condition.equals(""))
									headerStr = headerStr + "  [" + condition + "]  ";
								headerStr = headerStr + label;
							}
							itemHeader.put(child,headerStr);
						}
						
						Node left = null;
						Node right = null;
						
						final NodeList itemChildChildrens = child.getChildNodes();
						final int itemChildCount = itemChildChildrens.getLength();
						for (int b = 0; b < itemChildCount; ++b) {
							final Node itemChild = itemChildChildrens.item(b);
							if (XmlClient.getInstance().hasParent(itemChild, "zofar:researchdata"))
								continue;
							
							if(itemChild.getNodeName().equals("zofar:left"))left = itemChild;
							if(itemChild.getNodeName().equals("zofar:right"))right = itemChild;
							
							if((left != null)&&(right != null))break;
						}
						if(left != null)itemLeft.put(child, left);
						if(right != null)itemRight.put(child, right);
					}

					// if (child.getNodeName().equals("zofar:header"))
					// continue;
					// if (child.getNodeName().equals("zofar:leftHeader"))
					// continue;
					// if (child.getNodeName().equals("zofar:rightHeader"))
					// continue;
					// if (child.getNodeName().equals("zofar:leftScaleHeader"))
					// continue;
					// if (child.getNodeName().equals("zofar:rightScaleHeader"))
					// continue;
					//
					// if (child.getNodeName().equals("zofar:item")) {
					// if ((container != null) &&
					// ((Table.class).isAssignableFrom(container.getClass()))) {
					// Table table = (Table) container;
					// Row row = doc.addRow(table);
					//
					// final ODFRenderer renderer =
					// ODFRenderer.getInstance(child);
					// if (renderer != null)
					// renderer.render(pageId, question, child, doc,
					// attachements, row);
					// }
					// }
					// final ODFRenderer renderer =
					// ODFRenderer.getInstance(child);
					// if (renderer != null)
					// renderer.render(pageId, question, child, doc,
					// attachements, container);
				}

				//Left
				for(Map<String, String> scaleHeader:leftScaleHeaders){
					final String label = scaleHeader.get("label");
					final String condition = scaleHeader.get("condition");
					String headerString = label;
					if((condition != null)&&(!condition.equals(""))){
						headerString = headerString+" ["+condition+"]";
					}
					doc.addText(headerString);					
				}

				for (int a = 0; a < childCount; ++a) {
					final Node child = children.item(a);
					if (XmlClient.getInstance().hasParent(child, "zofar:researchdata"))
						continue;
					
					final String itemHeaderStr = itemHeader.get(child);
					if((itemHeaderStr != null)&&(!itemHeaderStr.equals(""))){
						doc.addText(itemHeaderStr);			
					}
					
					final Node part = itemLeft.get(child);
					if(part != null){
						final NodeList partChilds = part.getChildNodes();
						final int partChildCount = partChilds.getLength();
						int index = 0;
						for (int b = 0; b < partChildCount; ++b) {
							final Node partChild = partChilds.item(b);
							if(partChild.getNodeName().equals("zofar:answerOption")){
								final Map<String, String> itemHeaderMap = leftHeaders.get(index);
								index = index + 1;
								final String label = itemHeaderMap.get("label");
								final String condition = itemHeaderMap.get("condition");
								String headerString = label;
								if((condition != null)&&(!condition.equals(""))){
									headerString = headerString+" ["+condition+"]";
								}
								final String aoStr = renderAO(partChild);
								if((aoStr != null)&&(!aoStr.equals("")))headerString = headerString+" "+aoStr;
								doc.addText(aoStr);
							}
							else{
								System.out.println("unkown partChildType : "+partChild.getNodeName());
							}
						}
					}
					doc.addTextLineBreak();
				}
				doc.addTextLineBreak();
				//Right
				for(Map<String, String> scaleHeader:rightScaleHeaders){
					final String label = scaleHeader.get("label");
					final String condition = scaleHeader.get("condition");
					String headerString = label;
					if((condition != null)&&(!condition.equals(""))){
						headerString = headerString+" ["+condition+"]";
					}
					doc.addText(headerString);					
				}
				for (int a = 0; a < childCount; ++a) {
					final Node child = children.item(a);
					if (XmlClient.getInstance().hasParent(child, "zofar:researchdata"))
						continue;
					
					final String itemHeaderStr = itemHeader.get(child);
					if((itemHeaderStr != null)&&(!itemHeaderStr.equals(""))){
						doc.addText(itemHeaderStr);			
					}
					
					final Node part = itemRight.get(child);
					if(part != null){
						final NodeList partChilds = part.getChildNodes();
						final int partChildCount = partChilds.getLength();
						int index = 0;
						for (int b = 0; b < partChildCount; ++b) {
							final Node partChild = partChilds.item(b);
							if(partChild.getNodeName().equals("zofar:answerOption")){
								final Map<String, String> itemHeaderMap = rightHeaders.get(index);
								index = index + 1;
								final String label = itemHeaderMap.get("label");
								final String condition = itemHeaderMap.get("condition");
								String headerString = label;
								if((condition != null)&&(!condition.equals(""))){
									headerString = headerString+" ["+condition+"]";
								}
								final String aoStr = renderAO(partChild);
								if((aoStr != null)&&(!aoStr.equals("")))headerString = headerString+" "+aoStr;
								doc.addText(aoStr);
							}
							else{
								System.out.println("unkown partChildType : "+partChild.getNodeName());
							}
						}
					}
					doc.addTextLineBreak();
				}
			}
		} else
			super.renderChildren(pageId, question, current, doc, attachements, container);

	}

	private String renderAO(Node partChild) throws Exception {
		final ODFRenderer renderer = ODFRenderer.getInstance(partChild);
		if ((renderer != null)&&((AnswerOptionODFRenderer.class).isAssignableFrom(renderer.getClass()))){
			final AnswerOptionODFRenderer aoRenderer = (AnswerOptionODFRenderer)renderer;
			final Set<String> headers = aoRenderer.getLabel(partChild);

			final String variable = aoRenderer.getVariable(partChild);
			final Set<String> visible = aoRenderer.getVisible(partChild);
			final String uid = aoRenderer.getUID(partChild);
			final String value = aoRenderer.getValue(partChild);
			final List<Node> attached = aoRenderer.getAttached(partChild);
			
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

			if (attached != null) {
				for (final Node tmp : attached) {
					Map<String, String> attachedHeaders = aoRenderer.getAttachedOpenHeader(tmp, "prefix");
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

					attachedHeaders = aoRenderer.getAttachedOpenHeader(tmp, "postfix");
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
			return headerStr;
		}
		return null;
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

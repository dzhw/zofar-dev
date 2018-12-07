package presentation.codebook.format.odf.renderer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

import org.odftoolkit.simple.table.Cell;
import org.odftoolkit.simple.table.Row;
import org.odftoolkit.simple.table.Table;
import org.odftoolkit.simple.text.list.List;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import eu.dzhw.zofar.management.utils.odf.components.SimpleWriterDocument;
import eu.dzhw.zofar.management.utils.xml.XmlClient;

public class CalendarODFRenderer extends ODFRenderer {

	public CalendarODFRenderer() {
		super();
	}

	@Override
	public void render(String pageId, Node question, Node current, SimpleWriterDocument doc, Map<String, Object> attachements, Object container,final boolean attachementMode) throws Exception {
		addQuestionType("Calendar", doc);
		final Set<String> visible = this.getVisible(current);
		if (visible != null)
			doc.addText(visible.toString());

		final Map<String, String> legends = getLegends(current);
		if (legends != null) {
			final Set<String> legendSet = new LinkedHashSet<String>();
			for (Map.Entry<String, String> legend : legends.entrySet()) {
				legendSet.add("(" + legend.getKey() + " ) " + legend.getValue());
			}
			doc.addList("Legende", legendSet, doc.getDoc());
		}

		final String[] rows = this.getRows(current);
		final String[] columns = this.getColumns(current);

//		System.out.println("Rows : " + Arrays.toString(rows));
//		System.out.println("Columns : " + Arrays.toString(columns));

		if ((rows != null) && (columns != null)) {
			java.util.List<String> newColumns = new ArrayList<String>();
			newColumns.add("");
			newColumns.addAll(Arrays.asList(columns));
			Table table = doc.addTable("", (String[]) newColumns.toArray(new String[0]));

			Map<Node, String> items = this.getItems(current);
			// Iterator<Node> itemsIt = items.keySet().iterator();
//			System.out.println("Items Count : " + items.size());
			Stack<Node> itemStack = new Stack<Node>();
			itemStack.addAll(items.keySet());

			for (final String row : rows) {
				Row tableRow = doc.addRow(table);
				final int cellCount = tableRow.getCellCount();
				Cell firstCell = tableRow.getCellByIndex(0);
				doc.addToCell(firstCell, doc.createTextParagraph(row,  null, firstCell));
				for (int a = 1; a < cellCount; ++a) {
					Cell cell = tableRow.getCellByIndex(a);
					final Set<String> itemSet = new LinkedHashSet<String>();
					if (!itemStack.isEmpty()) {
						Node item = itemStack.pop();
						Map<String, String> slotItems = getSlotItems(item);				
						if (slotItems != null) {
							for (Map.Entry<String, String> slotItem : slotItems.entrySet()) {
								itemSet.add("" + slotItem.getValue());
							}
						} else {
							itemSet.add("EMPTY");
						}
					}
					
					List list = doc.addList("", itemSet, cell);
//					System.out.println(row + " " + a+" : "+list);
					// if(itemsIt.hasNext()){
					// Node item = itemsIt.next();
					// Map<String, String> slotItems = getSlotItems(item);
					// if(slotItems != null){
					// final Set<String> itemSet = new LinkedHashSet<String>();
					// for (Map.Entry<String, String> slotItem :
					// slotItems.entrySet()) {
					//// itemSet.add("("+slotItem.getKey()+" )
					// "+slotItem.getValue());
					// itemSet.add(""+slotItem.getValue());
					// }
					// doc.addList("", itemSet, cell);
					// }
					// else break;
					// }

					// Map<String, String> slotItems getSlotItems(final Node
					// parentNode)
//					doc.addToCell(cell, doc.createTextParagraph(row + " " + a, doc.DEFAULTFONT, null, cell));
				}
			}
		}

		// renderChildren(pageId, question,current, doc,
		// attachements,container);
	}

	private Map<String, String> getLegends(final Node parentNode) {
		if (parentNode == null)
			return null;
		final NodeList items = XmlClient.getInstance().getByXPath(parentNode, "./configuration/SlotConfiguration");
		if (items != null) {
			Map<String, String> back = null;
			final int count = items.getLength();
			for (int a = 0; a < count; ++a) {
				final Node node = items.item(a);
				final NamedNodeMap nodeAttributes = node.getAttributes();
				if (nodeAttributes != null) {
					final Node slotNode = nodeAttributes.getNamedItem("slot");
					final Node labelNode = nodeAttributes.getNamedItem("label");
					if ((slotNode != null) && (labelNode != null)) {
						if (back == null)
							back = new LinkedHashMap<String, String>();
						back.put(slotNode.getTextContent(), labelNode.getTextContent());
					}
				}
			}
			return back;
		}
		return null;
	}

	private Map<Node, String> getItems(final Node parentNode) {
		if (parentNode == null)
			return null;
		final NodeList items = XmlClient.getInstance().getByXPath(parentNode, "./item");
		if (items != null) {
			Map<Node, String> back = null;
			final int count = items.getLength();
			for (int a = 0; a < count; ++a) {
				final Node node = items.item(a);
				final NamedNodeMap nodeAttributes = node.getAttributes();
				if (nodeAttributes != null) {
					final Node visibleNode = nodeAttributes.getNamedItem("visible");
					if (visibleNode != null) {
						if (back == null)
							back = new LinkedHashMap<Node, String>();
						back.put(node, visibleNode.getTextContent());
					}
				}
			}
			return back;
		}
		return null;
	}

	private Map<String, String> getSlotItems(final Node parentNode) {
		if (parentNode == null)
			return null;
		final NodeList items = XmlClient.getInstance().getByXPath(parentNode, "./SlotItem");
		if (items != null) {
			Map<String, String> back = null;
			final int count = items.getLength();
			for (int a = 0; a < count; ++a) {
				final Node node = items.item(a);
				final NamedNodeMap nodeAttributes = node.getAttributes();
				if (nodeAttributes != null) {
					final Node slotNode = nodeAttributes.getNamedItem("slot");
					final Node variableNode = nodeAttributes.getNamedItem("variable");
					if ((slotNode != null) && (variableNode != null)) {
						if (back == null)
							back = new LinkedHashMap<String, String>();
						back.put(slotNode.getTextContent(), variableNode.getTextContent());
					}
				}
			}
			return back;
		}
		return null;
	}

	private String[] getRows(final Node parentNode) {
		return getSplittedAttribute(parentNode, "rows");
	}

	private String[] getColumns(final Node parentNode) {
		return getSplittedAttribute(parentNode, "columns");
	}

	private String[] getSplittedAttribute(final Node parentNode, final String attribute) {
		if (parentNode == null)
			return null;

		final NamedNodeMap nodeAttributes = parentNode.getAttributes();
		if (nodeAttributes != null) {
			final Node node = nodeAttributes.getNamedItem(attribute);
			if (node != null)
				return node.getTextContent().split(",");
		}
		return null;
	}

}

package presentation.codebook.format.odf.renderer;

import java.util.Map;

import org.odftoolkit.simple.table.Row;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import eu.dzhw.zofar.management.utils.odf.components.SimpleWriterDocument;
import eu.dzhw.zofar.management.utils.xml.XmlClient;

public class LEFTODFRenderer extends ODFRenderer {

	public LEFTODFRenderer() {
		super();
	}

	@Override
	public void render(String pageId, Node question, Node current, SimpleWriterDocument doc, Map<String, Object> attachements, final Object container,final boolean attachementMode) throws Exception {
		renderChildren(pageId, question, current, doc, attachements, container);
	}

	@Override
	public void renderChildren(String pageId, Node question, Node current, SimpleWriterDocument doc, Map<String, Object> attachements, Object container,final boolean attachementMode) throws Exception {
		final NodeList children = current.getChildNodes();
		if (children != null) {
			if ((container != null) && ((Row.class).isAssignableFrom(container.getClass()))) {
				Row row = (Row) container;
				
				int colIndex = 0;
				final int childCount = children.getLength();
				for (int a = 0; a < childCount; ++a) {
					final Node child = children.item(a);
					if (XmlClient.getInstance().hasParent(child, "zofar:researchdata"))
						continue;
					if (child.getNodeName().equals("zofar:answerOption")) {
						final ODFRenderer renderer = ODFRenderer.getInstance(child);
						if (renderer != null) {
							renderer.render(pageId, question, child, doc, attachements, row.getCellByIndex(colIndex),attachementMode);
							colIndex = colIndex + 1;
						}
					}
				}
			}
		}
	}
}

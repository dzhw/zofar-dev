package presentation.codebook.format.odf.renderer;

import java.util.Map;

import org.w3c.dom.Node;

import eu.dzhw.zofar.management.utils.odf.components.SimpleWriterDocument;

public class SlotConfODFRenderer extends ODFRenderer {

	public SlotConfODFRenderer() {
		super();
	}

	@Override
	public void render(String pageId, Node question, Node current, SimpleWriterDocument doc, Map<String, Object> attachements, Object container,final boolean attachementMode) throws Exception {
//		renderChildren(pageId, question,current, doc, attachements,container);
	}

}

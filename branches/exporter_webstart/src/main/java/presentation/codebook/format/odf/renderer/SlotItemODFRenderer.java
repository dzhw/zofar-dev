package presentation.codebook.format.odf.renderer;

import java.util.Map;
import java.util.Set;

import org.w3c.dom.Node;

import eu.dzhw.zofar.management.utils.odf.components.SimpleWriterDocument;

public class SlotItemODFRenderer extends ODFRenderer {

	public SlotItemODFRenderer() {
		super();
	}

	@Override
	public void render(String pageId, Node question, Node current, SimpleWriterDocument doc, Map<String, Object> attachements, Object container,final boolean attachementMode) throws Exception {
		final Set<String> visible = this.getVisible(current);
		if(visible != null)doc.addText(visible.toString());
		renderChildren(pageId, question,current, doc, attachements,container,attachementMode);
	}

}

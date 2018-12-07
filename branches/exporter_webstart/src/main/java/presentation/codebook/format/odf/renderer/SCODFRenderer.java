package presentation.codebook.format.odf.renderer;

import java.util.Map;
import java.util.Set;

import org.w3c.dom.Node;

import eu.dzhw.zofar.management.utils.odf.components.SimpleWriterDocument;

public class SCODFRenderer extends ODFRenderer {

	public SCODFRenderer() {
		super();
	}

	@Override
	public void render(String pageId, Node question,Node current, SimpleWriterDocument doc, Map<String, Object> attachements,final Object container,final boolean attachementMode) throws Exception {
		addQuestionType("SingleChoice",doc);
		final Set<String> visible = this.getVisible(current);
		if(visible != null)doc.addText(visible.toString());
		renderChildren(pageId, question,current, doc, attachements,container,attachementMode);
	}

}

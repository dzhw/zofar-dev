/**
 *
 */
package de.his.zofar.presentation.surveyengine.ui.renderer;

import java.io.IOException;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.FacesRenderer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.faces.renderkit.html_basic.GroupRenderer;

import de.his.zofar.presentation.surveyengine.ui.components.question.UIQuestion;
import de.his.zofar.presentation.surveyengine.ui.components.question.atomic.UIPretestQuestion;

/**
 * renderer class for children of UIQuestion.
 *
 * @author le
 *
 */
@FacesRenderer(componentFamily = UIQuestion.COMPONENT_FAMILY, rendererType = ZofarQuestionRenderer.RENDERER_TYPE)
public class ZofarQuestionRenderer extends GroupRenderer {

	public static final String RENDERER_TYPE = "org.zofar.Question";
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ZofarQuestionRenderer.class);
	

	public ZofarQuestionRenderer() {
		super();
	}

	@Override
	public void encodeBegin(final FacesContext context, final UIComponent component) throws IOException {
		if (!component.isRendered()) {
			return;
		}
		final ResponseWriter writer = context.getResponseWriter();
		writer.startElement("article", component);
		writer.writeAttribute("id", component.getClientId(context), null);
		
		if ((UIPretestQuestion.class).isAssignableFrom(component.getClass())) {
			writer.writeAttribute("class", "question-pretest", null);
		}
		
		final UIComponent header = component.getFacet("header");
		if (header != null) {
			writer.startElement("header", header);
			writer.writeAttribute("class", "question-main", null);
			writer.writeAttribute("id", header.getClientId(context)+"_header", null);
			header.encodeAll(context);
			writer.endElement("header");
		}
	}

	@Override
	public void encodeEnd(final FacesContext context, final UIComponent component) throws IOException {
		if (!component.isRendered()) {
			return;
		}
		final ResponseWriter writer = context.getResponseWriter();
		final UIComponent footer = component.getFacet("footer");
		if ((footer != null) && (footer.isRendered())) {
			writer.startElement("footer", component);
			writer.writeAttribute("id", footer.getClientId(context)+"_footer", null);
			footer.encodeAll(context);
			writer.endElement("footer");
		}
		writer.endElement("article");
	}

}

package de.his.zofar.presentation.surveyengine.ui.renderer.matrix.doubleMatrix;

import java.io.IOException;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.FacesRenderer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.his.zofar.presentation.surveyengine.ui.components.question.matrix.singlechoice.UISingleChoiceMatrixItemResponseDomain;
import de.his.zofar.presentation.surveyengine.ui.renderer.answers.matrix.singlechoice.responsedomain.ZofarSingleChoiceMatrixItemResponseDomainRenderer;
/**
 * @author meisner
 *
 */
@FacesRenderer(componentFamily = UISingleChoiceMatrixItemResponseDomain.COMPONENT_FAMILY, rendererType = ZofarDoubleMatrixItemResponseDomainRenderer.RENDERER_TYPE)
public class ZofarDoubleMatrixItemResponseDomainRenderer extends ZofarSingleChoiceMatrixItemResponseDomainRenderer {

	public static final String RENDERER_TYPE = "org.zofar.DoubleMatrixItemResponseDomain";
	private static final Logger LOGGER = LoggerFactory.getLogger(ZofarDoubleMatrixItemResponseDomainRenderer.class);

	
	public ZofarDoubleMatrixItemResponseDomainRenderer() {
		super();
	}

	@Override
	public void encodeBegin(FacesContext context, UIComponent component) throws IOException {
//		LOGGER.info("encodeBegin");
		if (!component.isRendered()) {
			return;
		}
		// super.encodeBegin(context, component);
		final ResponseWriter writer = context.getResponseWriter();
		writer.startElement("div", component);
		writer.writeAttribute("id", component.getClientId(), null);
		writer.writeAttribute("class", "custom-form custom-form-radio make-2-cols add-pipe pipe-zofar", null);

		writer.startElement("div", component);
		writer.writeAttribute("class", "flex-wrapper", null);
	}

	@Override
	public void encodeEnd(FacesContext context, UIComponent component) throws IOException {
//		LOGGER.info("encodeEnd");
		if (!component.isRendered()) {
			return;
		}
		// super.encodeEnd(context, component);
		final ResponseWriter writer = context.getResponseWriter();
		writer.endElement("div");
		writer.endElement("div");
	}
}

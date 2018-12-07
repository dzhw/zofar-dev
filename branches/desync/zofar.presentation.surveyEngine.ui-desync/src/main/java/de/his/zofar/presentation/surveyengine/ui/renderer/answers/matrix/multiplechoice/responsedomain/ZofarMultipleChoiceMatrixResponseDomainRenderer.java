package de.his.zofar.presentation.surveyengine.ui.renderer.answers.matrix.multiplechoice.responsedomain;

import java.io.IOException;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.render.FacesRenderer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.his.zofar.presentation.surveyengine.ui.components.tablebase.AbstractTableResponseDomain;
import de.his.zofar.presentation.surveyengine.ui.renderer.answers.matrix.ZofarMatrixResponseDomainRenderer;
/**
 * @author meisner
 * 
 */
@FacesRenderer(componentFamily = AbstractTableResponseDomain.COMPONENT_FAMILY, rendererType = ZofarMultipleChoiceMatrixResponseDomainRenderer.RENDERER_TYPE)
public class ZofarMultipleChoiceMatrixResponseDomainRenderer extends
		ZofarMatrixResponseDomainRenderer {
	
	public static final String RENDERER_TYPE = "org.zofar.MultipleChoiceCompositeResponseDomain";
	
	private static final Logger LOGGER = LoggerFactory
			.getLogger(ZofarMultipleChoiceMatrixResponseDomainRenderer.class);

	public ZofarMultipleChoiceMatrixResponseDomainRenderer() {
		super();
	}
	
	@Override
	public void encodeBegin(FacesContext context, UIComponent component)
			throws IOException {
		if (!component.isRendered()) {
			return;
		}

		super.encodeBegin(context, component,"zo-mc-matrix-response-domain");
	}

	@Override
	public void encodeChildren(final FacesContext context,
			final UIComponent component) throws IOException {

		if (!component.isRendered()) {
			return;
		}
		super.encodeChildren(context, component);
	}

	@Override
	public void encodeEnd(FacesContext context, UIComponent component)
			throws IOException {
		if (!component.isRendered()) {
			return;
		}

		super.encodeEnd(context, component);
	}

}

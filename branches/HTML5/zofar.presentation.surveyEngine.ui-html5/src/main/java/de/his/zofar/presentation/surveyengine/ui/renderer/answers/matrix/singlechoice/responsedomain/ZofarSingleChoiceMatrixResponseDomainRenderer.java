package de.his.zofar.presentation.surveyengine.ui.renderer.answers.matrix.singlechoice.responsedomain;

import java.io.IOException;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.render.FacesRenderer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.his.zofar.presentation.surveyengine.ui.components.question.matrix.UIMatrixResponseDomain;
import de.his.zofar.presentation.surveyengine.ui.renderer.answers.matrix.ZofarMatrixResponseDomainRenderer;

/**
 * @author meisner
 *
 */
@FacesRenderer(componentFamily = UIMatrixResponseDomain.COMPONENT_FAMILY, rendererType = ZofarSingleChoiceMatrixResponseDomainRenderer.RENDERER_TYPE)
public class ZofarSingleChoiceMatrixResponseDomainRenderer extends ZofarMatrixResponseDomainRenderer {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ZofarSingleChoiceMatrixResponseDomainRenderer.class);
	
	public static final String RENDERER_TYPE = "org.zofar.SingleChoiceMatrixResponseDomain";

	public ZofarSingleChoiceMatrixResponseDomainRenderer() {
		super();
	}

	@Override
	public void encodeBegin(final FacesContext context, final UIComponent component) throws IOException {
		if (!component.isRendered()) {
			return;
		}
		super.encodeBegin(context, component);
	}

	@Override
	public void encodeChildren(final FacesContext context, final UIComponent component) throws IOException {
		if (!component.isRendered()) {
			return;
		}
		((UIMatrixResponseDomain)component).sequenceMatrixItems();
		super.encodeChildren(context, component);
	}

	@Override
	public void encodeEnd(final FacesContext context, final UIComponent component) throws IOException {
		if (!component.isRendered()) {
			return;
		}

		super.encodeEnd(context, component);
	}

}

package de.his.zofar.presentation.surveyengine.ui.renderer.matrix.singlechoice;

import java.io.IOException;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.render.FacesRenderer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.his.zofar.presentation.surveyengine.ui.components.question.matrix.singlechoice.UISingleChoiceMatrix;
import de.his.zofar.presentation.surveyengine.ui.renderer.ZofarQuestionRenderer;

/**
 * renderer class for SingleChoice Matrix
 *
 * @author meisner
 *
 */
@FacesRenderer(componentFamily = UISingleChoiceMatrix.COMPONENT_FAMILY, rendererType = ZofarSingleChoiceMatrixRenderer.RENDERER_TYPE)
public class ZofarSingleChoiceMatrixRenderer extends ZofarQuestionRenderer {

	public static final String RENDERER_TYPE = "org.zofar.SingleChoiceMatrix";
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ZofarSingleChoiceMatrixRenderer.class);

	public ZofarSingleChoiceMatrixRenderer() {
		super();
	}

	@Override
	public boolean getRendersChildren() {
		return true;
	}
	
	@Override
	public void encodeBegin(final FacesContext context, final UIComponent component) throws IOException {

		if (!component.isRendered()) {
			return;
		}
		super.encodeBegin(context, component);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see javax.faces.render.Renderer#encodeChildren(javax.faces.context.
	 * FacesContext , javax.faces.component.UIComponent)
	 */
	@Override
	public void encodeChildren(final FacesContext context, final UIComponent component) throws IOException {

		if (!component.isRendered()) {
			return;
		}
		for (final UIComponent child : component.getChildren()) {
			child.encodeAll(context);
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * javax.faces.render.Renderer#encodeEnd(javax.faces.context.FacesContext,
	 * javax.faces.component.UIComponent)
	 */
	@Override
	public void encodeEnd(final FacesContext context, final UIComponent component) throws IOException {

		if (!component.isRendered()) {
			return;
		}
		super.encodeEnd(context, component);
	}
}

package de.his.zofar.presentation.surveyengine.ui.renderer.matrix.multiplechoice;

import java.io.IOException;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.render.FacesRenderer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.his.zofar.presentation.surveyengine.ui.components.composite.multiplechoice.UIMultipleChoiceComposite;
import de.his.zofar.presentation.surveyengine.ui.renderer.ZofarQuestionRenderer;
/**
 * renderer class for MultipleChoice Matrix
 *
 * @author meisner
 *
 */
@FacesRenderer(componentFamily = UIMultipleChoiceComposite.COMPONENT_FAMILY, rendererType = ZofarMultipleChoiceMatrixRenderer.RENDERER_TYPE)
public class ZofarMultipleChoiceMatrixRenderer extends ZofarQuestionRenderer {
	
	public static final String RENDERER_TYPE = "org.zofar.MultipleChoiceComposite";
	
	private static final Logger LOGGER = LoggerFactory
			.getLogger(ZofarMultipleChoiceMatrixRenderer.class);

	public ZofarMultipleChoiceMatrixRenderer() {
		super();
	}
	
	@Override
	public synchronized boolean getRendersChildren() {
		return true;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * javax.faces.render.Renderer#encodeBegin(javax.faces.context.FacesContext,
	 * javax.faces.component.UIComponent)
	 */
	@Override
	public synchronized void encodeBegin(final FacesContext context,
			final UIComponent component) throws IOException {

		if (!component.isRendered()) {
			return;
		}
		super.encodeBegin(context, component);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * javax.faces.render.Renderer#encodeChildren(javax.faces.context.FacesContext
	 * , javax.faces.component.UIComponent)
	 */
	@Override
	public synchronized void encodeChildren(final FacesContext context,
			final UIComponent component) throws IOException {

		if (!component.isRendered()) {
			return;     
		}
        for (final UIComponent child : component.getChildren()) {
//        	LOGGER.info("child {} ({})",child,child.getRendererType());
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
	public synchronized void encodeEnd(final FacesContext context,
			final UIComponent component) throws IOException {

		if (!component.isRendered()) {
			return;
		}
		super.encodeEnd(context, component);
	}

}

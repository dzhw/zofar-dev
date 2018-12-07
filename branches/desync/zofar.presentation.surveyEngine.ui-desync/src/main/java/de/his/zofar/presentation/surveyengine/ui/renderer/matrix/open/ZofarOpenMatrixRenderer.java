package de.his.zofar.presentation.surveyengine.ui.renderer.matrix.open;

import java.io.IOException;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.render.FacesRenderer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.his.zofar.presentation.surveyengine.ui.components.matrix.open.UIOpenMatrix;
import de.his.zofar.presentation.surveyengine.ui.renderer.ZofarQuestionRenderer;
/**
 * renderer class for Open Matrix
 *
 * @author meisner
 *
 */
@FacesRenderer(componentFamily = UIOpenMatrix.COMPONENT_FAMILY, rendererType = ZofarOpenMatrixRenderer.RENDERER_TYPE)

public class ZofarOpenMatrixRenderer extends ZofarQuestionRenderer {
	
	public static final String RENDERER_TYPE = "org.zofar.OpenMatrix";
	
	private static final Logger LOGGER = LoggerFactory
			.getLogger(ZofarOpenMatrixRenderer.class);

	public ZofarOpenMatrixRenderer() {
		super();
	}
	
	@Override
	public boolean getRendersChildren() {
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
	public void encodeBegin(final FacesContext context,
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
	public void encodeChildren(final FacesContext context,
			final UIComponent component) throws IOException {

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
	public void encodeEnd(final FacesContext context,
			final UIComponent component) throws IOException {

		if (!component.isRendered()) {
			return;
		}
		super.encodeEnd(context, component);
	}

}

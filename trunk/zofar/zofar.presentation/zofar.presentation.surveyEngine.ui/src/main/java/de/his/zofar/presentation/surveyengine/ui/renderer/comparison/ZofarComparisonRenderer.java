package de.his.zofar.presentation.surveyengine.ui.renderer.comparison;

import java.io.IOException;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.render.FacesRenderer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.his.zofar.presentation.surveyengine.ui.components.composite.comparison.UIComparison;
import de.his.zofar.presentation.surveyengine.ui.renderer.ZofarQuestionRenderer;

/**
* @author meisner
*
*/
@FacesRenderer(componentFamily = UIComparison.COMPONENT_FAMILY, rendererType = ZofarComparisonRenderer.RENDERER_TYPE)
public class ZofarComparisonRenderer  extends ZofarQuestionRenderer {
	
	public static final String RENDERER_TYPE = "org.zofar.Comparison";
    private static final Logger LOGGER = LoggerFactory
			.getLogger(ZofarComparisonRenderer.class);

	public ZofarComparisonRenderer() {
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
//        	LOGGER.info("encodeChildren {} ({})",child,child.getRendererType());
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

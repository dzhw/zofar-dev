package de.his.zofar.presentation.surveyengine.ui.renderer.comparison;

import java.io.IOException;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.render.FacesRenderer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.his.zofar.presentation.surveyengine.ui.components.composite.comparison.UIComparisonItem;
import de.his.zofar.presentation.surveyengine.ui.renderer.answers.matrix.ZofarMatrixItemRenderer;
/**
 * @author meisner
 * 
 */
@FacesRenderer(componentFamily = UIComparisonItem.COMPONENT_FAMILY, rendererType = ZofarComparisonItemRenderer.RENDERER_TYPE)
public class ZofarComparisonItemRenderer extends ZofarMatrixItemRenderer {
	
	private static final Logger LOGGER = LoggerFactory
			.getLogger(ZofarComparisonItemRenderer.class);
	
	public static final String RENDERER_TYPE = "org.zofar.ComparisonItem";

	public ZofarComparisonItemRenderer() {
		super();
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
	}
	


}

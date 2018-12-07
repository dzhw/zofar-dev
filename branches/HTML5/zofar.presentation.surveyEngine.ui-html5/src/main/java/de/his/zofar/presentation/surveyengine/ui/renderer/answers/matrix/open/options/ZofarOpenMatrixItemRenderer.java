/*
 *
 */
package de.his.zofar.presentation.surveyengine.ui.renderer.answers.matrix.open.options;

import java.io.IOException;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.render.FacesRenderer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.his.zofar.presentation.surveyengine.ui.components.question.matrix.UIMatrixItem;
import de.his.zofar.presentation.surveyengine.ui.renderer.answers.matrix.ZofarMatrixItemRenderer;

/**
 * @author meisner
 *
 */
@FacesRenderer(componentFamily = UIMatrixItem.COMPONENT_FAMILY, rendererType = ZofarOpenMatrixItemRenderer.RENDERER_TYPE)
public class ZofarOpenMatrixItemRenderer extends ZofarMatrixItemRenderer {

	public static final String RENDERER_TYPE = "org.zofar.OpenMatrixItem";

	private static final Logger LOGGER = LoggerFactory.getLogger(ZofarOpenMatrixItemRenderer.class);

	public ZofarOpenMatrixItemRenderer() {
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

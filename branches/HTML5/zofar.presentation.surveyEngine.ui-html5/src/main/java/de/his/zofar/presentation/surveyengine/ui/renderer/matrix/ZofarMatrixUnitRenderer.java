package de.his.zofar.presentation.surveyengine.ui.renderer.matrix;

import java.io.IOException;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.render.FacesRenderer;
import javax.faces.render.Renderer;

import de.his.zofar.presentation.surveyengine.ui.components.tablebase.AbstractTableResponseDomainUnit;

/**
 * @author meisner
 *
 */
@FacesRenderer(componentFamily = AbstractTableResponseDomainUnit.COMPONENT_FAMILY, rendererType = ZofarMatrixUnitRenderer.RENDERER_TYPE)
public class ZofarMatrixUnitRenderer extends Renderer {

	public static final String RENDERER_TYPE = "org.zofar.MatrixUnit";

	public ZofarMatrixUnitRenderer() {
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

		final UIComponent header = component.getFacet("header");
		if ((header != null) && (header.isRendered())) {
			header.encodeAll(context);
		}
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
		final UIComponent footer = component.getFacet("footer");
		if ((footer != null) && (footer.isRendered())) {
			footer.encodeAll(context);
		}
	}

}

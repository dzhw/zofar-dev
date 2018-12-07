package de.his.zofar.presentation.surveyengine.ui.renderer.display;

import java.io.IOException;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.render.FacesRenderer;
import javax.faces.render.Renderer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.his.zofar.presentation.surveyengine.ui.components.display.UIDisplayContainer;

/**
 * @author meisner
 *
 */
@FacesRenderer(componentFamily = UIDisplayContainer.COMPONENT_FAMILY, rendererType = ContainerRenderer.RENDERER_TYPE)
public class ContainerRenderer extends Renderer {

	private static final Logger LOGGER = LoggerFactory.getLogger(ContainerRenderer.class);

	public static final String RENDERER_TYPE = "org.zofar.display.container";

	public ContainerRenderer() {
		super();
	}

	@Override
	public void encodeBegin(final FacesContext context, final UIComponent component) throws IOException {
		super.encodeBegin(context, component);
	}

	@Override
	public void encodeChildren(final FacesContext context, final UIComponent component) throws IOException {
		if (component == null) {
			return;
		}
		if (!component.isRendered()) {
			return;
		}

		for (final UIComponent child : component.getChildren()) {
			child.encodeAll(context);
		}
	}

	@Override
	public void encodeEnd(final FacesContext context, final UIComponent component) throws IOException {
		super.encodeEnd(context, component);
	}

	@Override
	public boolean getRendersChildren() {
		return true;
	}
}

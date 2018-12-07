package de.his.zofar.presentation.surveyengine.ui.renderer.buttons;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.FacesRenderer;
import javax.faces.render.Renderer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.his.zofar.presentation.surveyengine.ui.components.buttons.Jumper;
import de.his.zofar.presentation.surveyengine.ui.components.buttons.JumperContainer;

/**
 * @author meisner
 *
 */
@FacesRenderer(componentFamily = JumperContainer.COMPONENT_FAMILY, rendererType = BreadCrumbNavRenderer.RENDERER_TYPE)
public class BreadCrumbNavRenderer extends Renderer {

	public static final String RENDERER_TYPE = "org.zofar.BreadCrumbNav";

	private static final Logger LOGGER = LoggerFactory.getLogger(BreadCrumbNavRenderer.class);

	public BreadCrumbNavRenderer() {
		super();
	}

	@Override
	public void encodeChildren(final FacesContext context, final UIComponent component) throws IOException {

		if (!component.isRendered()) {
			return;
		}

		this.encodeChildrenHelper(context, component, component.getChildren());
	}

	private void encodeChildrenHelper(final FacesContext context, final UIComponent component, final List<UIComponent> children) throws IOException {
		if (!component.isRendered()) {
			return;
		}
		final ResponseWriter writer = context.getResponseWriter();
		final Iterator<UIComponent> it = children.iterator();
		while (it.hasNext()) {
			final UIComponent child = it.next();
			if (Jumper.class.isAssignableFrom(child.getClass())) {
				child.encodeAll(context);
				if (it.hasNext()) {
					writer.write("&nbsp;&gt;&nbsp;");
				}
			}

		}
	}

	@Override
	public boolean getRendersChildren() {
		return true;
	}

}

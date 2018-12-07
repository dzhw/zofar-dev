package de.his.zofar.presentation.surveyengine.ui.renderer.container;

import java.io.IOException;
import java.util.List;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.render.FacesRenderer;
import javax.faces.render.Renderer;

import de.his.zofar.presentation.surveyengine.ui.components.common.UISort;
import de.his.zofar.presentation.surveyengine.ui.components.container.Section;

/**
 * @author meisner
 * 
 */
@FacesRenderer(componentFamily = Section.COMPONENT_FAMILY, rendererType = SectionRenderer.RENDERER_TYPE)
public class SectionRenderer extends Renderer {

	public static final String RENDERER_TYPE = "org.zofar.Section";

	// private static final Logger LOGGER = LoggerFactory
	// .getLogger(SectionRenderer.class);

	public SectionRenderer() {
		super();
	}

	@Override
	public boolean getRendersChildren() {
		return true;
	}

	@Override
	public synchronized void encodeBegin(FacesContext context,
			UIComponent component) throws IOException {
		UIComponent header = component.getFacet("header");
		if ((header != null) && (header.isRendered()))
			header.encodeAll(context);
	}

	// @Override
	// public void encodeChildren(FacesContext context, UIComponent component)
	// throws IOException {
	// // TODO Auto-generated method stub
	// super.encodeChildren(context, component);
	// }

	@Override
	public synchronized void encodeChildren(final FacesContext context,
			final UIComponent component) throws IOException {
		this.encodeChildrenHelper(context, component, component.getChildren());
	}

	private synchronized void encodeChildrenHelper(final FacesContext context,
			final UIComponent component, final List<UIComponent> children)
			throws IOException {

		for (final UIComponent child : children) {
			if (UISort.class.isAssignableFrom(child.getClass())) {
				this.encodeChildrenHelper(context, component,
						((UISort) child).sortChildren());
			} else {
				child.encodeAll(context);
			}
		}
	}

	@Override
	public synchronized void encodeEnd(FacesContext context,
			UIComponent component) throws IOException {
		UIComponent footer = component.getFacet("footer");
		if ((footer != null) && (footer.isRendered()))
			footer.encodeAll(context);
	}
}

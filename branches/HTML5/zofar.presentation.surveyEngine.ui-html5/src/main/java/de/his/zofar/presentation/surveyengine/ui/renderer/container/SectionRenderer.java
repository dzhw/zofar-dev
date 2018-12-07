package de.his.zofar.presentation.surveyengine.ui.renderer.container;

import java.io.IOException;
import java.util.List;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
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

	public SectionRenderer() {
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
		
		final ResponseWriter writer = context.getResponseWriter();
		
		writer.startElement("section", component);
		if(((Section)component).getPageAttribute())writer.writeAttribute("data-page", "true", null);
		
		writer.writeAttribute("id", component.getClientId(context), null);
		
		if (((Section)component).getAccordionAttribute()) {
			writer.startElement("div", component);
			writer.writeAttribute("class", "containerAcc", null);
			writer.startElement("div", component);
			writer.writeAttribute("class", "headerAcc", null);
		}
		
		
		final UIComponent header = component.getFacet("header");
		
		if ((header != null) && (header.isRendered())) {
			writer.startElement("header", header);
			writer.writeAttribute("id", header.getClientId(context)+"_header", null);
			header.encodeAll(context);
			writer.endElement("header");
		}
		
		if (((Section)component).getAccordionAttribute()) {
			writer.endElement("div");
			writer.startElement("div", component);
			writer.writeAttribute("class", "collapseDiv", null);
		}
		
	}

	@Override
	public void encodeChildren(final FacesContext context, final UIComponent component) throws IOException {
		if (!component.isRendered()) {
			return;
		}

		this.encodeChildrenHelper(context, component, component.getChildren());
	}

	private void encodeChildrenHelper(final FacesContext context, final UIComponent component, final List<UIComponent> children) throws IOException {

		for (final UIComponent child : children) {
			if (UISort.class.isAssignableFrom(child.getClass())) {
				this.encodeChildrenHelper(context, component, ((UISort) child).sortChildren());
			} else {
				child.encodeAll(context);
			}
		}
	}

	@Override
	public void encodeEnd(final FacesContext context, final UIComponent component) throws IOException {
		if (!component.isRendered()) {
			return;
		}
		final ResponseWriter writer = context.getResponseWriter();
		if (((Section)component).getAccordionAttribute()) writer.endElement("div");
		final UIComponent footer = component.getFacet("footer");
		if ((footer != null) && (footer.isRendered())) {
			writer.startElement("footer", component);
			writer.writeAttribute("id", footer.getClientId(context)+"_footer", null);
			footer.encodeAll(context);
			writer.endElement("footer");
		}
		
		writer.endElement("section");
	}
}

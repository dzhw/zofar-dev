package de.his.zofar.presentation.surveyengine.ui.renderer.container;

import java.io.IOException;
import java.util.List;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.FacesRenderer;
import javax.faces.render.Renderer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.his.zofar.presentation.surveyengine.ui.components.answer.responseDomain.RadioButtonSingleChoiceResponseDomain;
import de.his.zofar.presentation.surveyengine.ui.components.answer.responseDomain.UIDropDownResponseDomain;
import de.his.zofar.presentation.surveyengine.ui.components.common.UISort;
import de.his.zofar.presentation.surveyengine.ui.components.container.Section;
import de.his.zofar.presentation.surveyengine.ui.util.JsfUtility;

/**
 * @author meisner
 *
 */
@FacesRenderer(componentFamily = Section.COMPONENT_FAMILY, rendererType = UnitRenderer.RENDERER_TYPE)
public class UnitRenderer extends Renderer {

	public static final String RENDERER_TYPE = "org.zofar.Unit";

	@SuppressWarnings("unused")
	private static final Logger LOGGER = LoggerFactory.getLogger(UnitRenderer.class);

	public UnitRenderer() {
		super();
	}

	@Override
	public boolean getRendersChildren() {
		return true;
	}

	@Override
	public void encodeBegin(final FacesContext context, final UIComponent component) throws IOException {
		LOGGER.info("begin {}", component.getClientId());

		if (!component.isRendered()) {
			return;
		}

		final ResponseWriter writer = context.getResponseWriter();

		final UIComponent rdc = JsfUtility.getInstance().getParentResponseDomain(component);
		if ((rdc != null) && ((UIDropDownResponseDomain.class).isAssignableFrom(rdc.getClass()))) {
			writer.startElement("optgroup", component);
			writer.writeAttribute("id", component.getClientId(context), null);

			final UIComponent header = component.getFacet("header");

			if ((header != null) && (header.isRendered())) {
				writer.writeAttribute("label", JsfUtility.getInstance().getTextComponentAsString(context,header), null);
			}
		} else {
			writer.startElement("fieldset", component);
			writer.writeAttribute("id", component.getClientId(context), null);

			final UIComponent header = component.getFacet("header");

			if ((header != null) && (header.isRendered())) {
				writer.startElement("legend", header);
				writer.writeAttribute("id", header.getClientId(context) + "_header", null);
				header.encodeAll(context);
				writer.endElement("legend");
			}
		}
	}

	@Override
	public void encodeChildren(final FacesContext context, final UIComponent component) throws IOException {
		LOGGER.info("children {}", component.getClientId());

		if (!component.isRendered()) {
			return;
		}

		final ResponseWriter writer = context.getResponseWriter();

		final String injectedClasses = ((Section) component).getInjectedClasses();

		if ((injectedClasses != null) && (!injectedClasses.equals(""))) {
			writer.startElement("div", component);
			writer.writeAttribute("class", injectedClasses, null);
		}
		this.encodeChildrenHelper(context, component, component.getChildren());
		if ((injectedClasses != null) && (!injectedClasses.equals(""))) {
			writer.endElement("div");
		}
	}

	private void encodeChildrenHelper(final FacesContext context, final UIComponent component, final List<UIComponent> children) throws IOException {
		LOGGER.info("helper {}", component.getClientId());
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
		LOGGER.info("end {}", component.getClientId());
		if (!component.isRendered()) {
			return;
		}
		final ResponseWriter writer = context.getResponseWriter();
		final UIComponent rdc = JsfUtility.getInstance().getParentResponseDomain(component);
		if ((rdc != null) && ((UIDropDownResponseDomain.class).isAssignableFrom(rdc.getClass()))) {
			writer.endElement("optgroup");
		} else {
			writer.endElement("fieldset");
		}
	}

}

package de.his.zofar.presentation.surveyengine.ui.renderer.answers.multiplechoice.responsedomain;

import java.io.IOException;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.FacesRenderer;

import de.his.zofar.presentation.surveyengine.ui.components.answer.responseDomain.MultipleChoiceResponseDomain;
import de.his.zofar.presentation.surveyengine.ui.components.common.UISort;
import de.his.zofar.presentation.surveyengine.ui.components.container.Section;
import de.his.zofar.presentation.surveyengine.ui.renderer.answers.ZofarResponseDomainRenderer;

/**
 * @author dick MUST STAY SYNCHRONIZED
 */
@FacesRenderer(componentFamily = MultipleChoiceResponseDomain.COMPONENT_FAMILY, rendererType = ZofarHorizontalCheckboxResponseDomainRenderer.RENDERER_TYPE)
public class ZofarHorizontalCheckboxResponseDomainRenderer extends ZofarMultipleChoiceResponseDomainRenderer {

	public static final String RENDERER_TYPE = "org.zofar.HorizontalCheckboxResponseDomain";

	// private static final Logger LOGGER = LoggerFactory
	// .getLogger(ZofarHorizontalRadioResponseDomainRenderer.class);

	public ZofarHorizontalCheckboxResponseDomainRenderer() {
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
	public synchronized void encodeBegin(final FacesContext context, final UIComponent component) throws IOException {

		if (!component.isRendered()) {
			return;
		}
		final ResponseWriter writer = context.getResponseWriter();
		writer.startElement("div", component);
		writer.writeAttribute("class", "form-orientation form-horizontal", null);
		writer.writeAttribute("id", component.getClientId(context), null);
		writer.startElement("div", component);
		writer.writeAttribute("class", "form-scroller", null);
		writer.startElement("div", component);
		writer.writeAttribute("class", "form-inner form-1-col", null);
		writer.startElement("div", component);
		writer.writeAttribute("class", "custom-form custom-form-checkbox", null);
		writer.writeAttribute("id", "group1", null);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see javax.faces.render.Renderer#encodeChildren(javax.faces.context.
	 * FacesContext , javax.faces.component.UIComponent)
	 */
	@Override
	public synchronized void encodeChildren(final FacesContext context, final UIComponent component)
			throws IOException {
		if (!component.isRendered()) {
			return;
		}
		for (final UIComponent child : component.getChildren()) {
			if (!child.isRendered()) {
				continue;
			}
			if ((Section.class).isAssignableFrom(child.getClass())) {
				child.encodeAll(context);
			} else if ((UISort.class).isAssignableFrom(child.getClass())) {
				for (final UIComponent sortChild : ((UISort) child).sortChildren()) {
					if (!sortChild.isRendered()) {
						continue;
					}
					sortChild.encodeAll(context);
				}
			} else {
				child.encodeAll(context);
			}
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
	public synchronized void encodeEnd(final FacesContext context, final UIComponent component) throws IOException {
		if (!component.isRendered()) {
			return;
		}

		final ResponseWriter writer = context.getResponseWriter();

		writer.endElement("div");
		writer.endElement("div");
		writer.endElement("div");
		writer.endElement("div");

	}
}

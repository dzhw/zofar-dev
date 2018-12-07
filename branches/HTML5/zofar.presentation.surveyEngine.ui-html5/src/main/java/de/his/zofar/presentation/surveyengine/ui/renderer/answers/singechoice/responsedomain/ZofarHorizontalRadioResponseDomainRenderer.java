package de.his.zofar.presentation.surveyengine.ui.renderer.answers.singechoice.responsedomain;

import java.io.IOException;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.FacesRenderer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.his.zofar.presentation.surveyengine.ui.components.answer.responseDomain.RadioButtonSingleChoiceResponseDomain;
import de.his.zofar.presentation.surveyengine.ui.components.common.UISort;
import de.his.zofar.presentation.surveyengine.ui.components.container.Section;

/**
 * @author meisner
 *
 */
@FacesRenderer(componentFamily = RadioButtonSingleChoiceResponseDomain.COMPONENT_FAMILY, rendererType = ZofarHorizontalRadioResponseDomainRenderer.RENDERER_TYPE)
public class ZofarHorizontalRadioResponseDomainRenderer extends ZofarSingleChoiceResponseDomainRenderer {

	public static final String RENDERER_TYPE = "org.zofar.HorizontalRadioResponseDomain";

	private static final Logger LOGGER = LoggerFactory.getLogger(ZofarHorizontalRadioResponseDomainRenderer.class);

	public ZofarHorizontalRadioResponseDomainRenderer() {
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
		writer.writeAttribute("class", "form-inner form-1-col pt-1 pt-sm-4", null);
		writer.startElement("div", component);
		writer.writeAttribute("class", "custom-form custom-form-radio add-pipe", null);
		writer.startElement("div", component);
		writer.writeAttribute("class", "flex-wrapper", null);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see javax.faces.render.Renderer#encodeChildren(javax.faces.context.
	 * FacesContext , javax.faces.component.UIComponent)
	 */
	@Override
	public synchronized void encodeChildren(final FacesContext context, final UIComponent component) throws IOException {
		if (!component.isRendered()) {
			return;
		}
		((RadioButtonSingleChoiceResponseDomain)component).sequenceAnswerOptions();
		
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
		writer.endElement("div");

	}
}

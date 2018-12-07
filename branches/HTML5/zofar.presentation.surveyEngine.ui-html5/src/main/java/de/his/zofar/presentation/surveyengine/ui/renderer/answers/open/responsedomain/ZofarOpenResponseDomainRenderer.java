package de.his.zofar.presentation.surveyengine.ui.renderer.answers.open.responsedomain;

import java.io.IOException;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.FacesRenderer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.his.zofar.presentation.surveyengine.ui.components.answer.responseDomain.OpenResponseDomain;
import de.his.zofar.presentation.surveyengine.ui.components.answer.responseDomain.RadioButtonSingleChoiceResponseDomain;
import de.his.zofar.presentation.surveyengine.ui.components.common.UISort;
import de.his.zofar.presentation.surveyengine.ui.components.container.Section;
import de.his.zofar.presentation.surveyengine.ui.renderer.answers.ZofarResponseDomainRenderer;

/**
 * @author meisner
 *
 */
@FacesRenderer(componentFamily = OpenResponseDomain.COMPONENT_FAMILY, rendererType = ZofarOpenResponseDomainRenderer.RENDERER_TYPE)
public class ZofarOpenResponseDomainRenderer extends ZofarResponseDomainRenderer  {
	
	public static final String RENDERER_TYPE = "org.zofar.OpenResponseDomain";
	private static final Logger LOGGER = LoggerFactory.getLogger(ZofarOpenResponseDomainRenderer.class);

	
	@Override
	public void encodeBegin(FacesContext context, UIComponent component) throws IOException {
		if (!component.isRendered()) {
			return;
		}
		final ResponseWriter writer = context.getResponseWriter();
		writer.startElement("div", component);
		writer.writeAttribute("class", "form-orientation form-responsive", null);
		writer.writeAttribute("id", component.getClientId(context), null);
		writer.startElement("div", component);
		writer.writeAttribute("class", "form-scroller", null);
		writer.startElement("div", component);
		writer.writeAttribute("class", "form-inner form-1-col", null);
		writer.startElement("div", component);
		writer.writeAttribute("class", "form-inline form-group", null);
	}

	@Override
	public void encodeChildren(final FacesContext context, final UIComponent component) throws IOException {
		if (!component.isRendered()) {
			return;
		}
		
//		((RadioButtonSingleChoiceResponseDomain)component).sequenceAnswerOptions();
//		
//		for (final UIComponent child : component.getChildren()) {
//			if (!child.isRendered()) {
//				continue;
//			}
//			if ((Section.class).isAssignableFrom(child.getClass())) {
//				((Section)child).setInjectedClasses("custom-form custom-form-radio");
//				child.encodeAll(context);
//			} else if ((UISort.class).isAssignableFrom(child.getClass())) {
//				for (final UIComponent sortChild : ((UISort) child).sortChildren()) {
//					if (!sortChild.isRendered()) {
//						continue;
//					}
//					sortChild.encodeAll(context);
//				}
//			} else {
//				child.encodeAll(context);
//			}
//		}
		super.encodeChildren(context, component);
	}

	@Override
	public void encodeEnd(FacesContext context, UIComponent component) throws IOException {
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

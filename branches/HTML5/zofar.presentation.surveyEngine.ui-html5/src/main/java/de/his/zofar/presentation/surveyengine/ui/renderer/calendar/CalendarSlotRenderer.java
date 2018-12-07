package de.his.zofar.presentation.surveyengine.ui.renderer.calendar;

import java.io.IOException;
import java.util.Map;

import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.FacesRenderer;
import javax.faces.render.Renderer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.his.zofar.presentation.surveyengine.ui.components.composite.calendar.UICalendarSlot;

/**
 * @author meisner
 *
 */
@FacesRenderer(componentFamily = UICalendarSlot.COMPONENT_FAMILY, rendererType = CalendarSlotRenderer.RENDERER_TYPE)
public class CalendarSlotRenderer extends Renderer {

	private static final Logger LOGGER = LoggerFactory.getLogger(CalendarSlotRenderer.class);

	public static final String RENDERER_TYPE = "de.his.zofar.Calendar.Slot";

	@Override
	public void decode(final FacesContext context, final UIComponent component) {
		if (!component.isRendered()) {
			return;
		}

		String clientId = null;

		if (clientId == null) {
			clientId = component.getClientId(context);
		}
		assert (clientId != null);
		// Convert the new value

		final Map<String, String> requestParameterMap = context.getExternalContext().getRequestParameterMap();

		final boolean isChecked = this.isChecked(requestParameterMap.get(clientId));
		this.setSubmittedValue(component, isChecked);
	}

	public void setSubmittedValue(final UIComponent component, final Object value) {

		if (component instanceof UIInput) {
			((UIInput) component).setValue(value);
			 if (CalendarSlotRenderer.LOGGER.isDebugEnabled()) {
			CalendarSlotRenderer.LOGGER.debug("set checkbox value: {}", value);
			 }
		}
	}

	private boolean isChecked(final String value) {

		 if (CalendarSlotRenderer.LOGGER.isDebugEnabled()) {
		CalendarSlotRenderer.LOGGER.debug("raw value from request map: {}", value);
		}

		return value != null;
	}

	@Override
	public void encodeBegin(FacesContext context, UIComponent component) throws IOException {
		if (!component.isRendered()) {
			return;
		}

		final Boolean isSelected = Boolean.valueOf(component.getAttributes().get("value").toString());
		 if (CalendarSlotRenderer.LOGGER.isDebugEnabled()) {
		CalendarSlotRenderer.LOGGER.debug("selectedValue {} = {}",component.getClientId(), isSelected);
		 }
		final ResponseWriter writer = context.getResponseWriter();
		writer.startElement("input", component);
		writer.writeAttribute("type", "checkbox", null);
		writer.writeAttribute("name", component.getClientId(), null);
		writer.writeAttribute("id", component.getClientId(), null);
		writer.writeAttribute("value", component.getId(), "value");
		if (isSelected) {
			writer.writeAttribute("checked", "checked", null);
		}
		writer.endElement("input");
	}

	@Override
	public boolean getRendersChildren() {
		return true;
	}

}

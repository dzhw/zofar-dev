package de.his.zofar.presentation.surveyengine.ui.components.composite.calendar;

import javax.faces.component.FacesComponent;
import javax.faces.component.NamingContainer;
import javax.faces.component.UIInput;

import de.his.zofar.presentation.surveyengine.ui.renderer.calendar.CalendarSlotRenderer;

/**
 * @author meisner
 *
 */
@FacesComponent(value = "de.his.zofar.Calendar.Slot")
public class UICalendarSlot extends UIInput implements NamingContainer {

	public static final String COMPONENT_FAMILY = "de.his.zofar.Calendar.Slot";

	public UICalendarSlot() {
		super();
	}

	@Override
	public String getFamily() {
		return COMPONENT_FAMILY;
	}

	@Override
	public String getRendererType() {
		return CalendarSlotRenderer.RENDERER_TYPE;
	}

}

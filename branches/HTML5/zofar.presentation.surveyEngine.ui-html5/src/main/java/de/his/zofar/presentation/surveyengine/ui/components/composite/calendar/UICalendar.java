package de.his.zofar.presentation.surveyengine.ui.components.composite.calendar;

import javax.faces.component.FacesComponent;
import javax.faces.component.UINamingContainer;

import de.his.zofar.presentation.surveyengine.ui.components.common.Identificational;
import de.his.zofar.presentation.surveyengine.ui.components.common.Visible;
import de.his.zofar.presentation.surveyengine.ui.renderer.calendar.CalendarRenderer;

/**
 * @author meisner
 *
 */
@FacesComponent(value = "de.his.zofar.Calendar.calendarHTML5")
public class UICalendar extends UINamingContainer implements Identificational, Visible {
	
	public static final String COMPONENT_FAMILY = "de.his.zofar.Calendar.calendarHTML5";

	public UICalendar() {
		super();
	}
	
	@Override
	public String getFamily() {
		return COMPONENT_FAMILY;
	}
	
	@Override
	public String getRendererType() {
		return CalendarRenderer.RENDERER_TYPE;
	}
	
	/**
	 * We do not want to participate in state saving.
	 */
	@Override
	public boolean isTransient() {
		return true;
	}
}

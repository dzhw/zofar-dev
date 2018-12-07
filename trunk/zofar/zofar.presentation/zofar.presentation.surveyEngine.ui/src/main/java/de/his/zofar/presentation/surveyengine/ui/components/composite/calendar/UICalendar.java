package de.his.zofar.presentation.surveyengine.ui.components.composite.calendar;

import javax.faces.component.FacesComponent;
import javax.faces.component.UINamingContainer;

import de.his.zofar.presentation.surveyengine.ui.components.common.Identificational;
import de.his.zofar.presentation.surveyengine.ui.components.common.Visible;

/**
 * @author meisner
 *
 */
@FacesComponent(value = "de.his.zofar.Calendar.Calendar")
public class UICalendar extends UINamingContainer implements Identificational,Visible {

	public UICalendar() {
		super();
	}

	@Override
	@Deprecated
	public String getUID() {
		return this.getId();
	}

	@Override
	@Deprecated
	public Boolean visibleCondition() {
		return this.isRendered();
	}

}

package de.his.zofar.presentation.surveyengine.ui.components.composite.calendar;

import javax.faces.component.FacesComponent;
import javax.faces.component.UINamingContainer;

import de.his.zofar.presentation.surveyengine.ui.components.common.Identificational;
import de.his.zofar.presentation.surveyengine.ui.components.common.Visible;
import de.his.zofar.presentation.surveyengine.ui.interfaces.IAnswerBean;

/**
 * @author meisner
 *
 */
@FacesComponent(value = "de.his.zofar.Calendar.ColumnItem")
public class UICalendarColumnItem extends UINamingContainer implements Identificational, Visible {

	public static final String COMPONENT_FAMILY = "de.his.zofar.Calendar.ColumnItem";

	public UICalendarColumnItem() {
		super();
	}
	
	public String getLabelAttribute() {
		final String value = (String) this.getAttributes().get("label");
		return value;
	}
	
	/**
	 * We do not want to participate in state saving.
	 */
	@Override
	public boolean isTransient() {
		return true;
	}
}

package de.his.zofar.presentation.surveyengine.ui.components.trigger;

import javax.faces.component.FacesComponent;
import javax.faces.component.UINamingContainer;

import de.his.zofar.presentation.surveyengine.ui.components.common.Identificational;

/**
 * @author meisner
 *
 */
@FacesComponent(value = "org.zofar.ScriptItem")
public class ScriptItem extends UINamingContainer implements Identificational {

	public ScriptItem() {
		super();
	}

	/**
	 * @return the value of direction-Attribute defined in Composite
	 */
	public String getValueAttribute() {
		String value = (String) this.getAttributes().get("value");
		if (value == null) {
			value = "";
		}
		value = value.trim();
		return value;
	}
}

/**
 *
 */
package de.his.zofar.presentation.surveyengine.ui.components.answer.options;

import javax.faces.component.FacesComponent;
import javax.faces.component.UINamingContainer;

import de.his.zofar.presentation.surveyengine.ui.components.common.Identificational;
import de.his.zofar.presentation.surveyengine.ui.components.common.Visible;

/**
 * @author meisner
 *
 */
@FacesComponent(value = "org.zofar.BooleanOption")
public class BooleanOption extends UINamingContainer implements Identificational,Visible {

    public BooleanOption() {
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

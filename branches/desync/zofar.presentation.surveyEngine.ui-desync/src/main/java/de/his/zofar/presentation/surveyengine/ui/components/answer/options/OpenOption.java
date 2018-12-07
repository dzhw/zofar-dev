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
@FacesComponent(value = "org.zofar.OpenOption")
public class OpenOption extends UINamingContainer implements Identificational,Visible {

    public OpenOption() {
        super();
    }
    
	@Override
	@Deprecated
	public Boolean visibleCondition() {
		return this.isRendered();
	}

	@Override
	@Deprecated
	public String getUID() {
		return this.getId();
	}
}

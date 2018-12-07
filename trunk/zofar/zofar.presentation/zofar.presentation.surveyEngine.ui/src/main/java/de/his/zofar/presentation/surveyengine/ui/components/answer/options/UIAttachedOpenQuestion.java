/**
 *
 */
package de.his.zofar.presentation.surveyengine.ui.components.answer.options;

import javax.faces.component.FacesComponent;
import javax.faces.component.UINamingContainer;

import de.his.zofar.presentation.surveyengine.ui.components.common.Identificational;
import de.his.zofar.presentation.surveyengine.ui.components.common.Visible;

/**
 * @author le
 *
 */
@FacesComponent(value = "org.zofar.AttachedOpenQuestion")
public class UIAttachedOpenQuestion extends UINamingContainer implements Identificational,Visible {

    public UIAttachedOpenQuestion() {
        super();
    }

    public String getCompleteInputId() {
        final String inputId = (String) this.getAttributes().get("inputId");
        return this.getClientId() + ":" + inputId;
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

/**
 *
 */
package de.his.zofar.presentation.surveyengine.ui.components.answer.responseDomain;

import javax.faces.component.FacesComponent;
import javax.faces.component.UINamingContainer;

import de.his.zofar.presentation.surveyengine.ui.components.common.Identificational;

/**
 * @author meisner
 *
 */
@FacesComponent(value = "org.zofar.BooleanResponseDomain")
public class BooleanResponseDomain extends UINamingContainer implements Identificational{

    public BooleanResponseDomain() {
        super();
    }
    
	@Override
	@Deprecated
	public String getUID() {
		return this.getId();
	}
}

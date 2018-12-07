package de.his.zofar.presentation.surveyengine.ui.components.answer.responseDomain;

import javax.faces.component.FacesComponent;
import javax.faces.component.UINamingContainer;

import de.his.zofar.presentation.surveyengine.ui.components.common.Identificational;
import de.his.zofar.presentation.surveyengine.ui.interfaces.IResponseDomain;

/**
 * @author dick/xxxx
 *
 */
@FacesComponent("org.zofar.responsedomain.DropDownMissing")
public class UIDropDownMissingResponseDomain extends UINamingContainer implements Identificational,IResponseDomain {

	public UIDropDownMissingResponseDomain() {
		super();
	}
}

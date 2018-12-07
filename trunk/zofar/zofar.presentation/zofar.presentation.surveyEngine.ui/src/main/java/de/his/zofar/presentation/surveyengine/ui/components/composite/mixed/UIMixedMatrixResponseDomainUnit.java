package de.his.zofar.presentation.surveyengine.ui.components.composite.mixed;

import javax.faces.component.FacesComponent;

import de.his.zofar.presentation.surveyengine.ui.components.common.Identificational;
import de.his.zofar.presentation.surveyengine.ui.components.common.Visible;
import de.his.zofar.presentation.surveyengine.ui.components.tablebase.AbstractTableResponseDomainUnit;

/**
 * @author meisner
 *
 */
@FacesComponent("org.zofar.MixedMatrixUnit")
public class UIMixedMatrixResponseDomainUnit  extends
AbstractTableResponseDomainUnit implements Identificational,Visible{

	public UIMixedMatrixResponseDomainUnit() {
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

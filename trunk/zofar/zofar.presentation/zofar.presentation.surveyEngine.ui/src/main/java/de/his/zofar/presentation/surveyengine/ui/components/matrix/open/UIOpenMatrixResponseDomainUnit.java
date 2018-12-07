/**
 *
 */
package de.his.zofar.presentation.surveyengine.ui.components.matrix.open;

import javax.faces.component.FacesComponent;

import de.his.zofar.presentation.surveyengine.ui.components.tablebase.AbstractTableResponseDomainUnit;

/**
 * @author le
 *
 */
@FacesComponent("org.zofar.OpenMatrixResponseDomainUnit")
public class UIOpenMatrixResponseDomainUnit extends
        AbstractTableResponseDomainUnit {

    public UIOpenMatrixResponseDomainUnit() {
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

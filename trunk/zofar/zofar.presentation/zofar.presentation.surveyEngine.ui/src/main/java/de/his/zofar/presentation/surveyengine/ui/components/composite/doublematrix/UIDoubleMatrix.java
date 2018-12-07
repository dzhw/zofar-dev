/**
 *
 */
package de.his.zofar.presentation.surveyengine.ui.components.composite.doublematrix;

import javax.faces.component.FacesComponent;
import javax.faces.component.UINamingContainer;

import de.his.zofar.presentation.surveyengine.ui.components.common.Identificational;
import de.his.zofar.presentation.surveyengine.ui.components.common.Visible;

/**
 * @author le
 *
 */
@FacesComponent(value = "org.zofar.DoubleMatrix")
public class UIDoubleMatrix extends UINamingContainer implements Identificational,Visible {

    /**
     *
     */
    public UIDoubleMatrix() {
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

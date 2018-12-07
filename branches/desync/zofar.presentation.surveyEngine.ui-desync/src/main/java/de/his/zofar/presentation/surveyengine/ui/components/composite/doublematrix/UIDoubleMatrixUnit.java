/**
 *
 */
package de.his.zofar.presentation.surveyengine.ui.components.composite.doublematrix;

import javax.faces.component.FacesComponent;

import de.his.zofar.presentation.surveyengine.ui.components.common.Identificational;
import de.his.zofar.presentation.surveyengine.ui.components.common.Visible;
import de.his.zofar.presentation.surveyengine.ui.components.tablebase.AbstractTableResponseDomainUnit;

/**
 * @author le
 *
 */
@FacesComponent(value = "org.zofar.DoubleMatrixUnit")
public class UIDoubleMatrixUnit  extends AbstractTableResponseDomainUnit implements Identificational,Visible {

    public UIDoubleMatrixUnit() {
        super();
    }
}

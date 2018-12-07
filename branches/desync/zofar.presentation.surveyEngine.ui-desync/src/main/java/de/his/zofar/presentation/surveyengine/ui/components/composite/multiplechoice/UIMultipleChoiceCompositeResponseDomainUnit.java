/**
 *
 */
package de.his.zofar.presentation.surveyengine.ui.components.composite.multiplechoice;

import javax.faces.component.FacesComponent;

import de.his.zofar.presentation.surveyengine.ui.components.common.Identificational;
import de.his.zofar.presentation.surveyengine.ui.components.common.Visible;
import de.his.zofar.presentation.surveyengine.ui.components.tablebase.AbstractTableResponseDomainUnit;

/**
 * @author le
 *
 */
@FacesComponent("org.zofar.MultipleChoiceCompositeResponseDomainUnit")
public class UIMultipleChoiceCompositeResponseDomainUnit extends
        AbstractTableResponseDomainUnit implements Identificational,Visible{

    public UIMultipleChoiceCompositeResponseDomainUnit() {
        super();
    }

}

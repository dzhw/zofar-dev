package de.his.zofar.presentation.surveyengine.ui.components.question;

import javax.faces.component.FacesComponent;

/**
 * @author meisner
 *
 */
@FacesComponent("org.zofar.PretestQuestion")
public class UIPretestQuestion extends UIOpenQuestion {

    @Override
    protected String getSpecificStyleClass() {
        return "zo-pretest";
    }

}

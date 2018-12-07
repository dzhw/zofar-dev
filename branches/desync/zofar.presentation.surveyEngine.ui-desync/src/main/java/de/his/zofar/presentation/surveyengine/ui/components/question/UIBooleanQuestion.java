/**
 *
 */
package de.his.zofar.presentation.surveyengine.ui.components.question;

import javax.faces.component.FacesComponent;

/**
 * @author le
 *
 */
@FacesComponent("org.zofar.BooleanQuestion")
public class UIBooleanQuestion extends UIQuestion {
	/*
     * (non-Javadoc)
     *
     * @see
     * de.his.zofar.presentation.surveyengine.ui.components.question.UIQuestion
     * #getSpecificStyleClass()
     */
    @Override
    protected String getSpecificStyleClass() {
        return "zo-boolean";
    }
}

/**
 *
 */
package de.his.zofar.presentation.surveyengine.ui.components.question;

import javax.faces.component.FacesComponent;

/**
 * container component for single choice questions.
 *
 * @author le
 *
 */
@FacesComponent("org.zofar.SingleChoiceQuestion")
public class UISingleChoiceQuestion extends UIQuestion {

	/*
     * (non-Javadoc)
     *
     * @see
     * de.his.zofar.presentation.surveyengine.ui.components.question.UIQuestion
     * #getSpecificStyleClass()
     */
    @Override
    protected String getSpecificStyleClass() {
        return "zo-singlechoice";
    }
}

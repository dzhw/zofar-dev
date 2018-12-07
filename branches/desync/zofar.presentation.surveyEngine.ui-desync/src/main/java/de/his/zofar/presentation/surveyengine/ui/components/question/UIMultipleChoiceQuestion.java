/**
 *
 */
package de.his.zofar.presentation.surveyengine.ui.components.question;

import javax.faces.component.FacesComponent;

/**
 * @author le
 *
 */
@FacesComponent("org.zofar.MultipleChoiceQuestion")
public class UIMultipleChoiceQuestion extends UIQuestion {

	/* (non-Javadoc)
     * @see de.his.zofar.presentation.surveyengine.ui.components.question.UIQuestion#getSpecificStyleClass()
     */
    @Override
    protected String getSpecificStyleClass() {
        return "zo-multiplechoice";
    }
}

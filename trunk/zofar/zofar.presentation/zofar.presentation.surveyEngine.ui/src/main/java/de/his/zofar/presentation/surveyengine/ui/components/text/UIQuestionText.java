/**
 *
 */
package de.his.zofar.presentation.surveyengine.ui.components.text;

import javax.faces.component.FacesComponent;

/**
 * @author le
 *
 */
@FacesComponent("org.zofar.text.Question")
public class UIQuestionText extends UIText {

	/*
     * (non-Javadoc)
     * 
     * @see de.his.zofar.presentation.surveyengine.ui.components.text.UIText#
     * getStyleClass()
     */
    @Override
    protected String getSpecificStyleClass() {
        return "zo-text-question";
    }

}

/**
 *
 */
package de.his.zofar.presentation.surveyengine.ui.components.text;

import javax.faces.component.FacesComponent;

/**
 * @author le
 *
 */
@FacesComponent("org.zofar.text.Intro")
public class UIIntroductionText extends UIText {

	/*
     * (non-Javadoc)
     *
     * @see de.his.zofar.presentation.surveyengine.ui.components.text.UIText#
     * getSpecificStyleClass()
     */
    @Override
    protected String getSpecificStyleClass() {
        return "zo-text-introduction";
    }

}

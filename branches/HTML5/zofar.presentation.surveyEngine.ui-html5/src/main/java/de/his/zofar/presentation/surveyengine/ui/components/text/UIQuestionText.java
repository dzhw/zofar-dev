/**
 *
 */
package de.his.zofar.presentation.surveyengine.ui.components.text;

import javax.faces.component.FacesComponent;

import de.his.zofar.presentation.surveyengine.ui.renderer.text.ZofarQuestionTextRenderer;

/**
 * @author le
 *
 */
@FacesComponent("org.zofar.text.Question")
public class UIQuestionText extends UIText {

	public UIQuestionText() {
		super();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see javax.faces.component.UIComponentBase#getRendererType()
	 */
	@Override
	public String getRendererType() {
		return ZofarQuestionTextRenderer.RENDERER_TYPE;
	}

}

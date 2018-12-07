/**
 *
 */
package de.his.zofar.presentation.surveyengine.ui.components.text;

import javax.faces.component.FacesComponent;

import de.his.zofar.presentation.surveyengine.ui.renderer.text.ZofarIntroductionTextRenderer;

/**
 * @author le
 *
 */
@FacesComponent("org.zofar.text.Intro")
public class UIIntroductionText extends UIText {
	
	public UIIntroductionText() {
		super();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see javax.faces.component.UIComponentBase#getRendererType()
	 */
	@Override
	public String getRendererType() {
		return ZofarIntroductionTextRenderer.RENDERER_TYPE;
	}

}

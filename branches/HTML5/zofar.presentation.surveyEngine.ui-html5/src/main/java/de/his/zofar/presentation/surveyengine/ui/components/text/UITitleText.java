/**
 *
 */
package de.his.zofar.presentation.surveyengine.ui.components.text;

import javax.faces.component.FacesComponent;

import de.his.zofar.presentation.surveyengine.ui.renderer.text.ZofarTitleTextRenderer;

/**
 * @author le
 *
 */
@FacesComponent("org.zofar.text.Title")
public class UITitleText extends UIText {
	public UITitleText() {
		super();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see javax.faces.component.UIComponentBase#getRendererType()
	 */
	@Override
	public String getRendererType() {
		return ZofarTitleTextRenderer.RENDERER_TYPE;
	}
}

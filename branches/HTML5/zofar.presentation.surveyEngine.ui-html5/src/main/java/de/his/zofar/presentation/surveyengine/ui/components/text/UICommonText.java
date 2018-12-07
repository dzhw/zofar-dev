/**
 *
 */
package de.his.zofar.presentation.surveyengine.ui.components.text;

import javax.faces.component.FacesComponent;

import de.his.zofar.presentation.surveyengine.ui.renderer.text.ZofarCommonTextRenderer;

/**
 * @author le
 *
 */
@FacesComponent("org.zofar.text.Common")
public class UICommonText extends UIText {

	public UICommonText() {
		super();
	}
	
	/*
	 * (non-Javadoc)
	 *
	 * @see javax.faces.component.UIComponentBase#getRendererType()
	 */
	@Override
	public String getRendererType() {
		return ZofarCommonTextRenderer.RENDERER_TYPE;
	}
}

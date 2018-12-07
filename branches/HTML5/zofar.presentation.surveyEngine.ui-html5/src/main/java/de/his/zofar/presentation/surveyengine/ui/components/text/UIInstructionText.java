/**
 *
 */
package de.his.zofar.presentation.surveyengine.ui.components.text;

import javax.faces.component.FacesComponent;

import de.his.zofar.presentation.surveyengine.ui.renderer.text.ZofarInstructionTextRenderer;

/**
 * @author le
 *
 */
@FacesComponent("org.zofar.text.Instruction")
public class UIInstructionText extends UIText {
	
	public UIInstructionText() {
		super();
	}
	
	/*
	 * (non-Javadoc)
	 *
	 * @see javax.faces.component.UIComponentBase#getRendererType()
	 */
	@Override
	public String getRendererType() {
		return ZofarInstructionTextRenderer.RENDERER_TYPE;
	}

}

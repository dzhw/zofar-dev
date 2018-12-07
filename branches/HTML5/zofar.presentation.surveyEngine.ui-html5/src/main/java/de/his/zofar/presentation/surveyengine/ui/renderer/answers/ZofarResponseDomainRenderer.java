/**
 *
 */
package de.his.zofar.presentation.surveyengine.ui.renderer.answers;

import javax.faces.render.Renderer;

/**
 * @author le
 *
 */
public abstract class ZofarResponseDomainRenderer extends Renderer {


	public static final String LABEL_POSITION_LEFT = "left";
	public static final String LABEL_POSITION_RIGHT = "right";
	public static final String LABEL_POSITION_TOP = "top";
	public static final String LABEL_POSITION_BOTTOM = "bottom";

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.faces.render.Renderer#getRendersChildren()
	 */
	@Override
	public boolean getRendersChildren() {
		return true;
	}

}

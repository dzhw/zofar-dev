/**
 *
 */
package de.his.zofar.presentation.surveyengine.ui.components.question;

import javax.faces.component.UINamingContainer;

import de.his.zofar.presentation.surveyengine.ui.components.common.Identificational;
import de.his.zofar.presentation.surveyengine.ui.components.common.Visible;
import de.his.zofar.presentation.surveyengine.ui.renderer.ZofarQuestionRenderer;

/**
 * base component for all questions in zofar. this also includes matrices and
 * other composites.
 *
 * @author le
 *
 */
public abstract class UIQuestion extends UINamingContainer implements Identificational, Visible {

	public static final String COMPONENT_FAMILY = "org.zofar.Question";

	public UIQuestion() {
		super();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see javax.faces.component.UIComponentBase#getRendererType()
	 */
	@Override
	public String getRendererType() {
		return ZofarQuestionRenderer.RENDERER_TYPE;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see javax.faces.component.UINamingContainer#getFamily()
	 */
	@Override
	public String getFamily() {
		return COMPONENT_FAMILY;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see javax.faces.component.UIComponentBase#isTransient()
	 */
	@Override
	public boolean isTransient() {
		return true;
	}

}

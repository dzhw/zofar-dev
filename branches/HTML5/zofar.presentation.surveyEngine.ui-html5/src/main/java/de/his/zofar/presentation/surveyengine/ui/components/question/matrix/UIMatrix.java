package de.his.zofar.presentation.surveyengine.ui.components.question.matrix;

import de.his.zofar.presentation.surveyengine.ui.components.question.UIQuestion;

public abstract class UIMatrix extends UIQuestion {
	
	public static final String COMPONENT_FAMILY = "org.zofar.Matrix";

	public UIMatrix() {
		super();
	}
	
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

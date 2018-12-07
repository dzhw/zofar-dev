/**
 *
 */
package de.his.zofar.presentation.surveyengine.ui.components.question.composite.doublematrix;

import javax.faces.component.FacesComponent;

import de.his.zofar.presentation.surveyengine.ui.components.question.UIQuestion;

/**
 * @author le
 *
 */
@FacesComponent(value = "org.zofar.DoubleMatrix")
public class UIDoubleMatrix  extends UIQuestion {
	
	public UIDoubleMatrix() {
		super();
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

/**
 *
 */
package de.his.zofar.presentation.surveyengine.ui.components.question.matrix.singlechoice;

import javax.faces.component.FacesComponent;

import de.his.zofar.presentation.surveyengine.ui.components.common.Identificational;
import de.his.zofar.presentation.surveyengine.ui.components.common.Visible;
import de.his.zofar.presentation.surveyengine.ui.components.question.matrix.UIMatrix;
import de.his.zofar.presentation.surveyengine.ui.renderer.matrix.singlechoice.ZofarSingleChoiceMatrixRenderer;

/**
 * @author le
 *
 */

@FacesComponent("org.zofar.SingleChoiceMatrix")
public class UISingleChoiceMatrix extends UIMatrix implements Identificational, Visible {

	public static final String COMPONENT_FAMILY = "org.zofar.SingleChoiceMatrix";

	public UISingleChoiceMatrix() {
		super();
	}

	@Override
	public String getRendererType() {
		return ZofarSingleChoiceMatrixRenderer.RENDERER_TYPE;
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

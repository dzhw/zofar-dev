/**
 *
 */
package de.his.zofar.presentation.surveyengine.ui.components.question.matrix.multiplechoice;

import javax.faces.component.FacesComponent;

import de.his.zofar.presentation.surveyengine.ui.components.common.Identificational;
import de.his.zofar.presentation.surveyengine.ui.components.common.Visible;
import de.his.zofar.presentation.surveyengine.ui.components.question.matrix.UIMatrix;
import de.his.zofar.presentation.surveyengine.ui.renderer.matrix.multiplechoice.ZofarMultipleChoiceMatrixRenderer;

/**
 * @author le
 *
 */

@FacesComponent("org.zofar.MultipleChoiceComposite")
public class UIMultipleChoiceComposite extends UIMatrix implements Identificational, Visible {

	public static final String COMPONENT_FAMILY = "org.zofar.MultipleChoiceComposite";

	public UIMultipleChoiceComposite() {
		super();
	}

	@Override
	public String getRendererType() {
		return ZofarMultipleChoiceMatrixRenderer.RENDERER_TYPE;
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

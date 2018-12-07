/**
 *
 */
package de.his.zofar.presentation.surveyengine.ui.components.question.matrix.multiplechoice;

import javax.faces.component.FacesComponent;

import de.his.zofar.presentation.surveyengine.ui.components.common.Identificational;
import de.his.zofar.presentation.surveyengine.ui.components.question.matrix.UIMatrixResponseDomain;
import de.his.zofar.presentation.surveyengine.ui.renderer.answers.matrix.multiplechoice.responsedomain.ZofarMultipleChoiceMatrixResponseDomainRenderer;

/**
 *
 * @author le
 *
 */
@FacesComponent("org.zofar.MultipleChoiceCompositeResponseDomain")
public class UIMultipleChoiceCompositeResponseDomain extends UIMatrixResponseDomain implements Identificational {

	public UIMultipleChoiceCompositeResponseDomain() {
		super();
	}

	@Override
	public String getRendererType() {
		return ZofarMultipleChoiceMatrixResponseDomainRenderer.RENDERER_TYPE;
	}
	
	public Boolean isShowValues() {
		Boolean isShowValues = false;
		if (this.getAttributes().get("isShowValues") != null) {
			isShowValues = (Boolean) this.getAttributes().get("isShowValues");
		}
		return isShowValues;
	}
}

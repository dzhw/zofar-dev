/**
 *
 */
package de.his.zofar.presentation.surveyengine.ui.components.question.matrix.multiplechoice;

import javax.faces.component.FacesComponent;

import de.his.zofar.presentation.surveyengine.ui.components.question.matrix.UIMatrixItem;
import de.his.zofar.presentation.surveyengine.ui.renderer.answers.matrix.multiplechoice.options.ZofarMultipleChoiceMatrixItemRenderer;

/**
 * @author le
 *
 */
@FacesComponent("org.zofar.MultipleChoiceCompositeItem")
public class UIMultipleChoiceCompositeItem extends UIMatrixItem{

	public UIMultipleChoiceCompositeItem() {
		super();
	}

	@Override
	public String getRendererType() {
		return ZofarMultipleChoiceMatrixItemRenderer.RENDERER_TYPE;
	}
	
	@Override
	public boolean isTransient() {
		return true;
	}
}

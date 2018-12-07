/**
 *
 */
package de.his.zofar.presentation.surveyengine.ui.components.question.matrix.singlechoice;

import javax.faces.component.FacesComponent;

import de.his.zofar.presentation.surveyengine.ui.components.question.matrix.UIMatrixItem;
import de.his.zofar.presentation.surveyengine.ui.renderer.answers.matrix.singlechoice.options.ZofarSingleChoiceMatrixItemRenderer;

/**
 * @author le
 *
 */
@FacesComponent("org.zofar.SingleChoiceMatrixItem")
public class UISingleChoiceMatrixItem extends UIMatrixItem{

	public UISingleChoiceMatrixItem() {
		super();
	}

	@Override
	public String getRendererType() {
		return ZofarSingleChoiceMatrixItemRenderer.RENDERER_TYPE;
	}

	@Override
	public boolean isTransient() {
		return true;
	}
}

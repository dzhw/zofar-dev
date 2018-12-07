/**
 *
 */
package de.his.zofar.presentation.surveyengine.ui.components.question.matrix.open;

import javax.faces.component.FacesComponent;

import de.his.zofar.presentation.surveyengine.ui.components.question.matrix.UIMatrixItem;
import de.his.zofar.presentation.surveyengine.ui.renderer.answers.matrix.open.options.ZofarOpenMatrixItemRenderer;

/**
 * @author meisner
 *
 */
@FacesComponent("org.zofar.OpenMatrixItem")
public class UIOpenMatrixItem extends UIMatrixItem {

	public UIOpenMatrixItem() {
		super();
	}

	@Override
	public String getRendererType() {
		return ZofarOpenMatrixItemRenderer.RENDERER_TYPE;
	}
}

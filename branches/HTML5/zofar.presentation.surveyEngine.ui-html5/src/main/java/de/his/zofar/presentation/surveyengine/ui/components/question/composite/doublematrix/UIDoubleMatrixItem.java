/**
 *
 */
package de.his.zofar.presentation.surveyengine.ui.components.question.composite.doublematrix;

import javax.faces.component.FacesComponent;

import de.his.zofar.presentation.surveyengine.ui.components.question.matrix.UIMatrixItem;
import de.his.zofar.presentation.surveyengine.ui.renderer.matrix.doubleMatrix.ZofarDoubleMatrixItemRenderer;

/**
 * @author meisner
 *
 */
@FacesComponent(value = "org.zofar.DoubleMatrixItem")
public class UIDoubleMatrixItem extends UIMatrixItem {

	public static final String COMPONENT_FAMILY = "org.zofar.DoubleMatrixItem";

	/**
	 *
	 */
	public UIDoubleMatrixItem() {
		super();
	}

	@Override
	public String getFamily() {
		return COMPONENT_FAMILY;
	}

	@Override
	public String getRendererType() {
		return ZofarDoubleMatrixItemRenderer.RENDERER_TYPE;
	}
}

package de.his.zofar.presentation.surveyengine.ui.components.question.composite.comparison;

import javax.faces.component.FacesComponent;

import de.his.zofar.presentation.surveyengine.ui.components.common.Identificational;
import de.his.zofar.presentation.surveyengine.ui.components.common.Visible;
import de.his.zofar.presentation.surveyengine.ui.components.question.matrix.UIMatrixItem;
import de.his.zofar.presentation.surveyengine.ui.renderer.comparison.ZofarComparisonItemRenderer;

/**
 * @author meisner
 *
 */
@FacesComponent(value = "org.zofar.ComparisonItem")
public class UIComparisonItem extends UIMatrixItem implements Identificational, Visible {

	public static final String COMPONENT_FAMILY = "org.zofar.ComparisonItem";

	public UIComparisonItem() {
		super();
	}

	@Override
	public String getFamily() {
		return COMPONENT_FAMILY;
	}

	@Override
	public String getRendererType() {
		return ZofarComparisonItemRenderer.RENDERER_TYPE;
	}
}

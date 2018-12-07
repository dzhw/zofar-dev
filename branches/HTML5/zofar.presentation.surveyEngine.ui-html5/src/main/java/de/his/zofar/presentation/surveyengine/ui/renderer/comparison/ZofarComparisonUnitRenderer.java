package de.his.zofar.presentation.surveyengine.ui.renderer.comparison;

import javax.faces.render.FacesRenderer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.his.zofar.presentation.surveyengine.ui.components.question.composite.comparison.UIComparisonUnit;
import de.his.zofar.presentation.surveyengine.ui.renderer.matrix.ZofarMatrixUnitRenderer;

/**
 * @author meisner
 *
 */
@FacesRenderer(componentFamily = UIComparisonUnit.COMPONENT_FAMILY, rendererType = ZofarComparisonUnitRenderer.RENDERER_TYPE)
public class ZofarComparisonUnitRenderer extends ZofarMatrixUnitRenderer {

	private static final Logger LOGGER = LoggerFactory.getLogger(ZofarComparisonUnitRenderer.class);

	public static final String RENDERER_TYPE = "org.zofar.ComparisonUnit";

	public ZofarComparisonUnitRenderer() {
		super();
	}

}

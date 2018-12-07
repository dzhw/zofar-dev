package de.his.zofar.presentation.surveyengine.ui.components.question.composite.comparison;

import javax.faces.component.FacesComponent;
import javax.faces.component.UINamingContainer;

import de.his.zofar.presentation.surveyengine.ui.components.common.Identificational;
import de.his.zofar.presentation.surveyengine.ui.components.common.Visible;
import de.his.zofar.presentation.surveyengine.ui.renderer.comparison.ZofarComparisonRenderer;

/**
 * @author meisner
 *
 */
@FacesComponent(value = "org.zofar.Comparison")
public class UIComparison extends UINamingContainer implements Identificational, Visible {

	public static final String COMPONENT_FAMILY = "org.zofar.Comparison";

	public UIComparison() {
		super();
	}

	@Override
	public String getFamily() {
		return COMPONENT_FAMILY;
	}

	@Override
	public String getRendererType() {
		return ZofarComparisonRenderer.RENDERER_TYPE;
	}
}

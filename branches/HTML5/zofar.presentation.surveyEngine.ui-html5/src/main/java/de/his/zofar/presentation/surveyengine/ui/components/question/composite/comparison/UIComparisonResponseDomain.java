package de.his.zofar.presentation.surveyengine.ui.components.question.composite.comparison;

import javax.faces.component.FacesComponent;

import de.his.zofar.presentation.surveyengine.ui.components.common.Identificational;
import de.his.zofar.presentation.surveyengine.ui.components.tablebase.AbstractTableResponseDomain;
import de.his.zofar.presentation.surveyengine.ui.renderer.comparison.ZofarComparisonResponseDomainRenderer;

/**
 * @author meisner
 *
 */
@FacesComponent(value = "org.zofar.ComparisonResponseDomain")
public class UIComparisonResponseDomain extends AbstractTableResponseDomain implements Identificational {

	public static final String COMPONENT_FAMILY = "org.zofar.ComparisonResponseDomain";

	public UIComparisonResponseDomain() {
		super();
	}

	@Override
	public String getFamily() {
		return COMPONENT_FAMILY;
	}

	@Override
	public String getRendererType() {
		return ZofarComparisonResponseDomainRenderer.RENDERER_TYPE;
	}
}

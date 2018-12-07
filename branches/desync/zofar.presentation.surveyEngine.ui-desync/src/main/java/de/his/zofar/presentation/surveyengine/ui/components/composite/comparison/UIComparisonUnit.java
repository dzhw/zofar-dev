package de.his.zofar.presentation.surveyengine.ui.components.composite.comparison;

import javax.faces.component.FacesComponent;

import de.his.zofar.presentation.surveyengine.ui.components.common.Identificational;
import de.his.zofar.presentation.surveyengine.ui.components.common.Visible;
import de.his.zofar.presentation.surveyengine.ui.components.tablebase.AbstractTableResponseDomainUnit;
import de.his.zofar.presentation.surveyengine.ui.renderer.comparison.ZofarComparisonUnitRenderer;
/**
 * @author meisner
 *
 */
@FacesComponent(value = "org.zofar.ComparisonUnit")
public class UIComparisonUnit extends AbstractTableResponseDomainUnit implements
		Identificational, Visible {
	
	public static final String COMPONENT_FAMILY = "org.zofar.ComparisonUnit";

	public UIComparisonUnit() {
		super();
	}
	
	@Override
	public String getFamily() {
		return COMPONENT_FAMILY;
	}

	@Override
	public String getRendererType() {
		return ZofarComparisonUnitRenderer.RENDERER_TYPE;
	}
	
	@Override
	@Deprecated
	public String getUID() {
		return this.getId();
	}

	@Override
	@Deprecated
	public Boolean visibleCondition() {
		return this.isRendered();
	}

}

package de.his.zofar.presentation.surveyengine.ui.components.composite.mixed;

import javax.faces.component.FacesComponent;

import de.his.zofar.presentation.surveyengine.ui.components.common.Identificational;
import de.his.zofar.presentation.surveyengine.ui.components.common.Visible;
import de.his.zofar.presentation.surveyengine.ui.components.tablebase.AbstractTableItem;
import de.his.zofar.presentation.surveyengine.ui.renderer.mixed.ZofarMixedMatrixItemRenderer;

/**
 * @author meisner
 *
 */
@FacesComponent("org.zofar.MixedMatrixItem")
public class UIMixedMatrixItem extends AbstractTableItem implements Identificational,Visible {
	
	public static final String COMPONENT_FAMILY = "org.zofar.MixedMatrixItem";

	public UIMixedMatrixItem() {
		super();
	}

	@Override
	public String getFamily() {
		return COMPONENT_FAMILY;
	}

	@Override
	public String getRendererType() {
		return ZofarMixedMatrixItemRenderer.RENDERER_TYPE;
	}

}

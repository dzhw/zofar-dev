package de.his.zofar.presentation.surveyengine.ui.components.composite.mixed;

import javax.faces.component.FacesComponent;
import javax.faces.component.UINamingContainer;

import de.his.zofar.presentation.surveyengine.ui.components.common.Identificational;
import de.his.zofar.presentation.surveyengine.ui.components.common.Visible;
import de.his.zofar.presentation.surveyengine.ui.renderer.mixed.ZofarMixedMatrixRenderer;
import de.his.zofar.presentation.surveyengine.ui.renderer.mixed.ZofarMixedMatrixResponseDomainRenderer;

/**
 * @author meisner
 *
 */
@FacesComponent(value = "org.zofar.MixedMatrix")
public class UIMixedMatrix extends UINamingContainer implements Identificational,Visible {
	
	public static final String COMPONENT_FAMILY = "org.zofar.MixedMatrix";

	public UIMixedMatrix() {
		super();
	}
	
	@Override
	public String getFamily() {
		return COMPONENT_FAMILY;
	}

	@Override
	public String getRendererType() {
		return ZofarMixedMatrixRenderer.RENDERER_TYPE;
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

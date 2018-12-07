package de.his.zofar.presentation.surveyengine.ui.components.question.composite.mixed;

import javax.faces.component.FacesComponent;

import de.his.zofar.presentation.surveyengine.ui.components.common.Identificational;
import de.his.zofar.presentation.surveyengine.ui.components.tablebase.AbstractTableResponseDomain;
import de.his.zofar.presentation.surveyengine.ui.renderer.mixed.ZofarMixedMatrixResponseDomainRenderer;

/**
 *
 * @author meisner
 *
 */
@FacesComponent(value = "org.zofar.MixedMatrixResponseDomain")
public class UIMixedMatrixResponseDomain extends AbstractTableResponseDomain implements Identificational {

	public static final String COMPONENT_FAMILY = "org.zofar.MixedMatrixResponseDomain";

	public UIMixedMatrixResponseDomain() {
		super();
	}

	@Override
	public String getFamily() {
		return COMPONENT_FAMILY;
	}

	@Override
	public String getRendererType() {
		return ZofarMixedMatrixResponseDomainRenderer.RENDERER_TYPE;
	}
}

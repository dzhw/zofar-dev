package de.his.zofar.presentation.surveyengine.ui.components.composite.mixed;

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
public class UIMixedMatrixResponseDomain extends
AbstractTableResponseDomain implements Identificational {
	
	private enum PropertyKeys {
		itemClasses
	}
	
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

	@Override
	public void setItemClasses(String itemClasses) {
		getStateHelper().put(PropertyKeys.itemClasses, itemClasses);
	}

	@Override
	public String getItemClasses() {
		return (String) getStateHelper().eval(PropertyKeys.itemClasses);
	}

}

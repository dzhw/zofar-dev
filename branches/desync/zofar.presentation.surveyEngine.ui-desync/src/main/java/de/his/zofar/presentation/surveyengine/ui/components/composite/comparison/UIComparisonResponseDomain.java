package de.his.zofar.presentation.surveyengine.ui.components.composite.comparison;

import javax.faces.component.FacesComponent;

import de.his.zofar.presentation.surveyengine.ui.components.common.Identificational;
import de.his.zofar.presentation.surveyengine.ui.components.tablebase.AbstractTableResponseDomain;
import de.his.zofar.presentation.surveyengine.ui.renderer.comparison.ZofarComparisonResponseDomainRenderer;

/**
 * @author meisner
 * 
 */
@FacesComponent(value = "org.zofar.ComparisonResponseDomain")
public class UIComparisonResponseDomain extends AbstractTableResponseDomain
		implements Identificational {

	private enum PropertyKeys {
		itemClasses
	}

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

	@Override
	@Deprecated
	public String getUID() {
		return this.getId();
	}
	
//    private static final String ITEM_CLASSES = "zo-odd zo-even";
    private static final String ITEM_CLASSES = "";

	@Override
	public void setItemClasses(String itemClasses) {
		getStateHelper().put(PropertyKeys.itemClasses, itemClasses);
	}

	@Override
	public String getItemClasses() {
//		return (String) getStateHelper().eval(PropertyKeys.itemClasses);
		return ITEM_CLASSES;
	}
}

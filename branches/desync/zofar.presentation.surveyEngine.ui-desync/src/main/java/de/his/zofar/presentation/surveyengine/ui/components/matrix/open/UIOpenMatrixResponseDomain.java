/**
 *
 */
package de.his.zofar.presentation.surveyengine.ui.components.matrix.open;

import javax.faces.component.FacesComponent;

import de.his.zofar.presentation.surveyengine.ui.components.tablebase.AbstractTableResponseDomain;
import de.his.zofar.presentation.surveyengine.ui.renderer.answers.matrix.open.responsedomain.ZofarOpenMatrixResponseDomainRenderer;

/**
 * @author le
 *
 */
@FacesComponent("org.zofar.OpenMatrixResponseDomain")
public class UIOpenMatrixResponseDomain extends AbstractTableResponseDomain {
	
	private enum PropertyKeys {
		itemClasses
	}

	@Override
	public void setItemClasses(String itemClasses) {
		getStateHelper().put(PropertyKeys.itemClasses, itemClasses);
	}

	@Override
	public String getItemClasses() {
		return (String) getStateHelper().eval(PropertyKeys.itemClasses);
	}

    public UIOpenMatrixResponseDomain() {
        super();
    }
    
	@Override
	public String getRendererType() {
		return ZofarOpenMatrixResponseDomainRenderer.RENDERER_TYPE;
	}
    
	@Override
	@Deprecated
	public String getUID() {
		return this.getId();
	}

}

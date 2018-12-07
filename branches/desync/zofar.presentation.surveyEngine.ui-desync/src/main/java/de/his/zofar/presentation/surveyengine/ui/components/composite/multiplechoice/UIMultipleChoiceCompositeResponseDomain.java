/**
 *
 */
package de.his.zofar.presentation.surveyengine.ui.components.composite.multiplechoice;

import javax.faces.component.FacesComponent;

import de.his.zofar.presentation.surveyengine.ui.components.common.Identificational;
import de.his.zofar.presentation.surveyengine.ui.components.tablebase.AbstractTableResponseDomain;
import de.his.zofar.presentation.surveyengine.ui.renderer.answers.matrix.multiplechoice.responsedomain.ZofarMultipleChoiceMatrixResponseDomainRenderer;

/**
 *
 * @author le
 *
 */
@FacesComponent("org.zofar.MultipleChoiceCompositeResponseDomain")
public class UIMultipleChoiceCompositeResponseDomain extends
        AbstractTableResponseDomain implements Identificational {
	
	private enum PropertyKeys {
		itemClasses
	}

    public UIMultipleChoiceCompositeResponseDomain() {
        super();
    }
    
	@Override
	public String getRendererType() {
		return ZofarMultipleChoiceMatrixResponseDomainRenderer.RENDERER_TYPE;
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

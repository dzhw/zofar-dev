/**
 *
 */
package de.his.zofar.presentation.surveyengine.ui.components.composite.multiplechoice;

import javax.faces.component.FacesComponent;
import javax.faces.component.UINamingContainer;

import de.his.zofar.presentation.surveyengine.ui.components.common.Identificational;
import de.his.zofar.presentation.surveyengine.ui.components.common.Visible;
import de.his.zofar.presentation.surveyengine.ui.renderer.matrix.multiplechoice.ZofarMultipleChoiceMatrixRenderer;

/**
 * @author le
 *
 */
@FacesComponent("org.zofar.MultipleChoiceComposite")
public class UIMultipleChoiceComposite extends UINamingContainer implements Identificational,Visible {
	
	public static final String COMPONENT_FAMILY = "org.zofar.MultipleChoiceComposite";

    public UIMultipleChoiceComposite() {
        super();
    }
    
	@Override
	public String getFamily() {
		return COMPONENT_FAMILY;
	}

	@Override
	public String getRendererType() {
		return ZofarMultipleChoiceMatrixRenderer.RENDERER_TYPE;
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

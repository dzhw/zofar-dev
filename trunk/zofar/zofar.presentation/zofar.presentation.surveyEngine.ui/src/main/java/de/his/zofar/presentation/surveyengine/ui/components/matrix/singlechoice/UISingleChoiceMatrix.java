/**
 *
 */
package de.his.zofar.presentation.surveyengine.ui.components.matrix.singlechoice;

import javax.faces.component.FacesComponent;
import javax.faces.component.UINamingContainer;

import de.his.zofar.presentation.surveyengine.ui.components.common.Identificational;
import de.his.zofar.presentation.surveyengine.ui.components.common.Visible;
import de.his.zofar.presentation.surveyengine.ui.renderer.matrix.singlechoice.ZofarSingleChoiceMatrixRenderer;

/**
 * @author le
 *
 */

@FacesComponent("org.zofar.SingleChoiceMatrix")
public class UISingleChoiceMatrix extends UINamingContainer implements Identificational,Visible {

	public static final String COMPONENT_FAMILY = "org.zofar.SingleChoiceMatrix";
	
    public UISingleChoiceMatrix() {
        super();
    }
    
	@Override
	public String getFamily() {
		return COMPONENT_FAMILY;
	}

	@Override
	public String getRendererType() {
		return ZofarSingleChoiceMatrixRenderer.RENDERER_TYPE;
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

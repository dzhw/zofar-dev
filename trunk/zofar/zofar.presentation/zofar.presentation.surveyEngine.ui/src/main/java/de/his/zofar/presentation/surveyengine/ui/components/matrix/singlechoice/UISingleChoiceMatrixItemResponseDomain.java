/**
 *
 */
package de.his.zofar.presentation.surveyengine.ui.components.matrix.singlechoice;

import javax.faces.component.FacesComponent;
import javax.faces.component.NamingContainer;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;

import de.his.zofar.presentation.surveyengine.ui.components.common.Identificational;
import de.his.zofar.presentation.surveyengine.ui.components.tablebase.AbstractTableResponseDomain;
import de.his.zofar.presentation.surveyengine.ui.renderer.answers.matrix.singlechoice.responsedomain.ZofarDifferentialMatrixItemResponseDomainRenderer;
import de.his.zofar.presentation.surveyengine.ui.renderer.answers.matrix.singlechoice.responsedomain.ZofarSingleChoiceMatrixItemResponseDomainRenderer;

/**
 * this class represents all <input type="radio" /> AKA response domain of a
 * single choice matrix item.
 *
 * the class is self rendered. it copies the functionality of the default select
 * one radio radio AKA HtmlRadioRenderer to the encodeChildren() method.
 *
 * @author le
 *
 */
@FacesComponent("org.zofar.SingleChoiceMatrixItemResponseDomain")
public class UISingleChoiceMatrixItemResponseDomain extends UIInput implements
        Identificational,NamingContainer {

	public static final String COMPONENT_FAMILY = "org.zofar.SingleChoiceMatrixItemResponseDomain";

    public UISingleChoiceMatrixItemResponseDomain() {
        super();
    }
    
	@Override
	public String getFamily() {
		return COMPONENT_FAMILY;
	}

	@Override
	public String getRendererType() {
		if(isDifferential()){
			return ZofarDifferentialMatrixItemResponseDomainRenderer.RENDERER_TYPE;
		}
		else 
			return ZofarSingleChoiceMatrixItemResponseDomainRenderer.RENDERER_TYPE;
	}
    
	@Override
	@Deprecated
	public String getUID() {
		return this.getId();
	}

    
	private Boolean isDifferential() {
		Boolean differential = false;
		UIComponent parent = this;
		while((parent != null)&&!((AbstractTableResponseDomain.class).isAssignableFrom(parent.getClass())))parent = parent.getParent();
		
		if ((parent != null)&&(parent.getAttributes().get("isDifferential") != null)) {
			differential = (Boolean) parent
					.getAttributes().get("isDifferential");
		}
		
		return differential;
	}
    
}

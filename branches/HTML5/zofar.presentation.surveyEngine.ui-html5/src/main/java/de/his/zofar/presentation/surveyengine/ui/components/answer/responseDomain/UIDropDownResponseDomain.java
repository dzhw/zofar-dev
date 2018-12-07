/**
 *
 */
package de.his.zofar.presentation.surveyengine.ui.components.answer.responseDomain;

import javax.faces.component.FacesComponent;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;

import de.his.zofar.presentation.surveyengine.ui.components.common.Identificational;
import de.his.zofar.presentation.surveyengine.ui.components.question.matrix.singlechoice.UISingleChoiceMatrixItem;
import de.his.zofar.presentation.surveyengine.ui.interfaces.IResponseDomain;
import de.his.zofar.presentation.surveyengine.ui.interfaces.ISingleChoiceResponseDomain;
import de.his.zofar.presentation.surveyengine.ui.renderer.answers.matrix.singlechoice.responsedomain.ZofarDropDownMatrixResponseDomainRenderer;
import de.his.zofar.presentation.surveyengine.ui.renderer.answers.singechoice.responsedomain.ZofarDropDownResponseDomainRenderer;
import de.his.zofar.presentation.surveyengine.ui.util.JsfUtility;

/**
 * @author le
 *
 */
@FacesComponent("org.zofar.responsedomain.DropDown")
public class UIDropDownResponseDomain extends UIInput implements ISingleChoiceResponseDomain, Identificational {

	private enum PropertyKeys {
		showValues
	}

	private UIComponent parentResponseDomain;

	/*
	 * (non-Javadoc)
	 *
	 * @see javax.faces.component.UIInput#getFamily()
	 */
	@Override
	public String getFamily() {
		return IResponseDomain.COMPONENT_FAMILY;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see javax.faces.component.UIComponentBase#getRendererType()
	 */
	@Override
	public String getRendererType() {
		final UIComponent parent = JsfUtility.getInstance().getParentResponseDomain(this.getParent());
		System.out.println("parent rdc : "+parent);
		
		if((parent == null) || ((UISingleChoiceMatrixItem.class).isAssignableFrom(parent.getParent().getClass()))) {
			return ZofarDropDownMatrixResponseDomainRenderer.RENDERER_TYPE;
			
		} else {
			return ZofarDropDownResponseDomainRenderer.RENDERER_TYPE;
		}
		
//		if((parent == null) || ((UIDropDownMissingResponseDomain.class).isAssignableFrom(parent.getClass()))) {
//			return ZofarDropDownResponseDomainRenderer.RENDERER_TYPE;
//		} else {
//			return ZofarDropDownMatrixResponseDomainRenderer.RENDERER_TYPE;
//		}
		
	}

	public UIComponent getParentResponseDomain() {
		if (this.parentResponseDomain == null) {
			this.parentResponseDomain = JsfUtility.getInstance().getParentResponseDomain(this.getParent());
		}

		return this.parentResponseDomain;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see javax.faces.component.UIComponentBase#isTransient()
	 */
	@Override
	public boolean isTransient() {
		return true;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see de.his.zofar.presentation.surveyengine.ui.interfaces.
	 * ISingleChoiceResponseDomain#setShowValues(boolean)
	 */
	@Override
	public void setShowValues(final boolean showValues) {
		this.getStateHelper().put(PropertyKeys.showValues, showValues);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see de.his.zofar.presentation.surveyengine.ui.interfaces.
	 * ISingleChoiceResponseDomain#isShowValues()
	 */
	@Override
	public boolean isShowValues() {
		return Boolean.valueOf(this.getStateHelper().eval(PropertyKeys.showValues, Boolean.FALSE).toString());
	}
}

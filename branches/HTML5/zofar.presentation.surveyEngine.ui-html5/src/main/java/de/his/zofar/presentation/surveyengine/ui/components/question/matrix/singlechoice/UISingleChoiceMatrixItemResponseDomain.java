/**
 *
 */
package de.his.zofar.presentation.surveyengine.ui.components.question.matrix.singlechoice;

import javax.faces.component.FacesComponent;
import javax.faces.component.NamingContainer;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;

import de.his.zofar.presentation.surveyengine.ui.components.common.Identificational;
import de.his.zofar.presentation.surveyengine.ui.components.question.composite.doublematrix.UIDoubleMatrixResponseDomain;
import de.his.zofar.presentation.surveyengine.ui.interfaces.IMatrixItemResponceDomain;
import de.his.zofar.presentation.surveyengine.ui.renderer.answers.matrix.singlechoice.responsedomain.ZofarSingleChoiceMatrixItemResponseDomainRenderer;
import de.his.zofar.presentation.surveyengine.ui.renderer.matrix.doubleMatrix.ZofarDoubleMatrixItemResponseDomainRenderer;
import de.his.zofar.presentation.surveyengine.ui.util.JsfUtility;

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
public class UISingleChoiceMatrixItemResponseDomain extends UIInput implements Identificational, NamingContainer,IMatrixItemResponceDomain {

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
		final UIComponent parentRdc = JsfUtility.getInstance().getParentResponseDomain(this.getParent());
		
		String back = ZofarSingleChoiceMatrixItemResponseDomainRenderer.RENDERER_TYPE;
		
		if((parentRdc != null)&&(UIDoubleMatrixResponseDomain.class).isAssignableFrom(parentRdc.getClass())){
			back = ZofarDoubleMatrixItemResponseDomainRenderer.RENDERER_TYPE;
		}
		return back;
	}
}

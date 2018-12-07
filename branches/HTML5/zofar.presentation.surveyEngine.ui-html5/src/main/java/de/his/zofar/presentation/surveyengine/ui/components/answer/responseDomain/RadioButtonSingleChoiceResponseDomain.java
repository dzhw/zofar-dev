/**
 *
 */
package de.his.zofar.presentation.surveyengine.ui.components.answer.responseDomain;

import java.util.List;

import javax.faces.component.FacesComponent;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.his.zofar.presentation.surveyengine.ui.components.answer.options.SingleOption;
import de.his.zofar.presentation.surveyengine.ui.components.common.Alignable;
import de.his.zofar.presentation.surveyengine.ui.components.common.Identificational;
import de.his.zofar.presentation.surveyengine.ui.interfaces.ISequence;
import de.his.zofar.presentation.surveyengine.ui.interfaces.ISingleChoiceResponseDomain;
import de.his.zofar.presentation.surveyengine.ui.renderer.answers.singechoice.responsedomain.ZofarHorizontalRadioResponseDomainRenderer;
import de.his.zofar.presentation.surveyengine.ui.renderer.answers.singechoice.responsedomain.ZofarHorizontalUnitRadioResponseDomainRenderer;
import de.his.zofar.presentation.surveyengine.ui.renderer.answers.singechoice.responsedomain.ZofarVerticalRadioResponseDomainRenderer;
import de.his.zofar.presentation.surveyengine.ui.renderer.answers.singechoice.responsedomain.ZofarVerticalUnitRadioResponseDomainRenderer;
import de.his.zofar.presentation.surveyengine.ui.util.JsfUtility;

/**
 * Renders a panel for the answer options of a single choice question. Each
 * answer option will be rendered as a radio button.
 *
 * @author Reitmann
 */
@FacesComponent(value = "org.zofar.RadioButtonSingleChoiceResponseDomain")
public class RadioButtonSingleChoiceResponseDomain extends UIInput implements ISingleChoiceResponseDomain, Identificational, Alignable {

	private enum PropertyKeys {
		showValues, missingSeparated, horizontal, labelPosition, alignAttached, direction
	}

	public static final String COMPONENT_FAMILY = "org.zofar.RadioButtonSingleChoiceResponseDomain";

	@SuppressWarnings("unused")
	private static final Logger LOGGER = LoggerFactory.getLogger(RadioButtonSingleChoiceResponseDomain.class);

	public RadioButtonSingleChoiceResponseDomain() {
		super();
	}

	@Override
	public String getFamily() {
		return COMPONENT_FAMILY;
	}

	@Override
	public String getRendererType() {
		final Boolean isHorizontal = ((String) this.getAttributes().get("direction")).equals("horizontal");
		final boolean containsUnits = JsfUtility.getInstance().hasChildOfType(this, de.his.zofar.presentation.surveyengine.ui.components.container.Section.class);
		String rendererType = "UNKOWN";
		if (containsUnits) {
			if (isHorizontal) {
				rendererType = ZofarHorizontalUnitRadioResponseDomainRenderer.RENDERER_TYPE;
			} else {
				rendererType = ZofarVerticalUnitRadioResponseDomainRenderer.RENDERER_TYPE;
			}
		} else {
			if (isHorizontal) {
				rendererType = ZofarHorizontalRadioResponseDomainRenderer.RENDERER_TYPE;
			} else {
				rendererType = ZofarVerticalRadioResponseDomainRenderer.RENDERER_TYPE;
			}
		}
//		System.out.println("render type : "+rendererType);
		return rendererType;
	}

	@Override
	public void processDecodes(final FacesContext context) {
		super.processDecodes(context);
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

	@Override
	public boolean isShowValues() {
		return Boolean.valueOf(this.getStateHelper().eval(PropertyKeys.showValues, false).toString());
	}

	@Override
	public String getDirection() {
		return (String) this.getStateHelper().eval(PropertyKeys.direction);
	}

	@Override
	public void setDirection(final String direction) {
		this.getStateHelper().put(PropertyKeys.direction, direction);
	}

	/**
	 * We do not want to participate in state saving.
	 */
	@Override
	public boolean isTransient() {
		return true;
	}

	public void setLabelPosition(final String labelPosition) {
		this.getStateHelper().put(PropertyKeys.labelPosition, labelPosition);
	}

	public String getLabelPosition() {
		return (String) this.getStateHelper().eval(PropertyKeys.labelPosition);
	}

	public String labelPosition() {
		return this.getLabelPosition();
	}

	public void setMissingSeparated(final boolean missingSeparated) {
		this.getStateHelper().put(PropertyKeys.missingSeparated, missingSeparated);
	}

	public boolean isMissingSeparated() {
		return Boolean.valueOf(this.getStateHelper().eval(PropertyKeys.missingSeparated, true).toString());
	}

	@Deprecated
	public void setAlignAttached(final boolean alignAttached) {
		this.getStateHelper().put(PropertyKeys.alignAttached, alignAttached);
	}

	@Deprecated
	public boolean isAlignAttached() {
		return Boolean.valueOf(this.getStateHelper().eval(PropertyKeys.alignAttached, false).toString());
	}
	
	public void sequenceAnswerOptions(){
		List<UIComponent> visibleAnswerOptions = JsfUtility.getInstance().getVisibleChildrensOfType(this, SingleOption.class);
		if(visibleAnswerOptions != null){
			int index = 0;
			for(final UIComponent visibleAnswerOption : visibleAnswerOptions){
				if((ISequence.class).isAssignableFrom(visibleAnswerOption.getClass())){
					((ISequence)visibleAnswerOption).setSequenceId(index);
					index = index + 1;
				}
			}
		}
	}

}

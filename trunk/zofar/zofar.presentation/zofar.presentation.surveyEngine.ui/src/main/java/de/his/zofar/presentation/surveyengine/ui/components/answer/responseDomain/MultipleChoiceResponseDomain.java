/**
 *
 */
package de.his.zofar.presentation.surveyengine.ui.components.answer.responseDomain;

import javax.faces.component.FacesComponent;
import javax.faces.component.UIComponent;
import javax.faces.component.UIComponentBase;
import javax.faces.component.UIInput;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.his.zofar.presentation.surveyengine.ui.components.common.Alignable;
import de.his.zofar.presentation.surveyengine.ui.components.common.Identificational;
import de.his.zofar.presentation.surveyengine.ui.interfaces.IMultipleChoiceResponseDomain;
import de.his.zofar.presentation.surveyengine.ui.renderer.answers.multiplechoice.responsedomain.ZofarHorizontalCheckboxResponseDomainRenderer;
import de.his.zofar.presentation.surveyengine.ui.renderer.answers.multiplechoice.responsedomain.ZofarVerticalCheckboxResponseDomainRenderer;

/**
 * Renders a panel for the answer options of a single choice question. Each
 * answer option will be rendered as a radio button.
 *
 * @author dick
 */
@FacesComponent(value = "org.zofar.MultipleChoiceResponseDomain")
public class MultipleChoiceResponseDomain extends UIComponentBase implements
        IMultipleChoiceResponseDomain, Identificational, Alignable {

    private enum PropertyKeys {
        showValues, itemClasses, missingSeparated, horizontal, labelPosition, alignAttached, direction
    }
    public static final String CSS_CLASS_DELIM = ",";

    public static final String COMPONENT_FAMILY = "org.zofar.MultipleChoiceResponseDomain";

    @SuppressWarnings("unused")
    private static final Logger LOGGER = LoggerFactory
			.getLogger(MultipleChoiceResponseDomain.class);

	public MultipleChoiceResponseDomain() {
		super();
	}

	@Override
	public String getFamily() {
		return COMPONENT_FAMILY;
	}

	@Override
	public String getRendererType() {
		Boolean isHorizontal = ((String)getAttributes().get("direction")).equals("horizontal");
		if(isHorizontal == null)isHorizontal = false;
        if (isHorizontal) {
        	return ZofarHorizontalCheckboxResponseDomainRenderer.RENDERER_TYPE;
        }
        else return ZofarVerticalCheckboxResponseDomainRenderer.RENDERER_TYPE;
	}
	

    /*
     * (non-Javadoc)
     *
     * @see de.his.zofar.presentation.surveyengine.ui.interfaces.
     * ISingleChoiceResponseDomain#setShowValues(boolean)
     */
    @Override
    public void setShowValues(final boolean showValues) {
        getStateHelper().put(PropertyKeys.showValues, showValues);
    }

    @Override
    public boolean isShowValues() {
        return Boolean.valueOf(getStateHelper().eval(PropertyKeys.showValues,
                false).toString());
	}


	@Override
	public String getUID() {
		return this.getId();
	}
    @Override
    public Boolean isHorizontalAligned() {
    	return this.getDirection().equals("horizontal");
    }
    
    @Override
    public String getDirection() {
    	return (String) getStateHelper().eval(PropertyKeys.direction);
    }
    
    public void setDirection(final String direction) {
        getStateHelper().put(PropertyKeys.direction, direction);
    }

    public void setLabelPosition(final String labelPosition) {
        getStateHelper().put(PropertyKeys.labelPosition, labelPosition);
    }

    public String getLabelPosition() {
        return (String) getStateHelper().eval(PropertyKeys.labelPosition);
    }

	public String labelPosition() {
        return getLabelPosition();
	}

    public void setMissingSeparated(final boolean missingSeparated) {
        getStateHelper().put(PropertyKeys.missingSeparated, missingSeparated);
    }

    public boolean isMissingSeparated() {
        return Boolean.valueOf(getStateHelper().eval(
                PropertyKeys.missingSeparated, true).toString());
	}

    /*
     * (non-Javadoc)
     *
     * @see
     * de.his.zofar.presentation.surveyengine.ui.interfaces.IResponseDomain#
     * setItemClasses(java.lang.String)
     */
    @Override
    public void setItemClasses(final String itemClasses) {
        getStateHelper().put(PropertyKeys.itemClasses, itemClasses);
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * de.his.zofar.presentation.surveyengine.ui.interfaces.IResponseDomain#
     * getItemClasses()
     */
    @Override
    public String getItemClasses() {
        return (String) getStateHelper().eval(PropertyKeys.itemClasses);
    }

    public void setAlignAttached(final boolean alignAttached) {
        getStateHelper().put(PropertyKeys.alignAttached, alignAttached);
    }

    public boolean isAlignAttached() {
        return Boolean.valueOf(getStateHelper().eval(
                PropertyKeys.alignAttached, false).toString());
    }

}

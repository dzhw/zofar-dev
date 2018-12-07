/**
 *
 */
package de.his.zofar.presentation.surveyengine;

import java.util.Map;

import javax.faces.context.FacesContext;
import javax.inject.Inject;

import de.his.zofar.presentation.surveyengine.controller.SessionController;
import de.his.zofar.presentation.surveyengine.util.JsfUtility;

/**
 * an implementation of the abstract value type bean for boolean values.
 *
 * @author le
 *
 */
public class BooleanValueTypeBean extends AbstractLabeledAnswerBean {

    /**
     *
     */
    private static final long serialVersionUID = 6612632744119338984L;

    /**
     * the value which must be stored.
     */
    private Boolean value;

    private final String itemUid;

    /**
     * constructor delegate for the parent constructor.
     *
     * @param sessionController
     * @param variableName
     * @param itemUid
     */
    @Inject
    public BooleanValueTypeBean(final SessionController sessionController,
            final String variableName, final String itemUid) {
        super(sessionController, variableName);
        this.itemUid = itemUid;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * de.his.zofar.presentation.surveyengine.AbstractAnswerBean#getStringValue
     * ()
     */
    @Override
    public String getStringValue() {
        return String.valueOf(this.value);
    }

    /**
     * @return the value
     */
    public Boolean getValue() {
        return this.value;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * de.his.zofar.presentation.surveyengine.AbstractAnswerBean#setStringValue
     * (java.lang.String)
     */
    @Override
    public void setStringValue(final String stringValue) {
        this.value = Boolean.valueOf(stringValue);
        this.saveValue();
    }

    /**
     * @param value
     *            the value to set
     */
    public void setValue(final Boolean value) {
        this.value = value;
        this.saveValue();
    }

    @Override
    public Object toPlaceholder() {
        final String label = this.getLabel();
        if ((label == null) || (label.isEmpty())) {
        	String expressionStr = null;
            if (this.getValue()){
            	expressionStr = "#{surveyConstants['boolean.true']}";
            }
            else{
            	expressionStr = "#{surveyConstants['boolean.false']}";
            }
            if(expressionStr != null){
            	JsfUtility jsfUtility = JsfUtility.getInstance();
            	return jsfUtility.evaluateValueExpression(FacesContext.getCurrentInstance(), expressionStr, String.class);
            }
        }
        if (this.getValue()){
            return label;
        }
        else return this.getAlternative();
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * de.his.zofar.presentation.surveyengine.AbstractLabeledAnswerBean#loadLabels
     * ()
     */
    @Override
    protected Map<String, String> loadLabels() {
        return this.getSessionController().loadLabelMap(this.getVariableName(),
                this.itemUid);
    }

}

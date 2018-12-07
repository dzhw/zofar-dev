/**
 *
 */
package de.his.zofar.presentation.surveyengine;

import javax.inject.Inject;

import de.his.zofar.presentation.surveyengine.controller.SessionController;

/**
 * an implementation of the abstract value type bean for number values.
 *
 * @author le
 *
 */
public class NumberValueTypeBean extends AbstractAnswerBean {

    /**
     *
     */
    private static final long serialVersionUID = 49785145868298064L;

    /**
     * the value which must be stored.
     */
    private Double value;

    /**
     * constructor delegate for the parent constructor.
     * 
     * @param sessionController
     * @param variableName
     */
    @Inject
    public NumberValueTypeBean(final SessionController sessionController,
            final String variableName) {
        super(sessionController, variableName);
    }

    /**
     * @return the value
     */
    public Double getValue() {
        return value;
    }

    /**
     * @param value
     *            the value to set
     */
    public void setValue(final Double value) {
        this.value = value;
        saveValue();
    }

    /*
     * (non-Javadoc)
     *
     * @see de.his.zofar.surveyengine.AbstractValueTypeBean#toPlaceholer()
     */
    @Override
    public Object toPlaceholder() {
        return this.getValue();
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
    	if (stringValue == null || stringValue.isEmpty() || stringValue.equals("null")) {
    		this.value = null;
    	} else {
    		this.value = Double.valueOf(stringValue);
    	}
    	saveValue();
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
        return String.valueOf(value);
    }

}

/**
 *
 */
package de.his.zofar.presentation.surveyengine;

import javax.inject.Inject;

import de.his.zofar.presentation.surveyengine.controller.SessionController;
import de.his.zofar.presentation.surveyengine.util.StringUtility;

/**
 * implementation of the abstract value type bean for string values.
 *
 * @author le
 *
 */
public class StringValueTypeBean extends AbstractAnswerBean {
    /**
     *
     */
    private static final long serialVersionUID = -806135259939169619L;

    /**
     * the value which is stored.
     */
    private String value;

    /**
     * delegates to the parent constructor.
     *
     * @param sessionController
     * @param variableName
     */
    @Inject
    public StringValueTypeBean(final SessionController sessionController,
            final String variableName) {
        super(sessionController, variableName);
    }

    /**
     * @return the value
     */
    public String getValue() {
        return value;
    }

    /**
     * @param value
     *            the value to set
     */
    public void setValue(final String value) {
    	// Filter HTML
    	final StringUtility stringUtility = StringUtility.getInstance();
        this.value = stringUtility.escapeHtml(value);
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
//    	// Filter HTML
//    	final StringUtility stringUtility = StringUtility.getInstance();
//        this.value = stringUtility.escapeHtml(stringValue);
    	setValue(stringValue);
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
        return value;
    }
}

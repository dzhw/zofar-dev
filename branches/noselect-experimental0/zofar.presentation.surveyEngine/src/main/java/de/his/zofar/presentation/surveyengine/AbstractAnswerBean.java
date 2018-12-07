/**
 *
 */
package de.his.zofar.presentation.surveyengine;

import java.io.Serializable;

import javax.faces.context.FacesContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.his.zofar.presentation.surveyengine.controller.SessionController;
import de.his.zofar.presentation.surveyengine.ui.interfaces.IAnswerBean;
import de.his.zofar.presentation.surveyengine.util.JsfUtility;

/**
 * a spring bean (request scoped) for each variable that exists in the survey.
 * this bean holds the answer for a participant and a variable.
 *
 * the class is intended to be configured in the application context XML file.
 *
 * @author le
 *
 */
public abstract class AbstractAnswerBean implements Serializable, IAnswerBean {

    /**
     *
     */
    private static final long serialVersionUID = -9219299988276085352L;

    private static final Logger LOGGER = LoggerFactory
            .getLogger(AbstractAnswerBean.class);

    /**
     * the instance of the session controller that belongs to the current
     * participant.
     *
     * the session controller does the saving and reading from storage.
     */
    private final SessionController sessionController;

    /**
     * the variable name that the instance of the value type bean belongs to.
     */
    private final String variableName;

    /**
     * TODO reitmann change to localizable map
     */
    private Object alternative = "unknown";

    /**
     * Constructor based injection to ensure that every instance of this class
     * has a reference on a session controller.
     *
     * on instantiating of this class the instance sets the value from the
     * storage (session controller).
     *
     * @param sessionController
     *            the session controller which saves the value or reads value
     *            from storage.
     * @param variableName
     *            the variable for which the instance holds the value for.
     */
    public AbstractAnswerBean(final SessionController sessionController,
            final String variableName) {
        super();
        this.sessionController = sessionController;
        this.variableName = variableName;
        this.setStringValue(sessionController
                .getValueForVariablename(variableName));
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (this.getClass() != obj.getClass()) {
            return false;
        }
        final AbstractAnswerBean other = (AbstractAnswerBean) obj;
        if (this.variableName == null) {
            if (other.variableName != null) {
                return false;
            }
        } else if (!this.variableName.equals(other.variableName)) {
            return false;
        }
        return true;
    }

    /**
     * @return the alternative
     */
    // protected Object getAlternative() {
    // if (this.alternative == null) {
    // this.alternative = "unkown";
    // }
    // return this.alternative;
    // }

    public final Object getAlternative() {
        if (this.alternative == null) {
            this.alternative = "unkown";
        }
        return JsfUtility.getInstance().evaluateValueExpression(
                FacesContext.getCurrentInstance(),
                "#{" + this.alternative + "}", String.class);
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * de.his.zofar.presentation.surveyengine.ui.IAnswerBean#getStringValue()
     */
    // @Override
    @Override
    public abstract String getStringValue();

    /*
     * (non-Javadoc)
     *
     * @see
     * de.his.zofar.presentation.surveyengine.ui.IAnswerBean#getVariableName()
     */
    // @Override
    @Override
    public final String getVariableName() {
        return this.variableName;
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime
                * result
                + ((this.variableName == null) ? 0 : this.variableName
                        .hashCode());
        return result;
    }

    /**
     * this method must called in the setter of the value that is intended to be
     * stored.
     */
    protected final void saveValue() {
        this.sessionController.setValueForVariablename(this.variableName,
                this.getStringValue());
    }

    /**
     * @param alternative
     *            the alternative to set
     */
    public void setAlternative(final Object alternative) {
        this.alternative = alternative;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * de.his.zofar.presentation.surveyengine.ui.IAnswerBean#setStringValue(java
     * .lang.String)
     */
    // @Override
    @Override
    public abstract void setStringValue(String stringValue);

    public abstract Object toPlaceholder();

    @Override
    public String toString() {
        return this.toPlaceholder() + "";
    }

    /**
     * @return the sessionController
     */
    public SessionController getSessionController() {
        return this.sessionController;
    }

}

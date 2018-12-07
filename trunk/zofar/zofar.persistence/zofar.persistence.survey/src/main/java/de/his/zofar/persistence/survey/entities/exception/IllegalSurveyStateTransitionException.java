/**
 * 
 */
package de.his.zofar.persistence.survey.entities.exception;

import de.his.zofar.persistence.survey.entities.SurveyState;

/**
 * @author Reitmann
 * 
 */
public class IllegalSurveyStateTransitionException extends RuntimeException {

    /**
     * 
     */
    private static final long serialVersionUID = -6956651557610685234L;

    /**
     * @param message
     */
    public IllegalSurveyStateTransitionException(final SurveyState oldState,
            final SurveyState newState) {
        super("Illegal state transition from "
                + (oldState != null ? oldState : "<null>") + " to "
                + (newState != null ? newState : "<null>"));
    }
}

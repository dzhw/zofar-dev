/**
 * 
 */
package de.his.zofar.persistence.survey.entities;

import java.util.Set;

import com.google.common.collect.Sets;

import de.his.zofar.persistence.survey.entities.exception.IllegalSurveyStateTransitionException;

/**
 * @author Reitmann
 * 
 */
public enum SurveyState {
    CANCELED, INFIELD(CANCELED), RELEASED(INFIELD), CREATED(CANCELED, INFIELD,
            RELEASED);

    private Set<SurveyState> successors;

    private SurveyState(final SurveyState... states) {
        this.successors = Sets.newHashSet(states);
    }

    public Set<SurveyState> getSuccessors() {
        return this.successors;
    }

    public boolean isValidSuccessor(final SurveyState newState) {
        if (this == newState || this.successors.contains(newState)) {
            return true;
        }
        throw new IllegalSurveyStateTransitionException(this, newState);
    }
}

/**
 *
 */
package de.his.zofar.service.survey.model;

import de.his.zofar.service.common.model.BaseDTO;

/**
 * @author le
 *
 */
public abstract class ParticipantValue extends BaseDTO {

    /**
     *
     */
    private static final long serialVersionUID = -5485618246393559599L;

    /**
     *
     */
    private Participant participant;

    /**
     *
     */
    private SurveyVariable variable;

    /**
     *
     */
    public ParticipantValue() {
        this(null, null);
    }

    /**
     * @param variable
     */
    public ParticipantValue(final SurveyVariable variable) {
        this(null, variable);
    }

    /**
     * @param participant
     * @param variable
     */
    public ParticipantValue(final Participant participant,
            final SurveyVariable variable) {
        super();
        this.participant = participant;
        this.variable = variable;
    }

    /**
     * @return the participant
     */
    public Participant getParticipant() {
        return participant;
    }

    /**
     * @param participant
     *            the participant to set
     */
    public void setParticipant(final Participant participant) {
        this.participant = participant;
    }

    /**
     * @return the variable
     */
    public SurveyVariable getVariable() {
        return variable;
    }

    /**
     * @param variable
     *            the variable to set
     */
    public void setVariable(final SurveyVariable variable) {
        this.variable = variable;
    }

}

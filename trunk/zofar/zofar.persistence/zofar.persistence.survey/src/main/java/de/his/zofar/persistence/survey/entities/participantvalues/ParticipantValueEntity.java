package de.his.zofar.persistence.survey.entities.participantvalues;

import static javax.persistence.CascadeType.PERSIST;

import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;

import de.his.zofar.persistence.common.entities.BaseEntity;
import de.his.zofar.persistence.survey.entities.ParticipantEntity;
import de.his.zofar.persistence.survey.entities.SurveyVariableEntity;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@SequenceGenerator(initialValue = 1, name = "primary_key_generator",
        sequenceName = "SEQ_PARTICIPANT_VALUE_ID")
public abstract class ParticipantValueEntity extends BaseEntity {

    @ManyToOne(optional = false, cascade = PERSIST)
    private ParticipantEntity participant;

    @ManyToOne(optional = false, cascade = PERSIST)
    private SurveyVariableEntity variable;

    public ParticipantValueEntity() {
        super();
    }

    public ParticipantValueEntity(final SurveyVariableEntity variable,
            final ParticipantEntity participant) {
        super();
        this.variable = variable;
        this.participant = participant;
    }

    public SurveyVariableEntity getVariable() {
        return this.variable;
    }

    public ParticipantEntity getParticipant() {
        return this.participant;
    }

    public abstract Object getValue();
}

package de.his.zofar.persistence.survey.entities.participantvalues;

import javax.persistence.Entity;

import de.his.zofar.persistence.survey.entities.ParticipantEntity;
import de.his.zofar.persistence.survey.entities.SurveyVariableEntity;

@Entity
public class ParticipantNumberValueEntity extends ParticipantValueEntity {

    private Long value;

    public ParticipantNumberValueEntity() {
        super();
    }

    public ParticipantNumberValueEntity(final SurveyVariableEntity variable,
            final ParticipantEntity participant, final Long value) {
        super(variable, participant);
        this.value = value;
    }

    @Override
    public Object getValue() {
        return this.value;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result
                + ((this.value == null) ? 0 : this.value.hashCode());
        return result;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (!super.equals(obj)) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ParticipantNumberValueEntity other = (ParticipantNumberValueEntity) obj;
        if (this.value == null) {
            if (other.value != null) {
                return false;
            }
        } else if (!this.value.equals(other.value)) {
            return false;
        }
        return true;
    }

}

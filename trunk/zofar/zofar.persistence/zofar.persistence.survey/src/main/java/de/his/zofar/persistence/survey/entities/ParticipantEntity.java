package de.his.zofar.persistence.survey.entities;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.MapKeyJoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;

import org.hibernate.validator.constraints.Email;

import de.his.zofar.persistence.common.entities.BaseEntity;
import de.his.zofar.persistence.survey.entities.participantvalues.ParticipantValueEntity;

@Entity
@SequenceGenerator(initialValue = 1, name = "primary_key_generator",
        sequenceName = "SEQ_PARTICIPANT_ID")
public class ParticipantEntity extends BaseEntity {

    @Email
    @Column(unique = false, nullable = false)
    private String eMail;

    @ManyToOne(optional = false, cascade = CascadeType.PERSIST)
    private SurveyEntity survey;

    @OneToMany(mappedBy = "participant", cascade = CascadeType.ALL)
    @MapKeyJoinColumn(name = "variable_id")
    private Map<SurveyVariableEntity, ParticipantValueEntity> variableValues;

    public void addVariableValue(final ParticipantValueEntity value) {
        if (this.variableValues == null) {
            this.variableValues = new HashMap<SurveyVariableEntity, ParticipantValueEntity>();
        }
        this.variableValues.put(value.getVariable(), value);
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
        final ParticipantEntity other = (ParticipantEntity) obj;
        if (this.eMail == null) {
            if (other.eMail != null) {
                return false;
            }
        } else if (!this.eMail.equals(other.eMail)) {
            return false;
        }
        return true;
    }

    public String getEMail() {
        return this.eMail;
    }

    public SurveyEntity getSurvey() {
        return this.survey;
    }

    public Map<SurveyVariableEntity, ParticipantValueEntity> getVariableValues() {
        return this.variableValues;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result
                + ((this.eMail == null) ? 0 : this.eMail.hashCode());
        return result;
    }

    public void setEMail(final String eMail) {
        this.eMail = eMail;
    }

    public void setSurvey(final SurveyEntity survey) {
        this.survey = survey;
    }

    public void setVariableValues(
            final Map<SurveyVariableEntity, ParticipantValueEntity> variableValues) {
        this.variableValues = variableValues;
    }

}

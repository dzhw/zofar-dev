package de.his.zofar.persistence.survey.entities;

import static javax.persistence.CascadeType.ALL;
import static javax.persistence.CascadeType.PERSIST;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.MapKeyJoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;

import de.his.zofar.persistence.question.entities.QuestionEntity;
import de.his.zofar.persistence.survey.entities.participantvalues.ParticipantValueEntity;
import de.his.zofar.persistence.valuetype.entities.VariableEntity;

@Entity
@SequenceGenerator(initialValue = 1, name = "primary_key_generator",
        sequenceName = "SEQ_SURVEY_VARIABLE_ID")
public class SurveyVariableEntity extends VariableEntity {
    @OneToMany(mappedBy = "variable", cascade = ALL)
    @MapKeyJoinColumn(name = "participant_id")
    private Map<ParticipantEntity, ParticipantValueEntity> values;

    @ManyToOne(optional = true, cascade = PERSIST)
    private QuestionEntity question;

    public Map<ParticipantEntity, ParticipantValueEntity> getValues() {
        return this.values;
    }

    public void setValues(final Map<ParticipantEntity, ParticipantValueEntity> values) {
        this.values = values;
    }

    public void addValue(final ParticipantValueEntity value) {
        if (this.values == null) {
            this.values = new HashMap<ParticipantEntity, ParticipantValueEntity>();
        }
        this.values.put(value.getParticipant(), value);
    }

    public QuestionEntity getQuestion() {
        return this.question;
    }

    public void setQuestion(final QuestionEntity question) {
        this.question = question;
    }
}

package de.his.zofar.persistence.question.entities.components;

import static javax.persistence.CascadeType.ALL;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;

import de.his.zofar.persistence.common.entities.BaseEntity;
import de.his.zofar.persistence.question.entities.concrete.OpenQuestionEntity;
import de.his.zofar.persistence.question.entities.concrete.SingleChoiceQuestionEntity;
import de.his.zofar.persistence.question.entities.questionvalues.QuestionValueEntity;

@Entity
@SequenceGenerator(initialValue = 1, name = "primary_key_generator",
        sequenceName = "SEQ_ANSWEROPTION_ID")
public class AnswerOptionEntity extends BaseEntity {

    private String displayText;

    @ManyToOne(cascade = ALL)
    private SingleChoiceQuestionEntity question;

    @OneToOne(cascade = ALL)
    private OpenQuestionEntity openQuestion;

    @OneToOne(cascade = CascadeType.PERSIST)
    private QuestionValueEntity value;

    public AnswerOptionEntity() {
        super();
    }

    public String getDisplayText() {
        return displayText;
    }

    public void setDisplayText(String displayText) {
        this.displayText = displayText;
    }

    public SingleChoiceQuestionEntity getQuestion() {
        return question;
    }

    public void setQuestion(SingleChoiceQuestionEntity question) {
        this.question = question;
    }

    public OpenQuestionEntity getOpenQuestion() {
        return openQuestion;
    }

    public void setOpenQuestion(OpenQuestionEntity open) {
        this.openQuestion = open;
    }

    /**
     * @return the value
     */
    public QuestionValueEntity getValue() {
        return value;
    }

    /**
     * @param value
     *            the value to set
     */
    public void setValue(QuestionValueEntity value) {
        this.value = value;
    }
}

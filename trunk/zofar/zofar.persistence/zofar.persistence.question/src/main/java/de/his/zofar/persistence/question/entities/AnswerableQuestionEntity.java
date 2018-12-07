package de.his.zofar.persistence.question.entities;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import de.his.zofar.persistence.valuetype.entities.VariableEntity;

@Entity
public abstract class AnswerableQuestionEntity extends MatrixQuestionComponentEntity {

    @ManyToOne(cascade = CascadeType.PERSIST)
    private VariableEntity variable;

    public AnswerableQuestionEntity() {
        super();
    }

    /**
     * @param questionText
     */
    public AnswerableQuestionEntity(String questionText) {
        super(questionText);
    }

    /**
     * @param questionText
     * @param variable
     */
    public AnswerableQuestionEntity(String questionText, VariableEntity variable) {
        super(questionText);
        this.variable = variable;
    }

    /**
     * @param introduction
     * @param questionText
     * @param instruction
     */
    public AnswerableQuestionEntity(String introduction, String questionText,
            String instruction) {
        super(introduction, questionText, instruction);
    }

    public VariableEntity getVariable() {
        return variable;
    }

    public void setVariable(VariableEntity variable) {
        this.variable = variable;
    }

}

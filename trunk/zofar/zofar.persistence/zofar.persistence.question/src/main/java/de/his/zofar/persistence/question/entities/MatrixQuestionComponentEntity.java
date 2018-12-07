package de.his.zofar.persistence.question.entities;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import de.his.zofar.persistence.question.entities.concrete.MatrixQuestionEntity;

@Entity
public abstract class MatrixQuestionComponentEntity extends QuestionEntity {
    @ManyToOne(cascade = CascadeType.PERSIST)
    private MatrixQuestionEntity parentMatrix;

    /**
     * 
     */
    public MatrixQuestionComponentEntity() {
        super();
    }

    /**
     * @param introduction
     * @param questionText
     * @param instruction
     */
    public MatrixQuestionComponentEntity(String introduction, String questionText,
            String instruction) {
        super(introduction, questionText, instruction);
    }

    /**
     * @param questionText
     */
    public MatrixQuestionComponentEntity(String questionText) {
        super(questionText);
    }

    /**
     * @return the parentMatrix
     */
    public MatrixQuestionEntity getParentMatrix() {
        return parentMatrix;
    }

    /**
     * @param parentMatrix
     *            the parentMatrix to set
     */
    public void setParentMatrix(MatrixQuestionEntity parentMatrix) {
        this.parentMatrix = parentMatrix;
    }
}

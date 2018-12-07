package de.his.zofar.service.question.model;

import de.his.zofar.service.question.model.concrete.MatrixQuestion;

//@DTOEntityMapping(entity = MatrixQuestionComponentEntity.class)
public abstract class MatrixQuestionComponent extends Question {
    /**
     *
     */
    private static final long serialVersionUID = -7868153817389514394L;

    /**
     *
     */
    private MatrixQuestion parentMatrix;

    /**
     *
     */
    public MatrixQuestionComponent() {
        super();
    }

    /**
     * @return the parentMatrix
     */
    public MatrixQuestion getParentMatrix() {
        return this.parentMatrix;
    }

    /**
     * @param parentMatrix
     *            the parentMatrix to set
     */
    public void setParentMatrix(final MatrixQuestion parentMatrix) {
        this.parentMatrix = parentMatrix;
    }
}

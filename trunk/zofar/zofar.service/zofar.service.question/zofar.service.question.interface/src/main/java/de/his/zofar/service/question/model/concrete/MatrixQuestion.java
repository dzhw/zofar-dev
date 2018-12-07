/**
 *
 */
package de.his.zofar.service.question.model.concrete;

import java.util.ArrayList;
import java.util.List;

import de.his.zofar.service.question.model.MatrixQuestionComponent;
import de.his.zofar.service.question.model.Question;

/**
 * @author le
 *
 */
// @DTOEntityMapping(entity = MatrixQuestionEntity.class)
public class MatrixQuestion extends Question {
    /**
     *
     */
    private static final long serialVersionUID = 3210258522686460216L;

    /**
     *
     */
    private List<MatrixQuestionComponent> matrixChildren;

    /**
     *
     */
    public MatrixQuestion() {
        super();
    }

    public void addChildQuestion(final MatrixQuestionComponent childQuestion) {
        if (this.matrixChildren == null) {
            this.matrixChildren = new ArrayList<MatrixQuestionComponent>();
        }
        childQuestion.setParentMatrix(this);
        this.matrixChildren.add(childQuestion);
    }

    /**
     * @return the matrixChildren
     */
    public List<MatrixQuestionComponent> getMatrixChildren() {
        return this.matrixChildren;
    }

    /**
     * @param matrixChildren
     *            the matrixChildren to set
     */
    public void setChildren(
            final List<MatrixQuestionComponent> matrixChildren) {
        this.matrixChildren = matrixChildren;
    }

}

/**
 * 
 */
package de.his.zofar.persistence.question.entities.concrete;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.OrderColumn;

import de.his.zofar.persistence.question.entities.MatrixQuestionComponentEntity;
import de.his.zofar.persistence.question.entities.QuestionEntity;

/**
 * @author le
 * 
 */
@Entity
public class MatrixQuestionEntity extends QuestionEntity {
    @OrderColumn
    @OneToMany(mappedBy = "parentMatrix", cascade = CascadeType.ALL)
    private List<MatrixQuestionComponentEntity> matrixChildren;

    /**
     * 
     */
    public MatrixQuestionEntity() {
        super();
    }

    /**
     * @param questionText
     */
    public MatrixQuestionEntity(String questionText) {
        super(questionText);
    }

    /**
     * @param questionText
     * @param matrixChildren
     */
    public MatrixQuestionEntity(String questionText,
            List<MatrixQuestionComponentEntity> matrixChildren) {
        super(questionText);
        this.matrixChildren = matrixChildren;
    }

    /**
     * @return the matrixChildren
     */
    public List<MatrixQuestionComponentEntity> getMatrixChildren() {
        return matrixChildren;
    }

    /**
     * @param matrixChildren
     *            the matrixChildren to set
     */
    public void setChildren(List<MatrixQuestionComponentEntity> matrixChildren) {
        this.matrixChildren = matrixChildren;
    }

    public void addChildQuestion(MatrixQuestionComponentEntity childQuestion) {
        if (this.matrixChildren == null) {
            this.matrixChildren = new ArrayList<MatrixQuestionComponentEntity>();
        }
        childQuestion.setParentMatrix(this);
        this.matrixChildren.add(childQuestion);
    }

}

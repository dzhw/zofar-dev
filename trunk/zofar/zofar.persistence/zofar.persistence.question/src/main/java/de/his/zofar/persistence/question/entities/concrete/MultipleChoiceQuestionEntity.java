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

/**
 * @author le
 * 
 */
@Entity
public class MultipleChoiceQuestionEntity extends MatrixQuestionComponentEntity {
    @OrderColumn
    @OneToMany(mappedBy = "parentMultipleChoiceQuestion",
            cascade = CascadeType.ALL)
    private List<SingleChoiceQuestionEntity> questions;

    /**
     * 
     */
    public MultipleChoiceQuestionEntity() {
        super();
    }

    /**
     * @return the questions
     */
    public List<SingleChoiceQuestionEntity> getQuestions() {
        return questions;
    }

    /**
     * @param questions
     *            the questions to set
     */
    public void setQuestions(List<SingleChoiceQuestionEntity> questions) {
        this.questions = questions;
    }

    public void addQuestion(SingleChoiceQuestionEntity question) {
        if (this.questions == null) {
            this.questions = new ArrayList<SingleChoiceQuestionEntity>();
        }
        // always save the back reference
        question.setParentMultipleChoiceQuestion(this);
        this.questions.add(question);
    }
}

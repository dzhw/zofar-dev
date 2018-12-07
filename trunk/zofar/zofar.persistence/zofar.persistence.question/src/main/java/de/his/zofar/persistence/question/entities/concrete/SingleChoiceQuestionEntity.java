package de.his.zofar.persistence.question.entities.concrete;

import static javax.persistence.CascadeType.ALL;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderColumn;

import de.his.zofar.persistence.question.entities.AnswerableQuestionEntity;
import de.his.zofar.persistence.question.entities.components.AnswerOptionEntity;

@Entity
public class SingleChoiceQuestionEntity extends AnswerableQuestionEntity {
    @OrderColumn
    @OneToMany(mappedBy = "question", cascade = ALL)
    private List<AnswerOptionEntity> answerOptions;

    @ManyToOne(cascade = CascadeType.PERSIST)
    private MultipleChoiceQuestionEntity parentMultipleChoiceQuestion;

    public SingleChoiceQuestionEntity() {
        super();
    }

    public List<AnswerOptionEntity> getAnswerOptions() {
        return answerOptions;
    }

    public void setAnswerOptions(List<AnswerOptionEntity> answerOptions) {
        this.answerOptions = answerOptions;
    }

    public void addAnswerOption(AnswerOptionEntity answerOption) {
        if (this.answerOptions == null) {
            this.answerOptions = new ArrayList<AnswerOptionEntity>();
        }
        answerOption.setQuestion(this);
        this.answerOptions.add(answerOption);
    }

    /**
     * @return the parentMultipleChoiceQuestion
     */
    public MultipleChoiceQuestionEntity getParentMultipleChoiceQuestion() {
        return parentMultipleChoiceQuestion;
    }

    /**
     * @param parentMultipleChoiceQuestion
     *            the parentMultipleChoiceQuestion to set
     */
    public void setParentMultipleChoiceQuestion(
            MultipleChoiceQuestionEntity parentMultipleChoiceQuestion) {
        this.parentMultipleChoiceQuestion = parentMultipleChoiceQuestion;
    }
}

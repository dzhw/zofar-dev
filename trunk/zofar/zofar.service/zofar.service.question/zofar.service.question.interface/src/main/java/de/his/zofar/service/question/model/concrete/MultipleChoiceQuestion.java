/**
 *
 */
package de.his.zofar.service.question.model.concrete;

import java.util.ArrayList;
import java.util.List;

import de.his.zofar.service.question.model.MatrixQuestionComponent;
import de.his.zofar.service.valuetype.model.BooleanValueType;

/**
 * @author le
 *
 */
// @DTOEntityMapping(entity = MultipleChoiceQuestionEntity.class)
public class MultipleChoiceQuestion extends MatrixQuestionComponent {
    /**
     *
     */
    private static final long serialVersionUID = -9070364954657678997L;

    private List<BooleanQuestion> questions;

    /**
     *
     */
    public MultipleChoiceQuestion() {
        super();
    }

    public void addQuestion(final BooleanQuestion question) {
        if (this.questions == null) {
            this.questions = new ArrayList<BooleanQuestion>();
        }

        // only BooleanValueType is allowed!
        if (!BooleanValueType.class.isAssignableFrom(question.getVariable()
                .getValueType().getClass())) {
            throw new IllegalArgumentException("value type must be boolean!");
        }

        // always save the back reference
        question.setParentMultipleChoiceQuestion(this);
        this.questions.add(question);
    }

    /**
     * @return the questions
     */
    public List<BooleanQuestion> getQuestions() {
        return this.questions;
    }

    /**
     * @param questions
     *            the questions to set
     */
    public void setQuestions(final List<BooleanQuestion> questions) {
        this.questions = questions;
    }
}

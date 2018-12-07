/**
 *
 */
package de.his.zofar.service.question.model;

import java.util.ArrayList;
import java.util.List;

import de.his.zofar.service.question.model.components.AnswerOption;
import de.his.zofar.service.valuetype.model.Variable;

/**
 * @author le
 *
 */
public abstract class AbstractSingleChoiceQuestion extends AnswerableQuestion {

    /**
     *
     */
    private static final long serialVersionUID = -1327311295581810180L;

    /**
     *
     */
    protected List<AnswerOption> answerOptions;

    /**
     *
     */
    protected AbstractSingleChoiceQuestion() {
        super();
    }

    /**
     * @param variable
     *            the variable
     */
    protected AbstractSingleChoiceQuestion(final Variable variable) {
        super(variable);
    }

    /**
     * @return the answerOptions
     */
    public final List<AnswerOption> getAnswerOptions() {
        if (answerOptions == null) {
            answerOptions = new ArrayList<AnswerOption>();
        }
        return answerOptions;
    }

    /**
     * @param answerOptions
     *            the answerOptions to set
     */
    public abstract void setAnswerOptions(final List<AnswerOption> answerOptions);

    /**
     * @param answerOption
     *            the answer option to add
     */
    public final void addAnswerOption(final AnswerOption answerOption) {
        if (this.answerOptions == null) {
            this.answerOptions = new ArrayList<AnswerOption>();
        }

        // the answer option must have a valid value.
        if (!isAnswerOptionValid(answerOption)) {
            throw new IllegalArgumentException(
                    "The answer option does not have a valid value.");
        }

        answerOption.setQuestion(this);
        answerOption.getValue().setVariable((QuestionVariable) getVariable());
        this.getAnswerOptions().add(answerOption);
    }

    /*
     * (non-Javadoc)
     *
     * @see de.his.hiob.service.question.external.dtos.AnswerableQuestionDTO#
     * createDefaultAnswerOptions
     * (de.his.hiob.service.valuetype.external.dtos.VariableDTO)
     */
    @Override
    protected abstract void createDefaultAnswerOptions();

    /**
     * @param answerOption
     *            the answer option to check
     * @return if answer option is valid
     */
    protected abstract boolean isAnswerOptionValid(AnswerOption answerOption);

}

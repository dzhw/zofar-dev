/**
 *
 */
package de.his.zofar.service.question.model.concrete;

import java.util.List;
import java.util.Map.Entry;

import de.his.zofar.service.question.model.AbstractSingleChoiceQuestion;
import de.his.zofar.service.question.model.QuestionVariable;
import de.his.zofar.service.question.model.components.AnswerOption;
import de.his.zofar.service.question.model.questionvalues.QuestionBooleanValue;
import de.his.zofar.service.valuetype.model.BooleanValueType;
import de.his.zofar.service.valuetype.model.ValueType;
import de.his.zofar.service.valuetype.model.Variable;
import de.his.zofar.service.valuetype.model.possiblevalues.PossibleValue;

/**
 * @author le
 *
 */
public class BooleanQuestion extends AbstractSingleChoiceQuestion {

    /**
     *
     */
    private static final long serialVersionUID = 490586489379639210L;

    /**
     *
     */
    private MultipleChoiceQuestion parentMultipleChoiceQuestion;

    /**
     *
     */
    private AnswerOption trueAnswerOption;

    /**
     *
     */
    public BooleanQuestion() {
        super();
    }

    /**
     * @param variable
     */
    public BooleanQuestion(final Variable variable) {
        super(variable);
    }

    /**
     * @return the parentMultipleChoiceQuestion
     */
    public MultipleChoiceQuestion getParentMultipleChoiceQuestion() {
        return parentMultipleChoiceQuestion;
    }

    /**
     * @param parentMultipleChoiceQuestion
     *            the parentMultipleChoiceQuestion to set
     */
    public void setParentMultipleChoiceQuestion(
            final MultipleChoiceQuestion parentQuestion) {
        this.parentMultipleChoiceQuestion = parentQuestion;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * de.his.hiob.service.question.external.dtos.AbstractSingleChoiceQuestionDTO
     * #setAnswerOptions(java.util.List)
     */
    @Override
    public final void setAnswerOptions(
            final List<AnswerOption> newAnswerOptions) {
        if (newAnswerOptions.size() > 2) {
            throw new IllegalArgumentException(
                    "boolean question cannot have more than 2 answer options");
        }
        for (final AnswerOption option : newAnswerOptions) {
            if (!isAnswerOptionValid(option)) {
                throw new IllegalArgumentException("answer option is not valid");
            }
            if ((Boolean) option.getValue().getValue()) {
                trueAnswerOption = option;
            }
        }
        answerOptions = newAnswerOptions;
    }

    /**
     * @return the answer option that represents the true option.
     */
    public final AnswerOption getTrueAnswerOption() {
        return trueAnswerOption;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * de.his.hiob.service.question.external.dtos.AbstractSingleChoiceQuestionDTO
     * #createDefaultAnswerOptions()
     */
    @Override
    protected final void createDefaultAnswerOptions() {
        if (!BooleanValueType.class.isAssignableFrom(getVariable()
                .getValueType().getClass())) {
            throw new IllegalStateException(
                    "BooleanQuestions can only contain boolean value types");
        }

        for (final Entry<?, ? extends PossibleValue> entry : getVariable()
                .getValueType().getPossibleValues().entrySet()) {
            final AnswerOption answerOption = new AnswerOption();
            answerOption.setValue(new QuestionBooleanValue(
                    (QuestionVariable) getVariable(), (Boolean) entry
                    .getKey()));
            if ((Boolean) entry.getKey()) {
                trueAnswerOption = answerOption;
            }
            getAnswerOptions().add(answerOption);
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * de.his.hiob.service.question.external.dtos.AbstractSingleChoiceQuestionDTO
     * #
     * isAnswerOptionValid(de.his.hiob.service.question.external.dtos.components
     * .AnswerOptionDTO)
     */
    @Override
    protected final boolean isAnswerOptionValid(
            final AnswerOption answerOption) {
        if (getVariable() == null || getVariable().getValueType() == null) {
            final ValueType valueType = getVariable().getValueType();
            // only answer options with boolean value type as value type are
            // allowed.
            if (BooleanValueType.class
                    .isAssignableFrom(valueType.getClass())) {
                return true;
            }
        }
        return false;
    }

}

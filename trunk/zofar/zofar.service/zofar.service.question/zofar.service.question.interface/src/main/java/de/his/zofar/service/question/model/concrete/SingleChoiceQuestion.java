package de.his.zofar.service.question.model.concrete;

import java.util.List;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.his.zofar.service.common.exceptions.NotYetImplementedException;
import de.his.zofar.service.question.model.AbstractSingleChoiceQuestion;
import de.his.zofar.service.question.model.QuestionVariable;
import de.his.zofar.service.question.model.components.AnswerOption;
import de.his.zofar.service.question.model.questionvalues.QuestionNumberValue;
import de.his.zofar.service.valuetype.model.NumberValueType;
import de.his.zofar.service.valuetype.model.ValueType;
import de.his.zofar.service.valuetype.model.Variable;
import de.his.zofar.service.valuetype.model.possiblevalues.PossibleNumberValue;

//@DTOEntityMapping(entity = SingleChoiceQuestionEntity.class)
public class SingleChoiceQuestion extends AbstractSingleChoiceQuestion {
    /**
     *
     */
    private static final long serialVersionUID = 5561629626292072348L;

    private static final Logger LOGGER = LoggerFactory
            .getLogger(SingleChoiceQuestion.class);

    public SingleChoiceQuestion() {
        super();
    }

    /**
     * @param variable
     */
    public SingleChoiceQuestion(final Variable variable) {
        super(variable);
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * de.his.hiob.service.question.external.dtos.AbstractSingleChoiceQuestionDTO
     * #setAnswerOptions(java.util.List)
     */
    @Override
    public final void setAnswerOptions(final List<AnswerOption> newAnswerOptions) {
        for (final AnswerOption option : newAnswerOptions) {
            if (!isAnswerOptionValid(option)) {
                throw new IllegalArgumentException("answer option is not valid");
            }
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see de.his.hiob.service.question.external.dtos.AnswerableQuestionDTO#
     * createDefaultAnswerOptions()
     */
    @Override
    protected final void createDefaultAnswerOptions() {
        final ValueType valueType = getVariable().getValueType();
        if (NumberValueType.class.isAssignableFrom(valueType.getClass())) {
            createAnswerOptionsFromNumberValueType((NumberValueType) valueType);
        } else {
            // TODO implement method for all value types
            throw new NotYetImplementedException();
        }
    }

    /**
     * create the answer option list for a number value type.
     *
     * @param numberValueType
     *            the number value type
     */
    private void createAnswerOptionsFromNumberValueType(
            final NumberValueType numberValueType) {
        // use the possible values list first as answer options
        if (numberValueType.getPossibleValues() != null
                && !(numberValueType.getPossibleValues().getEntryArray().length == 0)) {
            for (final de.his.zofar.service.valuetype.model.NumberValueType.PossibleValues.Entry entry : numberValueType.getPossibleValues().getEntryArray()) {
                final AnswerOption answerOption = new AnswerOption();
                answerOption.setValue(new QuestionNumberValue(
                        (QuestionVariable) getVariable(), entry.getKey()));
                // set the first label as the display text
//                answerOption.setDisplayText(entry.getValue().getLabels().get(0));
                answerOption.setDisplayText(entry.getValue().getLabelArray()[0]);
                // setting back reference
                answerOption.setQuestion(this);

                // adding to the answer option list
                getAnswerOptions().add(answerOption);
            }
        } else {
            if (numberValueType.getMaximum() == 0L) {
                throw new IllegalStateException(
                        "cannot create answer option list if no upper boundary is set.");
            }
            Long min = Long.valueOf(1);
            if (numberValueType.getMinimum() != 0L) {
                min = numberValueType.getMinimum();
            }
            final Long max = numberValueType.getMaximum();
            // final Integer decimalPlaces = numberValueType.getDecimalPlaces();

            for (Long value = min; value <= max; value++) {
                final AnswerOption answerOption = new AnswerOption();
                answerOption.setValue(new QuestionNumberValue(
                        (QuestionVariable) getVariable(), value));

                // set the display text
                answerOption.setDisplayText(value.toString());
                // setting back reference
                answerOption.setQuestion(this);

                // adding to the answer option list
                getAnswerOptions().add(answerOption);
            }
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
    protected final boolean isAnswerOptionValid(final AnswerOption answerOption) {
        if (getVariable() == null || getVariable().getValueType() == null) {
            throw new IllegalStateException("Single choice question "
                    + "must have a variable and a value type");
        }

        if (NumberValueType.class.isAssignableFrom(getVariable().getValueType()
                .getClass())) {
            return answerOptionIsNumberValueCheck(answerOption);
        } else {
            throw new NotYetImplementedException();
        }
    }

    /**
     * @param answerOption
     * @return
     */
    private boolean answerOptionIsNumberValueCheck(
            final AnswerOption answerOption) {
        // the answer option is not a Long
        if (!Long.class.isAssignableFrom(answerOption.getValue().getValue()
                .getClass())) {
            LOGGER.error("the answer option for a variable with a number value type must be a Long");
            return false;
        }

        final NumberValueType numberValueType = (NumberValueType) getVariable()
                .getValueType();
        final Long answerOptionsValue = (Long) answerOption.getValue()
                .getValue();

        // answer options value exceeds the upper boundary
        if (numberValueType.getMaximum() != 0L) {
            if (answerOptionsValue > numberValueType.getMaximum()) {
                LOGGER.error("answer option value exceeds the upper boundary");
                return false;
            }
        }

        // answer options value is below the lower boundary
        if (numberValueType.getMinimum() != 0L) {
            if (answerOptionsValue < numberValueType.getMinimum()) {
                LOGGER.error("answer option value is below the lower boundary");
                return false;
            }
        }

        // possible value contains the answer options value
        if (numberValueType.getPossibleValues() != null) {
//            if (!numberValueType.getPossibleValues().keySet().contains((answerOptionsValue))) {
//                return false;
//            }
        	boolean flag = false;
            for (final de.his.zofar.service.valuetype.model.NumberValueType.PossibleValues.Entry entry : numberValueType.getPossibleValues().getEntryArray()) {
            	if(entry.getKey() == answerOptionsValue){
            		flag = true;
            		break;
            	}
            }
            if(!flag)return false;
        }
        return true;
    }
}

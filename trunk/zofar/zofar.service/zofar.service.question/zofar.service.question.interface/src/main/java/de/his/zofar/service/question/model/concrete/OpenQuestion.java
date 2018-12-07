package de.his.zofar.service.question.model.concrete;

import de.his.zofar.service.common.exceptions.NotYetImplementedException;
import de.his.zofar.service.question.model.AnswerableQuestion;
import de.his.zofar.service.question.model.QuestionVariable;
import de.his.zofar.service.question.model.components.AnswerOption;
import de.his.zofar.service.question.model.questionvalues.QuestionStringValue;
import de.his.zofar.service.valuetype.model.StringValueType;
import de.his.zofar.service.valuetype.model.ValueType;

//@DTOEntityMapping(entity = OpenQuestionEntity.class)
public class OpenQuestion extends AnswerableQuestion {

    /**
     *
     */
    private static final long serialVersionUID = 6788826958566692379L;

    private AnswerOption possibleAnswer;

    /**
     *
     */
    public OpenQuestion() {
        super();
    }

    /*
     * (non-Javadoc)
     *
     * @see de.his.hiob.service.question.external.dtos.AnswerableQuestionDTO#
     * createDefaultAnswerOptions()
     */
    @Override
    protected void createDefaultAnswerOptions() {
        final ValueType valueType = getVariable().getValueType();
        if (StringValueType.class.isAssignableFrom(valueType.getClass())) {
            possibleAnswer = new AnswerOption();
            possibleAnswer.setValue(new QuestionStringValue(
                    (QuestionVariable) getVariable(), ""));
        } else {
            throw new NotYetImplementedException();
        }
    }

    /**
     * @return the possibleAnswer
     */
    public AnswerOption getPossibleAnswer() {
        return possibleAnswer;
    }

    /**
     * @param possibleAnswer
     *            the possibleAnswer to set
     */
    public void setPossibleAnswer(final AnswerOption possibleAnswer) {
        this.possibleAnswer = possibleAnswer;
    }
}

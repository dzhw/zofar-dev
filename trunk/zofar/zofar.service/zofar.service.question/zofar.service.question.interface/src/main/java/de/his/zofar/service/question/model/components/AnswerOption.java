package de.his.zofar.service.question.model.components;

import de.his.zofar.service.common.model.BaseDTO;
import de.his.zofar.service.common.model.conditionable.impl.Visibility;
import de.his.zofar.service.question.model.AbstractSingleChoiceQuestion;
import de.his.zofar.service.question.model.AnswerableQuestion;
import de.his.zofar.service.question.model.concrete.OpenQuestion;
import de.his.zofar.service.question.model.interfaces.Answer;
import de.his.zofar.service.question.model.questionvalues.QuestionValue;
import de.his.zofar.service.valuetype.model.Variable;

//@DTOEntityMapping(entity = AnswerOptionEntity.class)
public class AnswerOption extends BaseDTO implements Visibility, Answer {

    /**
     *
     */
    private static final long serialVersionUID = -1182397003634492471L;

    /**
     *
     */
    private String visibilityCondition;

    private String displayText;

    private AnswerableQuestion question;

    private OpenQuestion openQuestion;

    private QuestionValue value;

    public AnswerOption() {
        super();
    }

    public String getDisplayText() {
        return this.displayText;
    }

    public OpenQuestion getOpenQuestion() {
        return this.openQuestion;
    }

    public AnswerableQuestion getQuestion() {
        return this.question;
    }

    /**
     * @return the value
     */
    public QuestionValue getValue() {
        return this.value;
    }

    public void setDisplayText(final String displayText) {
        this.displayText = displayText;
    }

    public void setOpenQuestion(final OpenQuestion open) {
        this.openQuestion = open;
    }

    public void setQuestion(final AbstractSingleChoiceQuestion question) {
        this.question = question;
    }

    /**
     * @param value
     *            the value to set
     */
    public void setValue(final QuestionValue value) {
        this.value = value;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * de.his.hiob.service.common.external.dtos.conditionable.impl.Visibility
     * #getVisibilityCondition()
     */
    @Override
    public String getVisibilityCondition() {
        if (visibilityCondition == null) {
            visibilityCondition = "";
        }
        return visibilityCondition;
    }

    /**
     * @param visibilityCondition
     *            the visibilityCondition to set
     */
    public void setVisibilityCondition(final String visibilityCondition) {
        this.visibilityCondition = visibilityCondition;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * de.his.hiob.model.question.models.interfaces.Answer#setAnswerValue(java
     * .lang.Object)
     */
    @Override
    public void setAnswerValue(final Object value) {
        if (value == null) {
            throw new IllegalStateException(
                    "AnswerOption must have a QuestionValue!");
        }
        this.value.setValue(value);
    }

    /*
     * (non-Javadoc)
     *
     * @see de.his.hiob.model.question.models.interfaces.Answer#getAnswerValue()
     */
    @Override
    public Object getAnswerValue() {
        if (value == null) {
            throw new IllegalStateException(
                    "AnswerOption must have a QuestionValue!");
        }
        return value.getValue();
    }

    /*
     * (non-Javadoc)
     *
     * @see de.his.hiob.model.question.models.interfaces.Answer#getAnswerLabel()
     */
    @Override
    public String getAnswerLabel() {
        return displayText;
    }

	@Override
	public Variable getAnswerVariable() {
		return getQuestion().getVariable();
	}

	@Override
	public String toString() {
		return "AnswerOption [displayText=" + displayText + ", value=" + value
				+ "]";
	}

}

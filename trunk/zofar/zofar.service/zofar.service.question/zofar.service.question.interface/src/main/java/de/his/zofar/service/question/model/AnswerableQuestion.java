package de.his.zofar.service.question.model;

import de.his.zofar.service.valuetype.model.Variable;

//@DTOEntityMapping(entity = AnswerableQuestionEntity.class)
public abstract class AnswerableQuestion extends Question {

    /**
     *
     */
    private static final long serialVersionUID = 4532986056313013322L;

    private Variable variable;

    public AnswerableQuestion() {
        super();
    }

    public AnswerableQuestion(final Variable variable) {
        super();
        this.setVariable(variable);
    }

    /**
     * @return
     */
    public final Variable getVariable() {
        return this.variable;
    }

    /**
     * @param variable
     */
    public final void setVariable(final Variable variable) {
        this.variable = variable;
        createDefaultAnswerOptions();
    }

    /**
     * creates the default list of answer options based on the variable and
     * their value type
     *
     * @param variable
     *            the variable to create the answer options from
     */
    protected abstract void createDefaultAnswerOptions();
}

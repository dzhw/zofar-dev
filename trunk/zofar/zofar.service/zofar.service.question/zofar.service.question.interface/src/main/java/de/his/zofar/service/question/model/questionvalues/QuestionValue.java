/**
 *
 */
package de.his.zofar.service.question.model.questionvalues;

import de.his.zofar.service.common.model.BaseDTO;
import de.his.zofar.service.question.model.QuestionVariable;

/**
 * @author le
 *
 */
// @DTOEntityMapping(entity = QuestionValueEntity.class)
public abstract class QuestionValue extends BaseDTO {
    /**
     *
     */
    private static final long serialVersionUID = 6661693250012327418L;
    private QuestionVariable variable;

    /**
     *
     */
    public QuestionValue() {
        super();
    }

    /**
     * @param variable
     */
    public QuestionValue(final QuestionVariable variable) {
        super();
        this.variable = variable;
    }

    public abstract Object getValue();

    /**
     * @return the variable
     */
    public QuestionVariable getVariable() {
        return this.variable;
    }

    public abstract void setValue(final Object value);

    /**
     * @param variable
     *            the variable to set
     */
    public void setVariable(final QuestionVariable variable) {
        this.variable = variable;
    }

    @Override
    public String toString() {
        return "QuestionValue [variable=" + variable + "]";
    }
}

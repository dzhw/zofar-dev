/**
 *
 */
package de.his.zofar.service.question.model;

import de.his.zofar.service.valuetype.model.Variable;

/**
 * @author le
 *
 */
// @DTOEntityMapping(entity = QuestionVariableEntity.class)
public class QuestionVariable extends Variable {

	private static final long serialVersionUID = 974863818576679630L;

	/**
     *
     */
    public QuestionVariable() {
        super();
    }

    public QuestionVariable(final Variable var) {
        this();
        this.setUuid(var.getUuid());
        this.setName(var.getName());
        this.setValueType(var.getValueType());
    }

	@Override
	public String toString() {
		return "QuestionVariable [getName()=" + getName() + "]";
	}
}

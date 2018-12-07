/**
 *
 */
package de.his.zofar.service.questionnaire.model.trigger.components;

import de.his.zofar.service.question.model.interfaces.Answer;
import de.his.zofar.service.valuetype.model.Variable;


/**
 * @author meisner
 *
 */
public class UnboundAnswer implements Answer {

	private static final long serialVersionUID = -6404758851471974763L;
	private final Variable variable;
	private Object value;
	private final String label;

	public UnboundAnswer(final Variable variable,final String label) {
		super();
		this.variable = variable;
		this.label = label;
	}

	/* (non-Javadoc)
	 * @see de.his.hiob.model.question.models.interfaces.Answer#setAnswerValue(java.lang.Object)
	 */
	@Override
	public void setAnswerValue(final Object value) {
		this.value = value;
	}

	/* (non-Javadoc)
	 * @see de.his.hiob.model.question.models.interfaces.Answer#getAnswerValue()
	 */
	@Override
	public Object getAnswerValue() {
		return this.value;
	}

	/* (non-Javadoc)
	 * @see de.his.hiob.model.question.models.interfaces.Answer#getAnswerLabel()
	 */
	@Override
	public String getAnswerLabel() {
		return this.label;
	}

	/* (non-Javadoc)
	 * @see de.his.hiob.model.question.models.interfaces.Answer#getAnswerVariable()
	 */
	@Override
	public Variable getAnswerVariable() {
		return this.variable;
	}

}

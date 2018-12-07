package de.his.zofar.service.questionnaire.model.trigger.concrete;

import de.his.zofar.service.question.model.interfaces.Answer;
import de.his.zofar.service.questionnaire.model.trigger.Trigger;
import de.his.zofar.service.questionnaire.model.trigger.components.UnboundAnswer;
import de.his.zofar.service.valuetype.model.Variable;

/**
 * @author meisner
 *
 */
public class AutomatedAssignment extends Trigger {

	private static final long serialVersionUID = -1034044098987759911L;

	private final Variable assignVariable;
	private final String assignLabel;
	private final Object assignValue;
	private Answer assignAnswer;

	public AutomatedAssignment(final Variable assignVariable, final String assignLabel,
			final Object assignValue) {
		super();
		this.assignVariable = assignVariable;
		this.assignLabel = assignLabel;
		this.assignValue = assignValue;
	}

	@Override
	public Answer proceed() {
		if (this.assignAnswer == null) {
			this.assignAnswer = new UnboundAnswer(this.assignVariable,
					this.assignLabel);
		}
		this.assignAnswer.setAnswerValue(assignValue);
		return this.assignAnswer;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "AutomatedAssignment [assignVariable=" + assignVariable
				+ ", assignLabel=" + assignLabel + ", assignValue="
				+ assignValue + ", assignAnswer=" + assignAnswer
				+ ", isForward()=" + isForward() + ", isBackward()="
				+ isBackward() + ", getCondition()=" + getCondition() + "]";
	}



}

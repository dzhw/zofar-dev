package de.his.zofar.service.questionnaire.model.trigger;

import java.io.Serializable;

/**
 * @author meisner
 * 
 */

public abstract class Trigger implements Serializable {

	private static final long serialVersionUID = -3326888381422769905L;
	
	private boolean forward = false;
	private boolean backward = false;
	private String condition;

	protected Trigger() {
		super();
	}

	public boolean isForward() {
		return forward;
	}

	public void setForward(boolean forward) {
		this.forward = forward;
	}

	public boolean isBackward() {
		return backward;
	}

	public void setBackward(boolean backward) {
		this.backward = backward;
	}

	public String getCondition() {
		return condition;
	}

	public void setCondition(String condition) {
		this.condition = condition;
	}
	
	public abstract Object proceed();

}

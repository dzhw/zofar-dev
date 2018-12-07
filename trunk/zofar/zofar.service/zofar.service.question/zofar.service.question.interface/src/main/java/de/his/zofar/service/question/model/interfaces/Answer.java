package de.his.zofar.service.question.model.interfaces;

import java.io.Serializable;

import de.his.zofar.service.valuetype.model.Variable;

public interface Answer extends Serializable{

	public void setAnswerValue(Object value);
	public Object getAnswerValue();

	public String getAnswerLabel();
	public Variable getAnswerVariable();

}

package de.his.zofar.service.evaluation.service.interfaces;

import java.io.Serializable;
import java.util.Map;

public interface EvaluatorContextHolder extends Serializable{
	
	public Map<String, ?> getEvaluatorMap();
	public String getLabel(Object obj);

}

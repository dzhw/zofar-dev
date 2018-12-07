package de.his.zofar.service.evaluation.service.interfaces.impl;

import java.util.Map;

import de.his.zofar.service.evaluation.service.interfaces.EvaluatorContextHolder;

public abstract class EvaluatorContextHolderProxy implements EvaluatorContextHolder{

	private static final long serialVersionUID = 443237673430614390L;

	public EvaluatorContextHolderProxy() {
		super();
	}

	@Override
	public abstract Map<String, ?> getEvaluatorMap();

	@Override
	public abstract String getLabel(Object obj);

}
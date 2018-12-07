package de.his.zofar.service.valuetype.impl;

import java.util.Set;

import org.dozer.Mapper;
import org.springframework.stereotype.Service;

import de.his.zofar.service.common.AbstractService;
import de.his.zofar.service.valuetype.model.PanelVariable;
import de.his.zofar.service.valuetype.model.Question;
import de.his.zofar.service.valuetype.model.SurveyVariable;
import de.his.zofar.service.valuetype.model.ValueType;
import de.his.zofar.service.valuetype.model.Variable;
import de.his.zofar.service.valuetype.service.VariableService;

/**
 * @author meisner
 *
 */
@Service("variableService")
public class VariableServiceImpl extends AbstractService implements
		VariableService {

	public VariableServiceImpl(final Mapper mapper) {
		super(mapper);
		// TODO Auto-generated constructor stub
	}

	@Override
	public PanelVariable createPanelVariable(final String uuid, final String name,final String label,final ValueType valueType) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SurveyVariable createSurveyVariable(final String uuid,final String name,final String label,final ValueType valueType,
			final Question question) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Variable loadVariable(final String uuid) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<Variable> loadByType(final Class<? extends Variable> type) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void removeVariable(final Variable variable) {
		// TODO Auto-generated method stub

	}
	
	@Override
	public Variable save(final Variable variable) {
		// TODO Auto-generated method stub
		return variable;
	}

}

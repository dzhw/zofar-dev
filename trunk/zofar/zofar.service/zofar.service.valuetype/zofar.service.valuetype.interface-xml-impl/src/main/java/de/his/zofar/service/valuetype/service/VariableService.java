package de.his.zofar.service.valuetype.service;

import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import de.his.zofar.service.valuetype.model.PanelVariable;
import de.his.zofar.service.valuetype.model.Question;
import de.his.zofar.service.valuetype.model.SurveyVariable;
import de.his.zofar.service.valuetype.model.ValueType;
import de.his.zofar.service.valuetype.model.Variable;

/**
 * @author meisner
 * 
 */
@Service
public interface VariableService {

	@Transactional
	public abstract PanelVariable createPanelVariable(final String uuid,final String name,final String label,final ValueType valueType);
	
	@Transactional
	public abstract SurveyVariable createSurveyVariable(final String uuid,final String name,final String label, final ValueType valueType, final Question question);

	@Transactional
	public abstract Variable loadVariable(final String uuid);
	
	@Transactional
	public Set<Variable> loadByType(Class<? extends Variable> type);
	
	@Transactional
	public abstract void removeVariable(final Variable variable);
	@Transactional
	public abstract Variable save(final Variable variable);


}

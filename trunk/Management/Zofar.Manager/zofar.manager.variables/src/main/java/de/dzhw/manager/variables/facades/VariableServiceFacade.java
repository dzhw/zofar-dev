package de.dzhw.manager.variables.facades;

import java.io.Serializable;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.jsf.FacesContextUtils;

import de.his.zofar.service.valuetype.model.PanelVariable;
import de.his.zofar.service.valuetype.model.Question;
import de.his.zofar.service.valuetype.model.SurveyVariable;
import de.his.zofar.service.valuetype.model.ValueType;
import de.his.zofar.service.valuetype.model.Variable;
import de.his.zofar.service.valuetype.service.VariableService;

public class VariableServiceFacade implements Serializable {

	private static final long serialVersionUID = 4358365370957682518L;

	private static final Logger LOGGER = LoggerFactory
			.getLogger(VariableServiceFacade.class);
	
	private transient VariableService service = null;

	public VariableServiceFacade() {
		super();
		LOGGER.info("constructor");
	}
	 
	@PostConstruct
	private void init() {
		LOGGER.info("init");
	}
	
	/**
	 * Retrieve the singleton service from the FacesContext.
	 * 
	 * @return A reference to the service bean.
	 */
	private VariableService getService() {
		if (this.service == null) {
			this.service = findBean();
		}
		return this.service;
	}
	
	public PanelVariable createPanelVariable(final String uuid, final String name,final String label,final ValueType valueType) {
		return getService().createPanelVariable(uuid, name,label,valueType);
	}

	public SurveyVariable createSurveyVariable(final String uuid, final String name,final String label,final ValueType valueType,
			final Question question) {
		return getService().createSurveyVariable(uuid, name,label,valueType, question);
	}

	public Variable loadVariable(final String uuid) {
		return getService().loadVariable(uuid);
	}

	public Set<Variable> loadByType(final Class<? extends Variable> type) {
		return getService().loadByType(type);
	}

	public void removeVariable(final Variable variable) {
		getService().removeVariable(variable);
	}
	
	public Variable save(final Variable variable) {
		return getService().save(variable);
	}

	private VariableService findBean() {
		final WebApplicationContext webContext = FacesContextUtils
				.getRequiredWebApplicationContext(FacesContext
						.getCurrentInstance());
		return webContext.getBean(VariableService.class);
	}

}

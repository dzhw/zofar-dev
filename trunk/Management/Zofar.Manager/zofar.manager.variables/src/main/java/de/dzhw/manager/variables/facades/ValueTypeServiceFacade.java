package de.dzhw.manager.variables.facades;

import java.io.Serializable;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.jsf.FacesContextUtils;

import de.his.zofar.service.valuetype.model.ValueType;
import de.his.zofar.service.valuetype.service.ValueTypeService;

public class ValueTypeServiceFacade implements Serializable {
	
	private static final long serialVersionUID = -5947063154855125400L;

	private static final Logger LOGGER = LoggerFactory
			.getLogger(ValueTypeServiceFacade.class);
	
	private transient ValueTypeService service = null;

	public ValueTypeServiceFacade() {
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
	private ValueTypeService getService() {
		if (this.service == null) {
			this.service = findBean();
		}
		return this.service;
	}
	

	
	public ValueType createNumberValueType(final String identifier,final String description,final String measurementLevel,int decimalPlaces, long minimum,
			long maximum) {
		return getService().createNumberValueType(identifier,description,measurementLevel,decimalPlaces, minimum, maximum);
	}

	public ValueType createStringValueType(final String identifier,final String description,final String measurementLevel,int length, boolean canbeempty) {
		return getService().createStringValueType(identifier,description,measurementLevel,length, canbeempty);
	}

	public ValueType createBooleanValueType(final String identifier,final String description,final String measurementLevel) {
		return getService().createBooleanValueType(identifier,description,measurementLevel);
	}

	public ValueType loadByIdentifier(String identifier) {
		return getService().loadByIdentifier(identifier);
	}
	
	public Set<ValueType> loadByType(Class<? extends ValueType> type) {
		return getService().loadByType(type);
	}

	public void removeValueType(ValueType valueType) {
		getService().removeValueType(valueType);
	}
	
	public ValueType save(ValueType valueType) {
		return getService().save(valueType);
	}

	private ValueTypeService findBean() {
		final WebApplicationContext webContext = FacesContextUtils
				.getRequiredWebApplicationContext(FacesContext
						.getCurrentInstance());
		return webContext.getBean(ValueTypeService.class);
	}

}

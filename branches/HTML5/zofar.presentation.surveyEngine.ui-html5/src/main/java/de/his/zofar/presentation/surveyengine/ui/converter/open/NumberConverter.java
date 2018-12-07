package de.his.zofar.presentation.surveyengine.ui.converter.open;

import java.io.Serializable;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@FacesConverter("org.zofar.open.NumberConverter")
public class NumberConverter implements Serializable, Converter {

	/**
	 *
	 */
	private static final long serialVersionUID = 3680026520455850356L;

	private static final Logger LOGGER = LoggerFactory.getLogger(NumberConverter.class);

	public NumberConverter() {
		super();
	}

	@Override
	public Object getAsObject(final FacesContext context, final UIComponent component, final String value) {
		if (value == null) {
			return null;
		}
		if (component == null) {
			return null;
		}
		final String back = value.replace(',', '.');
		// LOGGER.info("as Object {} ==> {}",value,back);
		return back;
	}

	@Override
	public String getAsString(final FacesContext context, final UIComponent component, final Object value) {
		if (value == null) {
			return null;
		}
		if (component == null) {
			return null;
		}
		final String back = ((String) value).replace('.', ',');
		// LOGGER.info("as String {} ==> {}",value,back);
		return back;
	}

}

package de.his.zofar.presentation.surveyengine.ui.converter.open;

import java.io.Serializable;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.his.zofar.presentation.surveyengine.ui.renderer.answers.matrix.singlechoice.responsedomain.ZofarSingleChoiceMatrixResponseDomainRenderer;

@FacesConverter("org.zofar.open.NumberConverter")
public class NumberConverter implements Serializable, Converter {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3680026520455850356L;
	
	private static final Logger LOGGER = LoggerFactory
			.getLogger(NumberConverter.class);

	public NumberConverter() {
		super();
	}

	@Override
	public Object getAsObject(FacesContext context, UIComponent component,
			String value) {
		if(value == null)return null;
		if(component == null)return null;
		final String back = value.replace(',', '.');
//		LOGGER.info("as Object {} ==> {}",value,back);
		return back;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component,
			Object value) {
		if(value == null)return null;
		if(component == null)return null;
		final String back = ((String)value).replace('.',',');
//		LOGGER.info("as String {} ==> {}",value,back);
		return back;
	}

}

package de.his.zofar.presentation.question.beans;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.his.zofar.service.question.model.interfaces.Answer;

/**
 * @author meisner
 * 
 */
@ManagedBean
@SessionScoped
@FacesConverter("AnswerConverterBean")
public class AnswerConverterBean implements Converter, Serializable {

	private static final long serialVersionUID = -8362654695902766786L;

	private static final Logger LOGGER = LoggerFactory
			.getLogger(AnswerConverterBean.class);

	private Map<String, Object> map;

	public AnswerConverterBean() {
		super();

	}

	@PostConstruct
	private void init() {
		LOGGER.info("init AnswerConverterBean");
		map = new HashMap<String, Object>();
	}

	@Override
	public Object getAsObject(final FacesContext context, final UIComponent component,
			final String value) throws ConverterException {
//		LOGGER.info("getAsObject {} {}", value, component);
		if ((value != null)&&(map.containsKey(value))) {
			final Object back = map.get(value);
			map.remove(value);
			return back;
		}
		return null;
	}

	@Override
	public String getAsString(final FacesContext context, final UIComponent component,
			final Object value) throws ConverterException {
//		LOGGER.info("getAsString {} {}", value, component);
		if (value != null) {
			String key = UUID.randomUUID() + "";
			if((Answer.class).isAssignableFrom(value.getClass())){
				key = ((Answer)value).hashCode()+"";
			}
			map.put(key, value);
			return key;
		}
		return null;
	}

}

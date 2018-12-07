package de.his.zofar.presentation.surveyengine.ui.components.trigger.validators.constraints;

import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.his.zofar.presentation.surveyengine.ui.util.JsfUtility;

/**
 *
 * Class to throw a ValidatorException,when validationCondition result to false
 *
 * @author meisner
 * @see javax.faces.validator.Validator
 */

@FacesValidator("org.zofar.CustomConstraintsValidator")
public class CustomConstraintsValidator implements Validator {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(CustomConstraintsValidator.class);

	private String message = null;

	public void setMessage(final String message) {
		this.message = message;
	}

	@Override
	public void validate(final FacesContext context,
			final UIComponent component, final Object value)
			throws ValidatorException {

//		final ExternalContext externalContext = context.getExternalContext();

//		final Map<String, Object> requestMap = externalContext.getRequestMap();
//
//		 for (final String key : requestMap.keySet()) {
//		 LOGGER.info("# {} = {}",key,requestMap.get(key));
//		 }


		boolean flag = false;
		if (value != null) {
			if ((Boolean.class).isAssignableFrom(value.getClass())) {
				flag = (Boolean) value;
			}
			if ((String.class).isAssignableFrom(value.getClass())) {
				final JsfUtility jsfUtility = JsfUtility.getInstance();
				flag = jsfUtility.evaluateValueExpression(context, "#{"+((String) value)+"}", Boolean.class);
			}

		}
//		LOGGER.info("{} => {}", value, flag);
		if (!flag) {
			final FacesMessage msg = new FacesMessage(this.message);
//			FacesContext.getCurrentInstance().addMessage(null, msg);
			throw new ValidatorException(msg);
		}
	}

}

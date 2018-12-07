package de.his.zofar.presentation.surveyengine.ui.components.trigger;

import java.util.HashMap;
import java.util.Map;

import javax.faces.application.Application;
import javax.faces.application.FacesMessage;
import javax.faces.component.FacesComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.his.zofar.presentation.surveyengine.ui.components.trigger.validators.constraints.CustomConstraintsValidator;
import de.his.zofar.presentation.surveyengine.ui.exceptions.ZofarConstraintsOnexitException;
import de.his.zofar.presentation.surveyengine.ui.exceptions.ZofarConstraintsOnloadException;
import de.his.zofar.presentation.surveyengine.ui.interfaces.INavigator;
import de.his.zofar.presentation.surveyengine.ui.interfaces.Task;
import de.his.zofar.presentation.surveyengine.ui.util.JsfUtility;

/**
 * @author meisner
 * 
 */
@FacesComponent(value = "org.zofar.ConsistencyTrigger")
public class ConsistencyTrigger extends AbstractTrigger implements Task {

	/**
     *
     */
	private static final long serialVersionUID = -4555546838436103829L;
	private static final Logger LOGGER = LoggerFactory
			.getLogger(ConsistencyTrigger.class);

	public ConsistencyTrigger() {
		super();
	}

	public String getConstraintsConditionAttribute() {
		String value = String.valueOf(getAttributes().get("constraints"));
		if (value.equals("null")) {
			value = "true";
		}
		return value;
	}

	public String constraintsMessage() {
		String value = (String) getAttributes().get("message");
		if (value == null) {
			value = "UNKOWN MESSAGE";
		}
		return value;
	}

	private Object executeHelper(final boolean validDirection,
			final String condition, final String constraintsCondition,
			final String message) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("navigatorBean :  {} ", getNavigatorValue());
			LOGGER.debug("validDirection :  {} ", validDirection);
			LOGGER.debug("condition :  {} ", condition);
			LOGGER.debug("constraintsCondition {}", constraintsCondition);
			LOGGER.debug("message {}", message);
		}
		final JsfUtility jsfUtility = JsfUtility.getInstance();
		if (validDirection
				&& jsfUtility.evaluateValueExpression(getFacesContext(), "#{"
						+ condition + "}", Boolean.class)) {
			executeScriptItems();

			final FacesContext context = FacesContext.getCurrentInstance();
			final Application application = context.getApplication();

			final CustomConstraintsValidator validator = (CustomConstraintsValidator) application
					.createValidator("org.zofar.CustomConstraintsValidator");
			validator.setMessage(message);
			try {
				validator.validate(context, this, constraintsCondition);
			} catch (ValidatorException e) {
				final FacesContext facesContext = FacesContext.getCurrentInstance();
				final FacesMessage facesMessage = new FacesMessage(e.getMessage());
				facesContext.addMessage(this.getClientId(), facesMessage);
			}
		}
		return null;
	}

	@Override
	public Object execute() {
		setNavigatorValue(getNavigatorAttribute());
		// this.setConditionValue(this.getConditionAttribute());
		setDirectionValue(getDirectionAttribute());
		try {
			executeHelper(validDirection(), getConditionAttribute(),
					getConstraintsConditionAttribute(), constraintsMessage());
		} catch (final ValidatorException e) {
			throw new ZofarConstraintsOnloadException(e);
		}
		return null;
	}

	@Override
	public Map<String, Object> dump() {
		final Map<String, Object> dump = new HashMap<String, Object>();
		dump.put("navigator", getNavigatorAttribute());
		dump.put("condition", getConditionAttribute());
		dump.put("direction", getDirectionAttribute());
		dump.put("constraintsCondition", getConstraintsConditionAttribute());
		dump.put("constraintsMessage", constraintsMessage());
		return dump;
	}

	@Override
	public void executeTask(final Map<String, Object> dump) {
		setNavigatorValue((INavigator) dump.get("navigator"));
		setDirectionValue((String) dump.get("direction"));
		// this.setConditionValue((String) dump.get("condition"));
		try {
			executeHelper(validDirection(), (String) dump.get("condition"),
					(String) dump.get("constraintsCondition"),
					(String) dump.get("constraintsMessage"));
		} catch (final ValidatorException e) {
			throw new ZofarConstraintsOnexitException(e);
		}
	}

}

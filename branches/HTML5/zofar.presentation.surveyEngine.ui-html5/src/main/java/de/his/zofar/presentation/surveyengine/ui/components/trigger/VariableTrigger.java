/**
 *
 */
package de.his.zofar.presentation.surveyengine.ui.components.trigger;

import java.util.HashMap;
import java.util.Map;

import javax.faces.component.FacesComponent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.his.zofar.presentation.surveyengine.ui.interfaces.IAnswerBean;
import de.his.zofar.presentation.surveyengine.ui.interfaces.INavigator;
import de.his.zofar.presentation.surveyengine.ui.interfaces.Task;
import de.his.zofar.presentation.surveyengine.ui.util.JsfUtility;

/**
 * @author meisner
 *
 */
@FacesComponent(value = "org.zofar.VariableTrigger")
public class VariableTrigger extends AbstractTrigger implements Task {

	/**
	 *
	 */
	private static final long serialVersionUID = -1080328287743433867L;
	private static final Logger LOGGER = LoggerFactory.getLogger(VariableTrigger.class);

	public VariableTrigger() {
		super();
	}

	public Object triggerValue() {
		final Object value = this.getAttributes().get("value");
		return value;
	}

	public IAnswerBean variable() {
		final IAnswerBean value = (IAnswerBean) this.getAttributes().get("var");
		return value;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 *
	 * de.his.zofar.surveyengine.uicomponents.trigger.AbstractTrigger#execute()
	 */
	private Object executeHelper(final boolean validDirection, final String condition, final IAnswerBean variable, final Object triggerValue) {
		// if(LOGGER.isDebugEnabled()){

		// }

		final JsfUtility jsfUtility = JsfUtility.getInstance();
		final boolean conditionResult = jsfUtility.evaluateValueExpression(this.getFacesContext(), "#{" + condition + "}", Boolean.class);

		// LOGGER.info("navigatorBean : {} ", getNavigatorValue());
		// LOGGER.info("validDirection : {} ", validDirection);
		// LOGGER.info("condition : {} ==> {}", condition,conditionResult);
		// LOGGER.info("variable : {} ", variable.getVariableName());
		// LOGGER.info("triggerValue : {} ", triggerValue);

		if (validDirection && conditionResult) {
			this.executeScriptItems();
			if (variable != null) {
				final String value = jsfUtility.evaluateValueExpression(this.getFacesContext(), "#{" + triggerValue + "}", Object.class) + "";
				// LOGGER.info("==> set variable {} to value {}",
				// variable.getVariableName(),value+"
				// ("+variable.getClass()+")");
				variable.setStringValue(value);
			}
		}
		return null;
	}

	@Override
	public Object execute() {
		this.setNavigatorValue(this.getNavigatorAttribute());
		// this.setConditionValue(this.getConditionAttribute());
		this.setDirectionValue(this.getDirectionAttribute());
		return this.executeHelper(this.validDirection(), this.getConditionAttribute(), this.variable(), this.triggerValue());
	}

	@Override
	public Map<String, Object> dump() {
		final Map<String, Object> dump = new HashMap<String, Object>();
		dump.put("navigator", this.getNavigatorAttribute());
		dump.put("condition", this.getConditionAttribute());
		dump.put("direction", this.getDirectionAttribute());
		dump.put("variable", this.variable());
		dump.put("value", this.triggerValue());
		return dump;
	}

	@Override
	public void executeTask(final Map<String, Object> dump) {
		this.setNavigatorValue((INavigator) dump.get("navigator"));
		// this.setConditionValue((String) dump.get("condition"));
		this.setDirectionValue((String) dump.get("direction"));
		this.executeHelper(this.validDirection(), (String) dump.get("condition"), (IAnswerBean) dump.get("variable"), dump.get("value"));
	}

}

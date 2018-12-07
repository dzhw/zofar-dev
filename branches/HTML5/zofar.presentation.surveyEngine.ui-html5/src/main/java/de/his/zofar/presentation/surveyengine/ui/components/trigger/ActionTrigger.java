package de.his.zofar.presentation.surveyengine.ui.components.trigger;

import java.util.HashMap;
import java.util.Map;

import javax.el.ELContext;
import javax.el.MethodExpression;
import javax.faces.application.Application;
import javax.faces.component.FacesComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.his.zofar.presentation.surveyengine.ui.interfaces.INavigator;
import de.his.zofar.presentation.surveyengine.ui.interfaces.Task;
import de.his.zofar.presentation.surveyengine.ui.util.JsfUtility;

/**
 * @author meisner
 *
 */
@FacesComponent(value = "org.zofar.ActionTrigger")
public class ActionTrigger extends AbstractTrigger implements Task {

	/**
	 *
	 */
	private static final long serialVersionUID = -4274743725893136924L;
	private static final Logger LOGGER = LoggerFactory.getLogger(ActionTrigger.class);

	public ActionTrigger() {
		super();
	}

	public String getTriggerActionAttribute() {
		final String value = (String) this.getAttributes().get("command");
		return value;
	}

	@Override
	public Map<String, Object> dump() {
		final Map<String, Object> dump = new HashMap<String, Object>();
		dump.put("navigator", this.getNavigatorAttribute());
		dump.put("condition", this.getConditionAttribute());
		dump.put("direction", this.getDirectionAttribute());
		dump.put("action", this.getTriggerActionAttribute());
		return dump;
	}

	@Override
	public void executeTask(final Map<String, Object> dump) {
		LOGGER.debug("trigger executeTask");
		this.setNavigatorValue((INavigator) dump.get("navigator"));
		this.setDirectionValue((String) dump.get("direction"));
		// this.setConditionValue((String) dump.get("condition"));
		this.executeHelper(this.validDirection(), (String) dump.get("condition"), (String) dump.get("action"));
	}

	private Object executeHelper(final boolean validDirection, final String condition, final String action) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("navigatorBean :  {} ", this.getNavigatorValue());
			LOGGER.debug("validDirection :  {} ", validDirection);
			LOGGER.debug("condition :  {} ", condition);
			LOGGER.debug("action {}", action);
		}

		final JsfUtility jsfUtility = JsfUtility.getInstance();
		if (validDirection && jsfUtility.evaluateValueExpression(this.getFacesContext(), "#{" + condition + "}", Boolean.class)) {
			this.executeScriptItems();
			final FacesContext context = FacesContext.getCurrentInstance();
			final ELContext elContext = context.getELContext();
			final Application application = context.getApplication();
			final MethodExpression methodExpression = application.getExpressionFactory().createMethodExpression(FacesContext.getCurrentInstance().getELContext(), "#{" + action + "}", null, new Class[] { ActionEvent.class });
			methodExpression.invoke(elContext, new Object[0]);
		}
		return null;

	}

	@Override
	public Object execute() {
		LOGGER.debug("trigger execute");
		this.setNavigatorValue(this.getNavigatorAttribute());
		// this.setConditionValue(this.getConditionAttribute());
		this.setDirectionValue(this.getDirectionAttribute());
		this.executeHelper(this.validDirection(), this.getConditionAttribute(), this.getTriggerActionAttribute());
		return null;
	}

}

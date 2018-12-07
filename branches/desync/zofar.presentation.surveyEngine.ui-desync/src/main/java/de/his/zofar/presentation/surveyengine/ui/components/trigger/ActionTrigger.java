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
    private static final Logger LOGGER = LoggerFactory
			.getLogger(ActionTrigger.class);

	public ActionTrigger() {
		super();
	}

	public String getTriggerActionAttribute() {
		final String value = (String) getAttributes().get("command");
		return value;
	}

    @Override
    public Map<String, Object> dump() {
        final Map<String, Object> dump = new HashMap<String, Object>();
        dump.put("navigator", getNavigatorAttribute());
        dump.put("condition", getConditionAttribute());
        dump.put("direction", getDirectionAttribute());
        dump.put("action", getTriggerActionAttribute());
        return dump;
    }

    @Override
    public void executeTask(final Map<String, Object> dump) {
        LOGGER.debug("trigger executeTask");
        setNavigatorValue((INavigator) dump.get("navigator"));
        setDirectionValue((String) dump.get("direction"));
//        this.setConditionValue((String) dump.get("condition"));
        executeHelper(validDirection(), (String) dump.get("condition"),
                (String) dump.get("action"));
    }

    private Object executeHelper(final boolean validDirection,
            final String condition, final String action) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("navigatorBean :  {} ", getNavigatorValue());
            LOGGER.debug("validDirection :  {} ", validDirection);
            LOGGER.debug("condition :  {} ", condition);
            LOGGER.debug("action {}", action);
        }

        final JsfUtility jsfUtility = JsfUtility.getInstance();
        if (validDirection && jsfUtility.evaluateValueExpression(
                getFacesContext(), "#{" + condition + "}",
                Boolean.class)) {
            executeScriptItems();
            final FacesContext context = FacesContext.getCurrentInstance();
            final ELContext elContext = context.getELContext();
            final Application application = context.getApplication();
            final MethodExpression methodExpression = application
                    .getExpressionFactory().createMethodExpression(
                            FacesContext.getCurrentInstance().getELContext(),
                            "#{" + action + "}", null,
                            new Class[] { ActionEvent.class });
            methodExpression.invoke(elContext, new Object[0]);
        }
        return null;

    }

    @Override
    public Object execute() {
        LOGGER.debug("trigger execute");
        setNavigatorValue(getNavigatorAttribute());
//        this.setConditionValue(this.getConditionAttribute());
        setDirectionValue(getDirectionAttribute());
        executeHelper(validDirection(), getConditionAttribute(),
                getTriggerActionAttribute());
        return null;
    }

}

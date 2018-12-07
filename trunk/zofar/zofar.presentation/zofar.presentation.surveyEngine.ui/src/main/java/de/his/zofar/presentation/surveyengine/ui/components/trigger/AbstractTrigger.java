package de.his.zofar.presentation.surveyengine.ui.components.trigger;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.faces.component.FacesComponent;
import javax.faces.component.UIComponent;
import javax.faces.component.UINamingContainer;
import javax.faces.context.FacesContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.his.zofar.presentation.surveyengine.ui.components.common.Identificational;
import de.his.zofar.presentation.surveyengine.ui.components.common.Visible;
import de.his.zofar.presentation.surveyengine.ui.interfaces.INavigator;
import de.his.zofar.presentation.surveyengine.ui.interfaces.Task;
import de.his.zofar.presentation.surveyengine.ui.util.JsfUtility;

/**
 *
 * Parent class of all Triggers
 *
 * @author meisner
 *
 */
@FacesComponent(value = "org.zofar.Trigger")
public abstract class AbstractTrigger extends UINamingContainer
        implements Task, Identificational,Visible {

    /**
     *
     */
    private static final long serialVersionUID = -6371800205059902605L;
    public static final String DIRECTION_SAME = "same";
    public static final String DIRECTION_BACKWARD = "backward";
    public static final String DIRECTION_FORWARD = "forward";
    public static final String DIRECTION_UNKOWN = "unkown";

    private static final Logger LOGGER = LoggerFactory
            .getLogger(AbstractTrigger.class);

    private INavigator navigatorValue;
//    private String conditionValue;
    private String directionValue;

    protected AbstractTrigger() {
        super();
    }

	@Override
	@Deprecated
	public String getUID() {
		return getId();
	}

	@Override
	@Deprecated
	public Boolean visibleCondition() {
		return isRendered();
	}

//    /**
//     * evaluate a given condition as String to boolean
//     *
//     * @param condition
//     * @return true or false
//     */
//    protected Boolean eval(final String condition) {
//        if (condition == null) {
//            return null;
//        }
//        final FacesContext fc = FacesContext.getCurrentInstance();
//        final ELContext el = fc.getELContext();
//        final ExpressionFactory expressionFactory = ExpressionFactory
//                .newInstance();
//
//        final ValueExpression ve = expressionFactory.createValueExpression(el,
//                "#{" + condition + "}", Boolean.class);
//        final Boolean result = (Boolean) ve.getValue(el);
//        LOGGER.debug("eval ({}) to {}", condition, result);
//        return result;
//    }

    protected void executeScriptItems() {
        final List<ScriptItem> items = getScriptItems();
        if (items != null) {
            evalScriptItems(items);
        }
    }   

    /**
     * @return the List of ScriptItems defined in Composite
     */
    protected List<ScriptItem> getScriptItems() {
        if (getChildCount() == 0) {
            return null;
        }
        final Iterator<UIComponent> childIt = getChildren().iterator();
        final List<ScriptItem> back = new ArrayList<ScriptItem>();
        while (childIt.hasNext()) {
            final UIComponent child = childIt.next();
            if ((ScriptItem.class).isAssignableFrom(child.getClass())) {
                back.add((ScriptItem) child);
            }
        }
        return back;
    }   

    /**
     * evaluate given ScriptItems
     *
     * @param value
     *            of ScriptItems
     * @return
     */
    protected void evalScriptItems(final List<ScriptItem> scriptItems) {
        if (scriptItems == null) {
            return;
        }
        final Iterator<ScriptItem> itemIt = scriptItems.iterator();
        while (itemIt.hasNext()) {
            evalScriptItem(itemIt.next());
        }
    }

    /**
     * evaluate a given ScriptItem
     *
     * @param value
     *            of ScriptItem
     * @return
     */
    protected void evalScriptItem(final ScriptItem scriptItem) {
        if (scriptItem == null) {
            return;
        }
        final FacesContext fc = FacesContext.getCurrentInstance();

        final JsfUtility jsfUtility = JsfUtility.getInstance();
        final Object result = jsfUtility.evaluateValueExpression(fc, "#{"
                + scriptItem.getValueAttribute() + "}", Object.class);
        //LOGGER.info("evalScriptItem ({}) to {}", scriptItem, result);
    }

    public abstract Object execute();

    /**
     * @return the value of condition-Attribute defined in Composite
     */
    public String getConditionAttribute() {
        String value = String.valueOf(getAttributes().get("condition"));
        if (value.equals("null")) {
            value = "true";
        }
        return value;
    }

//    public String getConditionValue() {
//        return this.conditionValue;
//    }

    /**
     * @return the value of direction-Attribute defined in Composite
     */
    public String getDirectionAttribute() {
        String value = (String) getAttributes().get("direction");
        if (value == null) {
            value = DIRECTION_UNKOWN;
        }
        value = value.toLowerCase();
        value = value.trim();
        return value;
    }

    public String getDirectionValue() {
        return directionValue;
    }

    /**
     * @return the value of navigator-Attribute defined in Composite
     */
    public INavigator getNavigatorAttribute() {
        final INavigator value = (INavigator) getAttributes().get(
                "navigator");
        return value;
    }

    public INavigator getNavigatorValue() {
        return navigatorValue;
    }

//    public void setConditionValue(final Boolean condition) {
//        this.conditionValue = condition;
//    }

//    public void setConditionValue(final String condition) {
//        this.setConditionValue(this.eval(condition));
//    }

    public void setDirectionValue(final String direction) {
        directionValue = direction;
    }

    public void setNavigatorValue(final INavigator navigator) {
        navigatorValue = navigator;
    }

    /**
     * @return true if given movement-Direction equals to the configured
     *         direction
     */
    protected boolean validDirection() {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("navigator : {}", getNavigatorValue());
            LOGGER.debug("direction : {}", getDirectionValue());
        }

        if (getNavigatorValue() != null) {
            if ((getDirectionValue()).equals(DIRECTION_UNKOWN)) {
                return true;
            }
            if ((getDirectionValue()).equals(DIRECTION_FORWARD)
                    && (getNavigatorValue()).isForward()) {
                return true;
            }
            if ((getDirectionValue()).equals(DIRECTION_BACKWARD)
                    && (getNavigatorValue()).isBackward()) {
                return true;
            }
            if ((getDirectionValue()).equals(DIRECTION_SAME)
                    && (getNavigatorValue()).isSame()) {
                return true;
            }
        }
        return false;
    }
}

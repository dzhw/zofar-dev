/**
 *
 */
package de.his.zofar.presentation.surveyengine.ui.util;

import javax.el.ELContext;
import javax.el.ExpressionFactory;
import javax.el.ValueExpression;
import javax.faces.application.Application;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import de.his.zofar.presentation.surveyengine.ui.interfaces.IResponseDomain;

/**
 * singleton utility class for JSF functions that need a faces context instance.
 * 
 * @author le
 * 
 */
public class JsfUtility {

    private static final JsfUtility INSTANCE = new JsfUtility();

    private JsfUtility() {
        super();
    }

    public static final JsfUtility getInstance() {
        return INSTANCE;
    }

    /**
     * evaluate an EL expression against the given faces context. the return
     * type of the evaluated EL expression is defined by the type argument.
     *
     * @param context
     * @param expression
     * @param type
     * @return
     */
    @SuppressWarnings("unchecked")
    public <T> T evaluateValueExpression(final FacesContext context,
            final String expression, final Class<T> type) {
        final Application application = context.getApplication();
        final ELContext elContext = context.getELContext();
        final ExpressionFactory ef = application.getExpressionFactory();
        final ValueExpression valueExpression = ef.createValueExpression(
                elContext, expression, type);
        final T value = (T) valueExpression.getValue(elContext);

        return value;
    }
    
    
    public UIComponent getParentResponseDomain(final UIComponent parentComponent) {
        UIComponent parentResponseDomain = null;
        UIComponent parent = parentComponent;

        if (parent != null) {
	        if (parent instanceof IResponseDomain) {
	            parentResponseDomain = parent;
	        } else {
	            while (parent != null) {
	                parent = parent.getParent();
	                if (parent instanceof IResponseDomain) {
	                    parentResponseDomain = parent;
	                    break;
	                }
	            }
	        }
        }

        return parentResponseDomain;
    }

	public void setExpressionValue(final FacesContext context,final String expression,final Object value) {
        final Application application = context.getApplication();
        final ELContext elContext = context.getELContext();
        final ExpressionFactory ef = application.getExpressionFactory();
        final ValueExpression valueExpression = ef.createValueExpression(
                elContext, expression, value.getClass());
        valueExpression.setValue(elContext, value);
	}

}

/**
 *
 */
package de.his.zofar.presentation.surveyengine.util;

import javax.el.ELContext;
import javax.el.ExpressionFactory;
import javax.el.ValueExpression;
import javax.faces.application.Application;
import javax.faces.context.FacesContext;

/**
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

}

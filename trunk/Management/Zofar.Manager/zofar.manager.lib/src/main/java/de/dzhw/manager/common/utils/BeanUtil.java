package de.dzhw.manager.common.utils;

import javax.el.ELContext;
import javax.el.ELException;
import javax.el.ExpressionFactory;
import javax.el.ValueExpression;
import javax.faces.application.Application;
import javax.faces.context.FacesContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.dzhw.manager.modules.interfaces.Module;

public final class BeanUtil {
	
	private static final Logger LOGGER = LoggerFactory
			.getLogger(BeanUtil.class);
	
	private static final BeanUtil INSTANCE = new BeanUtil();

	private BeanUtil() {
		super();
	}
	
	public static synchronized BeanUtil getInstance(){
		return INSTANCE;
	}
	
	public Module findModule(final FacesContext context,final String name){
		return evaluateValueExpression(context,"#{" + name + "}", Module.class);
	}
	
	public <T> T findBean(final FacesContext context,
			final String expression, final Class<T> type){
		return evaluateValueExpression(context,expression,type);
	}
	
	@SuppressWarnings("unchecked")
	private <T> T evaluateValueExpression(final FacesContext context,
			final String expression, final Class<T> type) {
		if(context == null) return null;
		try {
			final Application application = context.getApplication();
			final ELContext elContext = context.getELContext();
			final ExpressionFactory ef = application.getExpressionFactory();
			final ValueExpression valueExpression = ef.createValueExpression(
					elContext, expression, type);
			final T value = (T) valueExpression.getValue(elContext);

			return value;
		} catch (ELException e) {
			LOGGER.error("Bean not found : {}", expression);
		}
		return null;
	}

}

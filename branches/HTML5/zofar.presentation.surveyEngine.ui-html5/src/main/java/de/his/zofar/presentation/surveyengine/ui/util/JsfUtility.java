/**
 *
 */
package de.his.zofar.presentation.surveyengine.ui.util;

import java.util.ArrayList;
import java.util.List;

import javax.el.ELContext;
import javax.el.ExpressionFactory;
import javax.el.ValueExpression;
import javax.faces.application.Application;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import com.sun.faces.facelets.compiler.UIInstructions;

import de.his.zofar.presentation.surveyengine.ui.components.text.UIText;
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
	public <T> T evaluateValueExpression(final FacesContext context, final String expression, final Class<T> type) {
		final Application application = context.getApplication();
		final ELContext elContext = context.getELContext();
		final ExpressionFactory ef = application.getExpressionFactory();
		final ValueExpression valueExpression = ef.createValueExpression(elContext, expression, type);
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
	
	public boolean hasChildOfType(final UIComponent component, final Class<?> clazz){
		if(component == null) return false;
		if(clazz == null) return false;
		final List<UIComponent> childs = component.getChildren();
		if((childs != null)&&(!childs.isEmpty())){
			for(final UIComponent child : childs){
				if(clazz.isAssignableFrom(child.getClass()))return true;
				else return this.hasChildOfType(child, clazz);
			}
		}
		return false;
	}
	
	public List<UIComponent> getVisibleChildrensOfType(final UIComponent component, final Class<?> clazz){
		if(component == null) return null;
		if(clazz == null) return null;
		final List<UIComponent> childs = component.getChildren();
		final List<UIComponent> back = new ArrayList<UIComponent>();
		if((childs != null)&&(!childs.isEmpty())){
			for(final UIComponent child : childs){
				if(!child.isRendered())continue;
				if(clazz.isAssignableFrom(child.getClass()))back.add(child);
				else back.addAll(getVisibleChildrensOfType(child,clazz));
			}
		}
		return back;
	}

	public void setExpressionValue(final FacesContext context, final String expression, final Object value) {
		final Application application = context.getApplication();
		final ELContext elContext = context.getELContext();
		final ExpressionFactory ef = application.getExpressionFactory();
		final ValueExpression valueExpression = ef.createValueExpression(elContext, expression, value.getClass());
		valueExpression.setValue(elContext, value);
	}

	public <V> V getAttribute(final UIComponent component, final String parameter, final V defaultValue) {
		@SuppressWarnings("unchecked")
		final V value = (V) component.getAttributes().get(parameter);
		if (value != null) {
			return value;
		}
		return defaultValue;
	}
	
	public UIComponent getComposite(final UIComponent component){
		if(component == null)return null;
		return component.getFacet("javax.faces.component.COMPOSITE_FACET_NAME");
	}
	
	public String getFieldAsString(final FacesContext context, final UIComponent component,final String field){
		final String fieldString = (String) component.getAttributes().get(field);
		return evaluateValueExpression(context, fieldString, String.class);
	}
	
	/**
	 * @param header
	 * @return
	 */
	public String getTextComponentAsString(final FacesContext context,final UIComponent header) {
		final StringBuffer text = new StringBuffer();

		if (header.isRendered()) {
			if (header instanceof UIText) {
				final Object value = header.getAttributes().get("value");
				if (value != null) {
					text.append(value);
				}
				for (final UIComponent child : header.getChildren()) {
					if (child.isRendered()) {
						text.append(getTextComponentAsString(context,child));
					}
				}
			}
			if (UIInstructions.class.isAssignableFrom(header.getClass())) {
				text.append(evaluateValueExpression(context, String.valueOf(header), String.class));
			}
		}
		return text.toString();
	}

}

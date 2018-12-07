/*
 *
 */
package de.his.zofar.presentation.surveyengine.ui.renderer.answers.multiplechoice.options;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.Renderer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.his.zofar.presentation.surveyengine.ui.util.JsfUtility;

/**
 * @author dick
 *
 */
public abstract class ZofarCheckboxOptionRenderer extends Renderer {

	private static final Logger LOGGER = LoggerFactory.getLogger(ZofarCheckboxOptionRenderer.class);

	protected ZofarCheckboxOptionRenderer() {
		super();
	}

	@Override
	public boolean getRendersChildren() {
		return false;
	}
	
	@Override
	public void encodeChildren(final FacesContext context, final UIComponent component) throws IOException {

		if (!component.isRendered()) {
			return;
		}

//		final UIComponent parent = (UIComponent) component.getAttributes().get("parentResponseDomain");
//		if (parent == null) {
//			return;
//		}	
//		final ResponseWriter writer = context.getResponseWriter();
		this.encodeItem(context, component);
//		this.encodeInput(context, component);
	}
	
	

	@Override
	public void decode(final FacesContext context, final UIComponent component) {

		if (!component.isRendered()) {
			return;
		}

		String clientId = null;

		if (clientId == null) {
			clientId = component.getClientId(context);
		}
		assert (clientId != null);
		// Convert the new value

		final Map<String, String> requestParameterMap = context.getExternalContext().getRequestParameterMap();

		final boolean isChecked = this.isChecked(requestParameterMap.get(clientId));
		this.setSubmittedValue(component, isChecked);
	}

	public void setSubmittedValue(final UIComponent component, final Object value) {

		if (component instanceof UIInput) {
			((UIInput) component).setValue(value);
			if (ZofarCheckboxOptionRenderer.LOGGER.isDebugEnabled()) {
				ZofarCheckboxOptionRenderer.LOGGER.debug("set checkbox value: {}", value);
			}
		}

	}

	/**
	 * @param value
	 *            the submitted value
	 * @return "true" if the component was checked, otherise "false"
	 */
	private boolean isChecked(final String value) {

		if (ZofarCheckboxOptionRenderer.LOGGER.isDebugEnabled()) {
			ZofarCheckboxOptionRenderer.LOGGER.debug("raw value from request map: {}", value);
		}

		return value != null;
	}

	protected boolean hasInput(final FacesContext context, final UIComponent component) throws IOException {

		if (!component.isRendered()) {
			return false;
		}

		final UIComponent parent = JsfUtility.getInstance().getParentResponseDomain(component);
		if (parent == null) {
			return false;
		}
		return true;
	}

	protected void encodeInputMatrix(final FacesContext context, final UIComponent component) throws IOException {
		if (!component.isRendered()) {
			return;
		}

		final UIComponent parent = component.getParent();
		if (parent == null) {
			return;
		}
		final Boolean isSelected = Boolean.valueOf(component.getAttributes().get("value").toString());
		final Boolean isExclusive = Boolean.valueOf(component.getAttributes().get("exclusive").toString());

		final ResponseWriter writer = context.getResponseWriter();
		writer.startElement("input", component);
		writer.writeAttribute("class", "custom-control-input", null);
		writer.writeAttribute("type", "checkbox", null);
		writer.writeAttribute("name", component.getClientId(), null);
		writer.writeAttribute("id", component.getClientId(), null);
		writer.writeAttribute("value", component.getId(), "value");
		final StringBuffer trigger = new StringBuffer();
		final StringBuffer attachedScripts = new StringBuffer();
		if (this.hasOpenQuestion(context, component)) {
			final List<UIComponent> openQuestions = this.getOpenQuestion(context, component);
			for (final UIComponent openQuestion : openQuestions) {
				final String script = "zofar_multipleChoice_register_exclusive_attached('" + parent.getClientId() + "', '" + component.getClientId() + "', '" + openQuestion.getClientId() + ":aoq'," + isExclusive + ");\n";
				attachedScripts.append(script + "\n");
				trigger.append("zofar_multipleChoice_triggerCheck('" + component.getClientId() + "','" + openQuestion.getClientId() + ":" + openQuestion.getAttributes().get("inputId") + "');");
			}
		}
		if (isExclusive) {
			trigger.append("zofar_multipleChoice_triggerExclusive('" + component.getClientId() + "','" + parent.getClientId() + "');");
		} else {
			trigger.append("zofar_multipleChoice_triggerNotExclusive('" + component.getClientId() + "','" + parent.getClientId() + "');");

		}

		if (trigger.length() > 0) {
			writer.writeAttribute("onchange", "javascript:" + trigger.toString(), null);
		}

		if (isSelected) {
			writer.writeAttribute("checked", "checked", null);
		}
		writer.endElement("input");
		writer.startElement("script", component);
		writer.writeAttribute("type", "text/javascript", null);
		writer.write(attachedScripts.toString());
		writer.endElement("script");
		this.insertExclusiveJavaScript(context, component, parent);
		return;
	}
	protected abstract void encodeItem(final FacesContext context, final UIComponent component) throws IOException;
	protected void encodeInput(final ResponseWriter writer,final FacesContext context, final UIComponent component) throws IOException {
		if (!component.isRendered()) {
			return;
		}
		final UIComponent parent = JsfUtility.getInstance().getParentResponseDomain(component);
		if (parent == null) {
			return;
		}

		final Boolean isSelected = Boolean.valueOf(component.getAttributes().get("value").toString());
		final Boolean isExclusive = Boolean.valueOf(component.getAttributes().get("exclusive").toString());
		if (ZofarCheckboxOptionRenderer.LOGGER.isDebugEnabled()) {
			ZofarCheckboxOptionRenderer.LOGGER.debug("selectedValue {}", isSelected);
		}

		writer.startElement("input", component);
		writer.writeAttribute("class", "custom-control-input", null);
		writer.writeAttribute("type", "checkbox", null);
		writer.writeAttribute("name", component.getClientId(), null);
		writer.writeAttribute("id", component.getClientId(), null);
		writer.writeAttribute("value", component.getId(), "value");
		final StringBuffer trigger = new StringBuffer();
		if (this.hasOpenQuestion(context, component)) {
			final List<UIComponent> openQuestions = this.getOpenQuestion(context, component);
			for (final UIComponent openQuestion : openQuestions) {
				trigger.append("zofar_multipleChoice_triggerCheck('" + component.getClientId() + "','" + openQuestion.getClientId() + ":" + openQuestion.getAttributes().get("inputId") + "');");
			}
		}
		if (isExclusive) {
			trigger.append("zofar_multipleChoice_triggerExclusive('" + component.getClientId() + "','" + parent.getClientId() + "');");
		} else {
			trigger.append("zofar_multipleChoice_triggerNotExclusive('" + component.getClientId() + "','" + parent.getClientId() + "');");

		}

		if (trigger.length() > 0) {
			writer.writeAttribute("onchange", "javascript:" + trigger.toString(), null);
		}

		if (isSelected) {
			writer.writeAttribute("checked", "checked", null);
		}
		writer.endElement("input");
		final UIComponent responsedomain = JsfUtility.getInstance().getParentResponseDomain(component);
		this.insertExclusiveJavaScript(context, component, responsedomain);
		return;
	}



	protected boolean hasLabel(final FacesContext context, final UIComponent component) throws IOException {

		if (!component.isRendered()) {
			return false;
		}

		final UIComponent parent = JsfUtility.getInstance().getParentResponseDomain(component);
		if (parent == null) {
			return false;
		}

		final String itemLabel = (String) component.getAttributes().get("label");

		final String label = JsfUtility.getInstance().evaluateValueExpression(context, itemLabel, String.class);

		if (label.equals("")) {
			return false;
		}

		return true;
	}

	protected void encodeLabel(final FacesContext context, final UIComponent component) throws IOException {

		if (!component.isRendered()) {
			return;
		}

		final UIComponent parent = JsfUtility.getInstance().getParentResponseDomain(component);
		if (parent == null) {
			return;
		}

		final String itemLabel = (String) component.getAttributes().get("label");

		final String label = JsfUtility.getInstance().evaluateValueExpression(context, itemLabel, String.class);

		final ResponseWriter writer = context.getResponseWriter();
		writer.startElement("span", component);
		writer.writeAttribute("class", "custom-control-indicator", null);
		writer.endElement("span");
		
		writer.startElement("span", component);
		writer.writeAttribute("class", "custom-control-description", null);
		writer.write(label);
		writer.endElement("span");
		return;
	}

	protected boolean hasValueLabel(final FacesContext context, final UIComponent component) throws IOException {

		if (!component.isRendered()) {
			return false;
		}

		final UIComponent parent = JsfUtility.getInstance().getParentResponseDomain(component);
		if (parent == null) {
			return false;
		}

		final Boolean showValues = (Boolean) parent.getAttributes().get("showValues");

		if ((showValues == null) || (!showValues)) {
			return false;
		}

		final String itemValue = (String) component.getAttributes().get("value");

		final String label = JsfUtility.getInstance().evaluateValueExpression(context, itemValue, String.class);

		if (label.equals("")) {
			return false;
		}
		return true;
	}

	protected void encodeValueLabel(final FacesContext context, final UIComponent component) throws IOException {

		if (!component.isRendered()) {
			return;
		}

		final UIComponent parent = JsfUtility.getInstance().getParentResponseDomain(component);
		if (parent == null) {
			return;
		}

		final Boolean showValues = (Boolean) parent.getAttributes().get("showValues");

		if ((showValues == null) || (!showValues)) {
			return;
		}

		final String itemValue = (String) component.getAttributes().get("value");

		final String label = JsfUtility.getInstance().evaluateValueExpression(context, itemValue, String.class);

		if (label.equals("")) {
			return;
		}

		final ResponseWriter writer = context.getResponseWriter();

		writer.startElement("label", component);
		writer.writeAttribute("for", component.getClientId(), null);
		writer.write("" + label + "");
		writer.endElement("label");
		return;
	}

	protected boolean hasOpenQuestion(final FacesContext context, final UIComponent component) throws IOException {
		if (!component.isRendered()) {
			return false;
		}
		for (@SuppressWarnings("unused")
		final UIComponent child : component.getChildren()) {
			return true;
		}
		return false;
	}

	private List<UIComponent> getOpenQuestion(final FacesContext context, final UIComponent component) throws IOException {
		if (!component.isRendered()) {
			return null;
		}
		List<UIComponent> openQuestions = null;
		for (@SuppressWarnings("unused")
		final UIComponent child : component.getChildren()) {
			if (openQuestions == null) {
				openQuestions = new ArrayList<UIComponent>();
			}
			openQuestions.add(child);
		}
		return openQuestions;
	}

	protected void encodeOpenQuestion(final FacesContext context, final UIComponent component,final Boolean alignAttached) throws IOException {
		if (!component.isRendered()) {
			return;
		}
		final ResponseWriter writer = context.getResponseWriter();
		for (final UIComponent child : component.getChildren()) {
			if(alignAttached){
				writer.startElement("div", component);
				writer.writeAttribute("class", "alignAttached", null);
			}
			child.encodeAll(context);

			writer.startElement("script", component);
			writer.writeAttribute("type", "text/javascript", null);
			final UIComponent responsedomain = JsfUtility.getInstance().getParentResponseDomain(component);
			final String script = "zofar_singleChoice_register_open('" + responsedomain.getClientId() + "', '" + child.getClientId() + ":" + child.getAttributes().get("inputId") + "');\n";
			writer.write(script);
			writer.endElement("script");

			if(alignAttached)writer.endElement("div");
		}
		return;
	}

	private void insertExclusiveJavaScript(final FacesContext context, final UIComponent component, final UIComponent responsedomain) throws IOException {
		final ResponseWriter writer = context.getResponseWriter();
		writer.startElement("script", component);
		writer.writeAttribute("type", "text/javascript", null);
		final Boolean isExclusive = Boolean.valueOf(component.getAttributes().get("exclusive").toString());
		final String script = "zofar_multipleChoice_register_exclusive('" + responsedomain.getClientId() + "', '" + component.getClientId() + "'," + isExclusive + ");\n";
		writer.write(script);
		writer.endElement("script");
	}

}

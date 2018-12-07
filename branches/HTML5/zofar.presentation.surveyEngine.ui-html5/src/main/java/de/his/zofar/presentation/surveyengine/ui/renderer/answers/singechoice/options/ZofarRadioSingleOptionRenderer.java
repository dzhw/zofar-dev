/*
 *
 */
package de.his.zofar.presentation.surveyengine.ui.renderer.answers.singechoice.options;

import java.io.IOException;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.Renderer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.his.zofar.presentation.surveyengine.ui.renderer.answers.ZofarResponseDomainRenderer;
import de.his.zofar.presentation.surveyengine.ui.util.JsfUtility;

public abstract class ZofarRadioSingleOptionRenderer extends Renderer {

	private static final Logger LOGGER = LoggerFactory.getLogger(ZofarRadioSingleOptionRenderer.class);

	protected ZofarRadioSingleOptionRenderer() {
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

		this.encodeItem(context, component);
	}
	
	protected abstract void encodeItem(final FacesContext context, final UIComponent component) throws IOException;
	
	protected void encodeInput(final ResponseWriter writer, final UIComponent component) throws IOException {

		if (!component.isRendered()) {
			return;
		}

//		final UIComponent parent = (UIComponent) component.getAttributes().get("parentResponseDomain");
		final UIComponent parent = JsfUtility.getInstance().getParentResponseDomain(component);
		if (parent == null) {
			return;
		}

		final String selectedValue = (String) parent.getAttributes().get("value");

		writer.startElement("input", component);
		writer.writeAttribute("type", "radio", null);
		writer.writeAttribute("name", parent.getClientId(), null);
		writer.writeAttribute("id", component.getClientId(), null);
		writer.writeAttribute("value", component.getId(), "value");
		writer.writeAttribute("onchange", "javascript:zofar_singleChoice_triggerRadio('" + parent.getClientId() + "','" + component.getClientId() + "');", null);

		if ((component.getId() != null) && !component.getId().isEmpty() && component.getId().equals(selectedValue)) {
			writer.writeAttribute("checked", "checked", null);
		}
		
		Boolean isMissing = (Boolean) component.getAttributes().get("missing");
		if (isMissing == null) {
			isMissing = false;
		}
		writer.writeAttribute("data-missing", isMissing, null);
		
		String labelPosition = (String) parent.getAttributes().get("labelPosition");
		if (labelPosition == null) {
			labelPosition = ZofarResponseDomainRenderer.LABEL_POSITION_RIGHT;
		}
//		writer.writeAttribute("data-label-pos", labelPosition, null);
		
		writer.writeAttribute("class", "custom-control-input", null);

		
		writer.endElement("input");
		return;
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
}

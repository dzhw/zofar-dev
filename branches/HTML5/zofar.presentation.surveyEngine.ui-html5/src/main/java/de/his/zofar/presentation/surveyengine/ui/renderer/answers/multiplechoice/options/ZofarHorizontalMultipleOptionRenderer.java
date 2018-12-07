/*
 *
 */
package de.his.zofar.presentation.surveyengine.ui.renderer.answers.multiplechoice.options;

import java.io.IOException;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.FacesRenderer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.his.zofar.presentation.surveyengine.ui.components.answer.options.MultipleOption;
import de.his.zofar.presentation.surveyengine.ui.renderer.answers.ZofarResponseDomainRenderer;
import de.his.zofar.presentation.surveyengine.ui.util.JsfUtility;

/**
 * @author dick
 *
 */
@FacesRenderer(componentFamily = MultipleOption.COMPONENT_FAMILY, rendererType = ZofarHorizontalMultipleOptionRenderer.RENDERER_TYPE)
public class ZofarHorizontalMultipleOptionRenderer extends ZofarCheckboxOptionRenderer {

	public static final String RENDERER_TYPE = "org.zofar.HorizontalMultipleOption";

	private static final Logger LOGGER = LoggerFactory.getLogger(ZofarHorizontalMultipleOptionRenderer.class);


	public ZofarHorizontalMultipleOptionRenderer() {
		super();
	}

	@Override
	public boolean getRendersChildren() {
		return true;
	}


	@Override
	public void encodeBegin(final FacesContext context, final UIComponent component) throws IOException {

		if (!component.isRendered()) {
			return;
		}

		final UIComponent parent = JsfUtility.getInstance().getParentResponseDomain(component);
		if (parent == null) {
			return;
		}
		
		final ResponseWriter writer = context.getResponseWriter();
		writer.startElement("div", component);
		writer.writeAttribute("class", "custom-form custom-form-checkbox", null);
		writer.startElement("div", component);
		writer.writeAttribute("class", "custom-control custom-checkbox", null);
	}

	protected void encodeItem(final FacesContext context, final UIComponent component) throws IOException {
		if (!component.isRendered()) {
			return;
		}

		final UIComponent parent = JsfUtility.getInstance().getParentResponseDomain(component);
		if (parent == null) {
			return;
		}
		
		String labelPosition = (String) parent.getAttributes().get("labelPosition");
		
		if (labelPosition == null) {
			labelPosition = ZofarResponseDomainRenderer.LABEL_POSITION_RIGHT;
		}
		
		Boolean isMissing = (Boolean) component.getAttributes().get("missing");
		if (isMissing == null) {
			isMissing = false;
		}
		
		Boolean alignAttached = (Boolean) parent.getAttributes().get("alignAttached");
		if (alignAttached == null) {
			alignAttached = false;
		}
		
		String labelStr = JsfUtility.getInstance().getFieldAsString(context, component, "label");
		final String valueStr = JsfUtility.getInstance().getFieldAsString(context, component, "value");
		
		
		final ResponseWriter writer = context.getResponseWriter();
		writer.startElement("label", component);
		writer.writeAttribute("data-label-pos", labelPosition, null);
		
		if(labelPosition.equals(ZofarResponseDomainRenderer.LABEL_POSITION_LEFT)){
			writer.startElement("span", component);
			writer.writeAttribute("data-missing", isMissing, null);
			writer.writeAttribute("data-label-type", "description", null);
			writer.writeAttribute("class", "custom-control-description", null);
			writer.write("" + labelStr + "");
			
			this.encodeOpenQuestion(context, component, alignAttached);
			writer.endElement("span");

			writer.startElement("span", component);
			writer.writeAttribute("data-missing", isMissing, null);
			writer.writeAttribute("data-label-type", "indicator", null);
			writer.writeAttribute("class", "custom-control-indicator", null);
			writer.write(" ");
			writer.endElement("span");
			
			this.encodeInput(writer,context, component);
		}
		else{
			this.encodeInput(writer, context, component);
			
			writer.startElement("span", component);
			writer.writeAttribute("data-missing", isMissing, null);
			writer.writeAttribute("data-label-type", "indicator", null);
			writer.writeAttribute("class", "custom-control-indicator", null);
			writer.write(" ");
			writer.endElement("span");
			
			writer.startElement("span", component);
			writer.writeAttribute("data-missing", isMissing, null);
			writer.writeAttribute("data-label-type", "description", null);
			writer.writeAttribute("class", "custom-control-description", null);
			writer.write("" + labelStr + "");
			
			this.encodeOpenQuestion(context, component, alignAttached);
			
			writer.endElement("span");
		}	
		writer.endElement("label");
	}

	@Override
	public void encodeEnd(final FacesContext context, final UIComponent component) throws IOException {
		final ResponseWriter writer = context.getResponseWriter();
		final UIComponent parent = JsfUtility.getInstance().getParentResponseDomain(component);
		if (parent == null) {
			return;
		}
		writer.endElement("div");
		writer.endElement("div");
	}


}

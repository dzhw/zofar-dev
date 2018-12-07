package de.his.zofar.presentation.surveyengine.ui.renderer.answers.singechoice.options;

import java.io.IOException;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.FacesRenderer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.his.zofar.presentation.surveyengine.ui.components.answer.options.SingleOption;
import de.his.zofar.presentation.surveyengine.ui.interfaces.ISequence;
import de.his.zofar.presentation.surveyengine.ui.renderer.answers.ZofarResponseDomainRenderer;
import de.his.zofar.presentation.surveyengine.ui.util.JsfUtility;

@FacesRenderer(componentFamily = SingleOption.COMPONENT_FAMILY, rendererType = ZofarVerticalRadioSingleOptionRenderer.RENDERER_TYPE)
public class ZofarVerticalRadioSingleOptionRenderer extends ZofarRadioSingleOptionRenderer {

	public static final String RENDERER_TYPE = "org.zofar.VerticalRadioSingleOption";

	private static final Logger LOGGER = LoggerFactory.getLogger(ZofarVerticalRadioSingleOptionRenderer.class);

	public ZofarVerticalRadioSingleOptionRenderer() {
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
//
//		final UIComponent parent = (UIComponent) component.getAttributes().get("parentResponseDomain");
//		if (parent == null) {
//			return;
//		}
		
		final ResponseWriter writer = context.getResponseWriter();
		writer.startElement("div", component);
		writer.writeAttribute("class", "custom-control custom-radio", null);
	}

	protected void encodeItem(final FacesContext context, final UIComponent component) throws IOException {
		if (!component.isRendered()) {
			return;
		}

//		final UIComponent parent = (UIComponent) component.getAttributes().get("parentResponseDomain");
		final UIComponent parent = JsfUtility.getInstance().getParentResponseDomain(component);
		if (parent == null) {
			return;
		}
		
		String labelPosition = (String) parent.getAttributes().get("labelPosition");
		
		if (labelPosition == null) {
			labelPosition = ZofarResponseDomainRenderer.LABEL_POSITION_RIGHT;
		}
		
		Boolean showValues = (Boolean) parent.getAttributes().get("showValues");
		if (showValues == null) {
			showValues = false;
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

		if(showValues && !isMissing){
			labelStr = labelStr.trim();
			if(labelStr.equals(""))labelStr = valueStr;
			else labelStr = "("+valueStr+") "+labelStr;
		}
		
		final ResponseWriter writer = context.getResponseWriter();
		writer.startElement("label", component);
		if(!((SingleOption)component).isMissing())writer.writeAttribute("class", "pipe-zofar-item", null);

		writer.writeAttribute("data-label-pos", labelPosition, null);
		
		if(((ISequence.class).isAssignableFrom(component.getClass()))){
			final Object sequenceId = ((ISequence)component).getSequenceId();
			if(sequenceId != null){
				writer.writeAttribute("data-sequence", sequenceId, null);
			}
		}
		
		
		
		if(labelPosition.equals(ZofarResponseDomainRenderer.LABEL_POSITION_LEFT)){
			writer.startElement("span", component);
			writer.writeAttribute("data-missing", isMissing, null);
			writer.writeAttribute("data-label-type", "description", null);
//			writer.writeAttribute("class", "custom-control-description", null);
			String classes = "custom-control-description";
			if(!((SingleOption)component).isShowLabelFlag()){
				classes = classes + " hidden-md-up";
			}
			writer.writeAttribute("class", classes, null);
			writer.startElement("span", component);
			writer.write("" + labelStr + "");
			writer.endElement("span");
			this.encodeOpenQuestion(context, component, alignAttached);
			writer.endElement("span");

			writer.startElement("span", component);
			writer.writeAttribute("data-missing", isMissing, null);
			writer.writeAttribute("data-label-type", "indicator", null);
			writer.writeAttribute("class", "custom-control-indicator", null);
			writer.write(" ");
			writer.endElement("span");
			
			this.encodeInput(writer, component);
		}
		else{
			this.encodeInput(writer, component);
			
			writer.startElement("span", component);
			writer.writeAttribute("data-missing", isMissing, null);
			writer.writeAttribute("data-label-type", "indicator", null);
			writer.writeAttribute("class", "custom-control-indicator", null);
			writer.write(" ");
			writer.endElement("span");
			
			writer.startElement("span", component);
			writer.writeAttribute("data-missing", isMissing, null);
			writer.writeAttribute("data-label-type", "description", null);
//			writer.writeAttribute("class", "custom-control-description", null);
			String classes = "custom-control-description";
			if(!((SingleOption)component).isShowLabelFlag()){
				classes = classes + " hidden-sm-up";
			}
			writer.writeAttribute("class", classes, null);
			writer.startElement("span", component);
			writer.write("" + labelStr + "");
			writer.endElement("span");
			
			this.encodeOpenQuestion(context, component, alignAttached);
			
			writer.endElement("span");
		}	
		writer.endElement("label");
	}

	@Override
	public void encodeEnd(final FacesContext context, final UIComponent component) throws IOException {

		if (!component.isRendered()) {
			return;
		}
		
//		final UIComponent parent = (UIComponent) component.getAttributes().get("parentResponseDomain");
		final UIComponent parent = JsfUtility.getInstance().getParentResponseDomain(component);
		if (parent == null) {
			return;
		}
		final ResponseWriter writer = context.getResponseWriter();
		writer.endElement("div");
	}

}

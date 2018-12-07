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
import de.his.zofar.presentation.surveyengine.ui.interfaces.ISequence;
import de.his.zofar.presentation.surveyengine.ui.renderer.answers.ZofarResponseDomainRenderer;
import de.his.zofar.presentation.surveyengine.ui.util.JsfUtility;

/**
 * @author dick
 *
 */
@FacesRenderer(componentFamily = MultipleOption.COMPONENT_FAMILY, rendererType = ZofarVerticalMultipleOptionRenderer.RENDERER_TYPE)
public class ZofarVerticalMultipleOptionRenderer extends ZofarCheckboxOptionRenderer {

	public static final String RENDERER_TYPE = "org.zofar.VerticalMultipleOption";

	private static final Logger LOGGER = LoggerFactory.getLogger(ZofarVerticalMultipleOptionRenderer.class);

	public ZofarVerticalMultipleOptionRenderer() {
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

//		VD
		final ResponseWriter writer = context.getResponseWriter();
//		writer.startElement("div", component);
//		writer.writeAttribute("class", "custom-form custom-form-checkbox", null);
		writer.startElement("div", component);
		writer.writeAttribute("class", "custom-control custom-checkbox", null);
		// writer.startElement("label", component);

		// final UIComponent parent =
		// JsfUtility.getInstance().getParentResponseDomain(component);
		// if (parent == null) {
		// return;
		// }
		//
		// Boolean alignAttached = (Boolean)
		// parent.getAttributes().get("alignAttached");
		// if (alignAttached == null) {
		// alignAttached = false;
		// }
		//
		// Boolean isMissing = (Boolean) component.getAttributes().get("missing");
		// if (isMissing == null) {
		// isMissing = false;
		// }
		//
		// final String labelPosition = (String)
		// parent.getAttributes().get("labelPosition");
		//
		// if ((labelPosition != null) &&
		// labelPosition.equals(ZofarResponseDomainRenderer.LABEL_POSITION_LEFT)) {
		// } else {
		// this.encodeInput(writer,context,component);
		// this.encodeValueLabel(context, component);
		// }

	}

	// protected void encodeItem(final FacesContext context, final UIComponent
	// component) throws IOException {
	// if (!component.isRendered()) {
	// return;
	// }
	// final UIComponent parent =
	// JsfUtility.getInstance().getParentResponseDomain(component);
	// if (parent == null) {
	// return;
	// }
	//
	// String labelPosition = (String) parent.getAttributes().get("labelPosition");
	//
	// if (labelPosition == null) {
	// labelPosition = ZofarResponseDomainRenderer.LABEL_POSITION_RIGHT;
	// }
	//
	// Boolean isMissing = (Boolean) component.getAttributes().get("missing");
	// if (isMissing == null) {
	// isMissing = false;
	// }
	//
	// Boolean alignAttached = (Boolean)
	// parent.getAttributes().get("alignAttached");
	// if (alignAttached == null) {
	// alignAttached = false;
	// }
	//
	// String labelStr = JsfUtility.getInstance().getFieldAsString(context,
	// component, "label");
	//
	// final ResponseWriter writer = context.getResponseWriter();
	//// writer.startElement("label", component);
	// writer.writeAttribute("data-label-pos", labelPosition, null);
	//
	// if (labelPosition.equals(ZofarResponseDomainRenderer.LABEL_POSITION_LEFT)) {
	// writer.startElement("span", component);
	// writer.writeAttribute("data-missing", isMissing, null);
	// // writer.writeAttribute("data-label-type", "description", null);
	// writer.writeAttribute("class", "custom-control-description", null);
	// writer.write("" + labelStr + "");
	//
	// this.encodeOpenQuestion(context, component, alignAttached);
	// writer.endElement("span");
	//
	// writer.startElement("span", component);
	// writer.writeAttribute("data-missing", isMissing, null);
	// writer.writeAttribute("class", "custom-control-indicator", null);
	// writer.write(" ");
	// writer.endElement("span");
	//
	// this.encodeInput(writer, context, component);
	// } else {
	// this.encodeInput(writer, context, component);
	//
	// writer.startElement("span", component);
	// writer.writeAttribute("data-missing", isMissing, null);
	// writer.writeAttribute("class", "custom-control-indicator", null);
	// writer.write(" ");
	// writer.endElement("span");
	//
	// writer.startElement("span", component);
	// writer.writeAttribute("data-missing", isMissing, null);
	// writer.writeAttribute("data-label-type", "description", null);
	// writer.writeAttribute("class", "custom-control-description", null);
	// writer.write("" + labelStr + "");
	//
	// this.encodeOpenQuestion(context, component, alignAttached);
	//
	// writer.endElement("span");
	// }
	// }

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

		if (showValues && !isMissing) {
			labelStr = labelStr.trim();
			if (labelStr.equals(""))
				labelStr = valueStr;
			else
				labelStr = "(" + valueStr + ") " + labelStr;
		}

		final ResponseWriter writer = context.getResponseWriter();
		writer.startElement("label", component);
		writer.writeAttribute("data-label-pos", labelPosition, null);

		if (((ISequence.class).isAssignableFrom(component.getClass()))) {
			final Object sequenceId = ((ISequence) component).getSequenceId();
			if (sequenceId != null) {
				writer.writeAttribute("data-sequence", sequenceId, null);
			}
		}

		if (labelPosition.equals(ZofarResponseDomainRenderer.LABEL_POSITION_LEFT)) {
			writer.startElement("span", component);
			writer.writeAttribute("data-missing", isMissing, null);
			writer.writeAttribute("data-label-type", "description", null);
			// writer.writeAttribute("class", "custom-control-description", null);
			String classes = "custom-control-description";
			if (!((MultipleOption) component).isShowLabelFlag()) {
				classes = classes + " hidden-md-up";
			}
			writer.writeAttribute("class", classes, null);

//			writer.write("" + labelStr + "");
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

			this.encodeInput(writer, context, component);
		} else {
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
//			writer.writeAttribute("class", "custom-control-description", null);
			String classes = "custom-control-description";
			if(!((MultipleOption)component).isShowLabelFlag()){
				classes = classes + " hidden-md-up";
			}
			writer.writeAttribute("class", classes, null);
//			writer.write("" + labelStr + "");
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

		final UIComponent parent = JsfUtility.getInstance().getParentResponseDomain(component);
		if (parent == null) {
			return;
		}

		// final String labelPosition = (String)
		// parent.getAttributes().get("labelPosition");
		//
		// if ((labelPosition != null) &&
		// labelPosition.equals(ZofarResponseDomainRenderer.LABEL_POSITION_LEFT)) {
		//
		// } else {
		// }
		final ResponseWriter writer = context.getResponseWriter();
		// writer.endElement("label");
		// writer.endElement("div");
		writer.endElement("div");
	}
}

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

/**
 * @author meisner
 * 
 */
@FacesRenderer(componentFamily = MultipleOption.COMPONENT_FAMILY, rendererType = ZofarHorizontalMultipleOptionRenderer.RENDERER_TYPE)
public class ZofarHorizontalMultipleOptionRenderer extends
		ZofarCheckboxOptionRenderer{

	public static final String RENDERER_TYPE = "org.zofar.HorizontalMultipleOption";

	private static final Logger LOGGER = LoggerFactory
			.getLogger(ZofarHorizontalMultipleOptionRenderer.class);


	private final static String INPUTCLASSES = "zo-ao-input zo-ao-input-horizontal ";
	private final static String VALUECLASSES = "zo-ao-value zo-ao-value-horizontal ";
	private final static String LABELCLASSES = "zo-ao-label zo-ao-label-horizontal ";
	private final static String OPENCLASSES = "zo-ao-attached zo-ao-attached-horizontal ";

	public ZofarHorizontalMultipleOptionRenderer() {
		super();
	}

	@Override
	public boolean getRendersChildren() {
		return true;
	}

	@Override
	public void encodeBegin(final FacesContext context, final UIComponent component)
			throws IOException {

		if (!component.isRendered()) {
			return;
		}

        final UIComponent parent = (UIComponent) component.getAttributes().get(
                "parentResponseDomain");
        if (parent == null) {
			return;
        }

        Boolean alignAttached = (Boolean) parent.getAttributes().get("alignAttached");
		if (alignAttached == null)alignAttached = false;

		final boolean valueLabelFlag = this.hasValueLabel(context, component);
		boolean labelFlag = this.hasLabel(context, component);
		boolean openFlag = this.hasOpenQuestion(context, component);
		if(alignAttached){
			labelFlag = true;
			openFlag = true;
		}

		boolean skipAll = true;
		if (valueLabelFlag)
			skipAll = false;
		if (labelFlag)
			skipAll = false;
		if (openFlag)
			skipAll = false;

		final String labelPosition = (String) parent.getAttributes().get("labelPosition");
		
        Boolean isMissing = (Boolean) component.getAttributes().get("missing");
		if (isMissing == null)isMissing = false;
		final ResponseWriter writer = context.getResponseWriter();

		if ((labelPosition != null)
				&& labelPosition.equals(ZofarResponseDomainRenderer.LABEL_POSITION_BOTTOM)) {
			writer.startElement("tr", component);
			writer.startElement("td", component);
			writer.writeAttribute("class", ZofarHorizontalMultipleOptionRenderer.INPUTCLASSES, null);
			writer.writeAttribute("align", "center", null);
			this.encodeInput(context, component);
			writer.endElement("td");
			writer.endElement("tr");

			if (skipAll) {
				writer.startElement("tr", component);
				writer.startElement("td", component);
				writer.endElement("td");
				writer.endElement("tr");
			} else {

			}

		} else {
			writer.startElement("tr", component);
			writer.startElement("td", component);
			writer.writeAttribute("class", ZofarHorizontalMultipleOptionRenderer.LABELCLASSES, null);
			writer.writeAttribute("align", "center", null);
			if (labelFlag) {

				this.encodeLabel(context, component);

			}
			writer.endElement("td");
			writer.endElement("tr");
			
			writer.startElement("tr", component);
			writer.startElement("td", component);
			writer.writeAttribute("class", ZofarHorizontalMultipleOptionRenderer.VALUECLASSES, null);
			writer.writeAttribute("align", "center", null);
			if (valueLabelFlag) {

				this.encodeValueLabel(context, component);

			}
			writer.endElement("td");
			writer.endElement("tr");
		}

	}

	@Override
	public void encodeChildren(final FacesContext context, final UIComponent component)
			throws IOException {
	}

	@Override
	public void encodeEnd(final FacesContext context, final UIComponent component)
			throws IOException {
		final ResponseWriter writer = context.getResponseWriter();
        final UIComponent parent = (UIComponent) component.getAttributes().get(
                "parentResponseDomain");
        if (parent == null) {
			return;
        }

        Boolean alignAttached = (Boolean) parent.getAttributes().get("alignAttached");
		if (alignAttached == null)alignAttached = false;

		final boolean valueLabelFlag = this.hasValueLabel(context, component);
		boolean labelFlag = this.hasLabel(context, component);
		boolean openFlag = this.hasOpenQuestion(context, component);
		if(alignAttached){
			labelFlag = true;
			openFlag = true;
		}

		boolean skipAll = true;
		if (valueLabelFlag)
			skipAll = false;
		if (labelFlag)
			skipAll = false;
		if (openFlag)
			skipAll = false;

		final String labelPosition = (String) parent.getAttributes().get("labelPosition");
		if ((labelPosition != null)
				&& labelPosition.equals(ZofarResponseDomainRenderer.LABEL_POSITION_BOTTOM)) {
			
			writer.startElement("tr", component);
			writer.startElement("td", component);
			writer.writeAttribute("class", ZofarHorizontalMultipleOptionRenderer.VALUECLASSES, null);
			writer.writeAttribute("align", "center", null);
			if (valueLabelFlag) {

				this.encodeValueLabel(context, component);

			}
			writer.endElement("td");
			writer.endElement("tr");
			
			writer.startElement("tr", component);
			writer.startElement("td", component);
			writer.writeAttribute("class", "zo-ao-label", null);
			writer.writeAttribute("align", "center", null);
			if (labelFlag) {

				this.encodeLabel(context, component);

			}
			writer.endElement("td");
			writer.endElement("tr");
			

		} else {
			if (skipAll) {
				writer.startElement("tr", component);
				writer.startElement("td", component);
				writer.endElement("td");
				writer.endElement("tr");
			} else {

			}
			writer.startElement("tr", component);
			writer.startElement("td", component);
			writer.writeAttribute("class", ZofarHorizontalMultipleOptionRenderer.INPUTCLASSES, null);
			writer.writeAttribute("align", "center", null);
			this.encodeInput(context, component);
			if (openFlag) {
				this.encodeOpenQuestion(context, component);
			}
			writer.endElement("td");
			writer.endElement("tr");
		}

	}

}

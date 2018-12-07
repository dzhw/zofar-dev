package de.his.zofar.presentation.surveyengine.ui.renderer.answers.singechoice.options;

import java.io.IOException;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.FacesRenderer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.his.zofar.presentation.surveyengine.ui.components.answer.options.SingleOption;

@FacesRenderer(componentFamily = SingleOption.COMPONENT_FAMILY, rendererType = ZofarComparisonRadioSingleChoiceOptionRenderer.RENDERER_TYPE)
public class ZofarComparisonRadioSingleChoiceOptionRenderer extends
		ZofarRadioSingleOptionRenderer {
	
	public static final String RENDERER_TYPE = "org.zofar.ComparisonRadioSingleOption";
	
	private static final Logger LOGGER = LoggerFactory
			.getLogger(ZofarComparisonRadioSingleChoiceOptionRenderer.class);
	
	public ZofarComparisonRadioSingleChoiceOptionRenderer() {
		super();
	}
	
	@Override
	public synchronized boolean getRendersChildren() {
		return true;
	}
	
	@Override
	public synchronized void encodeBegin(final FacesContext context,
			final UIComponent component) throws IOException {

		if (!component.isRendered()) {
			return;
		}
		encodeInput(context, component);
	}
	
	@Override
	public synchronized void encodeChildren(final FacesContext context,
			final UIComponent component) throws IOException {

		if (!component.isRendered()) {
			return;
		}
		encodeOpenQuestion(context, component);
	}
	
	@Override
	public synchronized void encodeEnd(final FacesContext context,
			final UIComponent component) throws IOException {

		if (!component.isRendered()) {
			return;
		}
	}
}

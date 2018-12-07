package de.his.zofar.presentation.surveyengine.ui.renderer.answers.singechoice.options;

import java.io.IOException;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.render.FacesRenderer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.his.zofar.presentation.surveyengine.ui.components.answer.options.SingleOption;

@FacesRenderer(componentFamily = SingleOption.COMPONENT_FAMILY, rendererType = ZofarComparisonRadioSingleChoiceOptionRenderer.RENDERER_TYPE)
public class ZofarComparisonRadioSingleChoiceOptionRenderer extends ZofarRadioSingleOptionRenderer {

	public static final String RENDERER_TYPE = "org.zofar.ComparisonRadioSingleOption";

	private static final Logger LOGGER = LoggerFactory.getLogger(ZofarComparisonRadioSingleChoiceOptionRenderer.class);

	public ZofarComparisonRadioSingleChoiceOptionRenderer() {
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
//		this.encodeInput(context, component);
	}

	@Override
	public void encodeChildren(final FacesContext context, final UIComponent component) throws IOException {

		if (!component.isRendered()) {
			return;
		}
//		this.encodeOpenQuestion(context, component);
	}

	@Override
	public void encodeEnd(final FacesContext context, final UIComponent component) throws IOException {

		if (!component.isRendered()) {
			return;
		}
	}

	@Override
	protected void encodeItem(FacesContext context, UIComponent component) throws IOException {
		// TODO Auto-generated method stub
		
	}
}

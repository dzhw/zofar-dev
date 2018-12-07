package de.his.zofar.presentation.surveyengine.ui.renderer.answers.multiplechoice.options;

import java.io.IOException;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.render.FacesRenderer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.his.zofar.presentation.surveyengine.ui.components.answer.options.MultipleOption;

/**
 * @author meisner
 * 
 */
@FacesRenderer(componentFamily = MultipleOption.COMPONENT_FAMILY, rendererType = ZofarComparisonCheckboxOptionRenderer.RENDERER_TYPE)
public class ZofarComparisonCheckboxOptionRenderer extends
		ZofarCheckboxOptionRenderer {

	public static final String RENDERER_TYPE = "org.zofar.ComparisonMultipleOption";

	private static final Logger LOGGER = LoggerFactory
			.getLogger(ZofarComparisonCheckboxOptionRenderer.class);

	public ZofarComparisonCheckboxOptionRenderer() {
		super();
	}

	@Override
	public boolean getRendersChildren() {
		return true;
	}

	@Override
	public void encodeBegin(final FacesContext context,
			final UIComponent component) throws IOException {

		if (!component.isRendered()) {
			return;
		}
		
		encodeInput(context, component);
	}

	@Override
	public void encodeChildren(final FacesContext context,
			final UIComponent component) throws IOException {

		if (!component.isRendered()) {
			return;
		}
		encodeOpenQuestion(context, component);
	}

	@Override
	public void encodeEnd(final FacesContext context,
			final UIComponent component) throws IOException {

		if (!component.isRendered()) {
			return;
		}
	}
}

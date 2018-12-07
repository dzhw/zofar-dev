package de.his.zofar.presentation.surveyengine.ui.renderer.answers.matrix.multiplechoice.options;

import java.io.IOException;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.render.FacesRenderer;

import de.his.zofar.presentation.surveyengine.ui.components.answer.options.MultipleOption;
import de.his.zofar.presentation.surveyengine.ui.renderer.answers.multiplechoice.options.ZofarCheckboxOptionRenderer;

@FacesRenderer(componentFamily = MultipleOption.COMPONENT_FAMILY, rendererType = ZofarMultipleChoiceMatrixOptionRenderer.RENDERER_TYPE)
public class ZofarMultipleChoiceMatrixOptionRenderer extends ZofarCheckboxOptionRenderer {
	
	public static final String RENDERER_TYPE = "org.zofar.MultipleChoiceMatrixOption";
	
	public ZofarMultipleChoiceMatrixOptionRenderer() {
		super();
	}
	
	@Override
	public void encodeBegin(final FacesContext context, final UIComponent component)
			throws IOException {

		if (!component.isRendered()) {
			return;
		}
		encodeInputMatrix(context, component);
	}
}

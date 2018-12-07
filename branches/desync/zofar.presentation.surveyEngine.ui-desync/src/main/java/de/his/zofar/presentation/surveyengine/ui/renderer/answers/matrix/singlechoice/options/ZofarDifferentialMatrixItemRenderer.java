package de.his.zofar.presentation.surveyengine.ui.renderer.answers.matrix.singlechoice.options;

import java.io.IOException;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.render.FacesRenderer;

import de.his.zofar.presentation.surveyengine.ui.components.tablebase.AbstractTableItem;
import de.his.zofar.presentation.surveyengine.ui.renderer.answers.matrix.ZofarMatrixItemRenderer;

/**
 * @author meisner
 * 
 */
@FacesRenderer(componentFamily = AbstractTableItem.COMPONENT_FAMILY, rendererType = ZofarDifferentialMatrixItemRenderer.RENDERER_TYPE)
public class ZofarDifferentialMatrixItemRenderer extends
		ZofarMatrixItemRenderer {

	public static final String RENDERER_TYPE = "org.zofar.DifferentialMatrixItem";

	public ZofarDifferentialMatrixItemRenderer() {
		super();
	}

	@Override
	public void encodeBegin(FacesContext context, UIComponent component)
			throws IOException {
	}

	@Override
	public void encodeEnd(FacesContext context, UIComponent component)
			throws IOException {
	}

}

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

//	private static final Logger LOGGER = LoggerFactory
//			.getLogger(ZofarDifferentialMatrixItemRenderer.class);

	public ZofarDifferentialMatrixItemRenderer() {
		super();
	}

	@Override
	public synchronized void encodeBegin(FacesContext context, UIComponent component)
			throws IOException {
	}

	@Override
	public synchronized void encodeEnd(FacesContext context, UIComponent component)
			throws IOException {
	}

}

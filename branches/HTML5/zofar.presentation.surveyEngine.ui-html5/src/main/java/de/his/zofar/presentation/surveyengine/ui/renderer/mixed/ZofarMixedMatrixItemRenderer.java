/*
 *
 */
package de.his.zofar.presentation.surveyengine.ui.renderer.mixed;

import java.io.IOException;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.FacesRenderer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.faces.facelets.compiler.UIInstructions;

import de.his.zofar.presentation.surveyengine.ui.components.question.composite.mixed.UIMixedMatrix;
import de.his.zofar.presentation.surveyengine.ui.components.question.composite.mixed.UIMixedMatrixItem;
import de.his.zofar.presentation.surveyengine.ui.components.tablebase.AbstractTableResponseDomain;
import de.his.zofar.presentation.surveyengine.ui.renderer.answers.matrix.ZofarMatrixItemRenderer;
import de.his.zofar.presentation.surveyengine.ui.util.JsfUtility;

/**
 * @author meisner
 *
 */
@FacesRenderer(componentFamily = UIMixedMatrixItem.COMPONENT_FAMILY, rendererType = ZofarMixedMatrixItemRenderer.RENDERER_TYPE)
public class ZofarMixedMatrixItemRenderer extends ZofarMatrixItemRenderer {

	private static final Logger LOGGER = LoggerFactory.getLogger(ZofarMixedMatrixItemRenderer.class);

	public static final String RENDERER_TYPE = "rg.zofar.MixedMatrixItem";

	public ZofarMixedMatrixItemRenderer() {
		super();
	}

	@Override
	public void encodeBegin(final FacesContext context, final UIComponent component) throws IOException {
		if (!component.isRendered()) {
			return;
		}

		super.encodeBegin(context, component);
	}

	@Override
	public void encodeChildren(final FacesContext context, final UIComponent component) throws IOException {

		if (!component.isRendered()) {
			return;
		}

		super.encodeChildren(context, component);
	}

	@Override
	public void encodeEnd(final FacesContext context, final UIComponent component) throws IOException {
		if (!component.isRendered()) {
			return;
		}

		super.encodeEnd(context, component);
	}

}

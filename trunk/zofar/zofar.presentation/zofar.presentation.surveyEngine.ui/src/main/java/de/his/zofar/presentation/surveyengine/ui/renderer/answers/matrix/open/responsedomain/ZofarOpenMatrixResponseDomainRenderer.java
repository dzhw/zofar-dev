package de.his.zofar.presentation.surveyengine.ui.renderer.answers.matrix.open.responsedomain;

import java.io.IOException;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.render.FacesRenderer;

import de.his.zofar.presentation.surveyengine.ui.components.tablebase.AbstractTableResponseDomain;
import de.his.zofar.presentation.surveyengine.ui.renderer.answers.matrix.ZofarMatrixResponseDomainRenderer;
/**
 * @author meisner
 * 
 */
@FacesRenderer(componentFamily = AbstractTableResponseDomain.COMPONENT_FAMILY, rendererType = ZofarOpenMatrixResponseDomainRenderer.RENDERER_TYPE)
public class ZofarOpenMatrixResponseDomainRenderer extends
		ZofarMatrixResponseDomainRenderer {

	public static final String RENDERER_TYPE = "org.zofar.OpenMatrixResponseDomain";
	
	public ZofarOpenMatrixResponseDomainRenderer() {
		super();
	}
	
	@Override
	public synchronized void encodeBegin(FacesContext context, UIComponent component)
			throws IOException {
		if (!component.isRendered()) {
			return;
		}

		super.encodeBegin(context, component,"zo-open-matrix-response-domain");
	}

	@Override
	public synchronized void encodeChildren(final FacesContext context,
			final UIComponent component) throws IOException {

		if (!component.isRendered()) {
			return;
		}

		super.encodeChildren(context, component);
	}

	@Override
	public synchronized void encodeEnd(FacesContext context, UIComponent component)
			throws IOException {
		if (!component.isRendered()) {
			return;
		}

		super.encodeEnd(context, component);
	}

}

package de.his.zofar.presentation.surveyengine.ui.renderer.text;

import java.io.IOException;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.FacesRenderer;

import de.his.zofar.presentation.surveyengine.ui.components.text.UIText;

@FacesRenderer(componentFamily = UIText.COMPONENT_FAMILY, rendererType = ZofarTitleTextRenderer.RENDERER_TYPE)
public class ZofarTitleTextRenderer extends ZofarTextRenderer {

	public static final String RENDERER_TYPE = "org.zofar.TitleText";

	@Override
	public boolean getRendersChildren() {
		return true;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * javax.faces.render.Renderer#encodeBegin(javax.faces.context.FacesContext,
	 * javax.faces.component.UIComponent)
	 */
	@Override
	public void encodeBegin(final FacesContext context, final UIComponent component) throws IOException {
		if (!component.isRendered()) {
			return;
		}
		if (((UIText) component).getContainerAttribute()) {
			final ResponseWriter writer = context.getResponseWriter();
			writer.startElement("div", component);
			writer.writeAttribute("class", "title-text", null);
			writer.writeAttribute("id", component.getClientId(context), null);
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * javax.faces.render.Renderer#encodeEnd(javax.faces.context.FacesContext,
	 * javax.faces.component.UIComponent)
	 */
	@Override
	public void encodeEnd(final FacesContext context, final UIComponent component) throws IOException {

		if (!component.isRendered()) {
			return;
		}
		if (((UIText) component).getContainerAttribute()) {
			final ResponseWriter writer = context.getResponseWriter();
			writer.endElement("div");
		}

	}

}

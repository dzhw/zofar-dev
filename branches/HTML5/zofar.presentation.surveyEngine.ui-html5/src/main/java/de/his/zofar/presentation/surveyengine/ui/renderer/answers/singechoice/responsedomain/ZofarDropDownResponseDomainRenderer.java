/**
 *
 */
package de.his.zofar.presentation.surveyengine.ui.renderer.answers.singechoice.responsedomain;

import java.io.IOException;

import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.FacesRenderer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.faces.facelets.compiler.UIInstructions;

import de.his.zofar.presentation.surveyengine.ui.components.container.Section;
import de.his.zofar.presentation.surveyengine.ui.components.text.UIText;
import de.his.zofar.presentation.surveyengine.ui.interfaces.IResponseDomain;
import de.his.zofar.presentation.surveyengine.ui.util.JsfUtility;

/**
 * @author le
 *
 */
@FacesRenderer(componentFamily = IResponseDomain.COMPONENT_FAMILY, rendererType = ZofarDropDownResponseDomainRenderer.RENDERER_TYPE)
public class ZofarDropDownResponseDomainRenderer extends ZofarSingleChoiceResponseDomainRenderer {

	private static final Logger LOGGER = LoggerFactory.getLogger(ZofarDropDownResponseDomainRenderer.class);

	public static final String RENDERER_TYPE = "org.zofar.DropDownResponseDomain";

	/*
	 * (non-Javadoc)
	 *
	 * @see de.his.zofar.presentation.surveyengine.ui.renderer.
	 * ZofarResponseDomainRenderer#encodeBegin(javax.faces.context.FacesContext,
	 * javax.faces.component.UIComponent)
	 */
	@Override
	public void encodeBegin(final FacesContext context, final UIComponent component) throws IOException {
		if (!component.isRendered()) {
			return;
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see de.his.zofar.presentation.surveyengine.ui.renderer.
	 * ZofarResponseDomainRenderer
	 * #encodeChildren(javax.faces.context.FacesContext,
	 * javax.faces.component.UIComponent)
	 */
	@Override
	public void encodeChildren(final FacesContext context, final UIComponent component) throws IOException {
		if (!component.isRendered()) {
			return;
		}
		final ResponseWriter writer = context.getResponseWriter();

		writer.startElement("select", component);
		writer.writeAttribute("class", "custom-select", null);
		writer.writeAttribute("name", component.getClientId(), null);
		writer.writeAttribute("id", component.getClientId() + ":select", null);

		for (final UIComponent child : component.getChildren()) {
			if (!child.isRendered()) {
				continue;
			}
			if (child instanceof Section) {
				child.encodeAll(context);
			} else {
				child.encodeAll(context);
			}
		}
		writer.endElement("select");
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see de.his.zofar.presentation.surveyengine.ui.renderer.
	 * ZofarResponseDomainRenderer#encodeEnd(javax.faces.context.FacesContext,
	 * javax.faces.component.UIComponent)
	 */
	@Override
	public void encodeEnd(final FacesContext context, final UIComponent component) throws IOException {

		if (!component.isRendered()) {
			return;
		}
	}
}

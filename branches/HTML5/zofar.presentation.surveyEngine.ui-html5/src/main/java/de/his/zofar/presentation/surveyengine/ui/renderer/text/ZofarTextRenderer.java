/**
 *
 */
package de.his.zofar.presentation.surveyengine.ui.renderer.text;

import java.io.IOException;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.FacesRenderer;
import javax.faces.render.Renderer;

import com.sun.faces.facelets.compiler.UIInstructions;

import de.his.zofar.presentation.surveyengine.ui.components.text.UIText;
import de.his.zofar.presentation.surveyengine.ui.util.JsfUtility;

/**
 * renderer class for all text components.
 *
 * @author le
 *
 */
@FacesRenderer(componentFamily = UIText.COMPONENT_FAMILY, rendererType = ZofarTextRenderer.RENDERER_TYPE)
public class ZofarTextRenderer extends Renderer {

	public static final String RENDERER_TYPE = "org.zofar.Text";

	/*
	 * (non-Javadoc)
	 *
	 * @see javax.faces.render.Renderer#getRendersChildren()
	 */
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
		super.encodeBegin(context, component);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see javax.faces.render.Renderer#encodeChildren(javax.faces.context.
	 * FacesContext , javax.faces.component.UIComponent)
	 */
	@Override
	public void encodeChildren(final FacesContext context, final UIComponent component) throws IOException {

		if (!component.isRendered()) {
			return;
		}
		final ResponseWriter writer = context.getResponseWriter();
        for (final UIComponent child : component.getChildren()) {
            if (UIInstructions.class.isAssignableFrom(child.getClass())) {
                writer.write(JsfUtility.getInstance().evaluateValueExpression(
                        context, String.valueOf(child), String.class));
            } else {
                child.encodeAll(context);
            }
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

		super.encodeEnd(context, component);

	}

}

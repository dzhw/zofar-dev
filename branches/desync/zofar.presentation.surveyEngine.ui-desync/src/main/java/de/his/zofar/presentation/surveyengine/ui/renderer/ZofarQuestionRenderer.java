/**
 *
 */
package de.his.zofar.presentation.surveyengine.ui.renderer;

import java.io.IOException;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.FacesRenderer;

import com.sun.faces.renderkit.html_basic.GroupRenderer;

import de.his.zofar.presentation.surveyengine.ui.components.question.UIQuestion;

/**
 * renderer class for children of UIQuestion.
 *
 * @author le
 *
 */
@FacesRenderer(componentFamily = UIQuestion.COMPONENT_FAMILY, rendererType = ZofarQuestionRenderer.RENDERER_TYPE)
public class ZofarQuestionRenderer extends GroupRenderer {

    public static final String RENDERER_TYPE = "org.zofar.Question";

    /*
     * (non-Javadoc)
     *
     * @see
     * com.sun.faces.renderkit.html_basic.GroupRenderer#encodeBegin(javax.faces
     * .context.FacesContext, javax.faces.component.UIComponent)
     */
    @Override
    public void encodeBegin(final FacesContext context,
            final UIComponent component) throws IOException {
        this.encodeHeaderFacet(context, component);
    }

    /**
     * encodes the header facet and wraps it with a DIV block.
     *
     * @param context
     * @param component
     * @throws IOException
     */
    private void encodeHeaderFacet(final FacesContext context,
            final UIComponent component) throws IOException {

        if (!component.isRendered()) {
            return;
        }

        final ResponseWriter writer = context.getResponseWriter();
        writer.startElement("span", component);
        if((UIQuestion.class).isAssignableFrom(component.getClass()))writer.writeAttribute("class", ((UIQuestion)component).getStyleClass(),null);
        final UIComponent header = component.getFacet("header");
        if (header != null) {
            writer.startElement("div", header);
            writer.writeAttribute("class", UIQuestion.HEADER_STYLE_CLASS, null);
            header.encodeAll(context);
            writer.endElement("div");
        }
    }

	@Override
	public void encodeEnd(final FacesContext context, final UIComponent component)
			throws IOException {
		final ResponseWriter writer = context.getResponseWriter();
		writer.endElement("span");
	}
    
    

}

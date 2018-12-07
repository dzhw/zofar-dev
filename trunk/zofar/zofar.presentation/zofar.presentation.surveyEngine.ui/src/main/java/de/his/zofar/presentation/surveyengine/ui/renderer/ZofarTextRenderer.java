/**
 *
 */
package de.his.zofar.presentation.surveyengine.ui.renderer;

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
    public synchronized void encodeBegin(final FacesContext context,
            final UIComponent component) throws IOException {

        if (!component.isRendered()) {
            return;
        }

        final ResponseWriter writer = context.getResponseWriter();
        boolean flag = false;

        if ((boolean) component.getAttributes().get("block")) {
            writer.startElement("div", component);
            flag = true;
        } 
        else if((de.his.zofar.presentation.surveyengine.ui.components.text.UIResponseOptionText.class).isAssignableFrom(component.getClass())){
    		
    	}
    	else {
            writer.startElement("span", component);
            flag = true;
        }
        
        if(flag){
            writer.writeAttribute("id", component.getClientId(context), null);

            final String styleClass = (String) component.getAttributes().get(
                    "styleClass");

            if (styleClass != null) {
                writer.writeAttribute("class", styleClass, null);
            }
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * javax.faces.render.Renderer#encodeChildren(javax.faces.context.FacesContext
     * , javax.faces.component.UIComponent)
     */
    @Override
    public synchronized void encodeChildren(final FacesContext context,
            final UIComponent component) throws IOException {

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
    public synchronized void encodeEnd(final FacesContext context,
            final UIComponent component) throws IOException {

        if (!component.isRendered()) {
            return;
        }

        final ResponseWriter writer = context.getResponseWriter();

        if (UIText.class.isAssignableFrom(component.getClass())
                && ((UIText) component).isBlock()) {
            writer.endElement("div");
        } 
        else if((de.his.zofar.presentation.surveyengine.ui.components.text.UIResponseOptionText.class).isAssignableFrom(component.getClass())){
    		
    	}
        else {
            writer.endElement("span");
        }

    }

}

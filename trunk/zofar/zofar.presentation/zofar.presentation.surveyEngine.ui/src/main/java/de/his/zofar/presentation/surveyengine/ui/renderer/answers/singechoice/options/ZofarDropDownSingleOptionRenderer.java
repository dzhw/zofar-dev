/**
 *
 */
package de.his.zofar.presentation.surveyengine.ui.renderer.answers.singechoice.options;

import java.io.IOException;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.FacesRenderer;
import javax.faces.render.Renderer;

import de.his.zofar.presentation.surveyengine.ui.components.answer.options.SingleOption;

/**
 * @author le
 *
 */
@FacesRenderer(componentFamily = SingleOption.COMPONENT_FAMILY, rendererType = ZofarDropDownSingleOptionRenderer.RENDERER_TYPE)
public class ZofarDropDownSingleOptionRenderer extends Renderer {

    public static final String RENDERER_TYPE = "org.zofar.DropDownSingleOption";

    private static final String SINGLE_OPTION_CSS_CLASS = "zo-ao";
    private static final String VALUE_LABEL_FORMAT = "%s - ";

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
        // disable rendering children.
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.faces.render.Renderer#getRendersChildren()
     */
    @Override
    public synchronized boolean getRendersChildren() {
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

        writer.startElement("option", component);
        writer.writeAttribute("id", component.getClientId(context), null);
        writer.writeAttribute("value", component.getId(), null);

        final UIComponent parentResponseDomain = (UIComponent) component
                .getAttributes().get("parentResponseDomain");
        if (parentResponseDomain != null) {
            final Object currentValue = parentResponseDomain.getAttributes()
                    .get("value");

            if (currentValue != null && currentValue.equals(component.getId())) {
                writer.writeAttribute("selected", "selected", null);
            }
        }

        String cssClass = SINGLE_OPTION_CSS_CLASS;

        final Boolean isMissing = (Boolean) component.getAttributes().get(
                "missing");
        if (isMissing) {
            cssClass += " zo-ao-missing";
        }
        writer.writeAttribute("class", cssClass, null);

        if (parentResponseDomain != null) {
            final Boolean isShowValues = (Boolean) parentResponseDomain
                    .getAttributes().get("showValues");

            if (isShowValues) {
                final String value = (String) component.getAttributes().get(
                        "value");
                if (value != null && !value.isEmpty()) {
                    writer.write(String.format(VALUE_LABEL_FORMAT, value));
                }
            }
        }

        // render labels facet
        if (component.getFacet("labels") != null) {
            component.getFacet("labels").encodeAll(context);
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
        writer.endElement("option");

    }

}

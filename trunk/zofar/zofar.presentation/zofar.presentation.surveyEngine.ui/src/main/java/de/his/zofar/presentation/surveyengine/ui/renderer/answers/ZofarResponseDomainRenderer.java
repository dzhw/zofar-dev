/**
 *
 */
package de.his.zofar.presentation.surveyengine.ui.renderer.answers;

import java.io.IOException;

import javax.faces.component.UIComponent;
import javax.faces.context.ResponseWriter;
import javax.faces.render.Renderer;

/**
 * @author le
 *
 */
public abstract class ZofarResponseDomainRenderer extends Renderer {

    /**
     *
     */
    protected static final String RESPONSEDOMAIN_CSS_CLASS = "zo-responsedomain";

    public static final String LABEL_POSITION_LEFT = "left";
    public static final String LABEL_POSITION_RIGHT = "right";
    public static final String LABEL_POSITION_TOP = "top";
    public static final String LABEL_POSITION_BOTTOM = "bottom";

    /**
     * @param writer
     * @param component
     * @throws IOException
     */
    protected synchronized void startTable(final ResponseWriter writer,
            final UIComponent component) throws IOException {
    	startTable(writer,component,null);
    }
    
    /**
     * @param writer
     * @param component
     * @throws IOException
     */
    protected synchronized void startTable(final ResponseWriter writer,
            final UIComponent component,String additionalClasses) throws IOException {
    	if(additionalClasses == null)additionalClasses = "";
        writer.startElement("table", component);
        writer.writeAttribute("id", component.getClientId(), null);
        writer.writeAttribute("class", (RESPONSEDOMAIN_CSS_CLASS+" "+additionalClasses).trim(), null);
//		writer.writeAttribute("border", 1, null);
		writer.writeAttribute("cellspacing", 0, null);
		writer.writeAttribute("cellpadding", 0, null);
    }

    /**
     * @param writer
     * @throws IOException
     */
    protected synchronized void endTable(final ResponseWriter writer) throws IOException {
        writer.endElement("table");
    }

    /**
     * @param itemClasses
     * @return
     */
    protected synchronized String[] itemClassesToArray(final String itemClasses) {
        if (itemClasses==null || itemClasses.isEmpty()) {
            return new String[0];
        }
        return itemClasses.trim().split("\\s* \\s*");
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

}

package de.his.zofar.presentation.surveyengine.ui.renderer.answers.singechoice.section;

import java.io.IOException;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.render.FacesRenderer;

import com.sun.faces.renderkit.html_basic.HtmlBasicRenderer;

import de.his.zofar.presentation.surveyengine.ui.components.container.Section;

/**
 * @author meisner
 *
 */
@FacesRenderer(componentFamily = Section.COMPONENT_FAMILY, rendererType = RadioSectionRenderer.RENDERER_TYPE)
public class RadioSectionRenderer extends HtmlBasicRenderer{
	
	public static final String RENDERER_TYPE = "org.zofar.RadioSection";
	
//	private static final Logger LOGGER = LoggerFactory
//			.getLogger(RadioSectionRenderer.class);

	public RadioSectionRenderer() {
		super();
	}

	@Override
	public boolean getRendersChildren() {
		return true;
	}

	@Override
	public synchronized void encodeBegin(FacesContext context, UIComponent component)
			throws IOException {
		
		if(!component.isRendered())return;

		UIComponent header = component.getFacet("header");
		if(header!=null)header.encodeAll(context);
				
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
    }
	
	@Override
	public synchronized void encodeEnd(FacesContext context, UIComponent component)
			throws IOException {
	}
}

package de.his.zofar.presentation.surveyengine.ui.renderer.answers.multiplechoice.section;

import java.io.IOException;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.render.FacesRenderer;

import com.sun.faces.renderkit.html_basic.HtmlBasicRenderer;

import de.his.zofar.presentation.surveyengine.ui.components.container.Section;

/**
 * @author dick
 *
 */
@FacesRenderer(componentFamily = Section.COMPONENT_FAMILY, rendererType = MultipleSectionRenderer.RENDERER_TYPE)
public class MultipleSectionRenderer extends HtmlBasicRenderer{
	
	public static final String RENDERER_TYPE = "org.zofar.MultipleSection";
	
//	private static final Logger LOGGER = LoggerFactory
//			.getLogger(RadioSectionRenderer.class);

	public MultipleSectionRenderer() {
		super();
	}

	@Override
	public boolean getRendersChildren() {
		return true;
	}

	@Override
	public void encodeBegin(FacesContext context, UIComponent component)
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
    public void encodeChildren(final FacesContext context,
            final UIComponent component) throws IOException {
    }
	
	@Override
	public void encodeEnd(FacesContext context, UIComponent component)
			throws IOException {
	}
}

package de.his.zofar.presentation.surveyengine.ui.renderer.Progress;

import java.io.IOException;
import java.util.Map;
import java.util.Stack;

import javax.faces.component.UIComponent;
import javax.faces.component.html.HtmlGraphicImage;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.FacesRenderer;
import javax.faces.render.Renderer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.his.zofar.presentation.surveyengine.ui.components.common.PictureBar;
/**
 * @author meisner
 *
 */
@FacesRenderer(componentFamily = PictureBar.COMPONENT_FAMILY, rendererType = ProgressRenderer.RENDERER_TYPE)
public class ProgressRenderer extends Renderer{
	
	public static final String RENDERER_TYPE = "org.zofar.picturebar";
	
	private static final Logger LOGGER = LoggerFactory
			.getLogger(ProgressRenderer.class);

	public ProgressRenderer() {
		super();
	}

	@Override
	public void encodeBegin(FacesContext context, UIComponent component) throws IOException {
		if (!component.isRendered())
			return;
		if((PictureBar.class).isAssignableFrom(component.getClass())){
			PictureBar tmp = (PictureBar)component;

			final Stack<String> movements = tmp.getNavigatorAttribute().getMovements();
			final Map milestones = tmp.getMilestonesAttribute();
			
			if(movements != null){
				HtmlGraphicImage dotImage = new HtmlGraphicImage();
				dotImage.setValue(tmp.getDotAttribute());
				
				HtmlGraphicImage arrowImage = new HtmlGraphicImage();
				arrowImage.setValue(tmp.getArrowAttribute());			
				
				for(String movement : movements){
					if((milestones != null)&&(milestones.containsKey(movement))){
						arrowImage.encodeAll(context);
						HtmlGraphicImage milestoneImage = new HtmlGraphicImage();
						milestoneImage.setValue(milestones.get(movement)+"");
						milestoneImage.encodeAll(context);
					}
					else dotImage.encodeAll(context);					
				}
			}
		}
	}

    @Override
    public void encodeChildren(final FacesContext context,
            final UIComponent component) throws IOException {
        // disable rendering children.
    }

	@Override
	public void encodeEnd(FacesContext context, UIComponent component) throws IOException {
		if (!component.isRendered())
			return;
		final ResponseWriter writer = context.getResponseWriter();
	}

	@Override
	public boolean getRendersChildren() {
		return false;
	}

}

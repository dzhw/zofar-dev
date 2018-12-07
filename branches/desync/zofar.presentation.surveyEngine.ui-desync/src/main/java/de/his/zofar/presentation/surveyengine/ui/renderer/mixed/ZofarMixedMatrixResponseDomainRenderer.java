/*
 * 
 */
package de.his.zofar.presentation.surveyengine.ui.renderer.mixed;

import java.io.IOException;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.render.FacesRenderer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.his.zofar.presentation.surveyengine.ui.components.composite.mixed.UIMixedMatrixItem;
import de.his.zofar.presentation.surveyengine.ui.components.composite.mixed.UIMixedMatrixResponseDomain;
import de.his.zofar.presentation.surveyengine.ui.renderer.answers.matrix.ZofarMatrixResponseDomainRenderer;

/**
* @author meisner
*
*/
@FacesRenderer(componentFamily = UIMixedMatrixResponseDomain.COMPONENT_FAMILY, rendererType = ZofarMixedMatrixResponseDomainRenderer.RENDERER_TYPE)
public class ZofarMixedMatrixResponseDomainRenderer extends ZofarMatrixResponseDomainRenderer{
	
	public static final String RENDERER_TYPE = "org.zofar.MixedMatrixResponseDomain";
    private static final Logger LOGGER = LoggerFactory
			.getLogger(ZofarMixedMatrixResponseDomainRenderer.class);

	public ZofarMixedMatrixResponseDomainRenderer() {
		super();
	}
	
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
	public void encodeBegin(final FacesContext context,
			final UIComponent component) throws IOException {
		if (!component.isRendered()) {
			return;
		}
		if (component.getChildren().size()>1){
			super.encodeBegin(context, component,"zo-mixed-matrix-response-domain zo-mixed-matrix-response-domain mixed_");
		}
		else {
			super.encodeBegin(context, component,"zo-mixed-matrix-response-domain zo-mixed-matrix-response-domain mixed_"+(component.getId()));
		}
	}
	
	@Override
	protected Boolean isHasQuestionColumn(final FacesContext context,
			final UIComponent component) {
		Boolean hasQuestionColumn = true;
		if (component.getAttributes().get(ZofarMatrixResponseDomainRenderer.QUESTION_COLUMN) != null) {
			hasQuestionColumn = Boolean.valueOf(component.getAttributes().get(
					ZofarMatrixResponseDomainRenderer.QUESTION_COLUMN)+"");
		}
		
		if(hasQuestionColumn){
			Boolean tmpFlag = false;
	        for (final UIComponent child : component.getChildren()) {
	        	if((UIMixedMatrixItem.class).isAssignableFrom(child.getClass())){
	        		final UIMixedMatrixItem tmp = (UIMixedMatrixItem)child;
	        		final UIComponent header = tmp.getFacet(de.his.zofar.presentation.surveyengine.ui.renderer.answers.matrix.ZofarMatrixItemRenderer.HEADER_FACET);
	        		if((header != null)&&(header.getChildren() != null)&&(!header.getChildren().isEmpty()))tmpFlag = true;
	        	}
	        }
	        if(!tmpFlag)hasQuestionColumn=false;
		}
		return hasQuestionColumn;
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

		if (!component.isRendered()) {
			return;     
		}
        for (final UIComponent child : component.getChildren()) {
//        	LOGGER.info("child {} ({})",child,child.getRendererType());
        	child.encodeAll(context);
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
	public void encodeEnd(final FacesContext context,
			final UIComponent component) throws IOException {

		if (!component.isRendered()) {
			return;
		}
		super.encodeEnd(context, component);
	}

}

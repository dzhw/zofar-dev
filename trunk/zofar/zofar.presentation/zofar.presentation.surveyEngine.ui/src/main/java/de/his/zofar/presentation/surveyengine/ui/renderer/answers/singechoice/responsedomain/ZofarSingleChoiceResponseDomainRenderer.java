/**
 *
 */
package de.his.zofar.presentation.surveyengine.ui.renderer.answers.singechoice.responsedomain;

import java.util.Map;

import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.his.zofar.presentation.surveyengine.ui.renderer.answers.ZofarResponseDomainRenderer;

/**
 * @author le
 *
 */
public abstract class ZofarSingleChoiceResponseDomainRenderer extends
        ZofarResponseDomainRenderer {

    private static final Logger LOGGER = LoggerFactory
            .getLogger(ZofarSingleChoiceResponseDomainRenderer.class);

    /*
     * (non-Javadoc)
     *
     * @see javax.faces.render.Renderer#decode(javax.faces.context.FacesContext,
     * javax.faces.component.UIComponent)
     */
//    @Override
//    public void decode(final FacesContext context, final UIComponent component) {
//
//        // this piece was shamelessly copied from mojarra implementation of the
//        // renderer
//
//        if (!(component instanceof UIInput)) {
//            // decode needs to be invoked only for components that are
//            // instances or subclasses of UIInput.
//            if (LOGGER.isTraceEnabled()) {
//                LOGGER.trace(
//                        "No decoding necessary since the component {} is not an instance or a sub class of UIInput",
//                        component.getId());
//            }
//            return;
//        }
//
//        final String clientId = component.getClientId(context);
//
//        assert (clientId != null);
//        final Map<String, String> requestMap = context.getExternalContext()
//                .getRequestParameterMap();
//        // Don't overwrite the value unless you have to!
//        final String newValue = requestMap.get(clientId);
//        
//        
//
//        ((UIInput) component).setValue(newValue);
//
//    }
    
    
    
	@Override
	public synchronized void decode(FacesContext context, UIComponent component) {
		final Map<String, String> paramMap = context.getExternalContext()
				.getRequestParameterMap();
		final String clientId = component.getClientId(context);
//		LOGGER.info("decode {} ({})",clientId,paramMap.get(clientId));
		if (paramMap.containsKey(clientId)) {
			// request parameter found, set submitted value
			((UIInput)component).setValue(paramMap.get(clientId));
		} else {
			// see reason for this action at decodeUISelectMany
			((UIInput)component).setValue("");
		}
	}

}

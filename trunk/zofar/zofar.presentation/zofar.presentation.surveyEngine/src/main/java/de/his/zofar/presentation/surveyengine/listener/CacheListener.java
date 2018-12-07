package de.his.zofar.presentation.surveyengine.listener;

import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Used to suspress Browser-Caching
 * 
 * @author meisner
 *
 */
public class CacheListener implements PhaseListener {

	private static final long serialVersionUID = 598946879776647822L;
	
	private static final Logger LOGGER = LoggerFactory
			.getLogger(CacheListener.class);

   /* (non-Javadoc)
 * @see javax.faces.event.PhaseListener#getPhaseId()
 */
@Override
	public PhaseId getPhaseId()
    {
        return PhaseId.RENDER_RESPONSE;
    }
   @Override
    public void afterPhase(final PhaseEvent event)
    {
    }
   
   /* 
* Suppress use of Browser-Cache
 * @see javax.faces.event.PhaseListener#beforePhase(javax.faces.event.PhaseEvent)
 */
@Override
    public void beforePhase(final PhaseEvent event)
    {
//    	LOGGER.info("add Cache suspression header");
        final FacesContext facesContext = event.getFacesContext();
        final HttpServletResponse response = (HttpServletResponse) facesContext
                .getExternalContext().getResponse();
        response.addHeader("Pragma", "no-cache");
        response.addHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        response.addHeader("Expires", "0");
    }

}

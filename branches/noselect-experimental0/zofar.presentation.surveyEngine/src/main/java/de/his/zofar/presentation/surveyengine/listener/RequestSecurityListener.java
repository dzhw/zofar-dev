package de.his.zofar.presentation.surveyengine.listener;

import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import de.his.zofar.presentation.surveyengine.security.concurrentSession.ConcurrentSessionManager;
import de.his.zofar.service.surveyengine.model.ParticipantPrincipal;

public class RequestSecurityListener implements PhaseListener {

	private static final long serialVersionUID = 1553996346970195722L;
	private static final Logger LOGGER = LoggerFactory
			.getLogger(RequestSecurityListener.class);

	public RequestSecurityListener() {
		super();
//		LOGGER.info("start RequestSecurityListener {}", this.hashCode());
	}

	private ConcurrentSessionManager getManager() {
		final FacesContext context = FacesContext.getCurrentInstance();
		final ConcurrentSessionManager manager = context
				.getApplication().evaluateExpressionGet(context,
						"#{concurrentSessionManager}",
						ConcurrentSessionManager.class);
		return manager;
	}

	@Override
	public void afterPhase(final PhaseEvent event) {
		// TODO Auto-generated method stub

	}

	@Override
	public void beforePhase(final PhaseEvent event) {
		final HttpServletRequest request = (HttpServletRequest)event.getFacesContext().getExternalContext().getRequest();
//		if(request.getMethod().equals("GET")){
		if(request.getMethod().equals("POST")){
			final HttpSession session = (HttpSession) event.getFacesContext().getExternalContext().getSession(false);
			
			if (LOGGER.isDebugEnabled())LOGGER.debug("check post {} ({})", request.getHeader("Location"),session.getId());
			
			if(session != null){
				final SecurityContext context = SecurityContextHolder.getContext();
				if (context != null) {
					final Authentication authentication = context.getAuthentication();
					if (authentication != null) {
						final ParticipantPrincipal participant = (ParticipantPrincipal) context.getAuthentication().getPrincipal();
						if (!getManager().check(participant.getUsername(), session.getId())) {
							final HttpServletResponse response = (HttpServletResponse)event.getFacesContext().getExternalContext().getResponse();
							final String location = response.getHeader("Location");
							if (LOGGER.isDebugEnabled())LOGGER.debug(" response => {} for {}",location,authentication.getName());
							boolean loop = false;
							if(location != null){
								if(location.endsWith("/j_spring_security_logout"))loop = true;
							}
							if(!loop){
								getManager().logout(session,authentication,request,response);
								return;
							}
							else if (LOGGER.isDebugEnabled())LOGGER.debug(" response target loop => {} for {}",location,authentication.getName());
							
						}
					}
				}
			}
		}
	}
	


	@Override
	public PhaseId getPhaseId() {
		return PhaseId.RESTORE_VIEW;
	}

}

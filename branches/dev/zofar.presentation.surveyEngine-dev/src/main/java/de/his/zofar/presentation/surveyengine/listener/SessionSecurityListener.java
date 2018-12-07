package de.his.zofar.presentation.surveyengine.listener;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import de.his.zofar.presentation.surveyengine.security.concurrentSession.ConcurrentSessionManager;

@Deprecated
public class SessionSecurityListener implements HttpSessionListener {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(SessionSecurityListener.class);

	public SessionSecurityListener() {
		super();
		LOGGER.info("start SessionSecurityListener {}",this.hashCode());
	}
	
	private ConcurrentSessionManager getManager(final HttpSession session){
		final ApplicationContext ctx = WebApplicationContextUtils.getWebApplicationContext(session.getServletContext());
		final ConcurrentSessionManager manager = (ConcurrentSessionManager) ctx.getBean("concurrentSessionManager");
		return manager;
	}


	@Override
	public void sessionCreated(final HttpSessionEvent se) {
//		LOGGER.info("Session created {} (Manager : {})", se.getSession().getId(),getManager(se.getSession()));
		
	}

	@Override
	public void sessionDestroyed(final HttpSessionEvent se) {
		LOGGER.info("Session destroyed {} (Manager : {})", se.getSession().getId(),getManager(se.getSession()));
		getManager(se.getSession()).remove(se.getSession().getId());
	}

}

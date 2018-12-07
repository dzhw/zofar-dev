package de.his.zofar.presentation.surveyengine.listener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.faces.context.FacesContext;
import javax.faces.event.PhaseListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.jsf.DelegatingPhaseListenerMulticaster;

public class CustomDelegatingPhaseListenerMulticaster extends
		DelegatingPhaseListenerMulticaster {

	private static final long serialVersionUID = 551284545920774560L;
	private static final Logger LOGGER = LoggerFactory
			.getLogger(CustomDelegatingPhaseListenerMulticaster.class);
	
	private List<PhaseListener> ownListeners;
	

	public CustomDelegatingPhaseListenerMulticaster() {
		super();
		ownListeners = new ArrayList<PhaseListener>();
		ownListeners.add(new RequestSecurityListener());
		ownListeners.add(new CacheListener());
		LOGGER.info("created");
	}

	@Override
	protected Collection<PhaseListener> getDelegates(FacesContext facesContext) {
		return ownListeners;
	}
	
	
	
}

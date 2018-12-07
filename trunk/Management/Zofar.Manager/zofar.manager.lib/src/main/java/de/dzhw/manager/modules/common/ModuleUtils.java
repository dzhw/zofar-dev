package de.dzhw.manager.modules.common;

import java.io.IOException;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ModuleUtils {
	
	private static final Logger LOGGER = LoggerFactory
			.getLogger(ModuleUtils.class);

	private static final ModuleUtils INSTANCE = new ModuleUtils();

	private ModuleUtils() {
		super();
	}
	
	public static synchronized ModuleUtils getInstance(){
		return INSTANCE;
	}
	
	public void redirectTo(final String redirect) {
		LOGGER.info("redirect to {}", redirect);
		final FacesContext context = FacesContext.getCurrentInstance();
		try {
			final ExternalContext externalContext = context
					.getExternalContext();
			final HttpServletResponse response = (HttpServletResponse) externalContext
					.getResponse();
			response.sendRedirect("./" + redirect + ".html");
		} catch (final IOException e) {
			LOGGER.error(e.getMessage());
		}
	}
	
	public String getApplicationPath(){
		HttpServletRequest req = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
		String url = req.getRequestURL().toString();
		return url.substring(0, url.length() - req.getRequestURI().length()) + req.getContextPath() + "/";
	}

}

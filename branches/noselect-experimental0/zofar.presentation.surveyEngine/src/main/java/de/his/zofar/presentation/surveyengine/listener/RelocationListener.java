package de.his.zofar.presentation.surveyengine.listener;

import java.io.IOException;
import java.io.Serializable;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.util.List;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.his.zofar.presentation.surveyengine.util.SystemConfiguration;

public class RelocationListener implements Filter, Serializable {

	private static final long serialVersionUID = 8750828005156185399L;
	private static final Logger LOGGER = LoggerFactory
			.getLogger(RelocationListener.class);
	
	private String jvmRoute = "UNKOWN";

	public RelocationListener() {
		super();
		
		final Map<?,?> env = System.getenv();
		if((env != null)&&(env.containsKey("jvmRoute"))){
			jvmRoute = (String) env.get("jvmRoute");
			LOGGER.info("set jvmRoute to {}",jvmRoute);
		}
		
		RuntimeMXBean runtimeMxBean = ManagementFactory.getRuntimeMXBean();
		List<String> arguments = runtimeMxBean.getInputArguments();
		if(arguments != null){
			for(final String argument : arguments){
//				LOGGER.info("argument {}",argument);
				if(argument.toLowerCase().startsWith("-djvmroute")){
					jvmRoute = (String) argument.substring(argument.lastIndexOf('=')+1).trim();
					LOGGER.info("set jvmRoute to {}",jvmRoute);
				}
			}
		}
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		LOGGER.info("init");
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		final SystemConfiguration system = SystemConfiguration.getInstance();
		if((system.cluster())&&(request != null)){
			if(((HttpServletRequest.class).isAssignableFrom(request.getClass()))&&((HttpServletResponse.class).isAssignableFrom(response.getClass()))){
				final HttpServletRequest httpRequest = (HttpServletRequest)request;
				final boolean requestedValid = httpRequest.isRequestedSessionIdValid();
				final HttpServletResponse httpResponse = (HttpServletResponse)response;
				if(!requestedValid){
					final String requestedSessionId = httpRequest.getRequestedSessionId();
					if(requestedSessionId != null){
						if(!requestedSessionId.endsWith(jvmRoute)){
							final Cookie[] cookies = httpRequest.getCookies();
							if(cookies != null){
								for(final Cookie cookie : cookies){
									if(cookie.getName().equals("relocation_sid")){
										cookie.setMaxAge(-1);
										cookie.setHttpOnly(true);
										cookie.setValue(requestedSessionId);
										httpResponse.addCookie(cookie);
									}
								}
							}						
							final Cookie cookie = new Cookie("relocation_sid",requestedSessionId);
							cookie.setMaxAge(-1);
							cookie.setHttpOnly(true);
							httpResponse.addCookie(cookie);
							LOGGER.info("Session id changed {} ({}) ==> relogin",requestedSessionId,cookie.getValue());
						}
					}
				}
				else{
				}
			}
		}
		chain.doFilter(request, response);
		return;
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}


}

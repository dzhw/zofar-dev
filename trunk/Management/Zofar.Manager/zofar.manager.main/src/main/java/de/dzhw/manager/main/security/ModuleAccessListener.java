package de.dzhw.manager.main.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import de.dzhw.manager.common.utils.BeanUtil;
import de.dzhw.manager.modules.interfaces.Module;

public class ModuleAccessListener implements PhaseListener {

	private static final long serialVersionUID = -2043904296178277337L;

	private static final Logger LOGGER = LoggerFactory
			.getLogger(ModuleAccessListener.class);

	public ModuleAccessListener() {
		super();
	}

	@Override
	public void afterPhase(PhaseEvent event) {

	}

	public void beforePhase(PhaseEvent event) {
//		LOGGER.info("check access");

		final FacesContext facesContext = event.getFacesContext();
		final HttpServletRequest request = (HttpServletRequest) facesContext
				.getExternalContext().getRequest();
		
		

//		LOGGER.info("Request : {}", request.getRequestURI());
		final Authentication auth = SecurityContextHolder.getContext()
				.getAuthentication();
		
		if((request.getRequestURI()).startsWith(request.getContextPath()+"/modules/")){
			String moduleName = request.getRequestURI();
			moduleName = moduleName.replaceAll(request.getContextPath()+"/modules/", "");
			moduleName = moduleName.replaceAll("/(.*)", "");
			
//			LOGGER.info("module name {}",moduleName);

			final Module moduleBean = BeanUtil.getInstance().findModule(
					facesContext, moduleName);

			final List<GrantedAuthority> roles = new ArrayList<GrantedAuthority>();
			if(moduleBean != null)roles.addAll(moduleBean.getRoles());
			
//			LOGGER.info("Roles : {}", roles);
			
			boolean flag = false;
			
			if(!roles.isEmpty()){
				
				final Collection<? extends GrantedAuthority> authorities = auth
						.getAuthorities();
//				LOGGER.info("authorities : {}", authorities);

				final Set<String> roleNames = new HashSet<String>();
				for (final GrantedAuthority role : roles) {
					roleNames.add(role.getAuthority());
				}
				
				for (final GrantedAuthority grantedAuthority : authorities) {
					if (roleNames.contains(grantedAuthority.getAuthority())) {
						flag = true;
					}
				}
				
			}
			if(!flag){
//				SecurityContextHolder.clearContext();
				LOGGER.info("logout");
			}
			
		}
	}
	
	public boolean allowed(final List<GrantedAuthority> roles) {
		final Authentication authentication = SecurityContextHolder
				.getContext().getAuthentication();
		
		final Collection<? extends GrantedAuthority> authorities = authentication
				.getAuthorities();
		LOGGER.info("authorities : {}", authorities);

		final Set<String> roleNames = new HashSet<String>();
		for (final GrantedAuthority role : roles) {
			roleNames.add(role.getAuthority());
		}

		for (final GrantedAuthority grantedAuthority : authorities) {
			// if
			// (grantedAuthority.getAuthority().equals(Role.ADMIN_ROLE.roleName()))
			// {
			if (roleNames.contains(grantedAuthority.getAuthority())) {
				return true;
			}
		}
		return false;
	}

	public PhaseId getPhaseId() {
		return PhaseId.RESTORE_VIEW;
	}
}

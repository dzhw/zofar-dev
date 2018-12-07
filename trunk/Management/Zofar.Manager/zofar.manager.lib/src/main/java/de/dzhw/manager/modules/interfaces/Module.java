package de.dzhw.manager.modules.interfaces;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;

public interface Module {
	
	public Collection<? extends GrantedAuthority> getRoles();
	public void relocate(final String viewId);

}

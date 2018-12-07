package de.dzhw.manager.participant.managedBeans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import de.dzhw.manager.modules.interfaces.Module;
 
@Component("participant")
@Scope("session")
public class Participant implements Module,Serializable {

	private static final long serialVersionUID = -8237187257690718468L;

	private static final Logger LOGGER = LoggerFactory
			.getLogger(Participant.class);
	
	List<GrantedAuthority> roles = null;

	public Participant() {
		super();
	}

	@PostConstruct
	private void init() {
		LOGGER.info("init");
		roles = new ArrayList<GrantedAuthority>();
		roles.add(new SimpleGrantedAuthority("ROLE_PARTICIPANT"));
	}
	
	public String test(){
		LOGGER.info("participant");
		return "test";
	}

	@Override
	public Collection<GrantedAuthority> getRoles() {
		return roles;
	}

	@Override
	public void relocate(String viewId) {
		// TODO Auto-generated method stub
		
	}
}

package de.dzhw.manager.questionnaire.managedBeans;

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

@Component("questionnaire")
@Scope("session")
public class Questionnaire implements Serializable, Module {

	private static final long serialVersionUID = 4208270531452608951L;

	private static final Logger LOGGER = LoggerFactory.getLogger(Questionnaire.class);

	private List<GrantedAuthority> roles = null;

	

	public Questionnaire() {
		super();
	}

	@PostConstruct
	private void init() {
		LOGGER.info("init");
		roles = new ArrayList<GrantedAuthority>();
		roles.add(new SimpleGrantedAuthority("ROLE_QUESTIONNAIRE"));
	}

	public String test() {
		LOGGER.info("questionnaire");
		return "test";
	}

	@Override
	public Collection<GrantedAuthority> getRoles() {
		return roles;
	}

	@Override
	public void relocate(String viewId) {
		LOGGER.info("relocate : {}", viewId);
	}

}

package de.dzhw.manager.main.security;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.faces.context.FacesContext;
import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.vote.AbstractAccessDecisionManager;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.FilterInvocation;

@Deprecated
public class ModuleSecurityManager extends AbstractAccessDecisionManager {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(ModuleSecurityManager.class);

	public ModuleSecurityManager() {
		super();
	}

	public ModuleSecurityManager(List<AccessDecisionVoter> decisionVoters) {
		super(decisionVoters);
	}

	final Pattern modulePattern = Pattern.compile("/([^/]*)",
			Pattern.CASE_INSENSITIVE);

	@Override
	public void decide(Authentication authentication, Object object,
			Collection<ConfigAttribute> configAttributes)
			throws AccessDeniedException, InsufficientAuthenticationException {
		if ((object == null) || !this.supports(object.getClass())) {
			throw new IllegalArgumentException(
					"Object must be a FilterInvocation");
		}

		String url = ((FilterInvocation) object).getRequestUrl();
		LOGGER.info("requested URL : {}", url);
		LOGGER.info("object : {}", object);
		LOGGER.info("configAttributes : {}", configAttributes);

		Collection<GrantedAuthority> roles = new HashSet<GrantedAuthority>();

		final Matcher matcher = modulePattern.matcher(url);
		if (matcher.find()) {
			String moduleName = matcher.group(1);
			LOGGER.info("retrieved Module name : {}", moduleName);
			// final Module moduleBean =
			// BeanUtil.getInstance().findModule(FacesContext.getCurrentInstance(),moduleName.toLowerCase());
			// LOGGER.info("found Module Bean : {}",moduleBean);
			// roles.addAll(moduleBean.getRoles());
		}

		// Collection<ConfigAttribute> roles =
		// service.getConfigAttributesFromSecuredUris(contexto, url);

		// int deny = 0;
		//
		// for (AccessDecisionVoter voter : getDecisionVoters()) {
		// int result = voter.vote(authentication, object, roles);
		//
		// if (logger.isDebugEnabled()) {
		// logger.debug("Voter: " + voter + ", returned: " + result);
		// }
		//
		// switch (result) {
		// case AccessDecisionVoter.ACCESS_GRANTED:
		// return;
		//
		// case AccessDecisionVoter.ACCESS_DENIED:
		//
		// deny++;
		//
		// break;
		//
		// default:
		// break;
		// }
		// }
		//
		// if (deny > 0) {
		// throw new
		// AccessDeniedException(messages.getMessage("AbstractAccessDecisionManager.accessDenied",
		// "Access is denied"));
		// }

		// To get this far, every AccessDecisionVoter abstained
		checkAllowIfAllAbstainDecisions();

	}

}

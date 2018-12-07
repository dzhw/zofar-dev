package de.dzhw.manager.main.managedBeans;

import java.io.File;
import java.io.FilenameFilter;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import de.dzhw.manager.common.utils.BeanUtil;
import de.dzhw.manager.modules.interfaces.Module;

@Component("moduleScanner")
@Scope("session")
public class ModuleScanner implements Serializable {

	private static final long serialVersionUID = 3575088571900937679L;
	private static final Logger LOGGER = LoggerFactory
			.getLogger(ModuleScanner.class);

	public ModuleScanner() {
		super();
	}

	private class IndexFilter implements FilenameFilter {

		public boolean accept(File dir, String name) {
			return name.equals("index.xhtml");
		}
	}

	public class Link implements Serializable {
		/**
		 * 
		 */
		private static final long serialVersionUID = 3315585904597379984L;
		private String name;
		private String url;

		public Link(String name, String url) {
			super();
			this.name = name;
			this.url = url;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getUrl() {
			return url;
		}

		public void setUrl(String url) {
			this.url = url;
		}
	}

	private List<Link> links;

	@PostConstruct
	private void init() {
		LOGGER.info("init");
		links = new ArrayList<Link>();
	}
	
	public String loginUser(){
		final Authentication authentication = SecurityContextHolder
				.getContext().getAuthentication();
		if(authentication == null)return "Not logged in";
		final Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
		return authorities+"";
	}
	
	public void updateLinks(){
		links.clear();
		final FacesContext context = FacesContext.getCurrentInstance();
		final ExternalContext externalContext = context.getExternalContext();

		final String absoluteWebPath = externalContext.getRealPath("/modules");
		final File webRoot = new File(absoluteWebPath);	
		final File[] subs = webRoot.listFiles();
		
		if ((subs != null) && (subs.length > 0)) {
			Arrays.sort(subs);
			for (File tmp : subs) {
				if (tmp.isDirectory()) {
					final File[] indexFiles = tmp.listFiles(new IndexFilter());
					boolean hasIndex = false;
					if ((indexFiles != null) && (indexFiles.length == 1)) {
						hasIndex = true;
					}

					if (hasIndex) {
//						LOGGER.info("module name : {} ", tmp.getName());
						final Module moduleBean = BeanUtil.getInstance().findModule(context,tmp.getName());

						final List<GrantedAuthority> roles = new ArrayList<GrantedAuthority>();
						// roles.add(new
						// SimpleGrantedAuthority("ROLE_ANONYMOUS"));
						if ((moduleBean != null)
								&& (moduleBean.getRoles() != null))
							roles.addAll(moduleBean.getRoles());
//						LOGGER.info("module : {} ==> roles : {}", moduleBean,roles);

						if (allowed(roles))
							links.add(new Link(tmp.getName(), "./modules/"
									+ tmp.getName() + "/index.html"));

					}

				}
			}
		} else {

		}
	}

	public List<Link> getLinks() {
		return links;
	}

	public void setLinks(List<Link> links) {
		this.links = links;
	}

	public boolean allowed(final List<GrantedAuthority> roles) {
		final Authentication authentication = SecurityContextHolder
				.getContext().getAuthentication();
		if(authentication == null)return false;
		final Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
		
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
}

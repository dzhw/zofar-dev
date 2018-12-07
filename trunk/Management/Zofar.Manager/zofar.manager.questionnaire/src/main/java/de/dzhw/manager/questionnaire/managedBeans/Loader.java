package de.dzhw.manager.questionnaire.managedBeans;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import de.dzhw.manager.admin.managedBeans.ManagementSession;


@Component("questionnaireLoader")
@Scope("session")
public class Loader implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1798761221611580705L;

	private static final Logger LOGGER = LoggerFactory.getLogger(Loader.class);
	
	private List<String> projects;

	public Loader() {
		super();
	}
	
	@PostConstruct
	private void init() {
		LOGGER.info("init");
		unloadProject();
		updateProjectList();
	}
	
	private ManagementSession getSession() {
		final FacesContext context = FacesContext.getCurrentInstance();
		ManagementSession bean = context.getApplication().evaluateExpressionGet(context, "#{managementsession}", ManagementSession.class);
		return bean;
	}
	
	
	public void updateProjectList() {
		if (projects == null)
			projects = new ArrayList<String>();
		ManagementSession ms = this.getSession();
		if (ms != null) {
			projects.clear();
			try {
				projects.addAll(ms.getBuilder().listProjects(ms.getSvnUrl(), ms.getSvnUser(), ms.getSvnPass()));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public List<String> listProjects() {
		return projects;
	}
	
	public String loadProject(final String projectName) {
		LOGGER.info("load Project {}", projectName);
		this.getSession().setProject(projectName);
		
		ManagementSession ms = this.getSession();
		if(ms != null){
			if(ms.getProject() == null)return null;
			else{
				File projectDir;
				try {
					projectDir = ms.getBuilder().checkoutProject(ms.getProject(), ms.getBaseDir(), ms.getSvnUrl(), ms.getSvnUser(), ms.getSvnPass());
					if(projectDir.exists())return "module_index";
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		}
		return "project_error";
	}
	
	public void unloadProject() {
		LOGGER.info("unload Project {}", this.getSession().getProject());
		this.getSession().setProject(null);

	}

	public String getProject() {
		ManagementSession ms = this.getSession();
		if (ms != null) {
			return ms.getProject();
		}
		return null;
	}

	public void setProject(String project) {
		ManagementSession ms = this.getSession();
		if (ms != null) {
			ms.setProject(project);
		}
	}

}

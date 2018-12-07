package de.dzhw.manager.questionnaire.managedBeans;

import java.io.File;
import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import de.dzhw.manager.admin.managedBeans.ManagementSession;

@Component("questionnaireCreator")
@Scope("session")
public class Creator implements Serializable {

	private static final long serialVersionUID = 1020036240438616255L;
	private static final Logger LOGGER = LoggerFactory.getLogger(Creator.class);
	
	private String projectName;
	private String dbLocation;
	private String dbUser;
	private String dbPass;
	

	public Creator() {
		super();
	}

	@PostConstruct
	private void init() {
		LOGGER.info("init");
		dbLocation = "localhost";
		dbUser = "postgres";
		dbPass = "postgres";
	}
	
	private ManagementSession getSession() {
		final FacesContext context = FacesContext.getCurrentInstance();
		ManagementSession bean = context.getApplication().evaluateExpressionGet(context, "#{managementsession}", ManagementSession.class);
		return bean;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public String getDbLocation() {
		return dbLocation;
	}

	public void setDbLocation(String dbLocation) {
		this.dbLocation = dbLocation;
	}

	public String getDbUser() {
		return dbUser;
	}

	public void setDbUser(String dbUser) {
		this.dbUser = dbUser;
	}

	public String getDbPass() {
		return dbPass;
	}

	public void setDbPass(String dbPass) {
		this.dbPass = dbPass;
	}

	public String createProject() {
		LOGGER.info("create Project {}", projectName);
		
		this.getSession().setProject(projectName);
		
		ManagementSession ms = this.getSession();
		if(ms != null){
			if(ms.getProject() == null)return null;
			else{
//				final File projectDir = ms.getBuilder().buildProject(projectName, dbLocation, dbUser, dbPass, overrideNav, cutHistory, saveMode, login, preloadOnStart, record, this.getSession().getBaseDir(), this.getSession().getSvnUrl(), this.getSession().getSvnUser(), this.getSession().getSvnPass());
				File projectDir;
				try {
					projectDir = ms.getBuilder().buildProject(projectName, dbLocation, dbUser, dbPass, false, true, "forward,backward,same", "TOKEN", false, false, this.getSession().getBaseDir(), this.getSession().getSvnUrl(), this.getSession().getSvnUser(), this.getSession().getSvnPass());
					if(projectDir.exists())return "module_index";
				} catch (Exception e) {
					e.printStackTrace();
				}	
			}
		}
		return "project_error";
	}

}

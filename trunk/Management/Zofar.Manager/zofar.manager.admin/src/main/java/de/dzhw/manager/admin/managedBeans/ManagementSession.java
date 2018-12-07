package de.dzhw.manager.admin.managedBeans;

import java.io.File;
import java.io.Serializable;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import eu.dzhw.zofar.management.dev.builder.BuilderClient;

@Component("managementsession")
@Scope("session")
public class ManagementSession implements Serializable {

	private String svnUrl;
	private String svnUser;
	private String svnPass;
	
	private File baseDir;
	private String project;
	
	private BuilderClient builder;
	
	private static final long serialVersionUID = -6315076272682621742L;
	
	private static final Logger LOGGER = LoggerFactory
			.getLogger(ManagementSession.class);

	public ManagementSession() {
		super();
	}
	
	@PostConstruct
	private void init() {
		LOGGER.info("init");
		this.svnUrl = "https://xxx/svn/hiob/tags/surveys";
		this.svnUser = "xxxx";
		this.svnPass = "xxx";
		this.baseDir = new File("xxx/TestProject");
		this.builder = BuilderClient.getInstance();
	}

	public String getSvnUrl() {
		return svnUrl;
	}

	public String getSvnUser() {
		return svnUser;
	}

	public String getSvnPass() {
		return svnPass;
	}

	public File getBaseDir() {
		return baseDir;
	}

	public String getProject() {
		return project;
	}

	public void setProject(String project) {
		this.project = project;
	}

	public boolean projectExist(){
		if((this.project != null)&&(!this.project.equals(""))){
			final File projectDir = this.getBuilder().getProject(this.project, this.baseDir);
			if(projectDir != null)return projectDir.exists();
		}
		return false;
	}

	public BuilderClient getBuilder() {
		return builder;
	}
}

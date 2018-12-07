package de.dzhw.manager.questionnaire.managedBeans;

import java.io.File;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import de.dzhw.manager.admin.managedBeans.ManagementSession;
import eu.dzhw.zofar.management.utils.xml.XmlClient;

@Component("questionnairedetails")
@Scope("request")
public class Details {

	private static final Logger LOGGER = LoggerFactory.getLogger(Details.class);

	public Details() {
		super();
	}

	@PostConstruct
	private void init() {
		LOGGER.info("init");
	}

	private ManagementSession getSession() {
		final FacesContext context = FacesContext.getCurrentInstance();
		ManagementSession bean = context.getApplication().evaluateExpressionGet(context, "#{managementsession}", ManagementSession.class);
		return bean;
	}

//	private Questionnaire getQuestionnaire() {
//		final FacesContext context = FacesContext.getCurrentInstance();
//		Questionnaire bean = context.getApplication().evaluateExpressionGet(context, "#{questionnaire}", Questionnaire.class);
//		return bean;
//	}
//	
	public String test(){
		LOGGER.info("Details");
		return "test";
	}
	
	private Document loadQmlDocument(){
		ManagementSession ms = this.getSession();
		if(ms != null){
			File projectDir = ms.getBuilder().getProject(ms.getProject(), ms.getBaseDir());
			if(projectDir.exists()){
				final File qmlFile = new File(projectDir,"/src/main/resources/questionnaire.xml");
				final Document back = XmlClient.getInstance().getDocument(qmlFile.getAbsolutePath());
				return back;
			}
		}
		return null;
	}
	
	public String qml(){
		LOGGER.info("QmlDetails");
		final Document qmlDoc = this.loadQmlDocument();
		if(qmlDoc != null){
			return XmlClient.getInstance().show(qmlDoc);
		}
		return "Laden fehlgeschlagen";
	}
	
	public String project(){
		LOGGER.info("ProjectDetails");
		final Document qmlDoc = this.loadQmlDocument();
		if(qmlDoc != null){
			final NodeList participants = XmlClient.getInstance().getByXPath(qmlDoc, "/questionnaire/preloads/*");
			final NodeList variables = XmlClient.getInstance().getByXPath(qmlDoc, "/questionnaire/variables/*");
			final NodeList pages = XmlClient.getInstance().getByXPath(qmlDoc, "/questionnaire/page");
			final StringBuffer back = new StringBuffer();
			back.append("participants : "+participants.getLength()+"\n");
			back.append("variables : "+variables.getLength()+"\n");
			back.append("pages : "+pages.getLength()+"\n");
			
			return back.toString();
		}
		return "Laden fehlgeschlagen";
	}
	


}

package de.his.zofar.presentation.surveyengine.controller;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.ActionEvent;
import javax.faces.event.ActionListener;
import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

/**
 * @author meisner
 * Controller to realize Link-based (Side- and Inline-) Navigation 
 *
 */
@Controller("jumperBean")
@Scope("request")
public class JumperBean implements Serializable,ActionListener{

	private static final long serialVersionUID = -9030015079457000536L;

	private static final Logger LOGGER = LoggerFactory
			.getLogger(JumperBean.class);
	
	@Inject
	private NavigatorBean navigator;
	
	private String target;
	
	@PostConstruct
	private void init() {
//        LOGGER.debug("init");
	}
	
//	public String jump(final String target){
//		LOGGER.info("jump {}",target);
//		this.target = target;
////		navigator.jumperMove(target);
//		return "jump";
//	}

	public String getTarget() {
//		LOGGER.info("get target {}",target);
		String back = navigator.getSameViewID();
		if(target != null){
			navigator.jumperMove(target);
			back = target;
		}
		return back;
	}

	public void setTarget(final String target) {
		this.target = target;
	}
	
	public boolean active(){
//		LOGGER.info("active {}",(target != null));
		return (target != null);
	}
	
	public boolean sideNavigationEnabled(){
		return true;
	}
	
	public boolean topNavigationEnabled(){
		return true;
	}

	@Override
	public void processAction(ActionEvent event)
			throws AbortProcessingException {
		target = (String)event.getComponent().getAttributes().get("targetPage");
//	LOGGER.info("processAction {}",target);		
	}
}

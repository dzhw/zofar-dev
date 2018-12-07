package de.his.zofar.presentation.surveyengine.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.ActionEvent;
import javax.faces.event.ActionListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.his.zofar.presentation.surveyengine.ui.interfaces.Task;

/**
 * Bean to execute as onExit marked Trigger Tasks on Page-Exit
 * i.e. when ActionListener on backward- or forward-Button is triggered
 * @author meisner
 * 
 */
@ManagedBean(name="onExitBean")
@ViewScoped
public class OnExitBean implements Serializable,ActionListener {
	
	private static final long serialVersionUID = -4619131763277549008L;
	private static final Logger LOGGER = LoggerFactory
			.getLogger(OnExitBean.class);
	
	private List<Task> tasks;

	public OnExitBean() {
		super();
	}

	@PostConstruct
	private void init(){
//		LOGGER.info("init");
		tasks = new ArrayList<Task>();
	}
	
	/**
	 * Queue Task till PageBlur
	 * @param task to queue
	 */
	private final Map<Task,Map<String,Object>> dumps = new HashMap<Task,Map<String,Object>>();
	
	
	public void executeBeforeExit(final Task task){
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("execute before exit {}",task);
		}
		
		dumps.put(task, task.dump());
		tasks.add(task);
	}
	
	/**
	 * Page Blur trigger
	 * cause execution of queued Tasks
	 */
	public void exit(){
//		LOGGER.info("exit");
		final Iterator<Task> it = tasks.iterator();
		while(it.hasNext()){
			final Task task = it.next();
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("execute task {} ==> {}",task,dumps.get(task));
			}
			
			task.executeTask(dumps.get(task));
		}
	}

	/** Hook to trigger Page Blur
	 * @see javax.faces.event.ActionListener#processAction(javax.faces.event.ActionEvent)
	 */
	@Override
	public void processAction(final ActionEvent actionEvent)
			throws AbortProcessingException {
		exit();
		
	}

}

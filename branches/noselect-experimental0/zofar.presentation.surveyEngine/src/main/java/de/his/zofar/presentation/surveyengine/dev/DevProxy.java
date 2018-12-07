package de.his.zofar.presentation.surveyengine.dev;

import java.io.Serializable;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.his.zofar.presentation.surveyengine.util.JsfUtility;

/**
 * Bean to provide Zofar specific EL - Functions
 * 
 * @author meisner
 * 
 */
//@ManagedBean(name = "devProxy")
//@RequestScoped
public class DevProxy implements Serializable {

	private static final long serialVersionUID = -6033696257041042442L;
	private static final Logger LOGGER = LoggerFactory
			.getLogger(DevProxy.class);
	
	private Map<String,String> registered;
	private Map<String,String> visibleRegistered;
	
	public DevProxy() {
		super();
	}
	

	@PostConstruct
	private void init() {
		LOGGER.info("init");
		registered = new LinkedHashMap<String,String>();
		visibleRegistered = new LinkedHashMap<String,String>();
	}
	
	public Object analyze(final String id,final String expression,final String mode){
		return this.analyze(id, expression, mode,false);
	}
	
	public Object analyze(final String id,final String expression,final String mode,final boolean override){
		final Object result =  JsfUtility.getInstance().evaluateValueExpression(FacesContext.getCurrentInstance(), "#{"+expression+"}", Boolean.class);
		final String key = id+" ("+mode+")";
//		final String visibleKey = "form:"+id;
		final String value = expression+" ==> "+result;
//		LOGGER.info("analyze key: "+key+" value: "+value);
//		System.out.println("analyze key: "+key+" value: "+value);
//		registered.put(key, value);
//		visibleRegistered.put(visibleKey.replaceAll(":", "\\\\\\\\:"), expression);
		if(override)return true;
		//final Object result =  JsfUtility.getInstance().evaluateValueExpression(FacesContext.getCurrentInstance(), "#{true}", Object.class);
		return result;
	}
	
	public boolean isEmpty() {
		return registered.isEmpty();
	}

	public Map<String, String> getRegistered() {
		return registered;
	}
	
	public boolean isVisiblesEmpty() {
		return visibleRegistered.isEmpty();
	}

	public Map<String, String> getVisibleRegistered() {
		return visibleRegistered;
	}
	
	public List<Object> getRegisteredKeys(){
	     return Arrays.asList(registered.keySet().toArray());
	}
	
	public List<Object> getVisibleRegisteredKeys(){
	     return Arrays.asList(visibleRegistered.keySet().toArray());
	}

}

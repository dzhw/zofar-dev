package de.his.zofar.presentation.surveyengine.util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SystemConfiguration implements Serializable {

	private static final long serialVersionUID = 7858422804128782036L;
	private static final SystemConfiguration INSTANCE = new SystemConfiguration();
	private Properties properties = null;
	
    private static final Logger LOGGER = LoggerFactory
            .getLogger(SystemConfiguration.class);

	private SystemConfiguration() {
		super();
        final JsfUtility jsfUtility = JsfUtility.getInstance();
        if(jsfUtility != null){
        		try {
        			properties = new Properties();
        			properties.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("system.properties"));
				} catch (FileNotFoundException e) {
					properties = null;
					LOGGER.error("Configuration file not found ==> {}",e);
				} catch (IOException e) {
					properties = null;
					LOGGER.error("Configuration file cannot be read ==> {}",e);
				}
        }
	}

	public static final SystemConfiguration getInstance() {
		return INSTANCE;
	}
	
	public Boolean overrideNavigation(){
//		LOGGER.debug("overrideNavigation");
		if((properties != null)&&(properties.containsKey("overrideNavigation")))return Boolean.parseBoolean((String)properties.get("overrideNavigation"));
		return false;
	}
	
	public Boolean preloadOnStart(){
		LOGGER.debug("preload_on_start");
		if((properties != null)&&(properties.containsKey("preload_on_start")))return Boolean.parseBoolean((String)properties.get("preload_on_start"));
		return false;
	}
	
	public String loginMode(){
//		LOGGER.debug("login");
		if((properties != null)&&(properties.containsKey("login")))return (String)properties.get("login");
		return "UNKOWN";
	}
	
	public Boolean cutHistory(){
//		LOGGER.debug("cutHistory");
		if((properties != null)&&(properties.containsKey("cutHistory")))return Boolean.parseBoolean((String)properties.get("cutHistory"));
		return false;
	}
	
	private List<String> movementStates(){
		if((properties != null)&&(properties.containsKey("saveMode"))){
			final List<String> back = new ArrayList<String>();
			
			final Iterator<String> iterator = Arrays.asList(properties.getProperty("saveMode").split(",")).iterator();

			while(iterator.hasNext()){
				back.add((iterator.next()).toLowerCase());
			}
			return back;
		}
		return null;
	}

	public Boolean saveOnForward(){
		if(movementStates() == null)return true;
		if((movementStates() != null)&&(movementStates().contains("forward")))return true;
		return false;
	}
	
	public Boolean saveOnBackward(){
		if(movementStates() == null)return true;
		if((movementStates() != null)&&(movementStates().contains("backward")))return true;
		return false;
	}
	
	public Boolean saveOnSame(){
		if(movementStates() == null)return true;
		if((movementStates() != null)&&(movementStates().contains("same")))return true;
		return false;
	}
	
	public Boolean record(){
//		LOGGER.debug("record");
		if((properties != null)&&(properties.containsKey("record")))return Boolean.parseBoolean((String)properties.get("record"));
		return false;
	}
	
	public String recordDir(){
//		LOGGER.debug("recordDir");
		if((properties != null)&&(properties.containsKey("recorddir")))return (String)properties.get("recorddir");
		return System.getProperty("java.io.tmpdir");
	}
	
	public Boolean cluster(){
		if((properties != null)&&(properties.containsKey("cluster")))return Boolean.parseBoolean((String)properties.get("cluster"));
		return false;
	}
}

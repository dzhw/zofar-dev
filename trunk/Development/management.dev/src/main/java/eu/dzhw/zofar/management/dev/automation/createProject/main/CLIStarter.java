package eu.dzhw.zofar.management.dev.automation.createProject.main;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.dzhw.zofar.management.dev.automation.AbstractExecutor.ABSTRACTPARAMETER;
import eu.dzhw.zofar.management.dev.automation.AbstractExecutor.ParameterMap;
import eu.dzhw.zofar.management.dev.automation.createProject.Executor;
import eu.dzhw.zofar.management.utils.system.ConfigurationUtils;

public class CLIStarter {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(CLIStarter.class);
	

	public static void main(String[] args) {
		final HashSet<String> PARAMETERNAMES = new HashSet<String>();
		PARAMETERNAMES.add("SVN_SERVER");
		PARAMETERNAMES.add("SVN_USER");
		PARAMETERNAMES.add("SVN_PASS");
		
		PARAMETERNAMES.add("JENKINS_SERVER");
		PARAMETERNAMES.add("JENKINS_USER");
		PARAMETERNAMES.add("JENKINS_PASS");
			
		PARAMETERNAMES.add("DB_SERVER");
		PARAMETERNAMES.add("DB_PORT");
		PARAMETERNAMES.add("DB_USER");
		PARAMETERNAMES.add("DB_PASS");
			
		PARAMETERNAMES.add("SURVEY_NAME");
		PARAMETERNAMES.add("SURVEY_ADDON_NAME");
		
		PARAMETERNAMES.add("WORK_DIR");

		
		final Map<String, String> additionalArgs = new HashMap<String, String>();
		if ((args != null) && (args.length > 0)) {
			final int argCount = args.length;
			for (int a = 0; a < argCount; a++) {
				final String arg = args[a];
				final String[] splittedArg = arg.split("=");
				if ((splittedArg != null) && (splittedArg.length == 2)) {
					final String key = splittedArg[0].trim();
					final String value = splittedArg[1].trim();
					additionalArgs.put(key, value);
				}
				LOGGER.info("Argument {}", args[a]);
			}
		}

		Properties configuration = null;
		
		// Read parameter from Configurationfile
		if(additionalArgs.containsKey("config")){
			configuration = ConfigurationUtils.getInstance().getConfigurationFromFileSystem(additionalArgs.get("config"));
			if(configuration == null){
				System.err.println("Config File do not exist or is not readable");
			}
		}
		
		if(configuration == null)configuration = new Properties();
		
		// Override Parameter in Configuration by Commandlineparameter	
		for(final Map.Entry<String, String> additionalArg:additionalArgs.entrySet()){
			if(PARAMETERNAMES.contains(additionalArg.getKey()))configuration.setProperty(additionalArg.getKey(), additionalArg.getValue());
		}
		
		boolean flag = true;
		//Check for completeness
		for(final String PARAMETERNAME : PARAMETERNAMES){
			if(!configuration.containsKey(PARAMETERNAME)){
				System.err.println("Parameter "+PARAMETERNAME+" is missing");
				flag = false;
			}
		}
		
		if(!flag)System.exit(1);
				
		final Executor executor = Executor.getInstance();
		
		try {
			LOGGER.info("process Survey " +(String)configuration.get("SURVEY_NAME")+" ("+ (String)configuration.get("SURVEY_ADDON_NAME")+")");
			ParameterMap<ABSTRACTPARAMETER, Object>  parameter = executor.getParameterMap((String)configuration.get("SURVEY_NAME"), (String)configuration.get("SURVEY_ADDON_NAME"),
					new File((String)configuration.get("WORK_DIR")), (String)configuration.get("SVN_SERVER"), (String)configuration.get("SVN_USER"), (String)configuration.get("SVN_PASS"),
					(String)configuration.get("JENKINS_SERVER"), (String)configuration.get("JENKINS_USER"), (String)configuration.get("JENKINS_PASS"), (String)configuration.get("DB_SERVER"), (String)configuration.get("DB_PORT"), (String)configuration.get("DB_USER"), (String)configuration.get("DB_PASS"));
			executor.process(parameter);
			LOGGER.info("done");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}

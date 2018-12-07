package eu.dzhw.zofar.management.dev.automation.deploy.main;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.dzhw.zofar.management.dev.automation.AbstractExecutor.ABSTRACTPARAMETER;
import eu.dzhw.zofar.management.dev.automation.AbstractExecutor.ParameterMap;
import eu.dzhw.zofar.management.dev.automation.deploy.Executor;
import eu.dzhw.zofar.management.utils.objects.CollectionClient;
import eu.dzhw.zofar.management.utils.system.ConfigurationUtils;

public class CLIStarter {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(CLIStarter.class);
	

	public static void main(String[] args) {
		final HashSet<String> PARAMETERNAMES = new HashSet<String>();
		PARAMETERNAMES.add("SVN_SERVER");
		PARAMETERNAMES.add("SVN_USER");
		PARAMETERNAMES.add("SVN_PASS");
		
		PARAMETERNAMES.add("CLUSTER_MAP");
		PARAMETERNAMES.add("APP_SSH_USER");
		PARAMETERNAMES.add("APP_SSH_PASS");
		PARAMETERNAMES.add("APP_LOGIN_CHAIN");
		
		PARAMETERNAMES.add("LB_SERVER");
		PARAMETERNAMES.add("LB_SSH_USER");
		PARAMETERNAMES.add("LG_SSH_PASS");
		
		PARAMETERNAMES.add("DB_SERVER");
		PARAMETERNAMES.add("DB_PORT");
		PARAMETERNAMES.add("DB_USER");
		PARAMETERNAMES.add("DB_PASS");
		
		PARAMETERNAMES.add("MONITOR_SERVER");
		PARAMETERNAMES.add("MONITOR_SSH_USER");
		PARAMETERNAMES.add("MONITOR_SSH_PASS");
		
		PARAMETERNAMES.add("CLUSTER_SELECT");
		
		PARAMETERNAMES.add("SURVEY_NAME");
		PARAMETERNAMES.add("SURVEY_ADDON_NAME");
		PARAMETERNAMES.add("SURVEY_BASE_URL");
		
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
		
//		additionalArgs.put("config","xxxx/Projekte/Automation/deploy.properties");

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
		
		Map<String, List<String>> clusterMap = new LinkedHashMap<String, List<String>>();
		final String[] clusters = ((String)configuration.get("CLUSTER_MAP")).split(Pattern.quote(";"));
		if(clusters != null){
			for(final String cluster:clusters){
				final String[] pair = cluster.split(Pattern.quote("*"));
				if((pair != null)&&(pair.length == 2)){
					final String clustername = pair[0];
					final String serverListStr = pair[1].substring(pair[1].indexOf('{')+1, pair[1].lastIndexOf('}'));				
					List<String> servers = CollectionClient.getInstance().explode(serverListStr, ",");
					clusterMap.put(clustername.trim(), servers);
				}
			}
		}
		
		Map<String, String> appLoginChain = new LinkedHashMap<String, String>();
		final String[] logins = ((String)configuration.get("APP_LOGIN_CHAIN")).split(Pattern.quote(","));
		if(logins != null){
			for(final String login:logins){
				final String[] pair = login.split(Pattern.quote(":"));
				if((pair != null)&&(pair.length == 2)){
					final String user = pair[0];
					final String pass = pair[1];
					appLoginChain.put(user.trim(), pass.trim());
				}
			}
		}
		List<String> clusterNames = new ArrayList<String>();
		final String[] clusterNamesStrs = ((String)configuration.get("CLUSTER_SELECT")).split(Pattern.quote(","));
		if(clusterNamesStrs != null){
			for(final String clusterNamesStr:clusterNamesStrs){
				clusterNames.add(clusterNamesStr.trim());
			}
		}
		
		final Executor executor = Executor.getInstance();
		
		try {
			LOGGER.info("process Survey " +(String)configuration.get("SURVEY_NAME")+" ("+ (String)configuration.get("SURVEY_ADDON_NAME")+")");
			ParameterMap<ABSTRACTPARAMETER, Object>  parameter = executor.getParameterMap((String)configuration.get("SURVEY_NAME"), (String)configuration.get("SURVEY_ADDON_NAME"), new File((String)configuration.get("WORK_DIR")), (String)configuration.get("SVN_SERVER"), (String)configuration.get("SVN_USER"), (String)configuration.get("SVN_PASS"), (String)configuration.get("DB_SERVER"), (String)configuration.get("DB_PORT"), (String)configuration.get("DB_USER"), (String)configuration.get("DB_PASS"), clusterMap, clusterNames, (String)configuration.get("LB_SERVER"), (String)configuration.get("LB_SSH_USER"), (String)configuration.get("LG_SSH_PASS"), (String)configuration.get("MONITOR_SERVER"), (String)configuration.get("MONITOR_SSH_USER"), (String)configuration.get("MONITOR_SSH_PASS"), (String)configuration.get("APP_SSH_USER"), (String)configuration.get("APP_SSH_PASS"), appLoginChain, (String)configuration.get("SURVEY_BASE_URL"));
			executor.process(parameter);
			LOGGER.info("done");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}

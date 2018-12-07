package eu.dzhw.zofar.management.dev.automation.reminder.main;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.dzhw.zofar.management.dev.automation.AbstractExecutor.ABSTRACTPARAMETER;
import eu.dzhw.zofar.management.dev.automation.AbstractExecutor.ParameterMap;
import eu.dzhw.zofar.management.dev.automation.reminder.Executor;
import eu.dzhw.zofar.management.utils.files.FileClient;
import eu.dzhw.zofar.management.utils.system.ConfigurationUtils;

public class CLIStarter {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(CLIStarter.class);

	public static void main(String[] args) {
		final HashSet<String> PARAMETERNAMES = new HashSet<String>();
		PARAMETERNAMES.add("DB_NAME");
		PARAMETERNAMES.add("DB_SERVER");
		PARAMETERNAMES.add("DB_PORT");
		PARAMETERNAMES.add("DB_USER");
		PARAMETERNAMES.add("DB_PASS");
		
		PARAMETERNAMES.add("MAILBOX_NAME");
		PARAMETERNAMES.add("MAILBOX_SERVER");
		PARAMETERNAMES.add("MAILBOX_USER");
		PARAMETERNAMES.add("MAILBOX_PASS");
		PARAMETERNAMES.add("MAILBOX_PATH");
		
		PARAMETERNAMES.add("COLUMN_TOKEN");
		PARAMETERNAMES.add("COLUMN_MAIL");
		
		PARAMETERNAMES.add("COLUMN_CHAR_DELIMITER");
		PARAMETERNAMES.add("COLUMN_CHAR_QUOTE");
		
		PARAMETERNAMES.add("FILE_INVITATIONS");
		PARAMETERNAMES.add("FILE_BLACKLIST");
		PARAMETERNAMES.add("DIR_OUTPUT");
		
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
			System.out.println("Configuration : "+PARAMETERNAME+" = "+configuration.getProperty(PARAMETERNAME));
			if(!configuration.containsKey(PARAMETERNAME)){
				System.err.println("Parameter "+PARAMETERNAME+" is missing");
				flag = false;
			}
		}
		
		if(!flag)System.exit(1);
		
		final FileClient file = FileClient.getInstance();
		
		final File outputDir = new File((String)configuration.get("DIR_OUTPUT"));
		
		final File invitations = new File((String)configuration.get("FILE_INVITATIONS"));
		final File blacklist = new File((String)configuration.get("FILE_BLACKLIST"));
		final File ignore = file.createOrGetFile(file.getNameWithoutSuffix(invitations)+"_ignore", "."+file.getSuffix(invitations), outputDir);
		final File remind = file.createOrGetFile(file.getNameWithoutSuffix(invitations)+"_remind", "."+file.getSuffix(invitations), outputDir);
		
		final Character delimiter = ((String)configuration.get("COLUMN_CHAR_DELIMITER")).charAt(0);
		final Character quote = ((String)configuration.get("COLUMN_CHAR_QUOTE")).charAt(0);
		
		
		final Executor executor = Executor.getInstance();
		
		try {
			ParameterMap<ABSTRACTPARAMETER, Object> parameters = executor.getParameterMap((String)configuration.get("MAILBOX_NAME"), (String)configuration.get("MAILBOX_PATH"), (String)configuration.get("MAILBOX_SERVER"), (String)configuration.get("MAILBOX_USER"), (String)configuration.get("MAILBOX_PASS"), (String)configuration.get("DB_SERVER"), (String)configuration.get("DB_NAME"), (String)configuration.get("DB_PORT"), (String)configuration.get("DB_USER"), (String)configuration.get("DB_PASS"), invitations,blacklist,remind,ignore,(String)configuration.get("COLUMN_TOKEN"),(String)configuration.get("COLUMN_MAIL"),delimiter,quote);
			
			for(final Map.Entry<ABSTRACTPARAMETER, Object> parameter : parameters.entrySet()){
				System.out.println("Parameter : "+parameter.getKey()+" = "+parameter.getValue());
			}
			
			
			executor.process(parameters);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}

package eu.dzhw.zofar.management.dev.automation.screenshot.main;

import java.awt.Dimension;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Pattern;

import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.dzhw.zofar.management.dev.automation.AbstractExecutor.ABSTRACTPARAMETER;
import eu.dzhw.zofar.management.dev.automation.AbstractExecutor.ParameterMap;
import eu.dzhw.zofar.management.dev.automation.screenshot.Executor;
import eu.dzhw.zofar.management.utils.files.DirectoryClient;
import eu.dzhw.zofar.management.utils.files.FileClient;
import eu.dzhw.zofar.management.utils.files.PackagerClient;
import eu.dzhw.zofar.management.utils.odf.TextClient;
import eu.dzhw.zofar.management.utils.odf.components.WriterDocument;
import eu.dzhw.zofar.management.utils.system.ConfigurationUtils;

public class CLIStarter {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(CLIStarter.class);

	public static void main(String[] args) {
		final HashSet<String> PARAMETERNAMES = new HashSet<String>();
		PARAMETERNAMES.add("SVN_SERVER");
		PARAMETERNAMES.add("SVN_USER");
		PARAMETERNAMES.add("SVN_PASS");
		
		PARAMETERNAMES.add("APP_URL_PROTOCOL");
		PARAMETERNAMES.add("APP_SERVER");
		PARAMETERNAMES.add("APP_URL_PORT");

		PARAMETERNAMES.add("APP_SSH_USER");
		PARAMETERNAMES.add("APP_SSH_PASS");
		PARAMETERNAMES.add("APP_LOGIN_CHAIN");
		
		PARAMETERNAMES.add("DB_SERVER");
		PARAMETERNAMES.add("DB_PORT");
		PARAMETERNAMES.add("DB_USER");
		PARAMETERNAMES.add("DB_PASS");
		
		PARAMETERNAMES.add("CONFIG_MDM");
		PARAMETERNAMES.add("CONFIG_NOVISIBLEMAP");

		PARAMETERNAMES.add("SURVEY_LIST");
		PARAMETERNAMES.add("SURVEY_TOKEN");
		
		PARAMETERNAMES.add("WORK_DIR");
		PARAMETERNAMES.add("SCREEN_DIR");
		
		final ArrayList<Dimension> dimensions = new ArrayList<Dimension>();
		dimensions.add(new Dimension(1200,1024));
		dimensions.add(new Dimension(768,1024));
		dimensions.add(new Dimension(766,1024));
		
		final List<String> languages = new ArrayList<String>();
		languages.add("de");
		languages.add("en");
		
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
		
		final DirectoryClient dirClient = DirectoryClient.getInstance();
		final TextClient textClient = TextClient.getInstance();

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
		
		final String appUrl = (String)configuration.get("APP_URL_PROTOCOL")+"://"+(String)configuration.get("APP_SERVER")+":"+(String)configuration.get("APP_URL_PORT");		
		final File workDir = new File((String)configuration.get("WORK_DIR"));
		final File screenDir = new File((String)configuration.get("SCREEN_DIR"));
		
		final List<String> surveys = new ArrayList<String>();
		final String[] surveysStrs = ((String)configuration.get("SURVEY_LIST")).split(Pattern.quote(","));
		if(surveysStrs != null){
			for(final String surveysStr:surveysStrs){
				surveys.add(surveysStr.trim());
			}
		}
		
//		System.out.println("SVN URL : "+(String)configuration.get("SVN_SERVER"));
//		
//		System.exit(1);
						
		final Executor executor = Executor.getInstance();
		final long start = System.currentTimeMillis();
		for(final String survey : surveys){
			LOGGER.info("process Survey " + survey);
			try {
				File surveyScreenshotDir = dirClient.createDir(screenDir,survey);
				dirClient.cleanDirectory(surveyScreenshotDir);
				ParameterMap<ABSTRACTPARAMETER, Object>  parameter = executor.getParameterMap(survey, workDir,surveyScreenshotDir,(String)configuration.get("SVN_SERVER"), (String)configuration.get("SVN_USER"), (String)configuration.get("SVN_PASS"), (String)configuration.get("DB_SERVER"), (String)configuration.get("DB_PORT"), (String)configuration.get("DB_USER"), (String)configuration.get("DB_PASS"),appUrl, (String)configuration.get("APP_SSH_USER"), (String)configuration.get("APP_SSH_PASS"), appLoginChain, Boolean.parseBoolean((String)configuration.get("CONFIG_MDM")), Boolean.parseBoolean((String)configuration.get("CONFIG_NOVISIBLEMAP")),(String)configuration.get("SURVEY_TOKEN"));
				executor.process(parameter,dimensions,true,languages);
				
				//Produce Wrapper Doc
				try {
					final WriterDocument doc = textClient.createDocument();

					textClient.addTOC(doc, "Seiten");
					textClient.addBreak(doc);
					List<File> files = dirClient.readDir(surveyScreenshotDir);

					for(final File file:files){
						String ext = FilenameUtils.getExtension(file.getAbsolutePath());
						if(!ext.equals("png"))continue;
						String name = file.getName().replaceAll(((String)configuration.get("APP_URL_PROTOCOL"))+"_"+((String)configuration.get("APP_SERVER"))+"_"+((String)configuration.get("APP_URL_PORT"))+"_"+survey+"_"+survey+"_", "");
						name = name.replaceAll("_html", "");
						name = name.replaceAll(Pattern.quote("."+ext), "");
						
						textClient.addBreak(doc);
						textClient.addHeading(doc, name);
						textClient.addImage(doc,file,900,600);
					}
					textClient.saveDocument(doc, surveyScreenshotDir.getAbsolutePath()+File.separator+survey+".odf");

				} catch (final Exception e) {
					e.printStackTrace();
				}
				//compress images
				final File compressed = FileClient.getInstance().createOrGetFile(survey+"_screenshots", ".zip", screenDir);

				try {
					Map<String, Object> packageObj = new HashMap<String, Object>();
					Map<String, Object> screenshots = new HashMap<String, Object>();
					
					final List<File> screenshotList = DirectoryClient.getInstance().readDir(surveyScreenshotDir);
					for(final File file : screenshotList)screenshots.put(file.getName(), file);
					packageObj.put("screenshots", screenshots);
					DirectoryClient.getInstance().deleteDir(surveyScreenshotDir.getParentFile(), surveyScreenshotDir.getName());
					PackagerClient.getInstance().packageZip(compressed, packageObj);
				} catch (IOException e) {
					e.printStackTrace();
				}
			} catch (Exception e) {
				LOGGER.error("failed to process Survey " + survey + " (" + e.getMessage() + ")");
			}
		}
		final long stop = System.currentTimeMillis();
		System.out.println("needed Time : "+((stop-start)/1000/60)+" Minutes");
	}
}

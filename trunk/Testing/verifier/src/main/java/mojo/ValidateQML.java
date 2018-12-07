package mojo;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import main.AllTests;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.io.Files;

import eu.dzhw.zofar.management.comm.svn.SVNClient;


/**
 * @author meisner
 * 
 */
@Mojo(name = "validate-qml")
public class ValidateQML extends AbstractMojo {
	
	final static Logger LOGGER = LoggerFactory.getLogger(ValidateQML.class);
	
    @Parameter(defaultValue = "${contentPath}", required = true)
    private String contentPath;
    
    @Parameter(defaultValue = "${schemaPath}", required = false)
    private String schemaPath;
    
    @Parameter(defaultValue = "${svnServer}", required = false)
    private String svnServer;
    
    @Parameter(defaultValue = "${svnUser}", required = false)
    private String svnUser;
    
    @Parameter(defaultValue = "${svnPass}", required = false)
    private String svnPass;
    
    @Parameter(defaultValue = "${svnPath}", required = false)
    private String svnPath;
    
    @Parameter(defaultValue = "${qmlSchemaFileName}", required = false)
    private String qmlSchemaFileName;    
    
    @Parameter(defaultValue = "${navigationSchemaFileName}", required = false)
    private String navigationSchemaFileName;    
    
    @Parameter(defaultValue = "${displaySchemaFileName}", required = false)
    private String displaySchemaFileName;
    
    @Parameter(defaultValue = "${researchdatacenterSchemaFileName}", required = false)
    private String researchdatacenterSchemaFileName;

	public void execute() throws MojoExecutionException, MojoFailureException {
		this.getLog().info("start validation");
		System.setProperty("suite", "true");
		System.setProperty("runMode", "maven");
		final Map<String, String> additionalArgs = new HashMap<String, String>();
		additionalArgs.put("contentPath", contentPath);
		
		File schemaDir = null;
		File questionnaire = null;
		if((svnServer != null)&&(svnUser != null)&&(svnPass != null)&&(svnPath != null)){
			try{
				//load schema files
				SVNClient svn = SVNClient.getInstance();
				schemaDir = Files.createTempDir();
				if(qmlSchemaFileName != null)questionnaire = svn.getFile(svnServer, svnUser, svnPass, svnPath+"/"+qmlSchemaFileName,schemaDir);
				if(displaySchemaFileName != null)svn.getFile(svnServer, svnUser, svnPass, svnPath+"/"+displaySchemaFileName,schemaDir);
				if(navigationSchemaFileName != null)svn.getFile(svnServer, svnUser, svnPass, svnPath+"/"+navigationSchemaFileName,schemaDir);
				if(researchdatacenterSchemaFileName != null)svn.getFile(svnServer, svnUser, svnPass, svnPath+"/"+researchdatacenterSchemaFileName,schemaDir);
				for(String file : schemaDir.list()){
					LOGGER.info("loaded schema file from svn : {} ({})",file,schemaDir.getAbsolutePath());
				}
			}
			catch (Exception e) {
				throw new MojoFailureException(e.getMessage());
			}
		}
		
		if(questionnaire != null)additionalArgs.put("schemaPath", questionnaire.getAbsolutePath());
		else if(schemaPath != null)additionalArgs.put("schemaPath", schemaPath);
		else throw new MojoFailureException("no schema source found");
		if (!additionalArgs.isEmpty()) {
			final Iterator<String> it = additionalArgs.keySet().iterator();
			while (it.hasNext()) {
				final String key = it.next();
				final String value = additionalArgs.get(key);
				System.setProperty(key, value);
			}
		}

		final JUnitCore runner = new JUnitCore();
		final Result result = runner.run(AllTests.class);
		boolean warning = true;
		List<String> failures = new ArrayList<String>();
		for (final Failure failure : result.getFailures()) {
//			MessageProvider.info(failure.getDescription().getTestClass(),
//					"!! {}", failure.getMessage());
			this.getLog().info(failure.getDescription().getTestClass()+
					" !! "+failure.getMessage());
			failures.add(failure.getDescription().getTestClass()+" !! "+failure.getMessage());			
			if((tests.integrity.VariableIntegrity.class).isAssignableFrom(failure.getDescription().getTestClass())){
				if(!failure.getMessage().startsWith("Multiple time used Variables found"))warning = false;
			}
			else{
				warning = false;
			}
		}
		System.clearProperty("suite");
		System.clearProperty("runMode");
		if (!additionalArgs.isEmpty()) {
			final Iterator<String> it = additionalArgs.keySet().iterator();
			while (it.hasNext()) {
				final String key = it.next();
				System.clearProperty(key);
			}
		}
		if(schemaDir != null){
			schemaDir.delete();
		}
		if(!failures.isEmpty()){	
			if(warning){
				LOGGER.info("[WARN] {}",failures.toString());
			}
			else throw new MojoFailureException(failures.toString());
		}
	}

}

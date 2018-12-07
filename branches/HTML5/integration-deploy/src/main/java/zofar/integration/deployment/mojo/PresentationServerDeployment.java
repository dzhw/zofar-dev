package zofar.integration.deployment.mojo;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Pattern;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.apache.maven.shared.invoker.MavenInvocationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

import eu.dzhw.zofar.management.comm.ssh.SSHClient;
import eu.dzhw.zofar.management.dev.maven.MavenClient;
import eu.dzhw.zofar.management.utils.files.DirectoryClient;
import eu.dzhw.zofar.management.utils.files.FileClient;
import eu.dzhw.zofar.management.utils.system.ConfigurationUtils;

/**
 * @author meisner
 * 
 */
@Mojo(name = "presentation-deploy")
public class PresentationServerDeployment extends AbstractMojo {

	@Parameter(defaultValue = "${sshIdentities}", required = false)
	private String sshIdentities;

	@Parameter(defaultValue = "${jumpServer}", required = true)
	private String jumpServer;

	@Parameter(defaultValue = "${jumpSSHUser}", required = true)
	private String jumpSSHUser;

	@Parameter(defaultValue = "${jumpSSHPass}", required = true)
	private String jumpSSHPass;

	@Parameter(defaultValue = "xxxx", required = true)
	private String presentationServer;

	@Parameter(defaultValue = "xxxx", required = true)
	private String presentationSSHUser;

	@Parameter(defaultValue = "XXXXXX!", required = true)
	private String presentationSSHPass;

	@Parameter(defaultValue = "admin", required = true)
	private String presentationManagerUser;

	@Parameter(defaultValue = "xxxx", required = true)
	private String presentationManagerPass;

	@Parameter(defaultValue = "xxxx", required = true)
	private String dbServer;

	@Parameter(defaultValue = "5432", required = true)
	private String dbPort;

	@Parameter(defaultValue = "${dbName}", required = true)
	private String dbName;

	@Parameter(defaultValue = "xxxx", required = true)
	private String dbUser;

	@Parameter(defaultValue = "xxxx", required = true)
	private String dbPass;

	@Parameter(defaultValue = "${project}", required = true)
	private MavenProject project;
	
	@Parameter(defaultValue = "${debugMode}", required = true)
	private boolean debugMode;
	
	@Parameter(defaultValue = "/var/lib/jenkins/.m2/settings.xml", required = true)
	private String mavenSettings;
	
	private File projectDir = null;

	protected final MavenClient mavenClient;

	final static Logger LOGGER = LoggerFactory.getLogger(PresentationServerDeployment.class);

	public PresentationServerDeployment() {
		super();
		mavenClient = MavenClient.getInstance();
	}

	public PresentationServerDeployment(String sshIdentities, String jumpServer, String jumpSSHUser, String jumpSSHPass, String presentationServer, String presentationSSHUser, String presentationSSHPass, String presentationManagerUser, String presentationManagerPass, String dbServer, String dbPort, String dbName, String dbUser, String dbPass, File projectDir, MavenClient mavenClient) {
		super();
		this.sshIdentities = sshIdentities;
		this.jumpServer = jumpServer;
		this.jumpSSHUser = jumpSSHUser;
		this.jumpSSHPass = jumpSSHPass;
		this.presentationServer = presentationServer;
		this.presentationSSHUser = presentationSSHUser;
		this.presentationSSHPass = presentationSSHPass;
		this.presentationManagerUser = presentationManagerUser;
		this.presentationManagerPass = presentationManagerPass;
		this.dbServer = dbServer;
		this.dbPort = dbPort;
		this.dbName = dbName;
		this.dbUser = dbUser;
		this.dbPass = dbPass;
		this.projectDir = projectDir;
		this.mavenClient = mavenClient;
	}



	@Override
	public void execute() throws MojoExecutionException, MojoFailureException {
		final SSHClient sshClient = SSHClient.getInstance();
		sshClient.getKownHosts();

		final Map<String, String> jumpIdentities = new HashMap<String, String>();
		
		if(sshIdentities != null) {
			final String[] identityPairs = sshIdentities.split("##");
			if (identityPairs != null) {
				for(final String identityPair:identityPairs){
					final String[] identity = identityPair.split("=");
					if((identity != null)&&(identity.length >= 2))
					jumpIdentities.put(identity[0],identity[1]);
				}
			}
		}
		


		final ArrayList<String> jumpPasswords = new ArrayList<String>();
		final String[] jumpSSHPasswordsStrs = jumpSSHPass.split("##");
		if (jumpSSHPasswordsStrs != null) {
			for(final String jumpPasswordsStr:jumpSSHPasswordsStrs){
				jumpPasswords.add(jumpPasswordsStr);
			}
		}

		final Map<String, String> presentationIdentities = new HashMap<String, String>();

		final ArrayList<String> presentationPasswords = new ArrayList<String>();
		final String[] presentationSSHPasswordsStrs = presentationSSHPass.split("##");
		if (presentationSSHPasswordsStrs != null) {
			for(final String presentationSSHPasswordsStr:presentationSSHPasswordsStrs){
				presentationPasswords.add(presentationSSHPasswordsStr);
			}
		}

		Session proxied = null;

		try {
			proxied = sshClient.connectByProxy(9099, jumpServer, jumpSSHUser, jumpPasswords, jumpIdentities, presentationServer, presentationSSHUser, presentationPasswords, presentationIdentities);
			if (proxied != null) {
				final File warFile = build();
				final String appName = warFile.getName().replaceAll(Pattern.quote(".war"), "");
				
				System.out.println("debugMode : "+debugMode);
				
				if(debugMode) {
					System.out.println("Debug Mode. Skipping real deployment of "+warFile.getAbsolutePath());
				}
				else {
					final String serverUrl = "http://xxxxx:8080";
					
					final String list = "'"+serverUrl+"/manager/text/list'";
					final String undeploy = "'"+serverUrl+"/manager/text/undeploy?path=/"+appName+"'";
					final String deploy ="'"+serverUrl+"/manager/text/deploy?path=/"+appName+"&war=file:/tmp/"+warFile.getName()+"'";
					
					// sshClient.exec(proxied, "ls xxxx");
					List<String> listResult = sshClient.exec(proxied, "curl --user "+presentationManagerUser+":"+presentationManagerPass+" "+list);
					boolean exist = false;
					boolean undeployed = true;
					boolean deployed = false;
				
					
					if(listResult != null){
						for(final String line : listResult){
							if(line.indexOf(':') == -1)continue;
							String[] app = line.split(":");
//							final String appPath = app[0].trim();
//							final String appStatus = app[1].trim();
//							final String appSessions = app[2].trim();
							final String appWar = app[3].trim();
							
							if (appWar.equals(appName))
								exist = true;
						}
					}
					
					System.out.println("Exists : " + exist);

					if (exist) {
						undeployed = false;
						System.out.println("start undeploy");
						List<String> undeployResult = sshClient.exec(proxied, "curl --user " + presentationManagerUser + ":" + presentationManagerPass + " " + undeploy+"");
						System.out.println("Undeploy : " + undeployResult);
						if (undeployResult != null) {
							for (final String line : undeployResult) {
								System.out.println("Undeploy : " + line);
								if(line.startsWith("OK")){
									undeployed = true;
									break;
								}
							}
						}
						System.out.println("undeploy done");
					}
					
					if(undeployed){
						System.out.println("start deploy");
						sshClient.scpTo(proxied, warFile, "/tmp");
						System.out.println("scp done");
						List<String> check = sshClient.exec(proxied,"ls -l /tmp");
						System.out.println("check : "+check);
						List<String> deployResult = sshClient.exec(proxied, "curl --user " + presentationManagerUser + ":" + presentationManagerPass + " " + deploy+"");
						System.out.println("deploy : " + deployResult);
						if (deployResult != null) {
							for (final String line : deployResult) {
								System.out.println("Deploy : " + line);
								if(line.startsWith("OK")){
									deployed = true;
									break;
								}
							}
						}
					}
					
					if(!deployed)throw new MojoFailureException("Deployment failed", new Exception());

//					sshClient.exec(proxied, "curl --user "+presentationManagerUser+":"+presentationManagerPass+" http://localhost:8080/manager/text/list");
//					sshClient.exec(proxied, "curl --user "+presentationManagerUser+":"+presentationManagerPass+" "+query);

				}
			}

		} catch (JSchException e) {
			throw new MojoFailureException(e.getMessage(), e);
		} catch (IOException e) {
			throw new MojoFailureException(e.getMessage(), e);
		} catch (MavenInvocationException e) {
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (proxied != null)
			proxied.disconnect();
	}


//	private File build() throws Exception {
//		File dbConfig = null;
//		if(projectDir != null){
//			dbConfig = new File(projectDir.getAbsolutePath() + "/src/main/resources/survey/database.properties");
//		}
//		else dbConfig = new File(project.getFile().getParent() + "/src/main/resources/survey/database.properties");
//		modifyDB(dbConfig, dbServer, dbPort, dbName, dbUser, dbPass);
//		System.out.println("modified DB : "+FileClient.getInstance().readAsString(dbConfig));
//
//		// Build
//		File proj = project.getFile().getParentFile();
//		if(projectDir != null)proj = projectDir;
//
//		final File warFile = mavenClient.doCleanPackage(proj);
//
//		System.out.println("generated WAR File : "+warFile.getAbsolutePath());
//		return warFile;
//	}
	
	private File build() throws Exception {
		File proj = project.getFile().getParentFile();
		if(projectDir != null)proj = projectDir;
		
		final File copiedProj = DirectoryClient.getInstance().createDir(DirectoryClient.getInstance().getTemp(), "mavenTmpBuild");
		DirectoryClient.getInstance().cleanDirectory(copiedProj);
		DirectoryClient.getInstance().copyDirectory(proj, copiedProj);
		mavenClient.doClean(copiedProj,new File(mavenSettings));
		
		File dbConfig = new File(copiedProj.getAbsolutePath() + "/src/main/resources/survey/database.properties");
		modifyDB(dbConfig, dbServer, dbPort, dbName, dbUser, dbPass);
		
		System.out.println("modified DB : "+FileClient.getInstance().readAsString(dbConfig));

		// Build
		final File warFile = mavenClient.doCleanInstall(copiedProj,new File(mavenSettings));

		System.out.println("generated WAR File : "+warFile.getAbsolutePath());
		return warFile;
	}

	private void modifyDB(File dbPropertieFile, final String dbLocation, final String dbPort, final String dbName, final String dbUser, final String dbPass) throws Exception {
		ConfigurationUtils conf = ConfigurationUtils.getInstance();
		Properties props = conf.getConfigurationFromFileSystem(dbPropertieFile.getAbsolutePath());
		props.setProperty("jdbc.username", dbUser);
		props.setProperty("jdbc.password", dbPass);
		props.setProperty("jdbc.url", "jdbc:postgresql://" + dbLocation + ":" + dbPort + "/" + dbName);
		if (conf.saveConfiguration(props, dbPropertieFile.getAbsolutePath())) {

		} else {
			System.err.println("cannot modify " + dbPropertieFile);
		}
	}

}

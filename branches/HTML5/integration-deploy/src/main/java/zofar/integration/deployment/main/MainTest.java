package zofar.integration.deployment.main;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import com.jcraft.jsch.Session;

import eu.dzhw.zofar.management.comm.ssh.SSHClient;
import eu.dzhw.zofar.management.dev.maven.MavenClient;
import eu.dzhw.zofar.management.utils.system.ConfigurationUtils;

public class MainTest {

	public static void main(String[] args) {
		final SSHClient sshClient = SSHClient.getInstance();
		sshClient.getKownHosts();

		final String sshIdentities = "xxx/.ssh/xxxx-zofar-sprung=XXXXXX";
		final String jumpServer = "xxx";
		final String jumpSSHUser = "xxx";
		final String jumpSSHPass = "XXXXXX!";
		final String presentationServer = "xxx";
		final String presentationSSHUser = "xxx";
		final String presentationSSHPass = "XXXXXX!";
		final String presentationManagerUser = "admin";
		final String presentationManagerPass = "xxx";
		final String dbServer = "xxx";
		final String dbPort = "5432";
		final String dbName = "devhtml5";
		final String dbUser = "xxx";
		final String dbPass = "xxx";

		
		final File projectDir = new File("xxx");

		final Map<String, String> jumpIdentities = new HashMap<String, String>();
		final String[] identityPairs = sshIdentities.split("##");
		if (identityPairs != null) {
			for (final String identityPair : identityPairs) {
				final String[] identity = identityPair.split("=");
				jumpIdentities.put(identity[0], identity[1]);
			}
		}

		final ArrayList<String> jumpPasswords = new ArrayList<String>();
		final String[] jumpSSHPasswordsStrs = jumpSSHPass.split("##");
		if (jumpSSHPasswordsStrs != null) {
			for (final String jumpPasswordsStr : jumpSSHPasswordsStrs) {
				jumpPasswords.add(jumpPasswordsStr);
			}
		}

		final Map<String, String> presentationIdentities = new HashMap<String, String>();

		final ArrayList<String> presentationPasswords = new ArrayList<String>();
		final String[] presentationSSHPasswordsStrs = presentationSSHPass.split("##");
		if (presentationSSHPasswordsStrs != null) {
			for (final String presentationSSHPasswordsStr : presentationSSHPasswordsStrs) {
				presentationPasswords.add(presentationSSHPasswordsStr);
			}
		}
		Session proxied = null;

		try {

			proxied = sshClient.connectByProxy(9099, jumpServer, jumpSSHUser, jumpPasswords, jumpIdentities, presentationServer, presentationSSHUser, presentationPasswords, presentationIdentities);
			if (proxied != null) {
				final File warFile = build();
				System.out.println("war File : "+warFile.getAbsolutePath());
				

			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (proxied != null)
			proxied.disconnect();

		System.exit(1);
	}
	
	private static File build() throws Exception {
		final String dbServer = "xxx";
		final String dbPort = "5432";
		final String dbName = "xxx";
		final String dbUser = "xxx";
		final String dbPass = "xxxx";
		final File projectDir = new File("xxx/ZofarDemo-html5");
		final File mavenSettings = new File("xxx/.m2/settings.xml");
		final File dbConfig = new File(projectDir + "/src/main/resources/survey/database.properties");
		modifyDB(dbConfig, dbServer, dbPort, dbName, dbUser, dbPass);
		System.out.println("modified");

		// Build
		final File warFile = MavenClient.getInstance().doCleanPackage(projectDir,mavenSettings);
		return warFile;
	}

	private static void modifyDB(File dbPropertieFile, final String dbLocation, final String dbPort, final String dbName, final String dbUser, final String dbPass) throws Exception {
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

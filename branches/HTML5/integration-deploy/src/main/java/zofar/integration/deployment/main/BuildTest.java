package zofar.integration.deployment.main;

import java.io.File;
import java.util.Properties;

import eu.dzhw.zofar.management.dev.maven.MavenClient;
import eu.dzhw.zofar.management.utils.files.FileClient;
import eu.dzhw.zofar.management.utils.system.ConfigurationUtils;

public class BuildTest {

	public static void main(String[] args) throws Exception {
		build();
	}
	
	private static File build() throws Exception {
		final String dbServer = "xxx";
		final String dbPort = "5432";
		final String dbName = "xxx";
		final String dbUser = "xxx";
		final String dbPass = "xxx";
		final File projectDir = new File("xxx/HTML5/Integration/HTML5 Demo");
		final File mavenSettings = new File("/xxx/.m2/settings.xml");

		final File dbConfig = new File(projectDir + "/src/main/resources/survey/database.properties");
		modifyDB(dbConfig, dbServer, dbPort, dbName, dbUser, dbPass);
		System.out.println("modified DB : "+FileClient.getInstance().readAsString(dbConfig));

		// Build
//		File warFile = null;
//		if(projectDir != null)warFile = mavenClient.doCleanPackage(projectDir);
//		else warFile = mavenClient.doCleanPackage(project.getFile().getParentFile());
		File proj = null;
		if(projectDir != null)proj = projectDir;
		final File warFile = MavenClient.getInstance().doCleanInstall(proj,mavenSettings);

		System.out.println("generated WAR File : "+warFile.getAbsolutePath());
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

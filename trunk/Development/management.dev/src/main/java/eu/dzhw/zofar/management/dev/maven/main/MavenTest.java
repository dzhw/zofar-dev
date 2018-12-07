package eu.dzhw.zofar.management.dev.maven.main;

import java.io.File;

import org.apache.maven.shared.invoker.MavenInvocationException;

import eu.dzhw.zofar.management.dev.maven.MavenClient;
import eu.dzhw.zofar.management.utils.files.DirectoryClient;

public class MavenTest {

	public static void main(String[] args) {
//		File tmp = FileUtils.getTempDirectory();
//		File projectDir = new File(tmp,"Project");
		DirectoryClient dirClient = DirectoryClient.getInstance();
		final File baseDir = dirClient.createDir(DirectoryClient.getInstance().getHome(), "TestProject");
		MavenClient mavenClient = MavenClient.getInstance();
//		try {
//			mavenClient.doCleanInstall(projectDir);
//		} catch (MavenInvocationException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		final File mavenHome = new File("/xxxx/.m2/settings.xml");
		File projectDir = mavenClient.createProjectFromArchetype("eu.dzhw.zofar", "befragung1", "de.his.zofar","zofar.survey.archetype","0.0.1-SNAPSHOT",baseDir,mavenHome);
		try {
			mavenClient.doCleanInstall(projectDir,mavenHome);
		} catch (MavenInvocationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	}

}

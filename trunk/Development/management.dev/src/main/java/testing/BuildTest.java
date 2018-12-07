package testing;

import java.io.File;

import eu.dzhw.zofar.management.dev.builder.BuilderClient;
import eu.dzhw.zofar.management.utils.files.DirectoryClient;

public class BuildTest {

	public static void main(String[] args) {

		DirectoryClient dirClient = DirectoryClient.getInstance();
		
		final File baseDir = dirClient.createDir(DirectoryClient.getInstance().getHome(), "TestProject");
	

		
		BuilderClient builder = BuilderClient.getInstance();
		final String name = "xxxx";
		
		final String dbLocation = "xxxx";
		final String dbUser = "xxxx";
		final String dbPass = "xxxx";
		
		boolean overrideNav = true;
		boolean cutHistory = true;
		String saveMode = "forward,backward,same";
		String login = "TOKEN";
		boolean preloadOnStart = false;
		boolean record = false;
		
		final String svnUrl = "xxxx";
		final String svnUser = "xxxx";
		final String svnPass = "xxxx";
		
		try {
//			builder.buildNewProject(name, dbLocation, dbUser, dbPass, overrideNav,cutHistory,false,false,false,saveMode,login,preloadOnStart,record,baseDir, svnUrl, svnUser, svnPass);
//			builder.buildNewProject(name, dbLocation, dbUser, dbPass, false, true, saveMode, login, false, false, baseDir, svnUrl, svnUser, svnPass,true);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}

package zofar.integration.deployment.main;

import java.io.File;

import org.tmatesoft.svn.core.SVNException;

import eu.dzhw.zofar.management.comm.svn.SVNClient;
import eu.dzhw.zofar.management.dev.maven.MavenClient;
import eu.dzhw.zofar.management.utils.files.DirectoryClient;
import zofar.integration.deployment.mojo.PresentationServerDeployment;

public class MojoWrapper {
	
	public static void main(String[] args) throws Exception {
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
		final String dbName = "xxx";
		final String dbUser = "xxxx";
		final String dbPass = "xxxx";
		
		final File svnBaseDir = DirectoryClient.getInstance().getTemp();
		
		final String svnProjectName = "xxxx-html5";
		final String svnUrl = "xxxx";
		final String svnUser = "xxxx";
		final String svnPass = "xxxx";
		
		File projectDir = null;
		SVNClient svn = SVNClient.getInstance();
		try {
			projectDir = new File(svnBaseDir, svnProjectName);
			svn.doCheckout(svnUrl, svnUser, svnPass, svnProjectName, projectDir);
		} catch (SVNException e) {
			throw new Exception(e);
		}
		final MavenClient mavenClient = MavenClient.getInstance();
		final PresentationServerDeployment mojo = new PresentationServerDeployment(sshIdentities, jumpServer, jumpSSHUser, jumpSSHPass, presentationServer, presentationSSHUser, presentationSSHPass, presentationManagerUser, presentationManagerPass, dbServer, dbPort, dbName, dbUser, dbPass, projectDir, mavenClient);
		mojo.execute();
		
		System.exit(1);
	}

}

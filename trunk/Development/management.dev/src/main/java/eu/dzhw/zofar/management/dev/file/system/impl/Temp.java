package eu.dzhw.zofar.management.dev.file.system.impl;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.dzhw.zofar.management.dev.file.system.FileHandler;
import eu.dzhw.zofar.management.utils.files.DirectoryClient;
import eu.dzhw.zofar.management.utils.files.FileClient;
@Deprecated
public class Temp implements FileHandler{
	
	final static Logger LOGGER = LoggerFactory.getLogger(Temp.class);

	public Temp() {
		super();
	}

//	public File getHome() {
//		final File tmpDir = new File(System.getProperty("java.io.tmpdir"));
//		return tmpDir;
//	}
//
//	public File getDir(final String name) {
//		File tmpDir = null;
//		if(!exist(name))tmpDir = createDir(name);
//		else tmpDir = new File(getHome(),name);
//		return tmpDir;
//	}
//
//	public boolean exist(final String name) {
//		final File tmp = new File(getHome(),name);
//		return tmp.exists();
//	}
//
//	public File copy(File source, File destDir) {
//		return FileClient.getInstance().copy(source, destDir);
//	}
//
//	public File move(File source, File destDir) {
//		return FileClient.getInstance().move(source, destDir);
//	}
//
//	public File createDir(String name) {
//		return DirectoryClient.getInstance().createDir(this.getHome(), name);
//	}
//
//	public boolean deleteDir(String name) {
//		return DirectoryClient.getInstance().deleteDir(this.getHome(), name);
//	}
}

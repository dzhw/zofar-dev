package eu.dzhw.zofar.management.utils.files.main;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;

import eu.dzhw.zofar.management.utils.files.DirectoryClient;
import eu.dzhw.zofar.management.utils.files.FileClient;
import eu.dzhw.zofar.management.utils.files.PackagerClient;
import eu.dzhw.zofar.management.utils.string.ReplaceClient;
@Deprecated
public class LogSplitter {
	
	private static LogSplitter INSTANCE;
	
	public static void main(String[] args) {
//		final String dirPath = "/media/hisuser/2cb73b46-a1a4-432a-9565-7f3adb6a0c00/tmp/Serverbackup/LoadBalancer/var/log/apache2";
		final String dirPath = "/media/hisuser/2cb73b46-a1a4-432a-9565-7f3adb6a0c00/tmp/Serverbackup/App3/var/log/tomcat7";
//		final String logName = "vmzofar-lb.his.de_access";
		final String logName = "localhost_access_log";
		
		final List<String> acceptedPatterns = new ArrayList<String>();
		acceptedPatterns.add("xxxx");

		
		LogSplitter.getInstance().splitAccessLog(dirPath, logName,acceptedPatterns);
	}

	private LogSplitter() {
		super();
	}
	
	public static LogSplitter getInstance() {
		if (INSTANCE == null)
			INSTANCE = new LogSplitter();
		return INSTANCE;
	}

	public void splitAccessLog(final String dirPath,final String logName,final List<String> skipPatterns){
		final DirectoryClient dir = DirectoryClient.getInstance();
		final PackagerClient packager = PackagerClient.getInstance();
		final FileClient fileClient = FileClient.getInstance();
		final ReplaceClient replaceClient = ReplaceClient.getInstance();
		final File rootDir = new File(dirPath);
		final List<File> files = dir.readDir(rootDir, null);
		final List<String> unkown = new ArrayList<String>();
		final File splitDir = new File(rootDir.getAbsolutePath()+File.separator+"splitted");
		if(!splitDir.exists())splitDir.mkdirs();
		try {
			FileUtils.cleanDirectory(splitDir);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		if(files != null){
			for(final File file : files){
				if(file.isFile()){
					Date stamp = new Date( file.lastModified() );
					if(file.getName().startsWith(logName)){
						System.out.println(""+file.getName()+" "+stamp);
						if(file.getName().endsWith(".gz")){
							final File decomped = packager.extractGZIP(file,true);
							analyseAccessLog(replaceClient,fileClient,decomped,rootDir,splitDir,file.getName(),logName,skipPatterns,unkown);
							decomped.delete();
						}
						else if(file.getName().endsWith(".1")){
							analyseAccessLog(replaceClient,fileClient,file,rootDir,splitDir,file.getName(),logName,skipPatterns,unkown);
						}
						else if(file.getName().endsWith(".log")){
							analyseAccessLog(replaceClient,fileClient,file,rootDir,splitDir,file.getName(),logName,skipPatterns,unkown);
						}
						else if(file.getName().endsWith(".txt")){
							analyseAccessLog(replaceClient,fileClient,file,rootDir,splitDir,file.getName(),logName,skipPatterns,unkown);
						}
					}
				}
			}
		}
	}

	private void analyseAccessLog(final ReplaceClient replaceClient,FileClient fileClient,File decomped,final File rootDir,final File splitDir,final String analyzedFileName,final String logName,final List<String> acceptPatterns,final List<String> unkown) {
		final LineIterator it = fileClient.iterateContent(decomped);
		while(it.hasNext()){
			final String line = it.next();
			List<String> founds = replaceClient.findInString("(GET|POST) /([^ /]*)", line);
			if(founds.isEmpty()) continue;
			
			if(founds.size() == 1){
				String pattern = founds.get(0);
				pattern = pattern.replaceAll("(GET|POST) /", "");
				if(replaceClient.contains(acceptPatterns,pattern)){
					
					final File file = new File(splitDir,pattern+"_"+logName+".log");
					if(!splitDir.exists())splitDir.mkdirs();
					try {
						fileClient.writeToFile(file.getAbsolutePath(), "["+analyzedFileName+"] "+line+"\n", true);
					} catch (IOException e) {
						System.err.println("("+splitDir.exists()+") Failed to write to "+file.getAbsolutePath()+ " Cause: "+e.getMessage());
					}
				}
				else{
					if(!unkown.contains(pattern)){
						System.out.println("not accepted pattern found : "+pattern);
						unkown.add(pattern);
					}
					
				}
			}
			else System.out.println("ambiguous pattern found : "+founds);
		}
	}
}

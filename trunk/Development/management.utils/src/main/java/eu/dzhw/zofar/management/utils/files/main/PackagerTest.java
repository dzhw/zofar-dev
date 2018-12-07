package eu.dzhw.zofar.management.utils.files.main;

import java.io.File;
import java.util.List;

import eu.dzhw.zofar.management.utils.files.FileClient;
import eu.dzhw.zofar.management.utils.files.PackagerClient;

public class PackagerTest {

	public static void main(String[] args) {
		final PackagerClient packager = PackagerClient.getInstance();

		final File screenshot_directory = new File("xxx");
		final File screenshotFile = new File(screenshot_directory,"xxx.zip");
		
		List<File> files = PackagerClient.getInstance().extractZip(screenshotFile);
		if(files != null){
			for(final File file : files){
				System.out.println("file : "+file.getAbsolutePath());
			}
		}
	}

}

package eu.dzhw.zofar.management.generator.screenshots.main;

import java.awt.Dimension;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import eu.dzhw.zofar.management.dev.automation.screenshot.ScreenshotGenerator;
import eu.dzhw.zofar.management.utils.files.DirectoryClient;
import eu.dzhw.zofar.management.utils.files.FileClient;
import eu.dzhw.zofar.management.utils.files.PackagerClient;

public class Screenshotter {

	public static void main(String[] args) {

		final String surveyname = "xxxx";

		final String token = "xxxx";
		
		final ArrayList<java.awt.Dimension> dimensions = new ArrayList<java.awt.Dimension>();
		dimensions.add(new Dimension(1200,1024));
		
		dimensions.add(new Dimension(768,1024));
		//IPhone
		dimensions.add(new Dimension(480,320));
		
		final List<String> languages = new ArrayList<String>();
		languages.add("de");
		languages.add("en");
		
		ScreenshotGenerator generator = ScreenshotGenerator.getInstance();
		File screenshotParentDir = DirectoryClient.getInstance().createDir(DirectoryClient.getInstance().getHome(),"screenshots");
		File screenshotDir = DirectoryClient.getInstance().createDir(screenshotParentDir,surveyname);
		DirectoryClient.getInstance().cleanDirectory(screenshotDir);
		try {
			generator.spiderSurveyForMDM("http://xxxx:8080/"+surveyname+"/special/login.html?zofar_token="+token, screenshotDir,dimensions,false,languages);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		final File compressed = FileClient.getInstance().createOrGetFile(surveyname+"_screenshots", ".zip", screenshotParentDir);
		try {
			Map<String, Object> packageObj = new HashMap<String, Object>();
			Map<String, Object> screenshots = new HashMap<String, Object>();
			final List<File> screenshotList = DirectoryClient.getInstance().readDir(screenshotDir);
			
			for(final File file : screenshotList)screenshots.put(file.getName(), file);
			packageObj.put("screenshots", screenshots);
			PackagerClient.getInstance().packageZip(compressed, packageObj);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}

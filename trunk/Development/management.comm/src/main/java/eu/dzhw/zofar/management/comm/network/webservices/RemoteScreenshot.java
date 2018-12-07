package eu.dzhw.zofar.management.comm.network.webservices;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.dzhw.zofar.management.utils.files.DirectoryClient;
import eu.dzhw.zofar.management.utils.files.FileClient;

public class RemoteScreenshot {
	
	/** The Constant INSTANCE. */
	private static final RemoteScreenshot INSTANCE = new RemoteScreenshot();
	
	/** The Constant LOGGER. */
	private static final Logger LOGGER = LoggerFactory.getLogger(RemoteScreenshot.class);

	private static final String USER_NAME = "xxxx";
	private static final String USER_KEY = "xxxx";
	private static final String SAUCELABS_URL = "https://" + USER_NAME + ":" + USER_KEY + "@ondemand.saucelabs.com:443/wd/hub";

	
	/**
	 * Instantiates a new HTTP client.
	 */
	private RemoteScreenshot() {
		super();
	}

	public static RemoteScreenshot getInstance() {
		return INSTANCE;
	}
	
	//https://wiki.saucelabs.com/display/DOCS/Platform+Configurator#/
	public File getScreenshot(final String url,final DesiredCapabilities caps) throws MalformedURLException {
		final FileClient fileClient = FileClient.getInstance();
			    
	    LOGGER.info("Use Caps : {}",caps.toString());
	 
	    WebDriver driver = new RemoteWebDriver(new URL(SAUCELABS_URL), caps);
	 
	    driver.get(url);
	    System.out.println("title of page is: " + driver.getTitle());
	    
	    final String name = url.replace(':', '_').replace('/', '_');
	 
		final File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
		final File targetFile = fileClient.createOrGetFile(name, ".png", DirectoryClient.getInstance().getTemp());
		fileClient.copyFile(scrFile, targetFile);
		driver.quit();

		return targetFile;
	}
	
	

}

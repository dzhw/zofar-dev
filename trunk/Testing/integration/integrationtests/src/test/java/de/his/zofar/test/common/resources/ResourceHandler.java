package de.his.zofar.test.common.resources;

import java.io.OutputStream;
import java.io.PrintStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.his.zofar.test.common.AbstractIntegrationTest;
import de.his.zofar.test.common.exceptions.ElementNotFoundException;
import de.his.zofar.test.common.exceptions.ZofarTestException;

public class ResourceHandler implements Serializable {

	private static final long serialVersionUID = 1956915645342522292L;
	private static final ResourceHandler INSTANCE = new ResourceHandler();

	public static final String BASE_URL = "http://vm2m-test.his.de:8080/integrationsurvey-0.0.1-SNAPSHOT/";
//	public static final String BASE_URL = "http://localhost:8080/integrationsurvey/";
	private HtmlUnitDriver driver;
	private final StringBuffer verificationErrors = new StringBuffer();
	private static final Logger LOGGER = LoggerFactory
			.getLogger(AbstractIntegrationTest.class);

	private ResourceHandler() {
		super();

		this.driver = new HtmlUnitDriver();
		this.driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		this.driver.setJavascriptEnabled(true);
		System.setErr(new PrintStream(new OutputStream() {
		    public void write(int b) {
		    }
		}));
	}

	public static ResourceHandler getInstance() {
		return INSTANCE;
	}

	public StringBuffer getVerificationErrors() {
		return verificationErrors;
	}

	public void login(final String token) {
		LOGGER.info("login {}", token);
		this.gotoPage("index.html");
		try {
			final WebElement usernameField = this.getTextInput("j_username");
			final WebElement passwordField = this
					.getPasswordInput("j_password");

			usernameField.clear();
			usernameField.sendKeys(token);

			passwordField.clear();
			passwordField.sendKeys(token);

			this.getSubmitInput("submit").click();
			this.gotoPage("index.html");
		} catch (ZofarTestException e) {
			e.printStackTrace();
		}
	}

	public void navigateForward() throws ZofarTestException {
//		LOGGER.info("navigateForward from {}", this.driver.getCurrentUrl());
		WebElement button = this.getSubmitInput("forwardBt");
		if (button != null)
			button.click();
	}

	public void navigateBackward() throws ZofarTestException {
//		LOGGER.info("navigateBackward from {}", this.driver.getCurrentUrl());
		WebElement button = this.getSubmitInput("backwardBt");
		if (button != null)
			button.click();
	}
	
	public void gotoStartPage(final String token) {
		this.driver.get(this.BASE_URL + "index.html");
		if(!(currentPage()).equals(BASE_URL+"index.html"))login(token);

	}
	
	public void gotoEndPage() {
		this.driver.get(this.BASE_URL + "end.html");
	}

	public void setTextContent(final String id, final String content)
			throws ZofarTestException {
		WebElement field = this.getTextInput(id);
		if (field != null) {
			field.clear();
			field.sendKeys(content);
		}
	}
	
	public void setRadioContent(final String id, final String content)
			throws ZofarTestException {
		WebElement field = this.getRadioInput(id);
		if (field != null) {
			field.clear();
			field.sendKeys(content);
		}
	}
	
	public void setCheckContent(final String id, final boolean check)
			throws ZofarTestException {
		WebElement field = this.getCheckboxInput(id);
		if (field != null) {
//			LOGGER.info("field id {}",field.getAttribute("id"));
			if(check&&(!field.isSelected())){
				field.click();
			}
			if((!check)&&(field.isSelected())){
				field.click();
			}
		}
	}

	public String currentPage() {
		return this.driver.getCurrentUrl();
	}

	private void gotoPage(final String page) {
		this.driver.get(this.BASE_URL + page);
		LOGGER.info("goto Page {} new Location {}", page,this.currentPage());
	}

	private WebElement getTextInput(final String id) throws ZofarTestException {
		return getInput(id, "//input[@type='text']");
	}
	
	private WebElement getRadioInput(final String id) throws ZofarTestException {
		return getInput(id, "//input[@type='radio']");
	}
	
	private WebElement getCheckboxInput(final String id) throws ZofarTestException {
		return getInput(id, "//input[@type='checkbox']");
	}

	private WebElement getPasswordInput(final String id)
			throws ZofarTestException {
		return getInput(id, "//input[@type='password']");
	}

	private WebElement getSubmitInput(final String id)
			throws ZofarTestException {
		return getInput(id, "//input[@type='submit']");
	}

	private WebElement getInput(final String id, final String pattern)
			throws ZofarTestException {
		final List<WebElement> back = this.driver.findElements(By
				.xpath(pattern));
		if (back != null) {
			final Iterator<WebElement> it = back.iterator();
			while (it.hasNext()) {
				final WebElement tmp = it.next();
//				LOGGER.info("found {} ({})", tmp, tmp.isDisplayed());
				if ((tmp.getAttribute("name").endsWith(id))
						&& (tmp.isDisplayed()))
					return tmp;
			}
		}
		throw new ElementNotFoundException("Element with id " + id
				+ " does not exist");
	}

	public Map<String,String> getResults() throws ZofarTestException {
		final List<WebElement> elements = this.driver.findElements(By
				.xpath("//b[starts-with(@id,'tag')]"));

		if (elements != null) {
			final Map<String,String> back = new HashMap<String,String>();
			final Iterator<WebElement> it = elements.iterator();
			while (it.hasNext()) {
				final WebElement tmp = it.next();
//    			 LOGGER.info("found {} = {}",tmp.getAttribute("id"),tmp.getText());
				back.put(tmp.getAttribute("id"),tmp.getText());
			}
			return back;
		}
		throw new ElementNotFoundException("no results found");
	}

}

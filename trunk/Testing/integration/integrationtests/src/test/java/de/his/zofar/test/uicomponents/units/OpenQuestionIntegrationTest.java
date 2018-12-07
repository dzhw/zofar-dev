package de.his.zofar.test.uicomponents.units;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.his.zofar.test.common.AbstractIntegrationTest;
import de.his.zofar.test.common.exceptions.WrongLocationException;
import de.his.zofar.test.common.exceptions.ZofarTestException;

public class OpenQuestionIntegrationTest extends AbstractIntegrationTest {
	
	private static final Logger LOGGER = LoggerFactory
			.getLogger(OpenQuestionIntegrationTest.class);

	
	@Before
	public void setUp() throws Exception {
		super.setUp();
		handler.gotoStartPage("part1");
	}

	@After
	public void tearDown() throws Exception {
		super.tearDown();
	}

	@Test
	public void test() throws ZofarTestException {
		if(!(handler.currentPage()).equals(handler.BASE_URL+"index.html"))throw new WrongLocationException("index.html expected, but found "+handler.currentPage());
		handler.navigateForward();
		//page1
		handler.setTextContent("j_idt39", "<i>keks</i>");
		handler.navigateForward();
		//page2 
		LOGGER.info("results {}",handler.getResults());
		Assert.assertEquals("#i#keks#/i#",handler.getResults().get("tag1"));
	}

}

package de.his.zofar.test.uicomponents.units;

import java.util.UUID;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.his.zofar.test.common.AbstractIntegrationTest;
import de.his.zofar.test.common.exceptions.WrongLocationException;
import de.his.zofar.test.common.exceptions.ZofarTestException;

public class SingleChoiceQuestionIntegrationTest extends
		AbstractIntegrationTest {
	
	private static final Logger LOGGER = LoggerFactory
			.getLogger(SingleChoiceQuestionIntegrationTest.class);

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
		handler.navigateForward();
		handler.navigateForward();
		if(!(handler.currentPage()).equals(handler.BASE_URL+"page3.html"))throw new WrongLocationException("page3.html expected, but found "+handler.currentPage());

		//page3
		handler.setRadioContent("j_idt28", "page3:body:question2:rd:ao2");
		final String uuid = UUID.randomUUID()+"";
		handler.setTextContent("aoq", uuid);
		handler.navigateForward();
		//page4 
		LOGGER.info("results {}",handler.getResults());
		Assert.assertEquals("nein",handler.getResults().get("tag1"));
		Assert.assertEquals(uuid,handler.getResults().get("tag2"));
	}

}

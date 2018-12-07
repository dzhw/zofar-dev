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

public class MultipleChoiceIntegrationTest extends AbstractIntegrationTest {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(MultipleChoiceIntegrationTest.class);

	
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
	public void test() throws ZofarTestException{
		if(!(handler.currentPage()).equals(handler.BASE_URL+"index.html"))throw new WrongLocationException("index.html expected, but found "+handler.currentPage());
		handler.navigateForward();
		handler.navigateForward();
		handler.navigateForward();		
		handler.navigateForward();		
		handler.navigateForward();		
		if(!(handler.currentPage()).equals(handler.BASE_URL+"page5.html"))throw new WrongLocationException("page5.html expected, but found "+handler.currentPage());
		handler.setCheckContent("v4", false);
		handler.setCheckContent("v5", true);
		handler.setCheckContent("v6", false);
		handler.setCheckContent("v7", true);
		handler.setCheckContent("v8", false);
		handler.setCheckContent("v9", true);
		handler.setCheckContent("v10", false);
		handler.setCheckContent("v11", true);
		handler.setCheckContent("v12", false);
		handler.setCheckContent("v13", true);
		handler.setCheckContent("v14", false);
		handler.setCheckContent("v15", true);
		handler.setCheckContent("v16", false);
		
		final String uuid = UUID.randomUUID()+"";
		handler.setTextContent("aoq", uuid);
		handler.navigateForward();
		//page4 
		LOGGER.info("results {}",handler.getResults());

//		Assert.assertEquals("Chip",handler.getResults().get("tag1"));
		Assert.assertEquals("",handler.getResults().get("tag1"));
		Assert.assertEquals("c't",handler.getResults().get("tag2"));
//		Assert.assertEquals("Mac-Welt",handler.getResults().get("tag3"));
		Assert.assertEquals("",handler.getResults().get("tag3"));
		Assert.assertEquals("Stern",handler.getResults().get("tag4"));
//		Assert.assertEquals("Spiegel",handler.getResults().get("tag5"));
		Assert.assertEquals("",handler.getResults().get("tag5"));
		Assert.assertEquals("Focus",handler.getResults().get("tag6"));
//		Assert.assertEquals("HörZu",handler.getResults().get("tag7"));
		Assert.assertEquals("",handler.getResults().get("tag7"));
		Assert.assertEquals("TV-Spielfilm",handler.getResults().get("tag8"));
//		Assert.assertEquals("Die Aktuelle",handler.getResults().get("tag9"));
		Assert.assertEquals("",handler.getResults().get("tag9"));
		Assert.assertEquals("Brigitte",handler.getResults().get("tag10"));
//		Assert.assertEquals("Geo",handler.getResults().get("tag11"));
		Assert.assertEquals("",handler.getResults().get("tag11"));
		Assert.assertEquals("Essen und Trinken",handler.getResults().get("tag12"));
		Assert.assertEquals("Nein",handler.getResults().get("tag13"));
		Assert.assertEquals(uuid,handler.getResults().get("tag14"));
	}

}

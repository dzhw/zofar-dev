package de.his.zofar.test.common;

import junit.framework.TestCase;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;

import de.his.zofar.test.common.resources.ResourceHandler;

@Ignore
public class AbstractIntegrationTest extends TestCase{
	
	protected ResourceHandler handler;

	@Before
	public void setUp() throws Exception {
		this.handler = ResourceHandler.getInstance();
	}

	@After
	public void tearDown() throws Exception {
	}
}

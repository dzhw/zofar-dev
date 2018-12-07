package integration.tests.common;

import org.junit.Ignore;
import org.junit.Test;

import integration.tests.AbstracTestBase;
import integration.utils.MessageProvider;
import junit.framework.TestCase;

public class DummyTest extends AbstracTestBase {
	
	@Ignore
	@Test
	public void test() {
		MessageProvider.info(this,"test dummy ... ");
		TestCase.assertTrue("dummies found "+"",true);
	}

}

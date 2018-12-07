package main;

import junit.framework.TestSuite;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tests.integrity.IdIntegrity;
import tests.integrity.MixedMatrixIntegrity;
import tests.integrity.OpenFieldIntegrity;
import tests.integrity.PageIntegrity;
import tests.integrity.QuestionIntegrity;
import tests.integrity.VariableIntegrity;
import tests.schema.SchemaValidation;

@RunWith(Suite.class)
@SuiteClasses({SchemaValidation.class,VariableIntegrity.class,IdIntegrity.class,QuestionIntegrity.class,PageIntegrity.class,OpenFieldIntegrity.class,MixedMatrixIntegrity.class})
public class AllTests extends TestSuite{
	final static Logger LOGGER = LoggerFactory.getLogger(AllTests.class);

	@BeforeClass 
    public static void setUpClass() {     
		if(System.getProperty("suite") == null)System.setProperty("suite", "true");
		if(System.getProperty("runMode") == null)System.setProperty("runMode", "junit");
    }

    @AfterClass 
    public static void tearDownClass() { 
    	System.clearProperty("suite");
		System.clearProperty("runMode"); 
    }
}

package tests.integrity;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

import junit.framework.TestCase;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import tests.common.AbstractVerificationTest;
import tests.common.MessageProvider;


public class IdIntegrity extends AbstractVerificationTest {

	public IdIntegrity() {
		super();
	}

	@Override
	@Before
	public void setUp() throws Exception {
		super.setUp();
	}

	@Override
	@After
	public void tearDown() throws Exception {
		super.tearDown();
	}
	
	@Test
	public void testDoubleIds() {
		MessageProvider.info(this,"test double ids ...");

	    final Set<String> usedIds = new HashSet<String>();
	    final Set<String> doubleUsedIds = new HashSet<String>();
	    final NodeList usingNodes = this.getByXPath("//parent::*[@uid]");
	    final int count = usingNodes.getLength();
	    for (int i = 0; i < count; ++i) {
	    	Node e = (Node) usingNodes.item(i);
	    	if(this.hasParent(e, "zofar:researchdata"))continue;
	    	final String path = this.createPath(e);
	    	if(usedIds.contains(path)){
	    		doubleUsedIds.add(path);
	    	}
	    	usedIds.add(path);
	    }
	    TestCase.assertTrue("double used uids found "+doubleUsedIds,doubleUsedIds.isEmpty());
	}
	
	@Test
	public void testDoubleAnswerOptionsIds() {
		MessageProvider.info(this,"test double AnswerOption ids ...");
	    final NodeList rdcNodes = this.getByXPath("//parent::responseDomain");

	    final Set<String> usedIds = new HashSet<String>();
	    final Set<String> doubleUsedIds = new LinkedHashSet<String>();
	    final int count = rdcNodes.getLength();
	    for (int i = 0; i < count; ++i) {
	    	Node rdc = (Node) rdcNodes.item(i);
	    	if(this.hasParent(rdc, "zofar:researchdata"))continue;
	    	final String rdcUID = this.createPath(rdc);
	    	testDoubleAnswerOptionsIdsHelper(rdcUID,rdc.getChildNodes(),usedIds,doubleUsedIds);
	    }
	    
	    TestCase.assertTrue("double used answeroption uids found "+doubleUsedIds,doubleUsedIds.isEmpty());
	}
	
	private void testDoubleAnswerOptionsIdsHelper(final String rdcUID ,final NodeList nodes,final Set<String> usedIds,final Set<String> doubleUsedIds){
		final int count = nodes.getLength();
		for (int i = 0; i < count; ++i) {
	    	Node e = (Node) nodes.item(i);
	    	if(this.hasParent(e, "zofar:researchdata"))continue;
    		final String type = e.getNodeName();
    		
    		if(type.equals("zofar:unit")){
    			testDoubleAnswerOptionsIdsHelper(rdcUID,e.getChildNodes(),usedIds,doubleUsedIds);
    		}
    		else if(type.equals("zofar:answerOption")){
	    		final NamedNodeMap attributes = e.getAttributes();
	    		final Node uid = attributes.getNamedItem("uid");

	    		final String id = rdcUID+":"+uid.getTextContent();
	    		if(usedIds.contains(id))doubleUsedIds.add(id);
	    		else usedIds.add(id);
    		}
	    }
	}
}

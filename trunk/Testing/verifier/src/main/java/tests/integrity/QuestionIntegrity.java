package tests.integrity;

import java.util.HashSet;
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

public class QuestionIntegrity extends AbstractVerificationTest {

	public QuestionIntegrity() {
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
	public void testNoHeader() {
		MessageProvider.info(this,"test questions for no header ...");

	    final Set<String> noHeader = new HashSet<String>();
	    final NodeList usingNodes = this.getByXPath("//parent::responseDomain");
	    final int count = usingNodes.getLength();
	    for (int i = 0; i < count; ++i) {
	    	Node e = (Node) usingNodes.item(i);
	    	if(e == null)continue;
	    	if(this.hasParent(e, "zofar:researchdata"))continue;
	    	Node parent = e.getParentNode();
	    	if(parent != null){
		    	final String nodeType = parent.getNodeName();
		    	if(nodeType.equals("zofar:item"))continue;
		    	//check for header
		    	NodeList headerList = this.getByXPath(parent,"header");
		    	if((headerList == null)||(headerList.getLength() == 0)){
		    		final String path = this.createPath(parent);
		    		noHeader.add(path);
		    	}
	    	}
	    }
	    TestCase.assertTrue("Questions without required header found "+noHeader,noHeader.isEmpty());
	}
	
	@Test
	public void testMCExclusive() {
		MessageProvider.info(this,"test MultipleChoice-Question for more than one exclusive AnswerItem ...");

	    final NodeList mcRDCs = this.getByXPath("//parent::responseDomain//*[@exclusive]");

	    final Set<String> exclusives = new HashSet<String>();
	    final Set<String> multipleExclusives = new HashSet<String>();
	    final int count = mcRDCs.getLength();
	    for (int i = 0; i < count; ++i) {
	    	Node e = (Node) mcRDCs.item(i);
	    	if(e == null)continue;
	    	if(this.hasParent(e, "zofar:researchdata"))continue;
	    	String type = e.getNodeName();
	    	while(!type.equals("zofar:responseDomain")){
	    		e = e.getParentNode();
	    		type = e.getNodeName();
	    	}
	    	if(type.equals("zofar:responseDomain")){
	    		final NodeList mcItems = this.getByXPath(e,"//*[@exclusive]");
	    		if(mcItems.getLength() > 1){
	    			final String path = createPath(e);
	    			if(!exclusives.contains(path))exclusives.add(path);
	    			else multipleExclusives.add(path);
	    		}
	    	}
	    }
	    TestCase.assertTrue("MultipleChoice-Questions with more than one exclusive AnswerItem found "+multipleExclusives,multipleExclusives.isEmpty());

	}
	
	@Test
	public void testNoAnswerOptions() {
		MessageProvider.info(this,"test questions for no Options ...");

	    final Set<String> noOptions = new HashSet<String>();
	    final NodeList usingNodes = this.getByXPath("//parent::responseDomain");
	    final int count = usingNodes.getLength();
	    for (int i = 0; i < count; ++i) {
	    	Node e = (Node) usingNodes.item(i);
	    	if(e == null)continue;
	    	if(this.hasParent(e, "zofar:researchdata"))continue;
	    	final String path = createPath(e);
	    	final String type = e.getNodeName();
	    	
	    	boolean empty = true;
	    	
	    	if(type.equals("zofar:responseDomain")){
		    	NodeList optionsList = this.getByXPath(e,"descendant::answerOption | descendant::question[@variable] | descendant::questionOpen[@variable]");
		    	if((optionsList == null)||(optionsList.getLength() == 0)){
		    	}
		    	else{
		    		empty = false;
		    	}
	    	}
	    	
	    	if(empty){
	    		noOptions.add(path);
	    	}
	    }
	    TestCase.assertTrue("Questions without AnswerOptions/Open Questions found "+noOptions,noOptions.isEmpty());
	}
}

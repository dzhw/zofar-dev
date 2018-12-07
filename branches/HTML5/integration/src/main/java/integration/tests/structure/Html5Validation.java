package integration.tests.structure;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.xmlbeans.XmlObject;
import org.junit.Ignore;
import org.junit.Test;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.gargoylesoftware.htmlunit.html.HtmlPage;

import eu.dzhw.zofar.management.comm.network.http.HTTPClient;
import eu.dzhw.zofar.management.comm.network.webservices.W3CMarkupValidatorService;
import eu.dzhw.zofar.management.comm.network.webservices.W3CMarkupValidatorService.TYPE;
import eu.dzhw.zofar.management.comm.network.webservices.W3CMarkupValidatorService.W3CMessage;
import eu.dzhw.zofar.management.utils.objects.CollectionClient;
import eu.dzhw.zofar.management.utils.xml.XmlClient;
import integration.tests.AbstracTestBase;
import integration.utils.MessageProvider;
import junit.framework.TestCase;

public class Html5Validation extends AbstracTestBase {
	
	final List<String> pages = new ArrayList<String>();

	@Override
	public void setUp() throws Exception {
		// TODO Auto-generated method stub
		super.setUp();
		showProperties();
		
		final XmlObject qml = this.getQML();
//		System.out.println("QML : "+qml);
		if(qml != null){
			final NodeList nodes = XmlClient.getInstance().getByXPath(qml.getDomNode(), "//*");

			if (nodes != null) {
				try {
					int count = nodes.getLength();
					for (int a = 0; a < count; a++) {
						Node node = nodes.item(a);
						final String nodeName = node.getNodeName();
						
						if (nodeName.equals("zofar:page")) {
							final String uidValue = ((Element) node).getAttribute("uid");
							System.out.println("add to Page List : "+uidValue);
							pages.add(uidValue);
						}
					}
				}catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	@Ignore
	@Test
	public void test() throws Exception {
		MessageProvider.info(this,"validate Page structure ... ");
		
		W3CMarkupValidatorService validator = W3CMarkupValidatorService.getInstance();
		
		HTTPClient spider = HTTPClient.getInstance();
		spider.setJavaScriptEnabled(true);
		
		final Map<String,List<W3CMessage>> errors = new LinkedHashMap<String,List<W3CMessage>>();
		final Map<String,String> content = new LinkedHashMap<String,String>();
		
		for(final String pagename : pages){
			final HtmlPage page = this.gotoPage(pagename+".html");
			if (page == null)
				TestCase.assertTrue("Page not found " + "", page != null);

			Map<TYPE, Object> result = validator.validate(page);	
			if(result != null){
				content.put(pagename, (String) result.get(TYPE.data));
				List<W3CMessage> messages = (List<W3CMessage>)result.get(TYPE.messages);	
				if(messages != null){
					for(final W3CMessage message:messages){
						if(message.getType().equals("error")){
							CollectionClient.getInstance().addToMap(errors, pagename, message);
						}
						System.out.println("Message : "+message.getType()+" ("+message.getFirstRow()+","+message.getFirstColumn()+" => "+message.getLastRow()+","+message.getLastColumn()+") "+message.getMessage());
					}
				}	
				
			}
		}
		for(Map.Entry<String,List<W3CMessage>> pageErrors: errors.entrySet()){
			TestCase.assertTrue("Validation Errors found on Page "+pageErrors.getKey()+" : "+pageErrors.getValue()+"\nSource:\n "+content.get(pageErrors.getKey()),(pageErrors.getValue() != null) && (!pageErrors.getValue().isEmpty()));
		}		
	}

}

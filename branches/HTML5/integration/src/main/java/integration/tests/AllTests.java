package integration.tests;

import org.apache.maven.plugin.MojoFailureException;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gargoylesoftware.htmlunit.Page;

import eu.dzhw.zofar.management.comm.network.http.HTTPClient;
import integration.utils.MessageProvider;
import junit.framework.TestSuite;

@RunWith(Suite.class)
public class AllTests extends TestSuite {
	
	final static Logger LOGGER = LoggerFactory.getLogger(AllTests.class);

	public AllTests() {
		super();
		LOGGER.info("init");
	}

	@BeforeClass 
    public static void setUpClass() throws MojoFailureException {     
		LOGGER.info("setUpClass");
		final String loginUrl = System.getProperty("url")+"/"+System.getProperty("name")+"/special/login.html?zofar_token="+System.getProperty("token");
		MessageProvider.info(AllTests.class,"try to log in ... "+loginUrl);
		final HTTPClient client = HTTPClient.getInstance();
		client.setJavaScriptEnabled(false);
		client.printContentIfNecessary(true);
		try {
			Page page = client.loadPage(loginUrl);

//			webRequest.getRequestParameters().add(new NameValuePair("jsessionid", "934179CEDE3B37016895C94ADF0D953D"));
//			if((page != null)&&((HtmlPage.class).isAssignableFrom(page.getClass()))){
//				Map<String,String> requestParameters = HTTPClient.getQueryMap((HtmlPage)page);
//				System.out.println("requestParameters : "+requestParameters.toString());
//				if(requestParameters.containsKey("jsessionid")){
//					System.setProperty("sessionid", requestParameters.get("jsessionid"));
//				}
//				else System.setProperty("sessionid","");
//			}

			MessageProvider.info(AllTests.class,"logged in ... "+page.toString());
		} catch (Exception e) {
			throw new MojoFailureException(loginUrl, e);
		}
	}

    @AfterClass 
    public static void tearDownClass() throws MojoFailureException { 
    	LOGGER.info("tearDownClass");
		final String logoutUrl = System.getProperty("url")+"/"+System.getProperty("name")+"/j_spring_security_logout";
		MessageProvider.info(AllTests.class,"try to log out ... "+logoutUrl);
		final HTTPClient client = HTTPClient.getInstance();
		client.setJavaScriptEnabled(false);
		client.printContentIfNecessary(true);
		try {
			Page page = client.loadPage(logoutUrl);
			MessageProvider.info(AllTests.class,"logged out ... "+page.toString());
		} catch (Exception e) {
			throw new MojoFailureException(logoutUrl, e);
		}
    }

}

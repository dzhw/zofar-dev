package integration.tests.common;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.maven.plugin.MojoFailureException;
import org.junit.Ignore;
import org.junit.Test;

import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

import eu.dzhw.zofar.management.utils.objects.CollectionClient;
import eu.dzhw.zofar.management.utils.string.StringUtils;
import integration.tests.AbstracTestBase;
import integration.utils.MessageProvider;
import junit.framework.TestCase;

public class DefaultTest extends AbstracTestBase {

	@Test
	public void testSelectionWidthAttachmentJS() throws MojoFailureException, IOException {
		final HtmlPage page = this.gotoPage("page0.html");
		if (page == null)
			TestCase.assertTrue("Page not found " + "", page != null);

		final String variableSC = "sc0";
		final String variableOpen = "open1";

		final String groupId = "form:page0:SingleChoice1:rdc";
		final String openId = "form:page0:SingleChoice1:rdc:ao4:aopen:aoq";

		final List<String> options = CollectionClient.getInstance().asList("ao1", "ao2", "ao3");
		final String toSelect = CollectionClient.getInstance().shuffledList(options).get(0);

		final String expectedSelect = "ao4";

		final String expectedValue = StringUtils.getInstance().randomString(10);

		Map<String, String> dbResultSC = null;
		Map<String, String> dbResultOpen = null;
		try {
			HtmlPage tmpPage = this.selectSCRadio(page, groupId, toSelect);

			final Page resultPage = this.writeToOpen(tmpPage, openId, expectedValue, true);

			dbResultSC = this.getFromDB(this.getProperty("token") + "", variableSC);
			System.out.println("selected : " + toSelect + " db : " + dbResultSC + " expected : " + expectedSelect);

			dbResultOpen = this.getFromDB(this.getProperty("token") + "", variableOpen);
			System.out.println("written : " + expectedValue + " db : " + dbResultOpen);


		} catch (Exception e) {
			MessageProvider.error(this, e.toString(),e);
		}
		
		TestCase.assertTrue("Expected Selection Value " + expectedSelect + " do not match DB " + dbResultSC + "",(dbResultSC != null) && dbResultSC.containsKey(variableSC) && dbResultSC.get(variableSC).equals(expectedSelect));
		TestCase.assertTrue("Written Value " + expectedValue + " do not match DB " + dbResultOpen + "", (dbResultOpen != null) && dbResultOpen.containsKey(variableOpen) && dbResultOpen.get(variableOpen).equals(expectedValue));

	}

}

package integration.tests.components.multiplechoice.question;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.maven.plugin.MojoFailureException;
import org.junit.Before;
import org.junit.Test;

import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

import eu.dzhw.zofar.management.utils.objects.CollectionClient;
import eu.dzhw.zofar.management.utils.string.StringUtils;
import integration.tests.AbstracTestBase;
import junit.framework.TestCase;

public class MultipleChoiceTest extends AbstracTestBase {

	@Test
	public void testBasicSelection() throws MojoFailureException, IOException {
		final HtmlPage page = this.gotoPage("page7.html");
		if(page == null)TestCase.assertTrue("Page not found "+"",page != null);

		final String variable = "sc0";
//		final String groupId = "form:page0:SingleChoice1:rdc";
		final String groupId = "form:page7:mC1";
		final List<String> options = CollectionClient.getInstance().asList("mc1","mc2","mc3","mc4","mc5");
		final String toSelect = CollectionClient.getInstance().shuffledList(options).get(0);
		Map<String, String> dbResult = null;
		try {
//			final Page resultPage = this.clickForward(selectSCRadio(page,groupId,toSelect));
			final Page resultPage = this.clickForward(selectMultipleCheckBox(page,groupId,toSelect));
			
			//db check
			dbResult = this.getFromDB(this.getProperty("token")+"", variable);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		TestCase.assertTrue("Selected Value "+toSelect+" do not match DB "+dbResult.get(variable)+"",dbResult.containsKey(variable) && dbResult.get(variable).equals(toSelect));
	}
	
	@Test
	public void testSelectionWidthAttachmentJS() throws MojoFailureException, IOException {
		final HtmlPage page = this.gotoPage("page7.html");
		if(page == null)TestCase.assertTrue("Page not found "+"",page != null);

//		final String scVariable = "sc0";
//		final String openVariable = "open1";
		
		final String groupId = "form:page7:mC1";
		final String openId = "form:page7:mC1:rd1:ao5:ao6:aoq";
		
		final List<String> options = CollectionClient.getInstance().asList("mc1","mc2","mc3","mc4","mc5");
		final String toSelect = CollectionClient.getInstance().shuffledList(options).get(0);
		
		final String expectedSelect = "mc5";
		
		final String expectedValue = StringUtils.getInstance().randomString(10);
		String selected = null;
		String written = null;
		try {
			
			final HtmlPage resultPage1 = selectMultipleCheckBox(page,groupId,toSelect);
			
			
			final HtmlPage resultPage2 = this.writeToOpen(resultPage1, openId, expectedValue);
			
			selected = this.getMultipleCheckbox(resultPage2, groupId);
			written = this.getFromPage(resultPage2, openId);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		TestCase.assertTrue("Selected Value "+selected+" do not match expected "+expectedSelect+"",(selected != null)&&selected.equals(expectedSelect));
		TestCase.assertTrue("Written Value "+written+" do not match expected "+expectedValue+"",(written != null)&&written.equals(expectedValue));
	}
	
}

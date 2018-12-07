package integration.tests.components.singlechoice.question;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.maven.plugin.MojoFailureException;
import org.junit.Test;

import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

import eu.dzhw.zofar.management.utils.objects.CollectionClient;
import eu.dzhw.zofar.management.utils.string.StringUtils;
import integration.tests.AbstracTestBase;
import integration.utils.MessageProvider;
import junit.framework.TestCase;

public class SingleChoiceDropDownTest extends AbstracTestBase {

	@Test
	public void testBasicSelection() throws MojoFailureException, IOException {
		final HtmlPage page = this.gotoPage("page1.html");
		if (page == null)
			TestCase.assertTrue("Page not found " + "", page != null);

		final String variable = "sc1dropDown";
		final String groupId = "form:page1:SingleChoice1:rdc:dropDown";
		final List<String> options = CollectionClient.getInstance().asList("ao0", "ao1", "ao2");
		final String toSelect = CollectionClient.getInstance().shuffledList(options).get(0);

		Map<String, String> dbResult = null;
		try {
			final Page resultPage = this.clickForward(selectSCDropdown(page, groupId, toSelect));

			// db check
			dbResult = this.getFromDB(this.getProperty("token") + "", variable);

		} catch (Exception e) {
			MessageProvider.error(this, e.toString(),e);
		}

		TestCase.assertTrue("Selected Value " + toSelect + " do not match DB " + dbResult.get(variable) + "", dbResult.containsKey(variable) && dbResult.get(variable).equals(toSelect));
	}

	@Test
	public void testMissingSelection() throws MojoFailureException, IOException {
		final HtmlPage page = this.gotoPage("page1.html");
		if (page == null)
			TestCase.assertTrue("Page not found " + "", page != null);

		final String variable = "sc1missing";
		final String groupId = "form:page1:SingleChoice1:rdc:missing";

		final List<String> options = CollectionClient.getInstance().asList("ao3", "ao4");
		final String toSelect = CollectionClient.getInstance().shuffledList(options).get(0);

		Map<String, String> dbResult = null;
		try {
			final Page resultPage = this.clickForward(selectSCRadio(page, groupId, toSelect));

			// db check
			dbResult = this.getFromDB(this.getProperty("token") + "", variable);
			TestCase.assertTrue("Selected Value " + toSelect + " do not match DB " + dbResult.get(variable) + "", dbResult.containsKey(variable) && dbResult.get(variable).equals(toSelect));

		} catch (Exception e) {
			MessageProvider.error(this, e.toString(),e);
		}

	}

	@Test
	public void testCombinedSelection() throws MojoFailureException, IOException {
		final HtmlPage page = this.gotoPage("page1.html");
		if (page == null)
			TestCase.assertTrue("Page not found " + "", page != null);

		final String variable = "sc1";
		final String groupIdDropDown = "form:page1:SingleChoice1:rdc:dropDown";
		final String groupIdRadio = "form:page1:SingleChoice1:rdc:missing";

		final List<String> optionsDropDown = CollectionClient.getInstance().asList("ao1", "ao2");
		final List<String> optionsRadio = CollectionClient.getInstance().asList("ao3", "ao4");
		final List<String> options = new ArrayList<String>();
		options.addAll(optionsDropDown);
		options.addAll(optionsRadio);
		final String toSelect = CollectionClient.getInstance().shuffledList(options).get(0);

		Map<String, String> dbResult = null;
		try {
			HtmlPage tmpPage = null;
			if (optionsDropDown.contains(toSelect)) {
				tmpPage = selectSCDropdown(page, groupIdDropDown, toSelect);
			} else if (optionsRadio.contains(toSelect)) {
				tmpPage = selectSCRadio(page, groupIdRadio, toSelect);
			} else
				throw new MojoFailureException("something went wrong with random selection in Combined Dropdown test");

			final Page resultPage = this.clickForward(tmpPage);

			// db check
			dbResult = this.getFromDB(this.getProperty("token") + "", variable);

		} catch (Exception e) {
			MessageProvider.error(this, e.toString(),e);
		}

		TestCase.assertTrue("Selected Value " + toSelect + " do not match DB " + dbResult.get(variable) + "", dbResult.containsKey(variable) && dbResult.get(variable).equals(toSelect));
	}

	@Test
	public void testSelectionWidthAttachmentJS() throws MojoFailureException, IOException {
		final HtmlPage page = this.gotoPage("page1.html");
		if (page == null)
			TestCase.assertTrue("Page not found " + "", page != null);

		final String variableSC = "sc1";
		final String variableOpen = "open2";
		final String groupIdDropDown = "form:page1:SingleChoice1:rdc:dropDown";
		final String groupIdRadio = "form:page1:SingleChoice1:rdc:missing";
		final String openId = "form:page1:SingleChoice1:rdc:missing:ao4:aopen:aoq";

		final String expectedSelect = "ao4";
//		final String expectedValue = StringUtils.getInstance().randomString(10,false);
		final String expectedValue = StringUtils.getInstance().randomString(10);
		
		final List<String> optionsDropDown = CollectionClient.getInstance().asList("ao0", "ao1", "ao2");
		final List<String> optionsRadio = CollectionClient.getInstance().asList("ao3");
		final List<String> options = new ArrayList<String>();
		options.addAll(optionsDropDown);
		options.addAll(optionsRadio);
		final String toSelect = CollectionClient.getInstance().shuffledList(options).get(0);

		Map<String, String> dbResultSC = null;
		Map<String, String> dbResultOpen = null;
		try {
			HtmlPage tmpPage = null;
			if (optionsDropDown.contains(toSelect)) {
				tmpPage = selectSCDropdown(page, groupIdDropDown, toSelect);
			} else if (optionsRadio.contains(toSelect)) {
				tmpPage = this.selectSCRadio(page, groupIdRadio, toSelect);
			} else
				System.err.println("something went wrong with random selection in DropDown SelectionWidthAttachmentJS Test");

			this.writeToOpen(tmpPage, openId, expectedValue);

			final Page resultPage = this.clickForward(tmpPage);

			dbResultSC = this.getFromDB(this.getProperty("token") + "", variableSC);
			System.out.println("selected : " + toSelect + " db : " + dbResultSC + " expected : " + expectedSelect);

			dbResultOpen = this.getFromDB(this.getProperty("token") + "", variableOpen);
			System.out.println("written : " + expectedValue + " db : " + dbResultOpen);


		} catch (Exception e) {
			MessageProvider.error(this, e.toString(),e);
		}
		
		TestCase.assertTrue("Expected Selection Value " + expectedSelect + " do not match DB " + dbResultSC + "", (dbResultSC != null) && dbResultSC.containsKey(variableSC) && dbResultSC.get(variableSC).equals(expectedSelect));
		TestCase.assertTrue("Written Value " + expectedValue + " do not match DB " + dbResultOpen + "", (dbResultOpen != null) && dbResultOpen.containsKey(variableOpen) && dbResultOpen.get(variableOpen).equals(expectedValue));

	}

}

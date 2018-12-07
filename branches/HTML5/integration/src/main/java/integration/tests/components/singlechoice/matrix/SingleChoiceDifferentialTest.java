package integration.tests.components.singlechoice.matrix;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

import eu.dzhw.zofar.management.utils.objects.CollectionClient;
import integration.tests.AbstracTestBase;
import junit.framework.TestCase;

public class SingleChoiceDifferentialTest extends AbstracTestBase {
//	@Test
//	public void testDifferentialSelection() throws Exception {
//		final HtmlPage page = this.gotoPage("page4.html");
//		if (page == null)
//			TestCase.assertTrue("Page not found " + "", page != null);
//
//		final Map<String, String> rdc_var = new HashMap<String, String>();
//		final Map<String, List<String>> rdc_options = new HashMap<String, List<String>>();
//		final Map<String, String> rdc_selected = new HashMap<String, String>();
//
//		rdc_var.put("form:page4:msc:rd:i1:rd1", "msc4");
//		rdc_var.put("form:page4:msc:rd:i2:rd1", "msc5");
//		rdc_var.put("form:page4:msc:rd:i3:rd1", "msc6");
//		rdc_var.put("form:page4:msc:rd:i4:rd1", "msc7");
//
//		rdc_options.put("form:page4:msc:rd:i1:rd1",
//				CollectionClient.getInstance().asList("ao1", "ao2", "ao3", "ao4", "ao5", "ao6"));
//		rdc_options.put("form:page4:msc:rd:i2:rd1",
//				CollectionClient.getInstance().asList("ao1", "ao2", "ao3", "ao4", "ao5", "ao6"));
//		rdc_options.put("form:page4:msc:rd:i3:rd1",
//				CollectionClient.getInstance().asList("ao1", "ao2", "ao3", "ao4", "ao5", "ao6"));
//		rdc_options.put("form:page4:msc:rd:i4:rd1",
//				CollectionClient.getInstance().asList("ao1", "ao2", "ao3", "ao4", "ao5", "ao6"));
//
//		HtmlPage tmpPage = page;
//		for (final Map.Entry<String, List<String>> item : rdc_options.entrySet()) {
//			final List<String> options = item.getValue();
//			final String toSelect = CollectionClient.getInstance().shuffledList(options).get(0);
//			rdc_selected.put(item.getKey(), toSelect);
//			tmpPage = selectSCRadio(tmpPage, item.getKey(), toSelect);
//		}
//		final Page resultPage = this.clickForward(tmpPage);
//
//		for (final Map.Entry<String, String> item : rdc_var.entrySet()) {
//			final String variable = item.getValue();
//			final String toSelect = rdc_selected.get(item.getKey());
//
//			Map<String, String> dbResult = null;
//			try {
//
//				// db check
//				dbResult = this.getFromDB(this.getProperty("token") + "", variable);
//
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//
//			TestCase.assertTrue("Selected Value " + toSelect + " do not match DB " + dbResult.get(variable) + "",
//					dbResult.containsKey(variable) && dbResult.get(variable).equals(toSelect));
//		}
//	}
}

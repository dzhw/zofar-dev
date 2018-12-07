package eu.dzhw.zofar.management.calc.geo.main;

import java.awt.geom.Point2D;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.html.HtmlDivision;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;

import eu.dzhw.zofar.management.calc.geo.GeoNamesClient;
import eu.dzhw.zofar.management.comm.network.http.HTTPClient;
import eu.dzhw.zofar.management.utils.files.CSVClient;
import eu.dzhw.zofar.management.utils.files.FileClient;
import junit.framework.Assert;
import junit.framework.TestCase;

public class GeoClientTests extends TestCase {

	private GeoNamesClient client = null;

	@Before
	public void setUp() throws Exception {
		FileClient fileClient = FileClient.getInstance();
		CSVClient cvsClient = CSVClient.getInstance();

		final List<String> countries = new ArrayList<String>();
		countries.add("AT");
		countries.add("BE");
		countries.add("CH");
		countries.add("CZ");
		countries.add("DE");
		countries.add("DK");
		countries.add("NL");

		final List<String> headers = new ArrayList<String>();
		headers.add("countrycode");
		headers.add("postalcode");
		headers.add("placename");
		headers.add("adminname1");
		headers.add("admincode1");
		headers.add("adminname2");
		headers.add("admincode2");
		headers.add("adminname3");
		headers.add("admincode3");
		headers.add("latitude");
		headers.add("longitude");
		headers.add("accuracy");

		try {
			final List<Map<String, String>> allData = new ArrayList<Map<String, String>>();
			for (final String country : countries) {
				final File dataFile = fileClient.getResource(country + ".txt");
				if (dataFile.exists()) {
					// System.out.println("import data for "+country);
					final List<Map<String, String>> data = cvsClient.loadCSV(dataFile, headers, false, '\t',null);
					if (data != null)
						allData.addAll(data);
					else
						System.out.println("no data found in " + dataFile.getAbsolutePath());
				}
			}

			client = new GeoNamesClient(allData);

		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		java.util.logging.Logger.getLogger("com.gargoylesoftware").setLevel(Level.OFF); 
	}

	@After
	public void tearDown() throws Exception {
		client = null;
	}

	@Test
	public void test1() throws Exception {
		final String place1 = "Krummhörn";
		final String place2 = "Münchenbernsdorf";
		process(place1,place2);
	}
	
	@Test
	public void test2() throws Exception {
		final String place1 = "Bramsche";
		final String place2 = "Bottrop";
		process(place1,place2);
	}
	
	@Test
	public void test3() throws Exception {
		final String place1 = "Korswandt";
		final String place2 = "Geltendorf";
		process(place1,place2);
	}
	
	@Test
	public void test4() throws Exception {
		final String place1 = "Kampen";
		final String place2 = "Zittau";
		process(place1,place2);
	}
	
	@Test
	public void test5() throws Exception {
		final String place1 = "Tuttlingen";
		final String place2 = "Nordhorn";
		process(place1,place2);
	}
	
	private void process(final String place1, final String place2) throws Exception{
		if (client == null)
			fail("GeoClient initialisation failed");
		
		final Double distanceFromWeb = getDistanceFromWeb(place1, place2);
		final Double distanceFromLocal = getDistanceFromLocal(place1, place2);
		
		Double difference = Double.MAX_VALUE;
		if((distanceFromWeb != null)&&(distanceFromLocal != null))difference = Math.abs(distanceFromWeb-distanceFromLocal);
		
		System.out.println(place1+" <==> " + place2);
		System.out.println("Web Distance : " + distanceFromWeb);
		System.out.println("local Distance : " + distanceFromLocal);
		System.out.println("Difference Distance : " + difference);
		Assert.assertFalse("Distance Difference between "+place1+" and "+place2+" bigger than 5 km", difference > 5);
	}

	private Double getDistanceFromLocal(final String place1, final String place2) {
		try {
			Point2D startLocation = null;

			Map<String, List<Map<String, String>>> startPlaces = client.getPlacesByName(place1, "DE");
			for (final Map.Entry<String, List<Map<String, String>>> item : startPlaces.entrySet()) {
				final List<Map<String, String>> nationalStartPlaces = item.getValue();
				if (nationalStartPlaces.isEmpty())
					continue;
				for (final Map<String, String> nationalStartPlace : nationalStartPlaces) {
					startLocation = client.getLocation(nationalStartPlace);
				}
			}

			if (startLocation != null) {
				Map<String, List<Map<String, String>>> foundPlaces = client.getPlacesByName(place2, "DE");
				for (final Map.Entry<String, List<Map<String, String>>> item : foundPlaces.entrySet()) {
					final List<Map<String, String>> nationalPlaces = item.getValue();
					if (nationalPlaces.isEmpty())
						continue;
					for (final Map<String, String> nationalPlace : nationalPlaces) {
						final Point2D location = client.getLocation(nationalPlace);
						double distance = client.distanceKM(startLocation, location);
						return distance;

					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private Double getDistanceFromWeb(final String place1, final String place2) throws Exception {
		final HTTPClient spider = HTTPClient.getInstance();
		spider.setJavaScriptEnabled(false);
		Page page = spider.loadPage("http://www.postleitzahl.org/entfernung.html");
		
		
		if (page != null) {
			HtmlTextInput field1 = null;
			HtmlTextInput field2 = null;
			HtmlSubmitInput button = null;

			if ((HtmlPage.class).isAssignableFrom(page.getClass())) {
				final HtmlPage tmpPage = (HtmlPage) page;
				List<HtmlSubmitInput> buttons = spider.getSubmits(tmpPage);
				if (buttons != null) {
					for (final HtmlSubmitInput tmp : buttons) {
						final String value = tmp.getValueAttribute();
						if ((value != null) && (tmp.toString().equals("HtmlSubmitInput[<input type=\"submit\" name=\"submit\" value=\"&#160;Entfernung berechnen&#160;\">]"))) {
							button = tmp;
						}
					}
				}

				List<HtmlTextInput> fields = spider.getFields(tmpPage);
				if (fields != null) {
					for (final HtmlTextInput field : fields) {
						final String name = field.getNameAttribute();
						if ((name != null) && (name.equals("form[city1]"))) {
							field1 = field;
						}
						if ((name != null) && (name.equals("form[city2]"))) {
							field2 = field;
						}
					}
				}
			}

			if (button != null) {
				try {
					if (field1 != null)
						field1.setValueAttribute(place1);
					if (field2 != null)
						field2.setValueAttribute(place2);

					final Page resultPage = button.click();
					if (resultPage != null) {
						if ((HtmlPage.class).isAssignableFrom(resultPage.getClass())) {
							final HtmlPage tmpPage = (HtmlPage) resultPage;
							List<HtmlDivision> divs = spider.getDivs(tmpPage);
							if (divs != null) {
								for (final HtmlDivision div : divs) {
									if (div.getAttribute("style").startsWith("margin: auto; border-style: solid; border-color: #FFCC00; border-width: 1px; padding: 8px; background-color: #FFFFCC; width: 190px; text-align: center; font-size: 14px; font-weight: bold;")) {
										return Double.parseDouble(div.asText().replaceAll("km", "").trim());
									}
								}
							}
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}
}

package de.his.zofar.simulator.io.unit;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.Stack;

import org.apache.commons.lang3.RandomStringUtils;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.ElementNotFoundException;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebResponse;
import com.gargoylesoftware.htmlunit.html.HtmlCheckBoxInput;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlInput;
import com.gargoylesoftware.htmlunit.html.HtmlOption;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlRadioButtonInput;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextArea;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;

public class TestUnit {

	private String targetProtocol = "http";
	private String targetUrl = null;
	private int targetPort = 80;

	private WebClient webClient = null;
	private WebClient gcClient = null;

	private HtmlPage page = null;

	private String startURL = null;

	Stack<Map<String, Object>> records;
	Map<String, Map<String,String>> types;

	private String token;

	public TestUnit(final String proxyURL, final int proxyPort,
			final String targetProtocol, final String targetUrl,
			final int targetPort, final Stack<Map<String, Object>> records,Map<String, Map<String,String>> types) {
		super();

		if (proxyURL != null) {
			System.setProperty("http.proxyHost", proxyURL);
			System.setProperty("http.proxyPort", proxyPort + "");
		}

		if (targetProtocol != null)
			this.targetProtocol = targetProtocol;
		this.targetUrl = targetUrl;
		this.targetPort = targetPort;

		this.records = records;
		this.types = types;

		if (proxyURL != null)
			webClient = new WebClient(BrowserVersion.FIREFOX_17, proxyURL,
					proxyPort);
		else
			webClient = new WebClient(BrowserVersion.FIREFOX_17);

		if (webClient != null) {
			webClient.getOptions().setUseInsecureSSL(true);
			webClient.getOptions().setCssEnabled(false);
			webClient.getOptions().setJavaScriptEnabled(false);
			webClient.getOptions().setThrowExceptionOnScriptError(false);

			// Redirect false to measure timing of post and get phase
			// webClient.getOptions().setRedirectEnabled(true);
			webClient.getOptions().setRedirectEnabled(true);

			webClient.getCookieManager().setCookiesEnabled(true);

			// set Timeout to 2 Minute
			// webClient.getOptions().setTimeout(60 * 1000 * 2);
			webClient.getOptions().setTimeout(60 * 1000 * 10);

			webClient.addRequestHeader("dzhw-client-id", webClient.toString());
		}

		if (proxyURL != null)
			gcClient = new WebClient(BrowserVersion.FIREFOX_17, proxyURL,
					proxyPort);
		else
			gcClient = new WebClient(BrowserVersion.FIREFOX_17);

		if (gcClient != null) {
			gcClient.getOptions().setUseInsecureSSL(true);
			gcClient.getOptions().setCssEnabled(false);
			gcClient.getOptions().setJavaScriptEnabled(false);

			// Redirect false to measure timing of post and get phase
			// webClient.getOptions().setRedirectEnabled(true);
			gcClient.getOptions().setRedirectEnabled(true);

			gcClient.getCookieManager().setCookiesEnabled(true);

			// set Timeout to 2 Minute
			// webClient.getOptions().setTimeout(60 * 1000 * 2);
			gcClient.getOptions().setTimeout(60 * 1000 * 10);

			gcClient.addRequestHeader("dzhw-client-id", webClient.toString());
		}
	}

	public Object startup(final String payload)
			throws FailingHttpStatusCodeException, MalformedURLException,
			IOException {
//		this.token = payload;
		this.token = payload.substring(payload.lastIndexOf('=')+1);
		if (targetUrl != null) {
			final String get = targetProtocol + "://" + targetUrl + ":"
					+ targetPort + "/" + payload;
			// System.out.println("Startup unit for " + get);
			this.startURL = get;
			long postTime = 0L;
			if (webClient != null) {
				final long start = System.currentTimeMillis();
				page = startupHelper(get);
				final long stop = System.currentTimeMillis();

				final WebResponse response = page.getWebResponse();
				postTime = stop - start - response.getLoadTime();
				return pack(page, postTime);
			}
		}
		return null;
	}

	private void triggerGC(final HtmlPage calledPage) {
//		final String url = targetProtocol + "://" + targetUrl + ":"
//				+ targetPort + "/" + "survey003a-0.0.1-SNAPSHOT/special/gc.jsp";		
//		try {
//			page = gcClient.getPage(url);
//			System.out.println("triggerGC ("+calledPage+") "+page.asText().replace('\n', ' '));
//		} catch (FailingHttpStatusCodeException e) {
//			e.printStackTrace();
//		} catch (MalformedURLException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
	}

	private HtmlPage startupHelper(String url) {
		HtmlPage page = null;
		try {
			page = webClient.getPage(url);
		} catch (FailingHttpStatusCodeException e) {
			if (e.getStatusCode() == 302) {
				final String location = e.getResponse().getResponseHeaderValue(
						"Location");
				page = startupHelper(location);
			} else if (e.getStatusCode() == 500) {
				System.err.println("[ERROR " + this.token
						+ "]Internal Server Error, Retry start " + page);
				page = startupHelper(page.getUrl().toString());
			} else if (e.getStatusCode() == 503) {
				System.err.println("[ERROR " + this.token
						+ "]Service Unavailable, Retry start " + page);
				page = startupHelper(page.getUrl().toString());
			} else {
				final String length = e.getResponse().getResponseHeaderValue(
						"content-length");
				final String enc = e.getResponse().getResponseHeaderValue(
						"tranfer-encoding");
				final String timestamp = new SimpleDateFormat(
						"dd.MM.yyyy HH:mm:ss.SSS").format(System
						.currentTimeMillis());
				System.err.println("" + timestamp + "; [ERROR " + this.token
						+ "]  Got failing status " + e.getStatusCode()
						+ " for " + page + "(" + e.getMessage() + ") ==> "
						+ length + " " + enc + " clientId:"
						+ webClient.toString());

			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		triggerGC(page);
		return page;
	}

	private Map<String, Object> getRecordHelper(final String identifier,
			final boolean cut) {
		final String key = "/" + identifier + ".xhtml";

		boolean flag = false;
		Iterator<Map<String, Object>> recordIt = this.records.iterator();
		while (recordIt.hasNext()) {
			Map<String, Object> item = recordIt.next();
			if (item.containsKey("page") && item.get("page").equals(key)) {
				flag = true;
				break;
			}
		}
		if (flag) {
			final Stack<Map<String, Object>> cuttedRecords = this.records;
			while (!cuttedRecords.isEmpty()) {
				Map<String, Object> item = cuttedRecords.get(0);
				cuttedRecords.remove(0);
				if (item.containsKey("page") && item.get("page").equals(key)) {
					if (!cut)
						cuttedRecords.add(0, item);
					this.records = cuttedRecords;
					return item;
				}
			}
		}
		return null;
	}

	public Map<String, Object> getRecord(final String identifier) {
		// System.out.println("record1 : "+identifier);
		return getRecordHelper(identifier, false);
		// return null;
	}

	private Map<String, Object> getRecord(final HtmlPage page) {
		final URL url = (page.getWebResponse().getWebRequest()).getUrl();
		final String identifier = getFileName(url);
		// System.out.println("record2 : "+identifier);
		final Map<String, Object> result = getRecordHelper(identifier, true);
		return result;
	}

	public Object next() throws IOException {
		HashMap<String, String> back = null;
		if ((webClient != null) && (page != null)) {
			fillFields(page, getRecord(page));
			try {
				final HtmlSubmitInput submit = retrieveSubmit(page, "forwardBt");
				long postTime = 0L;
				if (submit != null) {
					try {
						URL oldPage = page.getUrl();
						page = submit.click();
						if (oldPage.equals(page.getUrl())) {
							System.err.println("[ERROR " + this.token
									+ "]Got same page again " + page);
						}
					} catch (FailingHttpStatusCodeException e) {
						if (e.getStatusCode() == 302) {
							postTime = e.getResponse().getLoadTime();
							final String location = e.getResponse()
									.getResponseHeaderValue("Location");
							final URL redirectURL = new URL(location);
							page = (HtmlPage) webClient.getPage(redirectURL);
						} else if (e.getStatusCode() == 500) {
							System.err.println("[ERROR " + this.token
									+ "]Internal Server Error, Retry load "
									+ page);
							postTime = e.getResponse().getLoadTime();
							page = (HtmlPage) webClient.getPage(page.getUrl());
						} else if (e.getStatusCode() == 503) {
							System.err.println("[ERROR " + this.token
									+ "]Service Unavailable, Retry load "
									+ page);
							postTime = e.getResponse().getLoadTime();
							page = (HtmlPage) webClient.getPage(page.getUrl());
						} else {
							final String length = e.getResponse()
									.getResponseHeaderValue("content-length");
							final String enc = e.getResponse()
									.getResponseHeaderValue("tranfer-encoding");
							final String timestamp = new SimpleDateFormat(
									"dd.MM.yyyy HH:mm:ss.SSS").format(System
									.currentTimeMillis());
							System.err
									.println("" + timestamp + "; [ERROR "
											+ this.token
											+ "]  Got failing status "
											+ e.getStatusCode() + " for "
											+ page + "(" + e.getMessage()
											+ ") ==> " + length + " " + enc
											+ " clientId:"
											+ webClient.toString());

						}
					}
					back = pack(page, postTime);
				} else
					System.err.println("[ERROR " + this.token
							+ "]No submit Button found on " + page);
			} catch (final ElementNotFoundException e) {
				System.out.println("[NOTFOUND] " + this.startURL);
				shutdown();
			}

		}
		triggerGC(page);
		return back;
	}

	public Object previous() throws IOException {
		HashMap<String, String> back = null;
		if ((webClient != null) && (page != null)) {
			fillFields(page, getRecord(page));
			final HtmlSubmitInput submit = retrieveSubmit(page, "backwardBt");
			long postTime = 0L;
			if (submit != null) {
				try {
					page = submit.click();
				} catch (FailingHttpStatusCodeException e) {
					if (e.getStatusCode() == 302) {
						postTime = e.getResponse().getLoadTime();
						final String location = e.getResponse()
								.getResponseHeaderValue("Location");
						final URL redirectURL = new URL(location);
						page = (HtmlPage) webClient.getPage(redirectURL);
					}
				}
				back = pack(page, postTime);
			}
		}
		triggerGC(page);
		return back;
	}

	private HtmlSubmitInput retrieveSubmit(final HtmlPage page,
			final String name) {
		if (page != null) {
			final List<HtmlForm> forms = page.getForms();
			if (forms != null) {
				final Iterator<HtmlForm> it = forms.iterator();
				while (it.hasNext()) {
					final HtmlForm form = it.next();
					final List<?> submits = form
							.getByXPath("//input[@type='submit']");
					if ((submits != null) && (!submits.isEmpty())) {
						final Iterator<?> submitIt = submits.iterator();
						while (submitIt.hasNext()) {
							final Object tmp = submitIt.next();
							if ((HtmlSubmitInput.class).isAssignableFrom(tmp
									.getClass())) {
								final HtmlSubmitInput submit = (HtmlSubmitInput) tmp;
								if (submit.getId().endsWith(name))
									return submit;
							}
						}
					}
				}
			}
		}
		return null;
	}

	private HashMap<String, String> pack(final HtmlPage page,
			final long postTime) {
		if (page == null)
			return null;
		final HashMap<String, String> back = new HashMap<String, String>();
		final WebResponse response = page.getWebResponse();
		back.put("time", response.getLoadTime() + "");
		back.put("posttime", postTime + "");
		back.put("code", response.getStatusCode() + "");
		back.put("mesg", response.getStatusMessage());

		final URL url = (response.getWebRequest()).getUrl();
		back.put("tag", getFileName(url));

		final String tmpCookie = (webClient.getCookieManager()
				.getCookie("JSESSIONID")).getValue();
		back.put("cookie", tmpCookie);

		String worker = "UNKNOWN";
		if (tmpCookie.indexOf(".") > -1) {
			worker = tmpCookie.substring(tmpCookie.lastIndexOf(".") + 1,
					tmpCookie.length()).toUpperCase();
		}

		back.put("worker", worker);
		return back;
	}

	private String getFileName(final URL extUrl) {
		String filename = "";
		final String path = extUrl.getPath();
		final String[] pathContents = path.split("[\\\\/]");
		if (pathContents != null) {
			final int pathContentsLength = pathContents.length;
			final String lastPart = pathContents[pathContentsLength - 1];
			final String[] lastPartContents = lastPart.split("\\.");
			if (lastPartContents != null && lastPartContents.length > 1) {
				final int lastPartContentLength = lastPartContents.length;
				String name = "";
				for (int i = 0; i < lastPartContentLength; i++) {
					if (i < (lastPartContents.length - 1)) {
						name += lastPartContents[i];
						if (i < (lastPartContentLength - 2)) {
							name += ".";
						}
					}
				}
				filename = name;
			}
		}
		return filename;
	}

	private static final HtmlRadioButtonInput[] SINGLEDUMMY = new HtmlRadioButtonInput[0];

	private void fillFields(final HtmlPage page,
			final Map<String, Object> recordData) {
		// System.out.println("use record : " + recordData);
		final List<HtmlForm> forms = page.getForms();
		if (forms != null) {
			final Iterator<HtmlForm> it = forms.iterator();
			while (it.hasNext()) {
				final HtmlForm form = it.next();
				fillFormFields(form, recordData);
			}
		}
	}

	private void setContent(final String inputId,
			final Set<HtmlRadioButtonInput> input,
			final Map<String, Object> recordData) {
		if (input == null)
			return;
		Object record = null;
		if ((recordData != null) && recordData.containsKey(inputId))
			record = (String) recordData.get(inputId);
		
		String variable = "UNKOWN";
		Map<String,String> meta = getMetaFor(inputId);
		if(meta != null){
			variable = meta.get("VARIABLE");
		}

		if ((input != null) && (input.size() > 0)) {
			if (record != null) {
//				final Iterator<HtmlRadioButtonInput> it = input.iterator();
//				while (it.hasNext()) {
//					final HtmlRadioButtonInput tmp = it.next();
//					// System.out.println("Attribute ("+record.equals(tmp.getValueAttribute())+") : "+tmp.getValueAttribute());
//					if (record.equals(tmp.getValueAttribute())) {
//						try {
//							tmp.click();
//						} catch (final IOException e) {
//							e.printStackTrace();
//						}
//						break;
//					}
//				}
			} else {
				String value = "UNKOWN";
				final HtmlRadioButtonInput[] itemArray = input
						.toArray(SINGLEDUMMY);
				final int rndIndex = (int) (Math.random() * ((itemArray.length - 1) - 0));
				if ((rndIndex >= 0) && (rndIndex < itemArray.length)) {
					final HtmlRadioButtonInput select = itemArray[rndIndex];
					if (select != null) {
						if(select.isDisplayed()){
							try {
								value = select.getValueAttribute();
								select.click();
							} catch (final IOException e) {
								e.printStackTrace();
							}	
						}
						else{
							value = "INVISIBLE";
						}
					}
				}
				
				logSetted(variable,inputId,value);
			}
		}
	}
	
	private static final HtmlOption[] SINGLEDUMMYDROPDOWN = new HtmlOption[0];

	
	private void setContentDropDown(final String inputId,
			final Set<HtmlOption> input,
			final Map<String, Object> recordData) {
		if (input == null)
			return;
		Object record = null;
		if ((recordData != null) && recordData.containsKey(inputId))
			record = (String) recordData.get(inputId);
		
		String variable = "UNKOWN";
		Map<String,String> meta = getMetaFor(inputId);
		if(meta != null){
			variable = meta.get("VARIABLE");
		}

		if ((input != null) && (input.size() > 0)) {
			if (record != null) {
//				final Iterator<HtmlOption> it = input.iterator();
//				while (it.hasNext()) {
//					final HtmlOption tmp = it.next();
//					// System.out.println("Attribute ("+record.equals(tmp.getValueAttribute())+") : "+tmp.getValueAttribute());
//					if (record.equals(tmp.getValueAttribute())) {
//						try {
//							tmp.click();
//						} catch (final IOException e) {
//							e.printStackTrace();
//						}
//						break;
//					}
//				}
			} else {
				String value = "UNKOWN";
				final HtmlOption[] itemArray = input
						.toArray(SINGLEDUMMYDROPDOWN);
				final int rndIndex = (int) (Math.random() * ((itemArray.length - 1) - 0));
				if ((rndIndex >= 0) && (rndIndex < itemArray.length)) {
					final HtmlOption select = itemArray[rndIndex];
					if (select != null) {
						if(select.isDisplayed()){
							try {
								value = select.getValueAttribute();
								select.click();
							} catch (final IOException e) {
								e.printStackTrace();
							}
						}
						else{
							value = "INVISIBLE";
						}
					}
				}
				logSetted(variable,inputId,value);
			}
		}
	}
	
	private void setContent(final HtmlTextArea input,
			final Map<String, Object> recordData) {
		if (input == null)
			return;
		final HtmlTextArea tmp = input;
		final String inputId = tmp.getId();
		Object record = null;

		if ((recordData != null) && recordData.containsKey(inputId))
			record = (String) recordData.get(inputId);
		
		String variable = "UNKOWN";
		String mode = "UNKOWN";
		int max = -1;
		int minValue = -1;
		int maxValue = -1;
		Map<String,String> meta = getMetaFor(inputId);
		System.out.println("meta : "+meta);
		if(meta != null){
			variable = meta.get("VARIABLE").trim();
			mode = meta.get("MODE").trim();
			max = Integer.parseInt(meta.get("MAX").trim());
			minValue = Integer.parseInt(meta.get("MINVALUE").trim());
			maxValue = Integer.parseInt(meta.get("MAXVALUE").trim());
		}
		
		else System.err.println("no entry found for : "+inputId);

		if ((HtmlTextArea.class).isAssignableFrom(tmp.getClass())) {
			if (record != null){
//				tmp.setText(record + "");
			}else{
				String value = ""; 
				if(mode.equals("OPEN")){
					value = this.token+" "+(new Date()).toString(); 
					if(max >= 0) value = value.substring(0,Math.min(value.length(), max));
				}
				else if(mode.equals("OPEN.number")){
					Random rand = new Random();
//					int randomNum = rand.nextInt((10 - 1) + 1) + 1;
					
					int tmpMin = 10;
					if(minValue >= 0) tmpMin = minValue;
					
					int tmpMax = 10;
					if(maxValue >= 0) tmpMax = maxValue;
					
					
					int randomNum = rand.nextInt((tmpMax - tmpMin) + 1) + tmpMin;
					
					
					value = randomNum+""; 
				}
				else if(mode.equals("OPEN.grade")){
					Random rand = new Random();
					int randomNum = rand.nextInt((6 - 1) + 1) + 1;
					value = randomNum+""; 
				}
				else if(mode.equals("OPEN.nonumbers")){
					value = RandomStringUtils.randomAlphabetic(20); 
					if(max >= 0) value = value.substring(0,Math.min(value.length(), max));
				}
				else if(mode.equals("OPEN.mail")){
					value = RandomStringUtils.randomAlphabetic(4)+"@"+RandomStringUtils.randomAlphabetic(4)+"."+RandomStringUtils.randomAlphabetic(2); 
				}
				else{
					System.err.println("non checked open type : "+mode);
				}
				tmp.setText(value);
				logSetted(variable,inputId,value);
			}
		}
	}

	private void setContent(final HtmlInput input,
			final Map<String, Object> recordData) {
		if (input == null)
			return;
		final HtmlInput tmp = input;
		final String inputId = tmp.getId();
		Object record = null;

		if ((recordData != null) && recordData.containsKey(inputId))
			record = (String) recordData.get(inputId);
		
		String variable = "UNKOWN";
		String mode = "UNKOWN";
		int max = -1;
		int minValue = -1;
		int maxValue = -1;
		Map<String,String> meta = getMetaFor(inputId);
		System.out.println("meta : "+meta);
		if(meta != null){
			variable = meta.get("VARIABLE").trim();
			mode = meta.get("MODE").trim();
			max = Integer.parseInt(meta.get("MAX").trim());
			minValue = Integer.parseInt(meta.get("MINVALUE").trim());
			maxValue = Integer.parseInt(meta.get("MAXVALUE").trim());
		}

		if ((HtmlTextInput.class).isAssignableFrom(tmp.getClass())) {
			if (record != null){
//				tmp.setValueAttribute(record + "");
			}else{
				String value = ""; 
				if(mode.equals("OPEN")){
					value = this.token+" "+(new Date()).toString(); 
					if(max >= 0) value = value.substring(0,Math.min(value.length(), max));
				}
				else if(mode.equals("OPEN.text")){
					value = this.token+" "+(new Date()).toString(); 
					if(max >= 0) value = value.substring(0,Math.min(value.length(), max));
				}
				else if(mode.equals("OPEN.number")){
					Random rand = new Random();
//					int randomNum = rand.nextInt((10 - 1) + 1) + 1;
					
					int tmpMin = 10;
					if(minValue >= 0) tmpMin = minValue;
					
					int tmpMax = 10;
					if(maxValue >= 0) tmpMax = maxValue;
					
					
					int randomNum = rand.nextInt((tmpMax - tmpMin) + 1) + tmpMin;
					value = randomNum+""; 
				}
				else if(mode.equals("OPEN.grade")){
					Random rand = new Random();
					int randomNum = rand.nextInt((6 - 1) + 1) + 1;
					value = randomNum+""; 
				}
				else if(mode.equals("OPEN.nonumbers")){
					value = RandomStringUtils.randomAlphabetic(20); 
					if(max >= 0) value = value.substring(0,Math.min(value.length(), max));
				}
				else if(mode.equals("OPEN.mail")){
					value = RandomStringUtils.randomAlphabetic(4)+"@"+RandomStringUtils.randomAlphabetic(4)+"."+RandomStringUtils.randomAlphabetic(2); 
				}
				else{
					System.err.println("non checked open type : "+mode);
				}
				tmp.setValueAttribute(value);
				logSetted(variable,inputId,value);
			}
		} else if ((HtmlCheckBoxInput.class).isAssignableFrom(tmp.getClass())) {
			if (record != null) {
				// boolean flag = (Boolean) record;
				// // System.out.println("flag : "+flag);
				// if (flag) {
				// try {
				// if (!tmp.isChecked())
				// tmp.click();
				// } catch (final IOException e) {
				// e.printStackTrace();
				// }
				// } else {
				// try {
				// if (tmp.isChecked())
				// tmp.click();
				// } catch (final IOException e) {
				// e.printStackTrace();
				// }
				// }
			}
			else {
				if(tmp.isDisplayed()){
					try {
						Random random = new Random();
						boolean value = random.nextBoolean();
						//Uncheck
						if(tmp.isChecked())tmp.click();
						if(value)tmp.click();
						logSetted(variable,inputId,tmp.isChecked()+"");
					} catch (final IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}
	
	private Map<String,String> getMetaFor(final String inputId){
		String cleanedId = inputId;
		cleanedId = cleanedId.replaceAll("j_idt[0-9]*:?", "");
		cleanedId = cleanedId.replaceAll(":?missing", "");
		cleanedId = cleanedId.replaceAll(":sort:", ":");
//		cleanedId = cleanedId.replaceAll(":ird:", ":");
		
		Map<String,String> meta = null;
		if(this.types.containsKey(cleanedId)){
			meta = this.types.get(cleanedId);
		}
//		System.out.println("[METADATA] "+inputId+" ("+cleanedId+") ==> "+meta); 
		return meta;
	}

	private void fillFormFields(final HtmlForm form,
			final Map<String, Object> recordData) {
		if (form == null)
			return;
		final List<?> singleInputs = form.getByXPath("//input[@type='radio']");
		final List<?> dropInputs = form.getByXPath("//select");
		final List<?> multipleInputs = form
				.getByXPath("//input[@type='checkbox']");
		final List<?> textInputs = form.getByXPath("//input[@type='text']");
		final List<?> textAreas = form.getByXPath("//textarea");

		if ((textInputs != null) && (!textInputs.isEmpty())) {
			final Iterator<?> it = textInputs.iterator();
			while (it.hasNext()) {
				final Object tmp = it.next();
				if (tmp == null)
					continue;
				if (!(HtmlInput.class).isAssignableFrom(tmp.getClass()))
					continue;
				setContent((HtmlInput) tmp, recordData);
			}
		}

		if ((textAreas != null) && (!textAreas.isEmpty())) {
			final Iterator<?> it = textAreas.iterator();
			while (it.hasNext()) {
				final Object tmp = it.next();
				if (tmp == null)
					continue;
				// System.out.println("textarea : "+tmp.getClass());
				if (!(HtmlTextArea.class).isAssignableFrom(tmp.getClass()))
					continue;
				setContent((HtmlTextArea) tmp, recordData);
			}
		}

		if ((multipleInputs != null) && (!multipleInputs.isEmpty())) {
			final Iterator<?> it = multipleInputs.iterator();
			while (it.hasNext()) {
				final Object tmp = it.next();
				if (tmp == null)
					continue;
				if (!(HtmlInput.class).isAssignableFrom(tmp.getClass()))
					continue;
				setContent((HtmlInput) tmp, recordData);
			}
		}
		if ((singleInputs != null) && (!singleInputs.isEmpty())) {
			Iterator<?> it = singleInputs.iterator();
			final HashMap<String, HashSet<HtmlRadioButtonInput>> groups = new HashMap<String, HashSet<HtmlRadioButtonInput>>();
			while (it.hasNext()) {
				final Object tmp = it.next();
				if ((tmp != null) && (tmp instanceof HtmlRadioButtonInput)) {
					final HtmlRadioButtonInput input = (HtmlRadioButtonInput) tmp;
					final String name = input.getAttribute("name");

					HashSet<HtmlRadioButtonInput> alternatives = null;
					if (groups.containsKey(name))
						alternatives = groups.get(name);
					if (alternatives == null)
						alternatives = new HashSet<HtmlRadioButtonInput>();

					alternatives.add(input);

					groups.put(name, alternatives);
				}
			}

			it = groups.keySet().iterator();
			while (it.hasNext()) {
				final Object group = it.next();
				if ((group != null) && (group instanceof String)) {
					// String groupId = decryptId((String) group);
					final String groupId = (String) group;

					if (groupId != null) {
						final HashSet<HtmlRadioButtonInput> items = groups
								.get(group);
						if (items == null)
							continue;
						setContent(groupId, items, recordData);
					}
				}
			}
		}
		
		if ((dropInputs != null) && (!dropInputs.isEmpty())) {
			Iterator<?> it = dropInputs.iterator();
			final HashMap<String, HashSet<HtmlOption>> groups = new HashMap<String, HashSet<HtmlOption>>();
			while (it.hasNext()) {
				final Object tmp = it.next();
				if ((tmp != null) && (tmp instanceof HtmlOption)) {
					final HtmlOption input = (HtmlOption) tmp;
					final String name = input.getAttribute("name");

					HashSet<HtmlOption> alternatives = null;
					if (groups.containsKey(name))
						alternatives = groups.get(name);
					if (alternatives == null)
						alternatives = new HashSet<HtmlOption>();

					alternatives.add(input);

					groups.put(name, alternatives);
				}
			}

			it = groups.keySet().iterator();
			while (it.hasNext()) {
				final Object group = it.next();
				if ((group != null) && (group instanceof String)) {
					// String groupId = decryptId((String) group);
					final String groupId = (String) group;

					if (groupId != null) {
						final HashSet<HtmlOption> items = groups
								.get(group);
						if (items == null)
							continue;
						setContentDropDown(groupId, items, recordData);
					}
				}
			}
		}
	}
	
	private Map<String,Map<String,Object>> persistenceMap;
	
	private void logSetted(final String variable,final String id,final String value){
		if(this.persistenceMap == null)persistenceMap = new HashMap<String,Map<String,Object>>();
		Map<String,Object> pair = null;
		if(persistenceMap.containsKey(variable)){
			pair = persistenceMap.get(variable);
			pair.put("VALUE", value);
			pair.put("VERSION", ((Integer)pair.get("VERSION"))+1);
		}
		else{
			pair = new HashMap<String,Object>();
			pair.put("VALUE", value);
			pair.put("VERSION", 0);
		}
		persistenceMap.put(variable, pair);
//		System.out.println("[PERSISTENCECHECK] "+this.token+";"+id+";"+variable+";"+value);
	}
	
	public String getPersistenceLog(){
		if(this.persistenceMap != null){
			final StringBuffer back = new StringBuffer();
			back.append("VARIABLE;DATA;VERSION;\n");
			for(final Map.Entry<String, Map<String,Object>> entry:persistenceMap.entrySet()){
				final String variable = entry.getKey();
				final String data = (String) entry.getValue().get("VALUE");
				final Integer version = (Integer)entry.getValue().get("VERSION");
				back.append(variable+";"+data+";"+version+";\n");
			}
			return back.toString();
		}
		return null;
	}

	public void shutdown() {
		if (webClient != null) {
			webClient.closeAllWindows();
			webClient = null;
		}
	}

}

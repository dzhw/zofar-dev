package integration.tests;

import java.io.IOException;
import java.sql.Connection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.maven.plugin.MojoFailureException;
import org.apache.xmlbeans.XmlObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.w3c.dom.Document;

import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.html.HtmlCheckBoxInput;
import com.gargoylesoftware.htmlunit.html.HtmlInput;
import com.gargoylesoftware.htmlunit.html.HtmlOption;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlRadioButtonInput;
import com.gargoylesoftware.htmlunit.html.HtmlSelect;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.gargoylesoftware.htmlunit.html.HtmlCheckBoxInput;

import eu.dzhw.zofar.management.comm.db.postgresql.PostgresClient;
import eu.dzhw.zofar.management.comm.network.http.HTTPClient;
import eu.dzhw.zofar.management.utils.xml.XmlClient;
import integration.utils.MessageProvider;
import junit.framework.TestCase;

@Ignore
public abstract class AbstracTestBase extends TestCase {

	public AbstracTestBase() {
		super();
	}

	@Override
	@Before
	public void setUp() throws Exception {
	}

	@Override
	@After
	public void tearDown() throws Exception {
		this.toIndex();
	}

	protected void showProperties() {
		for (final Entry<Object, Object> property : System.getProperties().entrySet()) {
			MessageProvider.info(this, "Property " + property.getKey() + " = " + property.getValue());
		}
	}

	protected Object getProperty(final String key) {
		if (System.getProperties().containsKey(key)) {
			return System.getProperties().get(key);
		}
		return null;
	}

	protected String getBaseUrl() {
		if (System.getProperties().containsKey("url") && System.getProperties().containsKey("name")) {
			return System.getProperty("url") + "/" + System.getProperty("name");
		}
		return null;
	}
	
	protected XmlObject getQML() throws Exception {
		if (System.getProperties().containsKey("project.path") && System.getProperties().containsKey("project.path")) {
//			return System.getProperty("project.path") + "/src/main/resources/questionnaire.xml";
			final Document doc = XmlClient.getInstance().getDocument(System.getProperty("project.path") + "/src/main/resources/questionnaire.xml");
			if(doc != null) return XmlClient.getInstance().docToXmlObject(doc);
		}
		return null;
	}

	// protected HtmlPage loadPage(final String url) throws Exception{
	// final HTTPClient client = HTTPClient.getInstance();
	// final Page page = client.loadPage(url);
	// if((page != null)&&(HtmlPage.class).isAssignableFrom(page.getClass())){
	// return (HtmlPage)page;
	// }
	// return null;
	// }

	protected void toIndex() throws MojoFailureException {
		final String url = this.getBaseUrl() + "/index.html";
		MessageProvider.info(this, "goto Index Page... " + url);
		final HTTPClient client = HTTPClient.getInstance();
		client.setJavaScriptEnabled(false);
		client.printContentIfNecessary(true);
		try {
			final Page page = client.loadPage(url);
			MessageProvider.info(this, "Index Page reached ... " + page.toString());
		} catch (final Exception e) {
			throw new MojoFailureException(url, e);
		}
	}

	protected HtmlPage gotoPage(final String pagename) throws IOException, MojoFailureException {
		final String url = this.getBaseUrl() + "/" + pagename;
		MessageProvider.info(this, "goto Page ... " + url);
		final HTTPClient client = HTTPClient.getInstance();
		client.setJavaScriptEnabled(true);
		client.setCSSEnabled(false);
		client.printContentIfNecessary(true);
		try {
			final Page page = client.loadPage(url);
			MessageProvider.info(this, "page reached ... " + page.toString());
			return (HtmlPage) page;
		} catch (final Exception e) {
			throw new MojoFailureException(url, e);
		}
	}

	protected Page clickForward(final HtmlPage page) throws IOException {
		return this.clickSubmit(page, "forwardBt");
	}

	protected Page clickBack(final HtmlPage page) throws IOException {
		return this.clickSubmit(page, "backwardBt");
	}

	private Page clickSubmit(final HtmlPage page, final String name) throws IOException {
		HtmlSubmitInput submit = null;
		final List<HtmlSubmitInput> submits = HTTPClient.getInstance().getSubmits(page);
		if ((submits != null) && (!submits.isEmpty())) {
			for (final HtmlSubmitInput tmp : submits) {
				if ((tmp.getNameAttribute() != null) && tmp.getNameAttribute().endsWith(name)) {
					submit = tmp;
					break;
				}
			}
		}

		if (submit != null) {
			final Page newPage = submit.click();
			return newPage;
		}
		return null;
	}

	protected Map<String, String> getAllFromDB(final String token) throws Exception {
		if (System.getProperties().containsKey("db.password") && System.getProperties().containsKey("db.username") && System.getProperties().containsKey("db.host") && System.getProperties().containsKey("db.dbname")) {
			return this.getAllFromDB(this.getProperty("db.host") + "", "5432", this.getProperty("db.dbname") + "", this.getProperty("db.username") + "", this.getProperty("db.password") + "", token);
		} else {
			throw new Exception("uncomplete DB Login Informations");
		}
	}

	protected Map<String, String> getAllFromDB(final String db_location, final String db_port, final String db_name, final String db_user, final String db_password, final String token) throws Exception {
		final PostgresClient postgres = PostgresClient.getInstance();
		Connection connection = null;
		try {
			connection = postgres.getConnection(db_location, db_port, db_name, db_user, db_password);
		} catch (final Exception e) {
			throw new Exception("cannot establish connection : location=" + db_location + " port=" + db_port + " dbname=" + db_name + " db_user=" + db_user + " db_password=" + db_password, e);
		}

		if (connection == null) {
			return null;
		}

		List<Map<String, String>> fromDB = null;
		try {
			fromDB = postgres.queryDb(connection, "select t1.variablename AS variable,t1.value as data,t1.version as version from surveydata AS t1,participant AS t2 where t1.participant_id=t2.id AND t2.token = '" + token + "';");
		} catch (final Exception e) {
			throw new Exception("cannot query db : location=" + db_location + " port=" + db_port + " dbname=" + db_name + " db_user=" + db_user + " db_password=" + db_password, e);
		}

		if (fromDB == null) {
			return null;
		}

		final Map<String, String> fromDBData = new HashMap<String, String>();

		for (final Map<String, String> line : fromDB) {
			final String variable = line.get("variable");
			final String tmpData = line.get("data");
			fromDBData.put(variable, tmpData);
		}

		return fromDBData;
	}

	protected String getFromPage(final HtmlPage tmpPage, final String inputId) {
		HtmlInput input = null;
		final List<HtmlInput> inputs = HTTPClient.getInstance().getInputs(tmpPage);
		for (final HtmlInput tmp : inputs) {
			if (!tmp.isDisplayed())
				continue;
			if ((tmp.getNameAttribute() != null) && tmp.getNameAttribute().equals(inputId)) {
				input = tmp;
				break;
			}
		}

		if (input != null) {
			final String type = input.getTypeAttribute();
			if (type != null) {
				if (type.equals("radio"))
					return ((HtmlRadioButtonInput) input).isChecked() + "";
				else if (type.equals("checkbox"))
					return ((HtmlCheckBoxInput) input).isChecked() + "";
				else if (type.equals("text"))
					return ((HtmlTextInput) input).getValueAttribute();
				else if (type.equals("submit"))
					return ((HtmlSubmitInput) input).isChecked() + "";
				else
					MessageProvider.error(this, "unhandled input type found  " + type);
			}
		} else
			MessageProvider.error(this, "no input found with id " + inputId);
		return null;
	}

	protected Map<String, String> getFromDB(final String token, final String variable) throws Exception {
		if (System.getProperties().containsKey("db.password") && System.getProperties().containsKey("db.username") && System.getProperties().containsKey("db.host") && System.getProperties().containsKey("db.dbname")) {
			return this.getFromDB(this.getProperty("db.host") + "", "5432", this.getProperty("db.dbname") + "", this.getProperty("db.username") + "", this.getProperty("db.password") + "", token, variable);
		} else {
			throw new Exception("uncomplete DB Login Informations");
		}
	}

	protected Map<String, String> getFromDB(final String db_location, final String db_port, final String db_name, final String db_user, final String db_password, final String token, final String variable) throws Exception {
		final PostgresClient postgres = PostgresClient.getInstance();
		Connection connection = null;
		try {
			connection = postgres.getConnection(db_location, db_port, db_name, db_user, db_password);
		} catch (final Exception e) {
			throw new Exception("cannot establish connection : location=" + db_location + " port=" + db_port + " dbname=" + db_name + " db_user=" + db_user + " db_password=" + db_password, e);
		}

		if (connection == null) {
			return null;
		}

		List<Map<String, String>> fromDB = null;
		try {
			fromDB = postgres.queryDb(connection, "select t1.variablename AS variable,t1.value as data,t1.version as version from surveydata AS t1,participant AS t2 where t1.participant_id=t2.id AND t2.token = '" + token + "' AND t1.variablename = '" + variable + "';");
		} catch (final Exception e) {
			throw new Exception("cannot query db : location=" + db_location + " port=" + db_port + " dbname=" + db_name + " db_user=" + db_user + " db_password=" + db_password, e);
		}

		if (fromDB == null) {
			return null;
		}

		final Map<String, String> fromDBData = new HashMap<String, String>();

		for (final Map<String, String> line : fromDB) {
			final String tmpVariable = line.get("variable");
			final String tmpData = line.get("data");
			fromDBData.put(tmpVariable, tmpData);
		}

		return fromDBData;
	}

	protected HtmlPage selectSCRadio(final HtmlPage tmpPage, final String groupId, final String valueId) throws Exception {
		final List<?> singleInputs = HTTPClient.getInstance().getRadios(tmpPage);
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

			if (!groups.containsKey(groupId)) {
				throw new Exception("No Group found with ID " + groupId);
			}

			final HashSet<HtmlRadioButtonInput> items = groups.get(groupId);
			if ((items == null) || (items.isEmpty())) {
				throw new Exception("No Items found for Group " + groupId);
			}

			boolean found = false;

			for (final HtmlRadioButtonInput select : items) {
				if (select != null) {
					if (select.isDisplayed()) {
						try {
							final String value = select.getValueAttribute();
							if ((value != null) && (value.equals(valueId))) {
								return select.click();
							}
						} catch (final IOException e) {
							e.printStackTrace();
						}
					}
				}
			}
			if (!found)
				throw new Exception("No visible Item found with ID " + valueId);

		}
		return tmpPage;
	}

	protected HtmlPage selectSCDropdown(final HtmlPage tmpPage, final String groupId, final String valueId) throws Exception {
		final List<HtmlSelect> selects = HTTPClient.getInstance().getSelects(tmpPage);
		if ((selects != null) && (!selects.isEmpty())) {
			Iterator<HtmlSelect> it = selects.iterator();
			boolean found = false;
			while (it.hasNext()) {
				final HtmlSelect select = it.next();
				if (!select.isDisplayed())
					continue;
				final String selectName = select.getNameAttribute();

				if ((selectName != null) && (selectName.equals(groupId))) {
					List<HtmlOption> options = select.getOptions();
					if (options != null) {
						for (final HtmlOption option : options) {
							System.out.println("option : value = " + option.getValueAttribute() + " displayed = " + option.isDisplayed());

							if (!option.isDisplayed())
								continue;
							if (option.getValueAttribute().equals(valueId)) {
								return (HtmlPage) select.setSelectedAttribute(option, true);
							}
						}
					}

					// return (HtmlPage) select.setSelectedAttribute(valueId,
					// true);
				}

			}
			if (!found)
				throw new Exception("No visible Select Option found with ID " + valueId);
		}
		return tmpPage;
	}

	protected String getSCRadio(final HtmlPage tmpPage, final String groupId) throws Exception {
		final List<?> singleInputs = HTTPClient.getInstance().getRadios(tmpPage);
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

			if (!groups.containsKey(groupId)) {
				throw new Exception("No Group found with ID " + groupId);
			}

			final HashSet<HtmlRadioButtonInput> items = groups.get(groupId);
			if ((items == null) || (items.isEmpty())) {
				throw new Exception("No Items found for Group " + groupId);
			}

			for (final HtmlRadioButtonInput select : items) {
				if (select != null) {
					if (select.isDisplayed()) {
						if (select.isChecked())
							return select.getValueAttribute();
					}
				}
			}

		}
		return null;
	}
	
	protected HtmlPage writeToOpen(final HtmlPage tmpPage, final String fieldId, final String valueId) throws Exception {
		return this.writeToOpen(tmpPage, fieldId, valueId, false);
	}

	protected HtmlPage writeToOpen(final HtmlPage tmpPage, final String fieldId, final String valueId,final boolean enter) throws Exception {
		final List<?> fields = HTTPClient.getInstance().getFields(tmpPage);
		if ((fields != null) && (!fields.isEmpty())) {
			boolean found = false;

			Iterator<?> it = fields.iterator();
			while (it.hasNext()) {
				final Object tmp = it.next();
				if ((tmp != null) && (tmp instanceof HtmlTextInput)) {
					final HtmlTextInput input = (HtmlTextInput) tmp;
					final String name = input.getAttribute("name");

					if (input.isDisplayed() && (name.equals(fieldId))) {
						input.focus();
						input.fireEvent("keydown");
						HtmlPage insertedPage =  (HtmlPage) input.setValueAttribute(valueId);
						if(!enter)return (HtmlPage)insertedPage;
						else {
							return (HtmlPage)input.type('\n');
						}
						
						// found = true;
					}
				}
			}
			if (!found)
				throw new Exception("No visible Item found with ID " + fieldId);

		}
		return null;
	}
	
	protected HtmlPage selectMultipleCheckBox(final HtmlPage tmpPage, final String groupId, final String valueId) throws Exception {
		final List<?> mupltipleCheck = HTTPClient.getInstance().getCheckboxes(tmpPage);
		if ((mupltipleCheck != null) && (!mupltipleCheck.isEmpty())) {
			Iterator<?> it = mupltipleCheck.iterator();
			final HashMap<String, HashSet<HtmlCheckBoxInput>> groups = new HashMap<String, HashSet<HtmlCheckBoxInput>>();
			while (it.hasNext()) {
				final Object tmp = it.next();
				if ((tmp != null) && (tmp instanceof HtmlCheckBoxInput)) {
					final HtmlCheckBoxInput input = (HtmlCheckBoxInput) tmp;
					final String name = input.getAttribute("name");

					HashSet<HtmlCheckBoxInput> alternatives = null;
					if (groups.containsKey(name))
						alternatives = groups.get(name);
					if (alternatives == null)
						alternatives = new HashSet<HtmlCheckBoxInput>();

					alternatives.add(input);

					groups.put(name, alternatives);
				}
			}

			if (!groups.containsKey(groupId)) {
				throw new Exception("No Group found with ID " + groupId);
			}

			final HashSet<HtmlCheckBoxInput> items = groups.get(groupId);
			if ((items == null) || (items.isEmpty())) {
				throw new Exception("No Items found for Group " + groupId);
			}

			boolean found = false;

			for (final HtmlCheckBoxInput select : items) {
				if (select != null) {
					if (select.isDisplayed()) {
						try {
							final String value = select.getValueAttribute();
							if ((value != null) && (value.equals(valueId))) {
								return select.click();
							}
						} catch (final IOException e) {
							e.printStackTrace();
						}
					}
				}
			}
			if (!found)
				throw new Exception("No visible Item found with ID " + valueId);

		}
		return tmpPage;
	}
	
	protected String getMultipleCheckbox(final HtmlPage tmpPage, final String groupId) throws Exception {
		final List<?> multipleCheckbox = HTTPClient.getInstance().getCheckboxes(tmpPage);
		if ((multipleCheckbox != null) && (!multipleCheckbox.isEmpty())) {
			Iterator<?> it = multipleCheckbox.iterator();
			final HashMap<String, HashSet<HtmlCheckBoxInput>> groups = new HashMap<String, HashSet<HtmlCheckBoxInput>>();
			while (it.hasNext()) {
				final Object tmp = it.next();
				if ((tmp != null) && (tmp instanceof HtmlCheckBoxInput)) {
					final HtmlCheckBoxInput input = (HtmlCheckBoxInput) tmp;
					final String name = input.getAttribute("name");

					HashSet<HtmlCheckBoxInput> alternatives = null;
					if (groups.containsKey(name))
						alternatives = groups.get(name);
					if (alternatives == null)
						alternatives = new HashSet<HtmlCheckBoxInput>();

					alternatives.add(input);

					groups.put(name, alternatives);
				}
			}

			if (!groups.containsKey(groupId)) {
				throw new Exception("No Group found with ID " + groupId);
			}

			final HashSet<HtmlCheckBoxInput> items = groups.get(groupId);
			if ((items == null) || (items.isEmpty())) {
				throw new Exception("No Items found for Group " + groupId);
			}

			for (final HtmlCheckBoxInput select : items) {
				if (select != null) {
					if (select.isDisplayed()) {
						if (select.isChecked())
							return select.getValueAttribute();
					}
				}
			}

		}
		return null;
	}

}

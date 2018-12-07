package eu.dzhw.zofar.management.comm.network.webservices;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gargoylesoftware.htmlunit.Page;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import eu.dzhw.zofar.management.comm.network.http.HTTPClient;

public class GenderService {

	/** The Constant INSTANCE. */
	private static final GenderService INSTANCE = new GenderService();

	/** The Constant LOGGER. */
	private static final Logger LOGGER = LoggerFactory.getLogger(GenderService.class);
	
	private static final String USER_KEY = "xxxx";

	/**
	 * Instantiates a new HTTP client.
	 */
	private GenderService() {
		super();
	}

	public static GenderService getInstance() {
		return INSTANCE;
	}
	
	public String getGenderBySurname(final String surname, final String country){
		try {
			Page answer = HTTPClient.getInstance().loadPage("https://gender-api.com/get?key="+USER_KEY+"&name="+surname+"&country="+country);
			if(answer != null){
				final String content = answer.getWebResponse().getContentAsString();
			    Gson gson = new Gson();
			    JsonObject json = gson.fromJson(content, JsonObject.class);
			    String gender = json.get("gender").getAsString(); 
			    return gender;
			}
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return null;
	}

}

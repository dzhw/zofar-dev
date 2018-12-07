package eu.dzhw.zofar.management.dev.automation.mdm.presentation.json;

import java.util.HashMap;
import java.util.Map;

import javax.json.JsonObject;

import eu.dzhw.zofar.management.comm.json.JSONClient;
import eu.dzhw.zofar.management.dev.automation.mdm.presentation.AbstractFormat;

public class JSONFormatter extends AbstractFormat {

	/** The instance. */
	private static JSONFormatter INSTANCE;

	/**
	 * Instantiates a new txt format.
	 */
	private JSONFormatter() {
		super();
	}

	/**
	 * Gets the single instance of TxtFormat.
	 * 
	 * @return single instance of TxtFormat
	 */
	public static AbstractFormat getInstance() {
		if (INSTANCE == null)
			INSTANCE = new JSONFormatter();
		return INSTANCE;
	}

	@Override
	public Object formatJSON(Map<String, JsonObject> data) {
		if(data == null)return null;
		
		Map<String, String> back = new HashMap<String,String>();
		for (Map.Entry<String, JsonObject> item : data.entrySet()) {
			final JsonObject json = item.getValue();
			final String result = JSONClient.getInstance().writeToString(json);
			if(result == null) {
				System.err.println("null at JSON for "+item.getKey());
			}
			back.put(item.getKey(), result);
		}
		return back;
	}

	@Override
	public Object formatCSV(Map<String, Map<String, Object>> data) {
		return null;
	}
}

package presentation.mdm.format.json;

import java.util.HashMap;
import java.util.Map;

import javax.json.JsonObject;

import eu.dzhw.zofar.management.comm.json.JSONClient;
import presentation.mdm.format.AbstractFormat;

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
			back.put(item.getKey(), JSONClient.getInstance().writeToString(json));
		}
		return back;
	}

	@Override
	public Object formatCSV(Map<String, Map<String, Object>> data) {
		return null;
	}
}

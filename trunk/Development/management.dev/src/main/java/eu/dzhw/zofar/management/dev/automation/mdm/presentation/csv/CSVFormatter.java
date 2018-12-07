package eu.dzhw.zofar.management.dev.automation.mdm.presentation.csv;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.json.JsonObject;

import eu.dzhw.zofar.management.dev.automation.mdm.presentation.AbstractFormat;
import eu.dzhw.zofar.management.utils.files.CSVClient;

public class CSVFormatter extends AbstractFormat {

	/** The instance. */
	private static CSVFormatter INSTANCE;

	/**
	 * Instantiates a new txt format.
	 */
	private CSVFormatter() {
		super();
	}

	/**
	 * Gets the single instance of TxtFormat.
	 * 
	 * @return single instance of TxtFormat
	 */
	public static AbstractFormat getInstance() {
		if (INSTANCE == null)
			INSTANCE = new CSVFormatter();
		return INSTANCE;
	}

	@Override
	public Object formatJSON(Map<String, JsonObject> data) {
		return null;
	}

	@Override
	public String formatCSV(Map<String, Map<String, Object>> data) throws Exception {
		final List<String> headers = new ArrayList<String>();
		headers.add("variablen_id");
		headers.add("instrumentId");
		headers.add("question_number");
		headers.add("relatedQuestionStrings.de");
		headers.add("relatedQuestionStrings.en");
		
		final List<Map<String, String>> dataList = new ArrayList<Map<String, String>>();

		for(Map.Entry<String, Map<String, Object>> entry : data.entrySet()){
			final Map<String, Object> valueMap = entry.getValue();
			final Map<String, String> backMap = new LinkedHashMap<String,String>();
			for(Map.Entry<String, Object> valueEntry : valueMap.entrySet()){
				backMap.put(valueEntry.getKey(), valueEntry.getValue()+"");
			}
			dataList.add(backMap);
		}
		
		return CSVClient.getInstance().saveCSVAsString(headers, dataList, ';', '"');
	}



}

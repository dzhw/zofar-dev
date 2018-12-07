package eu.dzhw.zofar.management.utils.string.main;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import eu.dzhw.zofar.management.utils.files.CSVClient;

public class CSVCleaner {
    
    public static void main(String[] args) {
	CSVClient csvClient = CSVClient.getInstance();
	final File csvDir = new File("xxxx");
	final File csvFile = new File(csvDir, "xxxx.csv");
	final File cleanedCsvFile = new File(csvDir, csvFile.getName().replaceAll(".csv", "cleaned.csv"));
	try {
	    final ArrayList<String> headers = csvClient.getCSVHeaders(csvFile, ',', '\'');
	    final List<Map<String, String>> data = csvClient.loadCSV(csvFile, headers, true, ',', '\'');
	    final List<Map<String, String>> cleanedData = new ArrayList<Map<String, String>>();
	    for (final Map<String, String> row : data) {
		Map<String, String> cleanedRow = new HashMap<String,String>();
		for (final String header : headers) {
		    String value = row.get(header);
		    if (header.equals("xxx")) {
			System.out.println(header + " = " + value);
			value = value.replaceAll(" ", "");
			value = value.replaceAll("\\.{2,}", ".");
			value = value.trim();
		    }
		    cleanedRow.put(header, value);
		}
		cleanedData.add(cleanedRow);
	    }
	    csvClient.saveCSV(cleanedCsvFile, headers, cleanedData, ';', '\'');
	} catch (Exception e) {
	    e.printStackTrace();
	}
	
    }
    
}

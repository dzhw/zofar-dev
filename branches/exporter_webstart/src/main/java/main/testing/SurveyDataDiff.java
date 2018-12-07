package main.testing;

import java.io.File;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import eu.dzhw.zofar.management.utils.files.CSVClient;
import eu.dzhw.zofar.management.utils.files.FileClient;

public class SurveyDataDiff {

	public static void main(String[] args) throws IOException {
		CSVClient csvClient = CSVClient.getInstance();
		FileClient fileClient = FileClient.getInstance();
		
		final File csv1 = new File("xxx/data.csv");
		final File csv2 = new File("xxx/dataAlt.csv");
		final File diff = fileClient.createOrGetFile("data", ".diff", new File("xxx"));
		
		fileClient.writeToFile(diff, "", false);
		
		final List<String> headerCSV1 = csvClient.getCSVHeaders(csv1,',','"');
		System.out.println("header1 ("+headerCSV1.size()+") : "+headerCSV1);
		
		final List<Map<String, String>> data1 =  csvClient.loadCSV(csv1, headerCSV1, true,',','"');
		Map<String,Map<String, String>> data1Map = new LinkedHashMap<String,Map<String, String>>();
		for(final Map<String, String> row : data1){
			data1Map.put(row.get("id"), row);
		}
		
		final List<String> headerCSV2 = csvClient.getCSVHeaders(csv2,',','"');
		System.out.println("header2 ("+headerCSV2.size()+") : "+headerCSV2);

		final List<Map<String, String>> data2 =  csvClient.loadCSV(csv2, headerCSV2, true,',','"');
		Map<String,Map<String, String>> data2Map = new LinkedHashMap<String,Map<String, String>>();
		for(final Map<String, String> row : data2){
			data2Map.put(row.get("id"), row);
		}
		
		for(Map.Entry<String,Map<String, String>> item1 : data1Map.entrySet()){
			final String id = item1.getKey();
			final Map<String, String> item1Data = item1.getValue();
			final Map<String, String> item2Data = data2Map.get(id);
			
			for(final String header : headerCSV1){
				String value1 = null;
				if((item1Data != null)&&(item1Data.containsKey(header)))value1 = item1Data.get(header);
				
				String value2 = null;
				if((item2Data != null)&&(item1Data.containsKey(header)))value2 = item2Data.get(header);
				
				if(((value1 != null) ^ (value2 != null))||(!value1.equals(value2))){
//					System.out.println("ID : "+id+"\tVariable : "+header+"\tValues : "+value1+" != "+value2);
					fileClient.writeToFile(diff, "ID : "+id+"\tVariable : "+header+"\tValues : "+value1+" != "+value2+"\n", true);
				}
			}
			
			fileClient.writeToFile(diff,"\n",true);
		}
	}

}

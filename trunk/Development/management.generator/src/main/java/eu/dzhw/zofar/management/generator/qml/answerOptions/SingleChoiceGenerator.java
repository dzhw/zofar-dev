package eu.dzhw.zofar.management.generator.qml.answerOptions;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import eu.dzhw.zofar.management.utils.files.CSVClient;

public class SingleChoiceGenerator {

	private static SingleChoiceGenerator INSTANCE;

	private SingleChoiceGenerator() {
		super();
	}

	public static SingleChoiceGenerator getInstance() {
		if (INSTANCE == null)
			INSTANCE = new SingleChoiceGenerator();
		return INSTANCE;
	}
	
	public List<String> generate(final File csv){
		return generate(csv,1);
	}
	
	public List<String> generate(final File csv,final int startIndex){
		if(csv == null)return null;
		if(!csv.canRead())return null;
		try {
			List<String> headers = new ArrayList<String>();
			headers.add("Value");
			headers.add("Label");
			
			List<Map<String,String>> list = CSVClient.getInstance().loadCSV(csv,headers,false);
			if(list == null)return null;
			if(list.isEmpty())return null;
			
			int lft = startIndex;
			final List<String> back = new ArrayList<String>();
			for(Map<String,String> entry : list){
				back.add("<zofar:answerOption uid=\"ao"+lft+"\" value=\""+entry.get("Value")+"\" label=\""+entry.get("Label")+"\"></zofar:answerOption>");
				lft++;
			}
			return back;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

}

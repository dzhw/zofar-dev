package main;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import model.HeaderEntry;
import model.ValueEntry;
import service.counting.CountingService;
import service.data.DataService;
import utils.ConfigurationUtils;

public class Codebook {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		final Properties system = ConfigurationUtils.getInstance()
				.getConfiguration("System.properties");

		final List<String> variables = new ArrayList<String>();
		
//		variables.clear();
		variables.add("merkmal1");
		variables.add("merkmal2");
		variables.add("merkmal3");
		variables.add("merkmal4");
		variables.add("merkmal5");

		DataService dataService = DataService.getInstance();
		CountingService service = CountingService.getInstance();
		Map<HeaderEntry, Map<ValueEntry, Integer>> codebookMatrix = service
				.buildUpDataMatrix(system.getProperty("qml.location"),null,dataService.getParticipants(dataService.getConfiguration()));
		System.out.println("codebookmatrix : "+codebookMatrix);
		if(codebookMatrix != null){
			final Set<HeaderEntry> varnames = codebookMatrix.keySet();
			final Iterator<HeaderEntry> varnameIt = varnames.iterator();
			while(varnameIt.hasNext()){
				final HeaderEntry varname = varnameIt.next();
				final Set<ValueEntry> valueCounts = codebookMatrix.get(varname).keySet();
				final Iterator<ValueEntry> valueIt = valueCounts.iterator();
				System.out.println("Variable : "+varname.getVariable()+" Text : "+varname.getText());
				while(valueIt.hasNext()){
					final ValueEntry valueItem = valueIt.next();
					final String value = valueItem.getValue();
					String label = valueItem.getLabel().replaceAll("\n", " ");
					label = label.replaceAll("\r", "");
					label = label.replaceAll("\t", " ");
					label = label.replaceAll(" {2,}", " ");
					final Integer count = codebookMatrix.get(varname).get(valueItem);
					System.out.println("\tValue : ("+value+") "+label+" Count : "+count);
				}
				System.out.println();
			}
		}

	}

}

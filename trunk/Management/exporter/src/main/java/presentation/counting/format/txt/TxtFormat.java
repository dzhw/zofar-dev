package presentation.counting.format.txt;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import model.HeaderEntry;
import model.ValueEntry;
import presentation.counting.format.AbstractFormat;
import utils.StringUtils;

public class TxtFormat extends AbstractFormat {
	
	private static TxtFormat INSTANCE;

	private TxtFormat() {
		super();
	}
	
	public static AbstractFormat getInstance(){
		if(INSTANCE == null)INSTANCE = new TxtFormat();
		return INSTANCE;
	}
	
	private String buildExport(Map<HeaderEntry, Map<ValueEntry, Integer>> data){
		final StringBuffer buffer = new StringBuffer();

		final Set<HeaderEntry> varnames = data.keySet();
		final Iterator<HeaderEntry> varnameIt = varnames.iterator();
		
		while (varnameIt.hasNext()) {
			final HeaderEntry variable = varnameIt.next();
			buffer.append("["+variable.getVariable()+"]");
			
			Set<String> header = variable.getText();
			for(String item : header){
				buffer.append(" "+item+"");
			}
			buffer.append("\n");
			final Map<ValueEntry, Integer> content = data.get(variable);
			for(Map.Entry<ValueEntry, Integer> item: content.entrySet()){
				final ValueEntry entry = item.getKey();
				buffer.append("\t("+entry.getValue()+") "+StringUtils.getInstance().cleanedString(entry.getLabel())+" ==> "+item.getValue()+"\n");
			}
			buffer.append("------------------------------------\n");
		}
		return buffer.toString();
	}

	@Override
	public Object format(Map<HeaderEntry, Map<ValueEntry, Integer>> data) {
		if (data == null)
			return null;
		if (data.isEmpty())
			return null;

		return buildExport(data);
	}

}

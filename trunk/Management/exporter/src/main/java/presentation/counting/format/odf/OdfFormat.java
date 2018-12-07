package presentation.counting.format.odf;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import model.HeaderEntry;
import model.ValueEntry;
import presentation.counting.format.AbstractFormat;
import utils.StringUtils;
import eu.dzhw.zofar.management.utils.files.FileClient;
import eu.dzhw.zofar.management.utils.odf.components.WriterDocument;

public class OdfFormat extends AbstractFormat {
	
	private static OdfFormat INSTANCE;

	
	private OdfFormat() {
		super();
	}
	
	public static AbstractFormat getInstance(){
		if(INSTANCE == null)INSTANCE = new OdfFormat();
		return INSTANCE;
	}

	private File buildExport(final Map<HeaderEntry, Map<ValueEntry, Integer>> data,final String path) throws Exception{
		final WriterDocument doc = new WriterDocument("MasterTemplate.odt");
		final Set<HeaderEntry> varnames = data.keySet();
		final Iterator<HeaderEntry> varnameIt = varnames.iterator();
		
		while (varnameIt.hasNext()) {
			final HeaderEntry variable = varnameIt.next();
			doc.addTitle(variable.getVariable());
			
			Set<String> header = variable.getText();
			for(String item : header){
				doc.addText(item);
			}
			final Map<ValueEntry, Integer> content = data.get(variable);
			
			final ArrayList<String> rows = new ArrayList<String>();
			final Map<String, Map<String, String>> tableData = new HashMap<String,Map<String,String>>();
			
			boolean openFlag= false;
			for(Map.Entry<ValueEntry, Integer> item: content.entrySet()){
				final ValueEntry entry = item.getKey();
				final String value = entry.getValue();
				final String label = StringUtils.getInstance().cleanedString(entry.getLabel());
				final String count = item.getValue()+"";

				final HashMap<String,String> rowData = new HashMap<String,String>();
				if(value.equals("OPEN")){
					final String rowId = UUID.randomUUID().toString();
					rows.add(rowId);
					//rowData.put("value", "");
					rowData.put("Aussage", label);
//					rowData.put("count", count);
					tableData.put(rowId, rowData);
					openFlag = true;
				}
				else{
					rows.add(value);
					rowData.put("Wert", value);
					rowData.put("Label", label);
					rowData.put("Anzahl", count);
					tableData.put(value, rowData);
				}
			}
			
			final ArrayList<String> columns = new ArrayList<String>();
			if(openFlag){
//				columns.add("value");
				columns.add("Aussage");
//				columns.add("count");
			}
			else{
				columns.add("Wert");
				columns.add("Label");
				columns.add("Anzahl");
			}
			
			String tableTitle = "";
			if(openFlag){
				tableTitle = "Freie Aussagen";
			}
			else{
				tableTitle = "Auszählung";
			}

			
			doc.addTable(tableTitle, rows, columns, tableData,false);
			doc.addPageBreak();
		}
		doc.save(path);
		return new File(path);
	}

	@Override
	public Object format(Map<HeaderEntry, Map<ValueEntry, Integer>> data) {
		if (data == null)
			return null;
		if (data.isEmpty())
			return null;

		try {
			return buildExport(data,FileClient.getInstance().createTempFile("counting", "odf").getAbsolutePath());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}

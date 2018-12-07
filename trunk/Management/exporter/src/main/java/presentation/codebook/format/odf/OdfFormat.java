package presentation.codebook.format.odf;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import model.HeaderEntry;
import model.ValueEntry;
import presentation.codebook.format.AbstractFormat;
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

	private File buildExport(final Map<HeaderEntry, Map<String, ValueEntry>> data,final String path, Map<String, String> mapping) throws Exception{
		final WriterDocument doc = new WriterDocument("MasterTemplate.odt");
		final Set<HeaderEntry> headers = data.keySet();
		final Iterator<HeaderEntry> headerIt = headers.iterator();
		
		while (headerIt.hasNext()) {
			final HeaderEntry header = headerIt.next();
			final Map<String, ValueEntry> options = data.get(header);
			
			doc.addTitle(header.getVariable());
			
			Set<String> headerSet = header.getText();
			for(String item : headerSet){
				doc.addText(item);
			}

			final List<String[]> tableData = new ArrayList<String[]>();
			
			boolean openFlag= false;
			for(Map.Entry<String, ValueEntry> item: options.entrySet()){
				final String uid = item.getKey();
				final ValueEntry valueEntry = item.getValue();
				
				final String label = StringUtils.getInstance().cleanedString(valueEntry.getLabel());
				String value = valueEntry.getValue()+"";

				if(value.equals("OPEN")){
					openFlag= true;
				}
				else{
					final String[] rowData = new String[3];
					rowData[0] = uid;
					if(mapping.containsKey(value))value = mapping.get(value);
					rowData[1] = value;
					rowData[2] = label;
					tableData.add(rowData);
				}
			}

			if(openFlag){
				doc.addText("Offene Angabe");
			}
			else{
				final String[] columns = new String[3];
				columns[0] = "ID";
				columns[1] = "Wert";
				columns[2] = "Label";
				doc.addTable("Antwort Optionen", columns, tableData);
			}
			doc.addPageBreak();
		}
		doc.save(path);
		return new File(path);
	}

	@Override
	public Object format(Map<HeaderEntry, Map<String, ValueEntry>> data, Map<String, String> mapping) {
		if (data == null)
			return null;
		if (data.isEmpty())
			return null;

		try {
			return buildExport(data,FileClient.getInstance().createTempFile("codebook", "odf").getAbsolutePath(),mapping);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}

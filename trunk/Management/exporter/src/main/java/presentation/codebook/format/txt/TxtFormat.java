package presentation.codebook.format.txt;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.xmlbeans.XmlString;

import de.his.export.xml.export.CodebookItemSetType;
import de.his.export.xml.export.CodebookItemType;
import de.his.export.xml.export.CodebookOptionSetType;
import de.his.export.xml.export.CodebookOptionType;
import de.his.export.xml.export.CodebookType;
import de.his.export.xml.export.ExportDocument;
import de.his.export.xml.export.ExportType;
import de.his.export.xml.export.TextSetType;
import eu.dzhw.zofar.management.utils.files.FileClient;
import eu.dzhw.zofar.management.utils.odf.components.WriterDocument;
import model.HeaderEntry;
import model.ValueEntry;
import presentation.codebook.format.AbstractFormat;
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

	private String buildExport(final Map<HeaderEntry, Map<String, ValueEntry>> data,final String path, Map<String, String> mapping) throws Exception{
		final StringBuffer buffer = new StringBuffer();
		
		final Set<HeaderEntry> headers = data.keySet();
		final Iterator<HeaderEntry> headerIt = headers.iterator();
		
		while (headerIt.hasNext()) {
			final HeaderEntry header = headerIt.next();
			final Map<String, ValueEntry> options = data.get(header);
			
			buffer.append("["+header.getVariable()+"]");
			
			Set<String> headerTexts = header.getText();
			for(String item : headerTexts){
				buffer.append(" "+item+"");
			}

			for(Map.Entry<String, ValueEntry> item: options.entrySet()){
				final String uid = item.getKey();
				final ValueEntry valueEntry = item.getValue();
				
				final String label = StringUtils.getInstance().cleanedString(valueEntry.getLabel());
				String value = valueEntry.getValue()+"";
				if(value.equals("OPEN")){
					buffer.append("\t("+uid+") Offene Angabe\n");
					break;
				}
				else{
					if(mapping.containsKey(value))value = mapping.get(value);
					buffer.append("\t("+uid+") "+label+" ==> "+value+"\n");
				}
			}
		}
		return buffer.toString();
	}

	@Override
	public Object format(Map<HeaderEntry, Map<String, ValueEntry>> data, Map<String, String> mapping) {
		if (data == null)
			return null;
		if (data.isEmpty())
			return null;

		try {
			return buildExport(data,FileClient.getInstance().createTempFile("codebook", "txt").getAbsolutePath(),mapping);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}

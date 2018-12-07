package presentation.data.format.xml;

import java.util.Calendar;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.xmlbeans.XmlOptions;

import presentation.data.format.AbstractFormat;
import de.his.export.xml.export.DataType;
import de.his.export.xml.export.DatasetType;
import de.his.export.xml.export.ExportDocument;
import de.his.export.xml.export.ExportType;
import de.his.export.xml.export.ParticipantType;


public class XmlFormat extends AbstractFormat {
	
	private static XmlFormat INSTANCE;
	
	private XmlFormat() {
		super();
	}
	
	public static XmlFormat getInstance(){
		if(INSTANCE == null)INSTANCE = new XmlFormat();
		return INSTANCE;
	}
	
	private ExportDocument buildExport(Set<ParticipantType> data, boolean ignoreFirst){
		final Iterator<ParticipantType> rowIt = data.iterator();
		final ExportDocument exportDocument = ExportDocument.Factory.newInstance();
		final ExportType export = exportDocument.addNewExport();
		export.setDate(Calendar.getInstance());
		while (rowIt.hasNext()) {
			final ParticipantType entry = rowIt.next();
			final ParticipantType participant = export.addNewParticipant();
			participant.setToken(entry.getToken());
			participant.setId(entry.getId());
			final DatasetType dataset = participant.addNewDataset();
			for(final DataType inputDataEntry: entry.getDataset().getDataArray()){
				final DataType dataEntry = dataset.addNewData();
				dataEntry.setVariable(inputDataEntry.getVariable());
				dataEntry.setStringValue(inputDataEntry.getStringValue());
			}
//			final Iterator<String> colIt = row.keySet().iterator();
//			while (colIt.hasNext()) {
//				final String col = colIt.next();
//				if(col.equals("token"))continue;
//				final Object value = row.get(col);
//				final DataType dataEntry = dataset.addNewData();
//				dataEntry.setVariable(col);
//				dataEntry.setStringValue((String)value);
//			}
		}
		return exportDocument;
	}

	public Object format(Set<ParticipantType> data) {
		return format(data,false);
	}
	
	public Object format(Set<ParticipantType> data, boolean ignoreFirst) {
		final ExportDocument export = buildExport(data,ignoreFirst);
		 XmlOptions opts = new XmlOptions();
		 opts.setSavePrettyPrint();
		 opts.setSavePrettyPrintIndent(4);
		return export.xmlText(opts);
	}
	
	@Override
	public Object format(Set<ParticipantType> data,final char fieldSeparator,final char rowSeparator) {
		return format(data,false);
	}
	
	@Override
	public Object format(Set<ParticipantType> data,final char fieldSeparator,final char rowSeparator, final Map<String,String> mapping) {
		return format(data,false);
	}

	@Override
	public Object format(Set<ParticipantType> data, char fieldSeparator,
			char rowSeparator, Map<String, String> mapping, boolean ignoreFirst) {
		return format(data,ignoreFirst);
	}
}

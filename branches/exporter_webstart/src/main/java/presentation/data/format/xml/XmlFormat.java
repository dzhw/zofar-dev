package presentation.data.format.xml;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlOptions;

import presentation.data.format.AbstractFormat;
import de.his.export.xml.export.DataType;
import de.his.export.xml.export.DatasetType;
import de.his.export.xml.export.ExportDocument;
import de.his.export.xml.export.ExportType;
import de.his.export.xml.export.ParticipantType;

// TODO: Auto-generated Javadoc
/**
 * The Class XmlFormat.
 */
public class XmlFormat extends AbstractFormat {

	/** The instance. */
	private static XmlFormat INSTANCE;

	/**
	 * Instantiates a new xml format.
	 */
	private XmlFormat() {
		super();
	}

	/**
	 * Gets the single instance of XmlFormat.
	 * 
	 * @return single instance of XmlFormat
	 */
	public static XmlFormat getInstance() {
		if (INSTANCE == null)
			INSTANCE = new XmlFormat();
		return INSTANCE;
	}

	/**
	 * Builds the export.
	 * 
	 * @param exportDocument
	 *            the export document
	 * @param data
	 *            the data
	 * @param ignoreFirst
	 *            the ignore first
	 * @return the export document
	 */
	private ExportDocument buildExport(final ExportDocument exportDocument, final Set<ParticipantType> data, final boolean ignoreFirst) {
		final Iterator<ParticipantType> rowIt = data.iterator();
		// final ExportDocument exportDocument =
		// ExportDocument.Factory.newInstance();
		final ExportType export = exportDocument.addNewExport();
		export.setDate(Calendar.getInstance());
		while (rowIt.hasNext()) {
			final ParticipantType entry = rowIt.next();
			final ParticipantType participant = export.addNewParticipant();
			participant.setToken(entry.getToken());
			participant.setId(entry.getId());
			final DatasetType dataset = participant.addNewDataset();
			for (final DataType inputDataEntry : entry.getDataset().getDataArray()) {
				final DataType dataEntry = dataset.addNewData();
				dataEntry.setVariable(inputDataEntry.getVariable());
				dataEntry.setStringValue(inputDataEntry.getStringValue());
			}
			// final Iterator<String> colIt = row.keySet().iterator();
			// while (colIt.hasNext()) {
			// final String col = colIt.next();
			// if(col.equals("token"))continue;
			// final Object value = row.get(col);
			// final DataType dataEntry = dataset.addNewData();
			// dataEntry.setVariable(col);
			// dataEntry.setStringValue((String)value);
			// }
		}
		return exportDocument;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see presentation.data.format.AbstractFormat#format(java.util.Set)
	 */
	@Override
	public Object format(final Set<ParticipantType> data) {
		return this.format(null, data, false,true);
	}

	/**
	 * Format.
	 * 
	 * @param prebuild
	 *            the prebuild
	 * @param data
	 *            the data
	 * @param ignoreFirst
	 *            the ignore first
	 * @return the object
	 */
	public Object format(final Object prebuild, final Set<ParticipantType> data, final boolean ignoreFirst, final boolean limitStrings) {
		ExportDocument prebuildObj = null;
		if ((prebuild != null) && (!(((String) prebuild).trim()).equals("")))
			try {
				prebuildObj = ExportDocument.Factory.parse((String) prebuild);
			} catch (final XmlException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		else
			prebuildObj = ExportDocument.Factory.newInstance();
		final ExportDocument export = this.buildExport(prebuildObj, data, ignoreFirst);
		final XmlOptions opts = new XmlOptions();
		opts.setSavePrettyPrint();
		opts.setSavePrettyPrintIndent(4);
		return export.xmlText(opts);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see presentation.data.format.AbstractFormat#format(java.util.Set, char,
	 * char)
	 */
	@Override
	public Object format(final Set<ParticipantType> data, final char fieldSeparator, final char rowSeparator) {
		return this.format(null, data, false,true);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see presentation.data.format.AbstractFormat#format(java.util.Set, char,
	 * char, java.util.Map)
	 */
	@Override
	public Object format(final Set<ParticipantType> data, final char fieldSeparator, final char rowSeparator, final Map<String, String> mapping) {
		return this.format(null, data, false,true);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see presentation.data.format.AbstractFormat#format(java.util.Set, char,
	 * char, java.util.Map, boolean)
	 */
	@Override
	public Object format(final Set<ParticipantType> data, final char fieldSeparator, final char rowSeparator, final Map<String, String> mapping, final boolean ignoreFirst, final boolean limitStrings) {
		return this.format(null, data, ignoreFirst,limitStrings);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see presentation.data.format.AbstractFormat#format(java.lang.Object,
	 * java.util.Set, char, char, java.util.Map, boolean)
	 */
	@Override
	public Object format(Object prebuild, final Set<ParticipantType> data, final char fieldSeparator, final char rowSeparator, final Map<String, String> mapping, final boolean ignoreFirst, final boolean limitStrings) {
			return this.format(prebuild, data, fieldSeparator, rowSeparator, mapping, ignoreFirst, limitStrings, null,null);
	}
	
	@Override
	public Object format(final Object prebuild, final Set<ParticipantType> data, final char fieldSeparator, final char rowSeparator, final Map<String, String> mapping, final boolean ignoreFirst, final boolean limitStrings,final Map<File,Set<String>> fileMapping,final Map<String,Set<File>> reverseFileMapping) {
		return this.format(prebuild, data, ignoreFirst,limitStrings);
	}
	
	@Override
	public Object formatAsStream(File history,File dataFile, Set<ParticipantType> data, char fieldSeparator, char rowSeparator, Map<String, String> mapping, boolean ignoreFirst, final boolean saveHistory, final boolean limitStrings) throws IOException {
		return this.formatAsStream(history, dataFile, data, fieldSeparator, rowSeparator, mapping, ignoreFirst, saveHistory, limitStrings,null);
	}
	
	@Override
	public Object formatAsStream(File history,File dataFile, Set<ParticipantType> data, char fieldSeparator, char rowSeparator, Map<String, String> mapping, boolean ignoreFirst, final boolean saveHistory, final boolean limitStrings,final Map<File,Set<String>> fileMapping) throws IOException{
		// TODO Auto-generated method stub
		return null;
	}
}

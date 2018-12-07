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
import eu.dzhw.zofar.management.utils.odf.components.WriterDocument;
import eu.dzhw.zofar.management.utils.string.ReplaceClient;

// TODO: Auto-generated Javadoc
/**
 * The Class OdfFormat.
 */
public class OdfFormat extends AbstractFormat {

	/** The instance. */
	private static OdfFormat INSTANCE;

	/**
	 * Instantiates a new odf format.
	 */
	private OdfFormat() {
		super();
	}

	/**
	 * Gets the single instance of OdfFormat.
	 * 
	 * @return single instance of OdfFormat
	 */
	public static AbstractFormat getInstance() {
		if (INSTANCE == null)
			INSTANCE = new OdfFormat();
		return INSTANCE;
	}

	/**
	 * Builds the export.
	 * 
	 * @param doc
	 *            the doc
	 * @param prebuild
	 *            the prebuild
	 * @param data
	 *            the data
	 * @return the file
	 * @throws Exception
	 *             the exception
	 */
	private File buildExport(final WriterDocument doc, final File prebuild, final Map<HeaderEntry, Map<ValueEntry, Integer>> data) throws Exception {
		// final WriterDocument doc = new WriterDocument("MasterTemplate.odt");
		final Set<HeaderEntry> varnames = data.keySet();
		final Iterator<HeaderEntry> varnameIt = varnames.iterator();

		while (varnameIt.hasNext()) {
			final HeaderEntry variable = varnameIt.next();
			doc.addHeading(variable.getVariable());

			final Set<String> header = variable.getText();
			for (final String item : header) {
				doc.addText(item);
			}
			final Map<ValueEntry, Integer> content = data.get(variable);

			final ArrayList<String> rows = new ArrayList<String>();
			final Map<String, Map<String, String>> tableData = new HashMap<String, Map<String, String>>();

			boolean openFlag = false;
			for (final Map.Entry<ValueEntry, Integer> item : content.entrySet()) {
				final ValueEntry entry = item.getKey();
				final String value = entry.getValue();
				final String label = ReplaceClient.getInstance().cleanedString(entry.getLabel());
				final String count = item.getValue() + "";

				final HashMap<String, String> rowData = new HashMap<String, String>();
				if (value.equals("OPEN")) {
					final String rowId = UUID.randomUUID().toString();
					rows.add(rowId);
					// rowData.put("value", "");
					rowData.put("Aussage", label);
					// rowData.put("count", count);
					tableData.put(rowId, rowData);
					openFlag = true;
				} else {
					rows.add(value);
					rowData.put("Wert", value);
					rowData.put("Label", label);
					rowData.put("Anzahl", count);
					tableData.put(value, rowData);
				}
			}

			final ArrayList<String> columns = new ArrayList<String>();
			if (openFlag) {
				// columns.add("value");
				columns.add("Aussage");
				// columns.add("count");
			} else {
				columns.add("Wert");
				columns.add("Label");
				columns.add("Anzahl");
			}

			String tableTitle = "";
			if (openFlag) {
				tableTitle = "Freie Aussagen";
			} else {
				tableTitle = "Auszählung";
			}

			doc.addTable(tableTitle, rows, columns, tableData, false);
			doc.addPageBreak();
		}
		doc.save(prebuild.getAbsolutePath());
		return new File(prebuild.getAbsolutePath());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see presentation.counting.format.AbstractFormat#format(java.util.Map)
	 */
	@Override
	public Object format(final Map<HeaderEntry, Map<ValueEntry, Integer>> data) {
		if (data == null)
			return null;
		if (data.isEmpty())
			return null;
		try {
			return this.buildExport(new WriterDocument("MasterTemplate.odt"), File.createTempFile("counting", ".odf"), data);
		} catch (final Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	// @Override
	// public Object format(Object prebuild,Map<HeaderEntry, Map<ValueEntry,
	// Integer>> data) {
	// if (data == null)
	// return null;
	// if (data.isEmpty())
	// return null;
	//
	// try {
	// if(prebuild == null){
	// return buildExport(new
	// WriterDocument("MasterTemplate.odt"),File.createTempFile("counting",
	// ".odf"),data);
	// }
	// else return buildExport(new
	// WriterDocument(((File)prebuild).getAbsolutePath()),(File)prebuild,data);
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// return null;
	// }

}

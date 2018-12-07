package presentation.codebook.format.xml;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.xmlbeans.XmlOptions;
import org.apache.xmlbeans.XmlString;
import org.w3c.dom.Node;

import de.his.export.xml.export.CodebookItemSetType;
import de.his.export.xml.export.CodebookItemType;
import de.his.export.xml.export.CodebookOptionSetType;
import de.his.export.xml.export.CodebookOptionType;
import de.his.export.xml.export.CodebookType;
import de.his.export.xml.export.ExportDocument;
import de.his.export.xml.export.ExportType;
import de.his.export.xml.export.TextSetType;
import eu.dzhw.zofar.management.utils.string.ReplaceClient;
import model.HeaderEntry;
import model.ValueEntry;
import presentation.codebook.format.AbstractFormat;

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
	public static AbstractFormat getInstance() {
		if (INSTANCE == null)
			INSTANCE = new XmlFormat();
		return INSTANCE;
	}

	/**
	 * Builds the export.
	 * 
	 * @param data
	 *            the data
	 * @param path
	 *            the path
	 * @param mapping
	 *            the mapping
	 * @return the file
	 * @throws Exception
	 *             the exception
	 */
	private File buildExport(final Map<HeaderEntry, Map<String, ValueEntry>> data, final Map<String, String> mapping) throws Exception {
		final ExportDocument exportDocument = ExportDocument.Factory.newInstance();
		final ExportType export = exportDocument.addNewExport();
		export.setDate(Calendar.getInstance());
		final CodebookType codebook = export.addNewCodebook();
		
//		if((mapping != null)&&(!mapping.isEmpty())){
//			final CodebookMappingSetType mappingSet = codebook.addNewMappingSet();
//
//			for(Map.Entry<String, String> entry:mapping.entrySet()){
//				final CodebookMappingType mappingItem = mappingSet.addNewMapping();
//				mappingItem.setType(entry.getKey());
//				mappingItem.setValue(entry.getValue());
//			}
//		}
		
		final CodebookItemSetType questionSet = codebook.addNewQuestionSet();

		final Set<HeaderEntry> headers = data.keySet();
		final Iterator<HeaderEntry> headerIt = headers.iterator();

		while (headerIt.hasNext()) {
			final CodebookItemType question = questionSet.addNewQuestion();

			final HeaderEntry header = headerIt.next();
			final Map<String, ValueEntry> options = data.get(header);

			question.setVariable(header.getVariable());
			final TextSetType headerSet = question.addNewHeader();

			final Set<String> headerTexts = header.getText();
			for (final String item : headerTexts) {
				final XmlString textItem = headerSet.addNewItem();
				textItem.setStringValue(item);
			}
			final CodebookOptionSetType optionSet = question.addNewOptions();
			for (final Map.Entry<String, ValueEntry> item : options.entrySet()) {
				final String uid = item.getKey();
				final ValueEntry valueEntry = item.getValue();

				final String label = ReplaceClient.getInstance().cleanedString(valueEntry.getLabel());
				String value = valueEntry.getValue() + "";

				final CodebookOptionType codebookOption = optionSet.addNewOption();
				if (value.equals("OPEN")) {
					codebookOption.setUid(uid);
					codebookOption.setValue(value);
					codebookOption.setLabel("Offene Angabe");
					break;
				} else {
					if (mapping.containsKey(value))
						value = mapping.get(value);
					final boolean missing = valueEntry.isMissing();
					codebookOption.setUid(uid);
					codebookOption.setValue(value);
					codebookOption.setMissing(missing+"");
					codebookOption.setLabel(label);
				}
			}
		}
		try {
			final File back = File.createTempFile("codebook", "xml");
			final XmlOptions opts = new XmlOptions();
			opts.setSavePrettyPrint();
			opts.setSavePrettyPrintIndent(4);
			exportDocument.save(back, opts);
			return back;
		} catch (final IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see presentation.codebook.format.AbstractFormat#format(java.util.Map,
	 * java.util.Map)
	 */
	@Override
	public Object format(final Map<HeaderEntry, Map<String, ValueEntry>> data, final Map<String, String> mapping) {
		if (data == null)
			return null;
		if (data.isEmpty())
			return null;

		try {
			return this.buildExport(data, mapping);
		} catch (final Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * presentation.codebook.format.AbstractFormat#formatVariableList(java.util
	 * .Map, java.util.Map)
	 */
	@Override
	public Object formatVariableList(final Map<HeaderEntry, Map<String, ValueEntry>> data, final Map<String, String> mapping) throws Exception {
		throw new Exception("Not implemented yet");
	}
	
	@Override
	public Object formatNew(Map<String, Set<Node>> data) throws Exception {
		throw new Exception("Not implemented yet");
	}

}

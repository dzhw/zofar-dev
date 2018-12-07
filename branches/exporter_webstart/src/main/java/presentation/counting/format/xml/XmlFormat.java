package presentation.counting.format.xml;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import model.HeaderEntry;
import model.ValueEntry;

import org.apache.xmlbeans.XmlOptions;
import org.apache.xmlbeans.XmlString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import presentation.counting.format.AbstractFormat;
import de.his.export.xml.export.CountingItemSetType;
import de.his.export.xml.export.CountingItemType;
import de.his.export.xml.export.CountingOptionSetType;
import de.his.export.xml.export.CountingOptionType;
import de.his.export.xml.export.CountingType;
import de.his.export.xml.export.ExportDocument;
import de.his.export.xml.export.ExportType;
import de.his.export.xml.export.TextSetType;

// TODO: Auto-generated Javadoc
/**
 * The Class XmlFormat.
 */
public class XmlFormat extends AbstractFormat {

	/** The instance. */
	private static XmlFormat INSTANCE;

	/** The Constant LOGGER. */
	private static final Logger LOGGER = LoggerFactory.getLogger(XmlFormat.class);

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
	 * @param exportDocument
	 *            the export document
	 * @param prebuild
	 *            the prebuild
	 * @param data
	 *            the data
	 * @return the file
	 */
	private File buildExport(final ExportDocument exportDocument, final File prebuild, final Map<HeaderEntry, Map<ValueEntry, Integer>> data) {
		try {
			ExportType export = exportDocument.getExport();
			if (export == null) {
				export = exportDocument.addNewExport();
				export.setDate(Calendar.getInstance());
			}
			CountingType counting = export.getCounting();
			if (counting == null) {
				counting = export.addNewCounting();
			}
			final Set<HeaderEntry> varnames = data.keySet();
			final Iterator<HeaderEntry> varnameIt = varnames.iterator();
			final CountingItemSetType questionSet = counting.addNewQuestionSet();
			while (varnameIt.hasNext()) {
				final CountingItemType question = questionSet.addNewQuestion();
				final HeaderEntry variable = varnameIt.next();
				question.setVariable(variable.getVariable());

				final Set<String> header = variable.getText();
				final TextSetType headerSet = question.addNewHeader();
				for (final String item : header) {
					final XmlString textItem = headerSet.addNewItem();
					textItem.setStringValue(item);
				}
				final CountingOptionSetType optionSet = question.addNewOptions();
				final Map<ValueEntry, Integer> content = data.get(variable);
				for (final Map.Entry<ValueEntry, Integer> item : content.entrySet()) {
					final ValueEntry entry = item.getKey();
					final CountingOptionType option = optionSet.addNewOption();
					option.setValue(entry.getValue());
					option.setLabel(entry.getLabel());
					option.setCount(item.getValue().toString());
				}
			}
			// File back = File.createTempFile("counting", ".xml");
			final File back = prebuild;
			final XmlOptions opts = new XmlOptions();
			opts.setSavePrettyPrint();
			opts.setSavePrettyPrintIndent(4);
			exportDocument.save(back, opts);
			return back;
		} catch (final IOException e1) {
			e1.printStackTrace();
		}

		return null;
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
			return this.buildExport(ExportDocument.Factory.newInstance(), File.createTempFile("counting", ".xml"), data);
		} catch (final IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	// @Override
	// public Object format(Object prebuild, Map<HeaderEntry, Map<ValueEntry,
	// Integer>> data) {
	// if (data == null)
	// return null;
	// if (data.isEmpty())
	// return null;
	// ExportDocument exportDocument = null;
	// if(prebuild == null){
	// try {
	// prebuild = File.createTempFile("counting", ".xml");
	// exportDocument = ExportDocument.Factory.newInstance();
	// } catch (IOException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	// }
	// else{
	// try {
	// exportDocument = ExportDocument.Factory.parse((File) prebuild);
	// } catch (XmlException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// } catch (IOException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	// }
	// return buildExport(exportDocument,(File) prebuild, data);
	// }
}

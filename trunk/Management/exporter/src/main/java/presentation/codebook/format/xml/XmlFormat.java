package presentation.codebook.format.xml;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import model.HeaderEntry;
import model.ValueEntry;

import org.apache.xmlbeans.XmlOptions;
import org.apache.xmlbeans.XmlString;

import presentation.codebook.format.AbstractFormat;
import utils.StringUtils;
import de.his.export.xml.export.CodebookItemSetType;
import de.his.export.xml.export.CodebookItemType;
import de.his.export.xml.export.CodebookOptionSetType;
import de.his.export.xml.export.CodebookOptionType;
import de.his.export.xml.export.CodebookType;
import de.his.export.xml.export.ExportDocument;
import de.his.export.xml.export.ExportType;
import de.his.export.xml.export.TextSetType;
import eu.dzhw.zofar.management.utils.files.FileClient;

public class XmlFormat extends AbstractFormat {

	private static XmlFormat INSTANCE;

	private XmlFormat() {
		super();
	}

	public static AbstractFormat getInstance() {
		if (INSTANCE == null)
			INSTANCE = new XmlFormat();
		return INSTANCE;
	}

	private File buildExport(
			final Map<HeaderEntry, Map<String, ValueEntry>> data,
			final String path, Map<String, String> mapping) throws Exception {
		final ExportDocument exportDocument = ExportDocument.Factory
				.newInstance();
		final ExportType export = exportDocument.addNewExport();
		export.setDate(Calendar.getInstance());
		CodebookType codebook = export.addNewCodebook();
		CodebookItemSetType questionSet = codebook.addNewQuestionSet();

		final Set<HeaderEntry> headers = data.keySet();
		final Iterator<HeaderEntry> headerIt = headers.iterator();

		while (headerIt.hasNext()) {
			CodebookItemType question = questionSet.addNewQuestion();

			final HeaderEntry header = headerIt.next();
			final Map<String, ValueEntry> options = data.get(header);

			question.setVariable(header.getVariable());
			TextSetType headerSet = question.addNewHeader();

			Set<String> headerTexts = header.getText();
			for (String item : headerTexts) {
				XmlString textItem = headerSet.addNewItem();
				textItem.setStringValue(item);
			}
			CodebookOptionSetType optionSet = question.addNewOptions();
			for (Map.Entry<String, ValueEntry> item : options.entrySet()) {
				final String uid = item.getKey();
				final ValueEntry valueEntry = item.getValue();

				final String label = StringUtils.getInstance().cleanedString(
						valueEntry.getLabel());
				String value = valueEntry.getValue() + "";

				final CodebookOptionType codebookOption = optionSet
						.addNewOption();
				if (value.equals("OPEN")) {
					codebookOption.setUid(uid);
					codebookOption.setValue(value);
					codebookOption.setLabel("Offene Angabe");
					break;
				} else {
					if (mapping.containsKey(value))
						value = mapping.get(value);
					codebookOption.setUid(uid);
					codebookOption.setValue(value);
					codebookOption.setLabel(label);
				}
			}
		}
		try {
			File back = File.createTempFile("codebook", "xml");
			XmlOptions opts = new XmlOptions();
			opts.setSavePrettyPrint();
			opts.setSavePrettyPrintIndent(4);
			exportDocument.save(back, opts);
			return back;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public Object format(Map<HeaderEntry, Map<String, ValueEntry>> data,
			Map<String, String> mapping) {
		if (data == null)
			return null;
		if (data.isEmpty())
			return null;

		try {
			return buildExport(data,
					FileClient.getInstance().createTempFile("codebook", "xml")
							.getAbsolutePath(), mapping);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}

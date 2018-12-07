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

import presentation.counting.format.AbstractFormat;
import de.his.export.xml.export.CountingItemSetType;
import de.his.export.xml.export.CountingItemType;
import de.his.export.xml.export.CountingOptionSetType;
import de.his.export.xml.export.CountingOptionType;
import de.his.export.xml.export.CountingType;
import de.his.export.xml.export.ExportDocument;
import de.his.export.xml.export.ExportType;
import de.his.export.xml.export.TextSetType;


public class XmlFormat extends AbstractFormat {
	
	private static XmlFormat INSTANCE;
	
	private XmlFormat() {
		super();
	}
	
	public static AbstractFormat getInstance(){
		if(INSTANCE == null)INSTANCE = new XmlFormat();
		return INSTANCE;
	}
	
	private File buildExport(Map<HeaderEntry, Map<ValueEntry, Integer>> data){

		final ExportDocument exportDocument = ExportDocument.Factory.newInstance();
		final ExportType export = exportDocument.addNewExport();
		export.setDate(Calendar.getInstance());
		
		
		CountingType counting = export.addNewCounting();
		final Set<HeaderEntry> varnames = data.keySet();
		final Iterator<HeaderEntry> varnameIt = varnames.iterator();
		CountingItemSetType questionSet = counting.addNewQuestionSet();
		while (varnameIt.hasNext()) {
			CountingItemType question = questionSet.addNewQuestion();
			final HeaderEntry variable = varnameIt.next();
			question.setVariable(variable.getVariable());
			
			Set<String> header = variable.getText();
			TextSetType headerSet = question.addNewHeader();
			for(String item : header){
				XmlString textItem = headerSet.addNewItem();
				textItem.setStringValue(item);
			}
			CountingOptionSetType optionSet = question.addNewOptions();
			final Map<ValueEntry, Integer> content = data.get(variable);
			for(Map.Entry<ValueEntry, Integer> item: content.entrySet()){
				final ValueEntry entry = item.getKey();
				CountingOptionType option = optionSet.addNewOption();
				option.setValue(entry.getValue());
				option.setLabel(entry.getLabel());
				option.setCount(item.getValue().toString());
			}
		}
		try {
			File back = File.createTempFile("counting", ".xml");
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
	public Object format(Map<HeaderEntry, Map<ValueEntry, Integer>> data) {
		if (data == null)
			return null;
		if (data.isEmpty())
			return null;

		return buildExport(data);
	}

}



package presentation.navigation.format.xml;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.xmlbeans.XmlOptions;

import model.TransitionEntry;
import presentation.navigation.format.AbstractFormat;
import de.his.export.xml.export.ExportDocument;
import de.his.export.xml.export.ExportType;
import de.his.export.xml.export.NavigationType;
import de.his.export.xml.export.TransitionItemType;
import de.his.export.xml.export.TransitionSetType;
import eu.dzhw.zofar.management.utils.files.FileClient;

public class XmlFormat extends AbstractFormat {
	
	private static XmlFormat INSTANCE;

	private XmlFormat() {
		super();
	}
	
	public static AbstractFormat getInstance(){
		if(INSTANCE == null)INSTANCE = new XmlFormat();
		return INSTANCE;
	}

	@Override
	public Object format(Map<String, Set<TransitionEntry>> data) {
		if (data == null)
			return null;
		if (data.isEmpty())
			return null;

		try {
			return buildExport(data,FileClient.getInstance().createTempFile("transitions", "xml").getAbsolutePath());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private Object buildExport(Map<String, Set<TransitionEntry>> data,
			String absolutePath) {
		final ExportDocument exportDocument = ExportDocument.Factory.newInstance();
		final ExportType export = exportDocument.addNewExport();
		export.setDate(Calendar.getInstance());
		final NavigationType navigation = export.addNewNavigation();
		final Set<String> sourcePages = data.keySet();
		final Iterator<String> sourceIt = sourcePages.iterator();
		
		while (sourceIt.hasNext()) {
			final String sourcePage = sourceIt.next();
			final Set<TransitionEntry> transitions = data.get(sourcePage);
			final TransitionSetType navigationSet = navigation.addNewTransitionSet();
			navigationSet.setSource(sourcePage);
			for(TransitionEntry transition:transitions){
				TransitionItemType transitionItem = navigationSet.addNewTransition();
				transitionItem.setTarget(transition.getTargetPage());
				if(transition.getCondition() != null)transitionItem.setCondition(transition.getCondition());
			}
		}
		try {
			File back = File.createTempFile("transitions", ".xml");
			 XmlOptions opts = new XmlOptions();
			 opts.setSavePrettyPrint();
			 opts.setSavePrettyPrintIndent(4);
			exportDocument.save(back,opts);
			return back;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

}

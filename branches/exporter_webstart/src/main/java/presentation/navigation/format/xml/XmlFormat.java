package presentation.navigation.format.xml;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import model.TransitionEntry;

import org.apache.xmlbeans.XmlOptions;

import presentation.navigation.format.AbstractFormat;
import de.his.export.xml.export.ExportDocument;
import de.his.export.xml.export.ExportType;
import de.his.export.xml.export.NavigationType;
import de.his.export.xml.export.TransitionItemType;
import de.his.export.xml.export.TransitionSetType;
import eu.dzhw.zofar.management.utils.files.FileClient;

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

	/*
	 * (non-Javadoc)
	 * 
	 * @see presentation.navigation.format.AbstractFormat#format(java.util.Map)
	 */
	@Override
	public Object format(final Map<String, Set<TransitionEntry>> data) {
		if (data == null)
			return null;
		if (data.isEmpty())
			return null;

		try {
			return this.buildExport(data, FileClient.getInstance().createTempFile("transitions", "xml").getAbsolutePath());
		} catch (final Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Builds the export.
	 * 
	 * @param data
	 *            the data
	 * @param absolutePath
	 *            the absolute path
	 * @return the object
	 */
	private Object buildExport(final Map<String, Set<TransitionEntry>> data, final String absolutePath) {
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
			for (final TransitionEntry transition : transitions) {
				final TransitionItemType transitionItem = navigationSet.addNewTransition();
				transitionItem.setTarget(transition.getTargetPage());
				if (transition.getCondition() != null)
					transitionItem.setCondition(transition.getCondition());
			}
		}
		try {
			final File back = File.createTempFile("transitions", ".xml");
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

}

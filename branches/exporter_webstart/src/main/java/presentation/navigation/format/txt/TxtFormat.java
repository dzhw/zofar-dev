package presentation.navigation.format.txt;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import model.TransitionEntry;
import presentation.navigation.format.AbstractFormat;
import eu.dzhw.zofar.management.utils.files.FileClient;

// TODO: Auto-generated Javadoc
/**
 * The Class TxtFormat.
 */
public class TxtFormat extends AbstractFormat {

	/** The instance. */
	private static TxtFormat INSTANCE;

	/**
	 * Instantiates a new txt format.
	 */
	private TxtFormat() {
		super();
	}

	/**
	 * Gets the single instance of TxtFormat.
	 * 
	 * @return single instance of TxtFormat
	 */
	public static AbstractFormat getInstance() {
		if (INSTANCE == null)
			INSTANCE = new TxtFormat();
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
			return this.buildExport(data, FileClient.getInstance().createTempFile("transitions", "txt").getAbsolutePath());
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
		final StringBuffer buffer = new StringBuffer();
		final Set<String> sourcePages = data.keySet();
		final Iterator<String> sourceIt = sourcePages.iterator();

		while (sourceIt.hasNext()) {
			final String sourcePage = sourceIt.next();
			final Set<TransitionEntry> transitions = data.get(sourcePage);
			buffer.append("Source : " + sourcePage + "\n");
			for (final TransitionEntry transition : transitions) {
				if (transition.getCondition() != null)
					buffer.append("\t =(" + transition.getCondition().replaceAll(" {2,}", " ") + ")=> " + transition.getTargetPage() + " \n");
				else
					buffer.append("\t ==> " + transition.getTargetPage() + " \n");
			}
			buffer.append("\n");
		}

		return buffer.toString();
	}

}

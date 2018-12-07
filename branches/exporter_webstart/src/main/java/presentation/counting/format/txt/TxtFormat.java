package presentation.counting.format.txt;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import model.HeaderEntry;
import model.ValueEntry;
import presentation.counting.format.AbstractFormat;
import eu.dzhw.zofar.management.utils.string.ReplaceClient;

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

	/**
	 * Builds the export.
	 * 
	 * @param prebuild
	 *            the prebuild
	 * @param data
	 *            the data
	 * @return the string
	 */
	private String buildExport(final String prebuild, final Map<HeaderEntry, Map<ValueEntry, Integer>> data) {
		final StringBuffer buffer = new StringBuffer(prebuild);

		final Set<HeaderEntry> varnames = data.keySet();
		final Iterator<HeaderEntry> varnameIt = varnames.iterator();

		while (varnameIt.hasNext()) {
			final HeaderEntry variable = varnameIt.next();
			buffer.append("[" + variable.getVariable() + "]");

			final Set<String> header = variable.getText();
			for (final String item : header) {
				buffer.append(" " + item + "");
			}
			buffer.append("\n");
			final Map<ValueEntry, Integer> content = data.get(variable);
			for (final Map.Entry<ValueEntry, Integer> item : content.entrySet()) {
				final ValueEntry entry = item.getKey();
				buffer.append("\t(" + entry.getValue() + ") " + ReplaceClient.getInstance().cleanedString(entry.getLabel()) + " ==> " + item.getValue() + "\n");
			}
			buffer.append("------------------------------------\n");
		}
		return buffer.toString();
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
		return this.buildExport("", data);
	}

	// @Override
	// public Object format(Object prebuild,Map<HeaderEntry, Map<ValueEntry,
	// Integer>> data) {
	// if(prebuild == null)prebuild="";
	// return buildExport((String)prebuild,data);
	// }
}

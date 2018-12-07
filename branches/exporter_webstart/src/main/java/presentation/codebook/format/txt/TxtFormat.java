package presentation.codebook.format.txt;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.w3c.dom.Node;

import eu.dzhw.zofar.management.utils.files.FileClient;
import eu.dzhw.zofar.management.utils.string.ReplaceClient;
import model.HeaderEntry;
import model.ValueEntry;
import presentation.codebook.format.AbstractFormat;

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
	 * @param data
	 *            the data
	 * @param path
	 *            the path
	 * @param mapping
	 *            the mapping
	 * @return the string
	 * @throws Exception
	 *             the exception
	 */
//	private String buildExport(final Map<HeaderEntry, Map<String, ValueEntry>> data, final String path, final Map<String, String> mapping) throws Exception {
	private String buildExport(final Map<HeaderEntry, Map<String, ValueEntry>> data, final Map<String, String> mapping) throws Exception {
		final StringBuffer buffer = new StringBuffer();
		
//		if((mapping != null)&&(!mapping.isEmpty())){
//			buffer.append("Missing-Codes:\n");
//			for(Map.Entry<String, String> entry:mapping.entrySet()){
//				buffer.append(entry.getKey()+" => "+entry.getValue()+"\n");
//			}
//			buffer.append("\n");
//		}

		final Set<HeaderEntry> headers = data.keySet();
		final Iterator<HeaderEntry> headerIt = headers.iterator();

		while (headerIt.hasNext()) {
			final HeaderEntry header = headerIt.next();
			final Map<String, ValueEntry> options = data.get(header);

			buffer.append("[" + header.getVariable() + "]");

			final Set<String> headerTexts = header.getText();
			for (final String item : headerTexts) {
				buffer.append(" " + item + "");
			}

			buffer.append("\n");

			for (final Map.Entry<String, ValueEntry> item : options.entrySet()) {
				final String uid = item.getKey();
				final ValueEntry valueEntry = item.getValue();

				final String label = ReplaceClient.getInstance().cleanedString(valueEntry.getLabel());
				String value = valueEntry.getValue() + "";
				if (value.equals("OPEN")) {
					buffer.append("\t(" + uid + ") Offene Angabe\n");
					break;
				} else {
					if (mapping.containsKey(value))
						value = mapping.get(value);
					final boolean missing = valueEntry.isMissing();
					if(missing) buffer.append("\t(" + uid + ") [MISSING] " + label + " ==> " + value + "\n");
					else buffer.append("\t(" + uid + ") " + label + " ==> " + value + "\n");
				}
			}
		}
		return buffer.toString();
	}

	/**
	 * Builds the var list.
	 * 
	 * @param data
	 *            the data
	 * @param path
	 *            the path
	 * @param mapping
	 *            the mapping
	 * @return the string
	 * @throws Exception
	 *             the exception
	 */
	private String buildVarList(final Map<HeaderEntry, Map<String, ValueEntry>> data, final String path, final Map<String, String> mapping) throws Exception {
		final StringBuffer buffer = new StringBuffer();

		final Set<HeaderEntry> headers = data.keySet();
		final Iterator<HeaderEntry> headerIt = headers.iterator();

		while (headerIt.hasNext()) {
			final HeaderEntry header = headerIt.next();

			buffer.append("" + header.getVariable() + "\n");
		}
		return buffer.toString();
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
		if (data == null)
			return null;
		if (data.isEmpty())
			return null;

		try {
			return this.buildVarList(data, FileClient.getInstance().createTempFile("variables", "txt").getAbsolutePath(), mapping);
		} catch (final Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public Object formatNew(Map<String, Set<Node>> data) throws Exception {
		throw new Exception("Not implemented yet");
	}
	
	
}

package presentation.data.format;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.his.export.xml.export.ParticipantType;

// TODO: Auto-generated Javadoc
/**
 * The Class AbstractFormat.
 */
public abstract class AbstractFormat {

	/** The Constant LOGGER. */
	private static final Logger LOGGER = LoggerFactory.getLogger(AbstractFormat.class);

	/**
	 * Instantiates a new abstract format.
	 */
	protected AbstractFormat() {
		super();
	}

	// public abstract Object format(final Set<Map<String, Object>> data);
	// public abstract Object format(final Set<Map<String, Object>> data,final
	// char fieldSeparator,final char rowSeparator);

	/**
	 * Format.
	 * 
	 * @param data
	 *            the data
	 * @return the object
	 */
	public abstract Object format(final Set<de.his.export.xml.export.ParticipantType> data);

	/**
	 * Format.
	 * 
	 * @param data
	 *            the data
	 * @param fieldSeparator
	 *            the field separator
	 * @param rowSeparator
	 *            the row separator
	 * @return the object
	 */
	public abstract Object format(final Set<ParticipantType> data, final char fieldSeparator, final char rowSeparator);

	/**
	 * Format.
	 * 
	 * @param data
	 *            the data
	 * @param fieldSeparator
	 *            the field separator
	 * @param rowSeparator
	 *            the row separator
	 * @param mapping
	 *            the mapping
	 * @return the object
	 */
	public abstract Object format(final Set<ParticipantType> data, final char fieldSeparator, final char rowSeparator, final Map<String, String> mapping);

	/**
	 * Format.
	 * 
	 * @param data
	 *            the data
	 * @param fieldSeparator
	 *            the field separator
	 * @param rowSeparator
	 *            the row separator
	 * @param mapping
	 *            the mapping
	 * @param ignoreFirst
	 *            the ignore first
	 * @return the object
	 */
	public abstract Object format(Set<ParticipantType> data, char fieldSeparator, char rowSeparator, Map<String, String> mapping, boolean ignoreFirst, final boolean limitStrings);

	/**
	 * Format.
	 * 
	 * @param prebuild
	 *            the prebuild
	 * @param data
	 *            the data
	 * @param fieldSeparator
	 *            the field separator
	 * @param rowSeparator
	 *            the row separator
	 * @param mapping
	 *            the mapping
	 * @param ignoreFirst
	 *            the ignore first
	 * @return the object
	 */
	public abstract Object format(Object prebuild, Set<ParticipantType> data, char fieldSeparator, char rowSeparator, Map<String, String> mapping, boolean ignoreFirst, final boolean limitStrings);
	public abstract Object format(Object prebuild, Set<ParticipantType> data, char fieldSeparator, char rowSeparator, Map<String, String> mapping, boolean ignoreFirst, final boolean limitStrings,final Map<File,Set<String>> fileMapping,final Map<String,Set<File>> reverseFileMapping);

	public abstract Object formatAsStream(final File historyDirectory, final File dataFile, final Set<ParticipantType> data, final char fieldSeparator, final char rowSeparator, final Map<String, String> mapping, final boolean ignoreFirst, final boolean saveHistory, final boolean limitStrings)	throws IOException;
	public abstract Object formatAsStream(final File historyDirectory, final File dataFile, final Set<ParticipantType> data, final char fieldSeparator, final char rowSeparator, final Map<String, String> mapping, final boolean ignoreFirst, final boolean saveHistory, final boolean limitStrings,final Map<File,Set<String>> fileMapping)	throws IOException;

}

package presentation.mdm.format;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import presentation.mdm.format.csv.CSVFormatter;
import presentation.mdm.format.json.JSONFormatter;

public class FormatFactory {

	/** The Constant LOGGER. */
	private static final Logger LOGGER = LoggerFactory.getLogger(FormatFactory.class);

	/** The instance. */
	private static FormatFactory INSTANCE;

	/**
	 * The Enum FORMAT.
	 */
	public enum FORMAT {
		/** The json. */
		json,csv
	}

	/**
	 * Instantiates a new format factory.
	 */
	private FormatFactory() {
		super();
	}

	/**
	 * Gets the single instance of FormatFactory.
	 * 
	 * @return single instance of FormatFactory
	 */
	public static FormatFactory getInstance() {
		if (INSTANCE == null)
			INSTANCE = new FormatFactory();
		return INSTANCE;
	}

	/**
	 * Gets the format.
	 * 
	 * @param format
	 *            the format
	 * @return the format
	 */
	public AbstractFormat getFormat(final FORMAT format) {
		if (format.equals(FORMAT.json))
			return JSONFormatter.getInstance();
		if (format.equals(FORMAT.csv))
			return CSVFormatter.getInstance();
		return null;
	}

}

package presentation.statistics.format;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import presentation.statistics.format.stata.StataFormat;

// TODO: Auto-generated Javadoc
/**
 * A factory for creating Format objects.
 */
public final class FormatFactory {

	/** The Constant LOGGER. */
	private static final Logger LOGGER = LoggerFactory.getLogger(FormatFactory.class);

	/** The instance. */
	private static FormatFactory INSTANCE;

	/**
	 * The Enum FORMAT.
	 */
	public enum FORMAT {

		/** The stata. */
		stata
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
		if (format.equals(FORMAT.stata))
			return StataFormat.getInstance();
		return null;
	}
}

package presentation.counting.format;

import java.util.Map;

import model.HeaderEntry;
import model.ValueEntry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

	/**
	 * Format.
	 * 
	 * @param data
	 *            the data
	 * @return the object
	 */
	public abstract Object format(final Map<HeaderEntry, Map<ValueEntry, Integer>> data);
	// public abstract Object format(final Object prebuild,final
	// Map<HeaderEntry, Map<ValueEntry, Integer>> data);
}

package presentation.dokudat.format;

import java.util.Map;

import model.HeaderEntry;

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
	 * @param map
	 *            the data
	 * @return the object
	 */
	public abstract Object format(Map<HeaderEntry, Object> data,final Map<String,String> mapping);
}

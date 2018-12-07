package presentation.codebook.format;

import java.util.Map;
import java.util.Set;

import model.HeaderEntry;
import model.ValueEntry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Node;

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
	
	public abstract Object formatNew(final Map<String, Set<Node>> data) throws Exception ;


	/**
	 * Format.
	 * 
	 * @param data
	 *            the data
	 * @param mapping
	 *            the mapping
	 * @return the object
	 */
	public abstract Object format(final Map<HeaderEntry, Map<String, ValueEntry>> data, Map<String, String> mapping);

	/**
	 * Format variable list.
	 * 
	 * @param data
	 *            the data
	 * @param mapping
	 *            the mapping
	 * @return the object
	 * @throws Exception
	 *             the exception
	 */
	public abstract Object formatVariableList(final Map<HeaderEntry, Map<String, ValueEntry>> data, Map<String, String> mapping) throws Exception;
}

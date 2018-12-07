package presentation.statistics.format;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import service.statistics.StatisticService.TYPE;

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
	 * @param structure
	 *            the structure
	 * @return the object
	 */
	public abstract Object format(Map<TYPE, Object> structure,final String surveyName,final String datasetName,final boolean limitLabels);
	
	/**
	 * Format.
	 * 
	 * @param structure
	 *            the structure
	 * @param mapping
	 *            the mapping
	 * @return the object
	 */
	public abstract Object format(Map<TYPE, Object> structure, Map<String, String> mapping,final String surveyName,final String datasetName,final boolean limitLabels);
}

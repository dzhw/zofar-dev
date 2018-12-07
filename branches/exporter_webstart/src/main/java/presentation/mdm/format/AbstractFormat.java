package presentation.mdm.format;

import java.util.Map;

import javax.json.JsonObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractFormat {

	/** The Constant LOGGER. */
	private static final Logger LOGGER = LoggerFactory.getLogger(AbstractFormat.class);

	/**
	 * Instantiates a new abstract format.
	 */
	protected AbstractFormat() {
		super();
	}
	
	public abstract Object formatJSON(final Map<String, JsonObject> data);
	public abstract Object formatCSV(final Map<String,Map<String,Object>> data) throws Exception;

}

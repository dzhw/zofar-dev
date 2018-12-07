package presentation.codebook.format;

import java.util.Map;

import model.HeaderEntry;
import model.ValueEntry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractFormat {
	
	private static final Logger LOGGER = LoggerFactory
			.getLogger(AbstractFormat.class);
	
	protected AbstractFormat() {
		super();
	}
	
	public abstract Object format(final Map<HeaderEntry, Map<String, ValueEntry>> data, Map<String, String> mapping);
}

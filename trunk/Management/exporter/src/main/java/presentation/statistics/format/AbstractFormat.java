package presentation.statistics.format;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import service.statistics.StatisticService.TYPE;

public abstract class AbstractFormat {
	
	private static final Logger LOGGER = LoggerFactory
			.getLogger(AbstractFormat.class);
	
	protected AbstractFormat() {
		super();
	}
	
	public abstract Object format(Map<TYPE, Object> structure);
	public abstract Object format(Map<TYPE, Object> structure,	Map<String, String> mapping);
}

package presentation.feedback.format;

import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.his.export.xml.export.ParticipantType;

public abstract class AbstractFormat {
	
	private static final Logger LOGGER = LoggerFactory
			.getLogger(AbstractFormat.class);
	
	protected AbstractFormat() {
		super();
	}
	
	public abstract Object formatExits(final Map<String,Integer> data);
	public abstract Object formatCounts(final Map<String,Integer> data);
}

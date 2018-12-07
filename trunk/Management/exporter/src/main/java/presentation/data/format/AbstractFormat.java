package presentation.data.format;

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
	
//	public abstract Object format(final Set<Map<String, Object>> data);
//	public abstract Object format(final Set<Map<String, Object>> data,final char fieldSeparator,final char rowSeparator);

	public abstract Object format(final Set<ParticipantType> data);
	public abstract Object format(final Set<ParticipantType> data,final char fieldSeparator,final char rowSeparator);
	public abstract Object format(final Set<ParticipantType> data,final char fieldSeparator,final char rowSeparator, final Map<String,String> mapping);
	public abstract Object format(Set<ParticipantType> data, char fieldSeparator,	char rowSeparator, Map<String, String> mapping, boolean ignoreFirst) ;
}

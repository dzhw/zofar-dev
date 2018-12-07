package presentation.statistics.format;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import presentation.statistics.format.stata.StataFormat;

public final class FormatFactory {
	
	private static final Logger LOGGER = LoggerFactory
			.getLogger(FormatFactory.class);
	
	private static FormatFactory INSTANCE;
	
	public enum FORMAT{
		stata
	}
	
	private FormatFactory() {
		super();
	}
	
	public static FormatFactory getInstance(){
		if(INSTANCE == null)INSTANCE = new FormatFactory();
		return INSTANCE;
	}
	
	public AbstractFormat getFormat(final FORMAT format){
		if(format.equals(FORMAT.stata))return StataFormat.getInstance();
		return null;
	}
}

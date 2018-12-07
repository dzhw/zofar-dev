package presentation.feedback.format;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import presentation.feedback.format.txt.TxtFormat;

public final class FormatFactory {
	
	private static final Logger LOGGER = LoggerFactory
			.getLogger(FormatFactory.class);
	
	private static FormatFactory INSTANCE;
	
	public enum FORMAT{
		txt
	}
	
	private FormatFactory() {
		super();
	}
	
	public static FormatFactory getInstance(){
		if(INSTANCE == null)INSTANCE = new FormatFactory();
		return INSTANCE;
	}
	
	public AbstractFormat getFormat(final FORMAT format){
		if(format.equals(FORMAT.txt))return TxtFormat.getInstance();
		return null;
	}
}

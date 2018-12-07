package presentation.navigation.format;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import presentation.navigation.format.txt.TxtFormat;
import presentation.navigation.format.xml.XmlFormat;

public final class FormatFactory {
	
	private static final Logger LOGGER = LoggerFactory
			.getLogger(FormatFactory.class);
	
	private static FormatFactory INSTANCE;
	
	public enum FORMAT{
		txt,xml
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
		if(format.equals(FORMAT.xml))return XmlFormat.getInstance();
		return null;
	}
}

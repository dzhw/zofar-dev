package presentation.data.format;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import presentation.data.format.csv.CsvFormat;
import presentation.data.format.xml.XmlFormat;

public final class FormatFactory {
	
	private static final Logger LOGGER = LoggerFactory
			.getLogger(FormatFactory.class);
	
	private static FormatFactory INSTANCE;
	
	public enum FORMAT{
		csv,xml
	}
	
	private FormatFactory() {
		super();
	}
	
	public static FormatFactory getInstance(){
		if(INSTANCE == null)INSTANCE = new FormatFactory();
		return INSTANCE;
	}
	
	public AbstractFormat getFormat(final FORMAT format){
		if(format.equals(FORMAT.xml))return XmlFormat.getInstance();
		if(format.equals(FORMAT.csv))return CsvFormat.getInstance();
		return null;
	}
}

package presentation.counting.format;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import presentation.counting.format.odf.OdfFormat;
import presentation.counting.format.txt.TxtFormat;
import presentation.counting.format.xml.XmlFormat;

public final class FormatFactory {
	
	private static final Logger LOGGER = LoggerFactory
			.getLogger(FormatFactory.class);
	
	private static FormatFactory INSTANCE;
	
	public enum FORMAT{
		odf,txt,xml
	}
	
	private FormatFactory() {
		super();
	}
	
	public static FormatFactory getInstance(){
		if(INSTANCE == null)INSTANCE = new FormatFactory();
		return INSTANCE;
	}
	
	public AbstractFormat getFormat(final FORMAT format){
		if(format.equals(FORMAT.odf))return OdfFormat.getInstance();
		if(format.equals(FORMAT.txt))return TxtFormat.getInstance();
		if(format.equals(FORMAT.xml))return XmlFormat.getInstance();
		return null;
	}
}

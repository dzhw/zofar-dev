package eu.dzhw.zofar.management.utils.string;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConvertClient {
	/** The Constant INSTANCE. */
	private static final ConvertClient INSTANCE = new ConvertClient();

	/** The Constant LOGGER. */
	private final static Logger LOGGER = LoggerFactory.getLogger(ConvertClient.class);

	/**
	 * Instantiates a new string utils.
	 */
	private ConvertClient() {
		super();
	}

	/**
	 * Gets the single instance of StringUtils.
	 * 
	 * @return single instance of StringUtils
	 */
	public static synchronized ConvertClient getInstance() {
		return INSTANCE;
	}
	

	public String convert(String source,Charset fromCharset,Charset toCharset) throws Exception{
//		throw new Exception("Not implemented yet");
		if(source == null)return null;
		return new String(source.getBytes(fromCharset),toCharset);
	}
	
	public List<String> availableCharacterSets(){
		return new ArrayList<String>(Charset.availableCharsets().keySet());
	}
	
	public Charset getCharacterSet(final String charsetName){
		return Charset.forName(charsetName);
	}

	
}

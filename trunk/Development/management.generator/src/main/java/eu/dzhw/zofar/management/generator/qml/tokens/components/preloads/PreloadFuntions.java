package eu.dzhw.zofar.management.generator.qml.tokens.components.preloads;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PreloadFuntions {

	private static final PreloadFuntions INSTANCE = new PreloadFuntions();

	private static final Logger LOGGER = LoggerFactory
			.getLogger(PreloadFuntions.class);

	private PreloadFuntions() {
		super();
	}

	public static PreloadFuntions getInstance() {
		return INSTANCE;
	}
	
	public String execute(final Integer index,String function,final String value){
		if(function == null)return value;
		if(function.equals(""))return value;
		else if(function.equals("Fester Wert")){
			return fester_Wert(index,value);
		}
		else if(function.equals("Zufälliger Wert aus")){
			return zufaelliger_Wert_aus(index,value);
		}
		return value;
	}
	
	private String fester_Wert(final Integer index,final String value){
		return value;
	}
	
	private String zufaelliger_Wert_aus(final Integer index,final String value){
		return value;
	}

}

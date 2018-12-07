package utils;

import java.util.regex.Pattern;

public class StringUtils {
	
	private static StringUtils INSTANCE;
	
	private StringUtils(){
		super();
	}
	
	public static StringUtils getInstance(){
		if(INSTANCE == null)INSTANCE = new StringUtils();
		return INSTANCE;
	}

	public String cleanedString(final String input){
		String output = input;
		output = output.replaceAll(Pattern.quote("#{layout.BREAK}"), "");
		output = output.replaceAll("\n", "");
		output = output.replaceAll("\t", " ");
		output = output.replaceAll(" {2,}", " ");
		output = output.trim();
		return output;
	}

}

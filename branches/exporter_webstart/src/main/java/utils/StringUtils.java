package utils;

import java.util.regex.Pattern;

// TODO: Auto-generated Javadoc
/**
 * The Class StringUtils.
 */
public class StringUtils {

	/** The instance. */
	private static StringUtils INSTANCE;

	/**
	 * Instantiates a new string utils.
	 */
	private StringUtils() {
		super();
	}

	/**
	 * Gets the single instance of StringUtils.
	 * 
	 * @return single instance of StringUtils
	 */
	public static StringUtils getInstance() {
		if (INSTANCE == null)
			INSTANCE = new StringUtils();
		return INSTANCE;
	}

	/**
	 * Cleaned string.
	 * 
	 * @param input
	 *            the input
	 * @return the string
	 */
	public String cleanedString(final String input) {
		String output = input;
		output = output.replaceAll(Pattern.quote("#{layout.BREAK}"), "");
		output = output.replaceAll("\n", "");
		output = output.replaceAll("\t", " ");
		output = output.replaceAll(" {2,}", " ");
		output = output.trim();
		return output;
	}

}

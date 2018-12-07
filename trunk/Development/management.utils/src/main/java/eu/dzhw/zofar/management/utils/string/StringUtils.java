/*
 * Utility Class to create and analyze Strings
 */
package eu.dzhw.zofar.management.utils.string;

import java.io.IOException;
import java.util.UUID;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.text.WordUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xerial.snappy.Snappy;

/**
 * The Class StringUtils.
 */
public class StringUtils {

	/** The Constant INSTANCE. */
	private static final StringUtils INSTANCE = new StringUtils();

	/** The Constant LOGGER. */
	private final static Logger LOGGER = LoggerFactory.getLogger(StringUtils.class);

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
	public static synchronized StringUtils getInstance() {
		return INSTANCE;
	}

	/**
	 * Diff.
	 * 
	 * @param pattern1
	 *            the pattern1
	 * @param pattern2
	 *            the pattern2
	 * @see org.apache.commons.lang3.StringUtils.difference
	 * @return the string
	 */
	public String diff(String pattern1, String pattern2) {
		if (pattern1 == null)
			return null;
		pattern1 = pattern1.trim();
		if (pattern1.equals(""))
			return null;
		if (pattern2 == null)
			return null;
		pattern2 = pattern2.trim();
		if (pattern2.equals(""))
			return null;

		return org.apache.commons.lang3.StringUtils.difference(pattern1, pattern2);
	}

	/**
	 * Distance.
	 * 
	 * @param pattern1
	 *            the pattern1
	 * @param pattern2
	 *            the pattern2
	 * @see org.apache.commons.lang3.StringUtils.getLevensteinDistance
	 * @return the double
	 */
	public double distance(String pattern1, String pattern2) {
		if (pattern1 == null)
			return -1;
		pattern1 = pattern1.trim();
		if (pattern1.equals(""))
			return -1;
		if (pattern2 == null)
			return -1;
		pattern2 = pattern2.trim();
		if (pattern2.equals(""))
			return -1;
		return org.apache.commons.text.similarity.LevenshteinDistance.getDefaultInstance().apply(pattern1, pattern2);
	}

	/**
	 * Shuffle String recursive.
	 * 
	 * @param content
	 *            the content
	 * @return the string
	 */
	public String shuffle(final String content) {
		if (content.length() <= 1)
			return content;

		final int split = content.length() / 2;

		final String temp1 = this.shuffle(content.substring(0, split));
		final String temp2 = this.shuffle(content.substring(split));

		if (Math.random() > 0.5)
			return temp1 + temp2;
		else
			return temp2 + temp1;
	}

	/**
	 * Generate String hash.
	 * 
	 * @param content
	 *            the content
	 * @return the long
	 */
	public long stringHash(String content) {
		long back = 0L;
		if (content != null) {
			content = content.trim();
			final char[] characters = content.toCharArray();
			if (characters != null) {
				final int count = characters.length;
				for (int a = 0; a < count; a++) {
					back += characters[a];
				}
			}
		}
		return back;
	}

	/**
	 * Repeat String.
	 * 
	 * @param times
	 *            the times
	 * @param value
	 *            the value
	 * @param glue
	 *            the glue
	 * @return the string
	 */
	public String times(final int times, final String value, final String glue) {
		String back = "";
		int tmp = 1;
		while (tmp < times) {
			if (tmp > 1)
				back = back + glue;
			back = back + value;
			tmp++;
		}
		return back;
	}

	/**
	 * Fillup String with fill Character to maxSize.
	 * 
	 * @param value
	 *            the value
	 * @param fill
	 *            the fill
	 * @param maxSize
	 *            the max size
	 * @return the string
	 */
	public String fillup(String value, final char fill, final int maxSize) {
		if (value == null)
			value = "null";
		final int count = Math.max(0, maxSize - value.length());
		for (int a = 0; a < count; a++)
			value += fill;
		return value;
	}

	/**
	 * Random string with limit size using
	 * 'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890' as character
	 * pool
	 * 
	 * @param limit
	 *            the limit
	 * @return the string
	 */
	public String randomString(final int limit) {
		return this.randomString(limit, true);
	}

	public String randomString(final int limit, boolean inclusiveNumbers) {
		String characterPool = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
		if (inclusiveNumbers)
			characterPool = characterPool + "1234567890";

		return this.randomString(characterPool, limit);
	}

	// /**
	// * Generate random token string.
	// *
	// * @param limit
	// * the limit
	// * @return the string
	// */
	// public String randomTokenString(final int limit) {
	// return this.randomString(limit);
	// }

	/**
	 * Generate random UUID as String.
	 * 
	 * @return the string
	 */
	public String randomUUID() {
		return UUID.randomUUID().toString();
	}

	/**
	 * Random string with limit size using custom character set as pool.
	 * 
	 * @param chars
	 *            the chars
	 * @param length
	 *            the length
	 * @return the string
	 */
	public String randomString(final String chars, final int length) {
		return RandomStringUtils.random(length, chars);
	}

	/** The Constant decimalPattern. */
	private static final String decimalPattern = "(-)?(\\d+)?(.)?(\\d+)?";

	/**
	 * Checks if value is a number.
	 * 
	 * @param value
	 *            the value
	 * @return true, if is number
	 */
	public boolean isNumber(final String value) {
		return checkPattern(value, decimalPattern);
	}

	/** The Constant integerPattern. */
	private static final String integerPattern = "(-)?(\\d+)?";

	/**
	 * Checks if value is an integer.
	 * 
	 * @param value
	 *            the value
	 * @return true, if is integer
	 */
	public boolean isInteger(final String value) {
		return checkPattern(value, integerPattern);
	}

	private boolean checkPattern(final String value, final String pattern) {
		if (value == null)
			return false;
		if ((value.trim()).equals(""))
			return false;
		return value.matches(pattern);
	}

	/**
	 * Escape String.
	 * 
	 * @param str
	 *            the str
	 * 
	 * @see org.apache.commons.lang3.StringEscapeUtils.escapeJava
	 * @return the string
	 */
	public String escape(final String str) {
		return StringEscapeUtils.escapeJava(str);
	}

	/**
	 * Capitalize every first Character of Word
	 * 
	 * @param str
	 * @return
	 */
	public String capitalize(final String str) {
		return WordUtils.capitalize(str);
	}

	public String strip(final String str) {
		// if(str == null)return "";
		return str.replaceAll("(^ )|( $)", "");
	}

	public String mix(final String str, final char chr) {
		if (str == null)
			return "";
		final StringBuffer back = new StringBuffer();
		char[] characters = str.toCharArray();
		int size = characters.length;
		boolean first = true;
		for (char tmp : characters) {
			if (!first)
				back.append(chr);
			back.append(tmp);
			first = false;
		}
		return back.toString();
	}
	
	
	public String trim(final String str, final int limit) {
		if (str == null)
			return null;
		if (str.length() <= limit)
			return str;
		return str.substring(0, limit);
	}

	/**
	 * Create limited Teaser from String and append '...'
	 * 
	 * @param str
	 *            the str
	 * @param limit
	 *            the limit
	 * @return the string
	 */
	public String teaser(final String str, final int limit) {
		if (str == null)
			return null;
		if (str.length() <= limit)
			return str;
		return trim(str,limit) + "...";
	}
	
	
	

	/**
	 * Create limited Tail from String and append leading '...'
	 * 
	 * @param str
	 *            the str
	 * @param limit
	 *            the limit
	 * @return the string
	 */
	public String tail(final String str, final int limit) {
		if (str == null)
			return null;
		if (str.length() <= limit)
			return str;
		return "..." + str.substring(str.length() - limit);
	}

	/**
	 * get Character by Hex Code
	 * 
	 * @param hexCode
	 *            (example 'A0' returns á)
	 * @return character represented by hex code
	 */
	public char getCharacterByHex(final String hexCode) {
		int hex = Integer.parseInt("25" + hexCode, 16);
		char[] toChars = Character.toChars(hex);
		return toChars[0];
	}

	public String compress(final String content) throws IOException {
		byte[] compressed = Snappy.compress(content.getBytes());
		return new String(Hex.encodeHex(compressed));
	}

	public String decompress(final String content) throws DecoderException, IOException {
		byte[] uncompressed = Hex.decodeHex(content.toCharArray());
		return new String(Snappy.uncompress(uncompressed));
	}
}

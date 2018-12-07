/*
 * Class of Methods to create Numbers
 */
package eu.dzhw.zofar.management.utils.numbers;

import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Class UtilClient.
 */
public class UtilClient {

	/** The Constant INSTANCE. */
	private static final UtilClient INSTANCE = new UtilClient();

	/** The Constant LOGGER. */
	final static Logger LOGGER = LoggerFactory.getLogger(UtilClient.class);
	
	/** The random. */
	private final Random random;

	/**
	 * Instantiates a new util client.
	 */
	private UtilClient() {
		super();
		this.random = new Random();
	}

	/**
	 * Gets the single instance of UtilClient.
	 * 
	 * @return single instance of UtilClient
	 */
	public static synchronized UtilClient getInstance() {
		return INSTANCE;
	}

	/**
	 * Ceil Double value.
	 * 
	 * @param value
	 *            the value
	 *            
	 * @see java.lang.Math.ceil           
	 * @return the int
	 */
	public int ceil(final double value) {
		return (int) Math.ceil(value);
	}

	/**
	 * create Random Integer.
	 * 
	 * @param min
	 *            the min
	 * @param max
	 *            the max
	 * @return the int
	 */
	public int randomInt(final int min, final int max) {
		return this.random.nextInt(max - min + 1) + min;
	}

}

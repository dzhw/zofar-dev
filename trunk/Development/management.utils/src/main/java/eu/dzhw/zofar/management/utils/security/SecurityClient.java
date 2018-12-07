package eu.dzhw.zofar.management.utils.security;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SecurityClient {

	/** The Constant INSTANCE. */
	private static final SecurityClient INSTANCE = new SecurityClient();

	/** The Constant LOGGER. */
	private final static Logger LOGGER = LoggerFactory.getLogger(SecurityClient.class);

	/**
	 * Instantiates a new collection client.
	 */
	private SecurityClient() {
		super();
	}

	/**
	 * Gets the single instance of CollectionClient.
	 * 
	 * @return single instance of CollectionClient
	 */
	public static synchronized SecurityClient getInstance() {
		return INSTANCE;
	}
	
	public String toMD5(final String payload) throws NoSuchAlgorithmException{
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(payload.getBytes());

        byte byteData[] = md.digest();

        //convert the byte to hex format method 1
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < byteData.length; i++) {
         sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
        }
        return sb.toString();
	}

}

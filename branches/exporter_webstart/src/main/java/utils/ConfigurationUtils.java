package utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

// TODO: Auto-generated Javadoc
/**
 * The Class ConfigurationUtils.
 */
public class ConfigurationUtils {

	/** The instance. */
	private static ConfigurationUtils INSTANCE;

	/**
	 * Instantiates a new configuration utils.
	 */
	private ConfigurationUtils() {
		super();
	}

	/**
	 * Gets the single instance of ConfigurationUtils.
	 *
	 * @return single instance of ConfigurationUtils
	 */
	public static ConfigurationUtils getInstance() {
		if (INSTANCE == null)
			INSTANCE = new ConfigurationUtils();
		return INSTANCE;
	}

	/**
	 * Gets the configuration.
	 *
	 * @param path the path
	 * @return the configuration
	 */
	public Properties getConfiguration(final String path) {
		final Properties prop = new Properties();
		if (path != null) {
			try {
				final InputStream in = ConfigurationUtils.class.getResourceAsStream(("/" + path).replaceAll("/{2,}", "/"));
				if (path.endsWith(".properties"))
					prop.load(in);
				else if (path.endsWith(".xml"))
					prop.loadFromXML(in);
				in.close();
			} catch (final IOException e) {
				e.printStackTrace();
			}
		}
		return prop;
	}
}

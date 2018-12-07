/*
 * 
 */
package eu.dzhw.zofar.management.utils.system;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map.Entry;
import java.util.Properties;

import org.apache.commons.io.FileUtils;

/**
 * The Class ConfigurationUtils.
 */
public class ConfigurationUtils {

	/** The instance. */
	private static ConfigurationUtils INSTANCE;

	/**
	 * Instantiates a new configuration utils.
	 */
	protected ConfigurationUtils() {
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
	 * Gets the system configuration (system.properties from Resource Directory)
	 * 
	 * @return the system configuration
	 */
	public Properties getSystemConfiguration() {
		return this.getConfiguration("system.properties");
	}

	/**
	 * Gets the configuration located at path (XML or classical Properties
	 * format)
	 * 
	 * @param path
	 *            the path
	 * @return the configuration
	 */
	public Properties getConfiguration(final String path) {
		final Properties prop = new Properties();
		if (path != null) {
			try {
				final InputStream in = ConfigurationUtils.class.getResourceAsStream(("/" + path).replaceAll("/{2,}", "/"));
				if (in != null) {
					if (path.endsWith(".properties"))
						prop.load(in);
					else if (path.endsWith(".xml"))
						prop.loadFromXML(in);
					in.close();
				}
			} catch (final IOException e) {
				e.printStackTrace();
			}
		}
		return prop;
	}
	
	/**
	 * Gets the configuration located at path (XML or classical Properties
	 * format)
	 * 
	 * @param path
	 *            the path
	 * @return the configuration
	 */
	public Properties getConfigurationFromFileSystem(final String path) {
		final Properties prop = new Properties();
		if (path != null) {
			try {
				final InputStream in = FileUtils.openInputStream(new File(path.replaceAll("/{2,}", "/")));
				if (in != null) {
					if (path.endsWith(".properties"))
						prop.load(in);
					else if (path.endsWith(".xml"))
						prop.loadFromXML(in);
					in.close();
				}
			} catch (final IOException e) {
				e.printStackTrace();
			}
		}
		return prop;
	}

	public boolean saveConfiguration(final Properties props, final String path) {
//		DateFormat df = new SimpleDateFormat();
//		final String comment = "Modified " + df.format(new Date());
		FileOutputStream stream = null;
		try {
			stream = new FileOutputStream(path);
			if (path.endsWith(".properties"))props.store(stream, null);
			else if (path.endsWith(".xml"))
				props.storeToXML(stream, null);
			return true;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		finally{
			if(stream != null)
				try {
					stream.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
		return false;
	}
	
//	private void store(Properties props, String propertyFilePath)
//	        throws FileNotFoundException {
//
//	    PrintWriter pw = new PrintWriter(propertyFilePath);
//
//	    for (Enumeration e = props.propertyNames(); e.hasMoreElements();) {
//	        String key = (String) e.nextElement();
//	        pw.println(key + "=" + props.getProperty(key));
//	    }
//	    pw.close();
//	}
	
	public void show(Properties props){
		for(final Entry<Object, Object> pair : props.entrySet())System.out.println("Prop : "+pair.getKey()+" = "+pair.getValue());
	}
}

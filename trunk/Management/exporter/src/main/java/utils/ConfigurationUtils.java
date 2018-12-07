package utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigurationUtils {
	
	private static ConfigurationUtils INSTANCE;
	
	private ConfigurationUtils(){
		super();
	}
	
	public static ConfigurationUtils getInstance(){
		if(INSTANCE == null)INSTANCE = new ConfigurationUtils();
		return INSTANCE;
	}
	
	public Properties getConfiguration(final String path){
		final Properties prop = new Properties();
		if(path != null){
			try {
				final InputStream in = ConfigurationUtils.class.getResourceAsStream(("/"+path).replaceAll("/{2,}", "/"));
				if(path.endsWith(".properties"))prop.load(in);
				else if(path.endsWith(".xml"))prop.loadFromXML(in);
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return prop;
	}
}

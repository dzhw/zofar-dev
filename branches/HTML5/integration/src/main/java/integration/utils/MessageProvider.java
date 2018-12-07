package integration.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MessageProvider {
	
	private static final String WARN_PREFIX = "* ";
	private static final String INFO_PREFIX = "# ";
	private static final String ERROR_PREFIX = "! ";
	
	public static void warn(final Object source, final String message){
		final Logger LOGGER = LoggerFactory.getLogger(retrieveClass(source));
		LOGGER.warn(WARN_PREFIX+message);
	}
	
	public static void warn(final Object source, final String message,final Object arg1){
		final Logger LOGGER = LoggerFactory.getLogger(retrieveClass(source));
		LOGGER.warn(WARN_PREFIX+message,arg1);
	}
	
	public static void warn(final Object source, final String message,final Object arg1,final Object arg2){
		final Logger LOGGER = LoggerFactory.getLogger(retrieveClass(source));
		LOGGER.warn(WARN_PREFIX+message,arg1,arg2);
	}

	public static void info(final Object source, final String message){
		final Logger LOGGER = LoggerFactory.getLogger(retrieveClass(source));
		LOGGER.info(INFO_PREFIX+message);
	}
	
	public static void info(final Object source, final String message,final Object arg1){
		final Logger LOGGER = LoggerFactory.getLogger(retrieveClass(source));
		LOGGER.info(INFO_PREFIX+message,arg1);
	}
	
	public static void info(final Object source, final String message,final Object arg1,final Object arg2){
		final Logger LOGGER = LoggerFactory.getLogger(retrieveClass(source));
		LOGGER.info(INFO_PREFIX+message,arg1,arg2);
	}
	
	public static void error(final Object source, final String message){
		final Logger LOGGER = LoggerFactory.getLogger(retrieveClass(source));
		LOGGER.error(ERROR_PREFIX+message);
	}
	
	public static void error(final Object source, final String message,final Object arg1){
		final Logger LOGGER = LoggerFactory.getLogger(retrieveClass(source));
		LOGGER.error(ERROR_PREFIX+message,arg1);
	}
	
	public static void error(final Object source, final String message,final Object arg1,final Object arg2){
		final Logger LOGGER = LoggerFactory.getLogger(retrieveClass(source));
		LOGGER.error(ERROR_PREFIX+message,arg1,arg2);
	}
	
	public static void error(final Object source, final String message, final Throwable exception){
		final Logger LOGGER = LoggerFactory.getLogger(retrieveClass(source));
		LOGGER.error(ERROR_PREFIX+message,exception);
	}
	
	private static Class retrieveClass(final Object obj){
		if(obj == null)return null;
		if(obj instanceof Class)return (Class)obj;
		return obj.getClass();
	}

}

package de.his.zofar.presentation.surveyengine.listener.errorHandling.exception;

/**
 * Marker-Class for Custom Zofar Exceptions
 * 
 * @author meisner
 * 
 */
public class ZofarException extends RuntimeException {

	private static final long serialVersionUID = 2878261611252219708L;

	public ZofarException() {
		super();
	}

	public ZofarException(String message) {
		super(message);
	}

	public ZofarException(Throwable cause) {
		super(cause);
	}

	public ZofarException(String message, Throwable cause) {
		super(message, cause);
	}

	public ZofarException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}

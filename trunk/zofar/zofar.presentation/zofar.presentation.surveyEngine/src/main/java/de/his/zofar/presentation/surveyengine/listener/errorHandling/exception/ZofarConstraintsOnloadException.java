package de.his.zofar.presentation.surveyengine.listener.errorHandling.exception;

/**
 * Marker-Class for OnLoadExceptions
 * @author meisner
 * 
 */
public class ZofarConstraintsOnloadException extends ZofarException {

	private static final long serialVersionUID = 7236752707485927725L;

	public ZofarConstraintsOnloadException() {
		super();
	}

	public ZofarConstraintsOnloadException(String message) {
		super(message);
	}

	public ZofarConstraintsOnloadException(Throwable cause) {
		super(cause);
	}

	public ZofarConstraintsOnloadException(String message, Throwable cause) {
		super(message, cause);
	}

	public ZofarConstraintsOnloadException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}

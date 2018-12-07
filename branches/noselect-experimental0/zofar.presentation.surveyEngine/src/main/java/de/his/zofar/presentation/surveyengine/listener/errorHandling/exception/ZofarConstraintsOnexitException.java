package de.his.zofar.presentation.surveyengine.listener.errorHandling.exception;

/**
 * Marker-Class for OnExitExceptions
 * 
 * @author meisner
 * 
 */
public class ZofarConstraintsOnexitException extends ZofarException {

	private static final long serialVersionUID = 773538141642603774L;

	public ZofarConstraintsOnexitException() {
		super();
	}

	public ZofarConstraintsOnexitException(String message) {
		super(message);
	}

	public ZofarConstraintsOnexitException(Throwable cause) {
		super(cause);
	}

	public ZofarConstraintsOnexitException(String message, Throwable cause) {
		super(message, cause);
	}

	public ZofarConstraintsOnexitException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}

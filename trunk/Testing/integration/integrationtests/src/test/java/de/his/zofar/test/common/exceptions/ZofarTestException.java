package de.his.zofar.test.common.exceptions;

public class ZofarTestException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5777750835834065490L;

	public ZofarTestException() {
		super();
	}

	public ZofarTestException(String message) {
		super(message);
	}

	public ZofarTestException(Throwable cause) {
		super(cause);
	}

	public ZofarTestException(String message, Throwable cause) {
		super(message, cause);
	}

	public ZofarTestException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}

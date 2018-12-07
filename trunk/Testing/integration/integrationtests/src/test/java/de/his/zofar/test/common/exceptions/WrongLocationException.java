package de.his.zofar.test.common.exceptions;

public class WrongLocationException extends ZofarTestException {

	private static final long serialVersionUID = -2723267413626211371L;

	public WrongLocationException() {
		super();
	}

	public WrongLocationException(String message) {
		super(message);
	}

	public WrongLocationException(Throwable cause) {
		super(cause);
	}

	public WrongLocationException(String message, Throwable cause) {
		super(message, cause);
	}

	public WrongLocationException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}

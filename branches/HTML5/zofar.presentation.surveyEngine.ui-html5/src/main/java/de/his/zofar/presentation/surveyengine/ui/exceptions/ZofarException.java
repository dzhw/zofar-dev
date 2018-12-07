/**
 *
 */
package de.his.zofar.presentation.surveyengine.ui.exceptions;

/**
 * Marker-Class for Custom Zofar Exceptions
 *
 * @author meisner
 *
 */
public class ZofarException extends RuntimeException {

	private static final long serialVersionUID = 2878261611252219708L;

	// public ZofarException() {
	// super();
	// }
	//
	// public ZofarException(final String message) {
	// super(message);
	// }

	public ZofarException(final Throwable cause) {
		super(cause);
	}

	// public ZofarException(final String message, final Throwable cause) {
	// super(message, cause);
	// }
	//
	// public ZofarException(final String message, final Throwable cause,
	// final boolean enableSuppression, final boolean writableStackTrace) {
	// super(message, cause, enableSuppression, writableStackTrace);
	// }

}

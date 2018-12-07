/**
 *
 */
package de.his.zofar.presentation.surveyengine.ui.exceptions;

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

    public ZofarConstraintsOnexitException(final String message) {
        super(message);
    }

    public ZofarConstraintsOnexitException(final Throwable cause) {
        super(cause);
    }

    public ZofarConstraintsOnexitException(final String message,
            final Throwable cause) {
        super(message, cause);
    }

    public ZofarConstraintsOnexitException(final String message,
            final Throwable cause, final boolean enableSuppression,
            final boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}

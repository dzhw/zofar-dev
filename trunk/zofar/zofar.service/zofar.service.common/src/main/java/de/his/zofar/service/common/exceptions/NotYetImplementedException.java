/**
 * 
 */
package de.his.zofar.service.common.exceptions;

/**
 * Exception to mark methods that are not yet implemented.
 * 
 * @author le
 * 
 */
public class NotYetImplementedException extends RuntimeException {

    /**
     * 
     */
    private static final long serialVersionUID = 6027851958586717939L;

    /**
     * Default constructor to create a RuntimeException with a 'Not yet
     * implemented!' message.
     */
    public NotYetImplementedException() {
        super("Not yet implemented!");
    }

}

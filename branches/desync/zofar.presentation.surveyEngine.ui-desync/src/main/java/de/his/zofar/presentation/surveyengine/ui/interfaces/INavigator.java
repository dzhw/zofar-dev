/**
 *
 */
package de.his.zofar.presentation.surveyengine.ui.interfaces;

import java.util.Stack;

/**
 * @author le
 *
 */
public interface INavigator {

    enum DirectionType {
        UNKOWN, SAME, FORWARD, BACKWARD
    }

    /**
     * Method to retrieve next-to-last visited page
     * 
     * @return next-to-last Page-Name
     */
    public abstract String getBackwardViewID();

    /**
     * Method to retrieve current visited page (needed by onexit-validation
     * Exception handling)
     * 
     * @return current Page-Name
     */
    public abstract String getSameViewID();

    /**
     * @return true if current Movement Direction is backward
     */
    public abstract boolean isBackward();

    /**
     * @return true if current Movement Direction is forward
     */
    public abstract boolean isForward();

    /**
     * Method used for render-decision of backward-Button
     * 
     * @return true, if current history is empty
     */
    public abstract boolean isHistoryEmpty();

    /**
     * @return true if current Movement Direction is same
     */
    public abstract boolean isSame();

    /**
     * Randomly pick a view and register the chosen viewId.
     * 
     * @param randomOnce
     *            true if we only need to shuffle once
     * @param viewIds
     *            List of viewIds, seperated by comma.
     * @return The randomly chosen viewId.
     */
    public abstract String pickAndSendViewID(boolean randomOnce, String viewIds);

    /**
     * Register viewId of current requested page and redirect to last valid page
     * if applicable
     * 
     * @param viewId
     */
    public abstract void registerViewID(String viewId);

    /**
     * Register viewId of page, which was sended by effective Navigation-case
     * 
     * @param viewId
     * @return viewId without File-Suffix and trailing /
     */
    public abstract String sendViewID(String viewId);

    public abstract Stack<String> getMovements();
}

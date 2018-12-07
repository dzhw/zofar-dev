/**
 *
 */
package de.his.zofar.presentation.surveyengine.ui.interfaces;

import javax.faces.component.NamingContainer;

/**
 * interfaces to describe a response domain in Zofar from which all response
 * domains should derived from.
 *
 * all children of a response domain gets client ids which contains the client
 * id of the response domain. @see NamingContainer.
 *
 * @author le
 *
 */
public interface IResponseDomain extends NamingContainer {

    public static final String COMPONENT_FAMILY = "org.zofar.ResponseDomain";
    public static final String ITEM_CLASSES = "zo-odd zo-even";

    /**
     * sets the CSS classes for each item to enable zebra pattern. multiple
     * classes must separated by a blank.
     *
     * @param itemClasses
     */
    public abstract void setItemClasses(String itemClasses);

    /**
     * returns the CSS classes for the items. multiple classes are separated by
     * a blank.
     *
     * @return
     */
    public abstract String getItemClasses();

}
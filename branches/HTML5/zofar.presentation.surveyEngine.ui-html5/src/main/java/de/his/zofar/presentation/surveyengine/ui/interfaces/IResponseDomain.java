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
}
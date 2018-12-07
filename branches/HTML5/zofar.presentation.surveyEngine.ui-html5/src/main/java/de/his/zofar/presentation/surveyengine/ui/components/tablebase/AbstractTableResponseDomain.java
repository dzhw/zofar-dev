/**
 *
 */
package de.his.zofar.presentation.surveyengine.ui.components.tablebase;

import javax.faces.component.UINamingContainer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.his.zofar.presentation.surveyengine.ui.components.common.Identificational;
import de.his.zofar.presentation.surveyengine.ui.interfaces.IResponseDomain;

/**
 * abstract custom component for all response domain that rendered in a table
 * with matrix items as rows.
 *
 * functionality that are included are: * sorting * zebra rendering of items
 *
 * @author le
 *
 */

@Deprecated
public abstract class AbstractTableResponseDomain extends UINamingContainer implements Identificational, IResponseDomain {

	public static final String COMPONENT_FAMILY = "org.zofar.MatrixResponseDomain";

	/**
	 * LOGGER
	 */
	@SuppressWarnings("unused")
	private static final Logger LOGGER = LoggerFactory.getLogger(AbstractTableResponseDomain.class);

	/**
	 *
	 */
	public AbstractTableResponseDomain() {
		super();
	}

	@Override
	public String getFamily() {
		return COMPONENT_FAMILY;
	}
}

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
public abstract class AbstractTableResponseDomain extends UINamingContainer implements Identificational, IResponseDomain {

	public static final String COMPONENT_FAMILY = "org.zofar.MatrixResponseDomain";

    /**
     * the HTML class. children of this class should set it own HTML classes
     * with setter of this member.
     */
//    private String htmlTableClassName = DEFAULT_TABLE_CLASS_NAME;

    /**
     * LOGGER
     */
    @SuppressWarnings("unused")
    private static final Logger LOGGER = LoggerFactory
            .getLogger(AbstractTableResponseDomain.class);

    /**
     *
     */
    public AbstractTableResponseDomain() {
        super();
//        this.setRendererType(null);
    }
    
	@Override
	public String getFamily() {
		return COMPONENT_FAMILY;
	}
    
	@Override
	@Deprecated
	public String getUID() {
		return this.getId();
	}

}

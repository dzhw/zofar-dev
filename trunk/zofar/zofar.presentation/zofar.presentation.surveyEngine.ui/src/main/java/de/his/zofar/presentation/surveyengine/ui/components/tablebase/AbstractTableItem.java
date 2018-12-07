/**
 *
 */
package de.his.zofar.presentation.surveyengine.ui.components.tablebase;

import javax.faces.component.UINamingContainer;

import de.his.zofar.presentation.surveyengine.ui.components.common.Identificational;
import de.his.zofar.presentation.surveyengine.ui.components.common.Visible;

/**
 * abstract custom component to be used as item in the
 * AbstractTableResponseDomain.
 *
 * @author le
 *
 */
public abstract class AbstractTableItem extends UINamingContainer implements Identificational,Visible {

	public static final String COMPONENT_FAMILY = "org.zofar.MatrixItem";
//    private static final String TABLE_CELL = "td";
//    private static final String HEADER_FACET = "header";

//    private boolean isQuestionColumnEnabled = true;

    /**
     *
     */
    public AbstractTableItem() {
        super();
//        setRendererType(null);
    }
    
	@Override
	public String getFamily() {
		return COMPONENT_FAMILY;
	}

	@Override
	public abstract String getRendererType();

    
	@Override
	@Deprecated
	public String getUID() {
		return this.getId();
	}

	@Override
	@Deprecated
	public Boolean visibleCondition() {
		return this.isRendered();
	}
}

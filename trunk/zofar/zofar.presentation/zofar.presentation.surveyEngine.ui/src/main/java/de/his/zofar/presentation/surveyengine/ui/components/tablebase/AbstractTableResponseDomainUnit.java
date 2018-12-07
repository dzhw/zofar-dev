/**
 *
 */
package de.his.zofar.presentation.surveyengine.ui.components.tablebase;

import javax.faces.component.UINamingContainer;

import de.his.zofar.presentation.surveyengine.ui.components.common.Identificational;
import de.his.zofar.presentation.surveyengine.ui.components.common.Visible;
import de.his.zofar.presentation.surveyengine.ui.renderer.matrix.ZofarMatrixUnitRenderer;

/**
 * abstract custom component to be used as unit in the
 * AbstractTableResponseDomain.
 *
 * @author le
 *
 */
public abstract class AbstractTableResponseDomainUnit  extends UINamingContainer implements Identificational,Visible {
//	private static final String QUESTION_COLUMN = "questionColumn";
	
	public static final String COMPONENT_FAMILY = "org.zofar.MatrixUnit";
//	/**
//	 * the possible row classes of the parent single choice matrix response
//	 * domain. it is set by the parent.
//	 */
//	private String[] rowClassesArray;

	/**
	 * the current index in the parent single choice matrix response domain.
	 */
//	private int rowIndex = 0;

	/**
     *
     */
	public AbstractTableResponseDomainUnit() {
		super();
//		setRendererType(null);
	}
	
	@Override
	public String getFamily() {
		return COMPONENT_FAMILY;
	}

	@Override
	public String getRendererType() {
		return ZofarMatrixUnitRenderer.RENDERER_TYPE;
	}
	
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

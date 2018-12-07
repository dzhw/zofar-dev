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

@Deprecated
public abstract class AbstractTableResponseDomainUnit extends UINamingContainer implements Identificational, Visible {

	public static final String COMPONENT_FAMILY = "org.zofar.MatrixUnit";

	public AbstractTableResponseDomainUnit() {
		super();
	}

	@Override
	public String getFamily() {
		return COMPONENT_FAMILY;
	}

	@Override
	public String getRendererType() {
		return ZofarMatrixUnitRenderer.RENDERER_TYPE;
	}
}

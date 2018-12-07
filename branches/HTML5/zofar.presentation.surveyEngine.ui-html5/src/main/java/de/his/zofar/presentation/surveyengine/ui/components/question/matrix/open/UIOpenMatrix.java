/**
 *
 */
package de.his.zofar.presentation.surveyengine.ui.components.question.matrix.open;

import javax.faces.component.FacesComponent;

import de.his.zofar.presentation.surveyengine.ui.components.common.Identificational;
import de.his.zofar.presentation.surveyengine.ui.components.common.Visible;
import de.his.zofar.presentation.surveyengine.ui.components.question.matrix.UIMatrix;
import de.his.zofar.presentation.surveyengine.ui.renderer.matrix.open.ZofarOpenMatrixRenderer;

/**
 * @author le
 *
 */
@FacesComponent("org.zofar.OpenMatrix")
public class UIOpenMatrix extends UIMatrix implements Identificational, Visible {

	public static final String COMPONENT_FAMILY = "org.zofar.OpenMatrix";

	public UIOpenMatrix() {
		super();
	}

	@Override
	public String getRendererType() {
		return ZofarOpenMatrixRenderer.RENDERER_TYPE;
	}
}

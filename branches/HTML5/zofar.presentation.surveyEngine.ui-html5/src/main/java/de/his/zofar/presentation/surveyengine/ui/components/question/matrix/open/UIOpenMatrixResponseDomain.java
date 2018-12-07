/**
 *
 */
package de.his.zofar.presentation.surveyengine.ui.components.question.matrix.open;

import javax.faces.component.FacesComponent;

import de.his.zofar.presentation.surveyengine.ui.components.question.matrix.UIMatrixResponseDomain;
import de.his.zofar.presentation.surveyengine.ui.renderer.answers.matrix.open.responsedomain.ZofarOpenMatrixResponseDomainRenderer;

/**
 * @author le
 *
 */
@FacesComponent("org.zofar.OpenMatrixResponseDomain")
public class UIOpenMatrixResponseDomain extends UIMatrixResponseDomain {

	public UIOpenMatrixResponseDomain() {
		super();
	}

	@Override
	public String getRendererType() {
		return ZofarOpenMatrixResponseDomainRenderer.RENDERER_TYPE;
	}
}

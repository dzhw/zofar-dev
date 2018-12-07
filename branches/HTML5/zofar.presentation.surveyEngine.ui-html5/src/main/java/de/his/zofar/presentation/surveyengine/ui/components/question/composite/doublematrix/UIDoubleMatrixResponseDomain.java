/**
 *
 */
package de.his.zofar.presentation.surveyengine.ui.components.question.composite.doublematrix;

import javax.faces.component.FacesComponent;

import de.his.zofar.presentation.surveyengine.ui.components.question.matrix.UIMatrixResponseDomain;
import de.his.zofar.presentation.surveyengine.ui.renderer.matrix.doubleMatrix.ZofarDoubleMatrixResponseDomainRenderer;

/**
 * @author le
 *
 */
@FacesComponent(value = "org.zofar.DoubleMatrixResponseDomain")
public class UIDoubleMatrixResponseDomain extends UIMatrixResponseDomain {

	public static final String COMPONENT_FAMILY = "org.zofar.DoubleMatrixResponseDomain";

	public UIDoubleMatrixResponseDomain() {
		super();
	}

	@Override
	public String getFamily() {
		return COMPONENT_FAMILY;
	}

	@Override
	public String getRendererType() {
		return ZofarDoubleMatrixResponseDomainRenderer.RENDERER_TYPE;
	}

}

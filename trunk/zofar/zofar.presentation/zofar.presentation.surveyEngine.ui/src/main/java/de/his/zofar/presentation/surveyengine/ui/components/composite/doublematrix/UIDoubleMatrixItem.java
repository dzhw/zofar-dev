/**
 *
 */
package de.his.zofar.presentation.surveyengine.ui.components.composite.doublematrix;

import javax.faces.component.FacesComponent;
import javax.faces.component.UINamingContainer;

import de.his.zofar.presentation.surveyengine.ui.components.common.Identificational;
import de.his.zofar.presentation.surveyengine.ui.components.common.Visible;
import de.his.zofar.presentation.surveyengine.ui.renderer.matrix.doubleMatrix.ZofarDoubleMatrixItemRenderer;

/**
 * @author le
 *
 */
@FacesComponent(value = "org.zofar.DoubleMatrixItem")
public class UIDoubleMatrixItem extends UINamingContainer implements Identificational,Visible {

	public static final String COMPONENT_FAMILY = "org.zofar.DoubleMatrixItem";

    /**
     *
     */
    public UIDoubleMatrixItem() {
        super();
//        this.setRendererType(null);
    }
    
    @Override
    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    @Override
    public String getRendererType() {
        return ZofarDoubleMatrixItemRenderer.RENDERER_TYPE;
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

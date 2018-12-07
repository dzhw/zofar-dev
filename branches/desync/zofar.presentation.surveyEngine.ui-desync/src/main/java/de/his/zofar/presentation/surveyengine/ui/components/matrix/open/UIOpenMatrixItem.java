/**
 *
 */
package de.his.zofar.presentation.surveyengine.ui.components.matrix.open;

import javax.faces.component.FacesComponent;

import de.his.zofar.presentation.surveyengine.ui.components.tablebase.AbstractTableItem;
import de.his.zofar.presentation.surveyengine.ui.renderer.answers.matrix.open.options.ZofarOpenMatrixItemRenderer;

/**
 * @author meisner
 *
 */
@FacesComponent("org.zofar.OpenMatrixItem")
public class UIOpenMatrixItem extends AbstractTableItem {

    public UIOpenMatrixItem() {
        super();
    }
    
	@Override
	public String getRendererType() {
		return ZofarOpenMatrixItemRenderer.RENDERER_TYPE;
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

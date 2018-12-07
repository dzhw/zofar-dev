/**
 *
 */
package de.his.zofar.presentation.surveyengine.ui.components.composite.multiplechoice;

import javax.faces.component.FacesComponent;

import de.his.zofar.presentation.surveyengine.ui.components.common.Identificational;
import de.his.zofar.presentation.surveyengine.ui.components.common.Visible;
import de.his.zofar.presentation.surveyengine.ui.components.tablebase.AbstractTableItem;
import de.his.zofar.presentation.surveyengine.ui.renderer.answers.matrix.multiplechoice.options.ZofarMultipleChoiceMatrixItemRenderer;

/**
 * @author le
 *
 */
@FacesComponent("org.zofar.MultipleChoiceCompositeItem")
public class UIMultipleChoiceCompositeItem extends AbstractTableItem implements Identificational,Visible{

    public UIMultipleChoiceCompositeItem() {
        super();
    }

	@Override
	public String getRendererType() {
		return ZofarMultipleChoiceMatrixItemRenderer.RENDERER_TYPE;
	}

}

/**
 *
 */
package de.his.zofar.presentation.surveyengine.ui.components.matrix.singlechoice;

import javax.faces.component.FacesComponent;

import de.his.zofar.presentation.surveyengine.ui.components.tablebase.AbstractTableResponseDomainUnit;

/**
 * @author le
 *
 */
@FacesComponent("org.zofar.SingleChoiceMatrixResponseDomainUnit")
public class UISingleChoiceMatrixResponseDomainUnit extends
        AbstractTableResponseDomainUnit {

//    private boolean isDifferential;

    public UISingleChoiceMatrixResponseDomainUnit() {
        super();
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

//    @Override
//    public void encodeChildren(final FacesContext context) throws IOException {
////        SingleChoiceMatrixHelper.getInstance().setChildrenDifferential(
////                this.getChildren(), this.isDifferential());
//        super.encodeChildren(context);
//    }

//    public boolean isDifferential() {
//        return this.isDifferential;
//    }
//
//    public void setDifferential(final boolean isDifferential) {
//        this.isDifferential = isDifferential;
//    }

}

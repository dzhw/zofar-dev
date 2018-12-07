/**
 *
 */
package de.his.zofar.presentation.surveyengine.ui.components.matrix.singlechoice;

import javax.faces.component.FacesComponent;

import de.his.zofar.presentation.surveyengine.ui.components.tablebase.AbstractTableResponseDomain;
import de.his.zofar.presentation.surveyengine.ui.renderer.answers.matrix.singlechoice.responsedomain.ZofarDifferentialMatrixResponseDomainRenderer;
import de.his.zofar.presentation.surveyengine.ui.renderer.answers.matrix.singlechoice.responsedomain.ZofarSingleChoiceMatrixResponseDomainRenderer;

/**
 * represents the HTML table in which the single choice matrix items will be
 * rendered.
 *
 * this component is self rendered (doesn't have a dedicated renderer. for
 * now!).
 *
 * @author le
 *
 */
@FacesComponent("org.zofar.SingleChoiceMatrixResponseDomain")
public class UISingleChoiceMatrixResponseDomain extends
        AbstractTableResponseDomain {
	
	private enum PropertyKeys {
		itemClasses
	}

	@Override
	public void setItemClasses(String itemClasses) {
		getStateHelper().put(PropertyKeys.itemClasses, itemClasses);
	}

	@Override
	public String getItemClasses() {
		return (String) getStateHelper().eval(PropertyKeys.itemClasses);
	}

    public UISingleChoiceMatrixResponseDomain() {
        super();
    }
    
	@Override
	public String getRendererType() {
		if(isDifferential()){
			return ZofarDifferentialMatrixResponseDomainRenderer.RENDERER_TYPE;
		}
		else 
			return ZofarSingleChoiceMatrixResponseDomainRenderer.RENDERER_TYPE;
	}

	@Override
	@Deprecated
	public String getUID() {
		return this.getId();
	}

    /**
     * Does this Matrix question represent a Differential question.
     *
     * @return
     */
    public Boolean isDifferential() {
        Boolean isDifferential = false;
        if (this.getAttributes().get("isDifferential") != null) {
            isDifferential = (Boolean) this.getAttributes().get(
                    "isDifferential");
        }
        return isDifferential;
    }

    public Boolean isShowValues() {
        Boolean isShowValues = false;
        if (this.getAttributes().get("isShowValues") != null) {
            isShowValues = (Boolean) this.getAttributes().get("isShowValues");
        }
        return isShowValues;
    }
}

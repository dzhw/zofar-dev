/**
 *
 */
package de.his.zofar.presentation.surveyengine.ui.components.matrix.singlechoice;

import javax.faces.component.FacesComponent;
import javax.faces.component.UIComponent;

import de.his.zofar.presentation.surveyengine.ui.components.tablebase.AbstractTableItem;
import de.his.zofar.presentation.surveyengine.ui.components.tablebase.AbstractTableResponseDomain;
import de.his.zofar.presentation.surveyengine.ui.renderer.answers.matrix.singlechoice.options.ZofarDifferentialMatrixItemRenderer;
import de.his.zofar.presentation.surveyengine.ui.renderer.answers.matrix.singlechoice.options.ZofarSingleChoiceMatrixItemRenderer;

/**
 * @author le
 *
 */
@FacesComponent("org.zofar.SingleChoiceMatrixItem")
public class UISingleChoiceMatrixItem extends AbstractTableItem {
	


//    private boolean isDifferential = false;

    public UISingleChoiceMatrixItem() {
        super();
    }
    
	@Override
	public String getRendererType() {
		if(isDifferential()){
			return ZofarDifferentialMatrixItemRenderer.RENDERER_TYPE;
		}
		else return ZofarSingleChoiceMatrixItemRenderer.RENDERER_TYPE;
	}
	
	private Boolean isDifferential() {
		Boolean differential = false;
		UIComponent parent = this;
		while(!((AbstractTableResponseDomain.class).isAssignableFrom(parent.getClass())))parent = parent.getParent();
		
		if (parent.getAttributes().get("isDifferential") != null) {
			differential = (Boolean) parent
					.getAttributes().get("isDifferential");
		}
		
		return differential;
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

    /*
     * (non-Javadoc)
     *
     * @see
     * javax.faces.component.UIComponentBase#encodeChildren(javax.faces.context
     * .FacesContext)
     */
//    @Override
//    public void encodeChildren(final FacesContext context) throws IOException {
//        final ResponseWriter writer = context.getResponseWriter();
//
//        for (final UIComponent child : this.getChildren()) {
//            if (UISingleChoiceMatrixItemResponseDomain.class
//                    .isAssignableFrom(child.getClass())) {
//                // the response domain renders the <td/>
//                ((UISingleChoiceMatrixItemResponseDomain) child)
//                        .setDifferential(this.isDifferential());
//                child.encodeAll(context);
//            } else if (UIDropDownSingleChoiceResponseDomain.class.isAssignableFrom(child.getClass())) {
//            	((UIDropDownSingleChoiceResponseDomain)child).setIsInMatrix(true);
//            	child.encodeAll(context);
//            } else {
//                writer.startElement("td", this);
//                child.encodeAll(context);
//                writer.endElement("td");
//            }
//        }
//    }

//    public boolean isDifferential() {
//        return this.isDifferential;
//    }
//
//    public void setDifferential(final boolean isDifferential) {
//        this.isDifferential = isDifferential;
//    }

}

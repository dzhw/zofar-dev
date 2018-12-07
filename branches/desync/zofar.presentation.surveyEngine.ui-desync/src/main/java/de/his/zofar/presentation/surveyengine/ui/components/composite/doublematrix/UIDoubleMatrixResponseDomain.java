/**
 *
 */
package de.his.zofar.presentation.surveyengine.ui.components.composite.doublematrix;

import javax.faces.component.FacesComponent;
import javax.faces.component.UINamingContainer;

import de.his.zofar.presentation.surveyengine.ui.components.common.Identificational;
import de.his.zofar.presentation.surveyengine.ui.interfaces.IResponseDomain;
import de.his.zofar.presentation.surveyengine.ui.renderer.matrix.doubleMatrix.ZofarDoubleMatrixResponseDomainRenderer;

/**
 * @author le
 *
 */
@FacesComponent(value = "org.zofar.DoubleMatrixResponseDomain")
public class UIDoubleMatrixResponseDomain extends UINamingContainer implements IResponseDomain,Identificational {
    private enum PropertyKeys {
        showValues, itemClasses, missingSeparated, horizontal, labelPosition, alignAttached, direction
    }
    
    public static final String COMPONENT_FAMILY = "org.zofar.DoubleMatrixResponseDomain";
//    private String[] rowClasses;
//    private int rowIndex = 0;

    /**
     *
     */
    public UIDoubleMatrixResponseDomain() {
        super();
//        this.setRendererType(null);
    }
    
	@Override
	public String getFamily() {
		return COMPONENT_FAMILY;
	}

	@Override
	public String getRendererType() {
		return ZofarDoubleMatrixResponseDomainRenderer.RENDERER_TYPE;
	}
    
	@Override
	@Deprecated
	public String getUID() {
		return this.getId();
	}




//    private String[] getRowClassesArray() {
//        String[] rowClasses = null;
//        final String rowClassesRaw = (String) this.getAttributes().get(
//                "itemClasses");
//
//        if (rowClassesRaw == null || rowClassesRaw.isEmpty()) {
//            rowClasses = new String[0];
//        } else {
//            rowClasses = rowClassesRaw.trim().split("\\s* \\s*");
//        }
//
//        return rowClasses;
//    }

    /*
     * (non-Javadoc)
     *
     * @see
     * de.his.zofar.presentation.surveyengine.ui.interfaces.IResponseDomain#
     * setItemClasses(java.lang.String)
     */
    @Override
    public void setItemClasses(final String itemClasses) {
        getStateHelper().put(PropertyKeys.itemClasses, itemClasses);
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * de.his.zofar.presentation.surveyengine.ui.interfaces.IResponseDomain#
     * getItemClasses()
     */
    @Override
    public String getItemClasses() {
        return (String) getStateHelper().eval(PropertyKeys.itemClasses);
    }

}

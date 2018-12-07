package de.his.zofar.presentation.surveyengine.ui.components.display;

import javax.faces.component.FacesComponent;
import javax.faces.component.UINamingContainer;

import de.his.zofar.presentation.surveyengine.ui.components.common.Identificational;
/**
 * @author meisner
 *
 */
@FacesComponent(value = "org.zofar.display.header")
public class UIDisplayTableHeader extends UINamingContainer  implements Identificational {
	
	public static final String COMPONENT_FAMILY = "org.zofar.display.header";
	public UIDisplayTableHeader() {
		super();
	}
	
	@Override
	@Deprecated
	public String getUID() {
		return this.getId();
	}
	
    /*
     * (non-Javadoc)
     *
     * @see javax.faces.component.UIComponentBase#getRendererType()
     */
    @Override
    public String getRendererType() {
        return null;
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.faces.component.UIComponent#getFamily()
     */
    @Override
    public String getFamily() {
        return COMPONENT_FAMILY;
    }
    
    /*
     * (non-Javadoc)
     *
     * @see javax.faces.component.UIComponentBase#isTransient()
     */
    @Override
    public boolean isTransient() {
        return true;
    }

}

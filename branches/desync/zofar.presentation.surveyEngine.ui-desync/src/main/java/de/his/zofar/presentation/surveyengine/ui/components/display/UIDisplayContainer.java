package de.his.zofar.presentation.surveyengine.ui.components.display;

import javax.faces.component.FacesComponent;
import javax.faces.component.UINamingContainer;

import de.his.zofar.presentation.surveyengine.ui.components.common.Identificational;
import de.his.zofar.presentation.surveyengine.ui.renderer.display.ContainerRenderer;
/**
 * @author meisner
 *
 */
@FacesComponent(value = "org.zofar.display.container")
public class UIDisplayContainer extends UINamingContainer implements Identificational {
	
	public static final String COMPONENT_FAMILY = "org.zofar.display.container";

	public UIDisplayContainer() {
		super();
	}
	
	@Override
	@Deprecated
	public String getUID() {
		return this.getId();
	}
	
    @Override
    public String getRendererType() {
        return ContainerRenderer.RENDERER_TYPE;
    }

    @Override
    public String getFamily() {
        return COMPONENT_FAMILY;
    }
    
    @Override
    public boolean isTransient() {
        return true;
    }
}

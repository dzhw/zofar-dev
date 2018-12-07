package de.his.zofar.presentation.surveyengine.ui.components.display;

import javax.faces.component.FacesComponent;
import javax.faces.component.UINamingContainer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.his.zofar.presentation.surveyengine.ui.components.common.Identificational;
import de.his.zofar.presentation.surveyengine.ui.components.common.Visible;
import de.his.zofar.presentation.surveyengine.ui.renderer.display.TableRenderer;
/**
 * @author meisner
 *
 */
@FacesComponent(value = "org.zofar.display.table")
public class UIDisplayTable extends UINamingContainer implements Identificational,Visible {
	
	public static final String COMPONENT_FAMILY = "org.zofar.display.table";
	
	private static final Logger LOGGER = LoggerFactory
			.getLogger(UIDisplayTable.class);
	
	public UIDisplayTable() {
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
	
    /*
     * (non-Javadoc)
     *
     * @see javax.faces.component.UIComponentBase#getRendererType()
     */
    @Override
    public String getRendererType() {
//    	LOGGER.info("getRendererType {}",TableRenderer.RENDERER_TYPE);
        return TableRenderer.RENDERER_TYPE;
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.faces.component.UIComponent#getFamily()
     */
    @Override
    public String getFamily() {
//    	LOGGER.info("getFamily {}",UIDisplayTable.COMPONENT_FAMILY);
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

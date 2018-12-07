package de.his.zofar.presentation.surveyengine.ui.components.composite.calendar;

import javax.faces.component.FacesComponent;
import javax.faces.component.UINamingContainer;

import de.his.zofar.presentation.surveyengine.ui.components.common.Identificational;
import de.his.zofar.presentation.surveyengine.ui.components.common.Visible;
import de.his.zofar.presentation.surveyengine.ui.renderer.calendar.LegendRenderer;

/**
 * @author meisner
 *
 */
@FacesComponent(value = "de.his.zofar.Calendar.Legend")
public class UICalendarLegend extends UINamingContainer implements Identificational,Visible {

	public static final String COMPONENT_FAMILY = "de.his.zofar.Calendar.Legend";
	
//	private static final Logger LOGGER = LoggerFactory
//			.getLogger(UICalendarLegend.class);

	public UICalendarLegend() {
		super();
	}
	
    @Override
    public String getFamily() {
        return UICalendarLegend.COMPONENT_FAMILY;
    }

    @Override
    public String getRendererType() {
        return LegendRenderer.RENDERER_TYPE; 
    }

    public String getLayoutAttribute() {
        final String value = (String) getAttributes().get("layout");
        return value;
    }
    
    public String getPositionAttribute() {
        final String value = (String) getAttributes().get("position");
        return value;
    }
    
    public boolean getLegendIconAttribute() {
        final boolean value = (boolean) getAttributes().get("showLegendIcon");
        return value;
    }
    
    public boolean getIndicatorAttribute() {
        final boolean value = (boolean) getAttributes().get("showIndicator");
        return value;
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

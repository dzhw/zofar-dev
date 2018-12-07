package de.his.zofar.presentation.surveyengine.ui.components.composite.calendar;

import javax.faces.component.FacesComponent;
import javax.faces.component.UINamingContainer;

import de.his.zofar.presentation.surveyengine.ui.components.common.Identificational;
import de.his.zofar.presentation.surveyengine.ui.components.common.Visible;
import de.his.zofar.presentation.surveyengine.ui.renderer.calendar.TileRenderer;

/**
 * @author meisner
 *
 */
@FacesComponent(value = "de.his.zofar.Calendar.Tile")
public class UICalendarTile extends UINamingContainer implements Identificational,Visible {

	public static final String COMPONENT_FAMILY = "de.his.zofar.Calendar.Tile";

	public UICalendarTile() {
		super();
	}
	
  @Override
  public String getFamily() {
      return UICalendarTile.COMPONENT_FAMILY;
  }

  @Override
  public String getRendererType() {
      return TileRenderer.RENDERER_TYPE; 
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

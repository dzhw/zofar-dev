package de.his.zofar.presentation.surveyengine.ui.components.composite.calendar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.faces.component.FacesComponent;
import javax.faces.component.UINamingContainer;

import de.his.zofar.presentation.surveyengine.ui.components.common.Identificational;
import de.his.zofar.presentation.surveyengine.ui.components.common.Visible;
import de.his.zofar.presentation.surveyengine.ui.renderer.calendar.SheetRenderer;

/**
 * @author meisner
 *
 */
@FacesComponent(value = "de.his.zofar.Calendar.Sheet")
public class UICalendarSheet extends UINamingContainer implements Identificational,Visible{

	public static final String COMPONENT_FAMILY = "de.his.zofar.Calendar.Sheet";

	public UICalendarSheet() {
		super();
	}
	
    @Override
    public String getFamily() {
        return UICalendarSheet.COMPONENT_FAMILY;
    }

    @Override
    public String getRendererType() {
        return SheetRenderer.RENDERER_TYPE; 
    }

	public int getColumnCount(){
		return getColumnList().size();
	}
	
	public List<String> getColumnList(){
		final List<String> back = new ArrayList<String>();
		back.addAll(Arrays.asList(getColumnsAttribute().split(",")));
		return back;
	}
	
    public String getColumnsAttribute() {
        final String value = (String) getAttributes().get("columns");
        return value;
    }
    
	public int getRowCount(){
		return getRowList().size();
	}
	
	public List<String> getRowList(){
		final List<String> back = new ArrayList<String>();
		back.addAll(Arrays.asList(getRowsAttribute().split(",")));
		return back;
	}
	
    public String getRowsAttribute() {
        final String value = (String) getAttributes().get("rows");
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

package de.his.zofar.presentation.surveyengine.ui.components.common;

import java.util.Map;

import javax.faces.component.FacesComponent;
import javax.faces.component.UINamingContainer;

import de.his.zofar.presentation.surveyengine.ui.renderer.Progress.ProgressRenderer;

@FacesComponent("org.zofar.picturebar")
public class PictureBar extends UINamingContainer {

	public static final String COMPONENT_FAMILY = "org.zofar.picturebar";

	public PictureBar() {
		super();
	}

	@Override
	public String getRendererType() {
		return ProgressRenderer.RENDERER_TYPE;
	}

	@Override
	public String getFamily() {
		return COMPONENT_FAMILY;
	}

	@Override
	public boolean isTransient() {
		return true;
	}

	public Integer getValueAttribute() {
		final Integer value = (Integer) this.getAttributes().get("value");
		return value;
	}

	public String getDotAttribute() {
		final String value = (String) this.getAttributes().get("dot");
		return value;
	}

	public String getArrowAttribute() {
		final String value = (String) this.getAttributes().get("arrow");
		return value;
	}

	public Map<?, ?> getMilestonesAttribute() {
		final Map<?, ?> value = (Map<?, ?>) this.getAttributes().get("milestones");
		return value;
	}

	public de.his.zofar.presentation.surveyengine.ui.interfaces.INavigator getNavigatorAttribute() {
		final de.his.zofar.presentation.surveyengine.ui.interfaces.INavigator value = (de.his.zofar.presentation.surveyengine.ui.interfaces.INavigator) this.getAttributes().get("navigator");
		return value;
	}

}

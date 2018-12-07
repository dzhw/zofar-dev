package de.his.zofar.presentation.surveyengine.ui.components.buttons;

import javax.faces.component.FacesComponent;
import javax.faces.component.UINamingContainer;

import de.his.zofar.presentation.surveyengine.ui.renderer.buttons.BreadCrumbNavRenderer;
import de.his.zofar.presentation.surveyengine.ui.renderer.buttons.SideNavRenderer;

/**
 * @author meisner
 *
 */
@FacesComponent(value = "org.zofar.JumperContainer")
public class JumperContainer extends UINamingContainer {

	public static final String COMPONENT_FAMILY = "org.zofar.JumperContainer";

	/**
	 *
	 */
	public JumperContainer() {
		super();
	}

	public String getSeparatorAttribute() {
		final String back = (String) this.getStateHelper().get("separator");
		if (back != null) {
			return back;
		}
		return "UNKOWN";
	}

	@Override
	public String getFamily() {
		return COMPONENT_FAMILY;
	}

	@Override
	public String getRendererType() {
		final String separatorKey = (String) this.getAttributes().get("separator");
		if (separatorKey != null) {
			if (separatorKey.equalsIgnoreCase("side")) {
				return SideNavRenderer.RENDERER_TYPE;
			}
			if (separatorKey.equalsIgnoreCase("top")) {
				return BreadCrumbNavRenderer.RENDERER_TYPE;
			}
		}
		return null;
	}
}

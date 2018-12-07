/**
 *
 */
package de.his.zofar.presentation.surveyengine.ui.components.container;

import java.util.ArrayList;
import java.util.List;

import javax.faces.component.FacesComponent;
import javax.faces.component.UIComponent;
import javax.faces.component.UINamingContainer;

import de.his.zofar.presentation.surveyengine.ui.components.common.Identificational;
import de.his.zofar.presentation.surveyengine.ui.components.common.Visible;
import de.his.zofar.presentation.surveyengine.ui.renderer.container.SectionRenderer;
import de.his.zofar.presentation.surveyengine.ui.renderer.container.UnitRenderer;
import de.his.zofar.presentation.surveyengine.ui.util.JsfUtility;

/**
 * @author meisner
 *
 */
@FacesComponent(value = "org.zofar.Section")
public class Section extends UINamingContainer implements Identificational, Visible {

	public static final String COMPONENT_FAMILY = "org.zofar.Section";
	private String injectedClasses;

	public Section() {
		super();
	}

	@Override
	public String getFamily() {
		return COMPONENT_FAMILY;
	}

	@Override
	public String getRendererType() {
		final UIComponent rdc = JsfUtility.getInstance().getParentResponseDomain(this);
		if(rdc != null)	return UnitRenderer.RENDERER_TYPE;
		else return SectionRenderer.RENDERER_TYPE;
	}
	
	public boolean getPageAttribute() {
		return Boolean.valueOf(this.getAttributes().get("page")+"");
	}
	public boolean getAccordionAttribute() {
		return Boolean.valueOf(this.getAttributes().get("isaccordion")+"");
	}

	public String getInjectedClasses() {
		return injectedClasses;
	}

	public void setInjectedClasses(String injectedClasses) {
		this.injectedClasses = injectedClasses;
	}

	/**
	 * this methods returns a list of composite components children, which are
	 * the actual children. getChildren() returns an empty list.
	 *
	 * @return the actual children of the section
	 */
	public List<UIComponent> getContent() {
		List<UIComponent> content;
		// if the are no children the content is the section
		if (this.getFacet(COMPOSITE_FACET_NAME).getChildren().isEmpty()) {
			content = new ArrayList<UIComponent>();
			content.add(this);
		} else {
			content = this.getFacet(COMPOSITE_FACET_NAME).getChildren();
		}
		return content;
	}
}

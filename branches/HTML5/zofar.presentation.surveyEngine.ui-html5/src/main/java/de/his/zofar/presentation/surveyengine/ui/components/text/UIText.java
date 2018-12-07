/**
 *
 */
package de.his.zofar.presentation.surveyengine.ui.components.text;

import javax.faces.component.UINamingContainer;
import javax.faces.component.html.HtmlPanelGroup;

import de.his.zofar.presentation.surveyengine.ui.components.common.Identificational;
import de.his.zofar.presentation.surveyengine.ui.components.common.Visible;
import de.his.zofar.presentation.surveyengine.ui.renderer.text.ZofarTextRenderer;
import de.his.zofar.presentation.surveyengine.ui.util.JsfUtility;

/**
 * base component for all text components.
 *
 * @author le
 *
 */
public abstract class UIText extends UINamingContainer implements Identificational, Visible {

	public static final String COMPONENT_FAMILY = "org.zofar.Text";

	public UIText() {
		super();
	}

	public String getContent() {
		final HtmlPanelGroup panel = (HtmlPanelGroup) this.getFacet(COMPOSITE_FACET_NAME).getChildren().get(0);
		String content;
		if (this.isRendered()) {
			content = panel.getChildren().get(0).toString();
		} else {
			content = "";
		}

		return content;
	}
	
	public boolean getContainerAttribute(){
		return Boolean.parseBoolean(JsfUtility.getInstance().getAttribute(this,"container", true+""));
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see javax.faces.component.UIComponentBase#getRendererType()
	 */
	@Override
	public String getRendererType() {
		return ZofarTextRenderer.RENDERER_TYPE;
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

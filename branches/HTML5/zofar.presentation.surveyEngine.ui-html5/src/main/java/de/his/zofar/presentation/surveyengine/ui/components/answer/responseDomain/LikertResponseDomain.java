package de.his.zofar.presentation.surveyengine.ui.components.answer.responseDomain;

import javax.faces.component.FacesComponent;
import javax.faces.component.UIInput;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.his.zofar.presentation.surveyengine.ui.components.common.Alignable;
import de.his.zofar.presentation.surveyengine.ui.components.common.Identificational;
import de.his.zofar.presentation.surveyengine.ui.interfaces.IResponseDomain;
import de.his.zofar.presentation.surveyengine.ui.renderer.answers.likert.responsedomain.ZofarHorizontalLikertResponseDomainRenderer;
import de.his.zofar.presentation.surveyengine.ui.renderer.answers.likert.responsedomain.ZofarVerticalLikertResponseDomainRenderer;

/**
 * Renders a Likertscale. Each answer option will be rendered as a radio button.
 *
 * @author Reitmann
 */
@FacesComponent(value = "org.zofar.LikertResponseDomain")
public class LikertResponseDomain extends UIInput implements IResponseDomain, Identificational, Alignable {

	public static final String COMPONENT_FAMILY = "org.zofar.LikertResponseDomain";

	public static final String CSS_CLASS_DELIM = ",";

	@SuppressWarnings("unused")
	private static final Logger LOGGER = LoggerFactory.getLogger(LikertResponseDomain.class);

	public LikertResponseDomain() {
		super();
		// we render everything ourselves
		// this.setRendererType(null);
	}

	@Override
	public String getFamily() {
		return COMPONENT_FAMILY;
	}

	@Override
	public String getRendererType() {
		final Boolean isHorizontal = ((String) this.getAttributes().get("direction")).equals("horizontal");
		if (isHorizontal) {
			return ZofarHorizontalLikertResponseDomainRenderer.RENDERER_TYPE;
		} else {
			return ZofarVerticalLikertResponseDomainRenderer.RENDERER_TYPE;
		}
	}

	@Override
	public String getDirection() {
		return (String) this.getStateHelper().eval("direction");
	}

	@Override
	public void setDirection(final String direction) {
		this.getStateHelper().put("direction", direction);
	}
}

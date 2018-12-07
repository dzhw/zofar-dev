/**
 *
 */
package de.his.zofar.presentation.surveyengine.ui.renderer.answers.singechoice.responsedomain;

import java.util.Map;

import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.his.zofar.presentation.surveyengine.ui.renderer.answers.ZofarResponseDomainRenderer;

/**
 * @author le
 *
 */
public abstract class ZofarSingleChoiceResponseDomainRenderer extends ZofarResponseDomainRenderer {

	private static final Logger LOGGER = LoggerFactory.getLogger(ZofarSingleChoiceResponseDomainRenderer.class);

	@Override
	public void decode(final FacesContext context, final UIComponent component) {
		final Map<String, String> paramMap = context.getExternalContext().getRequestParameterMap();
		final String clientId = component.getClientId(context);
		if (paramMap.containsKey(clientId)) {
			// request parameter found, set submitted value
			((UIInput) component).setValue(paramMap.get(clientId));
		} else {
			// see reason for this action at decodeUISelectMany
			((UIInput) component).setValue("");
		}
	}

}

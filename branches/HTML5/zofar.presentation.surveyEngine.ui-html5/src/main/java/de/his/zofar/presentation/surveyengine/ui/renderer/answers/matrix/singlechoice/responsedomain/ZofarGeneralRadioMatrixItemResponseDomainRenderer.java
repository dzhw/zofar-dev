package de.his.zofar.presentation.surveyengine.ui.renderer.answers.matrix.singlechoice.responsedomain;

import java.util.Map;

import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.his.zofar.presentation.surveyengine.ui.renderer.answers.ZofarResponseDomainRenderer;

/**
 * @author meisner
 *
 */
public abstract class ZofarGeneralRadioMatrixItemResponseDomainRenderer extends ZofarResponseDomainRenderer {

	private static final Logger LOGGER = LoggerFactory.getLogger(ZofarGeneralRadioMatrixItemResponseDomainRenderer.class);

	public ZofarGeneralRadioMatrixItemResponseDomainRenderer() {
		super();
	}

	/**
	 * this class renders its children.
	 *
	 * @see javax.faces.component.UIComponentBase#getRendersChildren()
	 */
	@Override
	public boolean getRendersChildren() {
		return true;
	}

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

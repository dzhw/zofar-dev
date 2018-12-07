/**
 *
 */
package de.his.zofar.presentation.surveyengine.ui.components.answer.options;

import javax.faces.component.FacesComponent;
import javax.faces.component.UINamingContainer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.his.zofar.presentation.surveyengine.ui.components.common.Identificational;
import de.his.zofar.presentation.surveyengine.ui.components.common.Visible;
import de.his.zofar.presentation.surveyengine.ui.interfaces.IAnswerOption;
import de.his.zofar.presentation.surveyengine.ui.renderer.answers.open.options.ZofarTextOpenOptionRenderer;

/**
 * @author meisner
 *
 */
@FacesComponent(value = "org.zofar.OpenOption")
public class OpenOption extends UINamingContainer implements Identificational, Visible,IAnswerOption {
	
	public static final String COMPONENT_FAMILY = "org.zofar.OpenOption";

	private static final Logger LOGGER = LoggerFactory.getLogger(OpenOption.class);

	private Object sequenceId;

	public OpenOption() {
		super();
	}
	
	@Override
	public String getFamily() {
		return COMPONENT_FAMILY;
	}
	
	@Override
	public String getRendererType() {
		String rendererType = ZofarTextOpenOptionRenderer.RENDERER_TYPE;;
		return rendererType;
	}
	
	@Override
	public void setSequenceId(Object id) {
		this.sequenceId = id;
	}

	@Override
	public Object getSequenceId() {
		return sequenceId;
	}
}

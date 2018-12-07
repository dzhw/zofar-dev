/**
 *
 */
package de.his.zofar.presentation.surveyengine.ui.components.answer.options;

import javax.faces.component.FacesComponent;
import javax.faces.component.UINamingContainer;

import de.his.zofar.presentation.surveyengine.ui.components.common.Identificational;
import de.his.zofar.presentation.surveyengine.ui.components.common.Visible;
import de.his.zofar.presentation.surveyengine.ui.interfaces.IAnswerOption;
import de.his.zofar.presentation.surveyengine.ui.renderer.answers.open.options.ZofarAttachedOpenRenderer;

/**
 * @author le
 *
 */
@FacesComponent(value = "org.zofar.AttachedOpenQuestion")
public class UIAttachedOpenQuestion extends UINamingContainer implements Identificational, Visible,IAnswerOption {
	
	public static final String COMPONENT_FAMILY = "org.zofar.AttachedOpenQuestion";
	private Object sequenceId;

	public UIAttachedOpenQuestion() {
		super();
	}
	
	@Override
	public String getFamily() {
		return COMPONENT_FAMILY;
	}

	@Override
	public String getRendererType() {
		return ZofarAttachedOpenRenderer.RENDERER_TYPE;
	}
	
	public String getCompleteInputId() {
		final String inputId = (String) this.getAttributes().get("inputId");
		return this.getClientId() + ":" + inputId;
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

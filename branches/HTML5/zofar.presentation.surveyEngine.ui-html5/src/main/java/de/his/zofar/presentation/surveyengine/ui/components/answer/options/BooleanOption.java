/**
 *
 */
package de.his.zofar.presentation.surveyengine.ui.components.answer.options;

import javax.faces.component.FacesComponent;
import javax.faces.component.UINamingContainer;

import de.his.zofar.presentation.surveyengine.ui.components.common.Identificational;
import de.his.zofar.presentation.surveyengine.ui.components.common.Visible;
import de.his.zofar.presentation.surveyengine.ui.interfaces.IAnswerOption;

/**
 * @author meisner
 *
 */
@FacesComponent(value = "org.zofar.BooleanOption")
public class BooleanOption extends UINamingContainer implements Identificational, Visible,IAnswerOption {

	private Object sequenceId;

	public BooleanOption() {
		super();
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

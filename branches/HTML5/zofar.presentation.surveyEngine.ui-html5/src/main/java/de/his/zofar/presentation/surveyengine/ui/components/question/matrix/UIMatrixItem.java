/**
 *
 */
package de.his.zofar.presentation.surveyengine.ui.components.question.matrix;

import javax.faces.component.UINamingContainer;

import de.his.zofar.presentation.surveyengine.ui.components.common.Identificational;
import de.his.zofar.presentation.surveyengine.ui.components.common.Visible;
import de.his.zofar.presentation.surveyengine.ui.interfaces.ISequence;

/**
 * abstract custom component to be used as item in a Matrix
 * 
 *
 * @author Meisner
 *
 */
public abstract class UIMatrixItem extends UINamingContainer implements Identificational, Visible, ISequence {

	public static final String COMPONENT_FAMILY = "org.zofar.MatrixItem";

	private Object sequenceId;

	/**
	 *
	 */
	public UIMatrixItem() {
		super();
	}

	@Override
	public String getFamily() {
		return COMPONENT_FAMILY;
	}

	@Override
	public abstract String getRendererType();

	@Override
	public void setSequenceId(Object id) {
		this.sequenceId = id;
	}

	@Override
	public Object getSequenceId() {
		return sequenceId;
	}

}

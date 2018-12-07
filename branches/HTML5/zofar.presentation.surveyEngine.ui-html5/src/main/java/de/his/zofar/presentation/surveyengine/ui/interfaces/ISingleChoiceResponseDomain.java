/**
 *
 */
package de.his.zofar.presentation.surveyengine.ui.interfaces;

import javax.faces.component.EditableValueHolder;

/**
 * describes a single choice response domain. that type of response domain is a
 * EditableValueHolder therefore is responsible to persist values.
 *
 * @author le
 *
 */
public interface ISingleChoiceResponseDomain extends IResponseDomain, EditableValueHolder {

	void setShowValues(boolean showValues);

	boolean isShowValues();

}

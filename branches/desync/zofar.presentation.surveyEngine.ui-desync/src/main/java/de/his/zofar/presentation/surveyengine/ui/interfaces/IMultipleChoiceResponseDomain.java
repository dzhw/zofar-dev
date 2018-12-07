/**
 *
 */
package de.his.zofar.presentation.surveyengine.ui.interfaces;

import javax.faces.component.EditableValueHolder;

/**
 * describes a single choice response domain. that type of response domain is a
 * EditableValueHolder therefore is responsible to persist values.
 *
 * @author dick
 *
 */
public interface IMultipleChoiceResponseDomain extends IResponseDomain {

    void setShowValues(boolean showValues);

    boolean isShowValues();

}
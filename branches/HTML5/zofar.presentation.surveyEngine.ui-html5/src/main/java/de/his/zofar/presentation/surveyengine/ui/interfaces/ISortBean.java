/**
 *
 */
package de.his.zofar.presentation.surveyengine.ui.interfaces;

import java.util.List;

import javax.faces.component.UIComponent;

/**
 * @author le
 *
 */
public interface ISortBean {

	List<UIComponent> sort(List<UIComponent> toSort, final String parentId, final String mode);

}

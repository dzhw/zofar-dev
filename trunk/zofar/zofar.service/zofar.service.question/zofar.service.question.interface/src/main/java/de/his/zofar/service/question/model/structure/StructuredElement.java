/**
 *
 */
package de.his.zofar.service.question.model.structure;

import de.his.zofar.service.common.model.conditionable.impl.Visibility;

/**
 * @author le
 *
 */
public interface StructuredElement extends Visibility {
    public Object getContent();
}

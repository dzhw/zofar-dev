/**
 *
 */
package de.his.zofar.service.common.internal;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author le
 *
 */
@Transactional(propagation = Propagation.MANDATORY)
public interface InternalServiceInterface {

}

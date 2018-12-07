/**
 *
 */
package de.his.zofar.service.common.mapper.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import de.his.zofar.persistence.common.entities.BaseEntity;
import de.his.zofar.service.common.mapper.DTOMappingBuilder;

/**
 * Mappings for classes marked with this annotation will be added to dozer
 * automatically.
 *
 * @author reitmann
 * @see DTOMappingBuilder
 */
@Documented
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface DTOEntityMapping {

    /**
     * @return The other side of the bidirectional mapping. Currently only
     *         models formally known as DTOs.
     */
    Class<? extends BaseEntity> entity();

}

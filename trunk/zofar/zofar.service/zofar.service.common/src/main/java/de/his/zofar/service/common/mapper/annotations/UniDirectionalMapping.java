/**
 *
 */
package de.his.zofar.service.common.mapper.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.dozer.BeanFactory;

import de.his.zofar.service.common.mapper.DTOMappingBuilder;

/**
 * Unidirectional mappings for classes marked with this annotation will be added
 * to dozer automatically.
 *
 * @author reitmann
 * @see DTOMappingBuilder
 */
@Documented
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface UniDirectionalMapping {

    /**
     * Optional annotation fields need a default value. Sometimes java s...!
     *
     * @author Reitmann
     *
     */
    class DummyFactory implements BeanFactory {

        private DummyFactory() {}

        @Override
        public Object createBean(final Object source,
                final Class<?> sourceClass, final String targetBeanId) {
            throw new RuntimeException("Should never be called!");
        }

    }

    /**
     * @return The destination side of the unidirectional mapping. Must be a
     *         concrete class!
     */
    Class<?> destination() default void.class;

    /**
     * @return (Optional) bean factory used to create instances of the
     *         destination
     */
    Class<? extends BeanFactory> destinationFactory() default DummyFactory.class;

    /**
     * @return The source side of the unidirectional mapping.
     */
    Class<?> source() default void.class;
}

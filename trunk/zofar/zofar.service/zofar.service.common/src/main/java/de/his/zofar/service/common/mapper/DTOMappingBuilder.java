/**
 *
 */
package de.his.zofar.service.common.mapper;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.dozer.DozerBeanMapper;
import org.dozer.Mapper;
import org.dozer.loader.api.BeanMappingBuilder;

import de.his.zofar.service.common.mapper.annotations.DTOEntityMapping;
import de.his.zofar.service.common.mapper.annotations.UniDirectionalMapping;

/**
 * The DTOMappingBuilder spring bean which scans for all classes annotated with
 * {@link DTOEntityMapping} or {@link UniDirectionalMapping} in the given
 * basePackage.
 * For each concrete class annotated with {@link DTOEntityMapping} it adds a
 * bidirectional mapping to the dozer mapper. Abstract classes are skipped in
 * order to be able to leverage polymorphism in dozer.
 *
 * For each concrete class annotated with {@link UniDirectionalMapping} it adds a
 * unidirectional mapping to the dozer mapper including a factory bean if
 * desired.
 *
 * The logic is implemented in the {@link MappingBuilder} pojo.
 *
 * @author Reitmann
 */
public class DTOMappingBuilder {

    @Inject
    private Mapper mapper;

    private String dtoBasePackage;

    @PostConstruct
    private void addDefaultMappings() {
        final BeanMappingBuilder builder = new MappingBuilder(
                this.dtoBasePackage);

        // ugly cast but I currently do not have a better solution
        ((DozerBeanMapper) this.mapper).addMapping(builder);
    }

    /**
     * @param dtoBasePackage
     *            The basePackage to be scanned. Wildcards are allowed!
     */
    public void setDtoBasePackage(final String dtoBasePackage) {
        this.dtoBasePackage = dtoBasePackage;
    }

}

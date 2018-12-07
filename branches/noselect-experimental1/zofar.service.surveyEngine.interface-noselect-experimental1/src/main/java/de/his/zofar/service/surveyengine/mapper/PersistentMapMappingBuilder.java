/**
 *
 */
package de.his.zofar.service.surveyengine.mapper;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.dozer.DozerBeanMapper;
import org.dozer.Mapper;
import org.dozer.loader.api.BeanMappingBuilder;

/**
 * @author le
 *
 */
public class PersistentMapMappingBuilder {

    @Inject
    private Mapper mapper;

    @PostConstruct
    private void addDefaultMappings() {
        final BeanMappingBuilder builder = new PersistentMapMapper();

        // ugly cast but I currently do not have a better solution
        ((DozerBeanMapper) this.mapper).addMapping(builder);
    }

}

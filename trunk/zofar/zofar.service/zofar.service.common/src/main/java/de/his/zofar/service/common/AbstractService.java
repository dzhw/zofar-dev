/**
 *
 */
package de.his.zofar.service.common;

import javax.inject.Inject;

import org.dozer.Mapper;
import org.springframework.stereotype.Service;

/**
 * Abstract Service. Provides an protected instance of the dozer mapper.
 *
 * @author le
 *
 */
@Service
public abstract class AbstractService {

    protected Mapper mapper;

    // protected MapperFacade mapper;

    @Inject
    public AbstractService(final Mapper mapper /* final MapperFacade mapper */) {
        this.mapper = mapper;
    }
}

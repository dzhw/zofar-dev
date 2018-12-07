/**
 *
 */
package de.his.zofar.service.valuetype.internal;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import de.his.zofar.persistence.valuetype.daos.ValueTypeDao;
import de.his.zofar.service.common.exceptions.NotYetImplementedException;
import de.his.zofar.service.common.internal.InternalServiceInterface;
import de.his.zofar.service.common.model.PageDTO;
import de.his.zofar.service.valuetype.model.ValueType;

/**
 * @author le
 *
 */
@Service
public class ValueTypeInternalService implements InternalServiceInterface {
	
    private static final Logger LOGGER = LoggerFactory
            .getLogger(ValueTypeInternalService.class);

    /**
     * the dao instance to work on the database.
     */
    @Resource
    private ValueTypeDao valueTypeDao;

    /**
     * search for all value types from persistence layer.
     *
     * @param query
     *            the query
     * @return a page with all value types
     */
//    public PageDTO<ValueType> searchAll(final ValueTypeQueryDTO query) {
//        throw new NotYetImplementedException();
//    }

    /**
     * loads a value type by its identifier. returns null if the identifier
     * doesn't exist.
     *
     * @param identifier
     *            the value type id
     * @return the value type with the identifier
     */
    public ValueType loadByIdentifier(final String identifier) {
        throw new NotYetImplementedException();
    }

    /**
     * saves the value type and return the saved value type. do not use the
     * argument for further usage. use the return value instead.
     *
     * @param valueType
     *            the value type to save
     * @return the saved value type
     */
    public ValueType save(final ValueType valueType) {
        throw new NotYetImplementedException();
    }

}

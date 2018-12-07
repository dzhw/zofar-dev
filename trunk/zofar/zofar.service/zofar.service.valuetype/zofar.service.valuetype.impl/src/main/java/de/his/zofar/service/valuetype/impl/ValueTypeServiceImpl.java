/**
 *
 */
package de.his.zofar.service.valuetype.impl;

import java.util.Set;

import javax.inject.Inject;

import org.dozer.Mapper;
import org.springframework.stereotype.Service;

import de.his.zofar.service.common.AbstractService;
import de.his.zofar.service.valuetype.internal.ValueTypeInternalService;
import de.his.zofar.service.valuetype.model.ValueType;
import de.his.zofar.service.valuetype.service.ValueTypeService;

/**
 * @author le
 *
 */
@Service("valueTypeService")
public class ValueTypeServiceImpl extends AbstractService implements
        ValueTypeService {

    @Inject
    private ValueTypeInternalService valueTypeInternalService;

    @Inject
    public ValueTypeServiceImpl(final Mapper mapper) {
        super(mapper);
    }

	@Override
	public ValueType createNumberValueType(final String identifier,final String description,final String measurementLevel,int decimalPlaces, long minimum,
			long maximum) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ValueType createStringValueType(final String identifier,final String description,final String measurementLevel,int length, boolean canbeempty) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ValueType createBooleanValueType(final String identifier,final String description,final String measurementLevel) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ValueType loadByIdentifier(String identifier) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<ValueType> loadByType(Class<? extends ValueType> type) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void removeValueType(ValueType valueType) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public ValueType save(ValueType valueType) {
		// TODO Auto-generated method stub
		return null;
	}
    
    
    

//    /**
//     * loads a value type by its identifier. returns null if the identifier
//     * doesn't exist.
//     *
//     * @param identifier
//     *            the value type id
//     * @return the value type with the identifier
//     */
//    @Override
//    @Transactional(readOnly = true)
//    public ValueType loadByIdentifier(final String identifier) {
//        return this.valueTypeInternalService.loadByIdentifier(identifier);
//    }
//
//    /**
//     * saves the value type and return the saved value type. do not use the
//     * argument for further usage. use the return value instead.
//     *
//     * @param valueType
//     *            the value type to save
//     * @return the saved value type
//     */
//    @Override
//    @Transactional
//    public ValueType save(final ValueType valueType) {
//        return this.valueTypeInternalService.save(valueType);
//    }
//
//    /**
//     * search all value types from persistence layer.
//     *
//     * @param query
//     *            the query
//     * @return all value types
//     */
//    @Override
//    @Transactional(readOnly = true)
//    public PageDTO<ValueType> searchAll(final ValueTypeQueryDTO query) {
//        return this.valueTypeInternalService.searchAll(query);
//    }
}

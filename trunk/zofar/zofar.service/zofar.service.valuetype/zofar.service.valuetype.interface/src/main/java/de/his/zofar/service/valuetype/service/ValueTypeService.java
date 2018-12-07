/**
 *
 */
package de.his.zofar.service.valuetype.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import de.his.zofar.persistence.valuetype.entities.MeasurementLevel;
import de.his.zofar.service.valuetype.model.ValueType;

/**
 * @author le
 * 
 */
@Service
public interface ValueTypeService {

	// /**
	// * search all value types from persistence layer.
	// *
	// * @param query
	// * the query
	// * @return all value types
	// */
	// @Transactional(readOnly = true)
	// public abstract PageDTO<ValueType> searchAll(ValueTypeQueryDTO query);
	//
	// /**
	// * loads a value type by its identifier. returns null if the identifier
	// * doesn't exist.
	// *
	// * @param identifier
	// * the value type id
	// * @return the value type with the identifier
	// */
	// @Transactional(readOnly = true)
	// public abstract ValueType loadByIdentifier(String identifier);
	//
	// /**
	// * saves the value type and return the saved value type. do not use the
	// * argument for further usage. use the return value instead.
	// *
	// * @param valueType
	// * the value type to save
	// * @return the saved value type
	// */
	// @Transactional
	// public abstract ValueType save(ValueType valueType);

	@Transactional
	public abstract ValueType createNumberValueType(final String identifier,final String description,final MeasurementLevel measurementLevel,final int decimalPlaces,final long minimum,final long maximum);

	@Transactional
	public abstract ValueType createStringValueType(final String identifier,final String description,final MeasurementLevel measurementLevel,final int length,final boolean canbeempty);
	
	@Transactional
	public abstract ValueType createBooleanValueType(final String identifier,final String description,final MeasurementLevel measurementLevel);
	
	@Transactional(readOnly = true)
	public abstract ValueType loadByIdentifier(final String identifier);

	@Transactional(readOnly = true)
	public abstract void removeValueType(final ValueType valueType);
}
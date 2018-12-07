/**
 *
 */
package de.his.zofar.service.valuetype.impl;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import de.his.zofar.service.valuetype.model.BooleanValueType;
import de.his.zofar.service.valuetype.model.NumberValueType;
import de.his.zofar.service.valuetype.model.NumberValueType.PossibleValues;
import de.his.zofar.service.valuetype.model.NumberValueType.PossibleValues.Entry;
import de.his.zofar.service.valuetype.model.StringValueType;
import de.his.zofar.service.valuetype.model.ValueType;
import de.his.zofar.service.valuetype.model.possibleValues.PossibleBooleanValue;
import de.his.zofar.service.valuetype.model.possibleValues.PossibleNumberValue;
import de.his.zofar.service.valuetype.service.ValueTypeService;
//import de.his.zofar.persistence.valuetype.entities.MeasurementLevel;
import de.his.zofar.service.valuetype.util.Converter;

/**
 * this class is a mock up of the ValueTypeInternalService and must be replaced
 * by the actual implementation of the service.
 *
 * @author meisner
 *
 */
@Service("valueTypeService")
public class ValueTypeServiceMock implements ValueTypeService {
    /**
     * the logger.
     */
    private static final Logger LOGGER = LoggerFactory
            .getLogger(ValueTypeServiceMock.class);


//    /*
//     * (non-Javadoc)
//     *
//     * @see
//     * de.his.hiob.service.valuetype.internal.ValueTypeInternalService#searchAll
//     * (de.his.hiob.service.valuetype.external.dtos.ValueTypeQueryDTO)
//     */
//    public PageDTO<ValueType> searchAll(final ValueTypeQueryDTO query) {
//        final List<ValueType> result = new ArrayList<ValueType>();
//        result.addAll(valueTypes.values());
//
//        final PageDTO<ValueType> page = new PageDTO<ValueType>();
//        page.setContent(result);
//        return page;
//    }
//

    /*
     * (non-Javadoc)
     *
     * @see
     * de.his.hiob.service.valuetype.internal.ValueTypeInternalService#save(
     * de.his.hiob.service.valuetype.external.dtos.ValueTypeDTO)
     */
    public ValueType save(final ValueType valueType) {
        valueTypes.put(valueType.getIdentifier(), valueType);
        return valueTypes.get(valueType.getIdentifier());
    }

    // from here on everything is fake

    /**
     * mockup of a persistence layer aka db for the value type.
     */
    private Map<String, ValueType> valueTypes;

    /**
     * set up the fake db.
     */
    @PostConstruct
    private void init() {
        LOGGER.warn("You are using a MOCK!");

        valueTypes = new HashMap<String, ValueType>();

        createSimpleBooleanValueType();
        createDefaultNotEmptyStringValueType();
        createYesNoMaybeValueType();
        createSeasonValueType();
        createDefaultEmptyStringValueType();
        createScaleInteger1To5ValueType();
        createSimpleYesNoUnknownValueType();
        createGenderValueType();
    }


    /**
     * creates the default boolean value type.
     */
    private void createSimpleBooleanValueType() {
        final BooleanValueType valueType = BooleanValueType.Factory.newInstance();
        final String identifier = "defaultbooleantype";

        valueType.setIdentifier(identifier);
        valueType
                .setDescription("Default boolean value type. yes = true, no = false");

//        // yes
//        valueType.addPossibleValue(createPossibleBooleanValue("yes", true)); 
//        // no
//        valueType.addPossibleValue(createPossibleBooleanValue("no", false));
        
        de.his.zofar.service.valuetype.model.BooleanValueType.PossibleValues pvs = valueType.addNewPossibleValues();
        // setting the yes value
        final de.his.zofar.service.valuetype.model.BooleanValueType.PossibleValues.Entry entry1 = pvs.addNewEntry();
        entry1.setKey("yes");
        entry1.setValue(createPossibleBooleanValue("yes", true));
        // setting the no value
        final de.his.zofar.service.valuetype.model.BooleanValueType.PossibleValues.Entry entry2 = pvs.addNewEntry();
        entry2.setKey("no");
        entry2.setValue(createPossibleBooleanValue("no", false));

        valueTypes.put(identifier, valueType);
    }

    @Override
	public ValueType createNumberValueType(final String identifier,final String description,final String measurementLevel,int decimalPlaces, long minimum,
			long maximum) {
		final NumberValueType valueType = NumberValueType.Factory.newInstance();
		valueType.setIdentifier(identifier);
		valueType.setDescription(description);
		valueType.setMeasurementLevel(Converter.getInstance().convertMeasurementLevel(measurementLevel));
		valueType.setDecimalPlaces(decimalPlaces);
		valueType.setMinimum(minimum);
		valueType.setMaximum(maximum);
		
		//Add possible Values
//		valueType.addPossibleValue(possibleVariableValue);
		
		valueTypes.put(identifier, valueType);
		return valueType;
	}

	@Override
	public ValueType createStringValueType(final String identifier,final String description,final String measurementLevel,int length, boolean canbeempty) {
		final StringValueType valueType = StringValueType.Factory.newInstance();
		valueType.setIdentifier(identifier);
		valueType.setDescription(description);
		valueType.setMeasurementLevel(Converter.getInstance().convertMeasurementLevel(measurementLevel));
		valueType.setCanBeEmpty(canbeempty);
		valueType.setLength(length);
		
		//Add possible Values
//		valueType.addPossibleValue(possibleVariableValue);
		
		valueTypes.put(identifier, valueType);
		return valueType;
	}

	@Override
	public ValueType createBooleanValueType(final String identifier,final String description,final String measurementLevel) {
		final BooleanValueType valueType = BooleanValueType.Factory.newInstance();
		valueType.setIdentifier(identifier);
		valueType.setDescription(description);
		valueType.setMeasurementLevel(Converter.getInstance().convertMeasurementLevel(measurementLevel));
		
		//Add possible Values
//		valueType.addPossibleValue(possibleValue);
		
		valueTypes.put(identifier, valueType);
		return valueType;
	}

    /*
     * (non-Javadoc)
     *
     * @see de.his.hiob.service.valuetype.internal.ValueTypeInternalService#
     * loadByIdentifier(java.lang.String)
     */
	@Override
    public ValueType loadByIdentifier(final String identifier) {
        if (!valueTypes.containsKey(identifier)) {
            throw new IllegalArgumentException("No value type with id: "
                    + identifier);
        }
        return valueTypes.get(identifier);
    }

	@Override
	public Set<ValueType> loadByType(Class<? extends ValueType> type) {
		final Set<ValueType> back = new HashSet<ValueType>();
		Iterator<String> it = valueTypes.keySet().iterator();
		while(it.hasNext()){
			final String identificator = it.next();
			final ValueType tmptype = valueTypes.get(identificator);
			if(type.isAssignableFrom(tmptype.getClass()))back.add(tmptype);
		}
		return back;
	}

	@Override
	public void removeValueType(ValueType valueType) {
		valueTypes.remove(valueType.getIdentifier());
	}

	/**
     * creates the default cannot be empty value type for open questions.
     */
    private void createDefaultNotEmptyStringValueType() {
        final StringValueType valueType = StringValueType.Factory.newInstance();
        final String identifier = "defaultnotemptystring";

        valueType.setIdentifier(identifier);
        valueType.setDescription("The default string value type. "
                + "Max 255 characters and can not be empty.");

        valueType.setCanBeEmpty(false);
        valueType.setLength(255);

        valueTypes.put(identifier, valueType);
    }

    /**
     * creates a value type yes, no and maybe.
     */
    private void createYesNoMaybeValueType() {
        final NumberValueType valueType = NumberValueType.Factory.newInstance();
        final String identifier = "yesnomaybe";

        valueType.setIdentifier(identifier);
        valueType.setDescription("Yes: 1, No: 2, Maybe: -97");

//        // yes
//        valueType.addPossibleValue(createPossibleNumberValue("yes", 1, false,
//                valueType));
//        // no
//        valueType.addPossibleValue(createPossibleNumberValue("no", 2, false,
//                valueType));
//        // don't know
//        valueType.addPossibleValue(createPossibleNumberValue("maybe", -97,
//                true, valueType));
        
        PossibleValues pvs = valueType.addNewPossibleValues();
        // setting the yes value
        final Entry entry1 = pvs.addNewEntry();
        entry1.setKey(1);
        entry1.setValue(createPossibleNumberValue("yes", 1, false,valueType));
        // setting the no value
        final Entry entry2 = pvs.addNewEntry();
        entry2.setKey(2);
        entry2.setValue(createPossibleNumberValue("no", 2, false,valueType));
        // setting the don't know value
        final Entry entry3 = pvs.addNewEntry();
        entry3.setKey(-97);
        entry3.setValue(createPossibleNumberValue("unknown", -97,true, valueType));

        valueTypes.put(identifier, valueType);
    }

    /**
     * creates a value type for the season (time of year).
     */
    private void createSeasonValueType() {
        final NumberValueType valueType = NumberValueType.Factory.newInstance();
        final String identifier = "seasons";

        valueType.setIdentifier(identifier);
        valueType.setDescription("Seasons (Time of year)");

//        // spring season
//        valueType.addPossibleValue(createPossibleNumberValue("spring", 1,
//                false, valueType));
//        // summer season
//        valueType.addPossibleValue(createPossibleNumberValue("summer", 2,
//                false, valueType));
//        // fall season
//        valueType.addPossibleValue(createPossibleNumberValue("fall", 3, false,
//                valueType));
//        // winter season
//        valueType.addPossibleValue(createPossibleNumberValue("winter", 4,
//                false, valueType));
        PossibleValues pvs = valueType.addNewPossibleValues();
        // setting the spring season
        final Entry entry1 = pvs.addNewEntry();
        entry1.setKey(1);
        entry1.setValue(createPossibleNumberValue("spring", 1, false,valueType));
        // setting the summer season
        final Entry entry2 = pvs.addNewEntry();
        entry2.setKey(2);
        entry2.setValue(createPossibleNumberValue("summer", 2, false,valueType));
        // setting the fall season
        final Entry entry3 = pvs.addNewEntry();
        entry3.setKey(-97);
        entry3.setValue(createPossibleNumberValue("fall", 3,true, valueType));
        
        // setting the winter season
        final Entry entry4 = pvs.addNewEntry();
        entry4.setKey(-97);
        entry4.setValue(createPossibleNumberValue("winter", 4,true, valueType));

        valueTypes.put(identifier, valueType);
    }

    /**
     * creates a the default empty value type for open questions.
     */
    private void createDefaultEmptyStringValueType() {
        final StringValueType valueType = StringValueType.Factory.newInstance();
        final String identifier = "defaultemptystring";

        valueType.setIdentifier(identifier);
        valueType.setDescription("The default string value type. "
                + "Max 255 characters and can be empty.");

        valueType.setCanBeEmpty(true);
        valueType.setLength(255);

        valueTypes.put(identifier, valueType);
    }

    /**
     * creates a scale value type with integer values between 1 and 5.
     */
    private void createScaleInteger1To5ValueType() {
        final NumberValueType valueType = NumberValueType.Factory.newInstance();
        final String identifier = "integerscale1to5";

        valueType.setIdentifier(identifier);
        valueType.setDescription("Integer scale with values between 1 and 5");

        // set the boundaries of this value type between 1 and 5
        valueType.setMinimum(Long.valueOf(1));
        valueType.setMaximum(Long.valueOf(5));
        // set the decimal places
        valueType.setDecimalPlaces(0);

        valueTypes.put(identifier, valueType);
    }

    /**
     * creates a value type for questions with yes, no or unknown values.
     */
    private void createSimpleYesNoUnknownValueType() {
        final NumberValueType valueType = NumberValueType.Factory.newInstance();
        final String identifier = "yesnounknown";

        valueType.setIdentifier(identifier);
        valueType.setDescription("Yes: 1, No: 2, Unknown: -97");


        PossibleValues pvs = valueType.addNewPossibleValues();
        // setting the yes value
        final Entry entry1 = pvs.addNewEntry();
        entry1.setKey(1);
        entry1.setValue(createPossibleNumberValue("yes", 1, false,valueType));
        // setting the no value
        final Entry entry2 = pvs.addNewEntry();
        entry2.setKey(2);
        entry2.setValue(createPossibleNumberValue("no", 2, false,valueType));
        // setting the unknown value
        final Entry entry3 = pvs.addNewEntry();
        entry3.setKey(-97);
        entry3.setValue(createPossibleNumberValue("unknown", -97,true, valueType));

        valueTypes.put(identifier, valueType);
    }

    /**
     * creates a value type for questions about the gender.
     */
    private void createGenderValueType() {
        final NumberValueType valueType = NumberValueType.Factory.newInstance();
        final String identifier = "gender";

        valueType.setIdentifier(identifier);
        valueType.setDescription("Representing the gender");

        PossibleValues pvs = valueType.addNewPossibleValues();
        // setting the female value
        final Entry entry1 = pvs.addNewEntry();
        entry1.setKey(1);
        entry1.setValue(createPossibleNumberValue("female", 1, false,valueType));
        // setting the male value
        final Entry entry2 = pvs.addNewEntry();
        entry2.setKey(2);
        entry2.setValue(createPossibleNumberValue("male", 2, false,valueType));
        // setting the unknown value
        final Entry entry3 = pvs.addNewEntry();
        entry3.setKey(-97);
        entry3.setValue(createPossibleNumberValue("unknown", -97,true, valueType));

        valueTypes.put(identifier, valueType);
    }

    /**
     * creates a possible number value.
     *
     * @param label
     *            the label of the possible value
     * @param value
     *            the value of the possible value
     * @param isMissing
     *            is the possible value a missing
     * @param valueType
     *            the value type that the possible value belongs to
     * @return the created possible value
     */
    private PossibleNumberValue createPossibleNumberValue(
            final String label, final Integer value, final boolean isMissing,
            final NumberValueType valueType) {
        final PossibleNumberValue possibleValue = PossibleNumberValue.Factory.newInstance();
        possibleValue.addLabel(label);
        possibleValue.setValue(Long.valueOf(value));
        possibleValue.setValueType(valueType);
        possibleValue.setIsMissing(isMissing);

        return possibleValue;
    }

    /**
     * creates a possible boolean value.
     *
     * @param label
     *            the label to set
     * @param value
     *            the value to set
     * @return the created possible value
     */
    private PossibleBooleanValue createPossibleBooleanValue(
            final String label, final Boolean value) {
        final PossibleBooleanValue possibleValue = PossibleBooleanValue.Factory.newInstance();
        possibleValue.addLabel(label);
        possibleValue.setValue(value);

        return possibleValue;
    }
}

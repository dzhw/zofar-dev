/**
 * 
 */
package de.his.zofar.service.valuetype.test.model;

import org.junit.Test;

import de.his.zofar.service.valuetype.model.BooleanValueType;
import de.his.zofar.service.valuetype.model.possiblevalues.PossibleBooleanValue;

/**
 * @author le
 * 
 */
public class BooleanValueTypeTest {

    // @Test(expected = IllegalArgumentException.class)
    // public void testAddInvalidPossibleValue() {
    // final BooleanValueTypeDTO valueType = new BooleanValueTypeDTO();
    // valueType.addPossibleValue(new PossibleStringValueDTO());
    // }

    /**
     * test if boolean value type throws an exception if an invalid possible
     * value is added.
     */
    @Test(expected = IllegalStateException.class)
    public void testAddInvalidPossibleValue() {
        final BooleanValueType valueType = new BooleanValueType();
        valueType.addPossibleValue(new PossibleBooleanValue());
    }

    /**
     * test if boolean value type throws an exception if too many value types
     * are added.
     */
    @Test(expected = IllegalStateException.class)
    public void testTooManyPossibleValues() {
        final BooleanValueType valueType = createCompleteBooleanValueType();
        valueType.addPossibleValue(new PossibleBooleanValue(true, valueType));
    }

    /**
     * creates a boolean value type for testing.
     * 
     * @return the create boolean value type
     */
    private BooleanValueType createCompleteBooleanValueType() {
        final BooleanValueType valueType = new BooleanValueType();

        valueType.addPossibleValue(new PossibleBooleanValue(true, valueType));
        valueType.addPossibleValue(new PossibleBooleanValue(false, valueType));

        return valueType;
    }

}

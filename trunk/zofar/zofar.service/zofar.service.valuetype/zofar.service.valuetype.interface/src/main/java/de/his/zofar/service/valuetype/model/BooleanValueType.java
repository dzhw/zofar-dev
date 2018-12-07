/**
 *
 */
package de.his.zofar.service.valuetype.model;

import java.util.HashMap;
import java.util.Map;

import de.his.zofar.service.valuetype.model.possiblevalues.PossibleBooleanValue;

/**
 * @author le
 *
 */
public class BooleanValueType extends ValueType {

    /**
     *
     */
    private static final long serialVersionUID = -924048173289917976L;
    /**
     *
     */
    private Map<Boolean, PossibleBooleanValue> possibleValues;

    /**
     * @return the possibleValues
     */
    @Override
    public Map<Boolean, PossibleBooleanValue> getPossibleValues() {
        if (possibleValues == null) {
            this.possibleValues =
                    new HashMap<Boolean, PossibleBooleanValue>();
        }
        return possibleValues;
    }

    /**
     * adds a possible value to the value type. if value already exists the
     * value will then be overwritten.
     *
     * @param possibleValue
     */
    public void addPossibleValue(final PossibleBooleanValue possibleValue) {
        if (this.possibleValues == null) {
            this.possibleValues =
                    new HashMap<Boolean, PossibleBooleanValue>();
        }
        // check constraints
        if (this.possibleValues.size() > 1) {
            throw new IllegalStateException(
                    "cannot have more than 2 possible values");
        }
        if (possibleValue.getValue() == null) {
            throw new IllegalStateException("possible value must have a value");
        }
        this.possibleValues.put(possibleValue.getValue(), possibleValue);
    }
}

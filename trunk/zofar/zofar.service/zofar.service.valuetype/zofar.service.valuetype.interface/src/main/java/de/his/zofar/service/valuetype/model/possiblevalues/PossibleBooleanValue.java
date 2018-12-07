/**
 *
 */
package de.his.zofar.service.valuetype.model.possiblevalues;

import de.his.zofar.service.valuetype.model.BooleanValueType;
import de.his.zofar.service.valuetype.model.ValueType;

/**
 * @author le
 *
 */
public class PossibleBooleanValue extends PossibleValue {

    /**
     *
     */
    private static final long serialVersionUID = -1488564230602192947L;

    /**
     *
     */
    private Boolean value;

    /**
     *
     */
    private BooleanValueType valueType;

    /**
     *
     */
    public PossibleBooleanValue() {
        super();
    }

    /**
     * @param value
     *            the value to set
     * @param valueType
     *            the value type to set
     */
    public PossibleBooleanValue(final Boolean value,
            final BooleanValueType valueType) {
        super();
        this.value = value;
        this.valueType = valueType;
    }

    /**
     * @return the value
     */
    public Boolean getValue() {
        return value;
    }

    /**
     * @param value
     *            the value to set
     */
    public void setValue(final Boolean value) {
        this.value = value;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * de.his.hiob.service.valuetype.external.dtos.possiblevalues.PossibleValueDTO
     * #getValueType()
     */
    @Override
    public ValueType getValueType() {
        return valueType;
    }

    /**
     * @param valueType
     *            the valueType to set
     */
    public void setValueType(final BooleanValueType valueType) {
        this.valueType = valueType;
    }

}

package de.his.zofar.service.valuetype.model;

import java.util.HashMap;
import java.util.Map;

import de.his.zofar.service.valuetype.model.possiblevalues.PossibleNumberValue;

public class NumberValueType extends ValueType {

    /**
     *
     */
    private static final long serialVersionUID = -171111488182418679L;

    private Long minimum;

    private Long maximum;

    private Integer decimalPlaces;

    private Map<Long, PossibleNumberValue> possibleValues;

    public void addPossibleValue(final PossibleNumberValue possibleVariableValue) {
        if (this.possibleValues == null) {
            this.possibleValues = new HashMap<Long, PossibleNumberValue>();
        }
        this.possibleValues.put(possibleVariableValue.getValue(),
                possibleVariableValue);
    }

    public Integer getDecimalPlaces() {
        return this.decimalPlaces;
    }

    public Long getMaximum() {
        return this.maximum;
    }

    public Long getMinimum() {
        return this.minimum;
    }

    @Override
    public Map<Long, PossibleNumberValue> getPossibleValues() {
        return this.possibleValues;
    }

    public void setDecimalPlaces(final Integer decimalPlaces) {
        this.decimalPlaces = decimalPlaces;
    }

    public void setMaximum(final Long maximum) {
        this.maximum = maximum;
    }

    public void setMinimum(final Long minimum) {
        this.minimum = minimum;
    }

    public void setPossibleValues(
            final Map<Long, PossibleNumberValue> possibleValues) {
        this.possibleValues = possibleValues;
    }

}

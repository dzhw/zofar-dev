package de.his.zofar.service.valuetype.model;

import java.util.HashMap;
import java.util.Map;

import de.his.zofar.service.valuetype.model.possiblevalues.PossibleStringValue;

public class StringValueType extends ValueType {

    /**
     *
     */
    private static final long serialVersionUID = -8329508523015454351L;

    private Integer length;

    private Boolean canBeEmpty;

    private Map<String, PossibleStringValue> possibleValues;

    public void addPossibleValue(
            final PossibleStringValue possibleVariableValue) {
        if (this.possibleValues == null) {
            this.possibleValues = new HashMap<String, PossibleStringValue>();
        }
        this.possibleValues.put(possibleVariableValue.getValue(),
                possibleVariableValue);
    }

    public Boolean getCanBeEmpty() {
        return this.canBeEmpty;
    }

    public Integer getLength() {
        return this.length;
    }

    @Override
    public Map<String, PossibleStringValue> getPossibleValues() {
        return this.possibleValues;
    }

    public void setCanBeEmpty(final Boolean canBeEmpty) {
        this.canBeEmpty = canBeEmpty;
    }

    public void setLength(final Integer length) {
        this.length = length;
    }

    public void setPossibleValues(
            final Map<String, PossibleStringValue> possibleValues) {
        this.possibleValues = possibleValues;
    }

}

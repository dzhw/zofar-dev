package de.his.zofar.persistence.valuetype.entities;

import static javax.persistence.CascadeType.ALL;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.MapKey;
import javax.persistence.OneToMany;

import de.his.zofar.persistence.valuetype.entities.possiblevalues.PossibleNumberValueEntity;

@Entity
public class NumberValueTypeEntity extends ValueTypeEntity {

    @Column(nullable = false)
    private Long minimum;

    @Column(nullable = false)
    private Long maximum;

    @Column(nullable = false)
    private Integer decimalPlaces;

    @OneToMany(mappedBy = "valueType", fetch = FetchType.LAZY, cascade = ALL)
    @MapKey(name = "value")
    private Map<Long, PossibleNumberValueEntity> possibleValues;

    public Long getMinimum() {
        return this.minimum;
    }

    public void setMinimum(final Long minimum) {
        this.minimum = minimum;
    }

    public Long getMaximum() {
        return this.maximum;
    }

    public void setMaximum(final Long maximum) {
        this.maximum = maximum;
    }

    public Integer getDecimalPlaces() {
        return this.decimalPlaces;
    }

    public void setDecimalPlaces(final Integer decimalPlaces) {
        this.decimalPlaces = decimalPlaces;
    }

    public Map<Long, PossibleNumberValueEntity> getPossibleValues() {
        return this.possibleValues;
    }

    public void setPossibleValues(
            final Map<Long, PossibleNumberValueEntity> possibleValues) {
        this.possibleValues = possibleValues;
    }

    public void addPossibleValue(final PossibleNumberValueEntity possibleVariableValue) {
        if (this.possibleValues == null) {
            this.possibleValues = new HashMap<Long, PossibleNumberValueEntity>();
        }
        this.possibleValues.put(possibleVariableValue.getValue(),
                possibleVariableValue);
    }

}

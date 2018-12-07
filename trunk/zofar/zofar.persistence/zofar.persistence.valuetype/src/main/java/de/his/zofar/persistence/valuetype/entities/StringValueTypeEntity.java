package de.his.zofar.persistence.valuetype.entities;

import static javax.persistence.CascadeType.ALL;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.MapKey;
import javax.persistence.OneToMany;

import de.his.zofar.persistence.valuetype.entities.possiblevalues.PossibleStringValueEntity;

@Entity
public class StringValueTypeEntity extends ValueTypeEntity {

    @Column(nullable = false)
    private Integer length;

    @Column(nullable = false)
    private Boolean canBeEmpty;

    @OneToMany(mappedBy = "valueType", fetch = FetchType.LAZY, cascade = ALL)
    @MapKey(name = "value")
    private Map<String, PossibleStringValueEntity> possibleValues;

    public Integer getLength() {
        return this.length;
    }

    public void setLength(final Integer length) {
        this.length = length;
    }

    public Boolean getCanBeEmpty() {
        return this.canBeEmpty;
    }

    public void setCanBeEmpty(final Boolean canBeEmpty) {
        this.canBeEmpty = canBeEmpty;
    }

    public Map<String, PossibleStringValueEntity> getPossibleValues() {
        return this.possibleValues;
    }

    public void setPossibleValues(
            final Map<String, PossibleStringValueEntity> possibleValues) {
        this.possibleValues = possibleValues;
    }

    public void addPossibleValue(final PossibleStringValueEntity possibleVariableValue) {
        if (this.possibleValues == null) {
            this.possibleValues = new HashMap<String, PossibleStringValueEntity>();
        }
        this.possibleValues.put(possibleVariableValue.getValue(),
                possibleVariableValue);
    }

}

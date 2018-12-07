package de.his.zofar.persistence.valuetype.entities.possiblevalues;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import de.his.zofar.persistence.valuetype.entities.NumberValueTypeEntity;

@Entity
public class PossibleNumberValueEntity extends PossibleValueEntity {

    private Long value;

    @ManyToOne(optional = false)
    private NumberValueTypeEntity valueType;

    @Override
    public NumberValueTypeEntity getValueType() {
        return this.valueType;
    }

    public void setValueType(final NumberValueTypeEntity valueType) {
        this.valueType = valueType;
    }

    public Long getValue() {
        return this.value;
    }

    public void setValue(final Long value) {
        this.value = value;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result
                + ((this.value == null) ? 0 : this.value.hashCode());
        return result;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (!super.equals(obj)) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final PossibleNumberValueEntity other = (PossibleNumberValueEntity) obj;
        if (this.value == null) {
            if (other.value != null) {
                return false;
            }
        } else if (!this.value.equals(other.value)) {
            return false;
        }
        return true;
    }

}

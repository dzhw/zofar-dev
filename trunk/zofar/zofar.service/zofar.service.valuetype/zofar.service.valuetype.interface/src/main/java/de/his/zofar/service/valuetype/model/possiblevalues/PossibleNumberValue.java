package de.his.zofar.service.valuetype.model.possiblevalues;

import de.his.zofar.service.valuetype.model.NumberValueType;

//@DTOEntityMapping(entity = PossibleNumberValueEntity.class)
public class PossibleNumberValue extends PossibleValue {

    /**
     *
     */
    private static final long serialVersionUID = 3769174610606508408L;

    private Long value;

    private NumberValueType valueType;

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
        final PossibleNumberValue other = (PossibleNumberValue) obj;
        if (this.value == null) {
            if (other.value != null) {
                return false;
            }
        } else if (!this.value.equals(other.value)) {
            return false;
        }
        return true;
    }

    public Long getValue() {
        return this.value;
    }

    @Override
    public NumberValueType getValueType() {
        return this.valueType;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result
                + ((this.value == null) ? 0 : this.value.hashCode());
        return result;
    }

    public void setValue(final Long value) {
        this.value = value;
    }

    public void setValueType(final NumberValueType valueType) {
        this.valueType = valueType;
    }

}

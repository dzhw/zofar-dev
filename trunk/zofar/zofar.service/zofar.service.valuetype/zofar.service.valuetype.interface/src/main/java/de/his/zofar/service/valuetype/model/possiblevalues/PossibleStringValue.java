package de.his.zofar.service.valuetype.model.possiblevalues;

import de.his.zofar.service.valuetype.model.StringValueType;

//@DTOEntityMapping(entity = PossibleStringValueEntity.class)
public class PossibleStringValue extends PossibleValue {

    /**
     *
     */
    private static final long serialVersionUID = -6296872128637014987L;

    private String value;

    private StringValueType valueType;

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
        final PossibleStringValue other = (PossibleStringValue) obj;
        if (this.value == null) {
            if (other.value != null) {
                return false;
            }
        } else if (!this.value.equals(other.value)) {
            return false;
        }
        return true;
    }

    public String getValue() {
        return this.value;
    }

    @Override
    public StringValueType getValueType() {
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

    public void setValue(final String value) {
        this.value = value;
    }

    public void setValueType(final StringValueType valueType) {
        this.valueType = valueType;
    }

}

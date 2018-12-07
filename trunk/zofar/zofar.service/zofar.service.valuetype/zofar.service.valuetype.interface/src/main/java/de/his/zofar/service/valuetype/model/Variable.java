package de.his.zofar.service.valuetype.model;

import de.his.zofar.service.common.model.BaseDTO;

//@DTOEntityMapping(entity = VariableEntity.class)
public abstract class Variable extends BaseDTO {
    /**
	 * 
	 */
	private static final long serialVersionUID = -4532270954579371390L;

	private String uuid;

    private String name;

    private ValueType valueType;

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
        final Variable other = (Variable) obj;
        if (this.uuid == null) {
            if (other.uuid != null) {
                return false;
            }
        } else if (!this.uuid.equals(other.uuid)) {
            return false;
        }
        return true;
    }

    public String getName() {
        return this.name;
    }

    public String getUuid() {
        return this.uuid;
    }

    public ValueType getValueType() {
        return this.valueType;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result
                + ((this.uuid == null) ? 0 : this.uuid.hashCode());
        return result;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public void setUuid(final String uuid) {
        this.uuid = uuid;
    }

    public void setValueType(final ValueType valueType) {
        this.valueType = valueType;
    }
}

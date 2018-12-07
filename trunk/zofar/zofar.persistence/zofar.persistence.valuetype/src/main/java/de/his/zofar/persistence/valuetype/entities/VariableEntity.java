package de.his.zofar.persistence.valuetype.entities;

import static javax.persistence.CascadeType.PERSIST;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;

import de.his.zofar.persistence.common.entities.BaseEntity;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@SequenceGenerator(initialValue = 1, name = "primary_key_generator",
        sequenceName = "SEQ_VARIABLE_ID")
public abstract class VariableEntity extends BaseEntity {
    @Column(unique = false, nullable = false)
    private String uuid;

    @Column(unique = false, nullable = false)
    private String name;

    @ManyToOne(cascade = PERSIST)
    private ValueTypeEntity valueType;

    public VariableEntity() {
        super();
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
        final VariableEntity other = (VariableEntity) obj;
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

    public ValueTypeEntity getValueType() {
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

    public void setValueType(final ValueTypeEntity valueType) {
        this.valueType = valueType;
    }
}

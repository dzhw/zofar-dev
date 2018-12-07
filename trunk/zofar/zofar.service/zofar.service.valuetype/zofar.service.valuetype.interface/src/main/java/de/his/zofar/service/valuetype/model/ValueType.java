package de.his.zofar.service.valuetype.model;

import java.util.Map;

import de.his.zofar.persistence.valuetype.entities.MeasurementLevel;
import de.his.zofar.service.common.model.BaseDTO;
import de.his.zofar.service.valuetype.model.possiblevalues.PossibleValue;

public abstract class ValueType extends BaseDTO {
    /**
     *
     */
    private static final long serialVersionUID = 2756425852756765275L;

    private String identifier;

    private String description;

    private MeasurementLevel measurementLevel;

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
        final ValueType other = (ValueType) obj;
        if (this.identifier == null) {
            if (other.identifier != null) {
                return false;
            }
        } else if (!this.identifier.equals(other.identifier)) {
            return false;
        }
        return true;
    }

    public String getDescription() {
        return this.description;
    }

    public String getIdentifier() {
        return this.identifier;
    }

    public MeasurementLevel getMeasurementLevel() {
        return this.measurementLevel;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result =
                prime
                * result
                + ((this.identifier == null) ? 0 : this.identifier
                        .hashCode());
        return result;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public void setIdentifier(final String identifier) {
        this.identifier = identifier;
    }

    public void setMeasurementLevel(final MeasurementLevel measurementLevel) {
        this.measurementLevel = measurementLevel;
    }

    public abstract Map<?, ? extends PossibleValue> getPossibleValues();

}

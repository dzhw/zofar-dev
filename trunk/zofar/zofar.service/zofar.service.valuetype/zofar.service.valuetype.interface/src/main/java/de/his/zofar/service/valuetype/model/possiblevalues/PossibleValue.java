package de.his.zofar.service.valuetype.model.possiblevalues;

import java.util.ArrayList;
import java.util.List;

import de.his.zofar.service.common.model.BaseDTO;
import de.his.zofar.service.valuetype.model.ValueType;

//@DTOEntityMapping(entity = PossibleValueEntity.class)
public abstract class PossibleValue extends BaseDTO {

    /**
     *
     */
    private static final long serialVersionUID = -1567411117371321610L;

    private Boolean isMissing;

    private List<String> labels;

    public void addLabel(final String label) {
        if (this.labels == null) {
            this.labels = new ArrayList<String>();
        }
        this.labels.add(label);
    }

    public Boolean getIsMissing() {
        return this.isMissing;
    }

    public List<String> getLabels() {
        return this.labels;
    }

    public abstract ValueType getValueType();

    public void setIsMissing(final Boolean isMissing) {
        this.isMissing = isMissing;
    }

    public void setLabels(final List<String> labels) {
        this.labels = labels;
    }

}
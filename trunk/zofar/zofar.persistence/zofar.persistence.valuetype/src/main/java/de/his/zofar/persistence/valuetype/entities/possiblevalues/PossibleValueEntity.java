package de.his.zofar.persistence.valuetype.entities.possiblevalues;

import static javax.persistence.FetchType.EAGER;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.OrderColumn;
import javax.persistence.SequenceGenerator;

import de.his.zofar.persistence.common.entities.BaseEntity;
import de.his.zofar.persistence.valuetype.entities.ValueTypeEntity;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@SequenceGenerator(initialValue = 1, name = "primary_key_generator",
        sequenceName = "SEQ_POSSIBLE_VARIABLE_VALUE_ID")
public abstract class PossibleValueEntity extends BaseEntity {

    @Column(nullable = false)
    private Boolean isMissing;

    @ElementCollection(fetch = EAGER)
    @OrderColumn
    @CollectionTable(name = "PossibleValueLabels")
    @Column(name = "label", nullable = false)
    private List<String> labels;

    public Boolean getIsMissing() {
        return this.isMissing;
    }

    public void setIsMissing(final Boolean isMissing) {
        this.isMissing = isMissing;
    }

    public List<String> getLabels() {
        return this.labels;
    }

    public void setLabels(final List<String> labels) {
        this.labels = labels;
    }

    public void addLabel(final String label) {
        if (this.labels == null) {
            this.labels = new ArrayList<String>();
        }
        this.labels.add(label);
    }

    public abstract ValueTypeEntity getValueType();

}
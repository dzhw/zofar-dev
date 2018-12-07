package de.his.zofar.persistence.panel.entities;

import static javax.persistence.CascadeType.ALL;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.Entity;
import javax.persistence.MapKeyJoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;

import de.his.zofar.persistence.panel.entities.panelistvalues.PanelistValueEntity;
import de.his.zofar.persistence.valuetype.entities.VariableEntity;

@Entity
@SequenceGenerator(initialValue = 1, name = "primary_key_generator",
sequenceName = "SEQ_PANEL_VARIABLE_ID")
public class PanelVariableEntity extends VariableEntity {
    @OneToMany(mappedBy = "variable", cascade = ALL)
    @MapKeyJoinColumn(name = "panelist_id")
    private Map<PanelistEntity, PanelistValueEntity> values;

    public Map<PanelistEntity, PanelistValueEntity> getValues() {
        return this.values;
    }

    public void setValues(final Map<PanelistEntity, PanelistValueEntity> values) {
        this.values = values;
    }

    public void addValue(final PanelistValueEntity value) {
        if (this.values == null) {
            this.values = new HashMap<PanelistEntity, PanelistValueEntity>();
        }
        this.values.put(value.getPanelist(), value);
    }
}

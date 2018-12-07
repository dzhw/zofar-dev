package de.his.zofar.persistence.panel.entities.panelistvalues;

import static javax.persistence.CascadeType.PERSIST;

import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;

import de.his.zofar.persistence.common.entities.BaseEntity;
import de.his.zofar.persistence.panel.entities.PanelVariableEntity;
import de.his.zofar.persistence.panel.entities.PanelistEntity;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@SequenceGenerator(initialValue = 1, name = "primary_key_generator",
sequenceName = "SEQ_PANELIST_VALUE_ID")
public abstract class PanelistValueEntity extends BaseEntity {

    @ManyToOne(optional = false, cascade = PERSIST)
    private PanelistEntity panelist;

    @ManyToOne(optional = false, cascade = PERSIST)
    private PanelVariableEntity variable;

    public PanelistValueEntity() {
        super();
    }

    public PanelistValueEntity(final PanelVariableEntity variable,
            final PanelistEntity panelist) {
        super();
        this.variable = variable;
        this.panelist = panelist;
    }

    public PanelVariableEntity getVariable() {
        return this.variable;
    }

    public void setVariable(final PanelVariableEntity variable) {
        this.variable = variable;
    }

    public PanelistEntity getPanelist() {
        return this.panelist;
    }

    public void setPanelist(final PanelistEntity panelist) {
        this.panelist = panelist;
    }

    public abstract Object getValue();
}

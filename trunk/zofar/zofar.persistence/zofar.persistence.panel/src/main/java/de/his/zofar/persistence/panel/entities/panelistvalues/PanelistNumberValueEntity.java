package de.his.zofar.persistence.panel.entities.panelistvalues;

import javax.persistence.Entity;

import de.his.zofar.persistence.panel.entities.PanelVariableEntity;
import de.his.zofar.persistence.panel.entities.PanelistEntity;

@Entity
public class PanelistNumberValueEntity extends PanelistValueEntity {

    private Long value;

    public PanelistNumberValueEntity() {
        super();
    }

    public PanelistNumberValueEntity(final PanelVariableEntity variable,
            final PanelistEntity panelist, final Long value) {
        super(variable, panelist);
        this.value = value;
    }

    @Override
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
        final PanelistNumberValueEntity other = (PanelistNumberValueEntity) obj;
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

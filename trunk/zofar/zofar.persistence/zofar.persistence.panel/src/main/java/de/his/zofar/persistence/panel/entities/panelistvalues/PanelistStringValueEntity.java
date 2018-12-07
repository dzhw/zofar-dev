package de.his.zofar.persistence.panel.entities.panelistvalues;

import javax.persistence.Entity;

import de.his.zofar.persistence.panel.entities.PanelVariableEntity;
import de.his.zofar.persistence.panel.entities.PanelistEntity;

@Entity
public class PanelistStringValueEntity extends PanelistValueEntity {

    private String value;

    public PanelistStringValueEntity() {
        super();
    }

    public PanelistStringValueEntity(final PanelVariableEntity variable,
            final PanelistEntity panelist, final String value) {
        super(variable, panelist);
        this.value = value;
    }

    @Override
    public Object getValue() {
        return this.value;
    }

    public void setValue(final String value) {
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
        final PanelistStringValueEntity other = (PanelistStringValueEntity) obj;
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

package de.his.zofar.persistence.panel.entities;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.MapKeyJoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;

import org.hibernate.validator.constraints.Email;

import de.his.zofar.persistence.common.entities.BaseEntity;
import de.his.zofar.persistence.panel.entities.panelistvalues.PanelistValueEntity;

@Entity
@SequenceGenerator(initialValue = 1, name = "primary_key_generator",
sequenceName = "SEQ_PANELIST_ID")
public class PanelistEntity extends BaseEntity {

    @Email
    @Column(unique = true, nullable = false)
    private String eMail;

    @ManyToOne(optional = false, cascade = CascadeType.PERSIST)
    private PanelEntity panel;

    @OneToMany(mappedBy = "panelist", cascade = CascadeType.ALL)
    @MapKeyJoinColumn(name = "variable_id")
    private Map<PanelVariableEntity, PanelistValueEntity> variableValues;

    public void addVariableValue(final PanelistValueEntity value) {
        if (this.variableValues == null) {
            this.variableValues = new HashMap<PanelVariableEntity, PanelistValueEntity>();
        }
        this.variableValues.put(value.getVariable(), value);
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
        final PanelistEntity other = (PanelistEntity) obj;
        if (this.eMail == null) {
            if (other.eMail != null) {
                return false;
            }
        } else if (!this.eMail.equals(other.eMail)) {
            return false;
        }
        return true;
    }

    public String getEMail() {
        return this.eMail;
    }

    public PanelEntity getPanel() {
        return this.panel;
    }

    public Map<PanelVariableEntity, PanelistValueEntity> getVariableValues() {
        return this.variableValues;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result
                + ((this.eMail == null) ? 0 : this.eMail.hashCode());
        return result;
    }

    public void setEMail(final String eMail) {
        this.eMail = eMail;
    }

    public void setPanel(final PanelEntity panel) {
        this.panel = panel;
    }

    public void setVariableValues(
            final Map<PanelVariableEntity, PanelistValueEntity> variableValues) {
        this.variableValues = variableValues;
    }
}

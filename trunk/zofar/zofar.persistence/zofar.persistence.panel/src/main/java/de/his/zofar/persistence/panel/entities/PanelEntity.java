package de.his.zofar.persistence.panel.entities;

import static javax.persistence.CascadeType.ALL;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;

import de.his.zofar.persistence.common.entities.BaseEntity;

@Entity
@SequenceGenerator(initialValue = 1, name = "primary_key_generator",
sequenceName = "SEQ_PANEL_ID")
public class PanelEntity extends BaseEntity {

    private String name;

    @OneToMany(mappedBy = "panel", cascade = ALL)
    private List<PanelistEntity> panelists;

    public String getName() {
        return this.name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public List<PanelistEntity> getPanelists() {
        return this.panelists;
    }

    public void setPanelists(final List<PanelistEntity> panelists) {
        this.panelists = panelists;
    }

    public void addPanelist(final PanelistEntity panelist) {
        if (this.panelists == null) {
            this.panelists = new ArrayList<PanelistEntity>();
        }
        this.panelists.add(panelist);
    }
}

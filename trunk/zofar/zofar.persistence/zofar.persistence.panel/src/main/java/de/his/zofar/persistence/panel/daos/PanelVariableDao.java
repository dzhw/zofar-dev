package de.his.zofar.persistence.panel.daos;

import org.springframework.stereotype.Repository;

import de.his.zofar.persistence.panel.entities.PanelVariableEntity;
import de.his.zofar.persistence.panel.entities.QPanelVariableEntity;
import de.his.zofar.persistence.valuetype.daos.VariableDao;

@Repository
public interface PanelVariableDao extends VariableDao<PanelVariableEntity> {
    public static QPanelVariableEntity qPanelVariable = QPanelVariableEntity.panelVariableEntity;
}

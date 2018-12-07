package de.his.zofar.persistence.valuetype.daos;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import de.his.zofar.persistence.common.daos.GenericCRUDDao;
import de.his.zofar.persistence.valuetype.entities.QVariableEntity;
import de.his.zofar.persistence.valuetype.entities.VariableEntity;

@Repository
public interface VariableDao<T extends VariableEntity> extends GenericCRUDDao<T> {
    public static QVariableEntity qVariable = QVariableEntity.variableEntity;

    public Page<VariableEntity> findByName(String name, Pageable pageable);
}

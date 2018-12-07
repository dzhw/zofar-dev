package de.his.zofar.persistence.valuetype.daos;

import org.springframework.stereotype.Repository;

import de.his.zofar.persistence.common.daos.GenericCRUDDao;
import de.his.zofar.persistence.valuetype.entities.QValueTypeEntity;
import de.his.zofar.persistence.valuetype.entities.ValueTypeEntity;

@Repository
public interface ValueTypeDao extends GenericCRUDDao<ValueTypeEntity> {
    public static QValueTypeEntity qValueType = QValueTypeEntity.valueTypeEntity;
}

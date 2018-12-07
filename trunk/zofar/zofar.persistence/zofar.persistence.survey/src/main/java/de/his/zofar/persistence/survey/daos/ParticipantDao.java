package de.his.zofar.persistence.survey.daos;

import org.springframework.stereotype.Repository;

import de.his.zofar.persistence.common.daos.GenericCRUDDao;
import de.his.zofar.persistence.survey.entities.ParticipantEntity;
import de.his.zofar.persistence.survey.entities.QParticipantEntity;

@Repository
public interface ParticipantDao extends GenericCRUDDao<ParticipantEntity> {
    public static QParticipantEntity qParticipant = QParticipantEntity.participantEntity;
}

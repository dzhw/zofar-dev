/**
 *
 */
package de.his.zofar.persistence.survey.daos;

import org.springframework.stereotype.Repository;

import de.his.zofar.persistence.common.daos.GenericCRUDDao;
import de.his.zofar.persistence.survey.entities.participantvalues.ParticipantValueEntity;
import de.his.zofar.persistence.survey.entities.participantvalues.QParticipantValueEntity;

/**
 * @author le
 *
 */
@Repository
public interface ParticipantValueDao extends
        GenericCRUDDao<ParticipantValueEntity> {
    public static QParticipantValueEntity qParticipantValue = QParticipantValueEntity.participantValueEntity;
}

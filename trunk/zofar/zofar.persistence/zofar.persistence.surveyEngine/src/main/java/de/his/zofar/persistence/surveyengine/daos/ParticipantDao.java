/**
 *
 */
package de.his.zofar.persistence.surveyengine.daos;

import de.his.zofar.persistence.common.daos.GenericCRUDDao;
import de.his.zofar.persistence.surveyengine.entities.ParticipantEntity;
import de.his.zofar.persistence.surveyengine.entities.QParticipantEntity;

/**
 * @author le
 *
 */
public interface ParticipantDao extends GenericCRUDDao<ParticipantEntity> {
    public static QParticipantEntity participant = QParticipantEntity.participantEntity;

    public ParticipantEntity findByToken(String token);

    /*
     * Overwritten to be able to monitor run times with Jamon.
     * 
     * (non-Javadoc)
     * 
     * @see org.springframework.data.repository.CrudRepository#save(S)
     */
    @Override
    @SuppressWarnings("unchecked")
    public ParticipantEntity save(ParticipantEntity entity);
}

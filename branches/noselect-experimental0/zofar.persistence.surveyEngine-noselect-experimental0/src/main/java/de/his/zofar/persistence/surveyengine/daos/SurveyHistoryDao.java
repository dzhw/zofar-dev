/**
 *
 */
package de.his.zofar.persistence.surveyengine.daos;

import java.util.List;

import org.springframework.data.jpa.repository.Query;

import de.his.zofar.persistence.common.daos.GenericCRUDDao;
import de.his.zofar.persistence.surveyengine.entities.ParticipantEntity;
import de.his.zofar.persistence.surveyengine.entities.QSurveyHistoryEntity;
import de.his.zofar.persistence.surveyengine.entities.SurveyHistoryEntity;

/**
 * @author le
 *
 */
public interface SurveyHistoryDao extends GenericCRUDDao<SurveyHistoryEntity> {
    public static QSurveyHistoryEntity surveyHistory = QSurveyHistoryEntity.surveyHistoryEntity;

    /*
     * Overwritten to be able to monitor run times with Jamon.
     *
     * (non-Javadoc)
     *
     * @see org.springframework.data.repository.CrudRepository#save(S)
     */
    @Override
    @SuppressWarnings("unchecked")
    public SurveyHistoryEntity save(SurveyHistoryEntity surveyHistory);

//    public List<SurveyHistoryEntity> findByParticipant(ParticipantEntity participant);
    public List<SurveyHistoryEntity> findByParticipantOrderByTimestampAsc(ParticipantEntity participant);

    @Query ("select history from SurveyHistoryEntity history WHERE history.participant=?1 AND history.timestamp >= ALL(select MAX(temp.timestamp) from SurveyHistoryEntity temp WHERE temp.participant=?1 AND temp.page LIKE '%index%')")
    public List<SurveyHistoryEntity> findCustomByParticipantOrderByTimestampAsc(ParticipantEntity participant);
}

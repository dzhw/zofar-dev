/**
 *
 */
package de.his.zofar.persistence.surveyengine.daos;

import java.util.List;

import de.his.zofar.persistence.common.daos.GenericCRUDDao;
import de.his.zofar.persistence.surveyengine.entities.ParticipantEntity;
import de.his.zofar.persistence.surveyengine.entities.QSurveyDataEntity;
import de.his.zofar.persistence.surveyengine.entities.SurveyDataEntity;

/**
 * @author le
 *
 */
public interface SurveyDataDao extends GenericCRUDDao<SurveyDataEntity> {
    public static QSurveyDataEntity surveyData = QSurveyDataEntity.surveyDataEntity;

    public List<SurveyDataEntity> findByParticipant(
            ParticipantEntity participant);
}

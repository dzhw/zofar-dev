package de.his.zofar.persistence.survey.daos;

import org.springframework.stereotype.Repository;

import de.his.zofar.persistence.common.daos.GenericCRUDDao;
import de.his.zofar.persistence.survey.entities.QSurveyEntity;
import de.his.zofar.persistence.survey.entities.SurveyEntity;


@Repository
public interface SurveyDao extends GenericCRUDDao<SurveyEntity> {
    public static QSurveyEntity qSurvey = QSurveyEntity.surveyEntity;

}

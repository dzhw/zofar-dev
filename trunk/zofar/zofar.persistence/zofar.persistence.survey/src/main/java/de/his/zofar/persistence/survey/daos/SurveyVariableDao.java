package de.his.zofar.persistence.survey.daos;

import org.springframework.stereotype.Repository;

import de.his.zofar.persistence.survey.entities.QSurveyVariableEntity;
import de.his.zofar.persistence.survey.entities.SurveyVariableEntity;
import de.his.zofar.persistence.valuetype.daos.VariableDao;

@Repository
public interface SurveyVariableDao extends VariableDao<SurveyVariableEntity> {
    public static QSurveyVariableEntity qSurveyVariable = QSurveyVariableEntity.surveyVariableEntity;
}

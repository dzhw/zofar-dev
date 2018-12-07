package de.his.zofar.persistence.question.daos;

import org.springframework.stereotype.Repository;

import de.his.zofar.persistence.common.daos.GenericCRUDDao;
import de.his.zofar.persistence.question.entities.QQuestionEntity;
import de.his.zofar.persistence.question.entities.QuestionEntity;

@Repository
public interface QuestionDao extends GenericCRUDDao<QuestionEntity> {
    public static QQuestionEntity qQuestion = QQuestionEntity.questionEntity;

}

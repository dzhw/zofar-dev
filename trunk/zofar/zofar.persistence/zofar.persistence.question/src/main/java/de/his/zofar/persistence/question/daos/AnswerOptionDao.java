package de.his.zofar.persistence.question.daos;

import org.springframework.stereotype.Repository;

import de.his.zofar.persistence.common.daos.GenericCRUDDao;
import de.his.zofar.persistence.question.entities.components.AnswerOptionEntity;
import de.his.zofar.persistence.question.entities.components.QAnswerOptionEntity;
import de.his.zofar.persistence.question.entities.concrete.OpenQuestionEntity;

@Repository
public interface AnswerOptionDao extends GenericCRUDDao<AnswerOptionEntity> {
    public static QAnswerOptionEntity qAnswerOption = QAnswerOptionEntity.answerOptionEntity;

    public AnswerOptionEntity findByOpenQuestion(OpenQuestionEntity openQuestion);
}

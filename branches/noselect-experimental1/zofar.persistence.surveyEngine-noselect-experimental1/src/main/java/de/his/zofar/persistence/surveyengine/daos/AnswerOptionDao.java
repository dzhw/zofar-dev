/**
 *
 */
package de.his.zofar.persistence.surveyengine.daos;

import de.his.zofar.persistence.common.daos.GenericCRUDDao;
import de.his.zofar.persistence.surveyengine.entities.AnswerOptionEntity;
import de.his.zofar.persistence.surveyengine.entities.QAnswerOptionEntity;

/**
 * @author le
 *
 */
public interface AnswerOptionDao extends GenericCRUDDao<AnswerOptionEntity> {
    public static QAnswerOptionEntity answerOption = QAnswerOptionEntity.answerOptionEntity;

    public AnswerOptionEntity findByVariableNameAndAnswerOptionUid(
            String variableName, String answerOptionUid);
}

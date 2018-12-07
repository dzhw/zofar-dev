/**
 *
 */
package de.his.zofar.service.question.internal;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import de.his.zofar.persistence.question.daos.QuestionDao;
import de.his.zofar.service.common.exceptions.NotYetImplementedException;
import de.his.zofar.service.common.internal.InternalServiceInterface;
import de.his.zofar.service.question.model.Question;

/**
 * @author le
 *
 */
@Service
public class QuestionInternalService implements InternalServiceInterface {

    @Resource
    private QuestionDao questionDao;

    /*
     * (non-Javadoc)
     *
     * @see
     * de.his.hiob.service.question.internal.QuestionInternalService#loadQuestion
     * (java.lang.Long)
     */
    public Question loadQuestion(final Long id) {
        // final QuestionEntity entity = questionDao.findOne(id);
        // return mapper.map(entity, Question.class);
        throw new NotYetImplementedException();
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * de.his.hiob.service.question.internal.QuestionInternalService#saveQuestion
     * (de.his.hiob.model.question.entities.Question)
     */
    public Question saveQuestion(final Question question) {
        // QuestionEntity entityToSave = mapper.map(question,
        // QuestionEntity.class);
        // final QuestionEntity savedEntity = questionDao.save(entityToSave);
        // return mapper.map(savedEntity, Question.class);
        throw new NotYetImplementedException();
    }

}

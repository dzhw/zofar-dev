/**
 *
 */
package de.his.zofar.service.question.impl;

import javax.inject.Inject;

import org.dozer.Mapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import de.his.zofar.service.common.AbstractService;
import de.his.zofar.service.question.internal.QuestionInternalService;
import de.his.zofar.service.question.model.Question;
import de.his.zofar.service.question.service.QuestionService;

/**
 * @author Reitmann
 *
 */
@Service
public class QuestionServiceImpl extends AbstractService implements
        QuestionService {

    @Inject
    private QuestionInternalService questionPersistenceService;

    @Inject
    public QuestionServiceImpl(final Mapper mapper) {
        super(mapper);
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * de.his.zofar.service.question.impl.QuestionService#loadQuestion(java.
     * lang.Long)
     */
    @Override
    @Transactional(readOnly = true)
    public Question loadQuestion(final Long id) {
        // final Question question =
        // this.questionInternalService.loadQuestion(id);
        // if (question != null) {
        // return this.mapper.map(question, QuestionDTO.class);
        // }
        // return null;
        return this.questionPersistenceService.loadQuestion(id);
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * de.his.zofar.service.question.impl.QuestionService#saveQuestion(de.his
     * .zofar.service.question.model.Question)
     */
    @Override
    @Transactional
    public Question saveQuestion(final Question question) {
        // Question entity = this.mapper.map(question, Question.class);
        // entity = this.questionInternalService.saveQuestion(entity);
        // return this.mapper.map(entity, QuestionDTO.class);
        return this.questionPersistenceService.saveQuestion(question);
    }
}

/**
 * 
 */
package de.his.zofar.service.question.service;

import org.springframework.transaction.annotation.Transactional;

import de.his.zofar.service.question.model.Question;

/**
 * @author le
 *
 */
public interface QuestionService {

    @Transactional(readOnly = true)
    public abstract Question loadQuestion(Long id);

    @Transactional
    public abstract Question saveQuestion(Question question);

}
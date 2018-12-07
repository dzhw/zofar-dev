/**
 *
 */
package de.his.zofar.service.question.test.impl;

import javax.annotation.Resource;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.dao.OptimisticLockingFailureException;

import de.his.zofar.service.question.service.QuestionService;
import de.his.zofar.service.test.AbstractServiceTest;

/**
 * @author Reitmann
 *
 */
public class QuestionServiceImplTest extends AbstractServiceTest {

    @Resource
    private QuestionService questionService;

    @Test
    @Ignore
    // TODO le: don't ignore this
    public void testLoadQuestion() {
    }

    @Test(expected = OptimisticLockingFailureException.class)
    @Ignore
    // TODO le: don't ignore this
    public void testUpdateQuestion() {
    }
}

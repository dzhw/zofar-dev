/**
 *
 */
package de.his.zofar.service.question.test.internal;

import java.util.UUID;

import javax.inject.Inject;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import de.his.zofar.service.common.exceptions.NotYetImplementedException;
import de.his.zofar.service.question.internal.QuestionInternalService;
import de.his.zofar.service.question.model.Question;
import de.his.zofar.service.question.model.QuestionVariable;
import de.his.zofar.service.question.model.concrete.SingleChoiceQuestion;
import de.his.zofar.service.question.model.structure.InstructionText;
import de.his.zofar.service.question.model.structure.IntroductionText;
import de.his.zofar.service.question.model.structure.QuestionText;
import de.his.zofar.service.test.AbstractServiceTest;
import de.his.zofar.service.valuetype.model.Variable;

/**
 * @author le
 *
 */
public class QuestionInternalServiceTest extends AbstractServiceTest {

    /**
     *
     */
    @Inject
    private QuestionInternalService questionPersistenceService;

    /**
     * Test method for
     * {@link de.his.zofar.service.question.internal.QuestionInternalService#loadQuestion(java.lang.Long)}
     * .
     */
    // @Test
    @Test(expected = NotYetImplementedException.class)
    @Transactional(readOnly = true)
    @Rollback(value = true)
    public void testLoadQuestion() {
        // final Question result = this.questionPersistenceService
        // .loadQuestion(10L);
        // Assert.assertNull(result);

        final Question question = this.questionPersistenceService
                .saveQuestion(createQuestion());
        Assert.assertNotNull(question.getId());

        final Question anotherQuestion = this.questionPersistenceService
                .loadQuestion(question.getId());
        Assert.assertNotNull(anotherQuestion.getId());
    }

    /**
     * Test method for
     * {@link de.his.zofar.service.question.internal.QuestionInternalService#saveQuestion(de.his.hiob.model.question.models.Question)}
     * .
     */
    // @Test(expected = OptimisticLockingFailureException.class)
    @Test(expected = NotYetImplementedException.class)
    @Transactional
    @Rollback(value = true)
    public void testSaveQuestion() {
        final Question question = this.questionPersistenceService
                .saveQuestion(createQuestion());
        Assert.assertNotNull(question.getId());

        final Question oldQuestion = question;
        oldQuestion.addHeaderElement(new QuestionText("Modified"));

        final Question firstModifiedQuestion = this.questionPersistenceService
                .saveQuestion(oldQuestion);

        oldQuestion.addHeaderElement(new QuestionText("Hurz"));
        final Question secondModifiedQuestion = this.questionPersistenceService
                .saveQuestion(oldQuestion);

        // QUIZ: never reached: Do you know why?
        Assert.assertNotEquals(firstModifiedQuestion.getHeader(),
                secondModifiedQuestion.getHeader());
    }

    private SingleChoiceQuestion createQuestion() {
        final SingleChoiceQuestion question = new SingleChoiceQuestion();
        question.addHeaderElement(new InstructionText("You have todo this"));
        question.addHeaderElement(new IntroductionText("Hello I am a question!"));
        question.addHeaderElement(new QuestionText("What is your name?"));

        return question;
    }

    @SuppressWarnings("unused")
    private Variable createVariable() {
        final QuestionVariable questionVariable = new QuestionVariable();
        questionVariable.setName("V1");
        questionVariable.setUuid(UUID.randomUUID().toString());

        return questionVariable;
    }

}

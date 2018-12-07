package de.his.zofar.persistence.question.test.daos;

import java.util.List;

import javax.annotation.Resource;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import de.his.zofar.persistence.question.daos.QuestionDao;
import de.his.zofar.persistence.question.entities.QuestionEntity;
import de.his.zofar.persistence.question.entities.components.AnswerOptionEntity;
import de.his.zofar.persistence.question.entities.concrete.MultipleChoiceQuestionEntity;
import de.his.zofar.persistence.question.entities.concrete.OpenQuestionEntity;
import de.his.zofar.persistence.question.entities.concrete.SingleChoiceQuestionEntity;
import de.his.zofar.persistence.test.daos.AbstractDaoTest;

public class QuestionDaoTest extends AbstractDaoTest {
    @Resource
    private QuestionDao questionDao;

    @Test
    @Transactional
    @Rollback(value = true)
    public void testOpenSave() {
        final QuestionEntity openQuestion = createOpenQuestion();
        this.questionDao.save(openQuestion);

        List<QuestionEntity> result = this.questionDao.findAll();
        Assert.assertEquals(1, result.size());
    }

    @Test
    @Transactional
    @Rollback(value = true)
    public void testSingleSave() {
        final QuestionEntity singleQuestion = createSingleChoiceQuestion();
        this.questionDao.save(singleQuestion);

        List<QuestionEntity> result = this.questionDao.findAll();
        Assert.assertEquals(1, result.size());

        Class<? extends QuestionEntity> clazz = result.get(0).getClass();
        Assert.assertTrue((SingleChoiceQuestionEntity.class).isAssignableFrom(clazz));

        Assert.assertEquals(2, ((SingleChoiceQuestionEntity) result.get(0))
                .getAnswerOptions().size());
    }

    @Test
    @Transactional
    @Rollback(value = true)
    public void testMultipleSave() {
        final QuestionEntity multipleQuestion = createMultipleChoiceQuestion();
        this.questionDao.save(multipleQuestion);

        List<QuestionEntity> result = this.questionDao.findAll();
        // there must be 2 questions! 1 multiple choice question and its child
        // the single choice question
        Assert.assertEquals(2, result.size());
    }

    private QuestionEntity createOpenQuestion() {
        final QuestionEntity question = new OpenQuestionEntity();
        question.setQuestionText("Wat, wer bist denn du?");
        return question;
    }

    private QuestionEntity createSingleChoiceQuestion() {
        final SingleChoiceQuestionEntity question = new SingleChoiceQuestionEntity();
        question.setQuestionText("Wer bin ich?");
        question.addAnswerOption(createAnswerOption1());
        question.addAnswerOption(createAnswerOption2());
        return question;
    }

    private QuestionEntity createMultipleChoiceQuestion() {
        final MultipleChoiceQuestionEntity question = new MultipleChoiceQuestionEntity();
        question.setQuestionText("Ich bin eine Mehrfachnennung. Wie viele Fragen habe ich?");
        question.addQuestion((SingleChoiceQuestionEntity) createSingleChoiceQuestion());
        return question;
    }

    private AnswerOptionEntity createAnswerOption1() {
        final AnswerOptionEntity answerOption = new AnswerOptionEntity();
        answerOption.setDisplayText("keks1");
        return answerOption;
    }

    private AnswerOptionEntity createAnswerOption2() {
        final AnswerOptionEntity answerOption = new AnswerOptionEntity();
        answerOption.setDisplayText("keks2");
        return answerOption;
    }
}

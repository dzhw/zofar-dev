package de.his.zofar.persistence.question.test.daos;

import java.util.List;

import javax.annotation.Resource;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import de.his.zofar.persistence.question.daos.AnswerOptionDao;
import de.his.zofar.persistence.question.entities.components.AnswerOptionEntity;
import de.his.zofar.persistence.question.entities.concrete.OpenQuestionEntity;
import de.his.zofar.persistence.test.daos.AbstractDaoTest;

public class AnswerOptionDaoTest extends AbstractDaoTest {
    @Resource
    private AnswerOptionDao answerOptionDao;

    private AnswerOptionEntity createAnswerOption() {
        final AnswerOptionEntity answerOption = new AnswerOptionEntity();
        answerOption.setDisplayText("keks");
        answerOption.setOpenQuestion(createOpenQuestion());
        return answerOption;
    }

    private OpenQuestionEntity createOpenQuestion() {
        final OpenQuestionEntity question = new OpenQuestionEntity();
        question.setQuestionText("Wat, wer bist denn du?");
        return question;
    }

    @Test
    @Transactional
    @Rollback(value = true)
    public void testCreateAnswerOption() {
        final AnswerOptionEntity answerOption = createAnswerOption();
        this.answerOptionDao.save(answerOption);

        Assert.assertNotNull(this.answerOptionDao
                .findByOpenQuestion(answerOption.getOpenQuestion()));
        Assert.assertEquals(answerOption, this.answerOptionDao
                .findByOpenQuestion(answerOption.getOpenQuestion()));
    }

    @Test
    @Transactional()
    @Rollback(value = true)
    public void testModifiyAnswerOption() {
        AnswerOptionEntity answerOption = createAnswerOption();
        answerOption = this.answerOptionDao.save(answerOption);

        answerOption.setDisplayText("Hurz!");

        // this.answerOptionDao.save(answerOption);

        final List<AnswerOptionEntity> answerOptions = this.answerOptionDao.findAll();
        // What to assert here? What happens here?
        Assert.assertNotSame("keks", answerOptions.get(0).getDisplayText());
    }

}

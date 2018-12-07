package de.his.zofar.persistence.survey.test.daos;

import javax.annotation.Resource;
import javax.validation.ConstraintViolationException;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import de.his.zofar.persistence.survey.daos.ParticipantDao;
import de.his.zofar.persistence.survey.entities.ParticipantEntity;
import de.his.zofar.persistence.survey.entities.SurveyEntity;
import de.his.zofar.persistence.test.daos.AbstractDaoTest;

public class ParticipantDaoTest extends AbstractDaoTest {

    @Resource
    private ParticipantDao participantDao;

    private ParticipantEntity createParticipant(final SurveyEntity survey) {
        final ParticipantEntity participant = new ParticipantEntity();
        participant.setSurvey(survey);
        participant.setEMail("reitmann@his.de");

        return participant;
    }

    @Test(expected = ConstraintViolationException.class)
    @Transactional
    @Rollback(true)
    public void createParticipantWithWrongEmail() {
        final ParticipantEntity participant = createParticipant(createSurvey());
        this.participantDao.save(participant);

        participant.setEMail("Hurz!");
        this.participantDao.flush();

        Assert.fail("There should have occurred an Exception!");
    }

    private SurveyEntity createSurvey() {
        final SurveyEntity survey = new SurveyEntity("NEPS");
        return survey;
    }

}

package de.his.zofar.service.survey.test.internal;

import javax.inject.Inject;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.transaction.annotation.Transactional;

import de.his.zofar.persistence.survey.entities.SurveyState;
import de.his.zofar.service.common.exceptions.NotYetImplementedException;
import de.his.zofar.service.survey.internal.SurveyInternalService;
import de.his.zofar.service.survey.model.Survey;
import de.his.zofar.service.test.AbstractServiceTest;

public class SurveyInternalServiceTest extends AbstractServiceTest {

    @Inject
    private SurveyInternalService surveyInternalService;

    private Survey createSurvey() {
        final Survey survey = new Survey();
        survey.setName("first");
        survey.setState(SurveyState.INFIELD);

        return survey;
    }

    @Test(expected = NotYetImplementedException.class)
    @Transactional
    public void testCreate() {
        final Survey surveyTest = this.surveyInternalService
                .create(createSurvey());
        Assert.assertNotNull(surveyTest.getName());
    }

}

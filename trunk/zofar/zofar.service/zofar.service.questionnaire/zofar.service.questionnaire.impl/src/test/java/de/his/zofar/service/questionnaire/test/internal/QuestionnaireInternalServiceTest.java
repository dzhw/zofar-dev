/**
 *
 */
package de.his.zofar.service.questionnaire.test.internal;

import javax.inject.Inject;

import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import de.his.zofar.service.common.exceptions.NotYetImplementedException;
import de.his.zofar.service.questionnaire.internal.QuestionnaireInternalService;
import de.his.zofar.service.questionnaire.model.Questionnaire;
import de.his.zofar.service.test.AbstractServiceTest;

/**
 * @author le
 *
 */
public class QuestionnaireInternalServiceTest extends AbstractServiceTest {
    private static final Logger LOGGER = LoggerFactory
            .getLogger(QuestionnaireInternalServiceTest.class);

    @Inject
    private QuestionnaireInternalService questionnairePersistenceService;

    @Test(expected = NotYetImplementedException.class)
    @Transactional
    @Rollback(true)
    public void test() {
        final Questionnaire questionnaire = questionnairePersistenceService
                .searchAll(null).getContent().get(0);

        LOGGER.debug("{}", questionnaire);

        Assert.assertNotNull(questionnaire);
    }
}

/**
 *
 */
package de.his.zofar.service.questionnaire.internal;

import java.util.UUID;

import org.springframework.stereotype.Service;

import de.his.zofar.service.common.exceptions.NotYetImplementedException;
import de.his.zofar.service.common.internal.InternalServiceInterface;
import de.his.zofar.service.common.model.PageDTO;
import de.his.zofar.service.questionnaire.model.Questionnaire;
import de.his.zofar.service.questionnaire.model.QuestionnairePage;
import de.his.zofar.service.questionnaire.model.QuestionnaireQuery;

/**
 * @author le
 *
 */
@Service
public class QuestionnaireInternalService implements InternalServiceInterface {

    /**
     * loading a questionnaire by the name of the questionnaire.
     *
     * @param uuid
     *            the questionnaires uuid
     * @return the questionnaire identified by the uuid
     */
    public Questionnaire loadQuestionnaire(final UUID uuid) {
        throw new NotYetImplementedException();
    }

    /**
     * search for questionnaires
     *
     * @param query
     * @return
     */
    public PageDTO<Questionnaire> searchAll(final QuestionnaireQuery query) {
        throw new NotYetImplementedException();
    }

    /**
     * @param pageUUID
     * @return
     */
    public QuestionnairePage loadPage(final UUID pageUUID) {
        throw new NotYetImplementedException();
    }

    /**
     * @param questionnaire
     * @return
     */
    public QuestionnairePage loadFirstPage(final Questionnaire questionnaire) {
        throw new NotYetImplementedException();
    }

}

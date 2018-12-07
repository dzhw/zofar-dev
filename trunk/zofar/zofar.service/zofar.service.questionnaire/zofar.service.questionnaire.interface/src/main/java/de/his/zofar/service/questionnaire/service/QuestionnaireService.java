/**
 * 
 */
package de.his.zofar.service.questionnaire.service;

import java.util.UUID;

import org.springframework.transaction.annotation.Transactional;

import de.his.zofar.service.common.model.PageDTO;
import de.his.zofar.service.questionnaire.model.Questionnaire;
import de.his.zofar.service.questionnaire.model.QuestionnairePage;
import de.his.zofar.service.questionnaire.model.QuestionnaireQuery;

/**
 * @author le
 *
 */
public interface QuestionnaireService {

    /**
     * loading a questionnaire by the name of the questionnaire
     *
     * @param name
     * @return
     */
    @Transactional(readOnly = true)
    public abstract Questionnaire loadQuestionnaire(UUID uuid);

    /**
     * @param query
     * @return
     */
    @Transactional(readOnly = true)
    public abstract PageDTO<Questionnaire> searchAll(QuestionnaireQuery query);

    /**
     * @param pageUUID
     * @return
     */
    @Transactional(readOnly = true)
    public abstract QuestionnairePage loadPage(UUID pageUUID);

    /**
     * @param questionnaire
     * @return
     */
    @Transactional(readOnly = true)
    public abstract QuestionnairePage loadFirstPage(Questionnaire questionnaire);

}
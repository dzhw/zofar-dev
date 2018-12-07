/**
 *
 */
package de.his.zofar.service.questionnaire.impl;

import java.util.UUID;

import javax.inject.Inject;

import org.dozer.Mapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import de.his.zofar.service.common.AbstractService;
import de.his.zofar.service.common.model.PageDTO;
import de.his.zofar.service.questionnaire.internal.QuestionnaireInternalService;
import de.his.zofar.service.questionnaire.model.Questionnaire;
import de.his.zofar.service.questionnaire.model.QuestionnairePage;
import de.his.zofar.service.questionnaire.model.QuestionnaireQuery;
import de.his.zofar.service.questionnaire.service.QuestionnaireService;

/**
 * @author le
 *
 */
@Service
public class QuestionnaireServiceImpl extends AbstractService implements
        QuestionnaireService {

    @Inject
    private QuestionnaireInternalService questionnaireInternalService;

    @Inject
    public QuestionnaireServiceImpl(final Mapper mapper) {
        super(mapper);
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * de.his.hiob.service.questionnaire.services.QuestionnaireService#loadFirstPage
     * (de.his.zofar.service.questionnaire.model.Questionnaire)
     */
    @Override
    @Transactional(readOnly = true)
    public QuestionnairePage loadFirstPage(final Questionnaire questionnaire) {
        return this.questionnaireInternalService.loadFirstPage(questionnaire);
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * de.his.hiob.service.questionnaire.services.QuestionnaireService#loadPage
     * (java.util.UUID)
     */
    @Override
    @Transactional(readOnly = true)
    public QuestionnairePage loadPage(final UUID pageUUID) {
        return this.questionnaireInternalService.loadPage(pageUUID);
    }

    /*
     * (non-Javadoc)
     *
     * @see de.his.hiob.service.questionnaire.services.QuestionnaireService#
     * loadQuestionnaire(java.util.UUID)
     */
    @Override
    @Transactional(readOnly = true)
    public Questionnaire loadQuestionnaire(final UUID uuid) {
        return this.questionnaireInternalService.loadQuestionnaire(uuid);
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * de.his.hiob.service.questionnaire.services.QuestionnaireService#searchAll
     * (de.his.zofar.service.questionnaire.model.QuestionnaireQuery)
     */
    @Override
    @Transactional(readOnly = true)
    public PageDTO<Questionnaire> searchAll(final QuestionnaireQuery query) {
        // TODO le: QuestionnaireQueryDTO doesn't do anything
        return this.questionnaireInternalService.searchAll(query);
    }

}

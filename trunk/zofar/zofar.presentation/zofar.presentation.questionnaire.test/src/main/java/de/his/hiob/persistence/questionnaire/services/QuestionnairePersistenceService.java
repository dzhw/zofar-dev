/**
 *
 */
package de.his.hiob.persistence.questionnaire.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import de.his.hiob.model.common.models.PageDTO;
import de.his.hiob.model.question.models.concrete.BooleanQuestion;
import de.his.hiob.model.question.models.concrete.MultipleChoiceQuestion;
import de.his.hiob.model.question.models.concrete.OpenQuestion;
import de.his.hiob.model.question.models.concrete.SingleChoiceQuestion;
import de.his.hiob.model.question.models.structure.StructuredElement;
import de.his.hiob.model.question.models.structure.Text;
import de.his.hiob.model.questionnaire.models.Questionnaire;
import de.his.hiob.model.questionnaire.models.QuestionnairePage;
import de.his.hiob.model.questionnaire.models.QuestionnaireQuery;
import de.his.hiob.model.questionnaire.models.Transition;
import de.his.hiob.service.question.services.QuestionService;

/**
 * @author le
 *
 */
@Service
public class QuestionnairePersistenceService {
    /**
     * the logger.
     */
    private static final Logger LOGGER = LoggerFactory
            .getLogger(QuestionnairePersistenceService.class);

    /**
     *
     */
    @Inject
    private QuestionService questionService;

    /*
     * (non-Javadoc)
     *
     * @see
     * de.his.hiob.service.questionnaire.internal.QuestionnaireInternalService
     * #loadQuestionnaire(java.util.UUID)
     */
    public Questionnaire loadQuestionnaire(final UUID uuid) {
        if (!questionnaires.containsKey(uuid)) {
            throw new IllegalArgumentException(
                    "there is no questionnaire with uuid " + uuid);
        }

        return questionnaires.get(uuid);
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * de.his.hiob.service.questionnaire.internal.QuestionnaireInternalService
     * #searchAll
     * (de.his.hiob.service.questionnaire.external.dtos.QuestionnaireQueryDTO)
     */
    public PageDTO<Questionnaire> searchAll(final QuestionnaireQuery query) {
        final PageDTO<Questionnaire> result = new PageDTO<Questionnaire>();
        final List<Questionnaire> s = new ArrayList<Questionnaire>();
        s.addAll(questionnaires.values());

        result.setContent(s);

        return result;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * de.his.hiob.service.questionnaire.internal.QuestionnaireInternalService
     * #loadPage(java.util.UUID)
     */
    @Transactional(readOnly = true)
    public QuestionnairePage loadPage(final UUID pageUUID) {
        if (!pages.containsKey(pageUUID)) {
            throw new IllegalArgumentException("there is no page with uuid "
                    + pageUUID);
        }
        return pages.get(pageUUID);
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * de.his.hiob.service.questionnaire.internal.QuestionnaireInternalService
     * #loadFirstPage
     * (de.his.hiob.service.questionnaire.external.dtos.QuestionnaireDTO)
     */
    public QuestionnairePage loadFirstPage(final Questionnaire questionnaire) {
        return pages.get(questionnaire.getFirstPageUuid());
    }

    // From here on everything is a fake!

    // TODO le: remove fake and implement daos, persistence services and calls
    // to the services of the appropriate components
    private Map<UUID, Questionnaire> questionnaires;
    private Map<UUID, QuestionnairePage> pages;

    @PostConstruct
    private void init() {
        LOGGER.warn("You are using a MOCK!");
        questionnaires = new HashMap<UUID, Questionnaire>();
        pages = new HashMap<UUID, QuestionnairePage>();

        final Questionnaire questionnaire = createQuestionnaireOne();

        questionnaires.put(questionnaire.getUuid(), questionnaire);
    }

    /**
     * @return
     */
    private Questionnaire createQuestionnaireOne() {
        final Questionnaire questionnaire = new Questionnaire();

        // creating the first page (gender question)
        final QuestionnairePage pageOne = createPageOne();
        // creating the first alternative of the first page (kids question)
        final QuestionnairePage pageTwo = createPageTwo();
        // creating the second alternative of the first page (like pie question)
        final QuestionnairePage pageThree = createPageThree();
        // creating the following page of third page (why don't like pie
        // question)
        final QuestionnairePage pageFour = createPageFour();
        // creates a multiple choice question (what dessert do you like)
        final QuestionnairePage pageFive = createPageFive();
        // end page
        final QuestionnairePage pageEnd = createEndPage();

        // setting the transition from pageOne to the second page (gender =>
        // kids)
        final Transition transitionOne = new Transition();
        transitionOne.setNextPageUuid(pageTwo.getUuid());
        transitionOne.setCondition("__gender != 1");
        pageOne.addTransition(transitionOne);
        // setting the transition from pageOne to the third page (gender => like
        // pie)
        final Transition transitionTwo = new Transition();
        transitionTwo.setNextPageUuid(pageThree.getUuid());
        transitionTwo.setCondition("__gender == 1");
        pageOne.addTransition(transitionTwo);
        // setting the transition from pageThree to pageFour (like pie => why
        // don't like pie)
        final Transition transitionThree = new Transition();
        transitionThree.setNextPageUuid(pageFour.getUuid());
        transitionThree.setCondition("__likepie != 1");
        pageThree.addTransition(transitionThree);
        final Transition transitionFour = new Transition();
        transitionFour.setNextPageUuid(pageFive.getUuid());
        transitionFour.setCondition("__likepie == 1");
        pageThree.addTransition(transitionFour);
        // setting transition from pageFour to pageFive (why don't like pie =>
        // what dessert do you like)
        final Transition transitionFive = new Transition();
        transitionFive.setNextPageUuid(pageFive.getUuid());
        pageFour.addTransition(transitionFive);
        // (what dessert do you like => end page)
        final Transition transitionEnd = new Transition();
        transitionEnd.setNextPageUuid(pageEnd.getUuid());
        pageFive.addTransition(transitionEnd);
        pageTwo.addTransition(transitionEnd);

        questionnaire.addPage(pageOne);
        questionnaire.addPage(pageTwo);
        questionnaire.addPage(pageThree);
        questionnaire.addPage(pageFour);
        questionnaire.addPage(pageFive);

        pages.put(pageOne.getUuid(), pageOne);
        pages.put(pageTwo.getUuid(), pageTwo);
        pages.put(pageThree.getUuid(), pageThree);
        pages.put(pageFour.getUuid(), pageFour);
        pages.put(pageFive.getUuid(), pageFive);
        pages.put(pageEnd.getUuid(), pageEnd);

        return questionnaire;
    }

    /**
     * the gender question.
     *
     * @return
     */
    private QuestionnairePage createPageOne() {
        final QuestionnairePage page = new QuestionnairePage();
        final SingleChoiceQuestion question = (SingleChoiceQuestion) questionService
                .loadQuestion(Long.valueOf(4));
        question.getVariable().setName("gender");

        // setting a open question for the unknown answer option
        final OpenQuestion openQuestion = (OpenQuestion) questionService
                .loadQuestion(Long.valueOf(6));
        openQuestion.getVariable().setName("gender-open");
        question.getAnswerOptions().get(2).setOpenQuestion(openQuestion);

        page.addQuestion(question);
        page.setIsFirstPage(true);
        return page;
    }

    /**
     * the how many kids question.
     *
     * @return
     */
    private QuestionnairePage createPageTwo() {
        final QuestionnairePage page = new QuestionnairePage();
        final SingleChoiceQuestion question = (SingleChoiceQuestion) questionService
                .loadQuestion(Long.valueOf(1));
        question.getVariable().setName("kids");
        page.addQuestion(question);
        return page;
    }

    /**
     * the pie question.
     *
     * @return
     */
    private QuestionnairePage createPageThree() {
        final QuestionnairePage page = new QuestionnairePage();
        final SingleChoiceQuestion question = (SingleChoiceQuestion) questionService
                .loadQuestion(Long.valueOf(3));
        question.getVariable().setName("likepie");
        page.addQuestion(question);
        return page;
    }

    /**
     * the why don't like pie question.
     *
     * @return
     */
    private QuestionnairePage createPageFour() {
        final QuestionnairePage page = new QuestionnairePage();
        final OpenQuestion question = (OpenQuestion) questionService
                .loadQuestion(Long.valueOf(2));
        question.getVariable().setName("whynopie");
        page.addQuestion(question);
        return page;
    }

    /**
     * the dessert question.
     *
     * @return
     */
    private QuestionnairePage createPageFive() {
        final QuestionnairePage page = new QuestionnairePage();
        final MultipleChoiceQuestion question = (MultipleChoiceQuestion) questionService
                .loadQuestion(Long.valueOf(5));
        int index = 0;
        for (final BooleanQuestion child : question.getQuestions()) {
            child.getVariable().setName("dessert_" + index++);
            for (final StructuredElement element : child.getHeader()) {
                if (element.getContent().equals("key lime pie")) {
                    child.setVisibilityCondition("__likepie != 1");
                }
            }
        }
        page.addQuestion(question);
        return page;
    }

    /**
     * @return
     */
    private QuestionnairePage createEndPage() {
        final QuestionnairePage page = new QuestionnairePage();
        page.addHeaderElement(new Text("Ende"));
        final Text forFemale = new Text("For female only.");
        forFemale.setVisibilityCondition("__gender == 1");
        page.addHeaderElement(forFemale);
        final Text forMale = new Text("For male only.");
        forMale.setVisibilityCondition("__gender == 2");
        page.addHeaderElement(forMale);
        return page;
    }

}

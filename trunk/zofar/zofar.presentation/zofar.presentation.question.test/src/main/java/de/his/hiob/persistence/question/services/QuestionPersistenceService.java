/**
 *
 */
package de.his.hiob.persistence.question.services;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import de.his.hiob.model.question.models.Question;
import de.his.hiob.model.question.models.QuestionVariable;
import de.his.hiob.model.question.models.concrete.BooleanQuestion;
import de.his.hiob.model.question.models.concrete.MultipleChoiceQuestion;
import de.his.hiob.model.question.models.concrete.OpenQuestion;
import de.his.hiob.model.question.models.concrete.SingleChoiceQuestion;
import de.his.hiob.model.question.models.structure.InstructionText;
import de.his.hiob.model.question.models.structure.QuestionText;
import de.his.hiob.service.valuetype.services.ValueTypeService;

/**
 * this class is a mock up of the QuestionInternalService and must be replaced
 * by the actual implementation of the service.
 *
 * @author le
 *
 */
@Service
public class QuestionPersistenceService {

    /**
     *
     */
    private static final Logger LOGGER = LoggerFactory
            .getLogger(QuestionPersistenceService.class);

    /**
     *
     */
    @Inject
    private ValueTypeService valueTypeService;

    /*
     * (non-Javadoc)
     *
     * @see
     * de.his.hiob.service.question.internal.QuestionInternalService#loadQuestion
     * (java.lang.Long)
     */
    public Question loadQuestion(final Long id) {
        if (!questions.containsKey(id)) {
            throw new IllegalArgumentException("no question exist with id "
                    + id);
        }
        return questions.get(id);
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * de.his.hiob.service.question.internal.QuestionInternalService#saveQuestion
     * (de.his.hiob.model.question.entities.Question)
     */
    public Question saveQuestion(final Question question) {
        if (question.getId() == null) {
            question.setId(getNextId());
        }
        questions.put(question.getId(), question);

        return question;
    }

    //
    // -------------------------------------- from here on everything is a MOCK
    //

    /**
     * the fake db.
     */
    private final Map<Long, Question> questions =
            new HashMap<Long, Question>();

    /**
     * init the fake DB.
     */
    @PostConstruct
    private void init() {
        LOGGER.warn("You are using a MOCK!");
        createHowManyKidsQuestion();
        createWhyDontLikePieQuestion();
        createLikePieQuestion();
        createAskGenderQuestion();
        createWhichDessertQuestion();
        createGenderUnknownOpenQuestion();
    }

    /**
     * create a question about how many kids does on have.
     */
    private void createHowManyKidsQuestion() {
        final SingleChoiceQuestion question = new SingleChoiceQuestion();
        question.setId(getNextId());

        // creating the variable
        final QuestionVariable variable = new QuestionVariable();
        variable.setUuid(UUID.randomUUID().toString());
        variable.setValueType(valueTypeService
                .loadByIdentifier("integerscale1to5"));

        // set question attributes
        question.addHeaderElement(new QuestionText(
                "How many children do you have?"));
        question.setVariable(variable);

        questions.put(question.getId(), question);
    }

    /**
     * create a why don't you like pie question.
     */
    private void createWhyDontLikePieQuestion() {
        final OpenQuestion question = new OpenQuestion();
        question.setId(getNextId());

        // creating the variable
        final QuestionVariable variable = new QuestionVariable();
        variable.setUuid(UUID.randomUUID().toString());
        variable.setValueType(valueTypeService
                .loadByIdentifier("defaultemptystring"));

        // set question attributes
        question.addHeaderElement(new QuestionText("Why don't your __kids kids like pie?"));
        question.addHeaderElement(new InstructionText(
                "Please give us the reason why you don't like pie."));
        question.setVariable(variable);

        questions.put(question.getId(), question);
    }

    /**
     * creates a like pie question.
     */
    private void createLikePieQuestion() {
        final SingleChoiceQuestion question = new SingleChoiceQuestion();
        question.setId(getNextId());

        // creating the variable
        final QuestionVariable variable = new QuestionVariable();
        variable.setUuid(UUID.randomUUID().toString());
        variable.setValueType(valueTypeService
                .loadByIdentifier("yesnomaybe"));

        // set question attributes
        question.addHeaderElement(new QuestionText("Do you like pie?"));
        question.setVariable(variable);

        questions.put(question.getId(), question);
    }

    /**
     * creates a question about the gender.
     */
    private void createAskGenderQuestion() {
        final SingleChoiceQuestion question = new SingleChoiceQuestion();
        question.setId(getNextId());

        // creating the variable
        final QuestionVariable variable = new QuestionVariable();
        variable.setUuid(UUID.randomUUID().toString());
        variable.setValueType(valueTypeService
                .loadByIdentifier("gender"));

        // set attributes of the question
        question.addHeaderElement(new QuestionText(
                "What gender do you have?"));
        question.setVariable(variable);

        questions.put(question.getId(), question);
    }

    /**
     * creates a question about which dessert one likes.
     */
    private void createWhichDessertQuestion() {
        final MultipleChoiceQuestion question =
                new MultipleChoiceQuestion();
        question.setId(getNextId());

        // list of desserts
        final String[] desserts =
                new String[] { "cupcake", "donut", "éclair", "frozen yoghurt",
                        "gingerbread", "honeycomb", "ice cream sandwich",
                        "jelly bean", "key lime pie" };

        for (final String dessert : desserts) {
            // creating the variable
            final QuestionVariable variable = new QuestionVariable();
            variable.setUuid(UUID.randomUUID().toString());
            variable.setValueType(valueTypeService
                    .loadByIdentifier("defaultbooleantype"));

            // creating child question
            final BooleanQuestion childQuestion =
                    new BooleanQuestion(variable);
            childQuestion.addHeaderElement(new QuestionText(dessert));

            // adding child question
            question.addQuestion(childQuestion);
        }

        // set attributes of the question
        question.addHeaderElement(new QuestionText(
                "Which dessert do you like then?"));
        question.addHeaderElement(new InstructionText(
                "Please select at least one of these delicious desserts."));

        questions.put(question.getId(), question);
    }

    /**
     * creates a gender open question (answer option 'unknown').
     */
    private void createGenderUnknownOpenQuestion() {
        final OpenQuestion question = new OpenQuestion();
        question.setId(getNextId());

        // creating the variable
        final QuestionVariable variable = new QuestionVariable();
        variable.setUuid(UUID.randomUUID().toString());
        variable.setValueType(valueTypeService
                .loadByIdentifier("defaultemptystring"));

        question.setVariable(variable);

        questions.put(question.getId(), question);
    }

    /**
     * @return the next id
     */
    private Long getNextId() {
        return new Long(questions.size() + 1);
    }

}

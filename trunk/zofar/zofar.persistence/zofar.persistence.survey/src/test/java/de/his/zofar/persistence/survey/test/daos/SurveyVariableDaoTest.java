package de.his.zofar.persistence.survey.test.daos;

import java.util.List;
import java.util.UUID;

import javax.annotation.Resource;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import de.his.zofar.persistence.question.entities.QuestionEntity;
import de.his.zofar.persistence.question.entities.concrete.OpenQuestionEntity;
import de.his.zofar.persistence.survey.entities.ParticipantEntity;
import de.his.zofar.persistence.survey.entities.SurveyEntity;
import de.his.zofar.persistence.survey.entities.SurveyVariableEntity;
import de.his.zofar.persistence.survey.entities.participantvalues.ParticipantNumberValueEntity;
import de.his.zofar.persistence.survey.entities.participantvalues.ParticipantStringValueEntity;
import de.his.zofar.persistence.test.daos.AbstractDaoTest;
import de.his.zofar.persistence.valuetype.daos.VariableDao;
import de.his.zofar.persistence.valuetype.entities.MeasurementLevel;
import de.his.zofar.persistence.valuetype.entities.NumberValueTypeEntity;
import de.his.zofar.persistence.valuetype.entities.StringValueTypeEntity;
import de.his.zofar.persistence.valuetype.entities.VariableEntity;

public class SurveyVariableDaoTest extends AbstractDaoTest {
    @Resource
    private VariableDao<VariableEntity> variableDao;

    private NumberValueTypeEntity createNumberValueType() {
        final NumberValueTypeEntity myValueType = new NumberValueTypeEntity();
        myValueType.setDecimalPlaces(0);
        myValueType.setDescription("Renés erster Variablentyp.");
        myValueType.setIdentifier("HIS4911");
        myValueType.setMinimum(0L);
        myValueType.setMaximum(10L);
        myValueType.setMeasurementLevel(MeasurementLevel.ORDINAL);
        return myValueType;
    }

    private SurveyVariableEntity createNumberVariable(
            final NumberValueTypeEntity numberValueType) {
        final SurveyVariableEntity numberVariable = new SurveyVariableEntity();
        numberVariable.setUuid(UUID.randomUUID().toString());
        numberVariable.setName("V2");
        numberVariable.setValueType(numberValueType);
        numberVariable.setQuestion(createQuestion());

        return numberVariable;
    }

    private ParticipantNumberValueEntity createNumberVariableValue(
            final SurveyVariableEntity numberVariable, final ParticipantEntity participant) {
        final ParticipantNumberValueEntity variableValue = new ParticipantNumberValueEntity(
                numberVariable, participant, 5L);

        return variableValue;
    }

    private ParticipantEntity createParticipant(final SurveyEntity survey) {
        final ParticipantEntity participant = new ParticipantEntity();
        participant.setSurvey(survey);
        participant.setEMail("reitmann@his.de");

        return participant;
    }

    private QuestionEntity createQuestion() {
        final QuestionEntity question = new OpenQuestionEntity();
        question.setQuestionText("Wat, wer bist denn du?");
        return question;
    }

    private StringValueTypeEntity createStringValueType() {
        final StringValueTypeEntity myValueType = new StringValueTypeEntity();
        myValueType.setDescription("Renés zweiter Wertetyp.");
        myValueType.setIdentifier("HIS0815");
        myValueType.setCanBeEmpty(false);
        myValueType.setLength(20);
        return myValueType;
    }

    private SurveyVariableEntity createStringVariable(
            final StringValueTypeEntity stringValueType) {
        final SurveyVariableEntity stringVariable = new SurveyVariableEntity();
        stringVariable.setUuid(UUID.randomUUID().toString());
        stringVariable.setName("V1");
        stringVariable.setValueType(stringValueType);
        stringVariable.setQuestion(createQuestion());

        return stringVariable;
    }

    private ParticipantStringValueEntity createStringVariableValue(
            final SurveyVariableEntity stringVariable, final ParticipantEntity participant) {
        final ParticipantStringValueEntity variableValue = new ParticipantStringValueEntity(
                stringVariable, participant, "Quemba ist super :-)");

        return variableValue;
    }

    private SurveyEntity createSurvey() {
        final SurveyEntity survey = new SurveyEntity("NEPS");
        return survey;
    }

    @Test
    @Transactional
    @Rollback(value = true)
    public void saveAnswers() {
        final SurveyVariableEntity stringVariable = createStringVariable(createStringValueType());

        final ParticipantEntity participant = createParticipant(createSurvey());

        stringVariable.addValue(createStringVariableValue(stringVariable,
                participant));

        this.variableDao.save(stringVariable);

        final List<VariableEntity> variables = this.variableDao.findAll();

        Assert.assertEquals(1, ((SurveyVariableEntity) variables.get(0)).getValues()
                .size());

        final SurveyVariableEntity numberVariable = createNumberVariable(createNumberValueType());

        numberVariable.addValue(createNumberVariableValue(numberVariable,
                participant));

        this.variableDao.save(numberVariable);

        final List<VariableEntity> numberVariables = this.variableDao.findAll();

        Assert.assertEquals(1, ((SurveyVariableEntity) numberVariables.get(0))
                .getValues().size());

        // demonstrate pagination, sorting is possible too
        final Pageable pageable = new PageRequest(0, 20);

        final Page<VariableEntity> page = this.variableDao.findAll(pageable);

        Assert.assertEquals(2, page.getNumberOfElements());

        final Page<VariableEntity> anotherPage = this.variableDao.findByName(
                stringVariable.getName(), pageable);

        Assert.assertEquals(1, anotherPage.getNumberOfElements());
    }

    @Test
    @Transactional
    @Rollback(value = true)
    public void saveNumberVariable() {
        final SurveyVariableEntity numberVariable = createNumberVariable(createNumberValueType());

        this.variableDao.save(numberVariable);

        final List<VariableEntity> variables = this.variableDao.findAll();

        Assert.assertEquals(1, variables.size());
    }

    @Test
    @Transactional
    @Rollback(value = true)
    public void saveStringVariable() {
        final SurveyVariableEntity stringVariable = createStringVariable(createStringValueType());

        this.variableDao.save(stringVariable);

        final List<VariableEntity> variables = this.variableDao.findAll();

        Assert.assertEquals(1, variables.size());
    }

}

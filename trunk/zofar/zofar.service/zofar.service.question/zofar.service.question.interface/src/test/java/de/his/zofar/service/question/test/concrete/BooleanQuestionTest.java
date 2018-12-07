/**
 *
 */
package de.his.zofar.service.question.test.concrete;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import de.his.zofar.service.question.model.QuestionVariable;
import de.his.zofar.service.question.model.components.AnswerOption;
import de.his.zofar.service.question.model.concrete.BooleanQuestion;
import de.his.zofar.service.question.model.questionvalues.QuestionNumberValue;
import de.his.zofar.service.question.model.questionvalues.QuestionStringValue;
import de.his.zofar.service.valuetype.model.BooleanValueType;
import de.his.zofar.service.valuetype.model.possiblevalues.PossibleBooleanValue;

/**
 * Test class for the BooleanQuestion class.
 *
 * @author le
 *
 */
public class BooleanQuestionTest {

    /**
     * test if the creation of default answer option from the variable and its
     * value type works.
     */
    @Test
    public void createDefaultAnswerOptionTest() {
        final BooleanQuestion question = new BooleanQuestion(
                createQuestionBooleanVariable());

        Assert.assertNotNull(question.getAnswerOptions());
        Assert.assertEquals(2, question.getAnswerOptions().size());
    }

    /**
     * Boolean question must throw an exception if one tries to add an invalid
     * answer option.
     */
    @Test(expected = IllegalArgumentException.class)
    public void addInvalidAnswerOptionTest() {
        final BooleanQuestion question = new BooleanQuestion(
                createQuestionBooleanVariable());

        // invalid question number value
        final AnswerOption invalidNumberOption = new AnswerOption();
        invalidNumberOption.setDisplayText("something");
        invalidNumberOption.setValue(new QuestionNumberValue(Long.valueOf(1)));

        question.addAnswerOption(invalidNumberOption);

        // invalid question string value
        final AnswerOption invalidStringOption = new AnswerOption();
        invalidStringOption.setDisplayText("something");
        invalidStringOption.setValue(new QuestionStringValue(""));

        question.addAnswerOption(invalidStringOption);
    }

    /**
     * test if setting an invalid answer option list results in a
     * IllegalArgumentException.
     */
    @Test(expected = IllegalArgumentException.class)
    public void setTooManyAnswerOptionsTest() {
        final BooleanQuestion question = new BooleanQuestion(
                createQuestionBooleanVariable());

        question.setAnswerOptions(createTooManyAnswerOptions());
    }

    /**
     * test if setting an invalid answer option list results in a
     * IllegalArgumentException.
     */
    @Ignore
    @Test(expected = IllegalArgumentException.class)
    public void setListWithInvalidAnswerOptionsTest() {
        final BooleanQuestion question = new BooleanQuestion(
                createQuestionBooleanVariable());

        question.setAnswerOptions(createAnswerOptionListWithInvalids());
    }

    /**
     * @return as list of to many answer options
     */
    private List<AnswerOption> createTooManyAnswerOptions() {
        final List<AnswerOption> toMany = new ArrayList<AnswerOption>();
        final int wayTooMany = 3;

        // final QuestionVariable variable = createQuestionBooleanVariable();

        for (int i = 0; i < wayTooMany; i++) {
            final AnswerOption option = new AnswerOption();
            option.setDisplayText("option_" + i);
            // option.setValue(new BooleanValueType(variable,
            // new PossibleBooleanValue()));
            toMany.add(option);
        }

        return toMany;
    }

    /**
     * @return
     */
    private List<AnswerOption> createAnswerOptionListWithInvalids() {
        final List<AnswerOption> invalid = new ArrayList<AnswerOption>();


        return invalid;
    }

    /**
     * creates a variable to be added to a boolean question.
     *
     * @return the question variable
     */
    private QuestionVariable createQuestionBooleanVariable() {
        final QuestionVariable variable = new QuestionVariable();

        variable.setName("V1");
        variable.setUuid(UUID.randomUUID().toString());

        final BooleanValueType valueType = new BooleanValueType();

        // yes
        final PossibleBooleanValue yes = new PossibleBooleanValue();
        yes.setLabels(Arrays.asList(new String[] { "yes" }));
        yes.setValue(true);
        yes.setValueType(valueType);
        yes.setIsMissing(false);
        valueType.addPossibleValue(yes);
        // no
        final PossibleBooleanValue no = new PossibleBooleanValue();
        no.setLabels(Arrays.asList(new String[] { "no" }));
        no.setValue(false);
        no.setValueType(valueType);
        no.setIsMissing(false);
        valueType.addPossibleValue(no);

        // set reference to variable
        variable.setValueType(valueType);

        return variable;
    }
}

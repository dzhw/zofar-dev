/**
 * 
 */
package de.his.zofar.service.question.test.concrete;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.junit.Assert;
import org.junit.Test;

import de.his.zofar.service.question.model.QuestionVariable;
import de.his.zofar.service.question.model.components.AnswerOption;
import de.his.zofar.service.question.model.concrete.SingleChoiceQuestion;
import de.his.zofar.service.question.model.questionvalues.QuestionNumberValue;
import de.his.zofar.service.valuetype.model.NumberValueType;
import de.his.zofar.service.valuetype.model.possiblevalues.PossibleNumberValue;

/**
 * @author le
 * 
 */
public class SingleChoiceQuestionTest {

    @Test
    public void testCreateDefaultAnswerOptions() {
        final SingleChoiceQuestion question = new SingleChoiceQuestion(
                createQuestionVariable());

        Assert.assertNotNull(question.getAnswerOptions());
        Assert.assertEquals(10, question.getAnswerOptions().size());
        Assert.assertEquals("label5", question.getAnswerOptions().get(4)
                .getDisplayText());
        Assert.assertEquals(new Long(5), question.getAnswerOptions().get(4)
                .getValue().getValue());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddInvalidAnswerOptionTest() {
        final SingleChoiceQuestion question = new SingleChoiceQuestion(
                createQuestionVariable());

        final AnswerOption invalidAnswerOption = new AnswerOption();
        invalidAnswerOption.setValue(new QuestionNumberValue(null, new Long(
                11)));

        question.addAnswerOption(invalidAnswerOption);
    }

    private QuestionVariable createQuestionVariable() {
        final QuestionVariable variable = new QuestionVariable();

        variable.setName("V1");
        variable.setUuid(UUID.randomUUID().toString());

        final NumberValueType valueType = new NumberValueType();
        final Map<Long, PossibleNumberValue> possibleValues = new HashMap<Long, PossibleNumberValue>();
        for (int i = 0; i < 10; i++) {
            final PossibleNumberValue possibleValue = new PossibleNumberValue();
            possibleValue.setValue(new Long(i + 1));
            possibleValue.setLabels(Arrays.asList(new String[] { "label"
                    + (i + 1) }));
            possibleValue.setValueType(valueType);
            possibleValues.put(possibleValue.getValue(), possibleValue);
        }
        valueType.setPossibleValues(possibleValues);

        // set reference to variable
        variable.setValueType(valueType);

        return variable;
    }
}

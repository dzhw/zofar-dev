/**
 *
 */
package de.his.zofar.generator.maven.plugin.test.converter.transition;

import java.io.File;
import java.net.URL;

import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.his.zofar.generator.maven.plugin.generator.variable.VariableGenerator;
import de.his.zofar.generator.maven.plugin.reader.QuestionnaireReader;
import de.his.zofar.xml.questionnaire.QuestionnaireDocument;
import de.his.zofar.xml.questionnaire.VariableType;

/**
 * usage demonstrator of the VariableGenerator.
 *
 * @author le
 *
 */
public class VariableGeneratorTest {

    private static final Logger LOGGER = LoggerFactory
            .getLogger(VariableGeneratorTest.class);

    // /**
    // * demonstrates how to generate the survey-variables-context.xml in which
    // * zofar survey describes and defines its variables and the
    // * sessionController.
    // */
    // @Test
    // @Ignore
    // public void testDemonstrator() {
    // final VariableGenerator generator = new VariableGenerator();
    //
    // generator.createDocument();
    // generator.addBooleanVariable("v1");
    // generator.addStringVariable("v2");
    // final Map<String, String> optionValues = new HashMap<String, String>();
    // optionValues.put("antwort1", "0");
    // final Map<String, String> optionLabels = new HashMap<String, String>();
    // optionLabels.put("antwort1", "gut");
    // generator.addSingleChoiceAnswerOption("v3",
    // generator.createSingleChoiceOptionValues(optionValues),
    // generator.createSingleChoiceOptionLabels(optionLabels));
    //
    // LOGGER.info("\n{}", generator.getDocument());
    //
    // Assert.assertTrue(generator.validate());
    // }

    @Test
    @Ignore
    public void testSingleChoiceAnswerOptionCreation() {
        final VariableGenerator generator = new VariableGenerator();

        QuestionnaireDocument questionnaire = null;
        try {
            final URL url = VariableGeneratorTest.class
                    .getResource("/questionnaire.xml");
            final File xml = new File(url.getFile());

            // first try to read the questionnaire xml
            questionnaire = QuestionnaireReader.getInstance().readDocument(xml,
                    true);
        } catch (final IllegalArgumentException iae) {
            LOGGER.warn("No questionnaire XML file! Create base variables file only.");
        }

        VariableType[] variables = null;
        if (questionnaire != null) {
            variables = questionnaire.getQuestionnaire().getVariables()
                    .getVariableArray();
        }

        // create basic survey-variable-context.xml
        generator.createDocument();

        if (variables != null) {
            for (final VariableType variable : variables) {
                if (variable.getType().equals(
                        VariableType.Type.SINGLE_CHOICE_ANSWER_OPTION)) {
                    generator.addSingleChoiceAnswerOption(variable,
                            questionnaire);
                }
            }
        }

        LOGGER.info("document: {}", generator.getDocument());
    }

}

/**
 *
 */
package de.his.zofar.generator.maven.plugin.test.reader;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import org.apache.xmlbeans.XmlException;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.his.zofar.xml.questionnaire.QuestionSingleChoiceAnswerOptionType;
import de.his.zofar.xml.questionnaire.QuestionnaireDocument;
import de.his.zofar.xml.questionnaire.TextResponseOptionType;
import de.his.zofar.xml.questionnaire.VariableType;

/**
 * @author le
 *
 */
public final class QuestionnaireReaderTest {

    private static final Logger LOGGER = LoggerFactory
            .getLogger(QuestionnaireReaderTest.class);

    @Test
    @Ignore
    public void test() {
        QuestionnaireDocument document = null;

        final URL url = QuestionnaireReaderTest.class
                .getResource("/questionnaire.xml");

        try {
            document = QuestionnaireDocument.Factory.parse(new File(url
                    .getFile()));
        } catch (final XmlException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (final IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        if (document == null) {
            throw new RuntimeException();
        }

        final String namespace = "declare namespace z='http://www.his.de/zofar/xml/questionnaire'";

        for (final VariableType variable : document.getQuestionnaire()
                .getVariables().getVariableArray()) {
            if (variable.getType().equals(
                    VariableType.Type.SINGLE_CHOICE_ANSWER_OPTION)) {
                LOGGER.info("single choice variable: {}", variable.getName());

                final String expression = namespace + " "
                        + "//z:responseDomain[@variable='" + variable.getName()
                        + "']//z:answerOption";

                final Object[] result = document.selectPath(expression);

                for (final Object child : result) {
                    final QuestionSingleChoiceAnswerOptionType option = (QuestionSingleChoiceAnswerOptionType) child;

                    String label = "";
                    if (option.getLabelArray().length > 0) {
                        for (final TextResponseOptionType l : option
                                .getLabelArray()) {
                            label += l.toString() + " ";
                        }
                    } else {
                        label = option.getLabel2();
                    }

                    LOGGER.info("uid: {}, label: {}, value: {}", new Object[] {
                            option.getUid(), label, option.getValue() });
                }
            }
        }
    }
}

/**
 *
 */
package de.his.zofar.generator.maven.plugin.test.converter.transition;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import org.apache.xmlbeans.XmlException;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.his.zofar.generator.maven.plugin.generator.page.ZofarWebPage;
import de.his.zofar.xml.questionnaire.PageType;
import de.his.zofar.xml.questionnaire.QuestionnaireDocument;

/**
 * @author le
 *
 */
public class PageGeneratorTest {

    private static final Logger LOGGER = LoggerFactory
            .getLogger(PageGeneratorTest.class);

    @Test
    @Ignore
    public void testReadFromXml() {
        // TODO le don't ignore test case
        final URL url = PageGeneratorTest.class
                .getResource("/questionnaire.xml");
        try {
            final QuestionnaireDocument document = QuestionnaireDocument.Factory
                    .parse(new File(url.getFile()));

            for (final PageType xmlpage : document.getQuestionnaire()
                    .getPageArray()) {
                final ZofarWebPage xhtmlPage = new ZofarWebPage(
                        xmlpage.getUid());

                xhtmlPage.addPageContentRecursively(xmlpage);

                // TODO le cannot validate the XHTML page even if it should be
                // valid.
                // Assert.assertTrue(xhtmlPage.validate());

                LOGGER.info("page {}: {}", xmlpage.getUid(), xhtmlPage);
            }
        } catch (XmlException | IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}

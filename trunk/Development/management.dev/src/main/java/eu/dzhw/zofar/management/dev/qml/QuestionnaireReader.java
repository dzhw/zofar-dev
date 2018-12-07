/**
 *
 */
package eu.dzhw.zofar.management.dev.qml;

import java.io.File;
import java.io.IOException;

import org.apache.xmlbeans.XmlException;

import de.his.zofar.xml.questionnaire.QuestionnaireDocument;

/**
 * singleton class that reads the questionnaire.xml.
 *
 * @author le
 *
 */
public class QuestionnaireReader {

    private static final QuestionnaireReader INSTANCE = new QuestionnaireReader();

    private QuestionnaireDocument questionnaire;

    /**
     *
     */
    private QuestionnaireReader() {
        super();
    }

    public static QuestionnaireReader getInstance() {
        return INSTANCE;
    }

    /**
     * reads the document (fixed file name 'questionnaire.xml') form and returns
     * it. if readNew is true the document will be newly read other return the
     * previously read document.
     *
     * @param xml
     * @param readNew
     * @return
     */
    public QuestionnaireDocument readDocument(final File xml,
            final Boolean readNew) {
        if (readNew || questionnaire == null) {
            try {
                if (!xml.isFile()) {
                    throw new IllegalArgumentException("cannot find file: "
                            + xml.getAbsolutePath());
                }

                questionnaire = QuestionnaireDocument.Factory.parse(xml);
            } catch (XmlException e) {
                e.printStackTrace();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
        return questionnaire;
    }

    /**
     * return the cached document.
     *
     * @param xml
     * @return
     */
    public QuestionnaireDocument readDocument(final File xml) {
        return readDocument(xml, false);
    }
}

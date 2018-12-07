/**
 *
 */
package de.his.zofar.presentation.questionnaire.components;

import java.io.Serializable;

import de.his.zofar.presentation.question.components.BaseQuestionComponent;
import de.his.zofar.service.question.model.structure.Text;

/**
 * this class represents the JSF view of a questionnaire page.
 *
 * @author le
 *
 */
public class QuestionnairePageComponent extends BaseQuestionComponent implements
Serializable {

    /**
     *
     */
    private static final long serialVersionUID = -620842057096719788L;

    /**
     *
     */
    private static final String QUESTIONNAIRE_PAGE_CLASS = "questionnaire-page";

    /**
     *
     */
    public QuestionnairePageComponent() {
        super();
        // creates a surrounding <div> block
        setLayout("block");
        setStyleClass(QUESTIONNAIRE_PAGE_CLASS);
    }

    /**
     * @param text
     */
    void addText(final Text text) {
        addOutputText(text, "page-text");
    }

}

/**
 *
 */
package de.his.zofar.presentation.questionnaire.components;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;

import de.his.zofar.presentation.question.components.QuestionComponentFactory;
import de.his.zofar.service.question.model.Question;
import de.his.zofar.service.question.model.structure.StructuredElement;
import de.his.zofar.service.question.model.structure.Text;
import de.his.zofar.service.questionnaire.model.QuestionnairePage;

/**
 * Factory for QuestionnairePage.
 *
 * @author le
 *
 */
@ManagedBean
@ApplicationScoped
public class QuestionnaireComponentFactory {

    /**
     *
     */
    @ManagedProperty(value = "#{questionComponentFactory}")
    private QuestionComponentFactory questionComponentFactory;

    /**
     * @param questionComponentFactory
     *            the questionComponentFactory to set
     */
    public void setQuestionComponentFactory(
            final QuestionComponentFactory questionComponentFactory) {
        this.questionComponentFactory = questionComponentFactory;
    }

    /**
     * creates the UIComponent for a questionnaire page with all its attributes
     * and questions.
     *
     * @param page
     *            the page to create the UIComponent from
     * @return the UIComponent
     */
    public QuestionnairePageComponent createQuestionnaireComponent(
            final QuestionnairePage page) {
        final QuestionnairePageComponent component =
                new QuestionnairePageComponent();

        // set the text of the page
        for (final StructuredElement element : page.getHeader()) {
            if (Text.class.isAssignableFrom(element.getClass())) {
                component.addText((Text) element);
            } else {
                throw new RuntimeException("Not yet implemented");
            }
        }

        // adding all questions of the page to the UIComponent
        for (final Question question : page.getQuestions()) {
            component.getChildren().add(
                    questionComponentFactory.createQuestionComponent(question));
        }

        return component;
    }
}

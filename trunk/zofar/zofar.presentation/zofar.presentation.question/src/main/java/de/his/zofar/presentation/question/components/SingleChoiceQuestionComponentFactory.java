/**
 * 
 */
package de.his.zofar.presentation.question.components;

import de.his.zofar.service.question.model.Question;
import de.his.zofar.service.question.model.concrete.SingleChoiceQuestion;

/**
 * A factory that creates an UIComponent for a single choice question with all
 * its properties.
 * 
 * Note: This class must not be used other than in the QuestionComponentFactory.
 * 
 * @author le
 * 
 */
class SingleChoiceQuestionComponentFactory extends BaseQuestionComponentFactory {

    /**
     * 
     */
    private static final long serialVersionUID = -1287132348976981143L;

    private static SingleChoiceQuestionComponentFactory INSTANCE = null;

    static SingleChoiceQuestionComponentFactory getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new SingleChoiceQuestionComponentFactory();
        }
        return INSTANCE;
    }

    private SingleChoiceQuestionComponentFactory() {
        super();
    }

    @Override
    BaseQuestionComponent createQuestionComponent(final Question question) {
        final SingleChoiceQuestionComponent component =
                new SingleChoiceQuestionComponent();
        component.setLayout("block");

        initBaseQuestionComponent(component, question);

        // adds the answer options to the component
        component.addAnswerOptions((SingleChoiceQuestion) question);
        return component;
    }
}

/**
 * 
 */
package de.his.zofar.presentation.question.components;

import de.his.zofar.service.question.model.Question;
import de.his.zofar.service.question.model.concrete.MultipleChoiceQuestion;

/**
 * @author le
 * 
 */
class MultipleChoiceQuestionComponentFactory extends
        BaseQuestionComponentFactory {

    /**
     * 
     */
    private static final long serialVersionUID = -2520122436888203959L;
    private static MultipleChoiceQuestionComponentFactory INSTANCE = null;

    static MultipleChoiceQuestionComponentFactory getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new MultipleChoiceQuestionComponentFactory();
        }
        return INSTANCE;
    }

    private MultipleChoiceQuestionComponentFactory() {
        super();
    }

    @Override
    BaseQuestionComponent createQuestionComponent(final Question question) {
        final MultipleChoiceQuestionComponent component = new MultipleChoiceQuestionComponent();
        initBaseQuestionComponent(component, question);

        component
                .addSingleChoiceQuestions(((MultipleChoiceQuestion) question)
                        .getQuestions());

        return component;
    }
}

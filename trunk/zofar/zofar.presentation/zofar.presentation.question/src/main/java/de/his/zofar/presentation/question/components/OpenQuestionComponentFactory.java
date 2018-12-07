/**
 * 
 */
package de.his.zofar.presentation.question.components;

import de.his.zofar.service.question.model.Question;
import de.his.zofar.service.question.model.concrete.OpenQuestion;

/**
 * A Factory that creates a UIComponent for an open question with all its
 * properties.
 * 
 * Note: This class must not be used other than in the QuestionComponentFactory.
 * 
 * @author le
 * 
 */
class OpenQuestionComponentFactory extends BaseQuestionComponentFactory {

    /**
     * 
     */
    private static final long serialVersionUID = -807296991282014465L;

    private static OpenQuestionComponentFactory INSTANCE = null;

    static OpenQuestionComponentFactory getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new OpenQuestionComponentFactory();
        }
        return INSTANCE;
    }

    private OpenQuestionComponentFactory() {
        super();
    }

    /**
     * Factory method to create the UIComponent
     * 
     * @param question
     * @return
     */
    @Override
    BaseQuestionComponent createQuestionComponent(final Question question) {
        final OpenQuestionComponent component = new OpenQuestionComponent();
        initBaseQuestionComponent(component, question);
        // adds an input text where the participant can enters the value for the
        // question variable
        component.addInputText(((OpenQuestion) question).getVariable());
        return component;
    }
}

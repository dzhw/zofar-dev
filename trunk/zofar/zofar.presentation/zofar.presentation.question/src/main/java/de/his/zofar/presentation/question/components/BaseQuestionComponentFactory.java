/**
 * 
 */
package de.his.zofar.presentation.question.components;

import java.io.Serializable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.his.zofar.service.question.model.Question;
import de.his.zofar.service.question.model.structure.InstructionText;
import de.his.zofar.service.question.model.structure.IntroductionText;
import de.his.zofar.service.question.model.structure.QuestionText;
import de.his.zofar.service.question.model.structure.StructuredElement;

/**
 * @author Reitmann
 * 
 */
abstract class BaseQuestionComponentFactory implements Serializable {
    /**
	 * 
	 */
    private static final long serialVersionUID = -4596593972040021544L;
    protected final Logger LOGGER = LoggerFactory.getLogger(getClass());

    /**
     * Factory method to create the UIComponent
     * 
     * @param question
     * @return
     */
    abstract BaseQuestionComponent createQuestionComponent(Question question);

    /**
     * @param baseQuestionComponent
     * @param question
     * @return
     */
    protected BaseQuestionComponent initBaseQuestionComponent(
            final BaseQuestionComponent baseQuestionComponent,
            final Question question) {
        for (final StructuredElement element : question.getHeader()) {
            final Class<?> clazz = element.getClass();
            if (IntroductionText.class.isAssignableFrom(clazz)) {
                baseQuestionComponent
                        .addIntroduction((IntroductionText) element);
            } else if (QuestionText.class.isAssignableFrom(clazz)) {
                baseQuestionComponent
                        .addQuestionText((QuestionText) element);
            } else if (InstructionText.class.isAssignableFrom(clazz)) {
                baseQuestionComponent
                        .addInstruction((InstructionText) element);
            } else {
                throw new RuntimeException("Not yet implemented");
            }
        }

        // sets the id of the question container aka <div> block to the question
        // name
        // baseQuestionComponent.setId(question.getName());

        // set visibility attribute to the whole component
        if (!question.getVisibilityCondition().isEmpty()) {
            baseQuestionComponent.addVisibility(question
                    .getVisibilityCondition());
        }

        return baseQuestionComponent;
    }
}

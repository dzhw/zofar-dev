/**
 *
 */
package de.his.zofar.presentation.question.components;

import javax.el.ValueExpression;
import javax.faces.component.html.HtmlOutputText;
import javax.faces.component.html.HtmlPanelGroup;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.his.zofar.service.evaluation.service.EvaluationService;
import de.his.zofar.service.question.model.structure.InstructionText;
import de.his.zofar.service.question.model.structure.IntroductionText;
import de.his.zofar.service.question.model.structure.QuestionText;
import de.his.zofar.service.question.model.structure.StructuredElement;

/**
 * This class sets all basic elements of a question.
 *
 * @author le
 *
 */
public abstract class BaseQuestionComponent extends HtmlPanelGroup{

    @SuppressWarnings("unused")
    private static final Logger LOGGER = LoggerFactory
			.getLogger(BaseQuestionComponent.class);

	// TODO make configurational
	protected final static String BEAN_NAME = "surveyEngineModelBean";

	protected final static String INTRODUCTION_TEXT_CLASS = "question-introduction";
	protected final static String QUESTION_TEXT_CLASS = "question-text";
	protected final static String INSTRUCTION_TEXT_CLASS = "question-instruction";
	protected final static String ANSWER_OPTION_CONTAINER_CLASS = "answer-option-container";
	protected final static String ANSWER_OPTION_ITEM_CLASS = "answer-option-item";

	/**
     *
     */
	protected BaseQuestionComponent() {
		super();
		// creating a div block around the whole component.
		setLayout("block");
	}

	/**
	 * @param text
	 */
	void addInstruction(final InstructionText text) {
		this.addOutputText(text, INSTRUCTION_TEXT_CLASS);
	}

	/**
	 * @param text
	 */
	void addIntroduction(final IntroductionText text) {
		this.addOutputText(text, INTRODUCTION_TEXT_CLASS);
	}

	/**
	 * Adds text to the question component.
	 *
	 * @param text
	 * @param htmlClass
	 */
	protected void addOutputText(final StructuredElement text,
			final String htmlClass) {
		// this is the <div> block
		final HtmlPanelGroup textGroup = new HtmlPanelGroup();
		textGroup.setLayout("block");
		textGroup.setStyleClass(htmlClass);
		// this is the actual string surrounded by <span>
		final HtmlOutputText outputText = new HtmlOutputText();
		outputText.setValueExpression("value",
				convertText((String) text.getContent()));

		outputText.setStyleClass(htmlClass);

		textGroup.getChildren().add(outputText);

		// set rendered attribute
		if (!text.getVisibilityCondition().isEmpty()) {
			textGroup.setValueExpression("rendered",
					createConditionExpression(text.getVisibilityCondition()));
		}

		getChildren().add(textGroup);
	}

	/**
	 * @param text
	 */
	void addQuestionText(final QuestionText text) {
		this.addOutputText(text, QUESTION_TEXT_CLASS);
	}

	/**
	 * @param condition
	 */
	void addVisibility(final String condition) {
		if (!condition.isEmpty()) {
			this.setValueExpression("rendered",
					createConditionExpression(condition));
		}
	}

//	/**
//	 * @param condition
//	 */
//	public void addSorting(final UUID uuid) {
//		if (!(uuid == null)) {
//			this.setValueExpression("sorting", createSortingExpression(uuid));
//		}
//	}

	/**
	 * Creates a value expression that binds the input to a map value of the
	 * managed bean to save the values.
	 *
	 * @param variableName
	 * @param typeClass
	 * @return
	 */
	protected ValueExpression createValueExpressionForInput(
			final String variableName, final Class<?> typeClass) {
		final String elExpression = "#{" + BEAN_NAME + ".valueMap['"
				+ variableName + "']}";

		// final String elExpression = "#{" + BEAN_NAME + ".getAnswer('"
		// + variableName + "')}";

		final ValueExpression ve = getFacesContext()
				.getApplication()
				.getExpressionFactory()
				.createValueExpression(getFacesContext().getELContext(),
						elExpression, typeClass);
		return ve;
	}

//	/**
//	 * @param condition
//	 * @return
//	 */
//	protected ValueExpression createSortingExpression(final UUID uuid) {
//		// LOGGER.debug("setting Sorting for {}", uuid);
//		// final String elExpression = "#{" + BEAN_NAME + ".sortingMap['" + uuid
//		// + "']}";
//		final String elExpression = "#{" + BEAN_NAME + ".getSorting('" + uuid
//				+ "')}";
//
//		final ValueExpression ve = getFacesContext()
//				.getApplication()
//				.getExpressionFactory()
//				.createValueExpression(getFacesContext().getELContext(),
//						elExpression, List.class);
//		return ve;
//	}

	/**
	 * @param condition
	 * @return
	 */
	protected ValueExpression createConditionExpression(final String condition) {
		// LOGGER.debug("setting visibility condition: {}", condition);
		// final String expression = "#{evaluatorBean.visible['" + condition
		// + "']}";
		final String expression = "#{evaluatorBean.isVisible('" + condition
				+ "')}";
		final ValueExpression ve = getFacesContext()
				.getApplication()
				.getExpressionFactory()
				.createValueExpression(getFacesContext().getELContext(),
						expression, Boolean.class);
		return ve;
	}

	protected ValueExpression convertText(final String text) {
		// final String expression = "#{evaluatorBean.text['"
		// + EvaluationService.quote(text) + "']}";
		final String expression = "#{evaluatorBean.convertText('"
				+ EvaluationService.quote(text) + "')}";
		final ValueExpression ve = getFacesContext()
				.getApplication()
				.getExpressionFactory()
				.createValueExpression(getFacesContext().getELContext(),
						expression, String.class);
		return ve;
	}
}

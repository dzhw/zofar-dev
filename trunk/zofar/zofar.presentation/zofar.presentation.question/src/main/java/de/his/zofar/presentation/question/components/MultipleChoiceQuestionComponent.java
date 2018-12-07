/**
 *
 */
package de.his.zofar.presentation.question.components;

import java.util.List;

import javax.el.ValueExpression;
import javax.faces.component.html.HtmlOutputLabel;
import javax.faces.component.html.HtmlPanelGroup;
import javax.faces.component.html.HtmlSelectBooleanCheckbox;

import de.his.zofar.service.question.model.concrete.BooleanQuestion;
import de.his.zofar.service.question.model.structure.StructuredElement;

/**
 * this class represents the jsf view of a multiple choice question
 *
 * @author le
 *
 */
// TODO fix required public for reflection
public class MultipleChoiceQuestionComponent extends BaseQuestionComponent {
    // private final HtmlSelectManyCheckbox answerOptionContainer =
    // new HtmlSelectManyCheckbox();
    private final HtmlPanelGroup answerOptionContainer = new HtmlPanelGroup();

    // private final HtmlPanelGroup dataList = new HtmlPanelGroup();

    /**
     * @param question
     */
    public MultipleChoiceQuestionComponent() {
        super();
        setStyleClass("multiple-choice-question");
    }

    // /**
    // * adds one single choice question to the answer option container
    // *
    // * @param question
    // */
    // private void addSingleChoiceQuestion(final BooleanQuestionDTO question) {
    // final UISelectItem checkbox = new UISelectItem();
    //
    // // set the label
    // String label = new String();
    // for (final StructuredElementDTO element : question.getHeader()) {
    // label += element.getContent();
    // }
    // checkbox.setItemLabel(label);
    //
    // // if (question.getAnswerOptions().size() == 1) {
    // // checkbox.setItemValue(question.getAnswerOptions().get(0).getValue()
    // // .getValue());
    // // }
    // checkbox.setItemValue(question.getTrueAnswerOption().getValue()
    // .getValue());
    //
    // checkbox.setValueExpression(
    // "value",
    // createValueExpressionForInput(question.getVariable().getName(),
    // Boolean.class));
    //
    // answerOptionContainer.getChildren().add(checkbox);
    // }

    /**
     * adds all single choice question to the answer option container
     *
     * @param questions
     */
    void addSingleChoiceQuestions(final List<BooleanQuestion> questions) {
        // // using "spread" to customize output of the container
        // answerOptionContainer.setLayout("spread");
        // answerOptionContainer.setId("foo");
        answerOptionContainer.setLayout("block");
        answerOptionContainer.setStyleClass(ANSWER_OPTION_CONTAINER_CLASS);

        // dataList.setLayout("block");
        // dataList.setStyleClass(ANSWER_OPTION_CONTAINER_CLASS);
        int index = 0;
        for (final BooleanQuestion question : questions) {
            // addSingleChoiceQuestion(question);

            // for the tomahawk implementation one needs to add the checkboxes
            // separately to the view in order to customize the output. Here to
            // group one checkbox item in a <div> block.
            final HtmlPanelGroup answerOption = new HtmlPanelGroup();
            answerOption.setLayout("block");
            answerOption.setStyleClass(ANSWER_OPTION_ITEM_CLASS + index);

            // final HtmlCheckbox checkbox = new HtmlCheckbox();
            // checkbox.setFor(answerOptionContainer.getId());
            // checkbox.setIndex(index);
            final HtmlSelectBooleanCheckbox checkbox = new HtmlSelectBooleanCheckbox();
            checkbox.setId(question.getVariable().getName());
            // checkbox.setValueExpression(
            // "value",
            // createValueExpressionForInput(question.getVariable()
            // .getName(), Boolean.class));
            checkbox.setValueExpression(
                    "value",
                    createValueExpressionForInput(question.getVariable()
                            .getName(), Boolean.class));

            final HtmlOutputLabel outputLabel = new HtmlOutputLabel();
            outputLabel.setFor(checkbox.getClientId());
            String label = "";
            for (final StructuredElement element : question.getHeader()) {
                label += element.getContent();
            }
            outputLabel.setValue(label);

            answerOption.getChildren().add(checkbox);
            answerOption.getChildren().add(outputLabel);

            // set visibility
            if (!question.getVisibilityCondition().isEmpty()) {
                answerOption.setValueExpression("rendered",
                        createConditionExpression(question
                                .getVisibilityCondition()));
            }

            answerOptionContainer.getChildren().add(answerOption);
            // dataList.getChildren().add(answerOption);
            index++;
        }
        getChildren().add(answerOptionContainer);
        // getChildren().add(dataList);
    }
    
	/**
	 * Creates a value expression that binds the input to a map value of the
	 * managed bean to save the values.
	 * 
	 * @param variableName
	 * @param typeClass
	 * @return
	 */
	@Override
	protected ValueExpression createValueExpressionForInput(
			final String variableName, final Class<?> typeClass) {
		final String elExpression = "#{" + BEAN_NAME + ".valueMap['"
				+ variableName + "'].answerValue}";

		final ValueExpression ve = getFacesContext()
				.getApplication()
				.getExpressionFactory()
				.createValueExpression(getFacesContext().getELContext(),
						elExpression, typeClass);
		return ve;
	}
}

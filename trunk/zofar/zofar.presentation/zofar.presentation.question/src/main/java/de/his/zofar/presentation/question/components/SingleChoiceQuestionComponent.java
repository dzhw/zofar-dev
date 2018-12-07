/**
 *
 */
package de.his.zofar.presentation.question.components;

import javax.el.ValueExpression;
import javax.faces.component.UIComponent;
import javax.faces.component.UISelectItem;
import javax.faces.component.html.HtmlPanelGroup;
import javax.faces.component.html.HtmlSelectOneMenu;
import javax.faces.convert.Converter;

import org.apache.myfaces.component.html.ext.HtmlSelectOneRadio;
import org.apache.myfaces.custom.radio.HtmlRadio;

import de.his.zofar.service.question.model.components.AnswerOption;
import de.his.zofar.service.question.model.concrete.SingleChoiceQuestion;
import de.his.zofar.service.question.model.interfaces.Answer;

/**
 * This class represents the JSF view of a single choice question.
 * 
 * @author le
 * 
 */
// TODO fix required public for reflection
public class SingleChoiceQuestionComponent extends BaseQuestionComponent {
	// private static final Logger LOGGER = LoggerFactory
	// .getLogger(SingleChoiceQuestionComponent.class);
	private boolean dropDown = false;

	/**
     *
     */
	private final String LAYOUT = "spread";

	/**
	 * @param question
	 */
	public SingleChoiceQuestionComponent() {
		super();
		setStyleClass("single-choice-question");
	}

	/**
	 * Adds all answer options from the single choice question.
	 * 
	 * @param question
	 */
	void addAnswerOptions(final SingleChoiceQuestion question) {
		// this also contains the answer option in case the sing choice question
		// is not rendered as a drop down list
		HtmlPanelGroup dataList = null;

		// this contains the answer options
		UIComponent answerOptionContainer = null;

		if (this.dropDown) {
			answerOptionContainer = new HtmlSelectOneMenu();
		} else {
			answerOptionContainer = new HtmlSelectOneRadio();
			((HtmlSelectOneRadio) answerOptionContainer).setLayout(LAYOUT);
		}

		// set a custom id, because JSF produces duplicate id for this component
		answerOptionContainer.setId(question.getVariable().getName());

		// sets the value expression to the variable map of the managed bean on
		// the container
		answerOptionContainer.setValueExpression(
				"value",
				createValueExpressionForInput(question.getVariable().getName(),
						Answer.class));

		final String elExpression = "#{answerConverterBean}";

		final ValueExpression ve = getFacesContext()
				.getApplication()
				.getExpressionFactory()
				.createValueExpression(getFacesContext().getELContext(),
						elExpression, Converter.class);

		answerOptionContainer.setValueExpression("converter", ve);

		// adds the answer options to the component
		if (question.getAnswerOptions() != null) {
			int rowIndex = 0;

			if (!dropDown) {
				// creating the container that contains the radio boxes
				dataList = new HtmlPanelGroup();
				dataList.setLayout("block");
				dataList.setStyleClass(ANSWER_OPTION_CONTAINER_CLASS);
				// dataList.setColumns(1);
				// dataList.setRowClasses("odd, even");
			}

			for (final AnswerOption option : question.getAnswerOptions()) {
				final UISelectItem selectItem = new UISelectItem();
				selectItem.setItemValue(option);
				selectItem.setItemLabel(option.getDisplayText());
				answerOptionContainer.getChildren().add(selectItem);

				if (!dropDown) {
					// for the tomahawk implementation of a single choice
					// question the radio boxes must be added to the JSF
					// component separately
					final HtmlPanelGroup listItemPanel = new HtmlPanelGroup();
					listItemPanel.setLayout("block");
					listItemPanel.setStyleClass(ANSWER_OPTION_ITEM_CLASS
							+ rowIndex);
					final HtmlRadio radio = new HtmlRadio();
					radio.setFor(answerOptionContainer.getClientId());
					radio.setIndex(rowIndex);

					listItemPanel.getChildren().add(radio);

					// adding optional open question of the answer option
					if (option.getOpenQuestion() != null && !dropDown) {
						final BaseQuestionComponent oo = OpenQuestionComponentFactory
								.getInstance().createQuestionComponent(
										option.getOpenQuestion());
						// creating <span> instead of <div>
						oo.setLayout("");
						listItemPanel.getChildren().add(oo);
					}

					// set visibility attribute
					if (!option.getVisibilityCondition().isEmpty()) {
						listItemPanel.setValueExpression("rendered",
								createConditionExpression(option
										.getVisibilityCondition()));
					}

					dataList.getChildren().add(listItemPanel);
					rowIndex++;
				}
			}
		}
		getChildren().add(answerOptionContainer);

		// TODO: in case of a drop down single choice question every optional
		// open question must be appended at the end of the question

		if (!dropDown) {
			getChildren().add(dataList);
		}
	}

	/**
	 * If set to true then the component is rendered as a menu list otherwise as
	 * radio buttons. Default is false.
	 * 
	 * @param dropDown
	 *            the dropDown to set
	 */
	void setDropDown(final boolean dropDown) {
		this.dropDown = dropDown;
	}
}

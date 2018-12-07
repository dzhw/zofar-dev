/**
 *
 */
package de.his.zofar.presentation.question.components;

import javax.el.ValueExpression;
import javax.faces.component.html.HtmlInputText;

import de.his.zofar.service.valuetype.model.NumberValueType;
import de.his.zofar.service.valuetype.model.StringValueType;
import de.his.zofar.service.valuetype.model.ValueType;
import de.his.zofar.service.valuetype.model.Variable;

/**
 * This class represents the JSF view of an open question.
 *
 * @author le
 *
 */
// TODO fix required public for reflection
public class OpenQuestionComponent extends BaseQuestionComponent {

	/**
	 * @param question
	 */
	public OpenQuestionComponent() {
		super();
		setStyleClass("open-question");
	}

	/**
	 * Adds the actual HTML input to the question component.
	 *
	 * @param variable
	 */
	void addInputText(final Variable variable) {
		if (variable == null) {
			throw new IllegalArgumentException("variable cannot be null");
		}
		if (variable.getValueType() == null) {
			throw new IllegalArgumentException(
					"value type of a variable cannot be null");
		}
		final HtmlInputText inputText = new HtmlInputText();
		final ValueType valueType = variable.getValueType();

		// creating the input text depending on the value type
		if (StringValueType.class.isAssignableFrom(valueType.getClass())) {
			final StringValueType stringValue = (StringValueType) valueType;
			inputText.setMaxlength(stringValue.getLength());
			inputText.setRequired(!stringValue.getCanBeEmpty());
			inputText.setValueExpression(
					"value",
					createValueExpressionForInput(variable.getName(),
							String.class));
		} else if (NumberValueType.class.isAssignableFrom(valueType.getClass())) {
			inputText.setValueExpression(
					"value",
					createValueExpressionForInput(variable.getName(),
							Long.class));
		} else {
			throw new RuntimeException("Not yet implemented. "
					+ valueType.getClass());
		}

		// setting the value binding
		// inputText
		// .setValueExpression(
		// "value",
		// createValueExpressionForInput(variable.getName(),
		// Answer.class));

		getChildren().add(inputText);
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

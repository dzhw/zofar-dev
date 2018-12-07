package de.his.zofar.service.evaluation.service;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.EvaluationException;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Service;

import de.his.zofar.service.evaluation.service.interfaces.EvaluatorContextHolder;

/**
 * @author Meisner
 * 
 */
@Service
public class EvaluationService {

	private class ContextHolder {

		private Map<String, Object> valueMap;

		/**
		 * @param valueMap
		 */
		public ContextHolder(final Map<String, Object> valueMap) {
			super();
			this.valueMap = valueMap;
		}

		@SuppressWarnings("unused")
		public Map<String, Object> getValueMap() {
			return this.valueMap;
		}

		/**
		 * @param key
		 * @param value
		 */
		@SuppressWarnings("unused")
		public void setValue(final String key, final Object value) {
			this.valueMap.put(key, value);
		}

		public String getMapName() {
			return "valueMap";
		}

		@Override
		public String toString() {
			return "ContextHolder [valueMap=" + valueMap + "]";
		}
	};

	private static final Logger LOGGER = LoggerFactory
			.getLogger(EvaluationService.class);

	private static final Pattern VARPATTERN = Pattern.compile("__([^ ]*)");
	private static final Pattern QUOTEPATTERN = Pattern
			.compile(" ?'[^ ]((?:(?!' ).)+)[^ ]' ?");

	/**
	 * Evaluate a given Condition in the context of the values given in a map in
	 * contextHolder
	 * 
	 * Condition : __a <= 5 OR __b == 'keks'
	 * 
	 * @param condition
	 * @param contextHolder
	 *            contains the Map of Values (values have to implement the
	 *            method getValue())
	 * @param mapName
	 *            name of ValueMap
	 * @return boolean
	 */
	public boolean evaluate(final String condition,
			final EvaluatorContextHolder contextHolder) {
//		LOGGER.info("evaluate Condition : {}", condition);
		String newCondition = condition;

		if (newCondition == null)
			newCondition = "";
		if (newCondition != null)
			newCondition = newCondition.trim();

		if (newCondition.equals(""))
			return true;

		final ContextHolder myContextHolder = new ContextHolder(buildMap(condition,
				contextHolder));
		newCondition = convertToValue(newCondition, myContextHolder);

		final EvaluationContext context = new StandardEvaluationContext(
				myContextHolder);
		final ExpressionParser parser = new SpelExpressionParser();
		final Expression exp = parser.parseExpression(newCondition);
		try{
			return exp.getValue(context, Boolean.class);
		}
		catch(EvaluationException e){
			LOGGER.error("Cannot evaluate {}. Maybe the ContextHolder Map contains values who not implements de.his.hiob.model.question.models.interfaces.Answer",condition);
		}
		return false;
	}

	/**
	 * Evaluate a given Text in the context of the values given in a map in
	 * contextHolder
	 * 
	 * Condition : The Variable a have the Value $a
	 * 
	 * @param text
	 * @param contextHolder
	 *            contains the Map of Values (values have to implement the
	 *            method getValue())
	 * @param mapName
	 *            name of ValueMap
	 * @return Text with replaced Placeholder
	 */
	public String evaluateText(final String text,
			final EvaluatorContextHolder contextHolder) {

		String newText = text;

		if (newText == null)
			newText = "";
		if (newText != null)
			newText = newText.trim();

		if (newText.equals(""))
			return "";

		final ContextHolder myContextHolder = new ContextHolder(buildMap(newText,
				contextHolder));
		newText = convertToText(newText,myContextHolder);

		final EvaluationContext context = new StandardEvaluationContext(
				myContextHolder);
		final ExpressionParser parser = new SpelExpressionParser();
		final Expression exp = parser.parseExpression(newText);
		try{
		return exp.getValue(context, String.class);
		}catch(EvaluationException e){
			LOGGER.error("Cannot evaluate {}. Maybe the ContextHolder Map contains values who not implements de.his.hiob.model.question.models.interfaces.Answer",text);
		}
		return null;
	}

	private String convertToValue(final String unconverted,
			final ContextHolder myContextHolder) {
		final Matcher filter = VARPATTERN.matcher(unconverted);
		String converted = escapeCharacters(unconverted);
		boolean found = filter.find();
		while (found) {
			final String match = filter.group();
			final String var = filter.group(1);
			converted = converted.replaceAll(Pattern.quote(match),
			this.createValueExpression(var, myContextHolder.getMapName()));
			found = filter.find();
		}
		return converted;
	}
	
	private String convertToText(final String unconverted, final ContextHolder myContextHolder){
		return convertToText(unconverted, "'", "'", "'+", "+'", myContextHolder);
	}

	private String convertToText(final String unconverted, final String prefix,
			final String suffix, final String termPrefix,
			final String termSuffix, final ContextHolder myContextHolder) {
		final Matcher filter = VARPATTERN.matcher(unconverted);
		String converted = escapeCharacters(unconverted);
		boolean found = filter.find();
		while (found) {
			final String match = filter.group();
			final String var = filter.group(1);
			converted = converted.replaceAll(
					Pattern.quote(match),
					termPrefix
							+ this.createTextExpression(var,
									myContextHolder.getMapName()) + termSuffix);
			found = filter.find();
		}
		return prefix + converted + suffix;
	}

	private Set<String> retrieveVariables(final String unconverted) {
		Set<String> back = new HashSet<String>();
		if (unconverted == null)
			return back;
		final Matcher filter = VARPATTERN.matcher(unconverted);
		boolean found = filter.find();
		while (found) {
			final String var = filter.group(1);
			back.add(var);
			found = filter.find();
		}
		return back;
	}

	private String escapeCharacters(final String original) {

		if (original == null)
			return null;
		String quoted = original;

		final Matcher filter = QUOTEPATTERN.matcher(original);
		boolean found = filter.find();
		while (found) {
			final String text = filter.group(1);
			String quotedText = text.replaceAll("'", "''");
			quoted = quoted.replaceAll(text, quotedText);
			found = filter.find();
		}
		quoted = quoted.replaceAll("'{2,}", "''");

		return quoted;
	}

	/**
	 * Quote String-Expression for Evaluation
	 * @param original
	 * @return quoted original
	 */
	public static String quote(final String original) {
		if (original == null)
			return null;
		String quoted = original;
		quoted = quoted.replaceAll("'", "");
		return quoted;
	}

	@SuppressWarnings("unchecked")
	private Map<String, Object> buildMap(final String expression,
			final EvaluatorContextHolder contextHolder) {
		final Map<String, Object> map = (Map<String, Object>) contextHolder
				.getEvaluatorMap();
		final Set<String> variables = retrieveVariables(expression);
		final Map<String, Object> valueMap = new HashMap<String, Object>();
		final Iterator<String> varIt = variables.iterator();
		while (varIt.hasNext()) {
			final String var = varIt.next();
			final Object value = map.get(var);
			valueMap.put(var, value);
		}
//		LOGGER.info("ContextHolder Map {}", map);
		return valueMap;
	}

	/**
	 * Creates a value expression that binds the input to a map value of the
	 * managed bean to save the values.
	 * 
	 * @param variableName
	 * @return
	 */
	private String createValueExpression(final String variableName,
			final String mapName) {
		return "" + mapName + "['" + variableName + "'].answerValue";
	}

	/**
	 * Creates a text expression that binds the input to a map value of the
	 * managed bean to save the values.
	 * 
	 * @param variableName
	 * @return
	 */
	private String createTextExpression(final String variableName,
			final String mapName) {
		return "" + mapName + "['" + variableName + "'].answerLabel";
	}
}
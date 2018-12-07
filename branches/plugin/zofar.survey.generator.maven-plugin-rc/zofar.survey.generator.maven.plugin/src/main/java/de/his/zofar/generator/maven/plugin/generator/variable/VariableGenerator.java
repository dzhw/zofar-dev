/**
 *
 */
package de.his.zofar.generator.maven.plugin.generator.variable;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.namespace.QName;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.xmlbeans.XmlCursor;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.schema.beans.BeanDocument.Bean;
import org.springframework.schema.beans.BeansDocument;
import org.springframework.schema.beans.BeansDocument.Beans;
import org.springframework.schema.beans.BeansDocument.Beans.DefaultAutowire;
import org.springframework.schema.beans.ConstructorArgDocument.ConstructorArg;
import org.springframework.schema.beans.EntryType;
import org.springframework.schema.beans.PropertyType;

import de.his.zofar.generator.maven.plugin.generator.AbstractGenerator;
import de.his.zofar.generator.maven.plugin.generator.page.PageManager;
import de.his.zofar.xml.questionnaire.MultipleChoiceItemType;
import de.his.zofar.xml.questionnaire.QuestionSingleChoiceAnswerOptionType;
import de.his.zofar.xml.questionnaire.QuestionnaireDocument;
import de.his.zofar.xml.questionnaire.TextResponseOptionType;
import de.his.zofar.xml.questionnaire.VariableType;

/**
 * this class creates the survey-variable-context.xml file which consists of all
 * variables that are used in the survey.
 * 
 * @author le
 * 
 */
public class VariableGenerator extends AbstractGenerator<BeansDocument> {

	private static final Logger LOGGER = LoggerFactory.getLogger(VariableGenerator.class);

	private static final String FILE_NAME = "survey-variable-context.xml";

	private static final String NAMESPACE_DECLARATION = "declare namespace z='http://www.his.de/zofar/xml/questionnaire'";
	private static final String ANSWEROPTION_QUERY = NAMESPACE_DECLARATION + " " + "//z:responseDomain[@variable='%1$s']//z:answerOption" + " | " + "//z:left[@variable='%1$s']//z:answerOption" + " | " + "//z:right[@variable='%1$s']//z:answerOption";
	private static final String ANSWEROPTIONMISSINGS_QUERY = NAMESPACE_DECLARATION + " " + "//z:responseDomain[@variable='%1$s']//z:answerOption[@missing]" + " | " + "//z:left[@variable='%1$s']//z:answerOption[@missing]" + " | "
			+ "//z:right[@variable='%1$s']//z:answerOption[@missing]";
	private static final String BOOLEAN_ANSWEROPTION_QUERY = NAMESPACE_DECLARATION + " " + "//z:multipleChoice//z:answerOption[@variable='%1$s']" + " | " + "//z:matrixMultipleChoice//z:answerOption[@variable='%1$s']";

	private static final String SESSION_CONTROLLER_NAME = "sessionController";
	private static final String SESSION_CONTROLLER_CLASS = "de.his.zofar.presentation.surveyengine.controller.SessionController";
	
	private static final String DEVPROXY_NAME = "devProxy";
	private static final String DEVPROXY_CLASS = "de.his.zofar.presentation.surveyengine.dev.DevProxy";
	
	private static final String NUMBER_VARIABLE_BEAN = "de.his.zofar.presentation.surveyengine.NumberValueTypeBean";
	private static final String BOOLEAN_VARIABLE_BEAN = "de.his.zofar.presentation.surveyengine.BooleanValueTypeBean";
	private static final String STRING_VARIABLE_BEAN = "de.his.zofar.presentation.surveyengine.StringValueTypeBean";
	private static final String SINGLECHOICEANSWEROPTION_BEAN = "de.his.zofar.presentation.surveyengine.SingleChoiceAnswerOptionTypeBean";
	private static final String SINGLECHOICEANSWEROPTION_VALUES_NAME = "optionValues";
	private static final String SINGLECHOICEANSWEROPTION_MISSINGS_NAME = "missingValues";
	private static final String SINGLECHOICEANSWEROPTION_LABELS_NAME = "labels";
	private static final String DEFAULT_VARIABLE_BEAN_SCOPE = "request";
	
	private final Map<String,Bean> variableCache;



	/**
	 * implement singleton pattern.
	 */
	public VariableGenerator() {
		super(BeansDocument.Factory.newInstance());
		variableCache = new HashMap<String,Bean>();
	}

	/**
	 * creates the survey-variable-context.xml with mandatory content.
	 * 
	 * @return the created document
	 */
	public final void createDocument() {
		// adding the root beans tag
		final Beans beans = this.getDocument().addNewBeans();

		// writing the xsi:schemaLocation attribute
		final XmlCursor cursor = this.getDocument().newCursor();
		if (cursor.toFirstChild()) {
			cursor.setAttributeText(new QName("http://www.w3.org/2001/XMLSchema-instance", "schemaLocation"), "http://www.springframework.org/schema/beans " + "http://www.springframework.org/schema/beans/spring-beans-3.2.xsd");
		}
		beans.setDefaultAutowire(DefaultAutowire.BY_NAME);
		
		if(PageManager.getInstance().getMojo().isOverrideRendering()){
			final Bean devProxy = beans.addNewBean();
			devProxy.setName(DEVPROXY_NAME);
			devProxy.setClass1(DEVPROXY_CLASS);
			devProxy.setScope("request");
		}

		// create mandatory session controller instance (session scoped)
		final Bean sessionController = beans.addNewBean();
		sessionController.setName(SESSION_CONTROLLER_NAME);
		sessionController.setClass1(SESSION_CONTROLLER_CLASS);
		sessionController.setScope("session");
	}

	/**
	 * saves the document in the path as survey-variable-context.xml.
	 * 
	 * @param path
	 *            the path in which the document will be saved. file name is
	 *            'survey-variable-context.xml'
	 * @throws IOException
	 */
	public final void saveDocument(final String path) throws IOException {
		// setting some options to customize the file
		final XmlOptions options = new XmlOptions();
		options.setSavePrettyPrint();
		options.setUseDefaultNamespace();

		if (!this.validate()) {
			throw new IllegalStateException("the document ist not a valid XML document. File: " + FILE_NAME);
		}

		this.saveDocument(path, FILE_NAME, options);
	}

	/**
	 * @param variableName
	 */
	public final void addNumberVariable(final VariableType variable) {
		this.addVariableBean(variable, NUMBER_VARIABLE_BEAN, DEFAULT_VARIABLE_BEAN_SCOPE);
	}

	/**
	 * adds a bean definition for a boolean variable.
	 * 
	 * @param variableName
	 *            the name of the boolean variable
	 * @param itemUid
	 *            the UID of the answer option
	 */
	public final void addBooleanVariable(final VariableType variable, final String itemUid) {
		final Bean bean = this.addVariableBean(variable, BOOLEAN_VARIABLE_BEAN, DEFAULT_VARIABLE_BEAN_SCOPE);

		final ConstructorArg arg = bean.addNewConstructorArg();
		arg.setValue2(itemUid);
	}

	/**
	 * adds a bean definition for a string variable.
	 * 
	 * @param variableName
	 *            the name of the string variable
	 */
	public final void addStringVariable(final VariableType variable) {
		this.addVariableBean(variable, STRING_VARIABLE_BEAN, DEFAULT_VARIABLE_BEAN_SCOPE);
	}

	/**
	 * @param variableName
	 */
	public final void addSingleChoiceAnswerOption(final VariableType variable) {
		this.addVariableBean(variable, SINGLECHOICEANSWEROPTION_BEAN, DEFAULT_VARIABLE_BEAN_SCOPE);
	}

	/**
	 * adds single choice answer options for a variable.
	 * 
	 * @param variableName
	 *            the name of the string variable
	 * @param properties
	 */
	public final void addSingleChoiceAnswerOption(final VariableType variable, final PropertyType... properties) {
		this.addVariableBean(variable, SINGLECHOICEANSWEROPTION_BEAN, DEFAULT_VARIABLE_BEAN_SCOPE, properties);
	}

	public final void addSingleChoiceAnswerOption(final VariableType variable, final QuestionnaireDocument source) {
		addSingleChoiceAnswerOption(variable, source, null);
	}

	/**
	 * @param variableName
	 * @param source
	 */
	public final void addSingleChoiceAnswerOption(final VariableType variable, final QuestionnaireDocument source, final Map<String, String> options) {
		if (options != null){
			this.addSingleChoiceAnswerOption(variable, this.createSingleChoiceOptionValues(this.parseValues(variable, options)), this.createSingleChoiceOptionMissings(this.parseMissings(variable.getName(), source)),this.createSingleChoiceOptionLabels(this.parseLabels(variable.getName(), source)));
//			this.addSingleChoiceAnswerOption(variable, this.createSingleChoiceOptionValues(this.parseValues(variable, options)), this.createSingleChoiceOptionMissings(this.parseMissings(variable.getName(), source)));
		}else
			this.addSingleChoiceAnswerOption(variable, this.createSingleChoiceOptionValues(this.parseValues(variable.getName(), source)), this.createSingleChoiceOptionMissings(this.parseMissings(variable.getName(), source)));

	}

	/**
	 * creates the option values map for the single choice answer option bean.
	 * 
	 * @param valuesMap
	 *            a map of all values, which are used for e.g. response
	 *            insertion.
	 * @return the created property type
	 */
	public final PropertyType createSingleChoiceOptionValues(final Map<String, String> valuesMap) {
		return this.createPropertyTypeMap(SINGLECHOICEANSWEROPTION_VALUES_NAME, valuesMap);
	}

	/**
	 * creates the option values map for the single choice answer option bean.
	 * 
	 * @param valuesMap
	 *            a map of all values, which are used for e.g. response
	 *            insertion.
	 * @return the created property type
	 */
	public final PropertyType createSingleChoiceOptionMissings(final Map<String, String> valuesMap) {
		return this.createPropertyTypeMap(SINGLECHOICEANSWEROPTION_MISSINGS_NAME, valuesMap);
	}

	/**
	 * creates the option values map for the single choice answer option bean.
	 * 
	 * @param valuesMap
	 *            a map of all values, which are used for e.g. response
	 *            insertion.
	 * @return the created property type
	 */
	public final PropertyType createSingleChoiceOptionLabels(final Map<String, String> valuesMap) {
		return this.createPropertyTypeMap(SINGLECHOICEANSWEROPTION_LABELS_NAME, valuesMap);
	}

	/**
	 * @param propertyName
	 * @param entries
	 * @return
	 */
	private PropertyType createPropertyTypeMap(final String propertyName, final Map<String, String> entries) {
		final PropertyType property = PropertyType.Factory.newInstance();

		property.setName(propertyName);
		// creating map entries
		final org.springframework.schema.beans.MapDocument.Map map = property.addNewMap();
		for (final Entry<String, String> entry : entries.entrySet()) {
			final String key = entry.getKey();
			final String value = entry.getValue();
			final EntryType entryType = map.addNewEntry();
			entryType.setKey2(key);
			entryType.setValue2(value);
		}

		return property;
	}

	/**
	 * @param variableName
	 * @param className
	 * @param scope
	 * @param propertyArray
	 * @return bean
	 */
	private Bean addVariableBean(final VariableType variable, final String className, final String scope, final PropertyType... propertyArray) {
		if(variableCache.containsKey(variable.getName()))return variableCache.get(variable.getName());
		
		final Bean bean = this.getDocument().getBeans().addNewBean();
		bean.setName(variable.getName());
		bean.setClass1(className);
		bean.setScope(scope);

		// set constructor arguments
		final ConstructorArg sessionController = bean.addNewConstructorArg();
		sessionController.setRef2(SESSION_CONTROLLER_NAME);
		final ConstructorArg value = bean.addNewConstructorArg();
		value.setValue2(variable.getName());

		PropertyType[] properties = propertyArray;

		if (variable.isSetAlternativeText()) {
			final PropertyType property = PropertyType.Factory.newInstance();
			property.setName("alternative");

			final String key = PageManager.getInstance().getMojo().addTextToBundle(variable.getName() + ".alternative", variable.getAlternativeText());

			property.setValue2(key);

			PageManager.getInstance().getMojo().writeLabelInformation(variable.getName() + ".alternative", variable.getName(), "true", key);

			properties = ArrayUtils.add(properties, property);
		}

		// set optional properties
		if (properties != null) {
			bean.setPropertyArray(properties);
		}
		variableCache.put(variable.getName(),bean);
		return bean;
	}

	/**
	 * @param variableName
	 * @param source
	 * @return
	 */
	private Map<String, String> parseValues(final VariableType variable, final Map<String, String> result) {
		return result;
	}

	/**
	 * @param variableName
	 * @param source
	 * @return
	 */
	private Map<String, String> parseValues(final String variableName, final QuestionnaireDocument source) {
		final Map<String, String> values = new LinkedHashMap<String, String>();

		final Object[] result = source.selectPath(String.format(ANSWEROPTION_QUERY, variableName));

		for (final Object child : result) {
			if ((QuestionSingleChoiceAnswerOptionType.class).isAssignableFrom(child.getClass())) {
				final QuestionSingleChoiceAnswerOptionType option = (QuestionSingleChoiceAnswerOptionType) child;

				if (!(option.isSetMissing() && option.getMissing()))
					values.put(option.getUid(), option.getValue());

			} else {
				LOGGER.info("unkown parseValue {}", child);
			}
		}

		return values;
	}

	/**
	 * @param variableName
	 * @param source
	 * @return
	 */
	private Map<String, String> parseMissings(final String variableName, final QuestionnaireDocument source) {
		final Map<String, String> values = new LinkedHashMap<String, String>();

		final Object[] result = source.selectPath(String.format(ANSWEROPTIONMISSINGS_QUERY, variableName));

		for (final Object child : result) {
			if ((QuestionSingleChoiceAnswerOptionType.class).isAssignableFrom(child.getClass())) {

				final QuestionSingleChoiceAnswerOptionType option = (QuestionSingleChoiceAnswerOptionType) child;
				if (option.isSetMissing() && option.getMissing())
					values.put(option.getUid(), option.getValue());
			} else {
				LOGGER.info("unkown parseMissing {}", child);
			}
		}

		return values;
	}

	/**
	 * @param variableName
	 * @param source
	 * @return
	 */
	private Map<String, String> parseLabels(String variableName, final QuestionnaireDocument source) {
		variableName = variableName.replaceAll("dropDown", "");
		variableName = variableName.replaceAll("missing", "");
		
		final Map<String, String> values = new HashMap<String, String>();

		final String query = "declare namespace zofar='http://www.his.de/zofar/xml/questionnaire' //zofar:responseDomain[@variable='"+variableName+"']//zofar:answerOption";
		final XmlObject[] result = source.selectPath(query);

		if(result != null){
			int count = result.length;
			for (int a=0;a<count;a++) {
				final XmlObject child = result[a];

				if ((QuestionSingleChoiceAnswerOptionType.class).isAssignableFrom(child.getClass())) {
					final QuestionSingleChoiceAnswerOptionType tmp = (QuestionSingleChoiceAnswerOptionType)child;
					
					final String uid = tmp.getUid();
					StringBuffer labelBuffer =new StringBuffer();
					
					if (tmp.getLabelArray().length > 0) {

						for (final TextResponseOptionType label : tmp.getLabelArray()) {
							labelBuffer.append(label.newCursor().getTextValue());
						}
					}
					String unfiltered = labelBuffer.toString();
					String filtered = unfiltered.replaceAll("#\\{[^\\}]*\\}", "");
					
					values.put(uid,filtered);
				}

			}

		}
		return values;
	}

	public String findItemUidOfBoolean(final String variableName, final QuestionnaireDocument source) {
		String itemUid = null;

		final Object[] result = source.selectPath(String.format(BOOLEAN_ANSWEROPTION_QUERY, variableName));

		for (final Object child : result) {
			final MultipleChoiceItemType option = (MultipleChoiceItemType) child;

			itemUid = option.getUid();
		}

		return itemUid;
	}

}

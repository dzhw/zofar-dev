/**
 *
 */
package de.his.zofar.generator.maven.plugin.generator.page;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.apache.maven.plugin.logging.Log;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlOptions;

import com.sun.java.jsf.facelets.CompositionDocument;
import com.sun.java.jsf.facelets.CompositionType;
import com.sun.java.jsf.facelets.DefineType;
import com.sun.java.jsf.facelets.ParamType;

import de.his.zofar.generator.maven.plugin.generator.AbstractGenerator;
import de.his.zofar.xml.questionnaire.PageType;
import de.his.zofar.xml.questionnaire.QuestionHeaderType;
import de.his.zofar.xml.questionnaire.QuestionPretestType;
import de.his.zofar.xml.questionnaire.TextQuestionType;
import de.his.zofar.xml.questionnaire.TriggerActionType;
import de.his.zofar.xml.questionnaire.TriggerConsistencyType;
import de.his.zofar.xml.questionnaire.TriggerJSCheckType;
import de.his.zofar.xml.questionnaire.TriggerSessionType;
import de.his.zofar.xml.questionnaire.TriggerVariableType;
import de.his.zofar.xml.questionnaire.TriggersType;
import de.his.zofar.xml.questionnaire.VariableType;
import eu.dzhw.zofar.xml.navigation.JumperContainerType;
import eu.dzhw.zofar.xml.navigation.JumperType;

/**
 * the instances of this class represents a single web page AKA XHTML pages in
 * zofar.
 * 
 * @author le
 * 
 */
public class ZofarWebPage extends AbstractGenerator<CompositionDocument> {

	private static final String FILE_NAME_PATTERN = "%s.xhtml";

	private final String name;
	private final boolean overrideRendering;
	private final boolean noVisibleMap;

	// private final File msgBundleFile;
	// private final Properties msgBundle = new Properties();
	// private Integer messageIndex = 0;

	/**
	 * @param pageName
	 */
	public ZofarWebPage(final String pageName) {
		this(pageName, null, false,true);
	}

	/**
	 * @param pageName
	 * @param progress
	 */
	public ZofarWebPage(final String pageName, final Integer progress, final boolean overrideRendering, final boolean noVisibleMap) {
		super(CompositionDocument.Factory.newInstance(getDocumentOptions()));
		name = pageName;
		this.overrideRendering = overrideRendering;
		this.noVisibleMap = noVisibleMap;
		createDocumentBody(progress);

		PageManager.getInstance().setCurrentPage(this);
	}

	private static XmlOptions getDocumentOptions() {
		final XmlOptions options = new XmlOptions();
		options.setLoadAdditionalNamespaces(Collections.singletonMap("ui", "http://java.sun.com/jsf/facelets"));

		return options;
	}

	private void createDocumentBody(final Integer progress) {
		// set template for this page AKA document
		final CompositionType page = getDocument().addNewComposition();
		page.setTemplate("template/survey.xhtml");

		if (progress != null) {
			final ParamType progressParam = page.addNewParam();
			progressParam.setName(ParamType.Name.PROGRESS);
			progressParam.setValue(String.valueOf(progress));
		}

		// define the page content
		final DefineType pageContent = page.addNewDefine();
		pageContent.setName("template-content");

		// add the root section to the page AKA the zofar page itself.
		pageContent.addNewSection().setId(name);
		// final com.sun.java.jsf.composite.common.PageType pagetype =
		// pageContent
		// .addNewPage();
		// pagetype.setId(name);
		// pagetype.setUid(name);

		// define the page footer
		if (this.overrideRendering && !this.noVisibleMap) {
			final DefineType pageFooter = page.addNewDefine();
			pageFooter.setName("template-footer");

			pageFooter.addNewDevRenderCondition();
		}
	}

	/**
	 * adds all contents of the page to the XHTML page.
	 * 
	 * @param xmlPage
	 * @throws XmlException
	 */
	public final void addPageNavigation(final PageType xmlPage, final Log log) throws XmlException {
		final CompositionType page = getDocument().getComposition();
		JumperContainerType xmlSide = xmlPage.getSide();

		// JumperContainerType[] xmlSide = xmlPage.getSideArray();

		if (xmlSide != null) {
			final DefineType sideNav = page.addNewDefine();
			sideNav.setName("template-left-navigation");
			com.sun.java.jsf.composite.common.JumperContainerType jumperContainer = sideNav.addNewJumperContainer();
			jumperContainer.setSeparator("side");
			// for (final JumperContainerType container : xmlSide) {
			for (final JumperType xmlJumper : xmlSide.getJumperArray()) {
				// log.info("add to sideNavi "+xmlJumper);
				com.sun.java.jsf.composite.common.JumperType jumper = jumperContainer.addNewJumper();
				jumper.setValue(xmlJumper.getValue());
				jumper.setTargetPage(xmlJumper.getTarget());
				if (jumper.isSetDisabled())
					jumper.setDisabled(xmlJumper.getDisabled());
				jumper.setLevel(xmlJumper.getLevel());
				if (jumper.isSetVisible())
					jumper.setVisible(xmlJumper.getVisible());
			}
			// }
		}
		// JumperContainerType[] xmlTop = xmlPage.getBreadcrumbArray();
		JumperContainerType xmlTop = xmlPage.getBreadcrumb();

		if (xmlTop != null) {
			final DefineType topNav = page.addNewDefine();
			topNav.setName("template-top-navigation");
			com.sun.java.jsf.composite.common.JumperContainerType jumperContainer = topNav.addNewJumperContainer();
			jumperContainer.setSeparator("top");
			// for (final JumperContainerType container : xmlTop) {
			for (final JumperType xmlJumper : xmlTop.getJumperArray()) {
				com.sun.java.jsf.composite.common.JumperType jumper = jumperContainer.addNewJumper();
				jumper.setValue(xmlJumper.getValue());
				jumper.setTargetPage(xmlJumper.getTarget());
				if (jumper.isSetDisabled())
					jumper.setDisabled(xmlJumper.getDisabled());
				if (jumper.isSetLevel())
					jumper.setLevel(xmlJumper.getLevel());
				if (jumper.isSetVisible())
					jumper.setVisible(xmlJumper.getVisible());
			}
			// }
		}
	}

	public final void addPretestComments(final PageType xmlPage, final Log log) throws XmlException {
		final CompositionType page = getDocument().getComposition();

		final String uid = xmlPage.getUid();

		final VariableType variable = VariableType.Factory.newInstance();
		variable.setName("comment" + uid);
		variable.setType(VariableType.Type.STRING);

		final QuestionPretestType comment = QuestionPretestType.Factory.newInstance();
		final QuestionHeaderType header = comment.addNewHeader();

		final TextQuestionType text = header.addNewQuestion();
		text.setUid("text" + variable.getName());
		text.newCursor().setTextValue("Kommentarfeld");
		comment.setType("text");
		comment.setSmallOption(false);
		comment.setUid("question" + variable.getName());
		comment.setVariable(variable.getName());
		comment.setRows(new BigInteger(3 + ""));
		comment.setColumns(new BigInteger(100 + ""));
		comment.setMaxLength(new BigInteger(2000 + ""));
		comment.setValidationMessage("Der Inhalt dieses Feldes ist auf 2000 Zeichen limitiert.");

		final IXhtmlCreator creator = CreatorFactory.newCreator(comment);
		creator.addToSection(comment, page.getDefine().getSection(), true);
		PageManager.getInstance().getAdditionalVariables().add(variable);
	}

	/**
	 * adds all contents of the page to the XHTML page.
	 * 
	 * @param xmlPage
	 * @throws XmlException
	 */
	public final void addPageContentRecursively(final PageType xmlPage) throws XmlException {
		final CompositionType page = getDocument().getComposition();

		// a page without transitions is an end page
		// if (xmlPage.getTransitionsArray() == null ||
		// xmlPage.getTransitionsArray().length < 1) {
		if (xmlPage.getTransitions() == null || xmlPage.getTransitions().getTransitionArray().length < 1) {
			final ParamType lastPageParam = page.addNewParam();
			lastPageParam.setName(ParamType.Name.LAST_PAGE);
			lastPageParam.setValue("true");
		}

		addTriggers(xmlPage, page.getDefine());

		final IXhtmlCreator creator = CreatorFactory.newCreator(xmlPage);
		creator.addToSection(xmlPage, page.getDefine().getSection(), true);
		// creator.addToSection(xmlPage, page.getDefine().getPage());
	}

	private void addTriggers(final PageType source, final DefineType target) {
		if (source.getTriggers() != null) {
			final TriggersType triggers = source.getTriggers();
			final TriggerCreator triggerCreator = new TriggerCreator();
			// for (final TriggersType triggers : triggersArray) {
			for (final TriggerConsistencyType trigger : triggers.getConsistencyArray()) {
				triggerCreator.addTriggerConsistency(trigger, target);
			}
			for (final TriggerSessionType trigger : triggers.getSessionArray()) {
				triggerCreator.addTriggerSession(trigger, target);
			}
			for (final TriggerVariableType trigger : triggers.getVariableArray()) {
				triggerCreator.addTriggerVariable(trigger, target);
			}
			for (final TriggerActionType trigger : triggers.getActionArray()) {
				triggerCreator.addTriggerAction(trigger, target);
			}
			for (final TriggerJSCheckType trigger : triggers.getJsCheckArray()) {
				triggerCreator.addTriggerJS(trigger, target);
			}
			// }
		}
	}

	/**
	 * saves the document in the path as ${pagename}.xhtml.
	 * 
	 * @param path
	 *            the path in which the page will be saved.
	 * @throws IOException
	 */
	public final void save(final String path) throws IOException {
		// setting some options to customize the file
		final XmlOptions options = new XmlOptions();
		options.setSavePrettyPrint();
		options.setUseDefaultNamespace();
		options.setSaveAggressiveNamespaces();

		final Map<String, String> suggestedPrefixes = new HashMap<String, String>();
		suggestedPrefixes.put("http://java.sun.com/jsf/facelets", "ui");
		suggestedPrefixes.put("http://java.sun.com/jsf/core", "f");
		suggestedPrefixes.put("http://java.sun.com/jsf/html", "h");
		suggestedPrefixes.put("http://java.sun.com/jsf/composite/text", "text");
		suggestedPrefixes.put("http://java.sun.com/jsf/composite/answer", "answer");
		suggestedPrefixes.put("http://java.sun.com/jsf/composite/common", "common");
		suggestedPrefixes.put("http://java.sun.com/jsf/composite/matrix", "matrix");
		suggestedPrefixes.put("http://java.sun.com/jsf/composite/trigger", "trigger");
		suggestedPrefixes.put("http://java.sun.com/jsf/composite/question", "question");
		suggestedPrefixes.put("http://java.sun.com/jsf/composite/composite", "composite");
		suggestedPrefixes.put("http://java.sun.com/jsf/composite/container", "container");
		suggestedPrefixes.put("http://java.sun.com/jsf/composite/display", "display");
		suggestedPrefixes.put("http://www.w3.org/1999/xhtml", "");
		options.setSaveSuggestedPrefixes(suggestedPrefixes);

		saveDocument(path, String.format(FILE_NAME_PATTERN, name), options);
	}

	/**
	 * @param pathToFile
	 * @return
	 */
	public final Boolean isPageExist(final String pathToFile) {
		final File file = new File(pathToFile + File.separator + String.format(FILE_NAME_PATTERN, name));
		return file.exists() && file.isFile();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public final String toString() {
		return getDocument().toString();
	}

}

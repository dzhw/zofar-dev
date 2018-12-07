/**
 *
 */
package de.his.zofar.generator.maven.plugin.generator.page;

import org.apache.xmlbeans.XmlException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.java.jsf.composite.answer.LargeOpenOptionType;
import com.sun.java.jsf.composite.answer.OpenGradeOptionType;
import com.sun.java.jsf.composite.answer.OpenMailOptionType;
import com.sun.java.jsf.composite.answer.OpenNumberOptionType;
import com.sun.java.jsf.composite.answer.OpenResponseDomainType;
import com.sun.java.jsf.composite.answer.SmallOpenOptionType;
import com.sun.java.jsf.composite.common.SortType;
import com.sun.java.jsf.composite.container.SectionType;
import com.sun.java.jsf.composite.question.OpenType;
import com.sun.java.jsf.composite.question.PretestType;
import com.sun.java.jsf.core.FacetType;

import de.his.zofar.xml.questionnaire.IdentificationalType;
import de.his.zofar.xml.questionnaire.QuestionOpenType;
import de.his.zofar.xml.questionnaire.QuestionPretestType;
import de.his.zofar.xml.questionnaire.TextResponseOptionType;

/**
 * creator for open questions.
 * 
 * @author le
 * 
 */
class XhtmlQuestionPretestCreator extends AbstractXhtmlQuestionCreator implements
		IXhtmlCreator {

	protected final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
	private static final String DEFAULT_RESPONSE_DOMAIN_UID = "responsedomain";
	private static final String DEFAULT_RESPONSE_OPTION_UID = "response";

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.his.zofar.generator.maven.plugin.generator.page.IXhtmlCreator#addToSection
	 * (de.his.zofar.xml.questionnaire.IdentificationalType,
	 * com.sun.java.jsf.composite.container.SectionType)
	 */
	@Override
	public void addToSection(final IdentificationalType source,
			final SectionType targetSection,final boolean root) throws XmlException {
		createElement(source, targetSection.addNewPretest());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.his.zofar.generator.maven.plugin.generator.page.IXhtmlCreator#addToSort
	 * (de.his.zofar.xml.questionnaire.IdentificationalType,
	 * com.sun.java.jsf.composite.common.SortType)
	 */
	@Override
	public void addToSort(final IdentificationalType source,
			final SortType target) throws XmlException {
		createElement(source, target.addNewPretest());
	}

	private void createElement(final IdentificationalType source,
			final PretestType target) throws XmlException {
		final QuestionPretestType sourceQuestion = (QuestionPretestType) source;
		setIdentifier(source, target);
		
        if (sourceQuestion.isSetVisible()) {
            target.setRendered(createElExpression(sourceQuestion
                    .getVisible()));
        }

		addQuestionHeader(sourceQuestion.getHeader(), target);

		final String variableReference = createVariableReference(sourceQuestion
				.getVariable());

		final OpenResponseDomainType responseDomain = target
				.addNewOpenResponseDomain();
		// have to set a constant id because in the QML we get only one id for
		// the whole open question
		responseDomain.setId(DEFAULT_RESPONSE_DOMAIN_UID);

//		OpenOptionType openOption = null;

		if (sourceQuestion.getType().equals("text")) {
			if (sourceQuestion.getSmallOption()) {
				SmallOpenOptionType openOption = responseDomain.addNewSmallOpenOption();
				if (sourceQuestion.isSetMaxLength()) {
					openOption
							.setMaxlength(sourceQuestion.getMaxLength()
									.intValue());
				}
				
		    	if(sourceQuestion.isSetValidationMessage()){
					String message = null;
					if (PageManager.getInstance().getMojo() != null) {
						message = PageManager
								.getInstance()
								.getMojo()
								.addTextToBundle(
										generateUid(sourceQuestion),
										sourceQuestion.getValidationMessage());
					}
					openOption.setValidationMessage(this.createElExpression(message));
		    	}
				
				if (sourceQuestion.isSetSize()) {
					openOption.setSize(sourceQuestion
							.getSize().intValue());
				}
				openOption.setVar(variableReference);
				// see comment at setting the id of the responsedomain
				openOption.setId(DEFAULT_RESPONSE_OPTION_UID);

				if (sourceQuestion.isSetPrefix()) {
					final FacetType prefix = openOption.addNewFacet();
					prefix.setName(FacetType.Name.PREFIX);
					for (final TextResponseOptionType text : sourceQuestion.getPrefix()
							.getLabelArray()) {
						addTextToFacet(text, prefix);
					}
				}

				if (sourceQuestion.isSetPostfix()) {
					final FacetType postfix = openOption.addNewFacet();
					postfix.setName(FacetType.Name.POSTFIX);
					for (final TextResponseOptionType text : sourceQuestion
							.getPostfix().getLabelArray()) {
						addTextToFacet(text, postfix);
					}
				}
			} else {
				LargeOpenOptionType openOption = responseDomain.addNewLargeOpenOption();
				if (sourceQuestion.isSetRows()) {
					openOption.setRows(sourceQuestion
							.getRows().intValue());
				}
				
				if (sourceQuestion.isSetMaxLength()) {
					openOption
							.setMaxlength(sourceQuestion.getMaxLength()
									.intValue());
				}
				
		    	if(sourceQuestion.isSetValidationMessage()){
					String message = null;
					if (PageManager.getInstance().getMojo() != null) {
						message = PageManager
								.getInstance()
								.getMojo()
								.addTextToBundle(
										generateUid(sourceQuestion),
										sourceQuestion.getValidationMessage());
					}
					openOption.setValidationMessage(this.createElExpression(message));
		    	}

				if (sourceQuestion.isSetColumns()) {
					openOption
							.setColumns(sourceQuestion.getColumns().intValue());
				}
				openOption.setVar(variableReference);
				// see comment at setting the id of the responsedomain
				openOption.setId(DEFAULT_RESPONSE_OPTION_UID);

				if (sourceQuestion.isSetPrefix()) {
					final FacetType prefix = openOption.addNewFacet();
					prefix.setName(FacetType.Name.PREFIX);
					for (final TextResponseOptionType text : sourceQuestion.getPrefix()
							.getLabelArray()) {
						addTextToFacet(text, prefix);
					}
				}

				if (sourceQuestion.isSetPostfix()) {
					final FacetType postfix = openOption.addNewFacet();
					postfix.setName(FacetType.Name.POSTFIX);
					for (final TextResponseOptionType text : sourceQuestion
							.getPostfix().getLabelArray()) {
						addTextToFacet(text, postfix);
					}
				}
			}
		} else if (sourceQuestion.getType().equals("number")) {
			OpenNumberOptionType openOption = responseDomain.addNewOpenNumberOption();
			if (sourceQuestion.isSetMinValue())
				openOption.setMinValue(sourceQuestion.getMinValue());
			if (sourceQuestion.isSetMaxValue())
				openOption.setMaxValue(sourceQuestion.getMaxValue());
			if (sourceQuestion.isSetValidationMessage()) {
				String message = null;
				if (PageManager.getInstance().getMojo() != null) {
					message = PageManager
							.getInstance()
							.getMojo()
							.addTextToBundle(generateUid(sourceQuestion),
									sourceQuestion.getValidationMessage());
				}
				((OpenNumberOptionType) openOption).setValidationMessage(this
						.createElExpression(message));
			}
			openOption.setVar(variableReference);
			// see comment at setting the id of the responsedomain
			openOption.setId(DEFAULT_RESPONSE_OPTION_UID);

			if (sourceQuestion.isSetPrefix()) {
				final FacetType prefix = openOption.addNewFacet();
				prefix.setName(FacetType.Name.PREFIX);
				for (final TextResponseOptionType text : sourceQuestion.getPrefix()
						.getLabelArray()) {
					addTextToFacet(text, prefix);
				}
			}

			if (sourceQuestion.isSetPostfix()) {
				final FacetType postfix = openOption.addNewFacet();
				postfix.setName(FacetType.Name.POSTFIX);
				for (final TextResponseOptionType text : sourceQuestion
						.getPostfix().getLabelArray()) {
					addTextToFacet(text, postfix);
				}
			}
		} else if (sourceQuestion.getType().equals("grade")) {
			OpenGradeOptionType openOption = responseDomain.addNewOpenGradeOption();
			if (sourceQuestion.isSetMinValue())
				openOption.setMinValue(sourceQuestion.getMinValue());
			if (sourceQuestion.isSetMaxValue())
				openOption.setMaxValue(sourceQuestion.getMaxValue());
	    	if(sourceQuestion.isSetValidationMessage()){
				String message = null;
				if (PageManager.getInstance().getMojo() != null) {
					message = PageManager
							.getInstance()
							.getMojo()
							.addTextToBundle(
									generateUid(sourceQuestion),
									sourceQuestion.getValidationMessage());
				}
				openOption.setValidationMessage(this.createElExpression(message));
	    	}
			openOption.setVar(variableReference);
			// see comment at setting the id of the responsedomain
			openOption.setId(DEFAULT_RESPONSE_OPTION_UID);

			if (sourceQuestion.isSetPrefix()) {
				final FacetType prefix = openOption.addNewFacet();
				prefix.setName(FacetType.Name.PREFIX);
				for (final TextResponseOptionType text : sourceQuestion.getPrefix()
						.getLabelArray()) {
					addTextToFacet(text, prefix);
				}
			}

			if (sourceQuestion.isSetPostfix()) {
				final FacetType postfix = openOption.addNewFacet();
				postfix.setName(FacetType.Name.POSTFIX);
				for (final TextResponseOptionType text : sourceQuestion
						.getPostfix().getLabelArray()) {
					addTextToFacet(text, postfix);
				}
			}
		}
		else if (sourceQuestion.getType().equals("mail")) {
			OpenMailOptionType openOption = responseDomain.addNewOpenMailOption();
			
			if (sourceQuestion.isSetMaxLength()) {
				openOption
						.setMaxlength(sourceQuestion.getMaxLength()
								.intValue());
			}
			
	    	if(sourceQuestion.isSetValidationMessage()){
				String message = null;
				if (PageManager.getInstance().getMojo() != null) {
					message = PageManager
							.getInstance()
							.getMojo()
							.addTextToBundle(
									generateUid(sourceQuestion),
									sourceQuestion.getValidationMessage());
				}
				openOption.setValidationMessage(this.createElExpression(message));
	    	}
			openOption.setVar(variableReference);
			// see comment at setting the id of the responsedomain
			openOption.setId(DEFAULT_RESPONSE_OPTION_UID);

			if (sourceQuestion.isSetPrefix()) {
				final FacetType prefix = openOption.addNewFacet();
				prefix.setName(FacetType.Name.PREFIX);
				for (final TextResponseOptionType text : sourceQuestion.getPrefix()
						.getLabelArray()) {
					addTextToFacet(text, prefix);
				}
			}

			if (sourceQuestion.isSetPostfix()) {
				final FacetType postfix = openOption.addNewFacet();
				postfix.setName(FacetType.Name.POSTFIX);
				for (final TextResponseOptionType text : sourceQuestion
						.getPostfix().getLabelArray()) {
					addTextToFacet(text, postfix);
				}
			}
		}
		else {
			LOGGER.warn("Type of open field is not known (text,number,grade,mail). Handeled as text");
			if (sourceQuestion.getSmallOption()) {
				SmallOpenOptionType openOption = responseDomain.addNewSmallOpenOption();
				if (sourceQuestion.isSetMaxLength()) {
					openOption
							.setMaxlength(sourceQuestion.getMaxLength()
									.intValue());
				}
		    	if(sourceQuestion.isSetValidationMessage()){
					String message = null;
					if (PageManager.getInstance().getMojo() != null) {
						message = PageManager
								.getInstance()
								.getMojo()
								.addTextToBundle(
										generateUid(sourceQuestion),
										sourceQuestion.getValidationMessage());
					}
					openOption.setValidationMessage(this.createElExpression(message));
		    	}
				if (sourceQuestion.isSetSize()) {
					openOption.setSize(sourceQuestion
							.getSize().intValue());
				}
				openOption.setVar(variableReference);
				// see comment at setting the id of the responsedomain
				openOption.setId(DEFAULT_RESPONSE_OPTION_UID);

				if (sourceQuestion.isSetPrefix()) {
					final FacetType prefix = openOption.addNewFacet();
					prefix.setName(FacetType.Name.PREFIX);
					for (final TextResponseOptionType text : sourceQuestion.getPrefix()
							.getLabelArray()) {
						addTextToFacet(text, prefix);
					}
				}

				if (sourceQuestion.isSetPostfix()) {
					final FacetType postfix = openOption.addNewFacet();
					postfix.setName(FacetType.Name.POSTFIX);
					for (final TextResponseOptionType text : sourceQuestion
							.getPostfix().getLabelArray()) {
						addTextToFacet(text, postfix);
					}
				}
			} else {
				LargeOpenOptionType openOption = responseDomain.addNewLargeOpenOption();
				
				if (sourceQuestion.isSetMaxLength()) {
					openOption
							.setMaxlength(sourceQuestion.getMaxLength()
									.intValue());
				}
		    	if(sourceQuestion.isSetValidationMessage()){
					String message = null;
					if (PageManager.getInstance().getMojo() != null) {
						message = PageManager
								.getInstance()
								.getMojo()
								.addTextToBundle(
										generateUid(sourceQuestion),
										sourceQuestion.getValidationMessage());
					}
					openOption.setValidationMessage(this.createElExpression(message));
		    	}
				if (sourceQuestion.isSetRows()) {
					openOption.setRows(sourceQuestion
							.getRows().intValue());
				}

				if (sourceQuestion.isSetColumns()) {
					openOption
							.setColumns(sourceQuestion.getColumns().intValue());
				}
				openOption.setVar(variableReference);
				// see comment at setting the id of the responsedomain
				openOption.setId(DEFAULT_RESPONSE_OPTION_UID);

				if (sourceQuestion.isSetPrefix()) {
					final FacetType prefix = openOption.addNewFacet();
					prefix.setName(FacetType.Name.PREFIX);
					for (final TextResponseOptionType text : sourceQuestion.getPrefix()
							.getLabelArray()) {
						addTextToFacet(text, prefix);
					}
				}

				if (sourceQuestion.isSetPostfix()) {
					final FacetType postfix = openOption.addNewFacet();
					postfix.setName(FacetType.Name.POSTFIX);
					for (final TextResponseOptionType text : sourceQuestion
							.getPostfix().getLabelArray()) {
						addTextToFacet(text, postfix);
					}
				}
			}
		}



		   	
		if (sourceQuestion.isSetVisible()) {
			target.setRendered(createElExpression(sourceQuestion.getVisible()));
		}
	}
}

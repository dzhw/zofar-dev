/**
 *
 */
package de.his.zofar.generator.maven.plugin.generator.page;

import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.java.jsf.composite.common.SortType;
import com.sun.java.jsf.composite.container.SectionType;
import com.sun.java.jsf.core.FacetType;

import de.his.zofar.xml.questionnaire.IdentificationalType;
import de.his.zofar.xml.questionnaire.SectionBodyType;
import de.his.zofar.xml.questionnaire.SectionHeaderType;
import de.his.zofar.xml.questionnaire.TextInstructionType;
import de.his.zofar.xml.questionnaire.TextIntroductionType;
import de.his.zofar.xml.questionnaire.TextParagraphType;
import de.his.zofar.xml.questionnaire.TextTitleType;

/**
 * creates the section of the XHTML pages with all its contents.
 *
 * @author le
 *
 */
class XhtmlSectionCreator extends AbstractXhtmlElementCreator implements
        IXhtmlCreator {
	
	private static final Logger LOGGER = LoggerFactory
			.getLogger(XhtmlSectionCreator.class);

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
        createElement(source, targetSection,root);
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
        // this.logger.info("trying to add {} to a sort container", source);

        if (de.his.zofar.xml.questionnaire.SectionType.class
                .isAssignableFrom(source.getClass())) {
            final de.his.zofar.xml.questionnaire.SectionType sourceSection = (de.his.zofar.xml.questionnaire.SectionType) source;

            final SectionType targetSection = target.addNewSection();
            setIdentifier(source, targetSection);
            
    		if (sourceSection.isSetVisible()) {
    			targetSection.setRendered(createElExpression(sourceSection.getVisible()));
    		}

            if (sourceSection.isSetHeader()) {
                addSectionHeader(sourceSection.getHeader(), targetSection);
            }

            if (sourceSection.isSetBody()) {
                addSectionBody(sourceSection.getBody(), targetSection);
            }
        } else {
            final IXhtmlCreator creator = CreatorFactory.newCreator(source);
            creator.addToSort(source, target);
        }
    }

    private void createElement(final IdentificationalType source,
            final SectionType target,final boolean root) throws XmlException {
        final de.his.zofar.xml.questionnaire.SectionType sourceSection = (de.his.zofar.xml.questionnaire.SectionType) source;
    	SectionType toSection = target;
    	if(root){

    	}
    	else{
    		toSection = target.addNewSection();
    	}

        setIdentifier(source, toSection);
        
		if (sourceSection.isSetVisible()) {
			toSection.setRendered(createElExpression(sourceSection.getVisible()));
		}

        if (sourceSection.isSetHeader()) {
            addSectionHeader(sourceSection.getHeader(), toSection);
        }

        if (sourceSection.isSetBody()) {
            addSectionBody(sourceSection.getBody(), toSection);
        }	
    }

    /**
     * @param source
     * @param targetSection
     * @throws XmlException
     */
    private void addSectionBody(final SectionBodyType source,
            final SectionType targetSection) throws XmlException {
        // TODO le: if sorting exist create a <common:sort /> tag

        if (source.isSetSortCondition() || source.isSetSortMode()) {
            final SortType sort = createSortContainer(source,
                    targetSection.addNewSort());

            for (final XmlObject element : source.selectPath(SELECT_PATH_IN_ORDER)) {
            	if((IdentificationalType.class).isAssignableFrom(element.getClass())){
                    final IXhtmlCreator creator = CreatorFactory.newCreator((IdentificationalType) element);
                    if(creator != null)creator.addToSort((IdentificationalType) element, sort);
                    else LOGGER.info("no creator found for {}",element);
            	}
            	else{
            		LOGGER.info("unkown sorted element {}",element.getClass());
            	}

            }
        } else {
            for (final XmlObject element : source.selectPath(SELECT_PATH_IN_ORDER)) {
            	if((IdentificationalType.class).isAssignableFrom(element.getClass())){
                    final IXhtmlCreator creator = CreatorFactory.newCreator((IdentificationalType) element);
                    if(creator != null)creator.addToSection((IdentificationalType) element,targetSection,false);
                    else LOGGER.info("no creator found for {}",element);
            	}
            	else{
            		LOGGER.info("unkown unsorted element {}",element.getClass());
            	}
            }
        }
    }

    /**
     * adds the section header with all its content.
     *
     * @param source
     * @param targetSection
     * @throws XmlException
     */
    private void addSectionHeader(final SectionHeaderType source,
            final SectionType targetSection) throws XmlException {
        final FacetType headerFacet = targetSection.addNewFacet();
        headerFacet.setName(FacetType.Name.HEADER);

        for (final XmlObject text : source.selectPath(SELECT_PATH_IN_ORDER)) {
            addText((de.his.zofar.xml.questionnaire.TextType) text, headerFacet);
        }
    }

    /**
     * adds text to the header facet of the section.
     *
     * @param source
     * @param targetFacet
     * @throws XmlException
     */
    private void addText(final de.his.zofar.xml.questionnaire.TextType source,
            final FacetType targetFacet) throws XmlException {
        final Class<?> textClass = source.getClass();
        if (!TextIntroductionType.class.isAssignableFrom(textClass)
                && !TextInstructionType.class.isAssignableFrom(textClass)
                && !TextTitleType.class.isAssignableFrom(textClass)
                && !TextParagraphType.class.isAssignableFrom(textClass)) {
            throw new RuntimeException(String.format(
                    "cannot add text of type %s to text header", source
                            .getClass().getSimpleName()));
        }

        addTextToFacet(source, targetFacet);
    }

}

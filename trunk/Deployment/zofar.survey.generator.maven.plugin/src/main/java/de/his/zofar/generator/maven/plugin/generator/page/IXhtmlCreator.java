/**
 *
 */
package de.his.zofar.generator.maven.plugin.generator.page;

import org.apache.xmlbeans.XmlException;

import com.sun.java.jsf.composite.common.SortType;
import com.sun.java.jsf.composite.container.SectionType;

import de.his.zofar.xml.questionnaire.IdentificationalType;

/**
 * @author le
 *
 */
interface IXhtmlCreator {

    /**
     * adds the element to the section, more precisely to the body of the
     * section.
     *
     * @param source
     * @param target
     * @throws XmlException
     */
    void addToSection(IdentificationalType source, SectionType target,final boolean root)
            throws XmlException;

    /**
     * @param source
     * @param target
     * @throws XmlException
     */
    void addToSort(IdentificationalType source, SortType target)
            throws XmlException;

}

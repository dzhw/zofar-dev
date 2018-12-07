/**
 *
 */
package de.his.zofar.generator.maven.plugin.test.converter.transition;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.java.xml.ns.javaee.FacesConfigNavigationCaseType;

import de.his.zofar.generator.maven.plugin.generator.transition.TransitionGenerator;

/**
 * usage demonstrator of the TransitionGenerator.
 *
 * @author le
 *
 */
public class TransitionGeneratorTest {

    private static final Logger LOGGER = LoggerFactory
            .getLogger(TransitionGeneratorTest.class);

    /**
     * demonstrates how to generate the faces-config.xml in which zofar survey
     * describes and defines its transitions.
     */
    @Test
    @Ignore
    public void testDemonstrator() {
        final TransitionGenerator generator = new TransitionGenerator();

        generator.createDocument();

        // create one navigation case with one rule
        final FacesConfigNavigationCaseType case1 = generator
                .createNavigationCase(null, "page1");
        generator.addNavigationRule("index", case1);

        // create one navigation case with two rules
        final FacesConfigNavigationCaseType case2 = generator
                .createNavigationCase("v1.value==1", "page3");
        final FacesConfigNavigationCaseType case3 = generator
                .createNavigationCase("v1.value==2", "page4");
        generator.addNavigationRule("page2", case2, case3);

        LOGGER.info("\n{}", generator.getDocument());

        Assert.assertTrue(generator.validate());
    }

}

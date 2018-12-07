/**
 *
 */
package de.his.zofar.generator.maven.plugin.mojo;

import java.io.File;
import java.io.IOException;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;

import com.sun.java.xml.ns.javaee.FacesConfigNavigationCaseType;

import de.his.zofar.generator.maven.plugin.generator.transition.TransitionGenerator;
import de.his.zofar.xml.questionnaire.PageType;
import de.his.zofar.xml.questionnaire.QuestionnaireDocument;
import de.his.zofar.xml.questionnaire.TransitionType;
import de.his.zofar.xml.questionnaire.TransitionsType;

/**
 * Generates all transitions in the faces-config.xml.
 *
 * @author le
 *
 */
public class TransitionsGeneratorMojo {

    private final TransitionGenerator generator = new TransitionGenerator();

    private final QuestionnaireDocument questionnaire;

    private final File outputDirectory;
    
    private final boolean linearNavigation;

    public TransitionsGeneratorMojo(final QuestionnaireDocument questionnaire,
            final File outputDirectory,final boolean linearNavigation) {
        this.questionnaire = questionnaire;
        this.outputDirectory = outputDirectory;
        this.linearNavigation = linearNavigation;
    }

    public final void execute() throws MojoExecutionException,
            MojoFailureException {

        PageType[] pages = null;
        if (this.questionnaire != null) {
            pages = this.questionnaire.getQuestionnaire().getPageArray();
        }

        // generate faces-config.xml with mandatory content
        this.generator.createDocument();

        if (pages != null) {
            // create a navigation rule for every transition
			int pageLft = 0;
			final int pagesCount = pages.length;
            for (final PageType page : pages) {
            	if(this.linearNavigation){
					if(pageLft < (pagesCount-1)){
						final FacesConfigNavigationCaseType[] cases = new FacesConfigNavigationCaseType[1];
						cases[0] = this.generator.createNavigationCase(null, pages[pageLft + 1].getUid());
						this.generator.addNavigationRule(page.getUid(), cases);
					}
					pageLft++;
            	}
            	else{
//                    final TransitionsType[] transitionsArray = page.getTransitionsArray();
                    final TransitionsType transitionsArray = page.getTransitions();
                    //for(final TransitionsType transitions:transitionsArray){
                        if (transitionsArray != null&& transitionsArray.getTransitionArray() != null) {
                            final FacesConfigNavigationCaseType[] cases = new FacesConfigNavigationCaseType[transitionsArray
                                    .getTransitionArray().length];
                            int i = 0;
                            for (final TransitionType transition : transitionsArray
                                    .getTransitionArray()) {
                                cases[i++] = this.generator.createNavigationCase(
                                        transition.getCondition(),
                                        transition.getTarget());
                            }
                            this.generator.addNavigationRule(page.getUid(), cases);
                        }
                    //}
               	}
            }
        }

        this.save();
    }

    /**
     * @throws MojoFailureException
     */
    public void save() throws MojoFailureException {
        // saving file to project path
        try {
            this.generator.saveDocument(this.outputDirectory.getAbsolutePath());
        } catch (final IOException e) {
            throw new MojoFailureException(
                    "could not save faces-config.xml to the file system.");
        }
    }

}

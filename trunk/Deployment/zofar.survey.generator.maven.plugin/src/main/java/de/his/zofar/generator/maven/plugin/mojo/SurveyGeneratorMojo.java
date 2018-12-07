/**
 *
 */
package de.his.zofar.generator.maven.plugin.mojo;

import java.io.File;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

import de.his.zofar.generator.maven.plugin.generator.page.PageManager;
import de.his.zofar.generator.maven.plugin.reader.QuestionnaireReader;
import de.his.zofar.xml.questionnaire.QuestionnaireDocument;

/**
 * @author le
 * 
 */
@Mojo(name = "generate-survey")
public class SurveyGeneratorMojo extends AbstractMojo {

	private final QuestionnaireReader reader = QuestionnaireReader.getInstance();

	private QuestionnaireDocument questionnaire;

	@Parameter(defaultValue = "${project}", required = true)
	private MavenProject project;

	@Parameter(defaultValue = "${basedir}/src/main/resources/questionnaire.xml", required = true)
	private File xml;

	@Parameter(defaultValue = "${basedir}/src/main/resources/survey/preload.csv", required = true)
	private File preloads;

	@Parameter(defaultValue = "${basedir}/src/main/resources/survey", required = true)
	private File surveyResource;

	@Parameter(defaultValue = "${basedir}/src/main/webapp", required = true)
	private File webapp;

	@Parameter(defaultValue = "${basedir}/src/main/resources/de/his/zofar/messages", required = true)
	private File resourceBundle;

	@Parameter(defaultValue = "${basedir}/src/main/webapp/WEB-INF", required = true)
	private File webinf;

	@Parameter(defaultValue = "${basedir}/src/main/resources", required = true)
	private File resources;

	@Parameter(defaultValue = "false", required = true)
	private boolean pretest;

	@Parameter(defaultValue = "false", required = true)
	private boolean linearNavigation;

	@Parameter(defaultValue = "false", required = true)
	private boolean overrideRendering;

	@Parameter(defaultValue = "false", required = true)
	private boolean mdm;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.maven.plugin.Mojo#execute()
	 */
	@Override
	public void execute() throws MojoExecutionException, MojoFailureException {
		readQuestionnaireDocument();

		final PagesGeneratorMojo generator = new PagesGeneratorMojo(questionnaire, getLog(), webapp, resourceBundle,
				surveyResource, pretest, overrideRendering);

		PageManager.getInstance().setMojo(generator);

		generatePreloads();

		generatePages();

		generateVariables();

		generateTransitions();

		generateCSS();
		
		if(mdm)generateMDM();
	}

	private void generateMDM() throws MojoExecutionException, MojoFailureException {
		getLog().info("Generating mdm data");

		final MDMGeneratorMojo generator = new MDMGeneratorMojo(questionnaire, surveyResource);

		generator.execute();
	}

	private void generateVariables() throws MojoExecutionException, MojoFailureException {
		getLog().info("Generating variables");

		final VariablesGeneratorMojo generator = new VariablesGeneratorMojo(questionnaire, surveyResource);

		generator.execute();
	}

	private void generatePages() throws MojoExecutionException, MojoFailureException {
		getLog().info("Generating zofar pages AKA composite components.");

		final PagesGeneratorMojo generator = PageManager.getInstance().getMojo();

		generator.execute();
	}

	private void generatePreloads() throws MojoExecutionException, MojoFailureException {
		getLog().info("Generating Preloads.");

		final PreloadGeneratorMojo generator = new PreloadGeneratorMojo(questionnaire, preloads, surveyResource,
				getLog());
		generator.execute();
	}

	private void generateCSS() throws MojoExecutionException, MojoFailureException {
		getLog().info("Generating general CSS.");

		final CssGeneratorMojo generator = new CssGeneratorMojo(webapp);

		generator.execute();
	}

	/**
	 *
	 */
	private void generateTransitions() throws MojoExecutionException, MojoFailureException {
		getLog().info("Generating zofar transitions and the faces-config.xml.");

		final TransitionsGeneratorMojo generator = new TransitionsGeneratorMojo(questionnaire, webinf,
				linearNavigation);

		generator.execute();
	}

	/**
	 * @return
	 */
	private void readQuestionnaireDocument() {
		try {
			// first try to read the questionnaire xml
			questionnaire = reader.readDocument(xml, true);
		} catch (final IllegalArgumentException iae) {
			getLog().warn("No questionnaire XML file! Creating basic survey.");
		}
	}

}

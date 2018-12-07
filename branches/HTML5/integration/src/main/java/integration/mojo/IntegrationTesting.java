package integration.mojo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.dzhw.zofar.management.utils.reflection.ReflectionClient;
import integration.tests.AbstracTestBase;
import integration.tests.AllTests;
import junit.framework.TestSuite;

/**
 * @author meisner
 * 
 */
@Mojo(name = "integration")
public class IntegrationTesting extends AbstractMojo {

	@Parameter(defaultValue = "unkown", required = true)
	private String url;

	@Parameter(defaultValue = "unkown", required = true)
	private String name;

	@Parameter(defaultValue = "integration1", required = true)
	private String token;

	@Parameter(defaultValue = "${project}", required = true)
	private MavenProject project;

	@Parameter(defaultValue = "${debugMode}", required = true)
	private boolean debugMode;

	final static Logger LOGGER = LoggerFactory.getLogger(IntegrationTesting.class);

	public IntegrationTesting() {
		super();
	}

	public void execute() throws MojoExecutionException, MojoFailureException {
		this.getLog().info("start integration testing");

		final Map<String, String> additionalArgs = new HashMap<String, String>();
		additionalArgs.put("url", url);
		additionalArgs.put("name", name);
		additionalArgs.put("token", token);
		if (project != null) {
			additionalArgs.put("project.name", project.getName());
			additionalArgs.put("project.version", project.getVersion());
			additionalArgs.put("project.path", project.getFile().getParent());

			for (final Entry<Object, Object> property : project.getProperties().entrySet()) {
				additionalArgs.put("project.property." + property.getKey(), property.getValue() + "");
			}
		}

		if (!additionalArgs.isEmpty()) {
			final Iterator<String> it = additionalArgs.keySet().iterator();
			while (it.hasNext()) {
				final String key = it.next();
				final String value = additionalArgs.get(key);
				System.setProperty(key, value);
			}
		}

		final JUnitCore runner = new JUnitCore();
		LOGGER.info("search for Tests...");
		final List<Class<? extends AbstracTestBase>> tests = new ArrayList<Class<? extends AbstracTestBase>>();
		try {
			final Package rootPackage = AllTests.class.getPackage();
			final Class<AbstracTestBase> parentClass = AbstracTestBase.class;

			tests.addAll(ReflectionClient.getInstance().getClassesByParentClass(rootPackage, parentClass));
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		if (tests.isEmpty()) {
			throw new MojoExecutionException("no tests found");
		}

		LOGGER.info("found tests {}", tests.toString());

		final TestSuite allTests = new AllTests();
		for (Class<? extends AbstracTestBase> test : tests) {
			if (test.isAnnotationPresent(org.junit.Ignore.class))
				continue;
			allTests.addTestSuite(test);
		}

		System.out.println("debugMode : " + debugMode);

		if (debugMode) {
			System.out.println("Debug Mode. Skipping tests ");
		} else {

			AllTests.setUpClass();
			final Result result = runner.run(allTests);
			AllTests.tearDownClass();
			LOGGER.info("...done");
			List<Failure> failures = new ArrayList<Failure>();
			for (final Failure failure : result.getFailures()) {
				// MessageProvider.info(failure.getDescription().getTestClass(),
				// "!! {}", failure.getMessage());
				this.getLog().error(failure.getException());
				failures.add(failure);
			}
			if (!additionalArgs.isEmpty()) {
				final Iterator<String> it = additionalArgs.keySet().iterator();
				while (it.hasNext()) {
					final String key = it.next();
					System.clearProperty(key);
				}
			}

			if (!failures.isEmpty()) {
				LOGGER.error("throw " + failures.toString());
				throw new MojoFailureException(failures.toString());
			}
		}
	}

}

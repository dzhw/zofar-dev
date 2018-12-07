package de.his.zofar.generator.maven.plugin.mojo;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.io.IOUtils;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CssGeneratorMojo {

	protected final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
	private File webapp;


	public CssGeneratorMojo(File webapp) {
		super();
		this.webapp = webapp;
	}

	/**
	 * @throws MojoExecutionException
	 * @throws MojoFailureException
	 */
	public final void execute() throws MojoExecutionException,
			MojoFailureException {
		final InputStream in = getClass().getResourceAsStream("/META-INF/resources/css/general_template.css");
		File resources = new File(webapp.getAbsolutePath() + "/resources/");
		if (!resources.exists())	resources.mkdir();
		File css = new File(resources.getAbsolutePath() + "/css/");
		if (!css.exists())	css.mkdir();
		File target = new File(css.getAbsolutePath() + "/general_template.css");
		if (target.exists())
			LOGGER.info("css File {} already exists. skip creation",
					target.getPath());
		else {
			LOGGER.info("copy content from original general.css to {}",
					target.getAbsolutePath());
			try {
				OutputStream out = new FileOutputStream(target, false);
				try {
					IOUtils.copy(in, out);
				} catch (IOException e) {
					LOGGER.error("failed to copy css file", e);
				} finally {
					IOUtils.closeQuietly(in);
					IOUtils.closeQuietly(out);
				}
			} catch (FileNotFoundException e) {
				LOGGER.error("failed to create css file", e);
			}
		}
		createImagesDir();
	}
	private void createImagesDir() {
		final InputStream in = getClass().getResourceAsStream("/META-INF/resources/images/dzhw.jpg");
		File resources = new File(webapp.getAbsolutePath() + "/resources/");
		if (!resources.exists())	resources.mkdir();
		File images = new File(resources.getAbsolutePath() + "/images/");
		if (!images.exists())	images.mkdir();
		File target = new File(images.getAbsolutePath() + "/dzhw.jpg");
		if (target.exists())
			LOGGER.info("images File {} already exists. skip creation",
					target.getPath());
		else {
			LOGGER.info("copy content from original logo.jpg to {}",
					target.getAbsolutePath());
			try {
				OutputStream out = new FileOutputStream(target, false);
				try {
					IOUtils.copy(in, out);
				} catch (IOException e) {
					LOGGER.error("failed to copy jpg file", e);
				} finally {
					IOUtils.closeQuietly(in);
					IOUtils.closeQuietly(out);
				}
			} catch (FileNotFoundException e) {
				LOGGER.error("failed to create jpg file", e);
			}
		}
	}

}

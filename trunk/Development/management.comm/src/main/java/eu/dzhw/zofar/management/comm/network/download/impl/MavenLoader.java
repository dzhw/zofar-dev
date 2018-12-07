/*
 * Maven Downloader
 */
package eu.dzhw.zofar.management.comm.network.download.impl;

import java.io.File;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.dzhw.zofar.management.comm.network.download.Loader;

/**
 * The Class MavenLoader.
 */
public class MavenLoader implements Loader {
	
	/** The Constant LOGGER. */
	final static Logger LOGGER = LoggerFactory.getLogger(MavenLoader.class);
	
	/** The loader. */
	private final HttpLoader loader;

	/**
	 * Instantiates a new maven loader.
	 */
	public MavenLoader() {
		super();
		loader = new HttpLoader();
	}

	/**
	 * Load Content from target.
	 *
	 * @param target the target
	 * @return the content as File
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public File getContent(final String target) throws IOException {
		return loader.getContent(target,true);
	}

}

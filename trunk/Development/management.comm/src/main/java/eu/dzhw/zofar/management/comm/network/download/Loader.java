/*
 * Interface for Downloader
 */
package eu.dzhw.zofar.management.comm.network.download;

import java.io.File;
import java.io.IOException;

/**
 * The Interface Loader.
 */
public interface Loader {
	
	/**
	 * Gets the content.
	 *
	 * @param target the target
	 * @return the content
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public File getContent(final String target) throws IOException;
}

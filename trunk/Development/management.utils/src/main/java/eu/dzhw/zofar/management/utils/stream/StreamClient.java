/*
 * Class to handle Streams
 */
package eu.dzhw.zofar.management.utils.stream;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.SequenceInputStream;
import java.util.Arrays;
import java.util.Collections;

import org.apache.commons.io.output.TeeOutputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Class StreamClient.
 */
public class StreamClient {
	/** The Constant INSTANCE. */
	private static final StreamClient INSTANCE = new StreamClient();

	/** The Constant LOGGER. */
	private final static Logger LOGGER = LoggerFactory.getLogger(StreamClient.class);

	/**
	 * Instantiates a new string utils.
	 */
	private StreamClient() {
		super();
	}

	/**
	 * Gets the single instance of StringUtils.
	 * 
	 * @return single instance of StringUtils
	 */
	public static synchronized StreamClient getInstance() {
		return StreamClient.INSTANCE;
	}
	
	/**
	 * Link Output streams.
	 *
	 * @param stream1 the stream1
	 * @param stream2 the stream2
	 * @return the tee output stream
	 */
	public OutputStream linkStreams(final OutputStream stream1, final OutputStream stream2) {
		final OutputStream back = new TeeOutputStream(stream1, stream2);
		return back;
	}
	
	/**
	 * Link Input streams.
	 *
	 * @param streams the streams
	 * @return the input stream
	 */
	public InputStream linkStreams(final InputStream... streams) {
		final InputStream back = new SequenceInputStream( Collections.enumeration(Arrays.asList(streams)));
		return back;
	}	
}
